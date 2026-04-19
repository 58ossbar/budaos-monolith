package com.budaos.modules.evgl.tch.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.enums.BizCodeEnume;
import com.budaos.modules.evgl.book.vo.RoomBookVo;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomSbService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomSb;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomSbMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class TevglTchClassroomSbServiceImpl implements TevglTchClassroomSbService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassroomSbServiceImpl.class);
	@Autowired
	private TevglTchClassroomSbMapper tevglTchClassroomSbMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@Override
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTchClassroomSb> tevglTchClassroomSbList = tevglTchClassroomSbMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomSbList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@Override
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTchClassroomSbList = tevglTchClassroomSbMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomSbList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTchClassroomSb
	 * @throws BudaosException
	 */
	@Override
	@SysLog(value="新增")
	public R save(TevglTchClassroomSb tevglTchClassroomSb) throws BudaosException {
		tevglTchClassroomSb.setSbId(Identities.uuid());
		ValidatorUtils.check(tevglTchClassroomSb);
		tevglTchClassroomSbMapper.insert(tevglTchClassroomSb);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchClassroomSb
	 * @throws BudaosException
	 */
	@Override
	@SysLog(value="修改")
	public R update(TevglTchClassroomSb tevglTchClassroomSb) throws BudaosException {
	    ValidatorUtils.check(tevglTchClassroomSb);
		tevglTchClassroomSbMapper.update(tevglTchClassroomSb);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@Override
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglTchClassroomSbMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@Override
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglTchClassroomSbMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@Override
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglTchClassroomSbMapper.selectObjectById(id));
	}

	@Override
	public R saveExtraBooksRelation(TevglTchClassroomSb sb, String traineeId) {
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(sb.getCtId());
		if (tevglTchClassroom == null) {
			return R.error("课堂已不存在");
		}
		/*if (!tevglTchClassroom.getPkgId().equals(sb.getCtPkgId())) {
			return R.error(BizCodeEnume.PARAM_INVALID.getCode(), BizCodeEnume.PARAM_INVALID.getMsg());
		}*/
		boolean isCreator = StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId()) && tevglTchClassroom.getCreateUserId().equals(traineeId);
		boolean isReceiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && tevglTchClassroom.getReceiverUserId().equals(traineeId);
		if (!isCreator && !isReceiver) {
			return R.error(BizCodeEnume.WITHOUT_PACKAGE.getCode(), BizCodeEnume.WITHOUT_PERMISSION.getMsg());
		}
		// 先删除
		tevglTchClassroomSbMapper.deleteByCtId(sb.getCtId());
		// 再重新生成
		List<TevglTchClassroomSb> insertList = new ArrayList<>();
		sb.getSubjectList().stream().forEach(item -> {
			TevglTchClassroomSb t = new TevglTchClassroomSb();
			t.setSbId(Identities.uuid());
			t.setCtId(sb.getCtId());
			t.setCtPkgId(tevglTchClassroom.getPkgId());
			t.setSubjectId(item.get("subjectId").toString());
			t.setPkgId(item.get("pkgId").toString());
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setCreateUserId(traineeId);
			insertList.add(t);
		});
		if (insertList.size() > 0) {
			tevglTchClassroomSbMapper.insertBatch(insertList);
		}
		return R.ok("设置成功");
	}

	@Override
	public R queryExtraBooks(Map<String, Object> params, String traineeId) {
		if (StrUtils.isNull(params.get("ctId")) || StrUtils.isEmpty(traineeId)) {
			return R.error(BizCodeEnume.PARAM_MISSING.getCode(), BizCodeEnume.PARAM_MISSING.getMsg());
		}
		params.put("loginUserId", traineeId);
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<RoomBookVo> bookList = tevglTchClassroomSbMapper.queryExtraBooks(query);
		//List<RoomBookVo> bookList = tevglTchClassroomSbMapper.queryExtraBooksByUnionAll(query);
		PageUtils pageUtil = new PageUtils(bookList,query.getPage(),query.getLimit());
		// 已选择好的教材
		List<String> subjectIdList = tevglTchClassroomSbMapper.findSubjectIdList(params.get("ctId").toString());
		return R.ok().put(Constant.R_DATA, pageUtil).put("subjectIdList", subjectIdList);
	}

	@Override
	public R findExtraBooks(String ctId, String traineeId) {
		// 找到当前课堂正在使用的教材
		RoomBookVo subject = tevglTchClassroomSbMapper.findSubjectByCtId(ctId);
		if (subject != null) {
			// 课堂详情页面中，左上区域的版本号展示的是所引用的教学包版本号
			TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(subject.getRefPkgId());
			if (tevglPkgInfo != null) {
				subject.setPkgVersion(tevglPkgInfo.getPkgVersion());
			}
		}
		// 找到课堂关联的教材
		List<RoomBookVo> subjectList = tevglTchClassroomSbMapper.findSubjectList(ctId);
		if (subjectList != null && subject != null) {
			subjectList.add(0, subject);
		}
		return R.ok().put(Constant.R_DATA, subjectList);
	}
}
