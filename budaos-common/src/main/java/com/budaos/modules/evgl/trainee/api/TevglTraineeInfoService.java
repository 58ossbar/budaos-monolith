package com.budaos.modules.evgl.trainee.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeInfoUpdateVo;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeInfoVo;

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

public interface TevglTraineeInfoService extends IBaseService<TevglTraineeInfo>{

	public TevglTraineeInfo selectByMobile (String mobile);
	
	public void doAfterBindMobile(TevglTraineeInfo traineeInfo);
	
	public List<TevglTraineeInfo> queryByMap(Map<String, Object> params);
	
	public TevglTraineeInfo selectObjectById(String id);
	
	/**
	 * 根据条件查询对象
	 * @param id
	 * @return
	 */
	TevglTraineeInfoVo selectTraineeVoById(Object id);
	
	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	Map<String, Object> selectObjectMapById(String id);
	
	/**
	 * 根据微信openid查询用户
	 * @param openid
	 * @return
	 */
	TevglTraineeInfo selectObjectByOpenId(String openid);
	
	/**
	 * 个人信息
	 * @param traineeId
	 * @return
	 */
	R viewTraineeInfoForManagementPanel(String traineeId);
	
	/**
	 * 查看学员信息
	 * @param traineeId
	 * @return
	 */
	R viewTraineeInfo(String traineeId);
	
	/**
	 * 学员列表
	 * @param params
	 * @return
	 */
	R listTrainee(Map<String, Object> params);
	
	/**
	 * 修改密码
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @param confimPwd 确保新密码与旧密码一致
	 * @param logUserId 当前要修改密码的人
	 * @return
	 */
	R updatePassword(String oldPwd, String newPwd, String confimPwd, String logUserId);
	
	/**
	 * 修改个人信息
	 * @param vo
	 * @return
	 */
	R updatePersonInfo(TevglTraineeInfoUpdateVo vo);
	
	/**
	 * 根据条件查询，不在任何班级里面的学员（学员：账号有效，且绑定了手机号码）
	 * @return
	 */
	List<TevglTraineeInfoVo> findTraineeListNotInClass(Map<String, Object> map);
	
	/**
	 * 根据条件，分页查询，不在任何班级里面的学员
	 * @param map
	 * @return
	 */
	R findTraineeListNotInClassWithPage(Map<String, Object> map);
	
}