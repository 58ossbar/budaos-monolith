package com.budaos.modules.evgl.web.controller.threefeetplatform.resource;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.common.GlobalRoomPermission;
import com.budaos.modules.evgl.book.api.BookService;
import com.budaos.modules.evgl.book.api.TevglBookChapterService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.vo.SaveChapterVisibleVo;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.pkg.api.*;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeamDetail;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.domain.TevglPkgRes;
import com.budaos.modules.evgl.pkg.domain.TevglPkgResgroup;
import com.budaos.modules.evgl.tch.api.*;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 我要开课-教学中心-资源模块
 * @author huj
 *
 */
@RestController
@RequestMapping("/resourceCenter-api")
public class ResourceController {

	private Logger log = LoggerFactory.getLogger(ResourceController.class);
	
	@Autowired
	private TevglPkgInfoService tevglPkgInfoService;
	@Autowired
	private TevglPkgResService tevglPkgResService;
	@Autowired
	private TevglTchClasstraineeService tevglTchClasstraineeService;
	@Autowired
	private TevglPkgResgroupService tevglPkgResgroupService;
	@Autowired
	private TevglTchClassroomService tevglTchClassroomService;
	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private TevglBookChapterService tevglBookChapterService;
	@Autowired
	private TevglTchClassroomTraineeService tevglTchClassroomTraineeService;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglBookpkgTeamDetailService tevglBookpkgTeamDetailService;
	@Autowired
	private TevglPkgResgroupAllowCopyService tevglPkgResgroupAllowCopyService;
	@Autowired
	private TevglPkgActivityRelationService tevglPkgActivityRelationService;
	@Autowired
	private BookService bookService;
	@Autowired
    private TevglTchTeacherService tevglTchTeacherService;
	@Autowired
	private TevglTchClassroomRoleprivilegeService tevglTchClassroomRoleprivilegeService;
	
	
	/**
	 * 从字典获取默认分组
	 * @param request
	 * @return
	 */
	@GetMapping("/listResourceGroup")
	@CheckSession
	public R listResourceGroup(HttpServletRequest request, String chapterId, String pkgId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if (StrUtils.isEmpty(chapterId) || StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}
		// 字典
		List<Map<String,Object>> dictList = dictService.getDictList("resourceGroup");
		// 返回数据
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		// 查询
		Map<String, Object> params = new HashMap<>();
		params.put("chapterId", chapterId);
		params.put("pkgId", pkgId);
		List<TevglPkgResgroup> list = tevglPkgResgroupService.selectListByMap(params);
		if (list != null && list.size() > 0) {
			// 筛选出已经存值的字典分组
			List<TevglPkgResgroup> existedList = list.stream().filter(a -> !StrUtils.isNull(a.getDictCode())).collect(Collectors.toList());
			if (dictList != null && dictList.size() > 0) {
				for (Map<String, Object> dict : dictList) {
					boolean flag = existedList.stream().anyMatch(a -> a.getDictCode().equals(dict.get("dictCode").toString()));
                    // 没添加过，才返回
					if (!flag) {
                        data.add(dict);
                    }
				}
			}
		} else {
			data.addAll(dictList);
		}
		return R.ok().put(Constant.R_DATA, data);
	}
	
	/**
	 * 教材的树形数据（教学包对应课程的树形数据目前使用的这个）
	 * @param subjectId
	 * @return
	 */
	@GetMapping("/getBookTreeData")
	@CheckSession
	public R getBookTreeData(HttpServletRequest request, String ctId, String pkgId, String subjectId, String chapterName) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// 旧版，暂时先不删除
		Map<String, Object> configuration = new HashMap<>();
		configuration.put("loginUserId", traineeInfo.getTraineeId());
		configuration.put("queryPermission", true);
		Map<String, Object> subjectInfo = tevglBookSubjectService.getBook(ctId, pkgId, subjectId, null, configuration);
		return R.ok().put(Constant.R_DATA, subjectInfo);
	}
	
	/**
	 * 教材的树形数据（教学包对应课程的树形数据目前使用的这个）
	 * @param subjectId
	 * @return
	 */
	@GetMapping("/getBookTreeDataNew")
	@CheckSession
	public R getBookTreeDataNew(HttpServletRequest request, String ctId, String pkgId, String subjectId, String chapterName) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if (StrUtils.isNotEmpty(ctId)) {
			TevglTchClassroom tevglTchClassroom = tevglTchClassroomService.selectObjectById(ctId);
            if (tevglTchClassroom == null) {
                return R.error("课堂不存在了！");
            }
            boolean isCreator = StrUtils.isEmpty(tevglTchClassroom.getReceiverUserId()) && tevglTchClassroom.getCreateUserId().equals(traineeInfo.getTraineeId());
            boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && traineeInfo.getTraineeId().equals(tevglTchClassroom.getTraineeId());
            boolean isReceiver = StrUtils.isNotEmpty(tevglTchClassroom.getReceiverUserId()) && tevglTchClassroom.getReceiverUserId().equals(traineeInfo.getTraineeId());
            // 如果是助教，校验下是否有设置学员是否可见的权限
            boolean hasSetVisiblePermission = false;
            if (isTeachingAssistant) {
                List<String> permissionList = tevglTchClassroomRoleprivilegeService.queryPermissionListByCtId(ctId);
                if (permissionList != null && permissionList.size() > 0) {
                    hasSetVisiblePermission = permissionList.stream().anyMatch(a -> a.equals(GlobalRoomPermission.ROOM_PERM_SUBJECT_CHAPTERVISIBLE));
                }
            }
            String identity = (isCreator || hasSetVisiblePermission || isReceiver) ? "teacher" : "trainee";
		    return tevglBookSubjectService.getBookForRoomPage(ctId, pkgId, subjectId, chapterName, traineeInfo.getTraineeId(), false, identity);
		} else {
		    return tevglBookSubjectService.getBookForPkgPage(pkgId, subjectId, chapterName, traineeInfo.getTraineeId());
		}
	}
	
	/**
	 * 章节内容，且返回了资源分组tab标签
	 * @param chapterId
	 * @return
	 */
	@GetMapping("/viewChapterInfo")
	@CheckSession
	public R viewChapterInfo(HttpServletRequest request, String chapterId, String pkgId, String type) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.viewChapterInfo(chapterId, traineeInfo.getTraineeId(), pkgId, type);
	}
	
	@GetMapping("/listChapters")
    @CheckSession
    public List<Map<String,Object>> listChaptersV2(HttpServletRequest request, String id, String serial, String type, String pkgId, String subjectId, String page) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return null;
        }
        // 如果是在课堂详情页面中触发此接口
        if (StrUtils.isNotEmpty(page) && "room".equals(page)) {
        	// 设置学员是否可见会用到
            return bookService.listChaptersForRoomPage(traineeInfo.getTraineeId(), id, pkgId, subjectId);
        } else {
            return bookService.listChaptersForPkgPage(traineeInfo.getTraineeId(), id,  serial,  type,  pkgId,  subjectId);
        }
    }
	
	/**
	 * 根据父节点查询子节点
	 * @param id 当前父节点的id值（可能是课程id和章节id）
	 * @param serial 原样返回的排序号
	 * @param type 类型，值为subject表示是在课程节点下新增章节节点，chapter表示在章节下新增子章节
	 * @param pkgId
	 * @param page 页面标识，room为课堂页面，pkg为教学包页面
	 * @return
	 */
	//@GetMapping("/listChapters")
	@CheckSession
	public List<Map<String,Object>> listChapters(HttpServletRequest request, String id, String serial, String type, String pkgId, String subjectId, String page) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return null;
		}

        // 如果是在课堂详情页面中触发此接口
        if (StrUtils.isNotEmpty(page) && "room".equals(page)) {
            return bookService.listChaptersForRoomPage(traineeInfo.getTraineeId(), id, pkgId, subjectId);
        }

        //  TODO 以下代码需要精简优化
		
		if (StrUtils.isEmpty(id) || StrUtils.isEmpty(pkgId)) {
			log.debug("必传参数为空");
			return null;
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoService.selectObjectById(pkgId);
		if (pkgInfo == null) {
			log.debug("无效的记录");
			return null;
		}
		TevglBookSubject tevglBookSubject = tevglBookSubjectService.selectObjectById(subjectId);
        if (tevglBookSubject == null) {
            return null;
        }
        boolean isBookCreator = traineeInfo.getTraineeId().equals(tevglBookSubject.getCreateUserId());
		// 返回结果
		List<Map<String,Object>> list = new ArrayList<>();
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		// 查活动
		params.clear();
		params.put("pkgId", pkgId);
		List<Map<String, Object>> activityList = tevglPkgActivityRelationService.selectSimpleListMap(params);
		// 获取当前登录拥有的章节权限
		params.clear();
		params.put("userId", traineeInfo.getTraineeId());
		List<TevglBookpkgTeamDetail> detailList = tevglBookpkgTeamDetailService.selectListByMap(params);
		List<String> chapterIds = detailList.stream().map(a -> a.getChapterId()).collect(Collectors.toList());
		// 获取当前被查看章节的子节点
		if ("subject".equals(type)) {
			params.clear();
			params.put("subjectId", id);
			// 获取具有层次结构的属性数据
			list = tevglBookSubjectService.getTree(id, pkgId);
			List<Map<String,Object>> res = new ArrayList<>();
			// 递归处理
            buildSb(list, id, serial, chapterIds, params, res, activityList);
            // 如果为创建者,默认有全部权限
			if (traineeInfo.getTraineeId().equals(pkgInfo.getCreateUserId())) {
				res.stream().forEach(node -> {
					node.put("isBookCreator", isBookCreator);
					if (StrUtils.isNotEmpty(page) && "room".equals(page)) {
						node.put("hasSetVisiblePermission", true);
					}
					// 来源教学包处于发布状态，不允许新增修改等操作
					if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
						node.put("hasPermission", false);
						node.put("hasNodePermission", false);
						// 但是
						if (pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
							node.put("hasPermission", true);
							node.put("hasNodePermission", true);	
						}
					} else {
						node.put("hasPermission", true);
						node.put("hasNodePermission", true);
					}
			    });
			}
		} else if ("chapter".equals(type)) {
			params.clear();
            params.put("parentId", id);
            params.put("pkgId", pkgId);
            params.put("sidx", "t1.order_num");
            params.put("order", "asc");
            list = tevglBookChapterService.selectSimpleListMap(params);
            List<Map<String,Object>> res = new ArrayList<>();
            build(list, id, serial, chapterIds, params, res, pkgId, activityList);
            // 如果为创建者,默认有全部权限
			if (traineeInfo.getTraineeId().equals(pkgInfo.getCreateUserId())) {
				res.stream().forEach(node -> {
					node.put("isBookCreator", isBookCreator);
					if (StrUtils.isNotEmpty(page) && "room".equals(page)) {
						node.put("hasSetVisiblePermission", true);
					}
	            	// 来源教学包处于发布状态，不允许新增修改等操作
					if (StrUtils.isNotEmpty(pkgInfo.getRefPkgId())) {
						node.put("hasPermission", false);
						node.put("hasNodePermission", false);
						// 但是
						if (pkgInfo.getPkgId().equals(pkgInfo.getRefPkgId())) {
							node.put("hasPermission", true);
							node.put("hasNodePermission", true);	
						}
					} else {
						node.put("hasPermission", true);
						node.put("hasNodePermission", true);
					}
	            });
			}
            return res;
		}
		return list;
	}
	
	/**
	 * 递归处理数据
	 * @param list 具有层次机构的树形章节数据
	 * @param parentId 父id
	 * @param serial 前端传递的序号
	 * @param chapterIds 当前登录用户所拥有的章节权限
	 * @param params 查询条件
	 * @param res 处理数据，返回结果
	 * @param pkgId 教学包id
	 * @param activityList 教学包对应的活动数据
	 * @return
	 */
	private List<Map<String,Object>> build(List<Map<String,Object>> list, String parentId, String serial, List<String> chapterIds, Map<String, Object> params, List<Map<String,Object>> res, String pkgId, List<Map<String, Object>> activityList) {
        if (list == null || list.size() == 0) {
            return res;
        }
        // 遍历
        for (int i = 0; i < list.size(); i++) {
        	// 本章节是否有活动
        	Map<String, Object> chapterInfo = list.get(i);
        	chapterInfo.put("hasActivity", activityList.stream().anyMatch(activityInfo -> !StrUtils.isNull(activityInfo.get("chapterId")) && activityInfo.get("chapterId").equals(chapterInfo.get("chapterId"))));
            // 返回标识
            list.get(i).put("type", "chapter");
            // 处理序号
            if (serial != null) {
                list.get(i).put("serial", serial + "." + (i+1));
                list.get(i).put("chapterName", serial + "." + (i+1) +" "+ list.get(i).get("chapterName"));
            } else {
                list.get(i).put("serial", (i+1));
                list.get(i).put("chapterName", (i+1) +" "+ list.get(i).get("chapterName"));
            }
            // 返回权限标识
            if (chapterIds.contains(list.get(i).get("chapterId"))) {
                list.get(i).put("hasPermission", true);
                list.get(i).put("hasNodePermission", true);
            } else {
                list.get(i).put("hasPermission", false);
                list.get(i).put("hasNodePermission", false);
            }
            if (!res.contains(list.get(i))) {
                res.add(list.get(i));
            }
            // 是否还有子节点
            params.clear();
            params.put("parentId", list.get(i).get("chapterId"));
            params.put("pkgId", pkgId);
            params.put("sidx", "t1.order_num");
            params.put("order", "asc");
            List<Map<String, Object>> nodeList = tevglBookChapterService.selectSimpleListMap(params);
            if (nodeList != null && nodeList.size() > 0) {
                List<Map<String,Object>> children = build(nodeList, list.get(i).get("chapterId").toString(), list.get(i).get("serial").toString(), chapterIds, params, res, pkgId, activityList);
                children.stream().forEach(a -> {
                    if (!res.contains(a)) {
                        res.add(a);
                    }
                });
            }
        }
        return res;
    }
	
	/**
	 * 递归处理数据
	 * @param list 具有层次机构的树形章节数据
	 * @param parentId 父id
	 * @param serial 前端传递的序号
	 * @param chapterIds
	 * @param params 查询条件
	 * @param res 处理后的数据，返回结果
	 * @param activityList 活动数据
	 * @return
	 */
	private List<Map<String,Object>> buildSb(List<Map<String,Object>> list, String parentId, String serial, List<String> chapterIds, Map<String, Object> params, List<Map<String,Object>> res, List<Map<String, Object>> activityList) {
		if (list == null || list.size() == 0) {
            return res;
        }
        // 遍历
        for (int i = 0; i < list.size(); i++) {
        	// 本章节是否有活动
        	Map<String, Object> chapterInfo = list.get(i);
        	chapterInfo.put("hasActivity", activityList.stream().anyMatch(activityInfo -> !StrUtils.isNull(activityInfo.get("chapterId")) && activityInfo.get("chapterId").equals(chapterInfo.get("chapterId"))));
            // 返回标识
            list.get(i).put("type", "chapter");
            // 处理序号
			/*if (serial != null) {
			    list.get(i).put("serial", serial + "." + (i+1));
			    list.get(i).put("chapterName", serial + "." + (i+1) +" "+ list.get(i).get("chapterName"));
			} else {
			    list.get(i).put("serial", (i+1));
			    list.get(i).put("chapterName", (i+1) +" "+ list.get(i).get("chapterName"));
			}*/
            // 返回权限标识
            if (chapterIds.contains(list.get(i).get("chapterId"))) {
                list.get(i).put("hasPermission", true);
                list.get(i).put("hasNodePermission", true);
            } else {
                list.get(i).put("hasPermission", false);
                list.get(i).put("hasNodePermission", false);
            }
            if (!res.contains(list.get(i))) {
                res.add(list.get(i));
            }
            // 是否还有子节点
            params.clear();
            params.put("parentId", list.get(i).get("chapterId"));
            params.put("sidx", "t1.order_num");
            params.put("order", "asc");
            @SuppressWarnings("unchecked")
			List<Map<String, Object>> nodeList = (List<Map<String, Object>>) list.get(i).get("children");
            if (nodeList != null && nodeList.size() > 0) {
                List<Map<String,Object>> children = buildSb(nodeList, list.get(i).get("chapterId").toString(), list.get(i).get("serial").toString(), chapterIds, params, res, activityList);
                children.stream().forEach(a -> {
                    if (!res.contains(a)) {
                        res.add(a);
                    }
                });
            }
        }
        return res;
	}
	
	/**
	 * 点击分组查询资源
	 * @param request
	 * @param resgroupId
	 * @return
	 */
	@GetMapping("/viewResInfo")
	@CheckSession
	public R viewResInfo(HttpServletRequest request, String resgroupId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		Map<String, Object> params = new HashMap<>();
		params.put("resgroupId", resgroupId);
		Map<String, Object> resInfo = tevglPkgResService.viewResInfo(params);
		return R.ok(resInfo);
	}
	
	/**
	 * 保存资源分组
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/saveResourceGroup")
	@CheckSession
	public R saveResourceGroup(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		String resgroupId = jsonObject.getString("resgroupId");
		// 新增
		if (StrUtils.isEmpty(resgroupId)) {
			return tevglPkgResgroupService.saveResGroup(jsonObject, traineeInfo.getTraineeId());
		} else { // 修改
			return tevglPkgResgroupService.editResGroup(jsonObject, traineeInfo.getTraineeId());	
		}
	}
	
	/**
	 * 删除资源分组
	 * @param request
	 * @param resgroupId
	 * @return
	 */
	@RequestMapping("/deleteResourceGroup")
	@CheckSession
	public R deleteResourceGroup(HttpServletRequest request, String resgroupId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgResgroupService.deleteResourceGroup(resgroupId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 重命名分组
	 * @param request
	 * @param resgroupId
	 * @param resgroupName
	 * @return
	 */
	@RequestMapping("renameResourceGroup")
	@CheckSession
	public R renameResourceGroup(HttpServletRequest request, String resgroupId, String resgroupName) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgResgroupService.renameResourceGroup(resgroupId, resgroupName, traineeInfo.getTraineeId());
	}
	
	/**
	 * 拖动排序
	 * @param request
	 * @param resgroupIds
	 * @return
	 */
	@RequestMapping("/sort")
	@CheckSession
	public R sort(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgResgroupService.sort(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 保存资源
	 * @param request
	 * @param tevglPkgRes
	 * @return
	 * @apiNote resId资源主键，pkgId所属教学包，resgroupId所属资源分组，资源内容resContent
	 */
	@PostMapping("/saveResource")
	@CheckSession
	public R saveResource(HttpServletRequest request, @RequestBody TevglPkgRes tevglPkgRes) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		tevglPkgRes.setUpdateUserId(traineeInfo.getTraineeId());
		return tevglPkgResService.editResInfo(tevglPkgRes, traineeInfo.getTraineeId(), tevglPkgRes.getPkgId());
	}
	
	/**
     * 批量设置章节对学员是否可见
     * @param request
     * @param pkgId
     * @return
     */
    @PostMapping("/getChapterTreeVisible")
    @CheckSession
    public R getChapterTreeVisibleForWeb(HttpServletRequest request, String pkgId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglBookChapterService.getChapterTreeVisibleForWeb(pkgId, traineeInfo.getTraineeId());
    }
	
	/**
	 * 设置章节对学员是否可见
	 * @param request
	 * @param pkgId
	 * @param chapterId
	 * @return
	 */
	@PostMapping("/setTraineesVisible")
	@CheckSession
	public R setTraineesVisible(HttpServletRequest request, String ctId, String pkgId, String chapterId, String isTraineesVisible) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.setChapterIsTraineesVisible(ctId, pkgId, chapterId, isTraineesVisible, traineeInfo.getTraineeId());
	}
	
	/**
	 * 批量设置章节对学员是否可见
	 * @param request
	 * @param vo
	 * @return
	 */
	@PostMapping("/setTraineesVisibleBatch")
	@CheckSession
	public R setTraineesVisibleBatch(HttpServletRequest request, @RequestBody SaveChapterVisibleVo vo) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.setTraineesVisibleBatchForWeb(vo, traineeInfo.getTraineeId());
	}
	
	/**
	 * 一键生成默认章节
	 * @param request
	 * @param pkgId
	 * @param chapterId
	 * @return
	 */
	@PostMapping("/create")
	@CheckSession
	public R create(HttpServletRequest request, String pkgId, String chapterId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.create(pkgId, chapterId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 更新是否可复制资源内容（Y可复制N不可复制）
	 * @param request
	 * @param ctId
	 * @param pkgId
	 * @param cpId
	 * @param value
	 * @return
	 */
	@PostMapping("/updateValue")
	@CheckSession
	public R updateValue(HttpServletRequest request, String ctId, String pkgId, String cpId, String value) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglPkgResgroupAllowCopyService.updateValue(ctId, pkgId, cpId, value, traineeInfo.getTraineeId());
	}
	
	/**
	 * 设置学员是否可见标签
	 * @param request
	 * @param ctId
	 * @param pkgId
	 * @param resgroupId
	 * @param isTraineesVisible
	 * @return
	 */
	@PostMapping("/setTraineesVisibleResgroup")
	@CheckSession
	public R setTraineesVisibleResgroup(HttpServletRequest request, String ctId, String pkgId, String resgroupId, String isTraineesVisible) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglBookChapterService.setTraineesVisibleResgroup(ctId, pkgId, resgroupId, isTraineesVisible, traineeInfo.getTraineeId());
	}
}
