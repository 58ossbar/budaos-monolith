package com.budaos.modules.evgl.site.vo;

import java.io.Serializable;

/**
 * @author huj
 * @create 2022-06-20 15:59
 * @email hujun@budaos.com
 */
public class TevglSiteSysMsgVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msgId;

    private String msgTitle;

    private String msgContent;

    private String readState;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    @Override
    public String toString() {
        return "TevglSiteSysMsgVo{" +
                "msgId='" + msgId + '\'' +
                ", msgTitle='" + msgTitle + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", readState='" + readState + '\'' +
                '}';
    }
}
