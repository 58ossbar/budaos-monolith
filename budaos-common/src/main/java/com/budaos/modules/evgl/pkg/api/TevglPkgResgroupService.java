package com.budaos.modules.evgl.pkg.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.domain.TevglPkgResgroup;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 教学包资源分组目录</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglPkgResgroupService extends IBaseService<TevglPkgResgroup>{
	
	/**
	 * <p>获取具有父子关系的树形数据</p>
	 * @author huj
	 * @data 2019年7月6日
	 * @param pkgId 教学包ID
	 * @return
	 */
	List<TevglPkgResgroup> getPkgResgroup(String pkgId);
	
	/**
	 * <p>查询资源分组</p>  
	 * @author huj
	 * @data 2019年12月20日	
	 * @param params
	 * @return
	 */
	R queryResGroup(Map<String, Object> params);
	
	/**
	 * <p>新增资源分组</p>  
	 * @author huj
	 * @data 2019年12月19日	
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R saveResGroup(JSONObject jsonObject, String loginUserId);
	
	/**
	 * <p>修改资源分组</p>  
	 * @author huj
	 * @data 2019年12月19日	
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R editResGroup(JSONObject jsonObject, String loginUserId);
	
	/**
	 * <p>删除资源分组</p>  
	 * @author huj
	 * @data 2019年12月19日	
	 * @param ids
	 * @return
	 */
	R deleteResGroup(String[] ids);
	
	/**
	 * 重命名
	 * @param resgroupId
	 * @param resgroupName
	 * @return
	 */
	R renameResourceGroup(String resgroupId, String resgroupName, String loginUserId);
	
	/**
	 * 删除资源分组
	 * @param resgroupId
	 * @param loginUserId
	 * @return
	 */
	R deleteResourceGroup(String resgroupId, String loginUserId);
	
	/**
	 * <p>排序</p>  
	 * @author huj
	 * @data 2019年12月19日	
	 * @param currentId 当前选中的记录ID
	 * @param targetId 目标记录的ID
	 * @param createUserId
	 * @return
	 */
	R sortNum(String currentId, String targetId, String createUserId);
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	List<TevglPkgResgroup> selectListByMap(Map<String, Object> params);
	
	/**
	 * 排序
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R sort(JSONObject jsonObject, String loginUserId);
	
}