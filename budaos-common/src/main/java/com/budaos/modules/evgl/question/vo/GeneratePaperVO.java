package com.budaos.modules.evgl.question.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 试卷
 * @author huj
 * @create 2022-11-18 8:49
 * @email hujun@budaos.com
 */
public class GeneratePaperVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer chapterNum;

    private Integer subjectNum;

    /**
     * 选择题
     */
    private List<GenerateQuestionVO> choiceQuestions;

    /**
     * 填空题
     */
    private List<GenerateQuestionVO> completionQuestions;

    /**
     * 复合题
     */
    private List<GenerateQuestionVO> compoundQuestions;

    /**
     * 判断题
     */
    private List<GenerateQuestionVO> judgeQuestions;

    /**
     * 简答题
     */
    private List<GenerateQuestionVO> shortAnswerQuestions;

    public GeneratePaperVO() {
    }

    public Integer getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(Integer chapterNum) {
        this.chapterNum = chapterNum;
    }

    public Integer getSubjectNum() {
        return subjectNum;
    }

    public void setSubjectNum(Integer subjectNum) {
        this.subjectNum = subjectNum;
    }

    public List<GenerateQuestionVO> getChoiceQuestions() {
        return choiceQuestions;
    }

    public void setChoiceQuestions(List<GenerateQuestionVO> choiceQuestions) {
        this.choiceQuestions = choiceQuestions;
    }

    public List<GenerateQuestionVO> getCompletionQuestions() {
        return completionQuestions;
    }

    public void setCompletionQuestions(List<GenerateQuestionVO> completionQuestions) {
        this.completionQuestions = completionQuestions;
    }

    public List<GenerateQuestionVO> getCompoundQuestions() {
        return compoundQuestions;
    }

    public void setCompoundQuestions(List<GenerateQuestionVO> compoundQuestions) {
        this.compoundQuestions = compoundQuestions;
    }

    public List<GenerateQuestionVO> getJudgeQuestions() {
        return judgeQuestions;
    }

    public void setJudgeQuestions(List<GenerateQuestionVO> judgeQuestions) {
        this.judgeQuestions = judgeQuestions;
    }

    public List<GenerateQuestionVO> getShortAnswerQuestions() {
        return shortAnswerQuestions;
    }

    public void setShortAnswerQuestions(List<GenerateQuestionVO> shortAnswerQuestions) {
        this.shortAnswerQuestions = shortAnswerQuestions;
    }

    @Override
    public String toString() {
        return "GeneratePaperVO{" +
                "chapterNum=" + chapterNum +
                ", subjectNum=" + subjectNum +
                ", choiceQuestions=" + choiceQuestions +
                ", completionQuestions=" + completionQuestions +
                ", compoundQuestions=" + compoundQuestions +
                ", judgeQuestions=" + judgeQuestions +
                ", shortAnswerQuestions=" + shortAnswerQuestions +
                '}';
    }
}
