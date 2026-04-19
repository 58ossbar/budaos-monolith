package com.budaos.modules.evgl.question.dto;

import java.io.Serializable;

/**
 * @author huj
 * @create 2022-11-18 14:59
 * @email hujun@budaos.com
 */
public class SaveScoreGapAmendDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课堂id
     */
    private String ctId;

    /**
     * 分值id
     */
    private String scoreId;

    /**
     * 老师给出的分值
     */
    private String score;

    /**
     * 对应的原因
     */
    private String reason;

    public SaveScoreGapAmendDTO() {
    }

    public String getCtId() {
        return ctId;
    }

    public void setCtId(String ctId) {
        this.ctId = ctId;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "SaveScoreGapAmendDTO{" +
                "ctId='" + ctId + '\'' +
                ", scoreId='" + scoreId + '\'' +
                ", score='" + score + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
