package com.budaos.modules.evgl.trainee.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeInfoVo;
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
public interface TevglTraineeInfoMapper extends BaseSqlMapper<TevglTraineeInfo> {
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);

	TevglTraineeInfo selectByMobile (String mobile);
	
	/**
	 * 根据微信openid查询
	 * @param wxopenId
	 * @return
	 */
	TevglTraineeInfo selectObjectByOpenId(String wxopenId);
	
	/**
	 * 
	 * @param map
	 * @return 目前只查询了 traineeId, traineeName, nickName
	 */
	List<Map<String, Object>> selectSimpleListByMap(Map<String, Object> map);
	
	/**
	 * 更新数量
	 * @param tevglTraineeInfo
	 */
	void plusNum(TevglTraineeInfo tevglTraineeInfo);

	/**
	 * 批量更新经验值
	 * @param list
	 */
	void plusEmpiricalValueBatch(@Param("list") List<TevglTraineeInfo> list);
	
	/**
	 * 查看对象
	 * @param traineeId
	 * @return traineeId学员主键、traineeName学员姓名（昵称）、traineeSex性别、traineeBirthday生日\
	 * email邮箱、traineeSchool就读学校、traineePic证件照（头像）、jobNumber学号、traineeType学员类型traineeTypeName、
	 * empiricalValue总经验值、blogsNum总博客数、classroomNum总开设课堂数
	 */
	Map<String, Object> selectObjectMapById(Object traineeId);
	
	/**
	 * 只查询主键和手机号码不为空的记录
	 * @return
	 */
	List<Map<String, Object>> selectTraineeIdAndMobile();
	
	/**
	 * 查找热门博主
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> popularBloggers(Map<String, Object> map);
	
	/**
	 * 获取当前用户所在的职业路径
	 * @param traineeId
	 * @return
	 */
	List<String> getTraineeMajorIdList(Object traineeId);
	
	/**
	 * 根据条件查询记录
	 * @param id
	 * @return
	 */
	TevglTraineeInfoVo selectTraineeVoById(Object id);
	
	/**
	 * 根据条件查询，不在任何班级里面的学员（学员：账号有效，且绑定了手机号码）
	 * @param map
	 * @return
	 */
	List<TevglTraineeInfoVo> findTraineeListNotInClass(Map<String, Object> map);

	/**
	 * 批量新增
	 * @param list
	 */
	void insertBatch(List<TevglTraineeInfo> list);
	
	/**
	 * 统计学员的总经验值
	 * @param traineeId
	 * @return
	 */
	Integer countEmpiricalValue(@Param("traineeId") Object traineeId);
	
	/**
	 * 统计学生的所有有效博客数
	 * @param traineeId
	 * @return
	 */
	Integer countBlogNum(@Param("traineeId") Object traineeId);

	/**
	 * 统计学生的博客获取到的点赞数
	 * @param traineeId
	 * @return
	 */
	Integer countBlogLikeNum(@Param("traineeId") Object traineeId);

	/**
	 * 统计学生在某课堂的课堂表现次数
	 * @param traineeId
	 * @param ctId
	 * @return
	 */
	Integer countClassroomPerformanceNum(@Param("traineeId") Object traineeId, @Param("ctId") Object ctId);
	
	/**
	 * 根据唯一token查询用户id
	 * @param token
	 * @return
	 */
	TevglTraineeInfoVo selectTraineeVoByToken(@Param("token") Object token);
}