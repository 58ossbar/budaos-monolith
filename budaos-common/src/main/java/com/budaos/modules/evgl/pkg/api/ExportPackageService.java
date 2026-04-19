package com.budaos.modules.evgl.pkg.api;

/**
 * 导出教学包
 * @author huj
 * @apiNote 
 * 1、教材可以单独导出为PDF文档或word文档
 * 2、云盘文件
 */
public interface ExportPackageService {

	/**
	 * 导出word教材
	 * @param pkgId
	 * @param subjectId
	 * @param traineeId
	 */
	String initWordDataBySubjectId(String pkgId, String subjectId, String ctId, String traineeId);
	
	/**
	 * 拼接处理为用于导出pdf的字符串
	 * @param pkgId
	 * @param subjectId
	 * @return
	 */
	String getHtmlString(String pkgId, String subjectId);
}
