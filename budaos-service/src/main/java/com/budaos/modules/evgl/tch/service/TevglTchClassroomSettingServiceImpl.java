package com.budaos.modules.evgl.tch.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalRoomSetting;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomSettingService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomSetting;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomSettingMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

@Service
public class TevglTchClassroomSettingServiceImpl implements TevglTchClassroomSettingService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassroomSettingServiceImpl.class);
	@Autowired
	private TevglTchClassroomSettingMapper tevglTchClassroomSettingMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTchClassroomSetting> tevglTchClassroomSettingList = tevglTchClassroomSettingMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomSettingList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTchClassroomSettingList = tevglTchClassroomSettingMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomSettingList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTchClassroomSetting
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglTchClassroomSetting tevglTchClassroomSetting) throws BudaosException {
		tevglTchClassroomSetting.setSettingId(Identities.uuid());
		ValidatorUtils.check(tevglTchClassroomSetting);
		tevglTchClassroomSettingMapper.insert(tevglTchClassroomSetting);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchClassroomSetting
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglTchClassroomSetting tevglTchClassroomSetting) throws BudaosException {
	    ValidatorUtils.check(tevglTchClassroomSetting);
		tevglTchClassroomSettingMapper.update(tevglTchClassroomSetting);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglTchClassroomSettingMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglTchClassroomSettingMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglTchClassroomSettingMapper.selectObjectById(id));
	}

	/**
	 * 保存设置
	 * @param ctId
	 * @param traineeId
	 * @param radio1
	 * @param radio2 up标识升序，down标识降序
	 * @return
	 */
	@Override
	public R saveSetting(String ctId, String traineeId, String radio1, String radio2) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(traineeId)) {
			return R.error("必传参数为空");
		}
		if (StrUtils.isEmpty(radio1) || "undefined".equals(radio1) || "NaN".equals(radio1)) {
			return R.error("请选择排序条件");
		}
		if (StrUtils.isEmpty(radio2) || "undefined".equals(radio2)) {
			return R.error("请选择升序还是降序");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null || !tevglTchClassroom.getCreateUserId().equals(traineeId)) {
			return R.ok("无法设置");
		}
		radio2 = "up".equals(radio2) ? "asc" : "desc";
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("modules", GlobalRoomSetting.MDULES_CLASSROOM_TRAINEE);
		List<TevglTchClassroomSetting> list = tevglTchClassroomSettingMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
			TevglTchClassroomSetting t = new TevglTchClassroomSetting();
			t.setSettingId(Identities.uuid());
			t.setCtId(ctId);
			t.setModule(GlobalRoomSetting.MDULES_CLASSROOM_TRAINEE);
			t.setSidx(radio1);
			t.setOrder(radio2);
			t.setCreateTime(DateUtils.getNowTimeStamp());
			tevglTchClassroomSettingMapper.insert(t);
		} else {
			TevglTchClassroomSetting t = list.get(0);
			t.setSidx(radio1);
			t.setOrder(radio2);
			tevglTchClassroomSettingMapper.update(t);
		}
		return R.ok("保存成功");
	}

	@Override
	public R viewSetting(String ctId) {
		if (StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("modules", GlobalRoomSetting.MDULES_CLASSROOM_TRAINEE);
		List<TevglTchClassroomSetting> classroomTraineeSettingList = tevglTchClassroomSettingMapper.selectListByMap(params);
		Map<String, Object> data = new HashMap<>();
		if (classroomTraineeSettingList.size() > 0) {
			data.put("radio1", classroomTraineeSettingList.get(0).getSidx());
			data.put("radio2", "asc".equals(classroomTraineeSettingList.get(0).getOrder()) ? "up" : "down");	
		}
		return R.ok().put(Constant.R_DATA, data);
	}
}
