package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 富文本编辑器文件上传处理类
 * @author zhuq
 *
 */
@RestController
@RequestMapping("cbeditor")
public class EditorUploaderController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());
	@Value("${com.budaos.file-upload-path}")
	private String basePath;
	@Value("${com.budaos.file-access-path:/uploads}")
	private String accessPath;
	@Value("${com.budaos.cbeditor-image-path:cbeditor-image}")
	private String imagePath;
	@Value("${com.budaos.cbeditor-image-suffix:}")
	private String imageSuffix;
	@Value("${com.budaos.cbeditor-media-path:cbeditor-media}")
	private String mediaPath;
	@Value("${com.budaos.cbeditor-media-suffix:}")
	private String mediaSuffix;
	@Value("${com.budaos.cbeditor-file-path:cbeditor-file}")
	private String filePath;
	@Value("${com.budaos.cbeditor-file-suffix:}")
	private String fileSuffix;
	
	private static String FILE_ID_KEY = "fileId";

	/**
	 * 图片上传
	 * @param file
	 * @return
	 */
	@PostMapping("uploadImg")
	public R uploadImage(MultipartFile file) {
		log.debug("接受到图片:" + DateUtils.getNowDateTimeToLong());
		if(file != null) {
			String url = "";
			String fileId = "";
			try {
				//校验文件格式是否非法
				String fileName = checkFileSuffix(file.getOriginalFilename(), imageSuffix);
				if(fileName == null) {
					return R.error("图片格式非法");
				}
				log.debug("图片格式非法校验完成:" + DateUtils.getNowDateTimeToLong());
				//初始化磁盘保存路径
				url = initFilePath(imagePath) + File.separator + fileName;
				log.debug("图片保存路径初始化完成:" + DateUtils.getNowDateTimeToLong());
				//保存图片
				file.transferTo(new File(basePath + url));
				log.debug("图片上传完成:" + DateUtils.getNowDateTimeToLong());
				// 获取uuid
				fileId = fileName.substring(0, fileName.lastIndexOf("."));
			} catch (Exception e) {
				e.printStackTrace();
				return R.error("系统异常");
			}
			return R.ok().put("location", accessPath + url).put(FILE_ID_KEY, fileId);
		}
		return R.error("未上传图片");
	}

	/**
	 * 视频上传
	 * @param file
	 * @return
	 */
	@PostMapping("uploadMedia")
	public R uploadMedia(MultipartFile file) {
		log.debug("接受到视频:" + DateUtils.getNowDateTimeToLong());
		if(file != null) {
			String url = "";
			String fileId = "";
			try {
				//校验文件格式是否非法
				String fileName = checkFileSuffix(file.getOriginalFilename(), mediaSuffix);
				if(fileName == null) {
					return R.error("视频格式非法");
				}
				log.debug("视频格式非法校验完成:" + DateUtils.getNowDateTimeToLong());
				//初始化磁盘保存路径
				url = initFilePath(mediaPath) + File.separator + fileName;
				log.debug("视频保存路径初始化完成:" + DateUtils.getNowDateTimeToLong());
				//保存文件
				file.transferTo(new File(basePath + url));
				log.debug("视频上传完成:" + DateUtils.getNowDateTimeToLong());
				// 获取uuid
				fileId = fileName.substring(0, fileName.lastIndexOf("."));
			} catch (Exception e) {
				e.printStackTrace();
				return R.error("系统异常");
			}
			return R.ok().put("location", accessPath + url).put(FILE_ID_KEY, fileId);
		}
		return R.error("未上传视频");
	}
	
	/**
	 * 文件上传
	 * @param file
	 * @return
	 */
	@PostMapping("uploadFile")
	public R uploadFile(MultipartFile file) {
		log.debug("接受到文件:" + DateUtils.getNowDateTimeToLong());
		if(file != null) {
			String url = "";
			String fileId = "";
			try {
				//校验文件格式是否非法
				String fileName = checkFileSuffix(file.getOriginalFilename(), fileSuffix);
				if(fileName == null) {
					return R.error("文件格式非法");
				}
				log.debug("文件格式非法校验完成:" + DateUtils.getNowDateTimeToLong());
				//初始化磁盘保存路径
				url = initFilePath(filePath) + File.separator + fileName;
				log.debug("文件保存路径初始化完成:" + DateUtils.getNowDateTimeToLong());
				//保存文件
				file.transferTo(new File(basePath + url));
				log.debug("文件上传完成:" + DateUtils.getNowDateTimeToLong());
				// 获取uuid
				fileId = fileName.substring(0, fileName.lastIndexOf("."));
			} catch (Exception e) {
				e.printStackTrace();
				return R.error("系统异常");
			}
			return R.ok().put("location", accessPath + url).put(FILE_ID_KEY, fileId);
		}
		return R.error("未上传文件");
	}
	
	/**
	 * 初始化磁盘保存路径
	 * @param path
	 * @return
	 */
	private String initFilePath(String path) {
		String tempPath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String url = File.separator + path + File.separator + tempPath;
		File parentPath = new File(basePath + url);
		if(!parentPath.exists()) {
			parentPath.mkdirs();
		}
		return url;
	}
	
	/**
	 * 校验文件格式是否合法，不合法返回null，合法则返回文件新名称
	 * @param fileName
	 * @param fileSuffix
	 * @return
	 */
	private String checkFileSuffix(String fileName, String fileSuffix) {
		//如果文件名不存在，则直接返回非法
		if(fileName == null || fileName.length() == 0) {
			return null;
		}
		//如果文件名不带.后，则表示无文件类型，直接返回非法
		int pos = fileName.lastIndexOf(".");
		if(pos < 0) {
			return null;
		}
		//如果文件名.后没有内容，则表示无文件类型，直接返回非法
		fileName = fileName.substring(pos);
		if(fileName.length() == 1) {
			return null;
		}
		//遍历白名单，如果匹配上，则直接返回匹配上的文件后缀名
		for(String sfx : fileSuffix.split(",")) {
			if(fileName.equalsIgnoreCase("." + sfx.trim())){
				return Identities.uuid() + fileName;
			}
		}
		//遍历完成没有匹配上则直接返回非法
		return null;
	}
}
