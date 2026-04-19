package com.budaos.modules.evgl.cloudpan.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;

import javax.servlet.http.HttpServletRequest;
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

public interface TcloudPanDirectoryService extends IBaseService<TcloudPanDirectory>{
	
	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	TcloudPanDirectory selectObjectById(Object id);
	
	/**
	 * PC网站端专用，顶级目录列表
	 * @param params
	 * @param loginUserId
	 * @param queryForWx 为true时，兼容小程序的，要查询直接归属于教学包的文件出来，而false时，不需要，pc端页面布局不一致
	 * @return
	 */
	R queryDirListNew(Map<String, Object> params, String loginUserId, boolean queryForWx);
	
	/**
	 * 云盘列表（小程序专属）（区别开，便于维护）
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	R queryDirListNewForWx(Map<String, Object> params, String loginUserId);
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectSimpleListMap(Map<String, Object> params);
	
	/**
	 * 查询顶级目录
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	List<Map<String, Object>> selectTopDirectoryList(Map<String, Object> params, String loginUserId);
	
	/**
	 * 新建文件夹
	 * @param pkgId
	 * @param dirId
	 * @param name 文件夹名
	 * @param icon
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	R createDir(String pkgId, String dirId, String name, String icon, String loginUserId);
	
    /**
	 * 查看（点击目录查询子目录和文件）（已废弃，暂不删除）
	 * @param ctId
	 * @param pkgId
	 * @param dirId
	 * @param name
	 * @param loginUserId
	 * @return
	 */
	@Deprecated
    R viewDetailNew(String ctId, String pkgId, String dirId, String name, String loginUserId);
    
    /**
     * 点击目录查询数据（教学包页面专用方法）（注意，dirId可能传值为空，则表示是在全部下传参）
     * @param pkgId 教学包id
     * @param dirId 文件夹id或空（注意，dirId可能传值为空，则表示是在全部下传参）
     * @param name 按名称查询
     * @param loginUserId 当前登录用户
     * @return
     */
    R viewDetailForPkgPage(String pkgId, String dirId, String name, String loginUserId);
    
    /**
     * 点击目录查询数据（课堂页面专用方法）（注意，dirId可能传值为空，则表示是在全部下传参）
     * @param ctId 课堂id
     * @param pkgId 课堂对应教学包id
     * @param dirId 文件夹id或空（注意，dirId可能传值为空，则表示是在全部下传参）
     * @param name 按名称查询
     * @param loginUserId 当前登录用户
     * @return
     */
    R viewDetailForRoomPage(String ctId, String pkgId, String dirId, String name, String loginUserId);
    
    /**
     * 重命名
     * @param dirId 当前被重命名的文件夹
     * @param name 用户输入的文件夹名称
     * @param icon 用户选中的图标
     * @param loginUserId 当前登录用户
     * @return
     */
    R rename(String dirId, String name, String icon, String loginUserId);
    
    /**
	 * 批量删除目录和文件
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	R deletesNew(JSONObject jsonObject, String loginUserId);
    
    /**
     * 获取目录树（包含文件夹和文件，且包含直接归属于教学包下的文件）
     * @param loginUserId
     * @return
     */
    R getDirectoryTree(String pkgId, String loginUserId);
    
    /**
     * 获取目录树（只包含文件夹）
     * @param pkgId
     * @param loginUserId
     * @return
     */
    List<Map<String, Object>> getDirectoryTree2(String pkgId, String loginUserId);
    
    /**
     * 移动
     * @param jsonObject
     * @param loginUserId
     * @return
     */
    R move(JSONObject jsonObject, String loginUserId);
    
    /**
     * 复制
     * @param jsonObject
     * @param loginUserId
     * @return
     */
    R copy(JSONObject jsonObject, String loginUserId);
    
    /**
     * 按名称搜索（只在本目录下搜索目录和文件）（小程序专用）
     * @param name
     * @param loginUserId
     * @return
     */
    R search(String ctId, Object pkgId, String dirId, String name, String loginUserId);
    
    /**
     * 文件夹上传
     * @param request
     * @param pkgId 必传参数，教学包id
     * @param dirId 选传参数，目录id，当该值未传时，上传至教学包id目录下
     * @param loginUserId 当前登录用户
     * @return
     */
    R uploadFolder(HttpServletRequest request, String pkgId, String dirId, String loginUserId);
}