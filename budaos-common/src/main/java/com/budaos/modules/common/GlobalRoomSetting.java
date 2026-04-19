package com.budaos.modules.common;

public class GlobalRoomSetting {

	/**
	 * 设置排序时，被影响的模块，课堂成员
	 * @apiNote 表t_evgl_tch_classroom_setting表中，sidx字段此时的含义如下<br>
	 * 1根据经验值排序<br>
	 * 2根据学员名称<br>
	 * 3根据手机号码<br>
	 * 4根据加入课堂时间
	 */
	public static String MDULES_CLASSROOM_TRAINEE = "classroom_trainee";
	
}
