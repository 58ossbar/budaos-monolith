package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TorgUser;
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
public interface TorgUserMapper extends BaseSqlMapper<TorgUser> {
	List<String> selectListByUserId(String userId);
	int selectTotalByMap(Map<String,Object> map);
}