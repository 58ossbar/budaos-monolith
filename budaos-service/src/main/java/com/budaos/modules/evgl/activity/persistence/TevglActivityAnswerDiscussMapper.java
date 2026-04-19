package com.budaos.modules.evgl.activity.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.activity.domain.TevglActivityAnswerDiscuss;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 答疑讨论</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglActivityAnswerDiscussMapper extends BaseSqlMapper<TevglActivityAnswerDiscuss> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件获取最大排序号(已经+1)
	 * @param map
	 * @return
	 */
	Integer getMaxSortNum(Map<String, Object> map);
	
	/**
	 * 查询记录
	 * @param activityId
	 * @return
	 * @apiNote 只查询返回了如下字段: activityId，resgroupId，activityTitle，content，activityPic，empiricalValue，
	 * sortNum，isAllowPic，isAllowVoice，isAllowVideo，purpose
	 */
	Map<String, Object> selectObjectMapById(Object activityId);
	
	/**
	 * 更新数量
	 * @param tevglActivityAnswerDiscuss
	 */
	void plusNum(TevglActivityAnswerDiscuss tevglActivityAnswerDiscuss);
	
	/**
	 * 聊天列表数据
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryChatList(Map<String, Object> map);
	
	/**
	 * 统计答疑讨论实际参与人数（只要发过一条消息，就表示参与了）
	 * @param activityId
	 * @return
	 */
	List<Map<String, Object>> countUserNum(Object activityId);
	
	TevglActivityAnswerDiscuss selectObjectByIdAndPkgId(Map<String, Object> params);
	
	TevglActivityAnswerDiscuss selectObjectById2(Object activityId);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglActivityAnswerDiscuss> list);
	
	TevglActivityAnswerDiscuss selectObjectByGroupId(Object groupId);
}