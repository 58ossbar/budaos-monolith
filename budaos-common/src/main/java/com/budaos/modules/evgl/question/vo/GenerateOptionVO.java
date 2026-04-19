package com.budaos.modules.evgl.question.vo;

import java.io.Serializable;

/**
 * 题目选项
 * @author huj
 * @create 2022-11-18 8:56
 * @email hujun@budaos.com
 */
public class GenerateOptionVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 选项id
     */
    private String optionId;

    /**
     * 题目id
     */
    private String questionsId;

    /**
     * 选项编码
     */
    private String code;

    /**
     * 选项内容
     */
    private String content;

    /**
     * 状态
     */
    private String state;

    public GenerateOptionVO() {
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getQuestionsId() {
        return questionsId;
    }

    public void setQuestionsId(String questionsId) {
        this.questionsId = questionsId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "GenerateOptionVO{" +
                "optionId='" + optionId + '\'' +
                ", questionsId='" + questionsId + '\'' +
                ", code='" + code + '\'' +
                ", content='" + content + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
