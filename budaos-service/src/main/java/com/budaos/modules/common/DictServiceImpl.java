package com.budaos.modules.common;

import com.budaos.common.utils.DictUtil;
import com.budaos.modules.sys.domain.TsysDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>从字典获取数据，便于前后端取值</p>
 * <p>Title: DictService.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: </p> 
 * @author huj
 * @date 2019年7月26日
 */
@Service
@RequestMapping("/dictionary")
public class DictServiceImpl implements DictService {

	@Autowired
	private DictUtil dictUtil;
	
	/**
	 * <p>根据字典编码获取字典集合，返回List<T></p>  
	 * @author huj
	 * @data 2019年7月26日
	 * @param type 该值对应字典表中DICT_TYPE的值
	 * @return
	 */
	@Override
	public List<TsysDict> getTsysDictListByType(String type) {
		return dictUtil.getByType(type);
    }
	
	/**
	 * <p>根据字典编码获取字典集合</p>  
	 * @author huj
	 * @data 2019年12月9日	
	 * @param type 该值对应字典表中DICT_TYPE的值
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDictList(String type){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		List<TsysDict> list = dictUtil.getByType(type);
		if (list != null && list.size() > 0 ) {
			result = list.stream().map(this::converToSimpleMap).collect(Collectors.toList());
		}
		return result;
	}
	
	/**
	 * 取部分属性
	 * @param dict
	 * @return
	 * @apiNote
	 * {
	 * 	dictId:主键，dictValue，dictCode，dictUrl
	 * }
	 */
	private Map<String, Object> converToSimpleMap(TsysDict dict){
		Map<String, Object> info = new HashMap<>();
		info.put("dictId", dict.getDictId());
		info.put("dictValue", dict.getDictValue());
		info.put("dictCode", dict.getDictCode());
		info.put("dictUrl", dict.getDictUrl());
		return info;
	}

	@Override
	public String getKeyByVal(String codeType, String val) {
		return dictUtil.getKeyByVal(codeType, val);
	}
	
}
