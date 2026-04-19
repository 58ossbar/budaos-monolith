package com.budaos.modules.mgr.tch.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.evgl.tch.api.TevglTchTeacherService;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
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
@RequestMapping("/api/tch/tevgltchteacher")
public class TevglTchTeacherController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchTeacherController.class);
	@Autowired
	private TevglTchTeacherService tevglTchTeacherService;
	@Autowired
	private UploadFileUtils uploadFileUtils;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('tch:tevgltchteacher:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglTchTeacherService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('tch:tevgltchteacher:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglTchTeacherService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('tch:tevgltchteacher:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglTchTeacherService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('tch:tevgltchteacher:add') or hasAuthority('tch:tevgltchteacher:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglTchTeacher tevglTchTeacher, HttpServletRequest request) {
		String attachId = request.getParameter("attachId");
		if(StrUtils.isEmpty(tevglTchTeacher.getTeacherId())) { //新增
			return tevglTchTeacherService.saveTeacherInfo(tevglTchTeacher, attachId);
		} else {
			return tevglTchTeacherService.updateTeacherInfo(tevglTchTeacher, attachId);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('tch:tevgltchteacher:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglTchTeacherService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('tch:tevgltchteacher:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglTchTeacherService.deleteBatch(ids);
	}
	
	/**
	 * <p>上传教师头像</p>
	 * @author huj
	 * @data 2019年7月25日
	 * @param picture
	 * @return
	 */
	@PostMapping("/uploadPic")
	public R uploadPic(@RequestPart(value = "file", required = false) MultipartFile picture) {
		return uploadFileUtils.uploadImage(picture, "/teacher-img", "5", 0, 0);
	}
	
	/**
	 * <p>更新状态或是否首页显示</p>
	 * @author huj
	 * @data 2019年7月28日
	 * @param tevglTchTeacher
	 * @return
	 */
	@PostMapping("/updateStatus")
	public R updateStatus(@RequestBody(required = false) TevglTchTeacher tevglTchTeacher) {
		return tevglTchTeacherService.updateStateOrShowIndex(tevglTchTeacher);
	}
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@GetMapping("/selectTeacherList")
	@PreAuthorize("hasAuthority('tch:tevgltchteacher:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R selectTeacherList(@RequestParam Map<String, Object> params) {
		params.put("state", "Y");
		params.put("sidx", "t1.create_time");
		params.put("order", "desc");
		return R.ok().put(Constant.R_DATA, tevglTchTeacherService.selectListByMapInnerJoinTraineeTable(params));
	}
}
