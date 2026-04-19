package com.budaos.modules.evgl.eao.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.eao.domain.TeaoFdRefusebill;
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
public interface TeaoFdRefusebillMapper extends BaseSqlMapper<TeaoFdRefusebill> {
	
	List<Map<String, Object>> selectListByMapForRefund(Map<String, Object> map);
}