package com.budaos.modules.evgl.question.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 题目
 * @author huj
 * @create 2022-11-18 8:51
 * @email hujun@budaos.com
 */
public class GenerateQuestionVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 题目id
     */
    private String questionsId;

    /**
     * 父题目id
     */
    private String parentId;

    /**
     * 题目名称
     */
    private String questionsName;

    /**
     * 题目解析
     */
    private String questionsParse;

    /**
     * 题目类型
     */
    private String questionsType;

    /**
     * 题目类型
     */
    private String questionsTypeName;

    /**
     * 难易程度
     */
    private String questionsComplexity;

    /**
     * 难易程度
     */
    private String questionsComplexityName;

    /**
     * 正确选项id
     */
    private String replyIds;

    /**
     * 课程id
     */
    private String subjectId;

    /**
     * 选项集合
     */
    private List<GenerateOptionVO> optionList;

    /**
     * 子题目
     */
    private List<GenerateQuestionVO> children;

    public GenerateQuestionVO() {
    }

    public String getQuestionsId() {
        return questionsId;
    }

    public void setQuestionsId(String questionsId) {
        this.questionsId = questionsId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getQuestionsName() {
        return questionsName;
    }

    public void setQuestionsName(String questionsName) {
        this.questionsName = questionsName;
    }

    public String getQuestionsParse() {
        return questionsParse;
    }

    public void setQuestionsParse(String questionsParse) {
        this.questionsParse = questionsParse;
    }

    public String getQuestionsType() {
        return questionsType;
    }

    public void setQuestionsType(String questionsType) {
        this.questionsType = questionsType;
    }

    public String getQuestionsTypeName() {
        return questionsTypeName;
    }

    public void setQuestionsTypeName(String questionsTypeName) {
        this.questionsTypeName = questionsTypeName;
    }

    public String getQuestionsComplexity() {
        return questionsComplexity;
    }

    public void setQuestionsComplexity(String questionsComplexity) {
        this.questionsComplexity = questionsComplexity;
    }

    public String getQuestionsComplexityName() {
        return questionsComplexityName;
    }

    public void setQuestionsComplexityName(String questionsComplexityName) {
        this.questionsComplexityName = questionsComplexityName;
    }

    public String getReplyIds() {
        return replyIds;
    }

    public void setReplyIds(String replyIds) {
        this.replyIds = replyIds;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public List<GenerateOptionVO> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<GenerateOptionVO> optionList) {
        this.optionList = optionList;
    }

    public List<GenerateQuestionVO> getChildren() {
        return children;
    }

    public void setChildren(List<GenerateQuestionVO> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "GenerateQuestionVO{" +
                "questionsId='" + questionsId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", questionsName='" + questionsName + '\'' +
                ", questionsParse='" + questionsParse + '\'' +
                ", questionsType='" + questionsType + '\'' +
                ", questionsTypeName='" + questionsTypeName + '\'' +
                ", questionsComplexity='" + questionsComplexity + '\'' +
                ", questionsComplexityName='" + questionsComplexityName + '\'' +
                ", replyIds='" + replyIds + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", optionList=" + optionList +
                ", children=" + children +
                '}';
    }
}
