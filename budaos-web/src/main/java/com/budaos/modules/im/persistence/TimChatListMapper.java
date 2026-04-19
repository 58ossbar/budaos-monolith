package com.budaos.modules.im.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.im.domain.TimChatList;
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
public interface TimChatListMapper extends BaseSqlMapper<TimChatList> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);

	/**
	 * 累加未读消息数量
	 * @param chat
	 */
	void plusNum(TimChatList chat);
	
	/**
	 * 根据对象查询列表数据
	 * @param chat
	 * @return
	 */
	List<TimChatList> selectListByBean(TimChatList chat);
	
	/**
	 * 获取自己和某人的聊天列表
	 * @param map
	 * @return
	 */
	Map<String, Object> getSeftWithUserChat(Map<String, Object> map);
	
	/**
	 * 获取某群的聊天列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getWithGroupChat(Map<String, Object> map);
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectSimpleList(Map<String, Object> map);
}