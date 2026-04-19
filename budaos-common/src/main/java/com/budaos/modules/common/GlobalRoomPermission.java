package com.budaos.modules.common;

public class GlobalRoomPermission {
	
	public static String LICENSE = "license";
	
	/**
	 * 没有权限时，提示语：没有权限，操作失败！请联系课堂老师
	 */
	public static String NO_AUTH_SHOW_TEXT = "没有权限，操作失败！请联系课堂老师";
	
	/**
	 * 顶级的【课堂权限设置】菜单
	 */
	public static String MENU_ID = "04c786f2bfbf46398e3b495f6c7014bc";
	
	/**
	 * 助教设置章节对学员是否可见标识
	 */
	public static String ROOM_PERM_SUBJECT_CHAPTERVISIBLE = "room:subject:chaptervisible";
	
	/**
	 * 助教设置章节分组标签是否可见
	 */
	public static String ROOM_PERM_SUBJECT_RESGROUPVISIBLE = "room:subject:resgroupvisible";
	
	/**
	 * 助教设置章节分组标签是否可见 hasSetResVisiblePermission
	 */
	public static String ROOM_PERM_SUBJECT_RESGROUPVISIBLE_KEY = "hasSetResVisiblePermission";
	
	/**
	 * 开始活动 room:act:start
	 */
	public static String ROOM_PERM_ACT_START = "room:act:start";
	
	/**
	 * 开始活动 hasStartActPermission
	 */
	public static String ROOM_PERM_ACT_START_KEY = "hasStartActPermission";
	
	/**
	 * 结束活动 room:act:end
	 */
	public static String ROOM_PERM_ACT_END = "room:act:end";
	
	/**
	 * 结束活动 hasEndActPermission
	 */
	public static String ROOM_PERM_ACT_END_KEY = "hasEndActPermission";
	
	/**
	 * 新建活动 room:act:add
	 */
	public static String ROOM_PERM_ACT_ADD = "room:act:add";
	
	/**
	 * 新建活动 hasAddActPermission
	 */
	public static String ROOM_PERM_ACT_ADD_KEY = "hasAddActPermission";
	
	/**
	 * 修改活动 room:act:edit
	 */
	public static String ROOM_PERM_ACT_EDIT = "room:act:edit";
	
	/**
	 * 修改活动 hasEditActPermission
	 */
	public static String ROOM_PERM_ACT_EDIT_KEY = "hasEditActPermission";
	
	/**
	 * 删除活动 room:act:delete
	 */
	public static String ROOM_PERM_ACT_DELETE = "room:act:delete";
	
	/**
	 * 删除活动 hasDeleteActPermission
	 */
	public static String ROOM_PERM_ACT_DELETE_KEY = "hasDeleteActPermission";
	
	/**
	 * 审核成员 room:trainee:check
	 */
	public static String CHECK_TRAINEEA = "room:trainee:check";
	
	/**
	 * 审核成员 hasCheckTraineePermission
	 */
	public static String CHECK_TRAINEE_KEY = "hasCheckTraineePermission";
	
	/**
	 * 将学员从课堂中移除 room:trainee:delete
	 */
	public static String DELETE_TRAINEE = "room:trainee:delete";
	
	/**
	 * 将学员从课堂中移除 hasDeleteTraineePermission
	 */
	public static String DELETE_TRAINEE_KEY = "hasDeleteTraineePermission";
	
	/**
	 * 编辑学员信息 room:trainee:edit
	 */
	public static String EDIT_TRAINEE_INFO = "room:trainee:edit";
	
	/**
	 * 编辑学员信息 hasEditTraineePermission
	 */
	public static String EDIT_TRAINEE_INFO_KEY = "hasEditTraineePermission";
	
	/**
	 * 是否有添加课程成员的权限room:trainee:add
	 */
	public static String ADD_TRAINEE_INFO = "room:trainee:add";
	
	/**
	 * 是否有添加课程成员的权限hasAddTraineePermission
	 */
	public static String ADD_TRAINEE_INFO_KEY = "hasAddTraineePermission";
	
	/**
	 * 新建课堂小组room:group:add
	 */
	public static String ADD_ROOM_GROUP = "room:group:add";
	
	/**
	 * 新建课堂小组hasAddGroupPermission
	 */
	public static String ADD_ROOM_GROUP_KEY = "hasAddGroupPermission";
	
	/**
	 * 重命名课堂小组room:group:rename
	 */
	public static String RENAME_ROOM_GROUP = "room:group:rename";
	
	/**
	 * 重命名课堂小组hasRenameGroupPermission
	 */
	public static String RENAME_ROOM_GROUP_KEY = "hasRenameGroupPermission";
	
	/**
	 * 删除课堂小组room:group:delete
	 */
	public static String DELETE_ROOM_GROUP = "room:group:delete";
	
	/**
	 * 删除课堂小组hasDeleteGroupPermission
	 */
	public static String DELETE_ROOM_GROUP_KEY = "hasDeleteGroupPermission";
	
	/**
	 * 添加小组成员room:group:addgmtrainee
	 */
	public static String ADD_ROOM_GROUP_TRAINEE = "room:group:addgmtrainee";
	
	/**
	 * 添加小组成员hasAddGmTraineePermission
	 */
	public static String ADD_ROOM_GROUP_TRAINEE_KEY = "hasAddGmTraineePermission";
	
	/**
	 * 设置课堂小组的某人为小组组长room:group:setleader
	 */
	public static String SET_ROOM_GROUP_LEADER = "room:group:setleader";
	
	/**
	 * 设置课堂小组的某人为小组组长hasSetGmLeaderPermission
	 */
	public static String SET_ROOM_GROUP_LEADER_KEY = "hasSetGmLeaderPermission";
	
	/**
	 * 删除课堂小组成员room:group:deletegmtrainee
	 */
	public static String DELETE_ROOM_GROUP_TRAINEE = "room:group:deletegmtrainee";
	
	/**
	 * 添加小组成员hasDeleteGmTraineePermission
	 */
	public static String DELETE_ROOM_GROUP_TRAINEE_KEY = "hasDeleteGmTraineePermission";
	

	/**
	 * 测试活动-简答题-打分权限 room:act:test:short-answer-question:grade
	 */
	public static String ACTIVITY_4_SHORT_ANSWER_GRADING = "room:act:test:short-answer-question:grade";

	/**
	 * 测试活动-简答题-打分权限 hasActivity4ShortAnswerGradingPermission
	 */
	public static String ACTIVITY_4_SHORT_ANSWER_GRADING_KEY = "hasActivity4ShortAnswerGradingPermission";
}
