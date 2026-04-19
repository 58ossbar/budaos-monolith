package com.budaos.modules.evgl.site.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.domain.TevglSiteResourceext;
import com.budaos.modules.evgl.site.vo.TsysResourceVo;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.modules.sys.domain.TsysResource;

import java.util.List;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglSiteResourceextService extends IBaseService<TevglSiteResourceext>{
	 
	/**
	 * <p>查询所有站点及栏目列表</p>
	 * @author huj
	 * @data 2019年7月15日
	 * @param menuId
	 * @return
	 */
	List<TsysResourceVo> querySite(String menuId);
	
	/**
	 * <p>查看站点信息</p>
	 * @author huj
	 * @data 2019年7月15日
	 * @param id
	 * @return
	 */
	TsysResource viewSite(String id);
	
	/**
	 * <p>新增或修改站点栏目</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param menu
	 * @return
	 */
	R saveOrUpdateSite(TsysResource menu);
	
	/**
	 * <p>删除站点</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param id
	 * @return
	 */
	R deleteSite(String id);
	
	/**
	 * <p>获取类型,如【站点】【栏目】</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @return
	 */
	R getSiteType();
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年7月17日
	 * @return
	 */
	TsysResource editSite(String id);
	
	/**
	 * <p>获取站点行业类型,如IT互联网、汽车等</p>
	 * @author huj
	 * @data 2019年7月20日
	 * @return
	 */
	List<TsysDict> getTradeType();
}