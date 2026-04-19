package com.budaos.modules.evgl.empirical.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalEmpiricalValueGetType;
import com.budaos.modules.common.enums.EmpiricalValueEnum;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.empirical.api.TevglEmpiricalLogAudioService;
import com.budaos.modules.evgl.empirical.domain.TevglEmpiricalLogAudio;
import com.budaos.modules.evgl.empirical.domain.TevglEmpiricalSetting;
import com.budaos.modules.evgl.empirical.persistence.TevglEmpiricalLogAudioMapper;
import com.budaos.modules.evgl.empirical.persistence.TevglEmpiricalSettingMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeEmpiricalValueLog;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeEmpiricalValueLogMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class TevglEmpiricalLogAudioServiceImpl implements TevglEmpiricalLogAudioService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglEmpiricalLogAudioServiceImpl.class);
	@Autowired
	private TevglEmpiricalLogAudioMapper tevglEmpiricalLogAudioMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TevglEmpiricalSettingMapper tevglEmpiricalSettingMapper;
	@Autowired
	private TevglTraineeEmpiricalValueLogMapper tevglTraineeEmpiricalValueLogMapper;
	
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
		List<TevglEmpiricalLogAudio> tevglEmpiricalLogAudioList = tevglEmpiricalLogAudioMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglEmpiricalLogAudioList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglEmpiricalLogAudioList = tevglEmpiricalLogAudioMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglEmpiricalLogAudioList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglEmpiricalLogAudio
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglEmpiricalLogAudio tevglEmpiricalLogAudio) throws BudaosException {
		tevglEmpiricalLogAudio.setCbId(Identities.uuid());
		ValidatorUtils.check(tevglEmpiricalLogAudio);
		tevglEmpiricalLogAudioMapper.insert(tevglEmpiricalLogAudio);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglEmpiricalLogAudio
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglEmpiricalLogAudio tevglEmpiricalLogAudio) throws BudaosException {
	    ValidatorUtils.check(tevglEmpiricalLogAudio);
		tevglEmpiricalLogAudioMapper.update(tevglEmpiricalLogAudio);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglEmpiricalLogAudioMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglEmpiricalLogAudioMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglEmpiricalLogAudioMapper.selectObjectById(id));
	}
	
	/**
	 * 查看音频时，记录并获得经验值
	 * @param ctId
	 * @param pkgId
	 * @param subjectId
	 * @param chapterId
	 * @param audioId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewAudio(String ctId, String pkgId, String subjectId, String chapterId, String audioId,
			String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId)
				|| StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(audioId) || StrUtils.isEmpty(loginUserId)) {
			return R.ok();
		}
		// 课堂创建者无需记录
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom != null && loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
			return R.ok();
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		TevglPkgInfo refPkgInfo = null;
		if (tevglPkgInfo != null) {
			refPkgInfo = tevglPkgInfoMapper.selectObjectById(tevglPkgInfo.getRefPkgId());
		}
		TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(subjectId);
		TevglBookChapter tevglBookChapter = tevglBookChapterMapper.selectObjectById(chapterId);
		// 是否已经记录过
		Map<String, Object> parmas = new HashMap<String, Object>();
		parmas.put("ctId", ctId);
		parmas.put("pkgId", pkgId);
		parmas.put("subjectId", subjectId);
		parmas.put("chapterId", chapterId);
		parmas.put("audioId", audioId);
		parmas.put("traineeId", loginUserId);
		List<TevglEmpiricalLogAudio> list = tevglEmpiricalLogAudioMapper.selectListByMap(parmas);
		// 已经得到过直接返回
		if (list != null && list.size() > 0) {
			return R.ok();
		}
		// 获取经验值规则
		parmas.clear();
		parmas.put("ctId", ctId);
		parmas.put("dictCode", "3");
		List<TevglEmpiricalSetting> tevglEmpiricalSettingList = tevglEmpiricalSettingMapper.selectListByMap(parmas);
		Integer empiricalValue = null;
		if (tevglEmpiricalSettingList != null && tevglEmpiricalSettingList.size() > 0) {
			BigDecimal value = tevglEmpiricalSettingList.get(0).getValue();
			if (value != null) {
				empiricalValue = value.intValue();
			}
		} else {
			empiricalValue = EmpiricalValueEnum.TYPE_3.getValue().intValue();
		}
		// 入库
		TevglEmpiricalLogAudio t = new TevglEmpiricalLogAudio();
		t.setCbId(Identities.uuid());
		t.setTraineeId(loginUserId);
		t.setCtId(ctId);
		t.setPkgId(pkgId);
		t.setSubjectId(subjectId);
		t.setChapterId(chapterId);
		t.setAudioId(audioId);
		t.setEmpiricalValue(empiricalValue);
		t.setCreateTime(DateUtils.getNowTimeStamp());
		tevglEmpiricalLogAudioMapper.insert(t);
		// 经验值记录表插入记录
		TevglTraineeEmpiricalValueLog log = new TevglTraineeEmpiricalValueLog();
		log.setEvId(Identities.uuid());
		log.setType(GlobalEmpiricalValueGetType.VIEW_AUDIO_11);
		log.setTraineeId(loginUserId);
		log.setCtId(ctId);
		log.setEmpiricalValue(empiricalValue);
		log.setState("Y");
		log.setCreateTime(DateUtils.getNowTimeStamp());
		String msg = "";
		String classroomName = tevglTchClassroom == null ? "" : "在课堂【" + tevglTchClassroom.getName() + "】的";
		String subjectName = tevglBookSubject == null ? "" : tevglBookSubject.getSubjectName();
		String version = refPkgInfo == null ? "" : refPkgInfo.getPkgVersion();
		if (StrUtils.isNotEmpty(version)) {
			subjectName = "教材【" + subjectName + "  " + version +"】中，";
		}
		String chapterName = tevglBookChapter == null ? "" : "查阅音频[" + tevglBookChapter.getChapterName() + "]，获得" + empiricalValue + "经验值";
		msg = classroomName + subjectName + chapterName;
		log.setMsg(msg);
		tevglTraineeEmpiricalValueLogMapper.insert(log);
		return R.ok();
	}
}
