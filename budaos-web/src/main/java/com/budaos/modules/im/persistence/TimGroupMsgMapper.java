package com.budaos.modules.im.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.im.domain.TimGroupMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TimGroupMsgMapper extends BaseSqlMapper<TimGroupMsg> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 更新数量
	 * @param timGroupMsg
	 */
	void plusNum(TimGroupMsg timGroupMsg);
	
	/**
	 * 批量更新
	 * @param map
	 * @return
	 */
	int plusNumBatch(Map<String, Object> map);
}