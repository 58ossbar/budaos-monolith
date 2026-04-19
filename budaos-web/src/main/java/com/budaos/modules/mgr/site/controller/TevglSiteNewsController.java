package com.budaos.modules.mgr.site.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.site.api.TevglSiteCheckinfoService;
import com.budaos.modules.evgl.site.api.TevglSiteNewsService;
import com.budaos.modules.evgl.site.domain.TevglSiteNews;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

@RestController
@RequestMapping("/api/site/tevglsitenews")
public class TevglSiteNewsController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteNewsController.class);
	@Autowired
	private TevglSiteNewsService tevglSiteNewsService;
	@Autowired
	private UploadFileUtils uploadFileUtils;
	@Autowired
	private TevglSiteCheckinfoService tevglSiteCheckinfoService;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('site:tevglsitenews:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglSiteNewsService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('site:tevglsitenews:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglSiteNewsService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitenews:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglSiteNewsService.view(id);
	}
	
	/**
	 * <p>进入修改时的查看明细</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @param id
	 * @return
	 */
	@GetMapping("/toEdit/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitenews:view')")
	@SysLog("查看明细")
	public R toEdit(@PathVariable("id") String id) {
		return tevglSiteNewsService.toEdit(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('site:tevglsitenews:add') or hasAuthority('site:tevglsitenews:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglSiteNews tevglSiteNews, HttpServletRequest request) {
		String attachId = request.getParameter("attachId");
		if(StrUtils.isEmpty(tevglSiteNews.getNewsid())) { //新增
			return tevglSiteNewsService.save2(tevglSiteNews, attachId);
		} else {
			return tevglSiteNewsService.update2(tevglSiteNews, attachId);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitenews:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglSiteNewsService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('site:tevglsitenews:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglSiteNewsService.deleteBatch(ids);
	}
	
	/**
	 * <p>获取发布方式</p>
	 * @author huj
	 * @data 2019年7月20日
	 */
	@GetMapping("/getDeployed")
	public R getDeployed() {
		return R.ok().put(Constant.R_DATA, tevglSiteNewsService.getDeployed());
	}
	
	/**
	 * <p>上传图片</p>
	 * @author huj
	 * @data 2019年7月18日
	 * @param picture
	 * @return
	 */
	@PostMapping("/uploadPic")
	public R uploadPic(@RequestPart(value = "file", required = false) MultipartFile picture) {
		return uploadFileUtils.uploadImage(picture, "/news-img", "4", 0, 0);
	}
	
	/**
	 * <p>从字典获取是否头条咨询</p>
	 * @author huj
	 * @data 2019年7月20日
	 * @return
	 */
	@GetMapping("/getIsHeadlineNews")
	public R getIsHeadlineNews() {
		return R.ok().put(Constant.R_DATA, tevglSiteNewsService.getIsHeadlineNews());
	}
	
	/**
	 * <p>从字典获取新增资讯分类,如企业新闻,行业新闻等</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @return
	 */
	@GetMapping("/getCategory")
	public R getCategory() {
		return tevglSiteNewsService.getCategory();
	}
	
	/**
	 * <p>从字典获取状态，状态1待审2已发布 3删除</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @return
	 */
	@GetMapping("/getStatus")
	public R getStatus() {
		return R.ok().put(Constant.R_DATA, tevglSiteNewsService.getNewsStatus());
	}
	
	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月28日
	 * @param tevglTchTeacher
	 * @return
	 */
	@PostMapping("/updateStatus")
	public R updateStatus(@RequestBody(required = false) TevglSiteNews tevglSiteNews) {
		return tevglSiteNewsService.updateState(tevglSiteNews);
	}
	
	/**
	 * <p>新闻资讯审核</p>  
	 * @author huj
	 * @data 2019年8月10日	
	 * @param ids
	 * @return
	 */
	@PostMapping("/check")
	@PreAuthorize("hasAuthority('site:tevglsitenews:check')")
	@SysLog("审核")
	public R check(@RequestBody String[] ids, @RequestParam("state")String state, @RequestParam("reason")String reason) {
		return tevglSiteNewsService.check(ids, state, reason);
	}
	
	/**
	 * <p>获取新闻的审核记录</p>  
	 * @author huj
	 * @data 2019年8月14日	
	 * @param params
	 * @return
	 */
	@GetMapping("/queryCheckInfo")
	public R queryCheckInfo(@RequestParam Map<String, Object> params) {
		return tevglSiteCheckinfoService.query(params);
	}
}
