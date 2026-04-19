package com.budaos.modules.evgl.pkg.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CbRoomUtils;
import com.budaos.modules.common.PkgPermissionUtils;
import com.budaos.modules.common.enums.BizCodeEnume;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.pkg.api.TevglPkgResService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgRes;
import com.budaos.modules.evgl.pkg.domain.TevglPkgResgroup;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgResMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgResgroupMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
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
 * <p> Title: 教学包资源</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglPkgResServiceImpl implements TevglPkgResService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgResServiceImpl.class);
	@Autowired
	private TevglPkgResMapper tevglPkgResMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private PkgPermissionUtils pkgPermissionUtils;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	
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
		List<TevglPkgRes> tevglPkgResList = tevglPkgResMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgResList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglPkgResList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglPkgResList = tevglPkgResMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgResList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglPkgResList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglPkgRes
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglPkgRes tevglPkgRes) throws BudaosException {
		tevglPkgRes.setResId(Identities.uuid());
		tevglPkgRes.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglPkgRes.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglPkgRes);
		tevglPkgResMapper.insert(tevglPkgRes);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglPkgRes
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglPkgRes tevglPkgRes) throws BudaosException {
	    tevglPkgRes.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglPkgRes.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglPkgRes);
		tevglPkgResMapper.update(tevglPkgRes);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglPkgResMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglPkgResMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglPkgResMapper.selectObjectById(id));
	}

	/**
	 * 新增资源---即将废弃
	 * @author huj
	 * @data 2019年8月13日	
	 * @param tevglPkgRes
	 * @return
	 */
	@Override
	public R saveResInfo( TevglPkgRes tevglPkgRes, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("参数loginUserId为空");
		}
		if (StrUtils.isEmpty(tevglPkgRes.getResgroupId())) {
			return R.error("参数resgroupId为空");
		}
		if (StrUtils.isEmpty(tevglPkgRes.getResContent())) {
			return R.error("内容不能为空");
		}
		// 获取资源分组信息
		TevglPkgResgroup tevglPkgResgroup = tevglPkgResgroupMapper.selectObjectById(tevglPkgRes.getResgroupId());
		if (tevglPkgResgroup == null) {
			return R.error("保存失败，请刷新后重试");
		}
		// 权限校验
		R r = pkgPermissionUtils.hasPermissionChapterV2(tevglPkgResgroup.getPkgId(), loginUserId, tevglPkgResgroup.getChapterId());
		if (!r.get("code").equals(0)) {
			return r;
		}
		Map<String, Object> map = new HashMap<>();
		// 理解:按照原型的话，貌似一个分组下只有一个资源
		map.put("resgroupId", tevglPkgRes.getResgroupId());
		List<TevglPkgRes> list = tevglPkgResMapper.selectListByMap(map);
		String resId = "";
		if (list != null && list.size() > 0) {
			TevglPkgRes pkgRes = list.get(0);
			resId = pkgRes.getResId();
			TevglPkgRes t = new TevglPkgRes();
			t.setResId(resId);
			t.setResContent(tevglPkgRes.getResContent());
			t.setUpdateTime(DateUtils.getNowTimeStamp());
			t.setUpdateUserId(tevglPkgRes.getCreateUserId());
			tevglPkgResMapper.update(t);
		} else {
			// 填充信息
			tevglPkgRes.setResId(Identities.uuid());
			tevglPkgRes.setPkgId(tevglPkgResgroup.getPkgId()); // 所属教学包
			tevglPkgRes.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
			tevglPkgRes.setState("Y");
			// 排序号处理
			map.clear();
			map.put("pkgId", tevglPkgRes.getPkgId());
			map.put("resgroupId", tevglPkgRes.getResgroupId());
			map.put("state", "Y");
			Integer sortNum = tevglPkgResMapper.getMaxSortNum(map);
			tevglPkgRes.setSortNum(sortNum);
			tevglPkgRes.setViewNum(0);
			// 保存并返回数据
			tevglPkgResMapper.insert(tevglPkgRes);
			resId = tevglPkgRes.getPkgId();
		}
		return R.ok().put(Constant.R_DATA, resId);
	}

	/**
	 * 修改资源
	 * @author huj
	 * @data 2019年8月13日	
	 * @param tevglPkgRes
	 * @return
	 */
	@Override
	public R editResInfo( TevglPkgRes tevglPkgRes, String loginUserId, String pkgId) throws BudaosException {
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(tevglPkgRes.getResId()) || StrUtils.isEmpty(pkgId)) {
			return R.error(BizCodeEnume.PARAM_MISSING.getCode(), BizCodeEnume.PARAM_MISSING.getMsg());
		}
		if (StrUtils.isNotEmpty(tevglPkgRes.getResContent())) {
			tevglPkgRes.setResContent(tevglPkgRes.getResContent().trim());	
		}
		if (StrUtils.isEmpty(tevglPkgRes.getResContent())) {
			return R.error("内容不能为空");
		}
		TevglPkgRes pkgRes = tevglPkgResMapper.selectObjectById(tevglPkgRes.getResId());
		if (pkgRes == null) {
			return R.error("无效的记录");
		}
		TevglPkgResgroup resgroup = tevglPkgResgroupMapper.selectObjectById(pkgRes.getResgroupId());
		if (resgroup == null) {
			return R.error("无效的记录");
		}
		// 权限校验
		R r = pkgPermissionUtils.hasPermissionChapterV2(pkgId, loginUserId, resgroup.getChapterId());
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 已发布的版本中，不允许再次编辑 pkg_id值有问题？
		/*TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(resgroup.getPkgId());
		if (tevglPkgInfo == null || "Y".equals(tevglPkgInfo.getReleaseStatus())) {
			return R.error(BizCodeEnume.EDIT_CONTENT_IS_NOT_ALLOWED.getCode(), BizCodeEnume.EDIT_CONTENT_IS_NOT_ALLOWED.getMsg());
		}*/
		// 实例化对象并填充
		TevglPkgRes t = new TevglPkgRes();
		t.setResId(tevglPkgRes.getResId());
		t.setResContent(tevglPkgRes.getResContent());
		t.setUpdateTime(DateUtils.getNowTimeStamp());
		// 保存数据
		tevglPkgResMapper.update(t);
		// 特殊情况更新章节数据
		TevglPkgResgroup pkgResgroup = tevglPkgResgroupMapper.selectObjectById(pkgRes.getResgroupId());
		if (pkgResgroup != null) {
			// 当分组为[课程内容时],同步更新章节表的内容
			if ("1".equals(pkgResgroup.getDictCode())
					&& StrUtils.isNotEmpty(pkgResgroup.getChapterId())) {
				TevglBookChapter chapter = new TevglBookChapter();
				chapter.setChapterId(pkgResgroup.getChapterId());
				chapter.setChapterContent(tevglPkgRes.getResContent());
				tevglBookChapterMapper.update(chapter);
			}
		}
		// 即时通讯
		/*TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectByPkgId(resgroup.getPkgId());
		if (tevglTchClassroom != null) {
			// 重新刷新章节与资源
			cbRoomUtils.sendIm(tevglTchClassroom.getCtId(), "reloadrescontent", "other", pkgResgroup.getChapterId());
		}*/
		return R.ok("保存成功");
	}

	/**
	 * 查看资源
	 * @author huj
	 * @data 2019年8月13日	
	 * @param params {'resgroupId':''}
	 * @return
	 */
	@Override
	public Map<String, Object> viewResInfo( Map<String, Object> params) {
		String resgroupId = (String) params.get("resgroupId");
		if (StrUtils.isEmpty(resgroupId)) {
			return R.error("参数resgroupId为空");
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("resId", "");
		result.put("resgroupId", "");
		result.put("resContent", "");
		// 根据章节和分组获取资源
		List<TevglPkgRes> list = tevglPkgResMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			TevglPkgRes tevglPkgRes = list.get(0);
			if (tevglPkgRes != null) {
				result.put("resId", tevglPkgRes.getResId());
				result.put("resgroupId", tevglPkgRes.getResgroupId());
				result.put("resContent", tevglPkgRes.getResContent());
			}
		}
		return R.ok().put(Constant.R_DATA, result);
	}

}
