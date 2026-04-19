package com.budaos.modules.common;

import com.budaos.modules.sys.domain.TsysDict;

import java.util.List;
import java.util.Map;

/**
 * <p>字典ֵ</p>
 * <p>Title: DictService.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: </p> 
 * @author huj
 * @date 2019年7月26日
 */
public interface DictService {

	/**
	 * <p>根据字典编码获取字典集合，返回List<T></p>  
	 * @author huj
	 * @data 2019年7月26日
	 * @param type 该值对应字典表中DICT_TYPE的值
	 * @return
	 */
	List<TsysDict> getTsysDictListByType(String type);
	
	/**
	 * <p>根据字典编码获取字典集合，返回List<Map>，且对象为驼峰命名</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @param type 该值对应字典表中DICT_TYPE的值
	 * @return
	 * @apiNote
	 * {
	 * 	dictId:主键，dictValue，dictCode，dictUrl
	 * }
	 */
	List<Map<String, Object>> getDictList(String type);
	
	String getKeyByVal(String codeType, String val);
}
