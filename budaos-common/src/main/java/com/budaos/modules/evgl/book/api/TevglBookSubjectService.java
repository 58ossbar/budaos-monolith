package com.budaos.modules.evgl.book.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.vo.TevglBookSubjectSelectVo;
import com.budaos.modules.sys.domain.TsysDict;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: 课程教材</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglBookSubjectService extends IBaseService<TevglBookSubject>{
	
	Map<String, Object> getBook(String ctId, String pkgId, String subjectId, String chapterName, Map<String, Object> configuration);
	Map<String, Object> getBook2(String ctId, String pkgId, String subjectId, String queryName, String loginUserId);
	/**
	 * <p></p>
	 * @author huj
	 * @data 2019年8月7日
	 * @param id
	 * @return
	 */
	TevglBookSubject selectObjectById(String id);
	
	/**
	 * <p>获取具有层次机构的树形数据</p>
	 * @author huj
	 * @data 2019年7月6日
	 * @param pkgId 必传参数，教学包主键（由于目前课程与教学包可能是一对多的关系）
	 * @param subjectId 必传参数，课程或教材主键
	 * @param queryName 可选参数，输入要搜索的章节名称
	 * @param configuration 可选参数 {"loginUserId": "[当前登录用户id]", "queryPermission": "[布尔值,是否需要查询当前登录用户所拥有的章节权限]"}
	 * @return
	 */
	//Map<String, Object> getBook(String ctId, String pkgId, String subjectId, String queryName, Map<String, Object> configuration);
	
	/**
	 * 获取树形数据（只包含章节）
	 * @param subjectId
	 * @return
	 */
	List<Map<String, Object>> getTree(String subjectId, String pkgId);
	
	/**
	 * 管理端获取树形数据
	 * @param subjectId
	 * @return
	 */
	R getTreeForMgr(String subjectId);
	
	/**
	 * 复制[章节][分组][资源]
	 * @param inputSubjectId 选择的课程id
	 * @param newSubjectId 新的课程id
	 * @param loginUserId 当前登录用户
	 * @param newPkgId 新的教学包主键
	 * @return
	 */
	R copy(String inputSubjectId, String newSubjectId, String loginUserId, String newPkgId);
	
	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param map 参数中若isSubjectRefNull为"Y"则查询的是课程，若isSubjectRefNotNull为"Y"则查询的是活教材
	 * @return
	 */
	R selectListByMapForWeb(Map<String, Object> map);
	
	/**
	 * <p>根据条件查询记录</p>  
	 * @author huj
	 * @data 2019年12月25日	
	 * @param map 参数中若isSubjectRefNull为"Y"则查询的是课程，若isSubjectRefNotNull为"Y"则查询的是活教材
	 * @return
	 */
	R selectListByMapForMgr(Map<String, Object> map);
	
	/**
	 * <p>重命名</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param id
	 * @param name
	 * @return
	 */
	R rename(String id, String name);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param id
	 * @param loginUserId
	 * @return
	 */
	R remove(String id, String loginUserId);
	
	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月27日
	 * @param id
	 * @return
	 */
	R updateState(TevglBookSubject tevglBookSubject);
	
	/**
	 * <p>从字典获取活教材封面图，满足前端录入界面需要</p>
	 * @author huj
	 * @data 2019年8月6日
	 * @return
	 */
	List<TsysDict> getSubjectLogo();
	
	/**
	 * <p>查看课程相关信息</p>
	 * @author huj
	 * @data 2019年8月6日
	 * @param id 课程ID
	 * @return
	 */
	R viewForEvglWeb(String id);
	
	/**
	 * <p>根据ID,组织word文档内容</p>
	 * @author huj
	 * @data 2019年8月7日
	 * @param id
	 * @return
	 */
	String initWordDataById(String id);
	R toexport(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * <p>更新阅读量、星级、收藏数、点赞数、举报数等</p>  
	 * @author huj
	 * @data 2019年8月14日	
	 * @param map
	 * @return
	 */
	void updateNum(TevglBookSubject tevglBookSubject);
	
	/**
	 * <p>收藏</p>  
	 * @author huj
	 * @data 2019年8月15日	
	 * @param targetId 被收藏的ID
	 * @param loginUserId 当前登录用户ID
	 * @param toTraineeId 被收藏的文件的所属人ID
	 * @return
	 */
	R collect(String targetId, String loginUserId, String toTraineeId);
	
	/**
	 * <p>取消收藏</p>  
	 * @author huj
	 * @data 2019年8月15日	
	 * @param targetId 被取消收藏的ID
	 * @param loginUserId 当前登录用户ID
	 * @param toTraineeId 被取消收藏的文件的所属人ID
	 * @return
	 */
	R cancelCollect(String targetId, String loginUserId, String toTraineeId);
	
	/**
	 * <p>获取活教材而非课程（无分页）</p>  
	 * @author huj
	 * @data 2019年8月20日	
	 * @param map
	 * @return
	 */
	List<TevglBookSubject> getLiveSubjectList(Map<String, Object> map);
	
	/**
	 * 课程下拉列表，注意此方法只会查询subject_ref为空的记录
	 * @param params
	 * @return
	 */
	R listSelectSubject(Map<String, Object> params);
	
	/**
	 * 获取一本书（章节树）（课堂页面专用）（优化版）
	 * @param ctId
	 * @param pkgId
	 * @param subjectId
	 * @param chapterName
	 * @param loginUserId
	 * @param queryForWx
	 * @param identity 身份标识，用于区别缓存，值：teacher、trainee
	 * @return
	 */
	R getBookForRoomPage(String ctId, String pkgId, String subjectId, String chapterName, String loginUserId, boolean queryForWx, String identity);
	
	/**
    * 获取一本书（章节树）（教学包页面专用）（优化版）
    * @param pkgId
    * @param subjectId
    * @param chapterName
    * @param loginUserId
    * @return
    */
   R getBookForPkgPage(String pkgId, String subjectId, String chapterName, String loginUserId);
	
   /**
    * 根据条件查询课程
    * @param map
    * @return
    */
   List<TevglBookSubjectSelectVo> querySubjectList(Map<String, Object> map);
}