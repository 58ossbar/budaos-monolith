package com.budaos.modules.im.core.entity;

/**
 * 前端请求的消息业务类型
 * @author zhuq
 *
 */
public class MsgType {
	public static final String TEXT = "text";//文本
	public static final String IMAGE = "image";//图片
	public static final String FILE = "file";//文件
	public static final String VOICE = "voice";//语音
	public static final String VIDEO = "video";//视频
	public static final String NEWS = "news";//图文消息
	public static final String OTHER = "other";//其他消息
	
	public static final String SYSTEM = "system"; // 系统公告消息
	public static final String ALERT = "alert"; // 系统提示消息
	public static final String ACTIVITY = "activity"; // 当为此消息类型，需弹出弹窗
	
}
