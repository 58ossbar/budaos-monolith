package com.budaos.modules.im.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.im.core.annotation.*;
import com.budaos.modules.im.core.config.Const;
import com.budaos.modules.im.core.entity.*;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.utils.redis.RedisUtils;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.SpringContextUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.tio.common.starter.annotation.TioServerMsgHandler;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuq
 * @date 2020年8月6日 消息处理类
 */

@TioServerMsgHandler
public class CBWsMsgHandler implements IWsMsgHandler, ApplicationListener<ContextRefreshedEvent>{

	private static Logger log = LoggerFactory.getLogger(CBWsMsgHandler.class);
	
	@Autowired
	private CBMqHandler cBMqHandler;
	
	//自定义websocket消息处理映射集
	private static Map<String, Object[]> handleDatas = new HashMap<>();
	private static Map<String, Object[]> consumerDatas = new HashMap<>();
	private static String handshakeKey = "handshake#on";
	private static String afterhandshakeKey = "handshake#after";
	@Value("${com.budaos.mq-type:}")
	private String mqType;
	@Value("${com.budaos.redisFlag:false}")
	private boolean redisFlag;
	@Value("${spring.application.name:}")
	private String appName;
	@Autowired
	private RedisUtils redisUtils;
	
	/**
	 * redis消息订阅监听器
	 * @param connectionFactory
	 * @return
	 */
	@Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
		//如果该项目不是使用的redis消息队列，则不设置
		if(!"redis".equals(mqType)) {
			return null;
		}
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
		ApplicationContext context = SpringContextUtils.getApplicationContext();
		//获取所有标记了消息注解的bean，根据topic添加监听
		Map<String, Object> beans = context.getBeansWithAnnotation(RedisListener.class);
		for(Object b : beans.values()){
			String topic = b.getClass().getAnnotation(RedisListener.class).topic();
	        container.addMessageListener((MessageListener)b, new ChannelTopic(topic));
		}
        return container;
    }
	/**
	 * 后台映射集中的每一个映射key的构造
	 * @param baseUrl
	 * @param url
	 * @param msgType
	 * @return
	 */
	private String initKey(String baseUrl, String url, String msgType) {
		if(!url.startsWith("/")) {
			url = "/" + url;
		}
		if(url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		if(StrUtils.isEmpty(baseUrl)) {
			return url + "#" + msgType;
		}
		if(!baseUrl.startsWith("/")) {
			baseUrl = "/" + baseUrl;
		}
		if(baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		}
		return baseUrl + url + "#" + msgType;
	}
	/**
	 * 前端请求的映射url构造
	 * @param baseUrl
	 * @param msgType
	 * @return
	 */
	private String initBusiType(String busiType, String msgType) {
		if(!busiType.startsWith("/")) {
			busiType = "/" + busiType;
		}
		if(busiType.endsWith("/")) {
			busiType = busiType.substring(0, busiType.length() - 1);
		}
		
		return busiType + "#" + msgType;
	}
	/**
	 * 所有bean都加载完成后，进行注解扫描，初始化websocket处理映射集
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		//获取所有标记了消息注解的bean
		Map<String, Object> beans = context.getBeansWithAnnotation(CBImService.class);
		for(Object b : beans.values()){
			String baseUrl = b.getClass().getAnnotation(CBImService.class).value();
			//获取bean中的所有方法
            Method[] methods = b.getClass().getDeclaredMethods();
            for(Method m : methods) {
            	//如果该方法标识了是消息处理的注解，则加入到消息处理映射集
            	CBImHandler handler = m.getDeclaredAnnotation(CBImHandler.class);
            	if(handler != null) {
            		String value = handler.value();//业务类型,websocket数据包中的busitype字段
            		if(StrUtils.isEmpty(value)) {
            			continue;
            		}
            		String msgType = handler.msgType();//消息类型,websocket数据包中的msgtype字段
            		String key = initKey(baseUrl, value, msgType);
            		if(handleDatas.containsKey(key)) {
            			log.error("重复handler映射：" + b.getClass().getName() + "#" + m.getName());
            		}else {
            			Object[] obj = new Object[2];
            			obj[0] = b;
            			obj[1] = m;
            			handleDatas.put(key, obj);
            		}
            	}
            	
            	//如果该方法标识了是消息处理的注解，则加入到消息处理映射集
            	CBImConsumer conHandler = m.getDeclaredAnnotation(CBImConsumer.class);
            	if(conHandler != null) {
            		String value = conHandler.value();//业务类型,websocket数据包中的busitype字段
            		if(StrUtils.isEmpty(value)) {
            			continue;
            		}
            		String msgType = conHandler.msgType();//消息类型,websocket数据包中的msgtype字段
            		String key = initKey(baseUrl, value, msgType);
            		if(consumerDatas.containsKey(key)) {
            			log.error("重复consumer映射：" + b.getClass().getName() + "#" + m.getName());
            		}else {
            			Object[] obj = new Object[2];
            			obj[0] = b;
            			obj[1] = m;
            			consumerDatas.put(key, obj);
            		}
            	}
            	
            	//握手处理
            	CBImHandshake handshake = m.getDeclaredAnnotation(CBImHandshake.class);
            	if(handshake != null) {
            		if(handleDatas.containsKey(handshakeKey)) {
            			log.error("重复handshake映射：" + b.getClass().getName() + "#" + m.getName());
            		}else {
            			Object[] obj = new Object[2];
            			obj[0] = b;
            			obj[1] = m;
            			handleDatas.put(handshakeKey, obj);
            		}
            	}
            	//握手后处理
            	CBImAfterHandshake afterandshake = m.getDeclaredAnnotation(CBImAfterHandshake.class);
            	if(afterandshake != null) {
            		if(handleDatas.containsKey(afterhandshakeKey)) {
            			log.error("重复afterhandshake映射：" + b.getClass().getName() + "#" + m.getName());
            		}else {
            			Object[] obj = new Object[2];
            			obj[0] = b;
            			obj[1] = m;
            			handleDatas.put(afterhandshakeKey, obj);
            		}
            	}
            }
        }
        log.debug("handler映射集：" + handleDatas.keySet());
        log.debug("consumer映射集：" + consumerDatas.keySet());
	}
	
	/**
	 * 握手时走这个方法，业务可以在这里获取cookie，request参数等
	 */
	@Override
	public HttpResponse handshake(HttpRequest request, HttpResponse response, ChannelContext channelContext)
			throws Exception {
		if(handleDatas.containsKey(handshakeKey)) {
			Object[] objects = handleDatas.get(handshakeKey);
			return (HttpResponse)((Method)objects[1]).invoke(objects[0], request, response, channelContext);
		}
		return response;
	}

	/**
	 * @param httpRequest
	 * @param httpResponse
	 * @param channelContext
	 * @throws Exception
	 * @author zhuq
	 */
	@Override
	public void onAfterHandshaked(HttpRequest request, HttpResponse response, ChannelContext channelContext)
			throws Exception {
		if(handleDatas.containsKey(afterhandshakeKey)) {
			Object[] objects = handleDatas.get(afterhandshakeKey);
			((Method)objects[1]).invoke(objects[0], request, response, channelContext);
		}
		if(redisFlag && StrUtils.isNotEmpty(channelContext.userid)) {
	        String key = appName + ":" + Const.REDIS_HKEY_ONLINE_USER;
	        redisUtils.setCacheHash(key, channelContext.userid, channelContext.getId());
	        log.debug("用户【" + channelContext.userid + "】已上线");
        }
	}

	/**
	 * 字节消息（binaryType = arraybuffer）过来后会走这个方法，一般文件上传时使用这种方式
	 */
	@Override
	public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		return null;
	}

	/**
	 * 当客户端发close flag时，会走这个方法
	 */
	@Override
	public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		Tio.remove(channelContext, "receive close flag");
		return null;
	}

	/**
	 * 字符消息（binaryType = blob）过来后会走这个方法
	 * text: {
	 * 	"busitype": "",//业务类型
	 * 	"msgtype": "",//内容类型
	 * 	"msg": ""//内容
	 * }
	 */
	@Override
	public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
		//心跳
		if ("1".equals(text) || "心跳内容".equals(text)) {
			return null;
		}
		JSONObject receiveMsg = JSONObject.parseObject(text);
		String busiType = receiveMsg.getString(Const.BUSI_TYPE);
		String msgType = receiveMsg.getString(Const.MSG_TYPE);
		String key = initBusiType(busiType, msgType);
		if(!handleDatas.containsKey(key)) {
			CBImUtils.sendToUser(channelContext, channelContext.userid, R.error(404, "无效的请求"));
			log.error("404无效的请求:" + text);
			return null;
		}
		JSONObject message = receiveMsg.getJSONObject(Const.BUSI_MSG);
		message.put("fromuser", channelContext.userid);
		message.put("channel", StrUtils.isNull(channelContext.get("channel")) ? ChannelType.MGR : channelContext.get("channel"));
		message.put("msgtime", DateUtils.getNowTimeStamp());
		message.put("msgtype", msgType);
		Object[] objects = handleDatas.get(key);
		((Method)objects[1]).invoke(objects[0], wsRequest, new JSONObject(receiveMsg), channelContext);
		//将消息推送到消息队列
		receiveMsg.put(Const.BUSI_MSG, message);
		//如果该请求没有消费者，则直接返回，不压入消息队列
		if(!consumerDatas.containsKey(key)) {
			log.debug("暂无消费者的消息：" + receiveMsg.toJSONString());
			return null;
		}
		if(StrUtils.isNotEmpty(mqType)) {
			cBMqHandler.sendMsgToMq(Const.TOPIC_CHAT_MSG, receiveMsg.toJSONString());
		}else {
			handlerMsg(receiveMsg);
		}
		// 返回值是要发送给客户端的内容，一般都是返回null
		return null;
	}
	
	/**
	 * 从消息队列中订阅数据并处理
	 * @param message 格式为： 
	 * {
	 * 	"busitype": "",//请求类型 Const.BUSI_TYPE
	 *  "msgtype": "",//消息类型 Const.MSG_TYPE
	 *  "msg": {}//消息内容 Const.BUSI_MSG
	 * }
	 */
	public void handlerMsg(JSONObject message) throws Exception{
		String busiType = message.getString(Const.BUSI_TYPE); // 请求消息业务类型
		String msgType = message.getString(Const.MSG_TYPE); // 请求消息业务类型
		JSONObject msg = message.getJSONObject(Const.BUSI_MSG);
		String key = initBusiType(busiType, msgType);
		if(!consumerDatas.containsKey(key)) {
			log.debug("暂无处理方法的消息：" + message.toJSONString());
			return;
		}
		Object[] objects = consumerDatas.get(key);
		try {
			if(MsgType.TEXT.equals(msgType)) {
				((Method)objects[1]).invoke(objects[0], JSON.toJavaObject(msg, TextMessage.class));
			}else if(MsgType.IMAGE.equals(msgType)) {
				((Method)objects[1]).invoke(objects[0], JSON.toJavaObject(msg, ImageMessage.class));
			}else if(MsgType.FILE.equals(msgType)) {
				((Method)objects[1]).invoke(objects[0], JSON.toJavaObject(msg, FileMessage.class));
			}else if(MsgType.NEWS.equals(msgType)) {
				((Method)objects[1]).invoke(objects[0], JSON.toJavaObject(msg, NewsMessage.class));
			}else if(MsgType.VIDEO.equals(msgType)) {
				((Method)objects[1]).invoke(objects[0], JSON.toJavaObject(msg, VideoMessage.class));
			}else if(MsgType.VOICE.equals(msgType)) {
				((Method)objects[1]).invoke(objects[0], JSON.toJavaObject(msg, VoiceMessage.class));
			}else {
				((Method)objects[1]).invoke(objects[0], message);
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("消息类型转换异常：" + e.getMessage());
		}
	}
}
