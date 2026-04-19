package com.budaos.modules.im.core.config;

/**
 * @author 李俊杰
 * @date 2020年5月27日
 */
public class Const {
    
	/**
	 * tio在线用户redis hkey
	 */
	public static final String REDIS_HKEY_ONLINE_USER = "online:user";
	
	public static final String TOPIC_CHAT_MSG = "cbim:chatmsg";
	
    /**
     * 消息编码格式
     */
    public static final String CHARSET = "utf-8";
    
    /**
     * 请求消息的业务类型
     */
    public static final String BUSI_TYPE = "busitype";
    
    /**
     * 请求消息的返回数据
     */
    public static final String DATA = "data";
    
    /**
     * 握手类型
     */
    public static final String HAND_TYPE = "handtype";

    /**
     * 握手外部用户id
     */
    public static final String ID = "id";
    /**
     * 握手内部用户id
     */
    public static final String USER_ID = "userid";
    
    /**
     * 握手来源渠道
     */
    public static final String USER_CHANNEL = "channel";
    
    /**
     * 握手用户token
     */
    public static final String USER_TOKEN = "token";
    
    /**
     * 请求消息业务内容类型(文本，图片，视频等)
     */
    public static final String MSG_TYPE = "msgtype";
    
    /**
     * 请求消息业务内容
     */
    public static final String BUSI_MSG = "msg";
    
    /**
     * 握手用户类型
     */
    public static final String USER_TYPE_SYS = "sys";
    
    /**
     * 握手用户类型
     */
    public static final String USER_TYPE_SITE = "site";
    
    public static final String CHANNEL_INFO_USERINFO = "userInfo";
    
    public static final String CHANNEL_INFO_CHANNELNAME = "channelName";
    
    /**
     * 握手用户类型
     */
    public static final String USER_TYPE_ANNO = "anno";
    
    public static final String REDIS_KEY_PRIVATEMSGNO = "cbim:privatemsgno";
}
