package com.budaos.modules.evgl.cloudpan.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TcloudPanFileService extends IBaseService<TcloudPanFile>{
	
	List<TcloudPanFile> selectListByMap(Map<String, Object> params);
	
	void insert(TcloudPanFile tcloudPanFile);
	
	Integer getMaxSortNum(Map<String, Object> params);
	
	/**
	 * 上传文件
	 * @param request
	 * @param dirId
	 * @param loginUserId
	 * @param pkgId
	 * @return
	 */
	R uploadFiles(HttpServletRequest request, String dirId, String loginUserId, String pkgId);
	
	/**
	 * 重命名
	 * @param fileId
	 * @param name
	 * @param loginUserId
	 * @param pkgId
	 * @return
	 */
	R rename(String fileId, String name, String loginUserId, String pkgId);
	
	/**
	 * 确认重命名
	 * @param fileId
	 * @param name
	 * @param loginUserId
	 * @return
	 */
	R confirm(String fileId, String name, String loginUserId);
	
	/**
	 * 删除文件
	 * @param pkgId
	 * @param fileId
	 * @param loginUserId
	 * @return
	 */
	R deleteFile(String pkgId, String fileId, String loginUserId);
	
	/**
	 * 批量删除
	 * @param pkgId
	 * @param fileIds
	 * @param loginUserId
	 * @return
	 */
	R deleteFileBatch(String pkgId, List<String> fileIds, String loginUserId);

	/**
	 * 下载
	 * @param response
	 * @param fileId
	 * @return
	 */
	void downloadFile(HttpServletResponse response, String fileId, String pkgId);
}