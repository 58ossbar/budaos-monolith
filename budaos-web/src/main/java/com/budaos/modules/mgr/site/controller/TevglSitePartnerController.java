package com.budaos.modules.mgr.site.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.site.api.TevglSitePartnerService;
import com.budaos.modules.evgl.site.domain.TevglSitePartner;
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
@RequestMapping("/api/site/tevglsitepartner")
public class TevglSitePartnerController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSitePartnerController.class);
	@Autowired
	private TevglSitePartnerService tevglSitePartnerService;
	@Autowired
	private UploadFileUtils uploadFileUtils;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('site:tevglsitepartner:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglSitePartnerService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('site:tevglsitepartner:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglSitePartnerService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitepartner:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglSitePartnerService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('site:tevglsitepartner:add') or hasAuthority('site:tevglsitepartner:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglSitePartner tevglSitePartner, HttpServletRequest request) {
		String attachId = request.getParameter("attachId");
		if(StrUtils.isEmpty(tevglSitePartner.getCompanyId())) { //新增
			return tevglSitePartnerService.save2(tevglSitePartner, attachId);
		} else {
			return tevglSitePartnerService.update2(tevglSitePartner, attachId);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitepartner:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglSitePartnerService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('site:tevglsitepartner:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglSitePartnerService.deleteBatch(ids);
	}
	
	/**
	 * <p>上传企业logo</p>
	 * @author huj
	 * @data 2019年7月25日
	 * @param picture
	 * @return
	 */
	@PostMapping("/uploadPic")
	public R uploadPic(@RequestPart(value = "file", required = false) MultipartFile picture) {
		return uploadFileUtils.uploadImage(picture, "/partner-img", "7", 0, 0);
	}
	
	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月28日
	 * @param tevglSiteNews
	 * @return
	 */
	@PostMapping("/updateStatus")
	public R updateStatus(@RequestBody(required = false) TevglSitePartner tevglSitePartner) {
		return tevglSitePartnerService.updateState(tevglSitePartner);
	}
}
