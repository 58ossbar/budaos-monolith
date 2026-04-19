package com.budaos.modules.im.utils;

import com.alibaba.fastjson.JSONObject;
import com.budaos.modules.im.core.entity.MsgType;
import com.budaos.utils.tool.StrUtils;

/**
 * 通知、提示小时
 * @author huj
 *
 */
public class CBImTipsUtils {

	/**
	 * 提示
	 * @param title
	 * @param content
	 * @param busitype 非必传，默认为alert
	 * @return
	 */
	public static JSONObject alert(String title, String content, String busitype) {
		return doAlert("cbid", title, content, null, busitype);
	}
	
	/**
	 * 系统通知
	 * @param title 弹窗顶部标题
	 * @param content 弹窗主体内容
	 * @return {"busitype": "system", "msg": {"title": "", "content": ""}}
	 */
	public static JSONObject confirm(String title, String content) {
		return doConfirm("cbid", title, content, null);
	}
	
	/**
	 * 
	 * @param id 自定义id
	 * @param title 弹窗顶部标题
	 * @param content 弹窗主体内容
	 * @return {"busitype": "system", "msg": {"title": "", "content": ""}}
	 */
	public static JSONObject confirm(String id, String title, String content) {
		return doConfirm(id, title, content, null);
	}
	
	/**
	 * 
	 * @param id 自定义id
	 * @param title 弹窗顶部标题
	 * @param content 弹窗主体内容
	 * @param pic 图片
	 * @return {"busitype": "system", "msg": {"title": "", "content": ""}}
	 */
	public static JSONObject confirm(String id, String title, String content, String pic) {
		return doConfirm(id, title, content, pic);
	}
	
	/**
	 * 
	 * @param id 自定义id
	 * @param title 弹窗顶部标题
	 * @param content 弹窗主体内容
	 * @param pic 图片
	 * @return {"busitype": "system", "msg": {"title": "", "content": ""}}
	 */
	private static JSONObject doConfirm(String id, String title, String content, String pic) {
		JSONObject msg = new JSONObject();
		msg.put("busitype", MsgType.SYSTEM);
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("title", title);
		busiMsg.put("content", content);
		msg.put("msg", busiMsg);
		return msg;
	}
	
	private static JSONObject doAlert(String id, String title, String content, String pic, String busitype) {
		JSONObject msg = new JSONObject();
		msg.put("busitype", StrUtils.isEmpty(busitype) ? MsgType.ALERT : busitype);
		JSONObject busiMsg = new JSONObject();
		busiMsg.put("title", title);
		busiMsg.put("content", content);
		msg.put("msg", busiMsg);
		return msg;
	}
	
}
