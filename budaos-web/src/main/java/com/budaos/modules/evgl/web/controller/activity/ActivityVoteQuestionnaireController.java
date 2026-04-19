package com.budaos.modules.evgl.web.controller.activity;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.activity.api.TevglActivityVoteQuestionnaireService;
import com.budaos.modules.evgl.activity.api.TevglActivityVoteQuestionnaireTraineeAnswerFileService;
import com.budaos.modules.evgl.activity.api.TevglActivityVoteQuestionnaireTraineeAnswerService;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireTraineeAnswerFile;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
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
 * 1投票/问卷
 * @author huj
 *
 */
@RestController
@RequestMapping("/activity/voteQuestionnaire")
public class ActivityVoteQuestionnaireController {

	@Autowired
	private TevglActivityVoteQuestionnaireService tevglActivityVoteQuestionnaireService;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerService tevglActivityVoteQuestionnaireTraineeAnswerService;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerFileService tevglActivityVoteQuestionnaireTraineeAnswerFileService;
	/*@Autowired
	private EvglUploadUtils uploadUtils;*/
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
	
	/**
	 * 上传
	 * @param file
	 * @return
	 */
	/*@PostMapping("/uploads")
	public R uploads(@RequestPart(value = "file", required = false) MultipartFile file) {
		return uploadUtils.uploads(file, "20", null, null);
	}*/
	
	/**
	 * 文件上传
	 * @param request
	 * @param files
	 * @return
	 * @throws Exception
	 */
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
				TevglActivityVoteQuestionnaireTraineeAnswerFile attach = new TevglActivityVoteQuestionnaireTraineeAnswerFile();
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
				} else {
					fileType = "other";
				}
				File path = new File(uploadPath + uploadFileUtils.getPathByParaNo("20"));
				if(!path.exists()) {
					path.mkdirs();
				}
				String savePath = uploadPath + uploadFileUtils.getPathByParaNo("20") + "/" + uuid + extension;
				File file = new File(savePath);
				multipartFile.transferTo(file);
				if ("video".equals(fileType) || "voice".equals(fileType)) {
					//attach.setDurationTime(VideoUtils.getDuration(file));
				}
				if ("video".equals(fileType)) {
					String firstCapture = VideoUtils.getVideoImageByFrame(file, uploadPath + uploadFileUtils.getPathByParaNo("21"), fileType + "/" + uuid + ".jpg");
					attach.setFirstCaptureAccessUrl(accessPath + uploadFileUtils.getPathByParaNo("20") + "/" + firstCapture);
					attach.setFirstCaptureSavePath(uploadPath + uploadFileUtils.getPathByParaNo("20") + "/" + firstCapture);
				}
				attach.setFileId(Identities.uuid());
				attach.setUrl(uuid + extension);
				attach.setFileType(fileType);
				attach.setFileSuffix(extension);
				attach.setFileSize(multipartFile.getSize());
				attach.setOriginalFilename(originalFilename);
				attach.setState("Y");
				attach.setSortNum(i);
				attach.setTraineeId(traineeInfo.getTraineeId());
				attach.setCreateTime(DateUtils.getNowTimeStamp());
				attach.setCreateUserId(traineeInfo.getTraineeId());
				tevglActivityVoteQuestionnaireTraineeAnswerFileService.save(attach);
				ids.add(attach.getFileId());
			}
		}
		return R.ok().put("media_id", ids.stream().collect(Collectors.joining(",")));
	}
	
	/**
	 * 保存投票问卷
	 * @param request
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/saveVoteQuestionnaireInfo")
	@CheckSession
	public R saveVoteQuestionnaireInfo(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		String activityId = jsonObject.getString("activityId");
		if (StrUtils.isEmpty(activityId)) {
			return tevglActivityVoteQuestionnaireService.saveVoteQuestionnaireInfo(jsonObject, traineeInfo.getTraineeId()); 
		} else {
			return tevglActivityVoteQuestionnaireService.updateVoteQuestionnaireInfoNew(jsonObject, traineeInfo.getTraineeId());
		}
	}
	
	/**
	 * 查看投票问卷活动信息
	 * @param activityId
	 * @return
	 */
	@GetMapping("/viewVoteQuestionnaireInfo")
	public R viewVoteQuestionnaireInfo(String activityId) {
		return tevglActivityVoteQuestionnaireService.viewVoteQuestionnaireInfo(activityId);
	}
	
	/**
	 * 查看学员针对投票/问卷的作答内容
	 * @param request
	 * @param params
	 * @return
	 */
	@GetMapping("viewTraineeAnswerListData")
	@CheckSession
	public R viewTraineeAnswerListData(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityVoteQuestionnaireService.viewTraineeAnswerListData(params, traineeInfo.getTraineeId());
	}
	
	/**
	 * 保存学员提交投票/问卷的作答内容
	 * @param jsonObject
	 * @return
	 */
	@PostMapping("/saveTraineeCommitAnswerContent")
	@CheckSession
	public R saveTraineeCommitAnswerContent(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityVoteQuestionnaireTraineeAnswerService.saveTraineeCommitAnswerContent(jsonObject, traineeInfo.getTraineeId());
	}
}
