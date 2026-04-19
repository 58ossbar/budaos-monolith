package com.budaos.modules.evgl.eao.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.eao.domain.TeaoFdOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface TeaoFdOrderMapper extends BaseSqlMapper<TeaoFdOrder> {
	
	TeaoFdOrder selectObjectBySeriano(String id);
	
	List<Map<String, Object>> selectListByState(Map<String, Object> map);
	
	List<String> findObjectByTraineeIdAndClassId(@Param("traineeId") String traineeId, @Param("classId") String classId);
}