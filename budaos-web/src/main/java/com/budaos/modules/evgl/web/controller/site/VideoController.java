package com.budaos.modules.evgl.web.controller.site;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.evgl.common.CbUploadUtils;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.site.api.TevglSiteVideoRelationService;
import com.budaos.modules.evgl.site.api.TevglSiteVideoService;
import com.budaos.modules.evgl.site.domain.TevglSiteVideo;
import com.budaos.modules.evgl.site.domain.TevglSiteVideoRelation;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 视频相关控制类
 * @author huj
 *
 */
@RestController
@RequestMapping("/site/video")
public class VideoController {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TevglSiteVideoService tevglSiteVideoService;
	@Autowired
	private TevglSiteVideoRelationService tevglSiteVideoRelationService;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglTchClassroomService tevglTchClassroomService;
	@Autowired
	private CbUploadUtils uploadUtils;
	
	@Value("${com.budaos.file-upload-path}")
	private String uploadPath;
	@Value("${com.budaos.file-access-path}")
	private String accessPath;
	
	/**
	 * 视频列表
	 * @param request
	 * @param params
	 * @return
	 */
	@GetMapping("queryVideoList")
	@CheckSession
	public R queryVideoList(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		params.put("createUserId", traineeInfo.getTraineeId());
		params.put("sidx", "create_time");
		params.put("order", "desc");
		return tevglSiteVideoService.queryVideoList(params);
	}
	
	/**
	 * 保存
	 * @param request
	 * @param file
	 * @param tevglSiteVideo
	 * @return
	 */
	@PostMapping("saveVideo")
	@CheckSession
	public R saveVideo(HttpServletRequest request, @RequestPart(name="file", required = false)MultipartFile file, TevglSiteVideo tevglSiteVideo) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if (StrUtils.isEmpty(tevglSiteVideo.getVideoId())) {
			return doSave(file, tevglSiteVideo, traineeInfo.getTraineeId());
		} else {
			return doUpdate(file, tevglSiteVideo, traineeInfo.getTraineeId());
		}
	}

	private R doSave(MultipartFile multipartFile, TevglSiteVideo tevglSiteVideo, String loginUserId){
		if (multipartFile == null || multipartFile.isEmpty()) {
			return R.error("请上传视频");
		}
		R r = uploadUtils.uploads(multipartFile, 26, true);
		if (!r.get("code").equals(0)) {
			return r;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>)r.get(Constant.R_DATA);
		String fileName = (String) data.get("fileName");
		String originalFilename = (String) data.get("originalFilename");
		Long fileSize = (Long)data.get("fileSize");
		String fileSuffix = (String) data.get("fileSuffix");
		String firstCaptureAccessUrl = data.get("firstCaptureAccessUrl").toString();
		String firstCaptureSavePath = data.get("firstCaptureAccessUrl").toString();
		tevglSiteVideo.setTitle(StrUtils.isEmpty(tevglSiteVideo.getTitle()) ? originalFilename : tevglSiteVideo.getTitle());
		tevglSiteVideo.setName(fileName);
		tevglSiteVideo.setFileSuffix(fileSuffix);
		tevglSiteVideo.setFileSize(fileSize);
		tevglSiteVideo.setOriginalFilename(originalFilename);
		tevglSiteVideo.setCheckState("0");
		tevglSiteVideo.setState("Y");
		tevglSiteVideo.setCreateTime(DateUtils.getNowTimeStamp());
		tevglSiteVideo.setCreateUserId(loginUserId);
		tevglSiteVideo.setFirstCaptureAccessUrl(firstCaptureAccessUrl);
		tevglSiteVideo.setFirstCaptureSavePath(firstCaptureSavePath);
		tevglSiteVideo.setType("1"); // 老师只允许上传辅教视频，不允许上传公共视频
		// 入库
		tevglSiteVideoService.saveVideo(tevglSiteVideo);
		log.debug("选择的课堂：" + tevglSiteVideo.getCtIdList());
		if (tevglSiteVideo.getCtIdList() != null && tevglSiteVideo.getCtIdList().size() > 0) {
			List<TevglSiteVideoRelation> list = new ArrayList<>();
			tevglSiteVideo.getCtIdList().stream().distinct().forEach(ctId -> {
				TevglSiteVideoRelation t = new TevglSiteVideoRelation();
				t.setVrId(Identities.uuid());
				t.setCtId(ctId);
				t.setVideoId(tevglSiteVideo.getVideoId());
				list.add(t);
			});
			tevglSiteVideoRelationService.insertBatch(list);
		}
		return R.ok("保存成功");
	}

	private R doUpdate(MultipartFile multipartFile, TevglSiteVideo tevglSiteVideo, String loginUserId){
		if (multipartFile != null) {
			R r = uploadUtils.uploads(multipartFile, 26, true);
			if (!r.get("code").equals(0)) {
				return r;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>)r.get(Constant.R_DATA);
			String fileName = (String) data.get("fileName");
			String originalFilename = (String) data.get("originalFilename");
			Long fileSize = (Long)data.get("fileSize");
			String fileSuffix = (String) data.get("fileSuffix");
			String firstCaptureAccessUrl = data.get("firstCaptureAccessUrl").toString();
			String firstCaptureSavePath = data.get("firstCaptureAccessUrl").toString();
			tevglSiteVideo.setTitle(StrUtils.isEmpty(tevglSiteVideo.getTitle()) ? originalFilename : tevglSiteVideo.getTitle());
			tevglSiteVideo.setName(fileName);
			tevglSiteVideo.setFileSuffix(fileSuffix);
			tevglSiteVideo.setFileSize(fileSize);
			tevglSiteVideo.setOriginalFilename(originalFilename);
			tevglSiteVideo.setCheckState("0");
			tevglSiteVideo.setState("Y");
			tevglSiteVideo.setCreateTime(DateUtils.getNowTimeStamp());
			tevglSiteVideo.setCreateUserId(loginUserId);
			tevglSiteVideo.setFirstCaptureAccessUrl(firstCaptureAccessUrl);
			tevglSiteVideo.setFirstCaptureSavePath(firstCaptureSavePath);
			tevglSiteVideo.setType("1"); // 老师只允许上传辅教视频，不允许上传公共视频
			// TODO 删除原来的视频与第一帧图片
			TevglSiteVideo oldVideo = tevglSiteVideoService.selectObjectById(tevglSiteVideo.getVideoId());
			if (oldVideo != null) {
				String videUrl = uploadUtils.getUploadPath() + uploadUtils.getPathByParaNo("26") + "/" + oldVideo.getName();
				String uploadPath = uploadUtils.getUploadPath();
				String val = uploadPath.substring(0, uploadPath.indexOf("/uploads"));
				String oldFirstCaptureSavePath = val + oldVideo.getFirstCaptureSavePath();
				File oldVideoFile = new File(videUrl);
				File oldFirstCaptureFile = new File(oldFirstCaptureSavePath);
				log.debug("删除videUrl：" + videUrl);
				log.debug("oldFirstCaptureSavePath:" + oldFirstCaptureSavePath);
				// 磁盘上删除
				if (oldVideoFile.exists() && oldVideoFile.isFile()) {
					oldVideoFile.delete();
				}
				if (oldFirstCaptureFile.exists() && oldFirstCaptureFile.isFile()) {
					oldFirstCaptureFile.delete();
				}
			}
		}
		tevglSiteVideo.setState("Y");
		tevglSiteVideo.setCheckState("0");
		tevglSiteVideo.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglSiteVideo.setUpdateUserId(loginUserId);
		tevglSiteVideoService.update(tevglSiteVideo);
		// 删除旧关系
		Map<String, Object> map = new HashMap<>();
		map.put("videoId", tevglSiteVideo.getVideoId());
		List<TevglSiteVideoRelation> list = tevglSiteVideoRelationService.selectListByMap(map);
		if (list != null && list.size() > 0) {
			List<String> idList = list.stream().map(a -> a.getVrId()).collect(Collectors.toList());
			tevglSiteVideoRelationService.deleteBatch(idList.stream().toArray(String[]::new));
		}
		// 再建立新的
		if (tevglSiteVideo.getCtIdList() != null && tevglSiteVideo.getCtIdList().size() > 0) {
			List<TevglSiteVideoRelation> insertList = new ArrayList<>();
			tevglSiteVideo.getCtIdList().stream().distinct().forEach(ctId -> {
				TevglSiteVideoRelation t = new TevglSiteVideoRelation();
				t.setVrId(Identities.uuid());
				t.setCtId(ctId);
				t.setVideoId(tevglSiteVideo.getVideoId());
				insertList.add(t);
			});
			tevglSiteVideoRelationService.insertBatch(insertList);
		}
		return R.ok("保存成功");
	}
	
	public static void main(String[] args) {
		String url = "d:/uploads";
		System.out.println(url.indexOf("uploads"));
		String v = url.substring(0, url.indexOf("/uploads"));
		System.out.println(v);
	}
	
	/**
	 * 删除视频
	 * @param request
	 * @param videoId
	 * @return
	 */
	@PostMapping("/deleteVideo")
	@CheckSession
	public R deleteVideo(HttpServletRequest request, String videoId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglSiteVideoService.deleteVideo(videoId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看视频
	 * @param request
	 * @param videoId
	 * @return
	 */
	@PostMapping("/viewVideo")
	@CheckSession
	public R videVideo(HttpServletRequest request, String videoId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglSiteVideoService.viewVideo(videoId);
	}
	
	/**
	 * 获取视频分类
	 * @return
	 */
	@GetMapping("/getVideoTypeList")
	public R getVideoType() {
		List<Map<String,Object>> dictList = dictService.getDictList("video_type");
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	/**
	 * 获取当前用户正在进行中的课堂
	 * @param request
	 * @return
	 */
	@GetMapping("getRoomList")
	@CheckSession
	public R getRoomList(HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("createUserId", traineeInfo.getTraineeId());
		map.put("classroomState", "2");
		map.put("withClassName", "Y");
		List<TevglTchClassroom> tevglTchClassroomList = tevglTchClassroomService.queryNoPage(map);
		List<Map<String, Object>> roomList = tevglTchClassroomList.stream()
			.map(item -> {
				Map<String, Object> info = new HashMap<>();
				info.put("ctId", item.getCtId());
				info.put("name", item.getName());
				return info;
			})
			.collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, roomList);
	}
}
