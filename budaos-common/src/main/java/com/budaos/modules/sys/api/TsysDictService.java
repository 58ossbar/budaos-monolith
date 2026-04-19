package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysDict;

import java.util.Map;

/**
 * 字典管理接口
 * 
 * @author huangwb
 * @date 2019-05-05 16:03
 */
public interface TsysDictService {
	/**
	 * 根据条件查询数据,用于前端数据操作
	 * 
	 * @author huangwb
	 * @param parentType
	 * @return
	 * @throws Exception
	 */
	R dictByDictType(String parentType);

	/**
	 * 
	 * 删除
	 * 
	 * @author huangwb
	 * @param ids
	 * @return R
	 */
	R deleteType(String[] ids);

	/**
	 * 
	 * 保存修改操作
	 * 
	 * @author huangwb
	 * @param tsysDict
	 * @return R
	 */
	R saveOrUpdate(TsysDict tsysDict, String attachId);

	/**
	 * 
	 * 查询操作
	 * 
	 * @author huangwb
	 * @param params (page页码,limit显示条数)
	 * 
	 * @return R
	 */
	R query(Map<String, Object> params);

	/**
	 * 
	 * 获取字典详情信息
	 * 
	 * @author huangwb
	 * @param dictId
	 * @return R
	 */
	R getDctInfo(String dictId);

	/**
	 * 获取字典表中系统的所有字典信息
	 * 
	 * @return
	 */
	R selectAllTsysDict();

	/**
	 * 获取字典表中parentType!=0的字典信息
	 * 
	 * @return
	 */
	R selectListByMapNotZero(Map<String, Object> map);

	/**
	 * 查询指定父字典id的所有数据
	 * 
	 * @param parentId
	 * @return
	 */
	R selectListParentId(String parentId);

	/**
	 * 根据条件查询数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	R dicttree(String dictname);

}
