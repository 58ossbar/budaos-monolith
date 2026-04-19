package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 创蓝图片上传组件文件上传处理类
 * @author zhuq
 *
 */
@RestController
@RequestMapping("/api/cbupload")
public class UploaderController {

	@Autowired
	private UploadFileUtils uploadFileUtils;
	/**
	 * <p>上传图片</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param picture
	 * @return
	 */
	@PostMapping("/uploadPic")
	public R uploadPic(@RequestPart(value = "file", required = false) MultipartFile picture, @RequestParam("type") String type) {
		if(StrUtils.isEmpty(type)) {
			type = "0";
		}
		return uploadFileUtils.uploadImage(picture, null, type, 0, 0);
	}
	
	/**
	 * <p>上传音频</p>
	 * @param file
	 * @param type
	 * @return
	 */
	@PostMapping("/uploadAudioFile")
	public R uploadAudioFileTest(@RequestPart MultipartFile file, HttpServletRequest request) {
		String type = request.getParameter("type");
		if(StrUtils.isEmpty(type)) {
			type = "0";
		}
		return uploadFileUtils.uploadAudioFile(file, null, type, null);
	}
}
