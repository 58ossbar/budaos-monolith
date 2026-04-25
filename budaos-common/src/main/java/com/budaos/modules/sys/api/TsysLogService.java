package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysLog;

import java.util.Map;

/**
 * 系统日志api
 * <p>Title: TsysLogServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖布道师学习通</p> 
 * @author huj
 * @date 2019年5月6日
 */
public interface TsysLogService {

	/**
	 * <p>根据条件查询站点列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param params
	 * @return
	 */
	R query(Map<String, Object> params);
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param sysLog
	 * @return
	 */
	R save(TsysLog sysLog);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param sysLog
	 * @return
	 */
	R update(TsysLog sysLog);
	
	/**
	 * <p>单挑删除</p>
	 * @author huj
	 * @data 2019年5月23日
	 * @param id
	 * @return
	 */
	R delete(String id);
	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param ids
	 * @return
	 */
	R deleteBatch(String[] ids);
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param id
	 * @return
	 */
	R view(String id);
}
