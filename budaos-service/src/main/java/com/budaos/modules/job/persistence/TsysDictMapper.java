package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TsysDict;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

@Mapper
public interface TsysDictMapper extends BaseSqlMapper<TsysDict> {
	@Cacheable(value="dict_cache")
	public List<TsysDict> selectListByMapNotZero(Map<String, Object> map);
	@Cacheable(value="dict_cache", key="'selectListParentId_'+#parentId")
	public List<TsysDict> selectListParentId(String parentId);
	@Cacheable(value="dict_cache")
	public List<TsysDict> selectAllTsysDict();

}