package com.budaos.modules.evgl.pkg.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.PkgUtils;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.pkg.api.TevglBookpkgTeamService;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeam;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeamDetail;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglBookpkgTeamDetailMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglBookpkgTeamMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> Title: 资源共建权限</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglBookpkgTeamServiceImpl implements TevglBookpkgTeamService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglBookpkgTeamServiceImpl.class);
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglBookpkgTeamDetailMapper tevglBookpkgTeamDetailMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private PkgUtils pkgUtils;
	
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
		List<TevglBookpkgTeam> tevglBookpkgTeamList = tevglBookpkgTeamMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglBookpkgTeamList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglBookpkgTeamList = tevglBookpkgTeamMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglBookpkgTeamList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglBookpkgTeam
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglBookpkgTeam tevglBookpkgTeam) throws BudaosException {
		tevglBookpkgTeam.setTeamId(Identities.uuid());
		ValidatorUtils.check(tevglBookpkgTeam);
		tevglBookpkgTeamMapper.insert(tevglBookpkgTeam);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglBookpkgTeam
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglBookpkgTeam tevglBookpkgTeam) throws BudaosException {
	    ValidatorUtils.check(tevglBookpkgTeam);
		tevglBookpkgTeamMapper.update(tevglBookpkgTeam);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglBookpkgTeamMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglBookpkgTeamMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglBookpkgTeamMapper.selectObjectById(id));
	}
	
	@Override
	public List<TevglBookpkgTeam> selectListByMap(Map<String, Object> map) {
		return tevglBookpkgTeamMapper.selectListByMap(map);
	}

	

	/**
	 * 授权操作
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	public R authorization( JSONObject jsonObject, String loginUserId) throws BudaosException {
		String pkgId = jsonObject.getString("pkgId"); // 选择的教学
		String subjectId = jsonObject.getString("subjectId"); // 教学包对应的课程
		JSONArray teacherJsonArray = jsonObject.getJSONArray("traineeIds"); // 被授权的人
		JSONArray chapterJsonArray = jsonObject.getJSONArray("chapterIdList"); // 被授权的章节
		String subeditorTraineeId = jsonObject.getString("subeditorTraineeId"); // 副主编
		
		// 合法性校验
		R r = authorizationCheckIsPass(pkgId, subjectId, loginUserId);
		if ((Integer)r.get("code") != 0) {
			return r;
		}
		if (teacherJsonArray == null || teacherJsonArray.size() == 0) {
			return R.error("请选择需要授权的教师");
		}
		if (StrUtils.isNull(teacherJsonArray.get(0))) {
			return R.error("该教师没有网站账号，暂无法授权");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		if ("Y".equals(pkgInfo.getPrivateUse())) {
			return R.error("抱歉，您只能自己使用，而不能再次进行授权");
		}
		// 如果没有选择章节,则直接删除权限
		if (chapterJsonArray == null || chapterJsonArray.size() == 0) {
			// 找到此人的主权限
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", teacherJsonArray.get(0));
			params.put("pgkId", pkgId);
			List<TevglBookpkgTeam> tevglBookpkgTeamList = tevglBookpkgTeamMapper.selectListByMap(params);
			if (tevglBookpkgTeamList != null && tevglBookpkgTeamList.size() > 0) {
				String teamId = tevglBookpkgTeamList.get(0).getTeamId();
				// 找出这个人的章节权限明细
				params.clear();
				params.put("teamId", teamId);
				List<TevglBookpkgTeamDetail> teamDetailList = tevglBookpkgTeamDetailMapper.selectListByMap(params);
				// 先删除明细记录
				if (teamDetailList != null && teamDetailList.size() > 0) {
					teamDetailList.stream().forEach(a -> {
						tevglBookpkgTeamDetailMapper.delete(a.getDetailId());
					});
				}
				// 主表记录也删除
				List<String> list = tevglBookpkgTeamList.stream().map(a -> a.getTeamId()).collect(Collectors.toList());
				tevglBookpkgTeamMapper.deleteBatch(list.stream().toArray(String[]::new));
				//tevglBookpkgTeamMapper.delete(tevglBookpkgTeamList.get(0).getTeamId());
			}
			return R.ok("清空此人权限");
		}
		List<String> teamIdList = new ArrayList<>();
		// 先查出这些人的所有权限,控制不重复插入
		Map<String, Object> params = new HashMap<String, Object>();
		// 生成主表记录
		for (int i = 0; i < teacherJsonArray.size(); i++) {
			String userId = (String)teacherJsonArray.get(i);
			TevglBookpkgTeam t = new TevglBookpkgTeam();
			t.setTeamId(Identities.uuid());
			t.setPgkId(pkgId);
			t.setSubjectId(subjectId);
			t.setUserId(userId);
			t.setIsSubeditor(userId.equals(subeditorTraineeId) ? "Y" : "N"); // 是否为副主编Y/N
			// 不重复生成记录
			params.clear();
			params.put("userId", userId);
			params.put("pgkId", pkgId);
			List<TevglBookpkgTeam> tevglBookpkgTeamList = tevglBookpkgTeamMapper.selectListByMap(params);
			if (tevglBookpkgTeamList == null || tevglBookpkgTeamList.size() == 0) {
				tevglBookpkgTeamMapper.insert(t);
				teamIdList.add(t.getTeamId()+"_"+userId);	
			} else {
				teamIdList.add(tevglBookpkgTeamList.get(0).getTeamId()+"_"+userId);
				TevglBookpkgTeam te = new TevglBookpkgTeam();
				te.setTeamId(tevglBookpkgTeamList.get(0).getTeamId());
				te.setIsSubeditor(t.getIsSubeditor());
				tevglBookpkgTeamMapper.update(te);
			}
		}
		// 生成明细记录
		for (int i = 0; i < teamIdList.size(); i++) {
			String tId = teamIdList.get(i);
			int index = tId.lastIndexOf("_");
			String teamId = tId.substring(0, index);
			String userId = tId.substring(index+1, tId.length());
			// 找出这个人的章节权限明细
			params.clear();
			params.put("teamId", teamId);
			List<TevglBookpkgTeamDetail> teamDetailList = tevglBookpkgTeamDetailMapper.selectListByMap(params);
			// 先删除明细记录
			if (teamDetailList != null && teamDetailList.size() > 0) {
				teamDetailList.stream().forEach(a -> {
					tevglBookpkgTeamDetailMapper.delete(a.getDetailId());
				});
			}
			// 再重新生成
			List<TevglBookpkgTeamDetail> detailList = new ArrayList<TevglBookpkgTeamDetail>();
			for (int j = 0; j < chapterJsonArray.size(); j++) {
				String chapterId = (String)chapterJsonArray.get(j);
				TevglBookpkgTeamDetail detail = new TevglBookpkgTeamDetail();
				detail.setDetailId(Identities.uuid());
				detail.setChapterId(chapterId);
				detail.setTeamId(teamId);
				detail.setUserId(userId);
				detailList.add(detail);	
			}	
			// 保存数据
			if (detailList != null && detailList.size() > 0) {
				tevglBookpkgTeamDetailMapper.insertBatch(detailList);
			}
		}
		return R.ok("授权成功");
	}
	
	/**
	 * 合法性校验
	 * @param pkgId
	 * @param subjectId
	 * @param loginUserId
	 * @return
	 */
	private R authorizationCheckIsPass(String pkgId, String subjectId, String loginUserId) {
		if (StrUtils.isEmpty(subjectId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("参数loginUserId为空");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录，操作失败");
		}
		if (!"Y".equals(pkgInfo.getState())) {
			return R.error("此教学包已无效，操作失败");
		}
		// 没有权限，无法操作教学包
		boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(pkgInfo, loginUserId);
		if (!hasOperatingAuthorization) {
			return R.error("没有权限，无法操作教学包");
		}
		return R.ok();
	}

	/**
	 * 根据条件查询章节权限
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryAuthorization( JSONObject jsonObject, String loginUserId) {
		String subjectId = jsonObject.getString("subjectId");
		String pkgId = jsonObject.getString("pkgId");
		String userId = jsonObject.getString("userId");
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId) 
				|| StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(userId)) {
			return R.error("必传参数为空");
		}
		// 最终返回数据格式
		Map<String, Object> data = new HashMap<>();
		// 被选人有用的章节权限
		List<String> resultList = new ArrayList<>();
		// 此教学包其它人的章节权限
		List<String> otherList = new ArrayList<>();
		data.put("userChapterIds", resultList);
		data.put("otherUserChapterIds", otherList);
		data.put("isSubeditor", "N");
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的记录");
		}
		boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(pkgInfo, loginUserId);
		if (!hasOperatingAuthorization) {
			return R.error("没有权限，无法查看");
		}
		//List<Object> userIds = Arrays.asList(userId);
		// 该教学包与课堂，主表是否有授权记录
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", pkgId);
		params.put("subjectId", subjectId);
		params.put("userId", userId);
		List<TevglBookpkgTeam> pkgTeamList = tevglBookpkgTeamMapper.selectListByMap(params);
		// 如果被选用户没有主表权限
		if (pkgTeamList == null || pkgTeamList.size() == 0) {
			// 查出此课程所有章节
			params.clear();
			params.put("subjectId", subjectId);
			List<TevglBookChapter> chapterList = tevglBookChapterMapper.selectListByMapForSimple(params);
			// 找到此教学包已授权给哪些人
			params.clear();
			params.put("pkgId", pkgId);
			params.put("subjectId", subjectId);
			List<TevglBookpkgTeam> tevglBookpkgTeamList = tevglBookpkgTeamMapper.selectListByMap(params);
			List<TevglBookpkgTeamDetail> detailList = new ArrayList<>();
			if (tevglBookpkgTeamList != null && tevglBookpkgTeamList.size() > 0) {
				// 筛选出非当前被选用户
				List<String> teamIds = tevglBookpkgTeamList.stream().filter(a -> !userId.equals(a.getUserId())).map(a -> a.getTeamId()).collect(Collectors.toList());
				// 查出这些人已有的权限
				params.clear();
				params.put("teamIds", teamIds);
				detailList = tevglBookpkgTeamDetailMapper.selectListByMap(params);
			}
			if (chapterList == null || chapterList.size() == 0) {
				data.put("userChapterIds", resultList);
				data.put("otherUserChapterIds", otherList);
				data.put("isSubeditor", "N");
				return R.ok().put(Constant.R_DATA, data);
			}
			// 如果没有章节权限,则返回全部章节
			if (detailList == null || detailList.size() == 0) {
				//otherList = chapterList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
				data.put("userChapterIds", resultList);
				data.put("otherUserChapterIds", otherList);
				data.put("isSubeditor", "N");
				return R.ok().put(Constant.R_DATA, data);
			} else {
				// 如果有
				otherList = detailList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
				data.put("userChapterIds", resultList);
				data.put("otherUserChapterIds", otherList);
				data.put("isSubeditor", "N");
				return R.ok().put(Constant.R_DATA, data);
			}
		}
		// 被选用户所拥有的章节权限
		params.clear();
		params.put("teamId", pkgTeamList.get(0).getTeamId());
		params.put("userId", userId);
		List<Map<String, Object>> list = tevglBookpkgTeamDetailMapper.selectChapterIntersectionByUserId(params);
		list.stream().forEach(a -> {
			resultList.add(a.get("chapterId").toString());
		});
		// 处理返回非被用户的章节
		params.clear();
		params.put("pkgId", pkgId);
		params.put("subjectId", subjectId);
		List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(params);
		List<TevglBookpkgTeam> collect = teamList.stream().filter(a -> !a.getUserId().equals(userId)).collect(Collectors.toList());
		log.debug("非被选人外:" + collect.size());
		if (collect != null && collect.size() > 0) {
			List<String> teamIds = collect.stream().map(a -> a.getTeamId()).collect(Collectors.toList());
			params.clear();
			params.put("teamIds", teamIds);
			List<TevglBookpkgTeamDetail> detailList = tevglBookpkgTeamDetailMapper.selectListByMap(params);
			otherList = detailList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
		} else {
			
		}
		// 由于课程节点并没有保存在数据
		if (resultList != null && resultList.size() > 0) {
			resultList.add(subjectId);
		}
		data.put("userChapterIds", resultList);
		data.put("otherUserChapterIds", otherList);
		data.put("isSubeditor", pkgTeamList.get(0).getIsSubeditor());
		return R.ok().put(Constant.R_DATA, data);
	}


	/**
	 * 验证教学包共建教材的某章节是否操作权限
	 *
	 * @param pkgId     教学包id
	 * @param chapterId 需要验证的章节
	 * @param traineeId 当前登录用户id
	 * @return 返回true表示有操作权限
	 */
	@Override
	public boolean checkNodePermission(String pkgId, String chapterId, String traineeId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(traineeId)) {
			return false;
		}
		return this.checkNodePermission(pkgId, Arrays.asList(chapterId), traineeId);
	}

	/**
	 * 验证教学包共建教材的章节是否操作权限
	 *
	 * @param pkgId         教学包id
	 * @param chapterIdList 需要验证的章节，注意只要有一个不满足，则任务没有操作权限
	 * @param traineeId     当前登录用户id
	 * @return 返回true表示有操作权限
	 */
	@Override
	public boolean checkNodePermission(String pkgId, List<String> chapterIdList, String traineeId) {
		if (StrUtils.isEmpty(pkgId) || chapterIdList == null || chapterIdList.size() == 0 || StrUtils.isEmpty(traineeId)) {
			return false;
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return false;
		}
		// 先查询教学包对应教材的章节
		List<String> allChapterIdList = tevglBookChapterMapper.selectChapterIdList(tevglPkgInfo.getSubjectId());
		for (int i = 0; i < chapterIdList.size(); i++) {
			String s = chapterIdList.get(i);
			if (!allChapterIdList.stream().anyMatch(a -> a.equals(s))) {
				return false;
			}
		}
		// 如果是教学包创建者或接管者，直接返回true，认定为有操作权限
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && traineeId.equals(tevglPkgInfo.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && traineeId.equals(tevglPkgInfo.getReceiverUserId());
		if (isCreator || isReceiver) {
			return true;
		}
		// 不然就是共建者了，共建者需要校验具体的章节
		Map<String, Object> map = new HashMap<>();
		map.put("pgkId", pkgId);
		map.put("userId", traineeId);
		List<String> teamIds = tevglBookpkgTeamMapper.selectTeamIdListByMap(map);
		if (teamIds == null || teamIds.size() == 0) {
			return false;
		}
		map.clear();
		map.put("teamIds", teamIds);
		map.put("chapterIds", chapterIdList);
		map.put("userId", traineeId);
		List<String> detailIds = tevglBookpkgTeamDetailMapper.selectDetailIdListByMap(map);
		if (detailIds == null || detailIds.size() == 0 || detailIds.size() < chapterIdList.size()) {
			return false;
		}
		return true;
	}
}
