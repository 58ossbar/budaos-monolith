package com.budaos.modules.evgl.web.controller.tchclass;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.book.api.TevglBookMajorService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.common.CbUploadUtils;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.tch.api.*;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomSb;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>我的课堂 </p> 
 * <p>Company: 布道师学习通</p>  
 * <p>Copyright: Copyright (c) 2019</p>
 * @author huj
 * @date 2019年8月19日
 */
@RestController
@RequestMapping("/classroom-api")
public class ClassroomController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private TevglTchClassroomService tevglTchClassroomService;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglTchClassService tevglTchClassService;
	@Autowired
	private TevglBookMajorService tevglBookMajorService;
	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private UploadFileUtils uploadFileUtils;
	@Autowired
	private CbUploadUtils evglUploadUtils;
	@Autowired
	private TevglTchClassroomTraineeService tevglTchClassroomTraineeService;
	@Autowired
	private TevglTchClassroomTopService tevglTchClassroomTopService;
	@Autowired
	private TevglTchClassroomResourceService tevglTchClassroomResourceService;
	@Autowired
	private TevglTchClassroomRoleprivilegeService tevglTchClassroomRoleprivilegeService;
	@Autowired
	private TevglTchClassroomSbService tevglTchClassroomSbService;
	
	/**
	 * <p>我的课堂列表（我教的课、我听的课）</p>  
	 * @author huj
	 * @data 2019年8月20日
	 * @param params {"majorId": "", "subjectId": "", "pageNum": "", "pageSize": "", "name": "", "sort": "newest/hot"}
	 * @param type 1加入的课堂（我听的课）2自己创建的课堂（我教的课），为空则查全部	
	 * @return
	 */
	@GetMapping("listMyClassroom")
	@CheckSession
	public R listClassroom(HttpServletRequest request, 
			@RequestParam Map<String, Object> params, String type) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		String loginUserId = traineeInfo.getTraineeId();
		params.put("channel", "pc");
		return tevglTchClassroomService.listClassroom(params, loginUserId, type).put("type", type);
	}

	/**
	 * 职业路径列表
	 * @param param
	 * @return
	 */
	@GetMapping("/listMajors")
	public R listMajors(@RequestParam Map<String, Object> param) {
		return R.ok().put(Constant.R_DATA, tevglBookMajorService.selectListMapByMapForWeb(param)); 
	}
	
	/**
	 * 课程列表
	 * @param param {"majorId": ""}
	 * @return
	 */
	@GetMapping("/listSubjects")
	public R listSubjects(@RequestParam Map<String, Object> params) {
		return tevglBookSubjectService.listSelectSubject(params); 
	}
	
	/**
	 * 课程列表
	 * @param param {"majorId": ""}
	 * @return
	 */
	@GetMapping("/listClassroomState")
	public R listClassroomState() {
		List<Map<String,Object>> dictList = dictService.getDictList("classroomState");
		// 过滤掉已结束
		//List<Map<String,Object>> list = dictList.stream().filter(a -> !a.get("dictCode").equals("3")).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	/**
	 * 根据邀请码搜索课堂
	 * @param request
	 * @param invitationCode
	 * @return
	 */
	@GetMapping("/serach")
	@CheckSession
	public R serach(HttpServletRequest request, String invitationCode) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		Map<String, Object> params = new HashMap<>();
		params.put("invitationCode", invitationCode);
		return tevglTchClassroomService.listClassroom(params, traineeInfo.getTraineeId(), null);
	}
	
	/**
	 * 上方区域课堂信息等其它信息
	 * @param id 课堂主键
	 * @return
	 */
	@GetMapping("/viewClassroomInfo/{id}")
	@CheckSession
	public R viewClassroomInfo(HttpServletRequest request, @PathVariable("id") String id) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomService.viewClassroomInfo(id, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看课堂信息（修改课堂信息时可用）
	 * @param ctId 课堂主键
	 * @return
	 */
	@GetMapping("/viewClassroomBaseInfo")
	@CheckSession
	public R viewClassroomBaseInfo(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomService.viewClassroomBaseInfo(ctId, traineeInfo.getTraineeId());
	}
	
	
	/**
	 * <p>我的书架列表</p>  
	 * @author huj
	 * @data 2019年8月20日	
	 * @param request
	 * @return
	 */
	@GetMapping("/listMyBookrack")
	@CheckSession
	public R listMyBookrack(HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("state", "Y");
		map.put("isSubjectRefNotNull", "Y");
		map.put("createUserId", traineeInfo.getTraineeId());
		return R.ok().put(Constant.R_DATA, tevglBookSubjectService.query(map));
	}
	
	/**
	 * <p>保存课堂</p>  
	 * @author huj
	 * @data 2019年8月19日	
	 * @param tevglTchClassroom
	 * @param request
	 * @return
	 */
	@PostMapping("/save")
	@CheckSession
	public R saveClassroomInfo(TevglTchClassroom tevglTchClassroom, HttpServletRequest request,
			@RequestPart(name = "file", required = false)MultipartFile file) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		try {
			// 课堂封面上传
			String type = "14";
			String fileName = "";
			String attachId = "";
			if (file != null && !file.isEmpty()) {
				R r = evglUploadUtils.uploads(file, type, null, null);
				Integer code = (Integer) r.get("code");
				if (code != 0) {
					return r;
				}
				@SuppressWarnings("unchecked")
				Map<String, Object> object = (Map<String, Object>) r.get("data");
				fileName = (String) object.get("fileName");
				attachId = (String) object.get("attachId");
				if (StrUtils.isNotEmpty(fileName)) {
					tevglTchClassroom.setPic(fileName);
				}
				if (StrUtils.isNotEmpty(attachId)) {
					tevglTchClassroom.setAttachId(attachId);
				}
			}
			if (StrUtils.isEmpty(tevglTchClassroom.getCtId())) { // 新增
				//return tevglTchClassroomService.saveClassroomInfo(tevglTchClassroom, traineeInfo.getTraineeId());
				return tevglTchClassroomService.saveClassroomInfoNew(tevglTchClassroom, traineeInfo.getTraineeId());
			} else { // 修改
				tevglTchClassroom.setUpdateUserId(traineeInfo.getTraineeId());
				return tevglTchClassroomService.updateClassroomInfo(tevglTchClassroom, traineeInfo.getTraineeId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("系统错误");
		}
	}
	
	/**
	 * <p>职业路径下拉列表</p>  
	 * @author huj
	 * @data 2019年8月20日	
	 * @return
	 */
	@GetMapping("/getMajorList")
	public R getMajorList(@RequestParam Map<String, Object> map) {
		map.put("state", "Y"); // 状态(Y有效N无效)
		return R.ok().put(Constant.R_DATA, tevglBookMajorService.selectListByMap(map));
	}
	
	/**
	 * <p>班级下拉列表</p>  
	 * @author huj
	 * @data 2019年8月19日	
	 * @return
	 */
	@GetMapping("/getClassList")
	@CheckSession
	public R getClassListData(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassService.queryClassListData(params, traineeInfo.getTraineeId());
	}
	
	/**
	 * <p>获取自己创建的教材---应该已废弃等待删除</p>  
	 * @author huj
	 * @data 2019年8月19日	
	 * @return
	 */
	@GetMapping("/getBookList")
	@CheckSession
	public R getBookList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		map.put("state", "Y"); // 状态(Y已发布N未发布)
		map.put("createUserId", traineeInfo.getTraineeId());
		return R.ok().put(Constant.R_DATA, tevglBookSubjectService.getLiveSubjectList(map));
	}
	
	/**
	 * <p>从字典获取课堂是否发布</p>  
	 * @author huj
	 * @data 2019年8月19日	
	 * @return
	 */
	@GetMapping("/getIsPublic")
	public R getIsPublic() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("isPublic"));
	}
	
	/**
	 * <p>获取邀请码</p>  
	 * @author huj
	 * @data 2019年8月20日	
	 * @return
	 */
	@GetMapping("/getInvitationCode")
	public R getInvitationCode() {
		// TODO
		int num = (int) ((Math.random() * 9 + 1) * 100000);
		return R.ok().put(Constant.R_DATA, num);
	}
	
	/**
	 * <p>图片上传</p>  
	 * @author huj
	 * @data 2019年8月20日	
	 * @param picture
	 * @return
	 */
	@PostMapping("/uploadPic")
	public R uploadPic(@RequestPart(value = "file", required = false) MultipartFile picture) {
		String type = "14"; // 课堂封面
		return uploadFileUtils.uploadImage(picture, null, type, 0, 0);
	}
	
	/**
	 * 加入课堂
	 * @param ctId
	 * @param invitationCode
	 * @return
	 */
	@RequestMapping("joinTheClassroom")
	@CheckSession
	public R joinTheClassroom(HttpServletRequest request, String ctId, String invitationCode) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("ctId",  ctId);
		map.put("invitationCode",  invitationCode);
		return tevglTchClassroomTraineeService.joinTheClassroom(map, traineeInfo.getTraineeId());
	}
	
	/**
	 * 课堂置顶
	 * @param ctId
	 * @param value
	 * @return
	 */
	@PostMapping("/setTop")
	public R setTop(HttpServletRequest request, String ctId, String value) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if ("Y".equals(value)) {
			return tevglTchClassroomTopService.setTop(ctId, traineeInfo.getTraineeId());
		} else {
			return tevglTchClassroomTopService.cancelTop(ctId, traineeInfo.getTraineeId());
		}
	}
	
	
	/**
	 * 删除课堂
	 * @param ctId
	 * @param token
	 * @return
	 */
	@PostMapping("/deleteClassroom")
	@CheckSession
	public R deleteClassroom(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomService.deleteClassroom(ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 开始课堂
	 * @param ctId
	 * @param token
	 * @return
	 */
	@RequestMapping("/start")
	@CheckSession
	public R start(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomService.start(ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 结束课堂
	 * @param ctId
	 * @param token
	 * @return
	 */
	@RequestMapping("/end")
	@CheckSession
	public R end(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomService.end(ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 获取年份
	 * @param request
	 * @param type type 类型值对应：1时查询加入的课堂（我听的课）的年份 2自己创建的课堂（我教的课）的年份，为空则查全部
	 * @return
	 */
	@RequestMapping("/getDates")
	@CheckSession
	public R getDates(HttpServletRequest request, String type) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return R.ok().put(Constant.R_DATA, tevglTchClassroomService.getDates(traineeInfo.getTraineeId(), type));
	}
	
	/**
	 * @author zhouyl加
	 * @date 2021年2月20日
	 * 查询当前登录用户创建的所有课堂信息
	 * @param ctId 课程id
	 * @param loginUserId
	 * @return
	 */
	@GetMapping("queryClassroomList")
	@CheckSession
	public R queryClassroomList(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomService.queryClassroomList(ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * @author zhouyl加
	 * @date 2021年2月20日
	 * 一键加入课堂
	 * @param jsonObject
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	@PostMapping("oneClickToJoinClassroom")
	@CheckSession
	public R oneClickToJoinClassroom(HttpServletRequest request, @RequestBody JSONObject jsonObject, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomService.oneClickToJoinClassroom(jsonObject, ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 
	 * @param request
	 * @param ctId
	 * @return
	 */
	@PostMapping("/getTreeData")
	@CheckSession
	public R getTreeData(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomResourceService.getTreeData(ctId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 保存
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/savePermissionSet")
	@CheckSession
	public R savePermissionSet(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomRoleprivilegeService.savePermissionSet(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查询权限
	 * @param request
	 * @param ctId
	 * @return
	 */
	@PostMapping("/queryPermissionList")
	@CheckSession
	public R queryPermissionList(HttpServletRequest request, String ctId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomRoleprivilegeService.queryPermissionList(ctId, traineeInfo.getTraineeId());
	}
	
    @RequestMapping("/verifyAccessToClass")
    @CheckSession
    public R verifyAccessToClass(HttpServletRequest request, String ctId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglTchClassroomService.verifyAccessToClass(ctId, traineeInfo.getTraineeId());
    }
    
    /**
     * 查询课外教材
     * @param request
     * @param params
     * @return
     */
    @RequestMapping("/queryExtraBooks")
    @CheckSession
    public R findExtracurricularMaterials(HttpServletRequest request, @RequestParam Map<String, Object> params) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglTchClassroomSbService.queryExtraBooks(params, traineeInfo.getTraineeId());
    }
    
    @RequestMapping("/findExtraBooks")
    @CheckSession
    public R findExtraBooks(HttpServletRequest request, String ctId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglTchClassroomSbService.findExtraBooks(ctId, traineeInfo.getTraineeId());
    }
    
    @RequestMapping("/saveExtraBooksRelation")
	@CheckSession
	public R saveExtraBooksRelation(HttpServletRequest request, @RequestBody TevglTchClassroomSb sb) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglTchClassroomSbService.saveExtraBooksRelation(sb, traineeInfo.getTraineeId());
	}
}
