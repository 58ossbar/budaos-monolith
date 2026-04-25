package com.budaos.modules.evgl.site.service;

import com.budaos.common.utils.ConvertUtil;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.book.persistence.TevglBookMajorMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubperiodMapper;
import com.budaos.modules.evgl.site.api.TevglIndexService;
import com.budaos.modules.evgl.site.persistence.TevglSiteAvdMapper;
import com.budaos.modules.evgl.site.persistence.TevglSitePartnerMapper;
import com.budaos.modules.evgl.site.persistence.TevglsiteResourceMapper;
import com.budaos.modules.evgl.stu.persistence.TevglStuStarMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchTeacherMapper;
import com.budaos.modules.sys.domain.TsysResource;
import com.budaos.utils.constants.Constant;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>【前端首页】接口实现类</p>
 * <p>Title: TevglIndexServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年7月3日
 */
@Service
public class TevglIndexServiceImpl implements TevglIndexService {

	@Autowired
	private TevglSiteAvdMapper tevglSiteAvdMapper; // 广告轮播图
	@Autowired
	private TevglTchTeacherMapper tevglTchTeacherMapper; // 布道师
	@Autowired
	private TevglStuStarMapper tevglStuStarMapper; // 就业明星，优秀学生，实训故事
	@Autowired
	private TevglsiteResourceMapper tevglsiteResourceMapper; // 导航菜单
	@Autowired
	private TevglSitePartnerMapper tevglSitePartnerMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private TevglBookMajorMapper tevglBookMajorMapper;
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TevglBookSubperiodMapper tevglBookSubperiodMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	
	
	
	/**
	 * <p>获取导航菜单</p>
	 * @author huj
	 * @data 2019年7月4日
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	@GetMapping("/getInitMenu")
	public R getInitMenu(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("display", "1");
		map.put("sidx", "order_num");
		map.put("order", "asc");
		List<TsysResource> menuList = tevglsiteResourceMapper.selectSiteListByMap(map); //  b43e1950c19d40deb8b9b701ffec5f65
		List<TsysResource> rootList = menuList.parallelStream().filter(a -> "5bb687aa97dd4084871bdc700789b5c4".equals(a.getParentId()))
		.collect(Collectors.toList());
		Map<String, List<TsysResource>> map2 = menuList.parallelStream().collect(Collectors.groupingBy(TsysResource::getParentId));
		String reqUrl = request.getRequestURI();
		rootList.forEach(a -> {
			a.setDisplay("N");
			if(map2.containsKey(a.getMenuId())) {
				a.setOpen(true);
			}else {
				a.setOpen(false);
			}
		});
		for(TsysResource a : rootList) {
			if(a.getUrl().indexOf(reqUrl) >= 0) {
				a.setDisplay("Y");
				break;
			}else if(map2.containsKey(a.getMenuId())){
				boolean flag = false;
				for(TsysResource res : map2.get(a.getMenuId())) {
					if(res.getUrl().indexOf(reqUrl) >= 0) {
						a.setDisplay("Y");
						flag = true;
					}
				}
				if(flag) {
					break;
				}
			}
		}
		Map<String, Object> data = new HashMap<String, Object>(); // 最终返回结果
		data.put("evglSiteMenuList", rootList);
		data.put("evglSiteMenuchildListList", menuList);
		// TODO
		rootList.forEach(a -> {
			List<TsysResource> childrenList = new ArrayList<TsysResource>();
			menuList.forEach(b -> {
				if (a.getMenuId().equals(b.getParentId())) {
					childrenList.add(b);
					a.setList(childrenList);
				}
			});
		});
		data.put("menuList", rootList);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	
	/**
	 * <p>首页</p>
	 * @author huj
	 * @data 2019年7月3日
	 * @param request
	 * @param response
	 * @param params
	 * @return 返回了广告轮播图、8个布道师、4个实训故事、6个校企合作信息
	 */
	@Override
	@GetMapping("/")
	public R index(@RequestParam Map<String, Object> params) {
		
		Map<String, Object> data = new HashMap<String, Object>(); // 最终返回结果
		Map<String, Object> map = new HashMap<String, Object>(); // 查询条件
		/*
		// 查询首页广告轮播图
		map.clear();
		map.put("sidx", "avd_num");
		map.put("order", "asc");
		map.put("avdState", "Y"); // 状态(Y已上线N已下线)
		map.put("avdBegintime", DateUtils.getNowTimeStamp());
		map.put("avdEndtime", DateUtils.getNowTimeStamp());
		map.put("menuId", "bfe52d8339744e6caa8211c6f6fbdcce"); // bfe52d8339744e6caa8211c6f6fbdcce该值对应首页
		List<TevglSiteAvd> avdList = tevglSiteAvdMapper.selectListByMap(map);
		String nowDateTime = DateUtils.getNowTimeStamp();
		// 过滤，只取有效时间内的广告图
		avdList = avdList.parallelStream().filter(a -> (nowDateTime.compareTo(((TevglSiteAvd) a).getAvdBegintime()) >= 0
				&& nowDateTime.compareTo(((TevglSiteAvd) a).getAvdEndtime()) <= 0)).collect(Collectors.toList());
		data.put("avdList", avdList);
		
		// 职业课程路径
		map.clear();
		map.put("state", "Y"); // 状态(Y有效N无效)
		map.put("showIndex", "Y"); // 是否推荐到首页
		//map.put("hot", "Y"); // 是否热门(Y是N否)
		map.put("sidx", "sort_num");
		map.put("order", "asc");
		PageHelper.startPage(1, 4);
		List<TevglBookMajor> tevglBookMajorList = tevglBookMajorMapper.selectListByMap(map);
		// 字典转换
		convertUtil.convertDict(tevglBookMajorList, "hot", "hot");
		if (tevglBookMajorList.size() > 0) {
			tevglBookMajorList.forEach(a -> {
				// 图片路径处理
				a.setMajorLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("11") + "/" + a.getMajorLogo());
				// 课程处理
				Map<String, Object> m = new HashMap<>();
				m.put("state", "Y");
				m.put("majorId", a.getMajorId());
				List<TevglBookSubperiod> list = tevglBookSubperiodMapper.selectListByMap(m);
				if (list.size() > 0 && list != null) {
					a.setSubjectTotalSize(list.size());
				} else {
					a.setSubjectTotalSize(0);
				}
			});
		}
		data.put("tevglBookMajorList", tevglBookMajorList);
		
		// 三尺讲台
		map.clear();
		map.clear();
		map.put("state", "Y"); // 状态(Y有效N无效)
		map.put("isPublic", "Y"); // 是否发布
		map.put("sidx", "create_time");
		map.put("order", "desc");
		List<TevglTchClassroom> tevglTchClassroomList = tevglTchClassroomMapper.selectListByMap(map);
		if (tevglTchClassroomList.size() > 0) {
			tevglTchClassroomList.forEach(a -> {
				// 课堂图片路径处理
				a.setPic(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("14") + "/" + a.getPic()); // 图片处理
				// 上课教师信息
				TevglTchTeacher teacher = tevglTchTeacherMapper.selectObjectById(a.getTeacherId());
				if (teacher != null) {
					teacher.setTeacherPic(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("7") + "/" + teacher.getTeacherPic());
				}
				a.setTevglTchTeacher(teacher);
			});
			
		}
		data.put("tevglTchClassroomList", tevglTchClassroomList);
		*/
		/*
		// 教学包 8个
		map.clear();
		map.put("state", "Y");
		map.put("showIndex", "Y"); // 是否推荐到首页
		map.put("sidx", "pkg_ref_count");
		map.put("order", "desc");
		Integer limit = (Integer) params.get("limit");
		if (limit != null) {
			PageHelper.startPage(1, limit);	
		} else {
			PageHelper.startPage(1, 10);	
		}
		List<Map<String, Object>> tevglPkgInfoList = tevglPkgInfoMapper.selectListMapByMapForEvglWeb(map);
		convertUtil.convertDict(tevglPkgInfoList, "subject_property", "subjectProperty"); // 课程属性(选修or必修)
		convertUtil.convertDict(tevglPkgInfoList, "pkg_limit", "pkgLimit"); // 使用限制(来源字典：授权，购买，免费)
		convertUtil.convertDict(tevglPkgInfoList, "deploy_main_type", "deployMainType"); // 使用限制(来源字典：授权，购买，免费)
		if (tevglPkgInfoList.size() > 0) {
			tevglPkgInfoList.forEach(a -> {
				// 图片处理
				String logo = (String) a.get("pkg_logo");
				a.put("pkg_logo", budaosFieAccessPath + uploadPathUtils.getPathByParaNo("12") + "/" + logo);
				// 查询每个教学包的创建人的信息
				//TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById((String) a.get("create_user_id"));
				//if (traineeInfo != null) {
				//	Map<String, Object> m = new HashMap<>();
				//	m.put("trainneId", traineeInfo.getTraineeId());
				//	m.put("traineeName", traineeInfo.getTraineeName());
				//}
			});
		}
		data.put("tevglPkgInfoList", tevglPkgInfoList);
		*/
		/*
		// 教师信息 (布道师) 8个
		map.clear();
		map.put("state", "Y"); // 教师状态(Y有效N无效)
		map.put("showIndex", "Y"); // 是否推荐到首页(Y是N否)
		map.put("sidx", "sort_num");
		map.put("order", "asc");
		PageHelper.startPage(1, 8);
		List<TevglTchTeacher> teacherList = tevglTchTeacherMapper.selectListByMap(map);
		teacherList.forEach(a -> {
			a.setTeacherPic(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("7") + "/" + a.getTeacherPic());
		});
		data.put("teacherList", teacherList);
		
		// 就业明星(实训故事) 4个
		map.clear();
		PageHelper.startPage(1, 4);
		map.put("state", "Y");
		List<TevglStuStar> startList = tevglStuStarMapper.selectListByMap(map);
		if(startList==null || startList.size()<4) {
			int size = 0;
			if(startList==null) {
				size = 4;
			}else {
				size = 4-startList.size();
			}
			PageHelper.startPage(1, size);
			map.put("state", "N");
			startList.addAll(tevglStuStarMapper.selectListByMap(map));
		}
		startList.forEach(a -> {
			a.setStarPic(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("8") + "/" + a.getStarPic());
		});
		data.put("startList", startList);
		*/
		// 合作企业 6个
		map.clear();
		map.put("state", "Y"); // 状态(Y有效N无效)
		map.put("isKeyPoint", "Y"); // 是否首页显示
		PageHelper.startPage(1, 12); // 首页默认展示6个合作企业
		List<Map<String, Object>> partnerList = tevglSitePartnerMapper.selectSimpleListMapByMap(map);
		partnerList.stream().forEach(a -> {
			a.put("companyLogo", budaosFieAccessPath + uploadPathUtils.getPathByParaNo("9") + "/" + a.get("companyLogo"));
		});
		data.put("partnerList", partnerList);
		
		return R.ok().put(Constant.R_DATA, data);
	}

}
