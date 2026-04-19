package com.budaos.modules.evgl.forum.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.forum.domain.TevglForumBlogPost;
import com.budaos.modules.evgl.forum.vo.TevglForumBlogPostVo;
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
public interface TevglForumBlogPostMapper extends BaseSqlMapper<TevglForumBlogPost> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 查看收藏博客的记录
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> collectBlog(Map<String, Object> map);
	
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> map);
	
	/**
	 * 查看关注过的博主的博客
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> followBloggers(Map<String, Object> map);
	
	/**
	 * 更新查阅数、收藏数、回复数、点赞数、举报数
	 * @param blogPost
	 */
	void plusNum(TevglForumBlogPost blogPost);
	
	/**
	 * 查找推荐标签
	 * @return
	 */
	List<Map<String, Object>> recommendedLabel(Map<String, Object> map);
	
	/**
	 * 获取某人的所有博客的点赞数
	 * @param traineeId
	 * @return
	 */
	Integer getTotalBlogLikes(String traineeId);
	
	/**
	 * 获取博客详情
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBlogDetails(Map<String, Object> map);
	
	List<Map<String, Object>> getDictCodeList(Map<String, Object> map);
	
	/**
	 * 进查询主键id
	 * @param map
	 * @return
	 */
	List<String> selectIdListByMap(Map<String, Object> map);
	
	/**
	 * 批量更新数量（浏览量等）
	 * @param map
	 */
	void plusNumBatch(Map<String, Object> map);
	
	/**
	 * 管理端根据条件查询博客列表
	 * @param map
	 * @return
	 */
	List<TevglForumBlogPostVo> selectVoListByMapForMgr(Map<String, Object> map);
	
}