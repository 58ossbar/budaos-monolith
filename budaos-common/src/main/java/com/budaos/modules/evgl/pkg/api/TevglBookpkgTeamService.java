package com.budaos.modules.evgl.pkg.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeam;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 资源共建权限</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglBookpkgTeamService extends IBaseService<TevglBookpkgTeam>{
	
	/**
	 * 授权共建权限
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R authorization(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 查询一个或多个人的章节权限
	 * @param jsonObject 必传参数：pkgId所属教学包，subjectId课程，traineeIds:[]
	 * @param loginUserId
	 * @return
	 */
	R queryAuthorization(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	List<TevglBookpkgTeam> selectListByMap(Map<String, Object> map);

	/**
	 * 验证教学包共建教材的某章节是否操作权限
	 * @param pkgId 教学包id
	 * @param chapterId 需要验证的章节
	 * @param traineeId 当前登录用户id
	 * @return 返回true表示有操作权限
	 */
	boolean checkNodePermission(String pkgId, String chapterId, String traineeId);

	/**
	 * 验证教学包共建教材的章节是否操作权限
	 * @param pkgId 教学包id
	 * @param chapterIdList 需要验证的章节
	 * @param traineeId 当前登录用户id
	 * @return 返回true表示有操作权限
	 */
	boolean checkNodePermission(String pkgId, List<String> chapterIdList, String traineeId);
	
}