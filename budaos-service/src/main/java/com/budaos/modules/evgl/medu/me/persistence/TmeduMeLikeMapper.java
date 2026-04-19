package com.budaos.modules.evgl.medu.me.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeLike;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 点赞表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TmeduMeLikeMapper extends BaseSqlMapper<TmeduMeLike> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 别人给我点的赞
	 * @param params
	 * @return likeId、likeType点赞类型、targetId目标、likeTime点赞时间、
	 * targetTraineeId被点赞目标的所属人（也就是自己）、
	 * traineeId、fromTraineeId别人、fromTraineeNames给自己点赞的人的名称、fromTraineePic
	 */
	List<Map<String, Object>> queryPeopleGiveMeLikes(Map<String, Object> params);
	
	int updateReadState(Map<String, Object> map);
	
	/**
	 * 统计当前用户未读的点赞数
	 * @param loginUserId
	 * @return
	 */
	int countUnreadNum(String loginUserId);
}