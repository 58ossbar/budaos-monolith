package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TuserPost;

import java.util.List;
import java.util.Map;

/**
 * <p>用户与岗位的关系 api</p>
 * <p>Title: TuserPostService.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年5月29日
 */
public interface TuserPostService {

	/**
	 * <p>查询列表</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param map
	 * @return
	 */
	List<TuserPost> selectListByMap(Map<String, Object> map);
	
	/**
	 * <p>查询明细</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	TuserPost selectObjectById(String id);
	
	/**
	 * <p>保存用户与岗位之间的关系</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param userId
	 * @param postIds
	 * @return
	 */
	R saveOrUpdate(String userId, List<String> postIds);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param tuserPost
	 * @return
	 */
	R update(TuserPost tuserPost);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	R delete(String id);

	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param ids
	 * @return
	 */
	R deleteBatch(String[] ids);

    /**
     * 根据用户id，查询岗位id
     * @param userId
     * @return
     */
    List<String> selectPostIdListByUserId(String userId);
}
