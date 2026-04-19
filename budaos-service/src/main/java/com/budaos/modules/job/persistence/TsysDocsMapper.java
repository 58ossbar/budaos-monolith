package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TsysDocs;
import org.apache.ibatis.annotations.Mapper;

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
public interface TsysDocsMapper extends BaseSqlMapper<TsysDocs> {
	public int selectMaxSortByMap(Map<String,Object> map);
	public void plusNum(TsysDocs tsysDocs);
	public List<TsysDocs> selectListSimpleByMap(Map<String,Object> map);
}