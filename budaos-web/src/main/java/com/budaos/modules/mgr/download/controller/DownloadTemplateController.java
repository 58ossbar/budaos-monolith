package com.budaos.modules.mgr.download.controller;

import com.budaos.common.exception.BudaosException;
import com.budaos.modules.common.utils.file.FileUtils;
import com.budaos.modules.evgl.common.config.BudaosConfig;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/download")
public class DownloadTemplateController {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("/downloadClassTraineeTemplate")
	public void downloadClassTraineeTemplate (HttpServletRequest request, HttpServletResponse response){
		try {
			// excel模板路径
			InputStream is = this.getClass().getResourceAsStream("/static/common/docs/班级成员模板.xlsx");
			// 读取excel模板
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			// 读取了模板内所有sheet内容
			String name = "模板";
			// 设置第一个tab的名称
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename="
					.concat(String.valueOf(URLEncoder.encode(name + ".xlsx", "UTF-8"))));
			response.setHeader("Connection", "close");
			workbook.write(response.getOutputStream());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("导出失败", e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("导出失败", e.toString());
		}
	}
	
	/**
	 * 通用下载请求
	 * @param response
	 * @param request
	 * @param fileName
	 * @return
	 */
	@GetMapping("/file")
	public void downloadFile (HttpServletResponse response, HttpServletRequest request, String fileName) {
		try {
			if (!FileUtils.checkAllowDownload(fileName)) {
				throw new BudaosException("文件名称非法，不允许下载");
			}
			String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = BudaosConfig.getDownloadPath() + fileName;
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            FileUtils.deleteFile(filePath);
		} catch (Exception e) {
			log.error("下载文件失败", e);
		}
	}
	
}
