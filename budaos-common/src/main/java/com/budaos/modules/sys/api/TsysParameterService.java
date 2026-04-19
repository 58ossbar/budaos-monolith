package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysParameter;

import java.util.Map;

/**
 * 参数管理
 * 
 * @author huangwb
 * @date 2019-05-05 16:03
 */
public interface TsysParameterService {
	/**
	 * 删除
	 * 
	 * @param ids
	 * @return R
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R delete(String[] ids);

	/**
	 * 保存修改
	 * 
	 * @param tsysparameter
	 * @return R
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R saveorupdate(TsysParameter tsysparameter);

	/**
	 * 所有配置列表
	 * 
	 * @param params
	 * @return R
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R list(Map<String, Object> params);

	/**
	 * 根据参数Id获取参数详情
	 * 
	 * @param parameterId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R get(String parameterId);

	R selectAllTsysParameter();

	/**
	 * 根据key更新参数value
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R updateValueByKey(String key, String value);

	/**
	 * 根据key获取value 如果key为空则给defaultValue默认值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	R getValue(String key, String defaultValue);

	/**
	 * 根据参数管理左侧的树结构
	 * 
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	R paraTree();

	/**
	 * 根据paraType获取类型的所有参数 方便前端下拉框选择
	 * 
	 * @param paraType
	 * @return R
	 * @author huangwb
	 * @date 2019-05-29 15:18
	 */
	R getPara(String paraType);
}
