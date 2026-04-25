package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysSerialno;

import java.util.Map;

/**
 * 业务流水号自定义api
 * <p>Title: TsysSerialnoServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年5月6日
 */
public interface TsysSerialnoService {

	/**
	 * <p>根据条件查询站点列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param params
	 * @return
	 */
	R query(Map<String, Object> map);
	
	/**
	 * <p>注:原ModelAndView进入新增页面的方法</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param tsysSerialno
	 * @return
	 */
	R add();
	
	/**
	 * <p>注:原ModelAndView进入修改页面的方法</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param params
	 * @return
	 */
	R edit(Map<String, Object> params);
	
	/**
	 * <p>执行数据保存和修改</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysSerialno
	 * @return
	 */
	R saveOrUpdate(TsysSerialno tsysSerialno);
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param tsysSerialno
	 * @return
	 */
	R save(TsysSerialno tsysSerialno);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param tsysSerialno
	 * @return
	 */
	R update(TsysSerialno tsysSerialno);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param ids
	 * @return
	 */
	R delete(String id);
	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月8日
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
