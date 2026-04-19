package com.budaos.modules.common;

import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.tch.persistence.TevglTchTeacherMapper;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class RoleUtils {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TevglTchTeacherMapper tevglTchTeacherMapper;
	
	/**
	 * 验证是否教师（根据教师表中主键teacher_id字段的值是否等于传入参数traineeId的值）
	 * @param traineeId
	 * @return {@link Boolean} 返回true表示是教师，false表示不是教师或教师账号无效了
	 * @apiNote 说明：新增教师时，控制了以学员id作为教师表主键id的值
	 */
	public boolean checkIsTeacher(String traineeId) {
		if (StrUtils.isEmpty(traineeId)) {
			return false;
		}
		TevglTchTeacher tchTeacher = tevglTchTeacherMapper.selectObjectByTraineeId(traineeId);
		if (tchTeacher == null) {
			return false;
		}
		if (!"Y".equals(tchTeacher.getState())) {
			log.info("【"+tchTeacher.getTeacherName()+"】"+"账号状态已无效");
			return false;
		}
		return true;
	}
	
}
