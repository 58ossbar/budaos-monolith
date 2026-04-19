package com.budaos.modules.evgl.book.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.domain.TevglBookRegular;

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

public interface TevglBookRegularService extends IBaseService<TevglBookRegular>{
	
	/**
	 * 获取课程体系考核规则树
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getSubjectRegularTree(Map<String, Object> params);
	
	/**
	 * 右键新增规则节点
	 * @param tevglBookRegular
	 * @return
	 */
	R saveRegular(TevglBookRegular tevglBookRegular);
	
	/**
	 * 点击课程将显示出该课程对应的预览规则页面
	 * @param subjectId
	 * @return
	 */
	R viewRegular(String subjectId);
	
	/**
	 * 新增修改基本信息
	 * @param tevglBookRegular
	 * @param type
	 * @return
	 */
	R saveOrUpdate(TevglBookRegular tevglBookRegular, String type);
	
	/**
	 * 总规则下的具体规则的分数最多还能输入多少
	 * @param parentId
	 * @return
	 */
	R queryRemainingScore(String parentId);
	
	/**
	 * 复制粘贴
	 * @param copySubjectId
	 * @param pasteSubjectId
	 * @return
	 */
	R paste(String copySubjectId, String pasteSubjectId);
	
	/**
	 * 删除规则
	 * @param regularId
	 * @param type
	 * @return
	 */
	R removeRegular(String regularId, String type);
}