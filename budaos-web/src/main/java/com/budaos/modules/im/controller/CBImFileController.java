package com.budaos.modules.im.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.im.core.entity.MsgType;
import com.budaos.modules.im.domain.TimAttach;
import com.budaos.modules.im.domain.TimUsertoken;
import com.budaos.modules.im.service.TimAttachService;
import com.budaos.modules.im.service.TimUsertokenService;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.budaos.utils.tool.VideoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cbim/file")
public class CBImFileController {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Value("${com.budaos.file-upload-path}")
	private String uploadPath;
	@Value("${com.budaos.file-access-path}")
	private String accessPath;
	@Autowired
	private TimAttachService timAttachService;
	@Autowired
	private TimUsertokenService timUsertokenService;

	private final List<String> imgSuffixList = Arrays.asList(".JPEG", ".PNG", ".JPG", ".GIF", ".BMP");
	private final List<String> videoSuffixList = Arrays.asList(".MP4", ".FLV", ".RMVB", ".AVI", ".WMV", ".MOV", ".QUICKTIME");
	private final List<String> audeoSuffixList = Arrays.asList(".MP3", ".WAV", ".WMA", ".OGG", ".FLAC", ".AAC", ".M4A");
	private final List<String> blackSuffixList = Arrays.asList(".EXE", ".SH", ".BAT");
	
	/**
	 * 文件上传处理类
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("upload")
	public R uploadFile(HttpServletRequest request, String[] fileName) throws Exception{
		String token = request.getHeader("im-token");
		if(StrUtils.isEmpty(token)) {
			return R.error(403, "无效的token");
		}
		TimUsertoken info = timUsertokenService.selectByToken(token);
		if(info == null) {
			return R.error(403, "无效的token");
		}
		List<String> fileIds = new ArrayList<>();
		//判断是否有图片
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
	    commonsMultipartResolver.setDefaultEncoding("utf-8");
		if(commonsMultipartResolver.isMultipart(request)) {
			//获取多文件
			MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request;
	        Map<String, MultipartFile> map = mulReq.getFileMap();
	        List<MultipartFile> files = new ArrayList<>();
	        for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {
	        	files.add(entry.getValue());
	        }
			if(files != null && files.size() > 0) {
				for(int i=0; i<files.size(); i++) {
					TimAttach attach = new TimAttach();
					String originName = (fileName != null && fileName.length > i) ? fileName[i] : files.get(i).getOriginalFilename();
					if(StrUtils.isEmpty(originName)) {
						continue;
					}
					int pos = originName.lastIndexOf(".");
					if(pos < 0) {
						continue;
					}
					String uuid = UUID.randomUUID().toString();
					String ext = originName.substring(pos);
					if(blackSuffixList.contains(ext.toUpperCase())) {
						return R.error("第" + (i+1) + "个文件格式不允许上传！");
					}
					String fileType = MsgType.FILE;
					if(imgSuffixList.contains(ext.toUpperCase())) {
						fileType = MsgType.IMAGE;
					}else if(videoSuffixList.contains(ext.toUpperCase())) {
						fileType = MsgType.VIDEO;
					}else if(audeoSuffixList.contains(ext.toUpperCase())) {
						fileType = MsgType.VOICE;
					}
					File path = new File(uploadPath + "/" + fileType);
					if(!path.exists()) {
						path.mkdirs();
					}
					String savePath = fileType + "/" + uuid + ext;
					File file = new File(uploadPath + "/" + savePath);
					files.get(i).transferTo(file);
					if(MsgType.VIDEO.equals(fileType) || MsgType.VOICE.equals(fileType)) {
						attach.setDurationTime(VideoUtils.getDuration(file));
					}
					if(MsgType.VIDEO.equals(fileType)) {
						String firstCapture = VideoUtils.getVideoImageByFrame(file, uploadPath, fileType + "/" + uuid + ".jpg");
						attach.setFirstCaptureAccessUrl(accessPath + "/" + firstCapture);
						attach.setFirstCaptureSavePath(uploadPath + "/" + firstCapture);
					}
					attach.setAttachId(Identities.uuid());
					attach.setAccessUrl(accessPath + "/" + savePath);
					attach.setPath(uploadPath + "/" + savePath);
					attach.setAttachType(fileType);
					attach.setFileName(originName);
					attach.setFileSuffix(ext);
					attach.setFileSize(files.get(i).getSize());
					attach.setCreateUserId(info.getUserId());
					attach.setCreateTime(DateUtils.getNowTimeStamp());
					timAttachService.save(attach);
					fileIds.add(attach.getAttachId());
				}
			}
		}
		return R.ok().put("media_id", fileIds.stream().collect(Collectors.joining(",")));
	}
	
	/**
	 * 下载文件
	 * @param response
	 * @param fileId
	 */
	@RequestMapping("/downloadFile")
	public void downloadFile(HttpServletResponse response, String fileId) {
		if (StrUtils.isEmpty(fileId)) {
			return;
		}
		TimAttach timAttach = timAttachService.selectObjectById(fileId);
		if (timAttach == null) {
			return;
		}
		String absolutePath = timAttach.getPath();
		// 实现文件下载
        byte[] buffer = new byte[1024];
        File f = new File(absolutePath);
        try(
        	FileInputStream fis = new FileInputStream(f);
        	BufferedInputStream bis = new BufferedInputStream(fis);
        	OutputStream os = response.getOutputStream();
        ){
        	response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(timAttach.getFileName(), "UTF-8"));
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
	}
}
