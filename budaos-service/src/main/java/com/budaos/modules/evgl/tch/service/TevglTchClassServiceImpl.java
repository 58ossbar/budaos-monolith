package com.budaos.modules.evgl.tch.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.tch.api.TevglTchClassService;
import com.budaos.modules.evgl.tch.domain.TevglTchClass;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassMapper;
import com.budaos.modules.job.persistence.TsysOrgMapper;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.modules.sys.domain.TsysOrg;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class TevglTchClassServiceImpl implements TevglTchClassService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassServiceImpl.class);
	@Autowired
	private TevglTchClassMapper tevglTchClassMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TsysAttachService tsysAttachService;
	@Autowired
	private TsysOrgMapper tsysOrgMapper;
	@Autowired
	private DictService dictService;
	
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
		List<TevglTchClass> tevglTchClassList = tevglTchClassMapper.selectListByMap(query);
		convertUtil.convertOrgId(tevglTchClassList, "orgId");
		convertUtil.convertUserId2RealName(tevglTchClassList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglTchClassList, "createUserId", "updateUserId");
		convertUtil.convertDict(tevglTchClassList, "classState", "class_state");
		convertUtil.convertDict(tevglTchClassList, "type", "class_type");
		handleDatas(tevglTchClassList);
		PageUtils pageUtil = new PageUtils(tevglTchClassList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglTchClassList = tevglTchClassMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglTchClassList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglTchClassList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTchClass
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglTchClass tevglTchClass) throws BudaosException {
		String attachId = tevglTchClass.getAttachId();
		String id = Identities.uuid();
		tevglTchClass.setClassId(id);
		tevglTchClass.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglTchClass.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglTchClass);
		tevglTchClassMapper.insert(tevglTchClass);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, id, "1", "13");
		}
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchClass
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglTchClass tevglTchClass) throws BudaosException {
	    tevglTchClass.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglTchClass.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglTchClass);
		tevglTchClassMapper.update(tevglTchClass);
		// 如果上传了资源文件
		String attachId = tevglTchClass.getAttachId();
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, tevglTchClass.getClassId(), "0", "13");
		}
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglTchClassMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglTchClassMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		TevglTchClass tevglTchClass = tevglTchClassMapper.selectObjectById(id);
		if (tevglTchClass == null) {
			return R.ok().put(Constant.R_DATA, new TevglTchClass());
		}
		tevglTchClass.setTeacherPic(uploadPathUtils.stitchingPath(tevglTchClass.getTeacherPic(), "7"));
		tevglTchClass.setTeachingAssistantPic(uploadPathUtils.stitchingPath(tevglTchClass.getTeachingAssistantPic(), "7"));
		if (StrUtils.isNotEmpty(tevglTchClass.getClassImg())) {
			tevglTchClass.setClassImg(uploadPathUtils.stitchingPath(tevglTchClass.getClassImg(), "13"));
		}
		if (StrUtils.isNotEmpty(tevglTchClass.getClassPic())) {
			/*int i = tevglTchClass.getClassPic().indexOf(budaosFieAccessPath);
			if (i == -1) {
				tevglTchClass.setClassPic(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("13") + "/" + tevglTchClass.getClassPic()); // 图片处理
			}*/
			String name = tevglTchClass.getClassPic();
			// 如果不是网络头像,则拼接地址
			if (name.indexOf("https") == -1 && name.indexOf("http") == -1) {
				// 如果没有uploads才拼接
				if (name.indexOf("uploads/dict") == -1) {
					String val = budaosFieAccessPath + "/dict/" + name;
					tevglTchClass.setClassPic(val);
				}
			}
		}
		return R.ok().put(Constant.R_DATA, tevglTchClass);
	}
	
	private void handleDatas(List<TevglTchClass> tevglTchClassList) {
		if (tevglTchClassList == null || tevglTchClassList.size() == 0) {
			return;
		}
		tevglTchClassList.forEach(a -> {
			// 处理班级图片
			handleClassPic(a);
			// 处理教师头像
			if (StrUtils.isNotEmpty(a.getTeacherPic())) {
				a.setTeacherPic(uploadPathUtils.stitchingPath(a.getTeacherPic(), "7"));
			}
			// 处理助教头像
			if (StrUtils.isNotEmpty(a.getTeachingAssistantPic())) {
				a.setTeachingAssistantPic(uploadPathUtils.stitchingPath(a.getTeachingAssistantPic(), "7"));
			}
			// 去掉日期间的杠
			a.setRegistrationStartTime(getYearMonthDay(a.getRegistrationStartTime()));
			a.setExpectTime(getYearMonthDay(a.getExpectTime()));
		});
	}
	
	private String getYearMonthDay(String sourceStr) {
		if (StrUtils.isEmpty(sourceStr)) {
			return sourceStr;
		}
		String yearMonthDay = "";
		String[] split = sourceStr.split("-");
		if (split != null) {
			for (int i = 0; i < split.length; i++) {
				yearMonthDay += split[i];
			}
		}
		return yearMonthDay;
	}
	
	private void handleClassPic(TevglTchClass a) {
		if (a == null) {
			return;
		}
		// 如果自定义上传了
		if (StrUtils.isNotEmpty(a.getClassImg())) {
			a.setClassPic(uploadPathUtils.stitchingPath(a.getClassImg(), "13"));
			a.setClassImg(uploadPathUtils.stitchingPath(a.getClassImg(), "13"));
		} else {
			// 否则使用的是默认图片
			if (StrUtils.isNotEmpty(a.getClassPic())) {
				// 如果不是网络头像,则拼接地址
				if (a.getClassPic().indexOf("https") == -1 && a.getClassPic().indexOf("http") == -1) {
					// 如果没有uploads才拼接
					if (a.getClassPic().indexOf("uploads/dict") == -1) {
						String val = budaosFieAccessPath + "/dict/" + a.getClassPic();
						a.setClassPic(val);
					}
				}
			}	
		}
	}

	/**
	 * 查询自己所教授的班级列表
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryClassListData(Map<String, Object> params, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (StrUtils.isNull(params.get("majorId"))) {
			return R.ok().put(Constant.R_DATA, new ArrayList<>());
		}
		params.put("classState", "3"); // 只取3授课状态的班级
		List<Map<String,Object>> list = tevglTchClassMapper.selectSimpleListMap(params);
		list.stream().forEach(item -> {
			String title = "";
			if (!StrUtils.isNull(item.get("orgParentName"))) {
				title += item.get("orgParentName");
			}
			if (!StrUtils.isNull(item.get("orgName"))) {
				title += " " + item.get("orgName");
			}
			title += " " + item.get("className");
			item.put("title", title);
		});
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectSimpleListMap(Map<String, Object> params) {
		return tevglTchClassMapper.selectSimpleListMap(params);
	}

	/**
	 * 获取机构年份班级树
	 * @param params
	 * @return
	 */
	@Override
	public R getClassTree(Map<String, Object> params) {
		// 点击的节点类型
		Object type = params.get("type");
		// 班级状态
		Object classState = params.get("classState");
		// 最终返回结果
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		// element树组件默认展开的节点
		List<String> defaultExpandedKeys = new ArrayList<>();
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sidx", "create_time");
		map.put("order", "desc");
		map.put("nonOrgType", "2");
		// 查询所有机构
		List<TsysOrg> orglist = tsysOrgMapper.selectListByMap(map);
		for(TsysOrg org : orglist){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id",org.getOrgId());
			m.put("name", org.getOrgName());
			m.put("parentId", org.getParentId());
			m.put("type", "01");
			list.add(m);
		}
		// 查询教育中心机构下面的班级
		map.clear();
		if(classState != null && !"".equals(classState)){
			map.put("classState", classState);
		}
		List<TevglTchClass> classList = tevglTchClassMapper.selectListByMap(map);
		convertUtil.convertDict(classList, "classState", "class_state");
		Map<String,List<TevglTchClass>> yearList = classList.stream()
				.filter(a -> StrUtils.isNotEmpty(a.getAcceptTime()))
				.collect(Collectors.groupingBy(a -> ((TevglTchClass)a).getOrgId()+"#"+((TevglTchClass)a).getAcceptTime().substring(0, 4)));
		List<Map<String,Object>> tempList = new ArrayList<>();
		yearList.forEach((k,v) -> {
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", k);
			m.put("name", k.split("#")[1]);
			m.put("parentId", k.split("#")[0]);
			m.put("type", "04");
			//list.add(m);
			tempList.add(m);
			defaultExpandedKeys.add(k);
			v.forEach(a -> {
				Map<String,Object> mm = new HashMap<String,Object>();
				mm.put("id", a.getClassId());
				mm.put("name", a.getClassName() + "("+a.getClassState()+")");
				mm.put("parentId", k);
				mm.put("type", "02");
				list.add(mm);
			});
		});
		// 按年份降序排序
		List<Map<String, Object>> res = tempList.stream().sorted((h1, h2) -> h2.get("name").toString().compareTo(h1.get("name").toString())).collect(Collectors.toList());
		list.addAll(res);
		List<TevglTchClass> emptyList = classList.stream().filter(a -> StrUtils.isEmpty(a.getAcceptTime())).collect(Collectors.toList());
		for(TevglTchClass cc : emptyList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", cc.getClassId());
			m.put("name", cc.getClassName() + "("+cc.getClassState()+")");
			m.put("parentId", cc.getOrgId());
			m.put("type", "02");
			list.add(m);
		}
		List<Map<String, Object>> resultList = buildTree("-1", list, 0);
		return R.ok().put(Constant.R_DATA, resultList)
				.put("defaultExpandedKeys", defaultExpandedKeys);
	}
	
	/**
	 * 递归构建树形数据
	 * @param parentId
	 * @param allList
	 * @param level
	 * @return
	 */
	private List<Map<String, Object>> buildTree(String parentId, List<Map<String, Object>> allList, int level) {
		if (allList == null || allList.size() == 0) {
			return null;
		}
		// 筛选出匹配的节点
		List<Map<String, Object>> nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId)).collect(Collectors.toList());
		if (nodeList != null && nodeList.size() > 0) {
			// level计算当前处于第几级
			level ++;
			for (int i = 0; i < nodeList.size(); i++) {
				Map<String, Object> node = nodeList.get(i);
				// 当前层级
				node.put("level", level);
				// 递归
				List<Map<String, Object>> list = buildTree(node.get("id").toString(), allList, level);
				if (list != null && list.size() > 0) {
					node.put("children", list);
				} else {
					node.put("children", null);
				}
			}
		}
		return nodeList;
	}

	@Override
	public TevglTchClass selectObjectById(Object id) {
		return tevglTchClassMapper.selectObjectById(id);
	}

	@Override
	public List<TevglTchClass> findClassList(Map<String, Object> params) {
		params.put("sidx", "create_time");
        params.put("order", "desc");
        Query query = new Query(params);
        return tevglTchClassMapper.selectListByMap(query);
	}
	
	@Override
	public R getClassDictTypeList(Map<String, Object> params) {
		List<Map<String,Object>> dictList = dictService.getDictList("class_type");
		dictList.stream().forEach(item -> {
			Object type = item.get("dictCode");
			if (StrUtils.notNull(type)) {
				item.put("totalCount", tevglTchClassMapper.countClassNumByType(type));
			} else {
				item.put("totalCount", 0);
			}
		});
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	

	@Override
	public R queryClassListForWeb(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTchClass> tevglTchClassList = tevglTchClassMapper.findClassListByMap(query);
		convertUtil.convertDict(tevglTchClassList, "classState", "class_state");
		handleDatas(tevglTchClassList);
		PageUtils pageUtil = new PageUtils(tevglTchClassList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}


}
