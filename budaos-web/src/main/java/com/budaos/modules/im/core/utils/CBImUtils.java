package com.budaos.modules.im.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.im.core.config.Const;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.websocket.common.WsResponse;

/**
 * 自己封装的工具类
 * @author zhuq
 *
 */
public class CBImUtils {

	
	/**
	 * 发送信息给目标用户
	 * @param tioConfig
	 * @param userid
	 * @param msg
	 */
	public static boolean sendToUser(ChannelContext channelContext, String userid, R msg) {
		return Tio.sendToUser(channelContext.tioConfig, userid, WsResponse.fromText(new JSONObject(msg).toString(), Const.CHARSET));
	}
	/**
	 * 发送信息给目标用户
	 * @param tioConfig
	 * @param userid
	 * @param msg
	 */
	public static boolean sendToUser(TioConfig tioConfig, String userid, R msg) {
		return Tio.sendToUser(tioConfig, userid, WsResponse.fromText(new JSONObject(msg).toString(), Const.CHARSET));
	}
	/**
	 * 发送信息给目标用户
	 * @param tioConfig
	 * @param userid
	 * @param msg
	 */
	public static boolean sendToUser(ChannelContext channelContext, String userid, Object msg) {
		return Tio.sendToUser(channelContext.tioConfig, userid, WsResponse.fromText(new JSONObject(R.ok().put(Const.DATA, msg)).toString(), Const.CHARSET));
	}
	/**
	 * 发送信息给目标用户
	 * @param tioConfig
	 * @param userid
	 * @param msg
	 */
	public static boolean sendToUser(TioConfig tioConfig, String userid, Object msg) {
		return Tio.sendToUser(tioConfig, userid, WsResponse.fromText(new JSONObject(R.ok().put(Const.DATA, msg)).toString(), Const.CHARSET));
	}
	/**
	 * 发送信息给自己用户
	 * @param tioConfig
	 * @param userid
	 * @param msg
	 */
	public static boolean sendToSelf(ChannelContext channelContext, R msg) {
		return Tio.sendToUser(channelContext.tioConfig, channelContext.userid, WsResponse.fromText(new JSONObject(msg).toString(), Const.CHARSET));
	}
	
	/**
	 * 发送信息给自己用户
	 * @param tioConfig
	 * @param userid
	 * @param msg
	 */
	public static boolean sendToSelf(ChannelContext channelContext, Object msg) {
		return Tio.sendToUser(channelContext.tioConfig, channelContext.userid, WsResponse.fromText(new JSONObject(R.ok().put(Const.DATA, msg)).toString(), Const.CHARSET));
	}
	
	/**
	 * 发送消息给目标群组
	 * @param tioConfig
	 * @param groupid
	 * @param msg
	 */
	public static void sendToGroup(ChannelContext channelContext, String groupid, R msg) {
		Tio.sendToGroup(channelContext.tioConfig, groupid, WsResponse.fromText(new JSONObject(msg).toString(), Const.CHARSET));
	}
	
	/**
	 * 发送消息给目标群组
	 * @param tioConfig
	 * @param groupid
	 * @param msg
	 */
	public static void sendToGroup(TioConfig tioConfig, String groupid, R msg) {
		Tio.sendToGroup(tioConfig, groupid, WsResponse.fromText(new JSONObject(msg).toString(), Const.CHARSET));
	}
	/**
	 * 发送消息给目标群组
	 * @param tioConfig
	 * @param groupid
	 * @param msg
	 */
	public static void sendToGroup(ChannelContext channelContext, String groupid, Object msg) {
		Tio.sendToGroup(channelContext.tioConfig, groupid, WsResponse.fromText(new JSONObject(R.ok().put(Const.DATA, msg)).toString(), Const.CHARSET));
	}
	/**
	 * 发送消息给目标群组
	 * @param tioConfig
	 * @param groupid
	 * @param msg
	 */
	public static void sendToGroup(TioConfig tioConfig, String groupid, Object msg) {
		Tio.sendToGroup(tioConfig, groupid, WsResponse.fromText(new JSONObject(R.ok().put(Const.DATA, msg)).toString(), Const.CHARSET));
	}
	
	/**
	 * 将json字符串转换成java对象
	 * @param <T>
	 * @param msg
	 * @param cls
	 * @return
	 */
	public static <T> T convertMsg(String msg, Class<T> cls) {
		return JSONObject.parseObject(msg, cls);
	}
	
	/**
	 * 将json对象转换成java对象
	 * @param <T>
	 * @param msg
	 * @param cls
	 * @return
	 */
	public static <T> T convertMsg(JSONObject msg, Class<T> cls) {
		return JSON.toJavaObject(msg, cls);
	}
	
}
