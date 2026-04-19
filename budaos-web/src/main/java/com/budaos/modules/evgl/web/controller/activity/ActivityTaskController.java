package com.budaos.modules.evgl.web.controller.activity;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.common.enums.BizCodeEnume;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.activity.api.*;
import com.budaos.modules.evgl.activity.domain.TevglActivityTask;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskFile;
import com.budaos.modules.evgl.activity.domain.TevglActivityTaskTraineeAnswerFile;
import com.budaos.modules.evgl.activity.query.ActTaskGroupQuery;
import com.budaos.modules.evgl.activity.vo.ActivityTaskScoreVo;
import com.budaos.modules.evgl.activity.vo.CommitTaskAnswerVo;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
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
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 5作业/小组任务
 * @author huj
 *
 */
@RestController
@RequestMapping("/activity/task")
public class ActivityTaskController {

	private final List<String> imageSuffixList = Arrays.asList(".JPEG", ".PNG", ".JPG", ".GIF", ".BMP");
	private final List<String> videoSuffixList = Arrays.asList(".MP4", ".FLV", ".RMVB", ".AVI", ".WMV", ".MOV", ".QUICKTIME");
	private final List<String> audioSuffixList = Arrays.asList(".MP3", ".WAV", ".WMA", ".OGG", ".FLAC", ".AAC", ".M4A");
	private final List<String> blackSuffixList = Arrays.asList(".EXE", ".SH", ".BAT");
	private final List<String> docsSuffixList = Arrays.asList(".PDF", ".DOCX", ".DOC", ".TXT", ".PPTX", ".PPT", ".XLX", ".XLSX", ".CHM");
	
	// 文件上传路径
	@Value("${com.budaos.file-upload-path}")
    private String uploadPath;
	// 文件显示路径
	@Value("${com.budaos.file-access-path}")
	private String accessPath;
	
	@Autowired
	private UploadFileUtils uploadFileUtils;
	
	@Autowired
	private TevglActivityTaskService tevglActivityTaskService;
	@Autowired
	private TevglActivityTaskFileService tevglActivityTaskFileService;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglActivityTaskScoreService tevglActivityTaskScoreService;
	@Autowired
    private TevglActivityTaskGroupMemberService tevglActivityTaskGroupMemberService;
	@Autowired
	private TevglActivityTaskTraineeAnswerFileService tevglActivityTaskTraineeAnswerFileService;
	
	/**
	 * 任务小组划分方式
	 * 1不划分小组、2随机划分小组、3线下划分小组、4使用成员小组方案
	 * 学生将以个人为单位完成作业/小组任务并提交结果
	 * @return
	 */
	@GetMapping("/getDivideGroupType")
	public R getDivideGroupType() {
		List<Map<String,Object>> dictList = dictService.getDictList("divideGroupType");
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	/**
	 * 评分方式、作业分值及评分点（可以任意选择一项或多项组合使用）
	 * 1老师评分、2指定助教/学生评分、3学生互评、4老师评分，组间互评，组内互评
	 * @return
	 */
	@GetMapping("/getScoreType")
	public R getScoreType() {
		List<Map<String,Object>> dictList = dictService.getDictList("scoreType");
		return R.ok().put(Constant.R_DATA, dictList);
	}
	
	/**
	 * 文件上传
	 * @param request
	 * @param fromType 必传参数，来源类型(1任务详情处提交的附件2参考答案处提交的附件)
	 * @param files 必传参数
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload")
	@CheckSession
	public R upload(HttpServletRequest request, String fromType,
			@RequestPart(value = "file", name = "file") MultipartFile[] files) throws Exception {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        if (StrUtils.isEmpty(fromType)) {
            return R.error(BizCodeEnume.PARAM_INVALID.getCode(), BizCodeEnume.PARAM_INVALID.getMsg());
        }
        if (files == null || files.length == 0) {
            return R.error("请上传文件");
        }
        if (files.length > 5) {
            return R.error("只能上传5个");
        }
        List<String> ids = new ArrayList<>();
        try {
        	// 等待新增的数据
            List<TevglActivityTaskTraineeAnswerFile> insertTraineeAnswerFileList = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                MultipartFile multipartFile = files[i];
                // 源文件名
                String originalFilename = multipartFile.getOriginalFilename();
                if (StrUtils.isEmpty(originalFilename)) {
                    continue;
                }
                int pos = originalFilename.lastIndexOf(".");
                if (pos < 0) {
                    continue;
                }
                // 后缀名
                String extension = originalFilename.substring(pos);
                if(blackSuffixList.contains(extension.toUpperCase())) {
                    return R.error("第" + (i+1) + "个文件格式不允许上传！");
                }
                // 文件类型
                String fileType = "file";
                if (imageSuffixList.contains(extension.toUpperCase())) {
                    fileType = "image";
                } else if(videoSuffixList.contains(extension.toUpperCase())) {
                    fileType = "video";
                } else if(audioSuffixList.contains(extension.toUpperCase())) {
                    fileType = "audio";
                } else if (docsSuffixList.contains(extension.toUpperCase())) {
                    fileType = "docs";
                } else {
                    fileType = "other";
                }
                //10M(10 * 1024 * 1024)指文件的字节大小
                if (multipartFile.getSize() > 10 * 1024 * 1024L) {
                    return R.error("每次上传的文件大小不能超过10M");
                }else if (multipartFile.getSize() > 300 * 1024 * 1024) {
                    return R.error("总共上传的文件大小不能超过300M");
                }
                // 磁盘上生成文件
                File path = new File(uploadPath + uploadFileUtils.getPathByParaNo("22"));
                if(!path.exists()) {
                    path.mkdirs();
                }
                String uuid = UUID.randomUUID().toString();
                String newName = uuid + extension;
                String savePath = uploadPath + uploadFileUtils.getPathByParaNo("22") + "/" + newName;
                File file = new File(savePath);
                multipartFile.transferTo(file);
                if (Arrays.asList("1", "2").contains(fromType)) {
                    TevglActivityTaskFile attach = new TevglActivityTaskFile();
                    if ("video".equals(fileType) || "voice".equals(fileType)) {
                        //attach.setDurationTime(VideoUtils.getDuration(file));
                    }
                    if ("video".equals(fileType)) {
                        String firstCapture = VideoUtils.getVideoImageByFrame(file, uploadPath + uploadFileUtils.getPathByParaNo("22"), fileType + "/" + uuid + ".jpg");
                        attach.setFirstCaptureAccessUrl(accessPath + uploadFileUtils.getPathByParaNo("22") + "/" + firstCapture);
                        attach.setFirstCaptureSavePath(uploadPath + uploadFileUtils.getPathByParaNo("22") + "/" + firstCapture);
                    }
                    attach.setFileId(Identities.uuid());
                    attach.setFromType(fromType);
                    attach.setFileName(newName);
                    attach.setFileType(fileType);
                    attach.setFileSuffix(extension);
                    attach.setFileSize(multipartFile.getSize());
                    attach.setOriginalFilename(originalFilename);
                    attach.setFileAccessUrl(accessPath + uploadFileUtils.getPathByParaNo("22") + "/" + newName);
                    attach.setFileSavePath(savePath);
                    attach.setState("Y");
                    attach.setCreateTime(DateUtils.getNowTimeStamp());
                    attach.setUpdateTime(DateUtils.getNowTimeStamp());
                    attach.setCreateUserId(traineeInfo.getTraineeId());
                    attach.setUpdateUserId(traineeInfo.getTraineeId());
                    tevglActivityTaskFileService.save(attach);
                    ids.add(attach.getFileId());
                } else {
                    TevglActivityTaskTraineeAnswerFile t = new TevglActivityTaskTraineeAnswerFile();
                    t.setFileId(Identities.uuid());
                    t.setFileName(newName);
                    t.setFileType(fileType);
                    t.setFileSuffix(extension);
                    t.setFileSize(multipartFile.getSize());
                    t.setOriginalFilename(originalFilename);
                    t.setFileAccessUrl(accessPath + uploadFileUtils.getPathByParaNo("22") + "/" + newName);
                    t.setFileSavePath(savePath);
                    t.setState("Y");
                    t.setCreateTime(DateUtils.getNowTimeStamp());
                    t.setUpdateTime(DateUtils.getNowTimeStamp());
                    t.setCreateUserId(traineeInfo.getTraineeId());
                    t.setUpdateUserId(traineeInfo.getTraineeId());
                    insertTraineeAnswerFileList.add(t);
                    ids.add(t.getFileId());
                }
            }
            if (insertTraineeAnswerFileList.size() > 0) {
                tevglActivityTaskTraineeAnswerFileService.insertBatch(insertTraineeAnswerFileList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("系统错误");
        }
        return R.ok().put("media_id", ids.stream().collect(Collectors.joining(",")));
	}
	
	/**
	 * 保存
	 * @param request
	 * @param tevglActivityTask
	 * @return
	 * @throws IOException 
	 */
	@PostMapping("/saveTaskInfo")
	@CheckSession
	public R saveTaskInfo(HttpServletRequest request, @RequestBody TevglActivityTask tevglActivityTask) throws IOException {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		// 新增
		if (StrUtils.isEmpty(tevglActivityTask.getActivityId())) {
			return tevglActivityTaskService.saveTaskInfo(tevglActivityTask, traineeInfo.getTraineeId());
		} else {
			return tevglActivityTaskService.updateTaskInfo(tevglActivityTask, traineeInfo.getTraineeId());
		}
	}
	
	/**
	 * 查看
	 * @param request
	 * @param activityId
	 * @return
	 */
	@PostMapping("view")
	@CheckSession
	public R view(HttpServletRequest request, String activityId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityTaskService.viewActivityTaskInfo(activityId);
	}
	
	/**
	 * 复制[作业/小组任务]活动
	 * @author zyl加
	 * @data 2020年11月27日
	 * @param request
	 * @param targetActivityId 活动id
	 * @param newPkgId 新教学包id
	 * @param loginUserId 登录用户id
	 * @param chapterId 章节id
	 * @param sortNum 排序号
	 * @return
	 */
	@PostMapping("copy")
	@CheckSession
	public R copy(HttpServletRequest request, String targetActivityId, String newPkgId, String loginUserId, String chapterId, int sortNum) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglActivityTaskService.copy(targetActivityId, newPkgId, loginUserId, chapterId, sortNum);
	}
	
    /**
     * 查看作业作答情况
     * @param request
     * @param vo
     * @return
     */
    @PostMapping("viewTraineeAnswerContent")
    @CheckSession
    public R viewTraineeAnswerContent(HttpServletRequest request, @RequestBody ActivityTaskScoreVo vo) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglActivityTaskGroupMemberService.viewTraineeAnswerContent(vo.getCtId(), vo.getActivityId(), traineeInfo.getTraineeId());
    }

	
    /**
     * 提交作业
     * @param request
     * @param vo
     * @param files
     * @return
     */
    @PostMapping("commitTask")
    @CheckSession
    //public R commitTask(HttpServletRequest request, CommitTaskAnswerVo vo, @RequestPart(name = "answerFile") MultipartFile[] files) {
    public R commitTask(HttpServletRequest request, @RequestBody CommitTaskAnswerVo vo) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        vo.setTraineeId(traineeInfo.getTraineeId());
        return tevglActivityTaskGroupMemberService.commitTast(vo);
    }
	

    /**
     * 查看作业作答情况
     * @param request
     * @param query
     * @return
     */
    @PostMapping("queryTraineeAnswerList")
    @CheckSession
    public R queryTraineeAnswerList(HttpServletRequest request, @RequestBody ActTaskGroupQuery query) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglActivityTaskGroupMemberService.queryTraineeAnswerList(query, traineeInfo.getTraineeId());
    }

    
    /**
     * 老师评分
     * @param request
     * @param vo
     * @return
     */
    @PostMapping("teachGiveScore")
    @CheckSession
    public R teachGiveScore(HttpServletRequest request, @RequestBody ActivityTaskScoreVo vo) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglActivityTaskScoreService.teachGiveScore(vo, traineeInfo.getTraineeId());
    }
	
}
