package com.budaos.modules.evgl.book.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.book.api.TevglBookMajorService;
import com.budaos.modules.evgl.book.domain.TevglBookMajor;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.domain.TevglBookSubperiod;
import com.budaos.modules.evgl.book.persistence.TevglBookMajorMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubperiodMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClass;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> Title: 职业课程</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglBookMajorServiceImpl implements TevglBookMajorService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglBookMajorServiceImpl.class);
	@Autowired
	private TevglBookMajorMapper tevglBookMajorMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglBookSubperiodMapper tevglBookSubperiodMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassMapper tevglTchClassMapper;

	/**
	 *
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglBookMajor> tevglBookMajorList = tevglBookMajorMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglBookMajorList, "createUserId", "updateUserId");
		convertUtil.convertOrgId(tevglBookMajorList, "orgId");
		PageUtils pageUtil = new PageUtils(tevglBookMajorList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 *
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglBookMajorList = tevglBookMajorMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglBookMajorList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglBookMajorList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglBookMajor
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglBookMajor tevglBookMajor) throws BudaosException {
		tevglBookMajor.setMajorId(Identities.uuid());
		tevglBookMajor.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglBookMajor.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglBookMajor);
		tevglBookMajorMapper.insert(tevglBookMajor);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglBookMajor
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglBookMajor tevglBookMajor) throws BudaosException {
	    tevglBookMajor.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglBookMajor.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglBookMajor);
		tevglBookMajorMapper.update(tevglBookMajor);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglBookMajorMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		if (ids == null || ids.length == 0) {
			return R.ok();
		}
		List<String> majorIds = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			majorIds.add(ids[i]);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("majorIds", majorIds);
		List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			return R.error("该职业课程路径下还存在课程，请先删除课程");
			/*
			// 删除职业课程路径于课程的关系
			list.forEach(a -> {
				tevglBookSubperiodMapper.delete(a.getSubperiodId());
			});
			*/
		}
		tevglBookMajorMapper.deleteBatch(ids);
		return R.ok();
	}
	
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R viewForMgr( String id) {
		TevglBookMajor major = tevglBookMajorMapper.selectObjectById(id);
		if (major == null) {
			return R.ok();
		}
		major.setMajorLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("11") + "/" + major.getMajorLogo());
		return R.ok().put(Constant.R_DATA, major);
	}
	
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		TevglBookMajor major = tevglBookMajorMapper.selectObjectById(id);
		if (major == null) {
			return R.ok();
		}
		major.setMajorLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("11") + "/" + major.getMajorLogo());
		convertUtil.convertOrgId(major, "orgId");
		// resultList 存储分组后的数据
		List<List<TevglBookSubperiod>> resultList = new ArrayList<>();
		// 查询该职业课程路径下的课程
		Map<String, Object> map = new HashMap<>();
		map.put("majorId", major.getMajorId());
		List<TevglBookSubperiod> tevglBookSubperiodList = tevglBookSubperiodMapper.selectListByMap(map);
		// 根据所属学期分组
		tevglBookSubperiodList.stream().collect(Collectors.groupingBy(TevglBookSubperiod::getTerm, Collectors.toList()))
		.forEach((name, dataList) -> {
			// 查询课程信息
			dataList.forEach(a -> {
				TevglBookSubject subject = tevglBookSubjectMapper.selectObjectById(a.getSubjectId());
				if (subject != null) {
					// 图片处理
					if (subject.getSubjectLogo() != null && !"".equals(subject.getSubjectLogo())) {
						int i = subject.getSubjectLogo().indexOf(budaosFieAccessPath);
						if (i == -1) {
							subject.setSubjectLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("10") + "/" + subject.getSubjectLogo());
						}
					}
				}
				a.setTevglBookSubject(subject);
			});
			resultList.add(dataList);
		});
		
		// 进一步处理数据，便于前端识别
		List<Map<String, Object>> ok = new ArrayList<>();
		resultList.forEach(list -> {
			Map<String, Object> mm = new HashMap<>();
			List<TevglBookSubject> subjects = new ArrayList<>();
			list.forEach(a -> { // a此时是实体TevglBookSubperiod，利用map的key不能重复，取值
				mm.put("subperiodId", a.getSubperiodId());
				mm.put("majorId", a.getMajorId());
				mm.put("subjectId", a.getSubjectId());
				mm.put("sortNum", a.getSortNum());
				mm.put("term", a.getTerm());
				mm.put("subjectProperty", a.getSubjectProperty());
				mm.put("classHour", a.getClassHour());
				mm.put("classScore", a.getClassScore());
				// 将课程加入集合
				subjects.add(a.getTevglBookSubject());
			});
			mm.put("subjectList", subjects); // 课程集合
			ok.add(mm);
		});
		
		// 计算总课时、总学分
		BigDecimal totalClassHour = new BigDecimal(0);
		BigDecimal totalClassScore = new BigDecimal(0);
		for (Map<String, Object> k : ok) {
			if (k.get("subjectList") != null) {
				@SuppressWarnings("unchecked")
				List<TevglBookSubject> list = (List<TevglBookSubject>) k.get("subjectList");
				for (TevglBookSubject subject : list) {
					if (subject.getClassHour() != null) {
						totalClassHour = totalClassHour.add(subject.getClassHour());
					}
					if (subject.getClassScore() != null) {
						totalClassScore = totalClassScore.add(subject.getClassScore());
					}
				}
			}
		}
		convertUtil.convertDict(ok, "term", "term"); // 所属学期
		return R.ok().put(Constant.R_DATA, major)
				.put("more", ok) // 学期下的课程
				.put("totalClassHour", totalClassHour)
				.put("totalClassScore", totalClassScore);
	}
	
	/**
	 * <p>复制</p>
	 * @author huj
	 * @data 2019年7月9日
	 * @param id
	 * @return
	 */
	@Override
	public R copy(String id) {
		TevglBookMajor major = tevglBookMajorMapper.selectObjectById(id);
		if (major == null) {
			return R.error("复制失败");
		}
		// 填充并保存职业课程路径信息
		TevglBookMajor tb = fillMyBookMajorInfo(major.getParentId(), major);
		tevglBookMajorMapper.insert(tb);
		// 查询并填充和保存专业课程关系
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("majorId", major.getMajorId());
		List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(map);
		if (list.size() > 0) {
			list.forEach(a -> {
				TevglBookSubperiod obj = fillMySubperiodInfo(tb.getMajorId(), null, a);
				tevglBookSubperiodMapper.insert(obj);
			});
		}
		return R.ok("复制成功");
	}
	
	/**
	 * <p>填充职业课程路径基本信息</p>
	 * @author huj
	 * @data 2019年7月9日
	 * @param parentId 父ID
	 * @param major 职业课程路径对象
	 * @return 返回已经填充好的对象
	 */
	private TevglBookMajor fillMyBookMajorInfo(String parentId, TevglBookMajor major) {
		TevglBookMajor tb = new TevglBookMajor(); 
		tb.setMajorId(major.getMajorId()); // 专业方向主键ID
		tb.setOrgId(major.getOrgId()); // 所属院校
		tb.setMajorName(major.getMajorName()); // 专业名称
		tb.setMajorLogo(major.getMajorLogo()); // 专业logo图
		tb.setMajorDesc(major.getMajorDesc()); // 专业简介(文本)
		tb.setMajorRemark(major.getMajorRemark()); // 专业详细描述(富文本)
		tb.setMajorType(major.getMajorType()); // 专业类型(拓展字段)
		tb.setParentId(parentId); // 父级ID
		tb.setLevel(major.getLevel()); // 层级
		tb.setShowIndex(major.getShowIndex()); // 是否推荐到首页(Y是N否)
		tb.setSortNum(major.getSortNum()); // 排序号
		tb.setCreateUserId(serviceLoginUtil.getLoginUserId()); // 创建人
		tb.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
		tb.setState(tb.getState()); // 状态(Y有效N无效)
		return tb;
	}
	

	/**
	 * <p>填充专业课程关系信息</p>
	 * @author huj
	 * @data 2019年7月8日
	 * @param subperiod
	 * @param sb
	 * @return
	 */
	private TevglBookSubperiod fillMySubperiodInfo(String majorId, String subjectId, TevglBookSubperiod subperiod) {
		TevglBookSubperiod obj = new TevglBookSubperiod();
		obj.setSubperiodId(Identities.uuid()); // 主键ID
		obj.setMajorId(majorId); // 专业ID
		obj.setSubjectId(subjectId); // 课程ID
		obj.setSortNum(subperiod.getSortNum()); // 排序号
		obj.setTerm(subperiod.getTerm()); // 所属学期(来源字典)
		obj.setSubjectProperty(subperiod.getSubjectProperty()); // 课程属性(选修or必修)
		obj.setClassHour(subperiod.getClassHour()); // 课时
		obj.setClassScore(subperiod.getClassScore()); // 学分
		obj.setCreateUserId(serviceLoginUtil.getLoginUserId()); // 创建人 
		obj.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
		return obj;
	}
	
	/**
	 * <p>前端查询该专业下的课程</p>
	 * @author huj
	 * @data 2019年7月11日
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectListMapByMapForWeb( Map<String, Object> map){
		String type = (String)map.get("type");
		map.put("sidx", "hot");
		map.put("order", "desc,sort_num asc");
		map.put("state", "Y");
		List<Map<String, Object>> majorList = tevglBookMajorMapper.selectListMapByMapForWeb(map);
		// 获取所有有效的课堂
		if (StrUtils.isNotEmpty(type) && "countClassroomNum".equals(type)) {
			map.clear();
			map.put("state", "Y");
			List<Map<String, Object>> classroomList = tevglTchClassroomMapper.selectListMapForCommon(map);
			majorList.stream().forEach(major -> {
				// 计算此专业下的课堂数量
				long classroomNum = classroomList.stream().filter(a -> a.get("majorId").equals(major.get("majorId"))).count();
				major.put("classroomNum", classroomNum);
			});
		}
		return majorList;
	}
	
	

	/**
	 * <p>查询所有(无分页)</p>
	 * @author huj
	 * @data 2019年7月12日
	 * @param params
	 * @return
	 */
	@Override
	public List<TevglBookMajor> queryAll(Map<String, Object> params) {
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		params.put("state", "Y");
		List<TevglBookMajor> tevglBookMajorList = tevglBookMajorMapper.selectListByMap(params);
		tevglBookMajorList.forEach(a -> {
			//图片路径处理
			a.setMajorLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("11") + "/" + a.getMajorLogo());
			Map<String, Object> map = new HashMap<>();
			map.put("state", "Y");
			map.put("majorId", a.getMajorId());
			List<TevglBookSubject> list = tevglBookSubjectMapper.selectListByMapForWeb(map);
			if (list != null && list.size() > 0) {
				a.setSubjectTotalSize(list.size());
			} else {
				a.setSubjectTotalSize(0);
			}
		});
		return tevglBookMajorList;
	}

	/**
	 * <p>根据条件查询记录</p>
	 * @param params
	 * @return
	 */
	@Override
	public R selectListByMapForWeb(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglBookMajor> tevglBookMajorList = tevglBookMajorMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglBookMajorList, "createUserId", "updateUserId");
		convertUtil.convertOrgId(tevglBookMajorList, "orgId");
		tevglBookMajorList.forEach(a -> {
			//图片路径处理
			a.setMajorLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("11") + "/" + a.getMajorLogo());
		});
		PageUtils pageUtil = new PageUtils(tevglBookMajorList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param map
	 * @return
	 */
	@Override
	public R queryForTree(Map<String, Object> map) {
		map.put("sidx", "sort_num");
		map.put("order", "asc");
		Query query = new Query(map);
		List<TevglBookMajor> tevglBookMajorList = tevglBookMajorMapper.selectListByMap(query);
		return R.ok().put(Constant.R_DATA, tevglBookMajorList);
	}

	/**
	 * <p>根据条件查询记录</p>
	 * @param params
	 * @return
	 */
	@Override
	public List<TevglBookMajor> selectListByMap(@RequestParam Map<String, Object> params) {
		params.put("state", "Y");
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<TevglBookMajor> list = tevglBookMajorMapper.selectListByMap(params);
		return list;
	}
	
	/**
	 * <b>职业路径与班级组成层次结构的数据</b>  
	 * @author huj
	 * @param name
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryMajorClassTreeData(String name) {
		// 查出职业路径
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", "Y");
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<TevglBookMajor> list = tevglBookMajorMapper.selectListByMap(params);
		List<Map<String, Object>> majorList = list.stream().map(a -> {
			Map<String, Object> info = new HashMap<>();
			info.put("id", a.getMajorId());
			info.put("name", a.getMajorName());
			return info;
		}).collect(Collectors.toList());
		// 查出班级
		params.clear();
		List<TevglTchClass> classList = tevglTchClassMapper.selectListByMap(params);
		majorList.stream().forEach(major -> {
			List<Map<String, Object>> children = classList.stream()
				.filter(a -> a.getMajorId().equals(major.get("id")))
				.map(a -> {
					Map<String, Object> info = new HashMap<>();
					info.put("id", a.getClassId());
					info.put("name", a.getClassName());
					return info;
				})
				.collect(Collectors.toList());
			if (children != null && children.size() > 0) {
				major.put("children", children);
			}
		});
		return majorList;
	}

	@Override
	public List<Map<String, Object>> queryMajorSubjectTreeData(String name) {
		// 查出职业路径
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", "Y");
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<TevglBookMajor> list = tevglBookMajorMapper.selectListByMap(params);
		List<Map<String, Object>> majorList = list.stream().map(a -> {
			Map<String, Object> info = new HashMap<>();
			info.put("id", a.getMajorId());
			info.put("name", a.getMajorName());
			return info;
		}).collect(Collectors.toList());
		majorList.stream().forEach(major -> {
			// 查出课程
			params.clear();
			params.put("state", "Y");
			params.put("isSubjectRefNull", "Y");
			params.put("majorId", major.get("id"));
			List<TevglBookSubject> tevglBookSubjectList = tevglBookSubjectMapper.selectListByMapForCommon(params);
			List<Map<String, Object>> children = tevglBookSubjectList.stream()
					.map(a -> {
						Map<String, Object> info = new HashMap<>();
						info.put("id", a.getSubjectId());
						info.put("name", a.getSubjectName());
						return info;
					})
					.collect(Collectors.toList());
			if (children != null && children.size() > 0) {
				major.put("children", children);
			} else {
				major.put("disabled", true);
			}
		});
		return majorList;
	}
	
}
