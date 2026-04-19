package com.budaos.modules.common;

/**
 * 点赞表相关类型，额外的自行补充，表字段注释，物理模型最好跟随维护
 * @author huj
 * @apiNote 1课程2视频3资讯4内部教材5开源文库6题库题目7博客8帖子10活教材11课堂12投票/问卷13头脑风暴14头脑风暴学员作答，还有其它点赞类型，在此依次追加，原有的不要动
 * 15答疑讨论的聊天内容16点赞评论
 *
 */
public class GlobalLike {

	/**
	 * 是否已点赞标识，isLike，true已点赞false未点赞
	 */
	public static String IS_LIKE = "isLike";
	
	/**
	 * 已点赞返回的提示语
	 */
	public static String ALREADY_LIKE_MSG = "你已经点过赞了";
	
	/**
	 * 未点赞返回的提示语
	 */
	public static String NO_LIKE_MSG = "你还没有点赞";
	
	/**
	 * 1课程
	 */
	public static String LIKE_1_SUBJECT = "1";
	
	/**
	 * 2视频
	 */
	public static String LIKE_2_VIDEO = "2";
	
	/**
	 * 3资讯
	 */
	public static String LIKE_3_NEWS = "3";
	
	/**
	 * 4内部教材
	 */
	public static String LIKE_4_DOCS = "4";
	
	/**
	 * 5开源文库
	 */
	public static String LIKE_5_DOCS_OPEN = "5";
	
	/**
	 * 6题库题目
	 */
	public static String LIKE_6_QUESTION = "6";
	
	/**
	 * 7博客
	 */
	public static String LIKE_7_BLOG = "7";
	
	/**
	 * 8帖子
	 */
	public static String LIKE_8_POST = "8";
	
	/**
	 * 10活教材
	 */
	public static String LIKE_10_SUBJECT_LIVE = "10";
	
	/**
	 * 11课堂
	 */
	public static String LIKE_11_CLASSROOM = "11";
	
	/**
	 * 12活动-投票问卷
	 */
	public static String LIKE_12_ACTIVITY_VOTE_QUESTIONNAIRE = "12";
	
	/**
	 * 13活动-头脑风暴
	 */
	public static String LIKE_13_ACTIVITY_BRAINSTORMING = "13";
	
	/**
	 * 14活动-头脑风暴-学员作答
	 */
	public static String LIKE_14_ACTIVITY_BRAINSTORMING_TRAINEE_ANSWER = "14";
	
	/**
	 * 15活动-答疑讨论-学员作答  （答疑讨论群的聊天记录）（targetId存的是消息表t_im_group_msg的主键）
	 */
	public static String LIKE_15_ACTIVITY_ANSWER_DISCUSS_TRAINEE_ANSWER = "15";
	
	/**
	 * 16评论点赞
	 */
	public static String LIKE_16_COMMENT_LIKE = "16";
}
