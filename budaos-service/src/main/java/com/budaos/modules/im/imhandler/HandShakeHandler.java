package com.budaos.modules.im.imhandler;

import com.budaos.modules.im.core.annotation.CBImAfterHandshake;
import com.budaos.modules.im.core.annotation.CBImService;
import com.budaos.modules.im.core.config.Const;
import com.budaos.modules.im.domain.TimUserinfo;
import com.budaos.modules.im.service.TimGroupUserService;
import com.budaos.modules.im.service.TimUserinfoService;
import com.budaos.modules.im.service.TimUsertokenService;
import com.budaos.utils.tool.EnvUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;

import java.util.List;

/**
 * 建立连接处理类(握手)
 * @author zhuq
 *
 */
@CBImService
@Service
public class HandShakeHandler {

	private static Logger log = LoggerFactory.getLogger(HandShakeHandler.class);
	@Autowired
	private TimGroupUserService groupUserService;
	@Autowired
	private TimUserinfoService TimUserinfoService;
	@Autowired
	private TimUsertokenService timUsertokenService;
	@Autowired
	private EnvUtils envUtils;
	
	/**
	 * websocket握手处理类，即建立长连接(类似于新用户注册，老用户登陆操作)
	 * @param request
	 * @param response
	 * @param channelContext
	 */
	@CBImAfterHandshake
	public void handler(HttpRequest request, HttpResponse response, ChannelContext channelContext) {
		String userid = request.getParam(Const.USER_ID);//对应timuserinfo表中的主键userid
		String channel = request.getParam(Const.USER_CHANNEL);//握手请求来源渠道
		String token = request.getParam(Const.USER_TOKEN);//握手请求授权码
		log.debug("当前服务环境：" + envUtils.getEnv());
		//非生产环境开启测试参数
		if(!envUtils.isProd() && StrUtils.isEmpty(userid)) {
			userid = request.getParam("id");
		}
		//生产环境开启同渠道来源踢下线处理开始
		/*
		if(envUtils.isProd()) {
			SetWithLock<ChannelContext> channelContextsByUserid = Tio.getByUserid(channelContext.tioConfig, userid);
			if(channelContextsByUserid != null) {
				for(ChannelContext context : channelContextsByUserid.getObj()) {
					//如果存在该渠道的链接，则中断原来的通道，即T下线
					if(channel.equals(context.get(Const.CHANNEL_INFO_CHANNELNAME))) {
						Tio.unbindUser(context.tioConfig, userid);
						Tio.close(context, "被踢下线了哦");
					}
				}
			}
		}
		*/
		//生产环境开启同渠道来源踢下线处理开结束
		//握手成功后，将建立连接的用户信息存储在通道中，方便后续消息交互中使用
		TimUserinfo userInfo = TimUserinfoService.selectObjectById(userid);
		channelContext.set(Const.CHANNEL_INFO_USERINFO, userInfo);
		channelContext.set(Const.CHANNEL_INFO_CHANNELNAME, channel);
		channelContext.setToken(token);
		//将授权码入库
		timUsertokenService.initToken(userid, channel, token);
		log.debug("建立链接：" + userid);
		Tio.bindUser(channelContext, userid);
		// 获取当前用户的群聊并循环绑定到群组
		List<String> list = groupUserService.listGroupIds(userid);
		log.debug("["+userid+"]"+"群组数量：" + list.size());
		if (list != null && list.size() > 0) {
			for (String groupId : list) {
				Tio.bindGroup(channelContext, groupId);
			}
		}	
	}
}
