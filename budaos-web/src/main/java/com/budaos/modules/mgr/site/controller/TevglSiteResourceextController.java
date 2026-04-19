package com.budaos.modules.mgr.site.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.site.api.TevglSiteResourceextService;
import com.budaos.modules.evgl.site.domain.TevglSiteResourceext;
import com.budaos.modules.sys.domain.TsysResource;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api/site/tevglsiteresourceext")
public class TevglSiteResourceextController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteResourceextController.class);
	@Autowired
	private TevglSiteResourceextService tevglSiteResourceextService;
	@Autowired
	private UploadFileUtils uploadFileUtils;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('site:tevglsiteresourceext:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglSiteResourceextService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('site:tevglsiteresourceext:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglSiteResourceextService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('site:tevglsiteresourceext:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglSiteResourceextService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('site:tevglsiteresourceext:add') or hasAuthority('site:tevglsiteresourceext:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglSiteResourceext tevglSiteResourceext) {
		if(StrUtils.isEmpty(tevglSiteResourceext.getSiteId())) { //新增
			return tevglSiteResourceextService.save(tevglSiteResourceext);
		} else {
			return tevglSiteResourceextService.update(tevglSiteResourceext);
		}
	}
	
	/**
	 * <p>新增站点栏目</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param menu
	 * @return
	 */
	@RequestMapping("/saveorupdatesite")
	@PreAuthorize("hasAuthority('site:tevglsiteresourceext:add') or hasAuthority('site:tevglsiteresourceext:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdateSite(TsysResource menu){
		//TODO
		return R.ok().put("menuId", menu.getMenuId());
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('site:tevglsiteresourceext:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglSiteResourceextService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('site:tevglsiteresourceext:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestParam(required = true) String[] ids) {
		return tevglSiteResourceextService.deleteBatch(ids);
	}
	
	/**
	 * <p>获取站点栏目</p>
	 * @author huj
	 * @data 2019年7月15日
	 * @param request
	 * @return
	 */
	@GetMapping("/querySite")
	@SysLog("获取全部站点栏目")
	public R querySite(@RequestParam Map<String, Object> map) {
		String menuId = (String) map.get("menuId");
		if ("".equals(menuId) || "undefined".equals(menuId) || "null".equals(menuId)) {
			menuId = null;
		}
		return R.ok().put(Constant.R_DATA, tevglSiteResourceextService.querySite(menuId));
	}
	
	/**
	 * <p>查看站点栏目信息</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param id
	 * @return
	 */
	@GetMapping("/viewSite/{id}")
	@SysLog("查看站点栏目信息")
	public R viewSite(@PathVariable("id") String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteResourceextService.viewSite(id));
	}
	
	/**
	 * <p>新增或修改站点栏目</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param menu
	 * @return
	 */
	@PostMapping("/saveSite")
	public R saveSite(@RequestBody(required = false) TsysResource menu) {
		return tevglSiteResourceextService.saveOrUpdateSite(menu);
	}
	
	/**
	 * <p></p>
	 * @author huj
	 * @data 2019年7月17日
	 * @param id
	 * @return
	 */
	@GetMapping("/editsite/{id}")
	public R editSite(@PathVariable("id") String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteResourceextService.editSite(id));
	}
	
	/**
	 * <p>删除栏目信息</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @param id
	 * @return
	 */
	@GetMapping("/removeSite/{id}")
	public R removeSite(@PathVariable("id") String id) {
		return tevglSiteResourceextService.deleteSite(id);
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
		return uploadFileUtils.uploadImage(picture, "/tevglsiteresourceext-img", "2", 0, 0);
	}
	
	/**
	 * <p>获取类型,如【站点】【栏目】</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @return
	 */
	@GetMapping("/getSiteType")
	public R getSiteType() {
		return tevglSiteResourceextService.getSiteType();
	}
	
	/**
	 * <p>获取站点行业类型,如IT互联网、汽车等</p>
	 * @author huj
	 * @data 2019年7月20日
	 * @return
	 */
	@GetMapping("/getTradeType")
	public R getTradeType() {
		return R.ok().put(Constant.R_DATA, tevglSiteResourceextService.getTradeType());
	}
	
}
