package com.budaos.modules.core.common.utils;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片上传工具类
 * 
 * @author huangwb
 * @data 2019年06月04日 下午2:27:40
 */
@Component
@RefreshScope
public class UploadFileUtils {
	@Autowired
	private TsysAttachService tsysAttachService;

	@Autowired
	private LoginUtils loginUtils;

	@Value("${com.budaos.file-upload-path}")
	public String budaosFieUploadPath;
	@Value("${com.budaos.cb-upload-paths:default}")
	private String cbUploadPaths;
	
	@Autowired
	private UploadFileUtils uploadFileUtils;

	// 匹配后缀图片格式
	private final List<String> imgSuffixList = Arrays.asList(".JPEG", ".PNG", ".JPG", ".GIF", ".BMP");
	// 匹配后缀音频格式
	private final List<String> audioSuffixList = Arrays.asList(".MP3");
	
	/**
	 * <p>上传音频文件</p>
	 * @param file 文件
	 * @param fileDir 可选参数，文件夹名，文件将被上传至该文件夹内
	 * @param fileType
	 * @param fileSize 可选参数，文件大小限制，单位MB 【未实现】
	 * @return
	 */
	public R uploadAudioFile(MultipartFile file, String fileDir, String fileType, Integer fileSize) {
		if (!file.isEmpty()) {
			String newFileName = null; // 新文件名
			String oldName = file.getContentType(); // 原文件类型
			String extension = "." + oldName.substring(oldName.lastIndexOf("/")+1); // 扩展名
			if (audioSuffixList.contains(extension.toUpperCase())) {
				try {
					if(StrUtils.isEmpty(fileDir)) {
						fileDir = uploadFileUtils.getPathByParaNo(fileType);
					}
					newFileName = Identities.uuid() + extension; 
					// 判断目录是否存在 不存在则创建指定的目录
					File f = new File(budaosFieUploadPath + fileDir);
					if (!f.exists()) {
						f.mkdirs();
					}
					// 保存图片地址
					String longName = budaosFieUploadPath + fileDir + "/" + newFileName;
					// 生成文件
					file.transferTo(new File(longName));
					// 上传成功之后保存在附件中添加一条数据
					String atachId = tsysAttachService.uploadFileInsertAttach(oldName, extension,
							String.valueOf(file.getSize()), newFileName, longName, fileType,
							loginUtils.getLoginUserId());
					// 返回给前端参数
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fileName", newFileName);
					map.put("attachId", atachId);
					return R.ok().put("data", map);
				} catch (Exception e) {
					e.printStackTrace();
					return R.error("上传失败");
				}
			} else {
				return R.error("格式只能为：" + audioSuffixList);
			}
		}
		return R.ok();
	}
	
	/**
	 * 单图片上传保存附件
	 * 
	 * @param dictIcon  上传文件
	 * @param fileDir   文件目录 例如：/dict
	 * @param maxWidth  图片最大长度
	 * @param maxHeight 图片最大宽度
	 * @return
	 * @author huangwb
	 * @data 2019年06月04日 下午2:27:40
	 */
	public R uploadImage(MultipartFile dictIcon, String fileDir, String fileType, int maxWidth, int maxHeigth) {
		if (!dictIcon.isEmpty()) {
			String oldNamePc = dictIcon.getContentType();
			// 获取图片后缀格式
			String extensionPc = "." + oldNamePc.substring(oldNamePc.lastIndexOf("/") + 1);
			String imgNamePc = null;
			// 如果图片格式正确
			if (imgSuffixList.contains(extensionPc.toUpperCase())) {
				try {
					if(StrUtils.isEmpty(fileDir)) {
						fileDir = getPathByParaNo(fileType);
					}
					// 图片名称
					imgNamePc = UUID.randomUUID() + extensionPc;
					// 判断目录是否存在 不存在则创建指定的目录
					File file = new File(budaosFieUploadPath + fileDir);
					if (!file.exists()) {
						file.mkdir();
					}
					// 保存图片地址
					String longNamePc = budaosFieUploadPath + fileDir + "/" + imgNamePc;
					// 调用图片压缩上传操作
					uploadImgThread(dictIcon, longNamePc, maxWidth, maxHeigth);
					// 上传成功之后保存在附件中添加一条数据
					String atachId = tsysAttachService.uploadFileInsertAttach(oldNamePc, extensionPc,
							String.valueOf(dictIcon.getSize()), imgNamePc, longNamePc, fileType,
							loginUtils.getLoginUserId());
					// 返回给前端参数
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("imgNamePc", imgNamePc);
					map.put("attachId", atachId);
					return R.ok().put("data", map);
				} catch (IllegalStateException e) {
					return R.error(e.getMessage()).put("data", imgNamePc);
				}
			} else {
				return R.error("图片格式不对,请上传正确的图片");
			}
		}
		return R.error("您暂未选择任何图片上传");
	}

	/**
	 *
	 * @desc //TODO 图片普遍压缩策略 根据不同的图片大小对图片进行不同比例的压缩
	 * @author huangwb
	 * 
	 * @data 2019年5月24日 下午4:27:40
	 */
	private void pictureCompressionStrategy(MultipartFile iocn, String url) throws IOException {
		// of来源 outputFormat修改图片格式 修改PNG为JPG或JPEG会报错
		// scale图片缩放比例值越小压缩越小
		// outputQuality图片质量越小图片质量越小 .size对图片按照指定格式缩放
		// .toFile文件保存的路径加图片名称
		// 转换成kb
		long pictureSize = iocn.getSize() / 1024;
		if (pictureSize > (5 * 1024)) {
			Thumbnails.of(iocn.getInputStream()).scale(0.15f).outputQuality(0.25f).toFile(url);
		} else if (pictureSize >= 1024) {
			Thumbnails.of(iocn.getInputStream()).scale(0.2f).outputQuality(0.25f).toFile(url);
		} else if (pictureSize >= 500) {
			Thumbnails.of(iocn.getInputStream()).scale(0.25f).outputQuality(0.25f).toFile(url);
		} else if (pictureSize >= 300) {
			Thumbnails.of(iocn.getInputStream()).scale(0.3f).outputQuality(0.25f).toFile(url);
		} 
		/*else if (pictureSize >= 200) {
			Thumbnails.of(iocn.getInputStream()).scale(0.5f).outputQuality(0.25f).toFile(url);
		} else if (pictureSize >= 100) {
			Thumbnails.of(iocn.getInputStream()).scale(0.6f).outputQuality(0.25f).toFile(url);
		} else if (pictureSize >= 40) {
			Thumbnails.of(iocn.getInputStream()).scale(0.8f).outputQuality(0.25f).toFile(url);
		}*/ else {
			Thumbnails.of(iocn.getInputStream()).scale(1f).toFile(url);
		}
	}

	/**
	 *
	 * @desc //TODO 设置中上传图片需要对图片进行等长度
	 * @author huangwb
	 * 
	 * @data 2019年5月24日 下午4:27:40
	 */
	private void pictureCompressionStrategy(MultipartFile iocn, String url, int maxWidth, int maxHeigth)
			throws IOException {
		Thumbnails.of(iocn.getInputStream()).size(maxWidth, maxHeigth).keepAspectRatio(false).toFile(url);
	}

	/**
	 * 
	 * @desc //TODO 开启线程保证用户上传大图时 刷新界面图片依旧能上传到服务器
	 * @author huangwb
	 * @param icon       上传头像的接收参数
	 * @param longNamePc 上传图片长地址
	 * 
	 * @data 2019年5月24日 下午4:27:40
	 */
	private void uploadImgThread(MultipartFile icon, String longNamePc, int maxWidth, int maxHeigth) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (maxWidth == 0 || maxHeigth == 0) {
						// 调用图片压缩策略
						pictureCompressionStrategy(icon, longNamePc);
					} else {
						pictureCompressionStrategy(icon, longNamePc, maxWidth, maxHeigth);
					}
				} catch (Exception e) {
					try {
						icon.transferTo(new File(longNamePc));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * 根据附件参数类型获取对应的附件存放路径
	 * @param ParaNo
	 * @return
	 */
	public String getPathByParaNo(String paraNo) {
		String[] paths = cbUploadPaths.split(",");
		if(StrUtils.isEmpty(paraNo) || Integer.parseInt(paraNo) >= paths.length) {
			return paths[0];
		}
		return paths[Integer.parseInt(paraNo)].trim();
	}
}
