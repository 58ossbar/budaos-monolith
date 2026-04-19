package com.budaos.modules.evgl.pkg.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;

import java.util.List;
import java.util.Map;

/**
 * <p> Title:教学包 </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglPkgInfoService extends IBaseService<TevglPkgInfo>{
	
	/**
	 * 保存教学包信息
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @return
	 */
	R saveInfo(TevglPkgInfo tevglPkgInfo, String loginUserId);
	
	/**
	 * 修改教学包信息
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @return
	 */
	R updateInfo(TevglPkgInfo tevglPkgInfo, String loginUserId);
	
	/**
	 * 根据条件查询课程
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectSubjectRefList(Map<String, Object> params);
	
	/**
	 * 我的教学包列表
	 * @param params
	 * @param loginUserId 当前登录用户
	 * @param together 为Y时查询自己创建的教学包和共建中的教学包
	 * @return
	 */
	R listMyPkgInfo(Map<String, Object> params, String loginUserId, String together);
	
	/**
	 * 教学包下拉列表（可查询自己创建、授权的、他人免费的）
	 * @param params
	 * @param loginUserId 登录用户
	 * @param queryAuthorized 布尔值，为true则会查询自己创建和已获得授权的教学包，false只会查询自己创建的教学包
	 * @param queryFree 布尔值，为true还会去查询别人创建的免费的教学包，false则不会查询
	 * @return
	 */
	List<Map<String, Object>> listPkgInfoForSelect(Map<String, Object> params, String loginUserId, boolean queryAuthorized, boolean queryFree);
	
	/**
	 * 【教学包下拉列表】注意：会一次性查询自己的，衍生版本，以及别人创建的免费的，以及被授权的
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	R queryPkgListByUnionAllForSelect(Map<String, Object> params, String loginUserId);
	
	/**
	 * 发布一个全新的教学包
	 * @param jsonObject
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R releaseTeachingPackage(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 引用教学包
	 * @param pkgId pkgId 必传参数，被复制的教学包主键
	 * @param loginUserId 必传参数，当前登录用户
	 * @param display 可选参数，可见性(来源字典:1私有or2公有or3都不可见)，默认 null，如果传了3，谁都看不到，只能从数据库去看
	 * @return
	 */
	R referenceTeachingPackage(String pkgId, String loginUserId, String display);
	
	/**
	 * 复制生成一个全新的教学包，会去复制课程，资源分组，资源，活动
	 * @param inputPkgId 必传参数，被复制的教学包主键
	 * @param loginUserId 必传参数，当前登录用户
	 * @param display 可选参数，可见性(来源字典:1私有or2公有or3都不可见)，默认 null，如果传了3，谁都看不到，只能从数据库去看
	 * @return
	 */
	R copy(String inputPkgId, String loginUserId, String display);
	R copyThen(String inputPkgId, String loginUserId, String display, String classroomName);
	
	/**
	 * 根据条件查询记录
	 * @param id
	 * @return
	 */
	TevglPkgInfo selectObjectById(Object id);
	
	/**
	 * 查看教学包基本信息
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R viewPkgBaseInfo(String pkgId, String loginUserId);
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectSimpleList(Map<String, Object> params);
	
	/**
	 * 将教学包授权给某人，这样某人才能使用这个教学包
	 * @param pkgId 教学包
	 * @param userId 被授权的人
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R authorizationToWho(String pkgId, String userId, String loginUserId);
	
	/**
	 * 取消授权
	 * @param pkgId
	 * @param userId
	 * @param loginUserId
	 * @return
	 */
	R unAuthorization(String pkgId, String userId, String loginUserId);
	
	/**
	 * 查询当前可更换的版本
	 * @param ctId 当前上课的课堂
	 * @param pkgId 当前上课的课堂对应的教学包id
	 * @param loginUserId
	 * @return
	 */
	R queryUpgradeList(String ctId, String pkgId, String loginUserId);
	
	/**
	 * 更换教学包版本
	 * @param ctId
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R doUpgradePkg(String ctId, String pkgId, String loginUserId);
	
	/**
	 * 修改教学包时，查询教学包信息（专用）
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	R viewPkgInfoForUpdate(String pkgId, String loginUserId);
	
	/**
	 * 获取当前教学包的直系父教学包下的直系子教学包们（即历史版本）
	 * @param pkgId
	 * @return
	 */
	R getHistoryPkgList(String pkgId);
	
	/**
	 * 后台管理系统，移交教学包时，教学包列表数据
	 * @param params
	 * @return
	 */
	R queryPackageForChange(Map<String, Object> params);
	
	/**
	 * 移交教学包
	 * @param jsonObject
	 * @return
	 */
	R doChangePackage(JSONObject jsonObject);
	
}