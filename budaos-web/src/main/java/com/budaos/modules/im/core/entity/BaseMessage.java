package com.budaos.modules.im.core.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息bean(字段命名保持跟微信api一致，方便对接)
 * 此类为公共父类，收发消息时使用具体类型的消息子类
 * 
 * @author zhuq
 *
 */
public class BaseMessage {

	private String fromuser;// 发送人
	private String channel;//来源渠道
	private String touser;// 接收人
	private String toparty;// 接收群组
	private String totag;// 接收标签
	private String msgtype;// 消息类型
	private String agentid;// 企业id
	private String msgtime;//消息发送时间
	private String safe;// 表示是否是保密消息，0表示否，1表示是，默认0
	private String enable_id_trans;// 表示是否开启id转译，0表示否，1表示是，默认0
	private String enable_duplicate_check;// 表示是否开启重复消息检查，0表示否，1表示是，默认0
	private String duplicate_check_interval;// 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时

	public BaseMessage() {
	}

	public BaseMessage(String touser, String msgtype) {
		this.touser = touser;
		this.msgtype = msgtype;
	}

	public String getFromuser() {
		return fromuser;
	}

	public void setFromuser(String fromuser) {
		this.fromuser = fromuser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(String toparty) {
		this.toparty = toparty;
	}

	public String getTotag() {
		return totag;
	}

	public void setTotag(String totag) {
		this.totag = totag;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getSafe() {
		return safe;
	}

	public void setSafe(String safe) {
		this.safe = safe;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getEnable_id_trans() {
		return enable_id_trans;
	}

	public void setEnable_id_trans(String enable_id_trans) {
		this.enable_id_trans = enable_id_trans;
	}

	public String getEnable_duplicate_check() {
		return enable_duplicate_check;
	}

	public void setEnable_duplicate_check(String enable_duplicate_check) {
		this.enable_duplicate_check = enable_duplicate_check;
	}

	public String getDuplicate_check_interval() {
		return duplicate_check_interval;
	}

	public void setDuplicate_check_interval(String duplicate_check_interval) {
		this.duplicate_check_interval = duplicate_check_interval;
	}
	
	public String getMsgtime() {
		return msgtime;
	}

	public void setMsgtime(String msgtime) {
		this.msgtime = msgtime;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
