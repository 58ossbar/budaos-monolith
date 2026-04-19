package com.budaos.modules.evgl.web.controller.activity;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.activity.api.TevglActivityBrainstormingAnswerFileService;
import com.budaos.modules.evgl.activity.api.TevglActivityBrainstormingService;
import com.budaos.modules.evgl.activity.api.TevglActivityBrainstormingTraineeAnswerService;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstorming;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstormingAnswerFile;
import com.budaos.modules.evgl.common.CbUploadUtils;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.pkg.api.TevglBookpkgTeamService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.budaos.utils.tool.VideoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 2头脑风暴
 * @author huj
 *
 */
@RestController
@RequestMapping("/activity/brainstorming")
public class ActivityBrainstormingController {

	@Autowired
	private TevglBookpkgTeamService tevglBookpkgTeamService;
	@Autowired
	private CbUploadUtils uploadUtils;
	@Autowired
	private TevglActivityBrainstormingService tevglActivityBrainstormingService;
	@Autowired
	private TevglActivityBrainstormingTraineeAnswerService tevglActivityBrainstormingTraineeAnswerService;
	@Autowired
	private TevglActivityBrainstormingAnswerFileService tevglActivityBrainstormingAnswerFileService;
	@Autowired
	private UploadFileUtils uploadFileUtils;
	
	// 文件上传路径
	@Value("${com.budaos.file-upload-path}")
    private String uploadPath;
	// 文件显示路径
	@Value("${com.budaos.file-access-path}")
	private String accessPath;
	// 一些限制
	private final List<String> imgSuffixList = Arrays.asList(".JPEG", ".PNG", ".JPG", ".GIF", ".BMP");
	private final List<String> videoSuffixList = Arrays.asList(".MP4", ".FLV", ".RMVB", ".AVI", ".WMV", ".MOV", ".QUICKTIME");
	private final List<String> audioSuffixList = Arrays.asList(".MP3", ".WAV", ".WMA", ".OGG", ".FLAC", ".AAC", ".M4A");
	private final List<String> blackSuffixList = Arrays.asList(".EXE", ".SH", ".BAT");
	private final List<String> docsSuffixList = Arrays.asList(".PDF", ".DOCX", ".DOC", ".TXT", ".PPTX", ".PPT", ".XLX", ".XLSX", ".CHM");
	
	
	/**
	 * 附件上传
	 * @param file
	 * @return
	 */
	@PostMapping("/uploads")
	public R uploads(@RequestPart(name = "file", required = false) MultipartFile file) {
		return uploadUtils.uploads(file, "21", null, null);
	}
	
	@PostMapping("/upload")
	@CheckSession
	public R upload(HttpServletRequest request, @RequestPart(value = "file", name = "file") MultipartFile[] files) throws Exception {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		List<String> ids = new ArrayList<>();
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				TevglActivityBrainstormingAnswerFile attach = new TevglActivityBrainstormingAnswerFile();
				MultipartFile multipartFile = files[i];
				String originalFilename = multipartFile.getOriginalFilename();
				if (StrUtils.isEmpty(originalFilename)) {
					continue;
				}
				int pos = originalFilename.lastIndexOf("."); // 源文件名
				if (pos < 0) {
					continue;
				}
				String uuid = UUID.randomUUID().toString();
				String extension = originalFilename.substring(pos); // 拓展名
				if(blackSuffixList.contains(extension.toUpperCase())) {
					return R.error("第" + (i+1) + "个文件格式不允许上传！");
				}
				String fileType = "file"; // 文件类型
				if (imgSuffixList.contains(extension.toUpperCase())) {
					fileType = "image";
					if (multipartFile.getSize() / 1024 / 1024 > 2) {
                        return R.error("图片大小不能超过2MB 请自行调整");
                    }
				} else if(videoSuffixList.contains(extension.toUpperCase())) {
					fileType = "video";
					if (multipartFile.getSize() / 1024 / 1024 > 10) {
                        return R.error("视频大小不能超过10MB 请自行调整");
                    }
				} else if(audioSuffixList.contains(extension.toUpperCase())) {
					fileType = "audio";
					if (multipartFile.getSize() / 1024 / 1024 > 10) {
                        return R.error("音频大小不能超过10MB 请自行调整");
                    }
				} else if (docsSuffixList.contains(extension.toUpperCase())) {
		            fileType = "docs";
		        } else {
					fileType = "other";
				}
				File path = new File(uploadPath + uploadFileUtils.getPathByParaNo("21"));
				if(!path.exists()) {
					path.mkdirs();
				}
				String savePath = uploadPath + uploadFileUtils.getPathByParaNo("21") + "/" + uuid + extension;
				File file = new File(savePath);
				multipartFile.transferTo(file);
				if ("video".equals(fileType) || "voice".equals(fileType)) {
					//attach.setDurationTime(VideoUtils.getDuration(file));
				}
				if ("video".equals(fileType)) {
					String firstCapture = VideoUtils.getVideoImageByFrame(file, uploadPath + uploadFileUtils.getPathByParaNo("21"), fileType + "/" + uuid + ".jpg");
					attach.setFirstCaptureAccessUrl(accessPath + uploadFileUtils.getPathByParaNo("21") + "/" + firstCapture);
					attach.setFirstCaptureSavePath(uploadPath + uploadFileUtils.getPathByParaNo("21") + "/" + firstCapture);
				}
				attach.setFiId(Identities.uuid());
				attach.setUrl(uuid + extension);
				attach.setFileType(fileType);
				attach.setFileSuffix(extension);
				attach.setFileSize(multipartFile.getSize());
				attach.setOriginalFilename(originalFilename);
				attach.setState("Y");
				attach.setSortNum(i);
				attach.setCreateTime(DateUtils.getNowTimeStamp());
				attach.setCreateUserId(traineeInfo.getTraineeId());
				tevglActivityBrainstormingAnswerFileService.save(attach);
				ids.add(attach.getFiId());
			}
		}
		return R.ok().put("media_id", ids.stream().collect(Collectors.joining(",")));
	}
	
	
	/**
	 * 保存头脑风暴
	 * @param request
	 * @param file
	 * @param tevglActivityBrainstorming
	 * @return
	 */
	@PostMapping("/saveActivityBrainstorming")
	@CheckSession
	public R saveActivityBrainstorming(HttpServletRequest request,
			@RequestPart(name="file", required = false) MultipartFile file
			, TevglActivityBrainstorming tevglActivityBrainstorming) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// 图片上传
		if (file != null && !file.isEmpty()) {
			R r = uploadUtils.uploads(file, "21", null, "image");
			if ((Integer)r.get("code") != 0) {
				return r;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> ob = (Map<String, Object>) r.get("data");
			String fileName = (String) ob.get("fileName");
			String attachId = (String) ob.get("attachId");
			// 填充至实体类
			if (StrUtils.isNotEmpty(fileName)) {
				tevglActivityBrainstorming.setActivityPic(fileName);	
			}
			if (StrUtils.isNotEmpty(attachId)) {
				tevglActivityBrainstorming.setAttachId(attachId);
			}
		}
		// 新增
		if (StrUtils.isEmpty(tevglActivityBrainstorming.getActivityId())) {
			return tevglActivityBrainstormingService.saveBrainstormingInfo(tevglActivityBrainstorming, traineeInfo.getTraineeId());
		} else {
			tevglActivityBrainstorming.setUpdateUserId(traineeInfo.getTraineeId());
			return tevglActivityBrainstormingService.updateBrainstormingInfo(tevglActivityBrainstorming, traineeInfo.getTraineeId());
		}
	}
	
	/**
	 * 查看头脑风暴
	 * @param activityId
	 * @return
	 */
	@GetMapping("/viewActivityBrainstormingInfo")
	public R viewActivityBrainstormingInfo(String activityId) {
		return tevglActivityBrainstormingService.viewActivityBrainstormingInfo(activityId);
	}
	
	/**
	 * 学员提交头脑风暴的作答内容
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/saveActivityBrainstormingTraineeAnswerInfo")
	@CheckSession
	public R saveActivityBrainstormingTraineeAnswerInfo(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityBrainstormingTraineeAnswerService.saveTraineeCommitAnswerContent(jsonObject, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查询作答内容（区别教师、学员）
	 * @param request
	 * @param ctId 课堂id
	 * @param activityId 活动id
	 * @param anId 表t_evgl_activity_brainstorming_trainee_answer主键
	 * @return
	 */
	@GetMapping("/viewTraineeAnswerInfo")
	@CheckSession
	public R viewTraineeAnswerInfo(HttpServletRequest request, String ctId, String activityId, String anId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityBrainstormingTraineeAnswerService.viewTraineeAnswerInfo(ctId, activityId, anId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查看所有学员的作答内容
	 * @param request
	 * @param params
	 */
	@GetMapping("viewTraineeAnswerListData")
	@CheckSession
	public R viewTraineeAnswerListData(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityBrainstormingTraineeAnswerService.viewTraineeAnswerListData(params, traineeInfo.getTraineeId());
	}
	
	/**
	 * 点赞与取消点赞
	 * @param anId
	 * @param value Y点赞N取消点赞
	 * @return
	 */
	@PostMapping("/clickLike")
	@CheckSession
	public R clickLike(HttpServletRequest request, String anId, String value) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if ("Y".equals(value)) {
			return tevglActivityBrainstormingTraineeAnswerService.clickLike(anId, traineeInfo.getTraineeId());
		} else {
			return tevglActivityBrainstormingTraineeAnswerService.cancelLike(anId, traineeInfo.getTraineeId());
		}
	}
	
}
