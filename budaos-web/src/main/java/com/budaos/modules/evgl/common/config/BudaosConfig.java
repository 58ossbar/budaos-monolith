package com.budaos.modules.evgl.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.budaos")
public class BudaosConfig {

	
	private static String baseUrl;
	
	/** 上传路径 */
	private static String fileUploadPath;
	
	/**
	 * 文件显示路径前缀
	 */
	private static String fileAccessPath;

	public static String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		BudaosConfig.baseUrl = baseUrl;
	}

	public static String getUploadPath() {
		return fileUploadPath;
	}

	public static String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		BudaosConfig.fileUploadPath = fileUploadPath;
	}

    public static String getFileAccessPath() {
		return fileAccessPath;
	}

	public void setFileAccessPath(String fileAccessPath) {
		BudaosConfig.fileAccessPath = fileAccessPath;
	}

	/**
     * 获取下载路径
     */
    public static String getDownloadPath(){
        return getFileUploadPath() + "/download/";
    }

	/**
	 * 从typora上传的图片，统一上传至
	 * @return
	 * @apiNote
	 * window环境: D:/uploads/typora/
	 * Linux环境: /mnt/cbstp/uploads/typora/
	 */
	public static String getUploadPathTypora(){
		return getFileUploadPath() + "/typora/";
	}

	public static String getTyporaAccessPath(){
		return getBaseUrl() + getFileAccessPath() + "/typora";
	}
}
