package com.budaos.modules.evgl.site.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.domain.TevglSiteNews;
import com.budaos.modules.evgl.site.vo.TevglSiteNewsVo;
import com.budaos.modules.sys.domain.TsysDict;

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

public interface TevglSiteNewsService extends IBaseService<TevglSiteNews>{
	
	/** 
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @param tevglSiteNews
	 * @param attachId
	 * @return
	 */
	R save2(TevglSiteNews tevglSiteNews, String attachId);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @param tevglSiteNews
	 * @param attachId
	 * @return
	 */
	R update2(TevglSiteNews tevglSiteNews, String attachId);
	
	/**
	 * <p>进入修改新闻资讯时的查看明细</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @param id
	 * @return
	 */
	R toEdit(String id);
	
	/**
	 * <p>从字典获取新增资讯分类,如企业新闻,行业新闻等</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @return
	 */
	R getCategory();
	
	/**
	 * <p>从字典获取发布方式,如直接发布、审核发布</p>
	 * @author huj
	 * @data 2019年7月20日
	 * @return
	 */
	List<TsysDict> getDeployed();
	
	/**
	 * <p>从字典获取是否头条咨询</p>
	 * @author huj
	 * @data 2019年7月20日
	 * @return
	 */
	List<TsysDict> getIsHeadlineNews();
	
	/**
	 * <p>从字典获取状态，状态1待审2已发布 3删除</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @return
	 */
	List<TsysDict> getNewsStatus();
	
	/**
	 * <p>从字典获取是否展示Y显示N隐藏</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @return
	 */
	List<TsysDict> getIsShow();
	
	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月28日
	 * @param tevglSiteNews
	 * @return
	 */
	R updateState(TevglSiteNews tevglSiteNews);
	
	/**
	 * <p>审核</p>  
	 * @author huj
	 * @data 2019年8月10日	
	 * @param ids
	 * @return
	 */
	R check(String[] ids, String state, String reason);
	
	/**
	 * 查询部分信息，且对象为驼峰命名
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListByMap(Map<String, Object> params);
	
	/**
	 * 小程序查询新闻详情
	 * @param id
	 * @return
	 */
	R viewForWx(Object id);
	
	/**
	 * 新闻列表（公众号情况下使用）
	 * @param params
	 * @return
	 */
	R queryForOfficial(Map<String, Object> params);
	
	/**
	 * 新增（公众号情况下使用）
	 * @param tevglSiteNews
	 * @return
	 */
	R saveForOfficial(TevglSiteNewsVo vo);
	
	/**
	 * 修改（公众号情况下使用）
	 * @param vo
	 * @return
	 */
	R updateForOfficial(TevglSiteNewsVo vo);
	
	/**
	 * 查看（公众号情况下使用）
	 * @param newsid
	 * @return
	 */
	R viewForOfficial(String newsid);
	
	/**
	 * 用户端查看详情
	 * @param newsid
	 * @param traineeId
	 * @return
	 */
	R viewDetailForOfficial(String newsid, String traineeId);

	/**
	 * 更新状态
	 * @param newsid
	 * @param status
	 * @return
	 */
	R release(String newsid, String status);
	
	/**
	 * 批量更新状态
	 * @param newidList
	 * @param status
	 * @return
	 */
	R releaseBatch(String[] newidList, String status);
}