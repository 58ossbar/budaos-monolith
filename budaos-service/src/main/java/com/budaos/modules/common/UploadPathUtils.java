package com.budaos.modules.common;

import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RefreshScope
public class UploadPathUtils {
	
	@Value("${com.budaos.file-upload-path:}")
	private String uploadPath;
	// 文件上传地址
	@Value("${com.budaos.cb-upload-paths:default}")
	private String cbUploadPaths;
	// 访问地址
	@Value("${com.budaos.file-access-path}")
	private String budaosFieAccessPath;
	// 匹配后缀视频格式
	private final List<String> videoSuffixList = Arrays.asList(".MP4", ".AVI", ".MOV", ".RMVB", ".RM", ".FLV", "3GP");
	// 匹配后缀图片格式
	private final List<String> imageSuffixList = Arrays.asList(".JPEG", ".PNG", ".JPG", ".GIF", ".BMP", "TIF", "PCX", "TGA", "EXIF", "FPX", "SVG", "SVG+XML", "PSD", "CDR", "PCD", "DXF", "UFO", "EPS", "AI", "RAW", "WMF", "WEBP");
	// 匹配后缀音频格式
	private final List<String> audioSuffixList = Arrays.asList(".MP3", ".WAV");

	/**
	 * 获取文件上传的前缀
	 * @return
	 * @apiNote 详见属性文件中com.budaos.file-upload-path的值<br>
	 * 布道师项目<br>
	 * windows环境：D:/uploads<br>
	 * linux环境：/mnt/cbstp/uploads<br>
	 */
	public String getUploadPath() {
		return uploadPath;
	}
	
	public String getAccessPath() {
		return budaosFieAccessPath;
	}
	
	/**
	 * 获取视频格式后缀
	 * @return [".MP4", ".AVI", ".MOV", ".RMVB", ".RM", ".FLV", "3GP"]
	 */
	public List<String> getVideoSuffixList() {
		return videoSuffixList;
	}

	/**
	 * 获取图片格式后缀
	 * @return [".JPEG", ".PNG", ".JPG", ".GIF", ".BMP", "TIF", "PCX", "TGA", "EXIF", "FPX", "SVG", "SVG+XML", "PSD", "CDR", "PCD", "DXF", "UFO", "EPS", "AI", "RAW", "WMF", "WEBP"]
	 */
	public List<String> getImageSuffixList() {
		return imageSuffixList;
	}

	/**
	 * 获取音频格式后缀
	 * @return [".MP3", ".WAV"]
	 */
	public List<String> getAudioSuffixList() {
		return audioSuffixList;
	}


	/**
	 * 根据附件参数类型获取对应的附件存放路径
	 * @param paraNo
	 * @return
	 */
	public String getPathByParaNo(String paraNo) {
		String[] paths = cbUploadPaths.split(",");
		if(StrUtils.isEmpty(paraNo) || Integer.parseInt(paraNo) >= paths.length) {
			return paths[0];
		}
		return paths[Integer.parseInt(paraNo)].trim();
	}
	
	/**
	 * 拼接附件路径（不为空，不为字符串的null，不为https或http开头的才去拼接）
	 * @param sourceName 假设传入值为：budaos.jpg
	 * @param type 可选参数，从属性文件中获取的对应的文件目录名，默认为0：default
	 * @return 示例返回结果： /uploads/default/budaos.jpg
	 */
	public String stitchingPath(Object sourceName, Integer type) {
		if (StrUtils.isNull(sourceName)) {
			return null;
		}
		// 如果没传，默认为0
		if (type == null) {
			type = 0;
		}
		return doStitchingPath(sourceName.toString(), String.valueOf(type));
	}
	
	/**
	 * 拼接附件路径（不为空，不为字符串的null，不为https或http开头的才去拼接）
	 * @param sourceName 假设传入值为：budaos.jpg
	 * @param type 可选参数，从属性文件中获取的对应的文件目录名，默认为0：default
	 * @return 示例返回结果： /uploads/default/budaos.jpg
	 */
	public String stitchingPath(Object sourceName, String type) {
		if (StrUtils.isNull(sourceName)) {
			return "/uploads/defaulthead.png";
		}
		return doStitchingPath(sourceName.toString(), type);
	}
	
	/**
	 * 拼接附件路径（不为空，不为字符串的null，不为https或http开头的才去拼接）
	 * @param sourceName 假设传入值为：budaos.jpg
	 * @param type 可选参数，从属性文件中获取的对应的文件目录名，默认为0：default
	 * @return 示例返回结果： /uploads/default/budaos.jpg
	 */
	public String stitchingPath(String sourceName, String type) {
		if (StrUtils.isEmpty(sourceName)) {
			return "/uploads/defaulthead.png";
		}
		return doStitchingPath(sourceName, type);
	}
	
	/**
	 * 实际拼接
	 * @param sourceName
	 * @param type
	 * @return
	 */
	private String doStitchingPath(String sourceName, String type) {
		String result = sourceName;
		type = StrUtils.isEmpty(type) ? "0" : type;
		if (StrUtils.isNotEmpty(sourceName) && !"null".equals(sourceName)) {
			if (sourceName.length() > 5) {
				String name = sourceName.substring(0, 5);
				// 如果不是网络头像,则拼接地址
				if (name.indexOf("https") == -1 && name.indexOf("http") == -1) {
					// 已经拼接过的不再拼接，没拼接的才拼
					if (sourceName.indexOf("uploads") == -1) {
						result = budaosFieAccessPath + getPathByParaNo(type) + "/" + sourceName;
					}
				}
			}
			
		}
		return result;
	}

	/**
	 * 判断文件后缀
	 * @param sourceName
	 * @return 视频返回video，图片返回image，音频返回audio，默认返回、不属于这三类的返回other
	 */
	public String handleFileSuffix(String sourceName) {
		String resultSuffix = "other";
		if (StrUtils.isNotEmpty(sourceName)) {
			int i = sourceName.lastIndexOf(".");
			String suffix = sourceName.substring(i, sourceName.length());
			// 如果属于视频
			if (videoSuffixList.contains(suffix.toUpperCase())) {
				resultSuffix = "video";
			} else if (imageSuffixList.contains(suffix.toUpperCase())) {
				resultSuffix = "image";
			} else if (audioSuffixList.contains(suffix.toUpperCase())) {
				resultSuffix = "audio";
			}
		}
		return resultSuffix;
	}
	
	/**
	 * 获取文件的绝对路径
	 * @param paraNo
	 * @return
	 * @apiNote 如Linux环境下：/mnt/cbstp/uploads/cloud-pan
	 */
	public String getAbsolutePath(String paraNo) {
		return uploadPath + getPathByParaNo(paraNo);
	}
	
	/**
	 * 获取文件的绝对路径
	 * @param paraNo
	 * @return
	 * @apiNote 如Linux环境下：/mnt/cbstp/uploads/cloud-pan/2bccabad-99e6-4348-95ff-68c5be8044d0.png
	 */
	public String getAbsolutePath(String fileName, String paraNo) {
		return uploadPath + getPathByParaNo(paraNo) + "/" + fileName;
	}
	
}
