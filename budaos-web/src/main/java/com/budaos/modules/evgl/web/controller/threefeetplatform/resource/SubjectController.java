package com.budaos.modules.evgl.web.controller.threefeetplatform.resource;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.book.api.TevglBookChapterService;
import com.budaos.modules.evgl.book.api.TevglBookMajorService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.book.api.TevglBookSubperiodService;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookMajor;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.vo.SaveChapterVo;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.site.api.TevglSiteNewsService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>课程</p>
 * <p>Title: SubjectController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年7月5日
 */
@RestController
@RequestMapping("subject-api")
public class SubjectController {

	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private TevglBookSubperiodService tevglBookSubperiodService;
	@Autowired
	private TevglBookChapterService tevglBookChapterService;
	@Autowired
	private TevglSiteNewsService tevglSiteNewsService;
	@Autowired
	private TevglBookMajorService tevglBookMajorService;
	@Autowired
	private DictService dictService;
	@Autowired
	private UploadFileUtils uploadFileUtils;
	
	/**
	 * <p>查看列表</p>
	 * @author huj
	 * @data 2019年7月10日
	 * @param params
	 * @return
	 */
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		params.put("state", "Y"); // 状态(Y已发布N未发布)
		return tevglBookSubjectService.query(params);
	}

	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param map
	 * @return
	 */
	@GetMapping("/query")
	public R query(@RequestParam Map<String, Object> map) {
		return tevglBookSubjectService.selectListByMapForWeb(map);
	}
	
	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param map
	 * @return
	 */
	@GetMapping("/querySelf")
	@CheckSession
	public R querySelf(HttpServletRequest request, @RequestParam Map<String, Object> map) {
		TevglTraineeInfo loginUser = LoginUtils.getLoginUser(request);
		if(loginUser == null) {
			return R.error("未登陆");
		}
		map.put("createUserId", loginUser.getTraineeId());
		return tevglBookSubjectService.selectListByMapForWeb(map);
	}
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年7月10日
	 * @param id
	 * @return
	 */
	@GetMapping("/view")
	public R view(String id, String pkgId) {
		return tevglBookSubjectService.viewForEvglWeb(id);
	}
	
	/**
	 * <p>根据字典值获取专业方向</p>  
	 * @author huj
	 * @data 2019年12月11日	
	 * @param params
	 * @return
	 */
	@GetMapping("/getMajor")
	public R getMajor(@RequestParam Map<String, Object> params) {
		List<TevglBookMajor> tevglBookMajorList = tevglBookMajorService.selectListByMap(params);
		List<Map<String, Object>> list = tevglBookMajorList.stream().map(a -> {
			Map<String, Object> info = new HashMap<>();
			info.put("majorId", a.getMajorId());
			info.put("majorName", a.getMajorName());
			return info;
		}).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * <p>获取技术</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param map
	 * @return
	 */
	@GetMapping("/getSubjectTechnology")
	public R getSubjectTechnology(@RequestParam Map<String, Object> map) {
		return R.ok().put(Constant.R_DATA, tevglBookSubperiodService.selectListForEvglWeb(map));
	}
	
	/**
	 * <p>获取课程属性(选修or必修)</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @return
	 */
	@GetMapping("/getSubjectProperty")
	public R getSubjectProperty() {
		return R.ok().put(Constant.R_DATA,  dictService.getDictList("subjectProperty"));
	}
	
	/**
	 * 获取章节（只查询一级章节）
	 * @param param {"chapterId": ""}
	 * @author zyl改
	 * @data 2020年11月13日
	 * @return
	 */
	@GetMapping("/listChapters")
	public R listChapters(@RequestParam Map<String, Object> params) {
		if (!StrUtils.isNull(params.get("subjectId"))) {
			params.put("parentId", params.get("subjectId"));
		}
		return tevglBookChapterService.listSelectChapter(params); 
	}
	
	/**
	 * <p>获取课程类型(来源字典:学校，平台等)</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @return
	 */
	@GetMapping("/getSubjectType")
	public R getSubjectType() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("subjectType2"));
	}
	
	/**
	 * <p>从字典获取技术</p>
	 * @author huj
	 * @data 2019年8月5日
	 * @return
	 */
	@GetMapping("/getTechnology")
	public R getTechnology() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("subjectTechnology"));
	}
	
	/**
	 * <p>保存章节</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param tevglBookChapter
	 * @return
	 */
	@PostMapping("/saveChapter")
	@CheckSession
	public R saveChapter(@RequestBody(required = false) TevglBookChapter tevglBookChapter, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// 如果是追加节点
		if (StrUtils.isNotEmpty(tevglBookChapter.getOperationType()) && "appendPeerNode".equals(tevglBookChapter.getOperationType())) {
			tevglBookChapter.setCreateUserId(traineeInfo.getTraineeId());
			return tevglBookChapterService.appendPeerNodes(tevglBookChapter);
		}
		// 如果是新增节点
		if (StrUtils.isEmpty(tevglBookChapter.getChapterId())) {
			tevglBookChapter.setCreateUserId(traineeInfo.getTraineeId());
			return tevglBookChapterService.saveChapterInfo(tevglBookChapter, traineeInfo.getTraineeId());
		} else {
			// 否则是修改节点
			tevglBookChapter.setUpdateUserId(tevglBookChapter.getCreateUserId());
			//return tevglBookChapterService.update(tevglBookChapter);
			return tevglBookChapterService.rename(tevglBookChapter.getPkgId(), tevglBookChapter.getChapterId(), tevglBookChapter.getChapterName(), traineeInfo.getTraineeId());
		}
	}
	
	/**
	 * 批量保存章节
	 * @param request
	 * @param vo
	 * @return
	 */
	@PostMapping("/batchSaveChapter")
	@CheckSession
	public R batchSaveChapter(HttpServletRequest request, @RequestBody SaveChapterVo vo) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.batchSaveChapter(vo, traineeInfo.getTraineeId());
	}
	
	/**
	 * <p>查看章节明细</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param id 章节主键
	 * @param type 标识：pkg表示是从教学包详情页面查看章节
	 * @return
	 */
	@GetMapping("/viewChapter")
	@CheckSession
	public R viewChapter(HttpServletRequest request, String id, String pkgId, String type) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.viewChapterInfo(id, traineeInfo.getTraineeId(), pkgId, type);
	}
	
	/**
	 * <p>获取发布方式</p>
	 * @author huj
	 * @data 2019年7月20日
	 */
	@GetMapping("/getDeployed")
	public R getDeployed() {
		return R.ok().put(Constant.R_DATA, tevglSiteNewsService.getDeployed());
	}
	
	/**
	 * <p>获取课程状态</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @return
	 */
	@GetMapping("/getState")
	public R getState() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("state0"));
	}
	

	/**
	 * <p>保存章节</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param tevglBookChapter
	 * @return
	 */
	@PostMapping("/updateChapter")
	public R updateChapter(@RequestBody(required = false) TevglBookChapter tevglBookChapter) {
		return tevglBookChapterService.update(tevglBookChapter);
	}
	
	/**
	 * <p>重命名节点</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param id 教材或章节主键
	 * @param name 名称
	 * @param type 1表示重命名教材2表示重命名章节
	 * @return
	 */
	@GetMapping("/rename")
	public R rename(HttpServletRequest request, String pkgId, String id, String name, String type) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if ("1".equals(type)) {
			return tevglBookSubjectService.rename(id, name);
		}
		if ("2".equals(type)) {
			return tevglBookChapterService.rename(pkgId, id, name, traineeInfo.getTraineeId());	
		}
		return R.ok();
	}
	
	/**
	 * <p>删除节点</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param pkgId 教学包主键
	 * @param id
	 * @param type 1课程2章节
	 * @return
	 */
	@GetMapping("/remove")
	@CheckSession
	public R remove(HttpServletRequest request, String pkgId, String id, String type) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if ("1".equals(type)) {
			return tevglBookSubjectService.remove(id, traineeInfo.getTraineeId());
		}
		// 删除章节
		if ("2".equals(type)) {
			//return tevglBookChapterService.remove(pkgId, id, traineeInfo.getTraineeId());
			return tevglBookChapterService.removeV2(pkgId, id, traineeInfo.getTraineeId());
		}
		return R.ok();
	}
	
	/**
	 * <p>移动，目前只是简单的修改排序号</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param map
	 * @return
	 */
	@GetMapping("/move")
	@CheckSession
	public R move(HttpServletRequest request, @RequestParam("currId") String currId, @RequestParam("targetId") String targetId
			,@RequestParam("pkgId") String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.move(currId, targetId, traineeInfo.getTraineeId(), pkgId);
	}
	
	/**
	 * <p>图片上传</p>
	 * @author huj
	 * @data 2019年7月29日
	 * @param picture
	 * @param type 该值来源参数表，10
	 * @return
	 */
	@PostMapping("/uploadPic")
	public R uploadPic(@RequestPart(value = "file", required = false) MultipartFile picture) {
		String type = "10"; // 活教材封面图
		return uploadFileUtils.uploadImage(picture, null, type, 0, 0);
	}
	
	
	/**
	 * <p>从字典获取活教材封面图</p>
	 * @author huj
	 * @data 2019年8月6日
	 * @return
	 */
	@GetMapping("/getSubjectLogo")
	public R getSubjectLogo() {
		return R.ok().put(Constant.R_DATA, tevglBookSubjectService.getSubjectLogo());
	}
	
	/**
	 * <p>活教材的导出，word文档</p>
	 * @author huj
	 * @data 2019年8月7日
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping("/toexport")
	public R toexport(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		return tevglBookSubjectService.toexport(params, request, response);
	}
	
	/**
	 * <p>更新阅读量、星级、收藏数、点赞数、举报数等</p>  
	 * @author huj
	 * @data 2019年8月14日	
	 * @param tevglBookSubject
	 * @return
	 */
	@PostMapping("/updateNum")
	public R updateNum(@RequestBody TevglBookSubject tevglBookSubject) {
		tevglBookSubjectService.updateNum(tevglBookSubject);
		return R.ok();
	}
	
	/**
	 * <p>收藏</p>  
	 * @author huj
	 * @data 2019年8月15日	
	 * @param request
	 * @param map
	 * @return
	 */
	@GetMapping("/collect")
	@CheckSession
	public R collect(HttpServletRequest request, @RequestParam Map<String, Object> map) {
		TevglTraineeInfo userInfo = LoginUtils.getLoginUser(request);
		if (userInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		String targetId = (String) map.get("targetId");
		String toTraineeId = (String) map.get("toTraineeId");
		return tevglBookSubjectService.collect(targetId, userInfo.getTraineeId(), toTraineeId);
	}
	
	/**
	 * <p>取消收藏</p>  
	 * @author huj
	 * @data 2019年8月15日	
	 * @param request
	 * @param map
	 * @return
	 */
	@GetMapping("/cancelCollect")
	@CheckSession
	public R cancelCollect(HttpServletRequest request, @RequestParam Map<String, Object> map) {
		TevglTraineeInfo userInfo = LoginUtils.getLoginUser(request);
		if (userInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		String targetId = (String) map.get("targetId");
		String toTraineeId = (String) map.get("toTraineeId");
		return tevglBookSubjectService.cancelCollect(targetId, userInfo.getTraineeId(), toTraineeId);
	}
	

	/**
	 * 课程下拉列表
	 * @param params
	 * @return
	 */
	@GetMapping("/listSelectSubject")
	public R listSelectSubject(@RequestParam Map<String, Object> params) {
		params.put("state", "Y"); // 状态(Y有效N无效)
		return tevglBookSubjectService.listSelectSubject(params);
	}
	
	/**
	 * 追加同级节点
	 * @param request
	 * @param tevglBookChapter
	 * @return
	 */
	//@PostMapping("/appendPeerNodes")
	//@CheckSession
	public R appendPeerNodes(HttpServletRequest request, @RequestBody TevglBookChapter tevglBookChapter) {
		TevglTraineeInfo userInfo = LoginUtils.getLoginUser(request);
		if (userInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.appendPeerNodes(tevglBookChapter);
	}

	/**
	 * 升级节点（例如升级2.1.1三级，则变成2.1二级）
	 * @param request
	 * @param chapterId
	 * @return
	 */
	@PostMapping("/upgradeNodes")
	@CheckSession
	public R upgradeNodes(HttpServletRequest request, String chapterId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.upgradeNodes(chapterId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 降级节点（废弃！拖拽节点）
	 * @param request
	 * @param chapterId
	 * @param toParentId
	 * @return
	 */
	//@PostMapping("/demotionNodes")
	@CheckSession
	public R demotionNodes(HttpServletRequest request, String chapterId, String toParentId, String type) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.demotionNodes(chapterId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 托拽节点
	 * @param request
	 * @param pkgId
	 * @param soureNodeId
	 * @param targetId
	 * @param moveType
	 * @return
	 */
	@PostMapping("/dragNode")
	@CheckSession
	public R dragNode(HttpServletRequest request, String pkgId, String subjectId, String soureNodeId, String targetId, String moveType, String list) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.dragNode(pkgId, subjectId, soureNodeId, targetId, moveType, traineeInfo.getTraineeId(), list);
	}
	
}
