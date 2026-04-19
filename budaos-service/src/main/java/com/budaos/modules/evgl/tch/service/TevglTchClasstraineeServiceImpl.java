package com.budaos.modules.evgl.tch.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.ServiceRoleUtils;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.tch.api.TevglTchClasstraineeService;
import com.budaos.modules.evgl.tch.domain.TevglTchClass;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.domain.TevglTchClasstrainee;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClasstraineeMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeInfoVo;
import com.budaos.modules.im.domain.TimUserinfo;
import com.budaos.modules.im.persistence.TimUserinfoMapper;
import com.budaos.modules.job.service.TuserRoleServiceImpl;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.budaos.utils.tool.TicketDesUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
@Transactional
public class TevglTchClasstraineeServiceImpl implements TevglTchClasstraineeService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClasstraineeServiceImpl.class);
	@Autowired
	private TevglTchClasstraineeMapper tevglTchClasstraineeMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	// 访问地址
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TuserRoleServiceImpl tuserRoleServiceImpl;
	@Autowired
	private TevglTchClassMapper tevglTchClassMapper;
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TimUserinfoMapper timUserinfoMapper;
	
	@Value("${com.budaos.defaultHeadImg:}")
	public String defaultHeadImg;
	
	
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
		List<TevglTchClasstrainee> tevglTchClasstraineeList = tevglTchClasstraineeMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClasstraineeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		String loginUserId = serviceLoginUtil.getLoginUserId();
		// 非超级管理员时
		List<String> roleIdList = tuserRoleServiceImpl.selectRoleListByUserId(serviceLoginUtil.getLoginUserId());
		if (roleIdList.size() == 1 && ServiceRoleUtils.TEACHER_ROLE_ID.equals(roleIdList.get(0))) {
			// 获取登录用户所教的班级
			Map<String, Object> map = new HashMap<>();
			map.put("teacherId", loginUserId);
			List<TevglTchClass> classList = tevglTchClassMapper.selectListByMap(map);
			if (classList.size() > 0) {
				List<String> classIds = new ArrayList<>();
				classList.forEach(a -> {
					classIds.add(a.getClassId());
				});
				// 获取班级学员
				map.clear();
				map.put("classIds", classIds);
				List<TevglTchClasstrainee> classTraineeList = tevglTchClasstraineeMapper.selectListByMap(map);
				if (classTraineeList.size() > 0) {
					List<String> traineeIds = new ArrayList<>();
					classTraineeList.forEach(a -> {
						traineeIds.add(a.getTraineeId());
					});
					params.put("traineeIds", traineeIds);
				} else {
					Query query = new Query(params);					
					PageUtils pageUtil = new PageUtils(new ArrayList<>(), query.getPage(),query.getLimit());
					return R.ok().put(Constant.R_DATA, pageUtil);
				}
			} else {
				Query query = new Query(params);					
				PageUtils pageUtil = new PageUtils(new ArrayList<>(), query.getPage(),query.getLimit());
				return R.ok().put(Constant.R_DATA, pageUtil);
			}
		}
		Object type = params.get("type");
		Object orgId = params.get("orgId");
		Object year = params.get("year");
		Object classId = params.get("classId");
		Map<String, Object> map = new HashMap<String, Object>();
		if ("orgNode".equals(type)) { // 如果点击的是机构节点
			map.clear();
			map.put("orgId", orgId);
			if (roleIdList.size() == 1 && ServiceRoleUtils.TEACHER_ROLE_ID.equals(roleIdList.get(0))) { // 
				map.put("teacherId", loginUserId);
			}
			List<TevglTchClass> classList = tevglTchClassMapper.selectListByMap(map);
			if (classList.size() > 0) {
				List<String> classIds = new ArrayList<>();
				classList.forEach(a -> {
					classIds.add(a.getClassId());
				});
				map.clear();
				map.put("classIds", classIds);
				List<TevglTchClasstrainee> classTrainee = tevglTchClasstraineeMapper.selectListByMap(map);
				if (classTrainee.size() > 0) {
					List<String> traineeIds = new ArrayList<String>();
					classTrainee.forEach(a -> {
						traineeIds.add(a.getTraineeId());
					});
					params.put("traineeIds", traineeIds);
				} else {
					Query query = new Query(params);					
					PageUtils pageUtil = new PageUtils(new ArrayList<>(), query.getPage(),query.getLimit());
					return R.ok().put(Constant.R_DATA, pageUtil);
				}
			} else {
				Query query = new Query(params);					
				PageUtils pageUtil = new PageUtils(new ArrayList<>(), query.getPage(),query.getLimit());
				return R.ok().put(Constant.R_DATA, pageUtil);
			}
		} else if (("yearNode".equals(type))) { // 如果点击的是年份节点
			if (!StrUtils.isNull(year)) {
				// 查出该机构下的班级
				map.clear();
				map.put("orgId", orgId);
				map.put("beginTime", year + "-01-01 00:00:00");
				map.put("endTime", year + "-12-31 23:59:59");
				if (roleIdList.size() == 1 && ServiceRoleUtils.TEACHER_ROLE_ID.equals(roleIdList.get(0))) { // 
					map.put("teacherId", loginUserId);
				}
				List<String> classIdList = new ArrayList<String>();
				List<TevglTchClass> list = tevglTchClassMapper.selectListByMap(map);
				if (list.size() > 0) {
					for (TevglTchClass t : list) {
						classIdList.add(t.getClassId());
					}
					map.clear();
					map.put("classIds", classIdList);
					List<TevglTchClasstrainee> classTrainee = tevglTchClasstraineeMapper.selectListByMap(map);
					if (classTrainee.size() > 0) {
						List<String> traineeIds = new ArrayList<String>();
						classTrainee.forEach(a -> {
							traineeIds.add(a.getTraineeId());
						});
						params.put("traineeIds", traineeIds);
					} else { // 说明该班级下面没有学员
						Query query = new Query(params);
						PageUtils pageUtil = new PageUtils(new ArrayList<>(),query.getPage(),query.getLimit());
						return R.ok().put(Constant.R_DATA, pageUtil);
					}
				}
			}
		} else if ("classNode".equals(type)) { // 点击的是班级节点
			if (!StrUtils.isNull(classId)) {
				// 查询该班级下的学员
				map.put("classId", classId);
				List<TevglTchClasstrainee> list = tevglTchClasstraineeMapper.selectListByMap(map);
				if (list.size() > 0) {
					List<String> traineeIds = new ArrayList<String>();
					list.forEach(a -> {
						traineeIds.add(a.getTraineeId());
					});	
					params.put("traineeIds", traineeIds);
				} else { // 说明该班级下面没有学员
					Query query = new Query(params);
					PageUtils pageUtil = new PageUtils(new ArrayList<>(),query.getPage(),query.getLimit());
					return R.ok().put(Constant.R_DATA, pageUtil);
				}
			}
		}
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTchClasstraineeList = tevglTchClasstraineeMapper.selectListMapByMap(query);
		tevglTchClasstraineeList.stream().forEach(trainee -> {
			trainee.put("traineePic", uploadPathUtils.stitchingPath(trainee.get("traineePic"), "16"));
			if (StrUtils.isNull(trainee.get("traineePic"))) {
				trainee.put("traineePic", "/uploads/defaulthead.png");
			}
		});
		convertUtil.convertDict(tevglTchClasstraineeList, "traineeSex", "sex");
		PageUtils pageUtil = new PageUtils(tevglTchClasstraineeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 新增
	 * @param tevglTchClasstrainee
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglTchClasstrainee tevglTchClasstrainee) throws BudaosException {
		tevglTchClasstrainee.setCtId(Identities.uuid());
		ValidatorUtils.check(tevglTchClasstrainee);
		tevglTchClasstraineeMapper.insert(tevglTchClasstrainee);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchClasstrainee
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglTchClasstrainee tevglTchClasstrainee) throws BudaosException {
	    ValidatorUtils.check(tevglTchClasstrainee);
		tevglTchClasstraineeMapper.update(tevglTchClasstrainee);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglTchClasstraineeMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglTchClasstraineeMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglTchClasstraineeMapper.selectObjectById(id));
	}

	/**
	 * <p>根据条件查询班级学员</p>  
	 * @author huj
	 * @data 2019年12月16日	
	 * @param params
	 * @return
	 */
	@Override
	@SysLog(value="根据条件查询班级学员")
	public R selectListMapForWeb( Map<String, Object> params) {
		String classId = (String) params.get("classId");
		if (StrUtils.isEmpty(classId) || "null".equals(classId)) {
			return R.error("参数classId为空");
		}
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTchClasstraineeList = tevglTchClasstraineeMapper.selectListMapForWeb(params);
		if (tevglTchClasstraineeList != null && tevglTchClasstraineeList.size() > 0) {
			convertUtil.convertDict(tevglTchClasstraineeList, "traineeSex", "sex");
		}
		PageUtils pageUtil = new PageUtils(tevglTchClasstraineeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 根据条件查询班级成员
	 * @param params 说明：必传的参数key； classId，选传的key有: trianeeName
	 * @return
	 * @apiNote 查询并返回了如下字段：ctId班级学员表主键，classId班级id，className班级名称， traineeId学员id，
	 * traineeName学员名称（如没有值则查的微信昵称），traineeSex学员性别，traineePic学员头像（没有证件照则查微信头像）
	 */
	@Override
	public List<Map<String, Object>> listClassTraineeInfo(Map<String, Object> params) {
		String classId = (String) params.get("classId");
		if (StrUtils.isEmpty(classId)) {
			return null;
		}
		List<Map<String,Object>> list = tevglTchClasstraineeMapper.selectListMapForWeb(params);
		// 处理图片路径
		list.stream().forEach(traineeInfo -> {
			String traineePic = (String) traineeInfo.get("traineePic");
			traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineePic, "16"));
			// 没有头像的话，默认一个头像
			if (traineeInfo.get("traineePic") == null) {
				traineeInfo.put("traineePic", "/uploads/defaulthead.png");
			}
		});
		return list;
	}

	/**
	 * 根据条件查询班级成员，注意：只会查询未加入课堂的成员
	 * @param params 必传参数key：ctId课堂主键
	 * @return
	 */
	@Override
	public R listClassTraineeExcludeJoinedClassroom(Map<String, Object> params) {
		String ctId = (String) params.get("ctId");
		String classId = (String) params.get("classId");
		if (StrUtils.isEmpty(classId) || StrUtils.isEmpty(ctId)) {
			return R.error("参数classId或ctId为空");
		}
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> list = tevglTchClasstraineeMapper.selectListMapForWebExclude(query);
		convertUtil.convertDict(list, "traineeSex", "sex");
		// 处理图片路径
		list.stream().forEach(traineeInfo -> {
			String traineePic = (String) traineeInfo.get("traineePic");
			traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineePic, "16"));
			// 没有头像的话，默认一个头像
			if (traineeInfo.get("traineePic") == null) {
				traineeInfo.put("traineePic", "/uploads/defaulthead.png");
			}
		});
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 管理端修改班级成员信息（其实就是修改学员信息）
	 * @param tevglTraineeInfo
	 * @return
	 */
	@Override
	public R updateTraineeForMgr(TevglTraineeInfo tevglTraineeInfo) {
		if (StrUtils.isEmpty(tevglTraineeInfo.getTraineeId())) {
			return R.error("保存失败");
		}
		if (StrUtils.isEmpty(tevglTraineeInfo.getTraineeName()) || StrUtils.isEmpty(tevglTraineeInfo.getClassId())) {
			return R.error("学员姓名和所属班级不能为空");
		}
		TevglTraineeInfo t = new TevglTraineeInfo();
		t.setTraineeId(tevglTraineeInfo.getTraineeId());
		t.setTraineeName(tevglTraineeInfo.getTraineeName());
		t.setTraineePic(tevglTraineeInfo.getTraineePic());
		t.setTraineeSex(tevglTraineeInfo.getTraineeSex());
		t.setJobNumber(tevglTraineeInfo.getJobNumber());
		t.setTraineeSchool(tevglTraineeInfo.getTraineeSchool());
		t.setEmail(tevglTraineeInfo.getEmail());
		tevglTraineeInfoMapper.update(t);
		// 先删除原有的（注意：需求变更导致了生产环境中的数据，一个人可能会在多个班级）	
		Map<String, Object> map = new HashMap<>();
		map.put("traineeId", t.getTraineeId());
		List<TevglTchClasstrainee> list = tevglTchClasstraineeMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			List<String> collect = list.stream().map(a -> a.getCtId()).collect(Collectors.toList());	
			tevglTchClasstraineeMapper.deleteBatch(collect.stream().toArray(String[]::new));
		}
		// 重新创建
		String[] classIdArray = tevglTraineeInfo.getClassId().split(",");
		List<TevglTchClasstrainee> insertList = new ArrayList<>();
		for (int i = 0; i < classIdArray.length; i++) {
			TevglTchClasstrainee ob = new TevglTchClasstrainee();
			ob.setCtId(Identities.uuid());
			ob.setTraineeId(tevglTraineeInfo.getTraineeId());
			ob.setClassId(classIdArray[i]);
			insertList.add(ob);
		}
		if (insertList.size() > 0) {
			tevglTchClasstraineeMapper.insertBatch(insertList);
		}
		return R.ok("保存成功");
	}
	

	/**
	 * excel导入班级成员
	 * @param request
	 * @param classId
	 * @return
	 */
	@Override
	@Transactional
	public R importExcel(HttpServletRequest request, String classId) {
		if (StrUtils.isEmpty(classId)) {
			return R.error("请先选择将学员导入至哪个班级");
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		if (fileMap == null || fileMap.isEmpty()) {
			return R.error("请上传班级成员excel数据");
		}
		// 等待入库的学员数据
		List<TevglTraineeInfo> insertTraineeList = new ArrayList<>();
		List<String> traineeIdList = new ArrayList<>();
		// 获取已经绑定了手机号码的学员记录
		List<Map<String, Object>> existedTraineeList = tevglTraineeInfoMapper.selectTraineeIdAndMobile();
		// 错误信息
		List<String> errorMessage = new ArrayList<>();
		int successLines = 0, errorLines = 0;
		// 遍历
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			// 获取上传文件对象
			MultipartFile file = entity.getValue();
			try {
				// 读取excel模板
				XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
				// 读取了模板内第一个tab内容
				XSSFSheet sheet = workbook.getSheetAt(0);
				// TODO 解析数据并写入到学员表
				//String className = sheet.getSheetName();
				// TODO 验证学号/工号唯一

				// 遍历excel中tab的每一行数据
				log.debug("excel中tab的多少行======================" + sheet.getLastRowNum());
				for (int i = 2; i <= sheet.getLastRowNum() - 1; i++) {
					// 根据模板，读取每一行的前?列的值
					XSSFRow row = sheet.getRow(i);
					String cell1 = getValueV4(row.getCell(0)); // 手机号码
					String cell2 = getValueV4(row.getCell(1)); // 学员姓名
					String cell3 = getValueV4(row.getCell(2)); // 性别
					String cell4 = getValueV4(row.getCell(3)); // 邮箱
					String cell5 = getValueV4(row.getCell(4)); // 所在学校
					String cell6 = getValueV4(row.getCell(5)); // 所学专业
					String cell7 = getValueV4(row.getCell(6)); // 学号
					//String cell8 = getValue(row.getCell(7)); // 院系名称
					if (StrUtils.isEmpty(cell1)) {
						errorMessage.add("第"+(i+1)+"行，第1列，手机号码为空，已忽略导入");
						errorLines ++;
						continue;
					}
					if (!isMobile(cell1.trim())) {
						errorMessage.add("第"+(i+1)+"行，第1列，手机号码格式不正确，已忽略导入");
						errorLines ++;
						continue;
					}
					if (StrUtils.isEmpty(cell2)) {
						errorMessage.add("第"+(i+1)+"行，第2列，学员姓名为空，已忽略导入");
						errorLines ++;
						continue;
					}
					// 长度判断
					if (StrUtils.isNotEmpty(cell2) && cell2.length() >  30) {
						errorMessage.add("第"+(i+1)+"行，第2列，内容长度超过30，已忽略导入");
						errorLines ++;
						continue;
					}
					if (StrUtils.isNotEmpty(cell3) && cell3.length() >  30) {
						errorMessage.add("第"+(i+1)+"行，第3列，内容长度超过30，已忽略导入");
						errorLines ++;
						continue;
					}
					if (StrUtils.isNotEmpty(cell4) && cell4.length() >  30) {
						errorMessage.add("第"+(i+1)+"行，第4列，内容长度超过30，已忽略导入");
						errorLines ++;
						continue;
					}
					if (StrUtils.isNotEmpty(cell5) && cell5.length() >  30) {
						errorMessage.add("第"+(i+1)+"行，第5列，内容长度超过30，已忽略导入");
						errorLines ++;
						continue;
					}
					if (StrUtils.isNotEmpty(cell6) && cell6.length() >  30) {
						errorMessage.add("第"+(i+1)+"行，第6列，内容长度超过30，已忽略导入");
						errorLines ++;
						continue;
					}
					try {
						List<String> collect = existedTraineeList.stream()
								.filter(a -> a.get("mobile").equals(cell1.trim()))
								.map(a -> a.get("traineeId").toString())
								.collect(Collectors.toList());
						String traineeId = "";
						if (collect != null && collect.size() > 0) {
							traineeId = collect.get(0);
						}
						log.debug("根据手机号码 {} 是否匹配到用户 {}", cell1, cell2);
						// 更新操作
						if (StrUtils.isNotEmpty(traineeId)) {
							log.debug("修改操作");
							traineeIdList.add(traineeId);
						} else { // 新增操作
							log.debug("新增操作");
							TevglTraineeInfo traineeInfo = new TevglTraineeInfo();
							traineeInfo.setTraineeId(Identities.uuid());
							traineeInfo.setMobile(cell1); // 手机号码
							traineeInfo.setTraineeName(cell2); // 姓名
							traineeInfo.setTraineeSex(convertSex(cell3)); // 性别
							traineeInfo.setEmail(cell4); // 邮箱
							traineeInfo.setTraineeSchool(cell5); // 所在学校
							traineeInfo.setMajor(cell6); // 所学专业
							traineeInfo.setJobNumber(cell7); // 学号
							// 学员状态(1游客2在册3中途退学4毕业5就职)
							traineeInfo.setTraineeState("1");
							// 用户类型(1客户2系统用户3学员4教师)
							traineeInfo.setTraineeType("1");
							traineeInfo.setTraineeHead(defaultHeadImg);
							traineeInfo.setTraineePic(defaultHeadImg);
							traineeInfo.setCreateTime(DateUtils.getNowTimeStamp());
							traineeInfo.setCreateUserId("1");
							// 设置默认密码为手机号码前6位
							String salt = RandomStringUtils.randomAlphanumeric(24);
							traineeInfo.setUserPasswd(TicketDesUtil.encryptWithMd5(cell1, salt));
							traineeInfo.setUserYan(salt);
							// 其它信息
							traineeInfo.setEmpiricalValue(0);
							traineeInfo.setBlogsNum(0);
							traineeInfo.setClassroomNum(0);
							traineeInfo.setFansNum(0);
							traineeInfo.setFollowNum(0);
							insertTraineeList.add(traineeInfo);
						}
						successLines++;
					} catch (Exception e) {
						e.printStackTrace();
						errorLines++;
						String message = e.getMessage();
						int lineNumber = i + 1;
						log.debug(lineNumber + " => " + message);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.debug(e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		if (insertTraineeList.size() > 0) {
			// 学员表入库（相当于创建账号）
			tevglTraineeInfoMapper.insertBatch(insertTraineeList);
			// 绑定学员与班级关系
			List<String> stringList = insertTraineeList.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
			R r = this.saveClassTraineeBatch(classId, stringList);
			log.debug("学员 {}", stringList);
			log.debug("excel导入 => 班级成员 => 批量新增执行结果 => {}", r);
			// TODO 往即时通讯里面插入账户信息
			List<TimUserinfo> insertTimUserinfoList = new ArrayList<TimUserinfo>();
			insertTraineeList.stream().forEach(trainee -> {
				TimUserinfo t = new TimUserinfo();
				t.setUserId(trainee.getTraineeId());
				t.setRelateId(trainee.getTraineeId());
				t.setUserRealname(trainee.getTraineeName());
				t.setUserimg(trainee.getTraineePic());
				t.setSex(trainee.getTraineeSex());
				t.setMobile(trainee.getMobile());
				t.setCreateTime(DateUtils.getNowTimeStamp());
				t.setStatus("Y");
				insertTimUserinfoList.add(t);
			});
			timUserinfoMapper.insertBatch(insertTimUserinfoList);
			
		}
		// 绑定已有账号的学员们与班级关系
		if (traineeIdList.size() > 0) {
			R r = this.saveClassTraineeBatch(classId, traineeIdList);
			log.debug("学员 {}", traineeIdList);
			log.debug("excel导入 => 班级成员 => 批量新增执行结果 => {}", r);
		}
		// 返回信息
		Map<String, Object> result = new HashMap<>();
		result.put("errorMessage", errorMessage);
		result.put("successLines", successLines);
		result.put("errorLines", errorLines);
		result.put("insertTraineeList", insertTraineeList);
		return R.ok().put(Constant.R_DATA, result);
	}

	/**
	 * 入库课堂成员表
	 * @param classId
	 * @param classTraineeList
	 * @param params
	 */
	private void doSaveClassroomTrainee(String classId, List<TevglTchClasstrainee> classTraineeList, Map<String, Object> params) {
		if (classTraineeList == null || classTraineeList.size() == 0) {
			return;
		}
		List<String> traineeIds = classTraineeList.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
		params.clear();
		params.put("classId", classId);
		// 课堂状态(1未开始2进行中3已结束)
		params.put("classroomState", "2");
		List<TevglTchClassroom> classroomList = tevglTchClassroomMapper.selectListByMap(params);
		log.debug("正在进行中的课堂：" + classroomList.size());
		// 遍历课堂
		classroomList.stream().forEach(classroom -> {
			params.clear();
			params.put("ctId", classroom.getCtId());
			List<TevglTchClassroomTrainee> classroomTraineeList = tevglTchClassroomTraineeMapper.selectListByMap(params);
			List<String> ids = classroomTraineeList.stream().map(classroomTrainee -> classroomTrainee.getTraineeId()).collect(Collectors.toList());
			log.debug("课程成员：" + ids);
			if (ids != null && ids.size() > 0) {
				// 去重
				traineeIds.removeAll(ids);
			}
			log.debug("班级成员：" + traineeIds);
			if (traineeIds != null && traineeIds.size() > 0) {
				// 入库
				traineeIds.stream().forEach(traineeId -> {
					TevglTchClassroomTrainee t = new TevglTchClassroomTrainee();
					t.setId(Identities.uuid());
					t.setCtId(classroom.getCtId());
					t.setTraineeId(traineeId);
					t.setClassId(classId);
					t.setJoinDate(DateUtils.getNow("yyyy-MM-dd"));
					t.setCreateTime(DateUtils.getNowTimeStamp());
					t.setState("Y");
					tevglTchClassroomTraineeMapper.insert(t);
				});	
			}
		});
	}
	
	/**
	 * 
	 * @param cell
	 * @return
	 */
	private String getValueV4(XSSFCell cell) {
		String value = "";
        if (null == cell) {
            return value;
        }
        switch (cell.getCellType()) {
            // 数值型
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 如果是date类型则 ，获取该cell的date值
                    Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = format.format(date);
                } else {// 纯数字
                    BigDecimal big = new BigDecimal(cell.getNumericCellValue() + "");
                    value = big.toString();
                    // 解决1234.0 去掉后面的.0
                    if (null != value && !"".equals(value.trim())) {
                        String[] item = value.split("[.]");
                        if (1 < item.length && "0".equals(item[1])) {
                            value = item[0];
                        }
                    }
                }
                break;
            // 字符串类型
            case STRING:
                value = cell.getStringCellValue().toString();
                break;
            // 公式类型
            case FORMULA:
                // 读公式计算值
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
                    value = cell.getStringCellValue().toString();
                }
                break;
            // 布尔类型
            case BOOLEAN:
                value = " " + cell.getBooleanCellValue();
                break;
            // 空值
            case BLANK:
                value = "";
                break;
            // 故障
            case ERROR:
                value = "";
                System.err.println("excel出现故障");
                break;
            default:
                value = cell.getStringCellValue().toString();
        }
        if ("null".endsWith(value.trim())) {
            value = "";
        }
        return value;
	}
	
	/**
	 * poi-ooxml 3.10-FINAL
	 * @param cell
	 * @return
	 */
	/*private String getValue(XSSFCell cell) {
	    String value = "";
	    if (null == cell) {
	        return value;
	    }
	    switch (cell.getCellType()) {
	        // 数值型
	        case XSSFCell.CELL_TYPE_NUMERIC:
	            if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                // 如果是date类型则 ，获取该cell的date值
	                Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
	                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                value = format.format(date);
	            } else {// 纯数字
	                BigDecimal big = new BigDecimal(cell.getNumericCellValue() + "");
	                value = big.toString();
	                // 解决1234.0 去掉后面的.0
	                if (null != value && !"".equals(value.trim())) {
	                    String[] item = value.split("[.]");
	                    if (1 < item.length && "0".equals(item[1])) {
	                        value = item[0];
	                    }
	                }
	            }
	            break;
	        // 字符串类型
	        case XSSFCell.CELL_TYPE_STRING:
	            value = cell.getStringCellValue().toString();
	            break;
	        // 公式类型
	        case XSSFCell.CELL_TYPE_FORMULA:
	            // 读公式计算值
	            value = String.valueOf(cell.getNumericCellValue());
	            if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
	                value = cell.getStringCellValue().toString();
	            }
	            break;
	        // 布尔类型
	        case XSSFCell.CELL_TYPE_BOOLEAN:
	            value = " " + cell.getBooleanCellValue();
	            break;
	        // 空值
	        case XSSFCell.CELL_TYPE_BLANK:
	            value = "";
	            break;
	        // 故障
	        case XSSFCell.CELL_TYPE_ERROR:
	            value = "";
	            System.err.println("excel出现故障");
	            break;
	        default:
	            value = cell.getStringCellValue().toString();
	    }
	    if ("null".endsWith(value.trim())) {
	        value = "";
	    }
	    return value;
	}*/
	
	/**
	 * <i>手机格式验证</i>
	 * @param mobile
	 * @return
	 */
	private boolean isMobile(String mobile) {
		Pattern p = Pattern.compile("^1[3456789]\\d{9}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}
	
	/**
	 * 将文本的性别转为对的字典
	 * @param name
	 * @return
	 */
	private String convertSex(String name) {
		String dictCode = "0";
		if ("男".equals(name)) {
			dictCode = "1";
		} else if ("女".equals(name)) {
			dictCode = "2";
		}
		return dictCode;
	}
	
	/**
	 * excel导出
	 *
	 * @param map
	 * @return
	 */
	@Override
	public List<TevglTraineeInfoVo> selectClassTraineeListByMap(Map<String, Object> map) {
		List<TevglTraineeInfoVo> list = tevglTchClasstraineeMapper.selectClassTraineeListByMap(map);
		return list;
	}


	/**
	 * 批量新增班级成员
	 * @param classId
	 * @param traineeIdList
	 * @return
	 * @throws BudaosException
	 */
	@Override
	public R saveClassTraineeBatch(String classId, List<String> traineeIdList) throws BudaosException {
		if (StrUtils.isEmpty(classId)) {
			return R.error("请选择班级");
		}
		if (traineeIdList == null || traineeIdList.size() == 0) {
			return R.error("请选择学员");
		}
		// 去重
		List<Object> userSelectedTraineeIds = traineeIdList.stream().distinct().collect(Collectors.toList());
		// 已经在该班级的成员
		Map<String, Object> map = new HashMap<>();
		map.put("classId", classId);
		List<TevglTchClasstrainee> classTraineeList = tevglTchClasstraineeMapper.selectListByMap(map);
		List<String> existedTraineeIdList = classTraineeList.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
		//
		List<String> okTraineeIdList = new ArrayList<>();
		userSelectedTraineeIds.stream().forEach(userSelectedTraineeId -> {
			if (!existedTraineeIdList.stream().anyMatch(existedTraineeId -> existedTraineeId.equals(userSelectedTraineeId))) {
				okTraineeIdList.add(userSelectedTraineeId.toString());
			}
		});
		if (okTraineeIdList.size() > 0) {
			List<TevglTchClasstrainee> insertList = new ArrayList<>();
			okTraineeIdList.stream().forEach(traineeId -> {
				TevglTchClasstrainee t = new TevglTchClasstrainee();
				t.setCtId(Identities.uuid());
				t.setClassId(classId);
				t.setTraineeId(traineeId);
				t.setCreateTime(DateUtils.getNowTimeStamp());
				insertList.add(t);
			});
			tevglTchClasstraineeMapper.insertBatch(insertList);
		}
		return R.ok("保存成功");
	}

	
}
