package com.budaos.modules.evgl.tch.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;

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

public interface TevglTchTeacherService extends IBaseService<TevglTchTeacher>{
	 
	/**
	 * <p>����</p>
	 * @author huj
	 * @data 2019��7��26��
	 * @param tevglTchTeacher
	 * @param attachId
	 * @return
	 */
	R saveTeacherInfo(TevglTchTeacher tevglTchTeacher, String attachId);
	
	/**
	 * <p>�޸�</p>
	 * @author huj
	 * @data 2019��7��26��
	 * @param tevglTchTeacher
	 * @param attachId
	 * @return
	 */
	R updateTeacherInfo(TevglTchTeacher tevglTchTeacher, String attachId);
	
	/**
	 * <p>��������ѯ��¼</p>
	 * @author huj
	 * @data 2019��7��23��
	 * @param map
	 * @return
	 */
	R querySimpleListMapByMap(Map<String, Object> map, String type);
	
	/**
	 * <p>����״̬���Ƿ���ҳ��ʾ</p>
	 * @author huj
	 * @data 2019��7��28��
	 * @param tevglTchTeacher
	 * @return
	 */
	R updateStateOrShowIndex(TevglTchTeacher tevglTchTeacher);
	
	/**
	 * <p>根据条件查询教师，无分页</p>  
	 * @author huj
	 * @data 2019年8月20日	
	 * @param map
	 * @return
	 */
	List<TevglTchTeacher> queryNoPage(Map<String, Object> map);
	
	TevglTchTeacher selectObjectById(Object id);
	TevglTchTeacher selectObjectByTraineeId(Object id);
	
	/**
	 * 修改个人信息
	 * @param data
	 * @return
	 */
	R updateTeacherInfo(Map<String, Object> data);
	
	/**
	 * 根据条件查询记录，返回List<T>，关联学员表
	 * @param map
	 * @return 
	 */
	List<TevglTchTeacher> selectListByMapInnerJoinTraineeTable(Map<String, Object> map);
}