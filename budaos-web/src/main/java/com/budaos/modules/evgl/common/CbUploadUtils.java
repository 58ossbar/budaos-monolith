package com.budaos.modules.evgl.common;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.utils.LoginUtils;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.budaos.utils.tool.VideoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CbUploadUtils {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private TsysAttachService tsysAttachService;
	@Autowired
	private LoginUtils loginUtils;
	@Value("${com.budaos.cb-upload-paths:default}")
	private String cbUploadPaths;
	@Value("${com.budaos.file-upload-path}")
	private String uploadPath;
	@Value("${com.budaos.file-access-path}")
	private String accessPath;

	// 匹配后缀音频格式
	public final List<String> audioSuffixList = Arrays.asList(".MP3", ".WAV");
	// 匹配后缀图片格式
	//public final List<String> imageSuffixList = Arrays.asList(".JPEG", ".PNG", ".JPG", ".GIF", ".BMP");
	public final List<String> imageSuffixList = Arrays.asList(".JPEG", ".PNG", ".JPG", ".GIF", ".BMP", "TIF", "PCX", "TGA", "EXIF", "FPX", "SVG", "SVG+XML", "PSD", "CDR", "PCD", "DXF", "UFO", "EPS", "AI", "RAW", "WMF", "WEBP");
	// 匹配后缀视频格式
	public final List<String> videoSuffixList = Arrays.asList(".MP4", ".AVI", ".MOV", ".RMVB", ".RM", ".FLV", ".3GP");
	
	/**
	 * 上传文件
	 * @param file 必传参数，媒体文件
	 * @return 返回的key有fileName、attachId
	 * 
	 */
	public R uploads(MultipartFile file) {
		return doUpload(file, "0", null, null, false);
	}
	
	/**
	 * 上传文件
	 * @param file 必传参数，媒体文件
	 * @param fileType 必传参数，值可为1、2、3、4..，根据getPathByParaNo()方法获取文件夹名
	 * @return 返回的key有fileName、attachId
	 * 
	 */
	public R uploads(MultipartFile file, int fileType) {
		return doUpload(file, String.valueOf(fileType), null, null, false);
	}
	
	/**
	 * 上传文件
	 * @param file 必传参数，媒体文件
	 * @param fileType 必传参数，值可为1、2、3、4..，根据getPathByParaNo()方法获取文件夹名
	 * @param generateTheFirstFrame 当媒体文件为视频时，是否视频生成第一帧图片
	 * @return
	 */
	public R uploads(MultipartFile file, int fileType, boolean generateTheFirstFrame) {
		return doUpload(file, String.valueOf(fileType), null, null, generateTheFirstFrame);
	}
	
	/**
	 * 上传文件
	 * @param file 必传参数，媒体文件
	 * @param fileType 必传参数，值可为1、2、3、4..，根据getPathByParaNo()方法获取文件夹名
	 * @param limitType 可选参数，限制上传类型，值为audio限制为上传只能音频文件，值为image限制为只能上传图片文件，值为video限制为只能上传视频文件
	 * @return 返回的key有fileName、attachId
	 * 
	 */
	public R uploads(MultipartFile file, int fileType, String limitType) {
		return doUpload(file, String.valueOf(fileType), null, limitType, false);
	}
	
	/**
	 * 上传文件
	 * @param file 必传参数，媒体文件
	 * @param folderName 必传参数，指定上传至哪个目录下
	 * @param limitType 可选参数，限制上传类型，值为audio限制为上传音频文件，值为image限制为上传图片文件
	 * @return
	 */
	public R uploads(MultipartFile file, String folderName, String limitType) {
		return doUpload(file, null, folderName, limitType, false);
	}
	
	/**
	 * 实际上传的方法
	 * @param multipartFile 必传参数，媒体文件
	 * @param fileType 特殊参数，与folderName不可同时为空，值可为1、2、3、4..，根据getPathByParaNo()方法获取文件夹名
	 * @param folderName 特殊参数，与fileType不可同时为空，指定上传至哪个目录下
	 * @param limitType 可选参数，限制上传类型，值为audio限制为上传只能音频文件，值为image限制为只能上传图片文件，值为video限制为只能上传视频文件
	 * @param generateTheFirstFrame 当媒体文件为视频时，是否视频生成第一帧图片
	 * @return
	 */
	private R doUpload(MultipartFile multipartFile, String fileType, String folderName, String limitType, boolean generateTheFirstFrame) {
		try {
			if (multipartFile == null || multipartFile.isEmpty()) {
				return R.error("请上传文件！");
			}
			// 原文件类型
			String oldName = multipartFile.getContentType();
			// 源文件名
			String originalFilename = multipartFile.getOriginalFilename();
			// 扩展名
			String extension = "." + oldName.substring(oldName.lastIndexOf("/")+1); 
			if (StrUtils.isNotEmpty(limitType)) {
				if ("audio".equals(limitType)) {
					if (!audioSuffixList.contains(extension.toUpperCase())) {
						return R.error("格式只能为：" + audioSuffixList);
					}
				} else if ("images".equals(limitType)) {
					if (!imageSuffixList.contains(extension.toUpperCase())) {
						return R.error("格式只能为：" + imageSuffixList);
					}
				} else if ("video".equals(limitType)) {
					if (!videoSuffixList.contains(extension.toUpperCase())) {
						return R.error("格式只能为：" + videoSuffixList);
					}
				}
			}
			// 新文件名默认为uuid组成
			String uuid = Identities.uuid();
			String newFileName =  uuid + extension;
			if (StrUtils.isEmpty(fileType)) {
				fileType = "0";
			}
			String fileDir = getPathByParaNo(fileType);
			// 如果传了folderName
			if (StrUtils.isNotEmpty(folderName)) {
				fileDir = folderName;
			}
			// 判断目录是否存在 不存在则创建指定的目录
			File f = new File(uploadPath + fileDir);
			if (!f.exists()) {
				f.mkdirs();
			}
			// 保存图片地址
			String longName = uploadPath + fileDir + "/" + newFileName;
			// 生成文件
			File file = new File(longName);
			multipartFile.transferTo(file);
			// 上传成功之后保存在附件中添加一条数据
			String atachId = tsysAttachService.uploadFileInsertAttach(oldName, extension,
					String.valueOf(multipartFile.getSize()), newFileName, longName, fileType,
					loginUtils.getLoginUserId());
			// 视频第1帧图片
			String firstCaptureAccessUrl = null;
			String firstCaptureSavePath = null;
			if (generateTheFirstFrame && StrUtils.isNotEmpty(extension) && videoSuffixList.contains(extension.toUpperCase())) {
				String firstCapture = VideoUtils.getVideoImageByFrame(file, uploadPath + fileDir + "/", uuid + ".jpg");
				firstCaptureAccessUrl = accessPath + fileDir + "/" + firstCapture;
				firstCaptureSavePath = uploadPath + fileDir + "/" + firstCapture;
				log.info("第一帧访问路径：" + firstCaptureSavePath);
				log.info("第一帧实际路径：" + firstCaptureSavePath);
			}
			// 返回给前端参数
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("imgNamePc", newFileName);
			map.put("fileName", newFileName); // 新文件名
			map.put("attachId", atachId); // 附件id
			map.put("originalFilename", originalFilename); // 源文件名
			map.put("contentType", oldName); // 文件类型
			map.put("fileSuffix", extension); // 后缀名
			map.put("fileSize", multipartFile.getSize()); // 文件大小
			map.put("firstCaptureAccessUrl", firstCaptureAccessUrl); // 视频第1帧图片访问路径
			map.put("firstCaptureSavePath", firstCaptureSavePath); // 视频第1帧图片实际路径
			log.info("文件被上传至:" + longName);
			log.info("返回结果：" + JSONObject.toJSONString(map));
			return R.ok().put("data", map);
		} catch (Exception e) {
			log.error("文件上传失败", e);
			e.printStackTrace();
			return R.error("文件上传失败！" + e);
		} 	
	}
	
	/**
	 * <i>上传文件</i>
	 * @param file 文件
	 * @param fileType 必传参数，值可为1、2、3、4..，根据getPathByParaNo()方法获取文件夹名
	 * @param folderName 可选参数，（优先级高于fileType），文件夹名，文件将被上传至该文件夹内
	 * @param uploadFileType 可选参数，上传类型，值为audio限制为上传音频文件，值为image限制为上传图片文件
	 * @return 返回的key有fileName、attachId
	 * 
	 */
	public R uploads(MultipartFile file, String fileType, String folderName, String uploadFileType) {
		if (file.isEmpty()) {
			return R.error("请上传文件！");
		}
		try {
			// 原文件类型
			String oldName = file.getContentType();
			// 源文件名
			String originalFilename = file.getOriginalFilename();
			// 扩展名
			String extension = "." + oldName.substring(oldName.lastIndexOf("/")+1); 
			if (StrUtils.isNotEmpty(uploadFileType)) {
				if ("audio".equals(uploadFileType)) {
					if (!audioSuffixList.contains(extension.toUpperCase())) {
						return R.error("格式只能为：" + audioSuffixList);
					}
				} else if ("images".equals(uploadFileType)) {
					if (!imageSuffixList.contains(extension.toUpperCase())) {
						return R.error("格式只能为：" + imageSuffixList);
					}
				}
			}
			String newFileName =  Identities.uuid() + extension; // 新文件名默认为uuid
			if (StrUtils.isEmpty(fileType)) {
				fileType = "0";
			}
			String fileDir = getPathByParaNo(fileType);
			// 如果传了folderName
			if (StrUtils.isNotEmpty(folderName)) {
				fileDir = folderName;
			}
			// 判断目录是否存在 不存在则创建指定的目录
			File f = new File(uploadPath + fileDir);
			if (!f.exists()) {
				f.mkdir();
			}
			// 保存图片地址
			String longName = uploadPath + fileDir + "/" + newFileName;
			log.debug("文件路径:" + longName);
			// 生成文件
			file.transferTo(new File(longName));
			// 上传成功之后保存在附件中添加一条数据
			String atachId = tsysAttachService.uploadFileInsertAttach(oldName, extension,
					String.valueOf(file.getSize()), newFileName, longName, fileType,
					loginUtils.getLoginUserId());
			// 返回给前端参数
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fileName", newFileName);
			map.put("imgNamePc", newFileName);
			map.put("attachId", atachId);
			map.put("originalFilename", originalFilename);
			return R.ok().put("data", map);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("文件上传失败！"+e);
		} 		
	}	
	
	/**
	 * 根据附件参数类型获取对应的附件存放路径
	 * @param paraNo
	 * @return
	 */
	public String getPathByParaNo(String paraNo) {
		log.debug("全部路径：" + cbUploadPaths);
		String[] paths = cbUploadPaths.split(",");
		if(StrUtils.isEmpty(paraNo) || Integer.parseInt(paraNo) >= paths.length) {
			return paths[0];
		}
		return paths[Integer.parseInt(paraNo)].trim();
	}
	
	/**
	 * 获取文件上传路径前缀
	 * @return
	 * @apiNote
	 * 返回结果示例：<br>
	 * windows： D:/uploads<br>
	 * Linux: /mnt/cbstp/uploads
	 */
	public String getUploadPath() {
		return uploadPath;
	}
}
