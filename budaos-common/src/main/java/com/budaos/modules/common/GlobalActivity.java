package com.budaos.modules.common;

/**
 * 活动相关常量，来源字典1投票问卷2头脑风暴3答疑讨论4测试活动5作业/小组任务6课堂表现7轻直播/讨论8签到，若还有其它活动，在此依次追加，原有的不要动
 * @author huj
 *
 */
public class GlobalActivity {
	

	/**
	 * hasStarted：活动是否已经开始true/false
	 */
	public static String HAS_STARTED = "hasStarted";
	
	/**
	 * hasBeenDone：活动是否已做过（参与了）true/false
	 */
	public static String HAS_BEEN_DONE = "hasBeenDone";
	
	/**
	 * isCreator：是否为此活动创建者，true/false
	 */
	public static String IS_CREATOR = "isCreator";
	
	/**
	 * 字典-活动分组
	 */
	public static String ACTIVITY_GROUP_DICT_TYPE = "activityGroup";
	
	/**
	 * 表示是来源字典的活动分组
	 */
	public static String ACTIVITY_GROUP_SYSTEM = "system";
	
	/**
	 * 表示是自定义的活动分组
	 */
	public static String ACTIVITY_GROUP_CUSTOM = "custom"; 
	
	/**
	 * 资源分组表t_evgl_pkg_resgroup的group_type字段标识:1活教材(课程)2活动
	 */
	public static String ACTIVITY_GROUP_TYPE_1 = "1";
	
	/**
	 * 资源分组表t_evgl_pkg_resgroup的group_type字段标识:1活教材(课程)2活动
	 */
	public static String ACTIVITY_GROUP_TYPE_2 = "2";

	/**
	 * 活动之【投票/问卷】【1】
	 */
	public static String ACTIVITY_1_VOTE_QUESTIONNAIRE = "1";
	
	/**
	 * 活动之【头脑 风暴】【2】
	 */
	public static String ACTIVITY_2_BRAINSTORMING = "2";
	
	/**
	 * 活动之【答疑/讨论】【3】
	 */
	public static String ACTIVITY_3_ANSWER_DISCUSS = "3";
	
	/**
	 * 4测试活动 
	 */
	public static String ACTIVITY_4_TEST_ACT = "4";
	
	/**
	 * 5作业/小组任务 
	 */
	public static String ACTIVITY_5_TASK_GROUP = "5";
	
	/**
	 * 6课堂表现
	 */
	public static String ACTIVITY_6_CLASSROOM_PERFORMANCE = "6";
	
	/**
	 * 7轻直播
	 */
	public static String ACTIVITY_7_LIGHT_LIVE = "7";
	
	/**
	 * 活动之【签到活动】【8】
	 */
	public static String ACTIVITY_8_SIGININ_INFO = "8";
	
	/**
	 * 实践考核
	 */
	public static String ACTIVITY_9_TRAINEE_EXAM = "9";
	
}
