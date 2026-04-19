package com.budaos.modules.evgl.pkg.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalActivity;
import com.budaos.modules.common.PkgPermissionUtils;
import com.budaos.modules.common.PkgUtils;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.pkg.api.TevglPkgResgroupService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.domain.TevglPkgRes;
import com.budaos.modules.evgl.pkg.domain.TevglPkgResgroup;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgResMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgResgroupMapper;
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
import java.util.stream.Collectors;

/**
 * <p> Title: 教学包资源分组目录</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglPkgResgroupServiceImpl implements TevglPkgResgroupService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgResgroupServiceImpl.class);
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglPkgResMapper tevglPkgResMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private PkgPermissionUtils pkgPermissionUtils;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	
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
		List<TevglPkgResgroup> tevglPkgResgroupList = tevglPkgResgroupMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgResgroupList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglPkgResgroupList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglPkgResgroupList = tevglPkgResgroupMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgResgroupList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglPkgResgroupList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglPkgResgroup
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglPkgResgroup tevglPkgResgroup) throws BudaosException {
		tevglPkgResgroup.setResgroupId(Identities.uuid());
		tevglPkgResgroup.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglPkgResgroup.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglPkgResgroup);
		tevglPkgResgroupMapper.insert(tevglPkgResgroup);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglPkgResgroup
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglPkgResgroup tevglPkgResgroup) throws BudaosException {
	    tevglPkgResgroup.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglPkgResgroup.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglPkgResgroup);
		tevglPkgResgroupMapper.update(tevglPkgResgroup);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglPkgResgroupMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglPkgResgroupMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglPkgResgroupMapper.selectObjectById(id));
	}
	
	@Override
	public List<TevglPkgResgroup> selectListByMap(Map<String, Object> params) {
		return tevglPkgResgroupMapper.selectListByMap(params);
	}
	
	/**
	 * <p>获取具有层级机构的教学包资源分组目录</p>
	 * @author huj
	 * @data 2019年7月6日
	 * @param pkgId 教学包ID
	 * @return
	 */
	public List<TevglPkgResgroup> getPkgResgroup( String pkgId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sidx", "sort_num");
		map.put("order", "asc");
		map.put("state", "Y");
		map.put("pkgId", pkgId);
		List<TevglPkgResgroup> list = tevglPkgResgroupMapper.selectListByMap(map);
		//return build("-1", list, 0);
		return build2("-1", list);
	}
	
	/**
	 * <p>递归构建树形数据</p>
	 * @author huj
	 * @data 2019年7月8日
	 * @param parentId
	 * @param allList
	 * @return
	 */
	private List<TevglPkgResgroup> build2(String parentId, List<TevglPkgResgroup> allList){
		if (parentId == null || "".equals(parentId)) {
			return null;
		}
		if (allList == null || allList.size() == 0) {
			return null;
		}
		List<TevglPkgResgroup> parentNode = allList.stream().filter(a -> a.getParentId().equals(parentId)).collect(Collectors.toList());
		if (parentNode.size() > 0) {
			parentNode.forEach(a -> {
				// 遍历教学包资源目录，根据教学包ID,教学包资源分组目录ID,查出对应目录下的教学包资源
				Map<String, Object> map = new HashMap<>();
				map.put("pkgId", a.getPkgId());
				map.put("resgroupId", a.getResgroupId());
				List<TevglPkgRes> tevglPkgResList = tevglPkgResMapper.selectListByMap(map);
				a.setTevglPkgResList(tevglPkgResList);
				// 递归遍历
				a.setChildren(build2(a.getResgroupId(), allList));
			});
		}
		return parentNode;
	}

	/**
	 * <p>查询资源分组</p>  
	 * @author huj
	 * @data 2019年12月20日	
	 * @param params
	 * @return
	 */
	@Override
	@SysLog(value="查询资源分组")
	public R queryResGroup( Map<String, Object> params) throws BudaosException {
		String sidx = (String) params.get("sidx");
		String order = (String) params.get("order");
		if (StrUtils.isEmpty(sidx) || "null".equals(sidx)) {
			params.put("sidx", "sort_num");
		}
		if (StrUtils.isEmpty(order) || "null".equals(order)) {
			params.put("order", "asc");
		}
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglPkgResgroup> tevglPkgResgroupList = tevglPkgResgroupMapper.selectListByMap(query);
		convertUtil.convertDict(tevglPkgResgroupList, "resType", "resourceGroup"); // 字典转换
		convertUtil.convertUserId2RealName(tevglPkgResgroupList, "createUserId", "updateUserId");
		List<Map<String,Object>> list = tevglPkgResgroupList.stream().map(this::converToSimpleData).collect(Collectors.toList());
		PageUtils pageUtil = new PageUtils(list,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * <p>新增资源分组</p>  
	 * @author huj
	 * @data 2019年12月19日
	 * @return
	 */
	@Override
	@SysLog(value="新增资源分组")
	public R saveResGroup( JSONObject jsonObject, String loginUserId) throws BudaosException {
		String pkgId = jsonObject.getString("pkgId");
		String subjectId = jsonObject.getString("subjectId");
		String chapterId = jsonObject.getString("chapterId");
		// 选择的默认字典资源分组,为2时，flag_activity字段值存Y
		JSONArray dictList = jsonObject.getJSONArray("dictList");
		JSONArray resgroupNameList = jsonObject.getJSONArray("resgroupNameList"); // 手动输入的多个分组名称
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(subjectId) 
				|| StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 如果没有选择默认分组
		if (dictList == null || dictList.size() == 0) {
			if (resgroupNameList == null || resgroupNameList.size() == 0) {
				return R.error("请填写名称");
			}
			for (int i = 0; i < resgroupNameList.size(); i++) {
				if (StrUtils.isEmpty(resgroupNameList.getString(i))) {
					return R.error("第"+(i+1)+"项的名称不能为空");
				}
			}
		} else {
			if (resgroupNameList != null && resgroupNameList.size() > 0) {
				for (int i = 0; i < resgroupNameList.size(); i++) {
					if (StrUtils.isEmpty(resgroupNameList.getString(i))) {
						return R.error("第"+(i+1)+"项名称不能为空");
					}
				}
			}
		}
		TevglBookChapter chapterInfo = tevglBookChapterMapper.selectObjectById(chapterId);
		if (chapterInfo == null) {
			return R.error("无效的章节，或许选则的不是章节节点");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的教学包记录");
		}
		subjectId = pkgInfo.getSubjectId();
		// 权限校验
		R r = pkgPermissionUtils.hasPermissionChapterV2(pkgId, loginUserId, chapterId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		String msg = "";
		// 注意点
		String refPkgId = pkgInfo.getPkgId();
		/*if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
			refPkgId = pkgInfo.getRefPkgId();
		}*/
		// 查询条件
		Map<String, Object> map = new HashMap<>();
		// 同教学包同章节不允许出现相同分组名称
	    map.put("createUserId", loginUserId);
	    map.put("pkgId", refPkgId);
	    map.put("chapterId", chapterId);
	    map.put("groupType", GlobalActivity.ACTIVITY_GROUP_TYPE_1);
	    List<TevglPkgResgroup> list = tevglPkgResgroupMapper.selectListByMap(map);	    
	    if (list.size() >= 10) {
	    	return R.error("分组个数已达限制了，无法继续添加");
	    }
		for (int i = 0; i < resgroupNameList.size(); i++) {
			String inputResgroupName = resgroupNameList.getString(i);
			for (TevglPkgResgroup t : list) {
				if (inputResgroupName.equals(t.getResgroupName())) {
					return R.error("操作失败，您刚才录入的，第"+(i+1)+"项重名，同章节不允许出现相同分组名称");
				}
			}
		}
		String resId = "";
		int index = 0;
		if (dictList != null && dictList.size() > 0) {
			for (int i = 0; i < dictList.size(); i++) {
				JSONObject dict = dictList.getJSONObject(i);
				String dictCode = dict.getString("dictCode");
				String resgroupName = dict.getString("dictValue");
				// 字典来的默认分组不重复添加
				if (!list.stream().anyMatch(a -> a.getResgroupName().equals(resgroupName))) {
					resId = doFillGroupInfo(refPkgId, subjectId, chapterId, resgroupName, loginUserId, dictCode, map);
					index ++;
				} else {
					msg += "["+resgroupName+"]，";
				}
			}
			if (StrUtils.isNotEmpty(msg)) {
				msg += "已重名，忽略保存。";
			}
		}
		// 手动输入了分组名称也保存
		
		int length = 0;
		int limitSize = 10 - (list.size() + index);
		// 如果输入的分组少于限制
		if (resgroupNameList.size() <= limitSize) {
			length = resgroupNameList.size();
		} else {
			length = limitSize;
			msg = "，分组个数超过限制，超过的分组已取消保存";
		}
		for (int i = 0; i < length; i++) {
			resId = doFillGroupInfo(refPkgId, subjectId, chapterId, resgroupNameList.get(i).toString(), loginUserId, null, map);
		}
		// 返回资源主键
		return R.ok("分组新建成功" + msg).put(Constant.R_DATA, resId);
	}
	
	/**
	 * 
	 * @param refPkgId 教学包主键
	 * @param subjectId 教学包对应的课程
	 * @param chapterId 章节
	 * @param resgroupName 分组名称
	 * @param loginUserId 登录用户
	 * @param params 查询条件
	 * @return
	 */
	private String doFillGroupInfo(String refPkgId, String subjectId, String chapterId, 
			String resgroupName, String loginUserId, String dictCode, Map<String, Object> params) {
		// 填充信息
		TevglPkgResgroup tevglPkgResgroup = new TevglPkgResgroup();
		tevglPkgResgroup.setResgroupId(Identities.uuid()); // 主键
		tevglPkgResgroup.setPkgId(refPkgId);
		tevglPkgResgroup.setSubjectId(subjectId);
		tevglPkgResgroup.setChapterId(chapterId);
		tevglPkgResgroup.setResgroupName(resgroupName);
		tevglPkgResgroup.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
		tevglPkgResgroup.setState("Y"); // 状态(Y有效N无效)
		tevglPkgResgroup.setResgroupTotal(0); // 资源总数
		tevglPkgResgroup.setCreateUserId(loginUserId);
		tevglPkgResgroup.setGroupType(GlobalActivity.ACTIVITY_GROUP_TYPE_1);
		tevglPkgResgroup.setDictCode(StrUtils.isEmpty(dictCode) ? null : dictCode);
		// 排序号处理
		params.put("pkgId", refPkgId);
		params.put("chapterId", tevglPkgResgroup.getChapterId());
	    tevglPkgResgroup.setSortNum(tevglPkgResgroupMapper.getMaxSortNum(params));
	    tevglPkgResgroupMapper.insert(tevglPkgResgroup);
	    // 生成分组之后，再随之生成资源记录
		TevglPkgRes res = new TevglPkgRes();
		res.setResId(Identities.uuid());
		res.setPkgId(refPkgId);
		res.setResgroupId(tevglPkgResgroup.getResgroupId());
		tevglPkgResMapper.insert(res);
		// 教学包的资源数+1，由于章节分组与资源一对一,直接把分组数当作资源数
		pkgUtils.plusPkgResNum(refPkgId, 1);
		return res.getResId();
	}
	
	/**
	 * <p>修改资源分组</p>  
	 * @author huj
	 * @data 2019年12月19日
	 * @return
	 */
	@Override
	@SysLog(value="修改资源分组")
	public R editResGroup( JSONObject jsonObject, String loginUserId) throws BudaosException {
		String resgroupId = jsonObject.getString("resgroupId");
		String resgroupName = jsonObject.getString("resgroupName");
		if ( StrUtils.isEmpty(resgroupId) || StrUtils.isEmpty(resgroupName)) {
			return R.error("必传参数为空");
		}
		resgroupName = resgroupName.trim();
		if (StrUtils.isEmpty(resgroupName)) {
			return R.error("名称不能为空字符");
		}
		TevglPkgResgroup pkgResgroup = tevglPkgResgroupMapper.selectObjectById(resgroupId);
		if (pkgResgroup == null) {
			return R.error("无效的记录");
		}
		String pkgId = pkgResgroup.getPkgId();
		String chapterId = pkgResgroup.getChapterId();
		// 权限校验
		R r = pkgPermissionUtils.hasPermissionChapterV2(pkgId, loginUserId, chapterId);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 同教学包同章节不允许出现相同分组名称
		Map<String, Object> map = new HashMap<>();
	    map.put("createUserId", loginUserId);
	    map.put("pkgId", pkgId);
	    map.put("chapterId", chapterId);
	    List<TevglPkgResgroup> list = tevglPkgResgroupMapper.selectListByMap(map);
	    if (list != null && list.size() > 0) {
	    	for (TevglPkgResgroup t : list) {
	    		if (!t.getResgroupId().equals(resgroupId)) {
					if (t.getResgroupName().equals(resgroupName)) {
						return R.error("同章节不允许出现相同分组名称");
					}	
	    		}
			}
	    }
	    TevglPkgResgroup tevglPkgResgroup = new TevglPkgResgroup();
	    tevglPkgResgroup.setResgroupId(resgroupId);
	    tevglPkgResgroup.setResgroupName(resgroupName);
	    tevglPkgResgroup.setUpdateUserId(loginUserId);
		tevglPkgResgroup.setUpdateTime(DateUtils.getNowTimeStamp()); // 修改时间
		tevglPkgResgroupMapper.update(tevglPkgResgroup);
		return R.ok("保存成功");
	}

	/**
	 * <p>删除资源分组</p>  
	 * @author huj
	 * @data 2019年12月19日	
	 * @param ids
	 * @return
	 */
	@Override
	@SysLog(value="删除资源分组")
	public R deleteResGroup( String[] ids) throws BudaosException {
		if (ids == null || ids.length == 0) {
			return R.error("参数ids为空");
		}
		// TODO 可能还需要一些检验操作
		tevglPkgResgroupMapper.deleteBatch(ids);
		return R.ok("删除成功");
	}

	/**
	 * <p>排序</p>  
	 * @author huj
	 * @data 2019年12月19日	
	 * @param currentId 当前选中的记录ID
	 * @param targetId 目标记录的ID
	 * @param createUserId
	 * @return
	 */
	@Override
	@SysLog(value="排序")
	public R sortNum(String currentId, String targetId, String createUserId) {
		if (StrUtils.isEmpty(currentId) || "null".equals(currentId)) {
			return R.error("参数currentId为空");
		}
		if (StrUtils.isEmpty(targetId) || "null".equals(targetId)) {
			return R.error("参数targetId为空");
		}
		TevglPkgResgroup currentObj = tevglPkgResgroupMapper.selectObjectById(currentId);
		TevglPkgResgroup targetObj = tevglPkgResgroupMapper.selectObjectById(targetId);
		if (currentObj == null) {
			return R.error("当前记录已不存在，请重试");
		}
		if (targetObj == null) {
			return R.error("目标记录已不存在，请重试");
		}
		Integer currentSortNum = currentObj.getSortNum();
		Integer targetSortNum = targetObj.getSortNum();
		TevglPkgResgroup currentT = new TevglPkgResgroup();
		currentT.setResgroupId(currentObj.getResgroupId());
		currentT.setSortNum(targetSortNum);
		tevglPkgResgroupMapper.update(currentT);
		TevglPkgResgroup targetT = new TevglPkgResgroup();
		targetT.setResgroupId(targetObj.getResgroupId());
		targetT.setSortNum(currentSortNum);
		tevglPkgResgroupMapper.update(targetT);
		return R.ok();
	}

	/**
	 * 取部分属性
	 * @param tevglPkgResgroup
	 * @return
	 */
	private Map<String, Object> converToSimpleData(TevglPkgResgroup tevglPkgResgroup){
		Map<String, Object> info = new HashMap<>();
		info.put("resgroupId", tevglPkgResgroup.getResgroupId());
		info.put("chapterId", tevglPkgResgroup.getChapterId());
		info.put("resgroupName", tevglPkgResgroup.getResgroupName());
		return info;
	}
	
	/**
	 * 删除资源分组
	 * @param resgroupId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteResourceGroup(String resgroupId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(resgroupId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgResgroup tevglPkgResgroup = tevglPkgResgroupMapper.selectObjectById(resgroupId);
		if (tevglPkgResgroup == null) {
			return R.error("无效的记录");
		}
		/*if (StrUtils.isNotEmpty(tevglPkgResgroup.getDictCode()) && "1".equals(tevglPkgResgroup.getDictCode())) {
			return R.error("默认的【课程内容】标签，暂不允许删除");
		}*/
		// 权限校验
		R r = pkgPermissionUtils.hasPermissionChapterV2(tevglPkgResgroup.getPkgId(), loginUserId, tevglPkgResgroup.getChapterId());
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 删除资源
		tevglPkgResMapper.deleteResgroupId(resgroupId);
		// 删除资源分组
		tevglPkgResgroupMapper.delete(resgroupId);
		// 教学包资源数-1
		pkgUtils.plusPkgResReduceNum(tevglPkgResgroup.getPkgId(), -1);
		return R.ok("删除成功");
	}

	/**
	 * 重命名
	 * @param resgroupId
	 * @param resgroupName
	 * @return
	 */
	@Override
	public R renameResourceGroup(String resgroupId, String resgroupName, String loginUserId) {
		if (StrUtils.isEmpty(resgroupName) || StrUtils.isEmpty(resgroupId)) {
			return R.error("必传参数为空");
		}
		if (resgroupName.length() > 50) {
			return R.error("名称不能超过50个字符");
		}
		TevglPkgResgroup tevglPkgResgroup = tevglPkgResgroupMapper.selectObjectById(resgroupId);
		if (tevglPkgResgroup == null) {
			return R.error("无效的记录");
		}
		if (StrUtils.isNotEmpty(tevglPkgResgroup.getDictCode()) && "1".equals(tevglPkgResgroup.getDictCode())) {
			return R.error("默认的分组【课程内容】标签，暂不允许修改");
		}
		// 权限校验
		R r = pkgPermissionUtils.hasPermissionChapterV2(tevglPkgResgroup.getPkgId(), loginUserId, tevglPkgResgroup.getChapterId());
		if (!r.get("code").equals(0)) {
			return r;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", tevglPkgResgroup.getPkgId());
		params.put("chapterId", tevglPkgResgroup.getChapterId());
		List<TevglPkgResgroup> list = tevglPkgResgroupMapper.selectListByMap(params);
		boolean flag = list.stream().anyMatch(a -> !a.getChapterId().equals(tevglPkgResgroup.getChapterId()) 
				&& a.getResgroupName().equals(resgroupName));
		if (flag) {
			return R.error("同章节不允许出现相同分组名称");
		}
		TevglPkgResgroup t = new TevglPkgResgroup();
		t.setResgroupId(resgroupId);
		t.setResgroupName(resgroupName);
		tevglPkgResgroupMapper.update(t);
		return R.ok("重命名成功");
	}
	
	/**
	 * 排序
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R sort(JSONObject jsonObject, String loginUserId) {
		Object pkgId = jsonObject.getString("pkgId");
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		JSONArray resgroupIds = jsonObject.getJSONArray("resgroupIds");
		if (resgroupIds == null || resgroupIds.size() == 0) {
			return R.ok();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkgId", pkgId);
		params.put("resgroupIds", resgroupIds);
		List<TevglPkgResgroup> list = tevglPkgResgroupMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < resgroupIds.size(); i++) {
				for (int j = 0; j < list.size(); j++) {
					if (resgroupIds.get(i).equals(list.get(j).getResgroupId())) {
						TevglPkgResgroup t = new TevglPkgResgroup();
						t.setResgroupId(resgroupIds.getString(i));
						t.setSortNum(i+1);
						tevglPkgResgroupMapper.update(t);
					}
				}
			}
		}
		return R.ok();
	}

}
