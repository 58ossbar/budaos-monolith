package com.budaos.modules.mgr.stu.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.stu.api.TevglStuStarService;
import com.budaos.modules.evgl.stu.domain.TevglStuStar;
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
@RequestMapping("/api/stu/tevglstustar")
public class TevglStuStarController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglStuStarController.class);
	@Autowired
	private TevglStuStarService tevglStuStarService;
	@Autowired
	private UploadFileUtils uploadFileUtils;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('stu:tevglstustar:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglStuStarService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('stu:tevglstustar:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglStuStarService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('stu:tevglstustar:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglStuStarService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('stu:tevglstustar:add') or hasAuthority('stu:tevglstustar:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglStuStar tevglStuStar, HttpServletRequest request) {
		String attachId = request.getParameter("attachId");
		if(StrUtils.isEmpty(tevglStuStar.getStarId())) { //新增
			return tevglStuStarService.save2(tevglStuStar, attachId);
		} else {
			return tevglStuStarService.update2(tevglStuStar, attachId);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('stu:tevglstustar:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglStuStarService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('stu:tevglstustar:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglStuStarService.deleteBatch(ids);
	}
	
	/**
	 * <p>上传头像</p>
	 * @author huj
	 * @data 2019年7月25日
	 * @param picture
	 * @return
	 */
	@PostMapping("/uploadPic")
	public R uploadPic(@RequestPart(value = "file", required = false) MultipartFile picture) {
		return uploadFileUtils.uploadImage(picture, "/stuStar-img", "6", 0, 0);
	}
	
	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月28日
	 * @param tevglSitePartner
	 * @return
	 */
	@PostMapping("/updateStatus")
	public R updateStatus(@RequestBody(required = false) TevglStuStar tevglStuStar) {
		return tevglStuStarService.updateState(tevglStuStar);
	}
}
