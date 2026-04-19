package com.budaos.modules.evgl.site.query;

import java.io.Serializable;

/**
 * @author huj
 * @create 2022-06-20 16:02
 * @email hujun@budaos.com
 */
public class TevglSiteSysMsgQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 接收者
     */
    private String toTraineeId;

    /**
     * 标题
     */
    private String msgTitle;

    /**
     * 消息类型0纯文本01课堂1投票问卷2头脑风暴3答疑讨论4测试活动5作业/小组任务6课堂表现7轻直播/讨论8签到
     */
    private String msgType;

    /**
     * 是否已读(Y已读N未读)
     */
    private String readState;

    public String getToTraineeId() {
        return toTraineeId;
    }

    public void setToTraineeId(String toTraineeId) {
        this.toTraineeId = toTraineeId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    @Override
    public String toString() {
        return "TevglSiteSysMsgQuery{" +
                "toTraineeId='" + toTraineeId + '\'' +
                ", msgTitle='" + msgTitle + '\'' +
                ", msgType='" + msgType + '\'' +
                ", readState='" + readState + '\'' +
                '}';
    }
}
