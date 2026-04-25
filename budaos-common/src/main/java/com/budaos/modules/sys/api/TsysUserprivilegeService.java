package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysUserprivilege;

import java.util.Map;

/**
 * <p>角色与菜单关系api(用户特权api)</p>
 * <p>Title: TsysUserprivilegeService.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年5月8日
 */
public interface TsysUserprivilegeService {

	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param map
	 * @return
	 */
	R query(Map<String, Object> map);
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param tsysUserprivilege
	 * @return
	 */
	R save(TsysUserprivilege tsysUserprivilege);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param tsysUserprivilege
	 * @return
	 */
	R update(TsysUserprivilege tsysUserprivilege);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月8日
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
	 * @data 2019年5月8日
	 * @param id
	 * @return
	 */
	R view(String id);
	
	void saveOrUpdate(String userId,String[] menuIds);
}
