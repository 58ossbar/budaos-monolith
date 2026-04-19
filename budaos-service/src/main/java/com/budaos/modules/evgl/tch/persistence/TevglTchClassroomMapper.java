package com.budaos.modules.evgl.tch.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface TevglTchClassroomMapper extends BaseSqlMapper<TevglTchClassroom> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	List<Map<String, Object>> selectClassroomYear(Map<String, Object> map);
	
	/**
	 * 根据条件查询创建课堂的时间
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectCreateClassroomYear(Map<String, Object> map);
	
	/**
	 * 课堂列表 返回对象为驼峰命名
	 * @param map
	 * @return
	 * @apiNote
	 * {
	 * ctId 课堂主键，name 课堂名称，pic 课堂封面， studyNum 学习人数， subjectId 教材主键，
	 * className 班级名称，
	 * teacherName 教师名称，  teacherPic 教师头像，
	 * subjectName 教材名称，
	 * pkgName 教学包名称， pkgResCount 教学包资源数， pkgActCount活动数
	 * subjectProperty 必修、选修， 
	 * teachingAssistantId 助教（学员主键）， traineeName 助教名称， traineePic 助教头像，
	 * }
	 */
	List<Map<String, Object>> selectListMapForCommon(Map<String, Object> map);
	
	/**
	 * 根据条件查询【我教的课】，课堂列表，（注意：此方法兼容了课堂移交前后的数据）
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListMapForCommonV2(Map<String, Object> map);
	
	/**
	 * 查询对像
	 * @param id 
	 * @return
	 * @apiNote 查询返回了如下字段：ctId课堂主键，majorId专业方向主键，teacherId教师主键，classId班级主键，
	 * pkgId教学包主键，pic课堂封面，invitationCode邀请码，name课堂名称，intro课堂简介，studyNum学习人数，
	 * qrCode课堂二维码，className班级名称，subjectId所属课程
	 */
	Map<String, Object> selectObjectMapById(Object id);
	
	/**
	 * 更新数量
	 * @param tevglTchClassroom
	 */
	void plusNum(TevglTchClassroom tevglTchClassroom);
	
	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglTchClassroom> list);
	
	/**
	 * 根据教学包主键查询课堂
	 * @param pkgId
	 * @return
	 */
	TevglTchClassroom selectObjectByPkgId(Object pkgId);
	
	/**
	 * 根据条件统计数量
	 * @param map
	 * @return
	 */
	Integer countNumByMap(Map<String, Object> map);
	
	/**
	 * 获取该用户加入的所有课堂id
	 * @param traineeId
	 * @return
	 */
	List<Map<String, Object>> getCtIdsByTraineeId(String traineeId);
	
	/**
	 * [我的书架]查询参与过的,创建的以及免费的课程信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryBookshelf(Map<String, Object> map);
	
	/**
	 * 用于修改收藏数、点赞数以及阅读量
	 * @param t
	 * @return
	 */
	void updateNum(TevglTchClassroom t);
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectCtIdPkgIdList(Map<String, Object> map);
	
	/**
	 * 根据条件查询群成员
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryImGroupUserList(Map<String, Object> params);
	
	/**
	 * 更新接收者id
	 * @param tevglTchClassroom
	 * @return
	 */
	int updateReceiverUserId(TevglTchClassroom tevglTchClassroom);
	
	/**
	 * 更新助教id
	 * @param tevglTchClassroom
	 * @return
	 */
	int updateTraineeId(TevglTchClassroom tevglTchClassroom);
	
	/**
	 * 统计该课堂的累计签到活动
	 * @param ctId
	 * @return
	 */
	Integer countSignupActivityNum(@Param("ctId") String ctId);
}