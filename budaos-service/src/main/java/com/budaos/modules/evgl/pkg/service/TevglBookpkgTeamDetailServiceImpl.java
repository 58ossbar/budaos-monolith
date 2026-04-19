package com.budaos.modules.evgl.pkg.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.api.TevglBookpkgTeamDetailService;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeam;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeamDetail;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglBookpkgTeamDetailMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglBookpkgTeamMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.tch.persistence.TevglTchTeacherMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: 资源共建权限明细</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglBookpkgTeamDetailServiceImpl implements TevglBookpkgTeamDetailService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglBookpkgTeamDetailServiceImpl.class);
	@Autowired
	private TevglBookpkgTeamDetailMapper tevglBookpkgTeamDetailMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglTchTeacherMapper tevglTchTeacherMapper;
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	 
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
		List<TevglBookpkgTeamDetail> tevglBookpkgTeamDetailList = tevglBookpkgTeamDetailMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglBookpkgTeamDetailList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglBookpkgTeamDetailList = tevglBookpkgTeamDetailMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglBookpkgTeamDetailList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglBookpkgTeamDetail
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglBookpkgTeamDetail tevglBookpkgTeamDetail) throws BudaosException {
		tevglBookpkgTeamDetail.setDetailId(Identities.uuid());
		ValidatorUtils.check(tevglBookpkgTeamDetail);
		tevglBookpkgTeamDetailMapper.insert(tevglBookpkgTeamDetail);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglBookpkgTeamDetail
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglBookpkgTeamDetail tevglBookpkgTeamDetail) throws BudaosException {
	    ValidatorUtils.check(tevglBookpkgTeamDetail);
		tevglBookpkgTeamDetailMapper.update(tevglBookpkgTeamDetail);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglBookpkgTeamDetailMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglBookpkgTeamDetailMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglBookpkgTeamDetailMapper.selectObjectById(id));
	}

	/**
	 * 单独对某个人授权
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	public R authorizationAlone( JSONObject jsonObject, String loginUserId) throws BudaosException {
		String pkgId = jsonObject.getString("pkgId");
		String teacherId = jsonObject.getString("teacherId");
		JSONArray jsonArray = jsonObject.getJSONArray("chapterIdList");
		// 合法性校验
		if (jsonArray == null || jsonArray.size() == 0) {
			return R.error("请选择授权的章节");
		}
		R r = checkIsPass(pkgId, teacherId, loginUserId);
		if ((Integer)r.get("code") != 0) {
			return r;
		}
		String subjectId = (String) r.get("subjectId");
		// 先判断主表是否有记录,若没有则生成
		String teamId = null;
		Map<String, Object> ps = new HashMap<>();
		ps.put("userId", teacherId);
		ps.put("pkgId", pkgId);
		List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(ps);
		if (list == null || list.size() == 0) {
			TevglBookpkgTeam t = new TevglBookpkgTeam();
			t.setTeamId(Identities.uuid());
			t.setPgkId(pkgId);
			t.setSubjectId(subjectId);
			t.setUserId(teacherId);
			tevglBookpkgTeamMapper.insert(t);
			teamId = t.getTeamId();
		} else {
			teamId = list.get(0).getTeamId();
		}
		for (int i = 0; i < jsonArray.size(); i++) {
			String chapterId = jsonArray.getString(i);
			TevglBookpkgTeamDetail t = new TevglBookpkgTeamDetail();
			t.setDetailId(Identities.uuid());
			t.setTeamId(teamId);
			t.setChapterId(chapterId);
			t.setUserId(teacherId);
			tevglBookpkgTeamDetailMapper.insert(t);
		}
		return R.ok("授权成功");
	}
	
	/**
	 * 合法性校验
	 * @param pkgId 教学包主键
	 * @param teacherId 被授权的人
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	private R checkIsPass(String pkgId, String teacherId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId)) {
			return R.error("参数pkgId为空");
		}
		if (StrUtils.isEmpty(teacherId)) {
			return R.error("参数teacherId为空");
		}
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("参数loginUserId为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		if (!loginUserId.equals(pkgInfo.getCreateUserId())) {
			return R.error("非法操作，无权限，操作失败");
		}
		if (!"Y".equals(pkgInfo.getState())) {
			return R.error("教学包已无效");
		}
		TevglTchTeacher tchTeacher = tevglTchTeacherMapper.selectObjectById(teacherId);
		if (tchTeacher == null) {
			return R.error("非法用户，授权失败");
		}
		return R.ok().put("subjectId", pkgInfo.getSubjectId());
	}

	@Override
	public List<TevglBookpkgTeamDetail> selectListByMap(Map<String, Object> params) {
		return tevglBookpkgTeamDetailMapper.selectListByMap(params);
	}
}
