package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.LoginUtils;
import com.budaos.modules.core.common.utils.UploadFileUtils;
import com.budaos.modules.sys.api.TsysDictService;
import com.budaos.modules.sys.domain.TsysDict;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/sys/dict")
public class TsysDictController {
	@Autowired
	private TsysDictService tsysDictService;

	@Autowired
	private LoginUtils loginUtils;

	@Autowired
	private UploadFileUtils uploadFileUtils;

	/**
	 * 根据条件查询数据,用于前端数据操作
	 * 
	 * @param parentType
	 * @return
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@GetMapping("/dictByDictType/{parentType}")
	@SysLog("获取字典菜单列表")
	public R dictByDictType(@PathVariable("parentType") String parentType) {
		if (parentType == null || StringUtils.isBlank(parentType)) {
			return R.error("您的参数信息有误，请检查参数信息是否正确");
		}
		return tsysDictService.dictByDictType(parentType);
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 * @return R
	 */
	@DeleteMapping("/deletes")
	@PreAuthorize("hasAuthority('sys:tsysdict:remove')")
	@SysLog("删除字典")
	public R deleteType(@RequestBody(required = false) String[] ids) {
		if (ids == null) {
			return R.error("您的参数信息有误，请检查参数信息是否正确");
		}
		return tsysDictService.deleteType(ids);
	}

	/**
	 * 保存修改操作
	 * 
	 * @param tsysDict
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@PostMapping("saveOrUpdate")
	@PreAuthorize("hasAuthority('sys:tsysdict:add') and hasAuthority('sys:tsysdict:edit')")
	@SysLog("字典的新增或修改")
	public R saveOrUpdate(@RequestBody(required = false) TsysDict tsysDict, HttpServletRequest request) {
		if (tsysDict.getDictId() == null || StringUtils.isBlank(tsysDict.getDictId())) {
			tsysDict.setCreateUserId(loginUtils.getLoginUserId());
		} else {
			tsysDict.setUpdateUserId(loginUtils.getLoginUserId());
		}
		return tsysDictService.saveOrUpdate(tsysDict, request.getParameter("attachId"));
	}

	@PostMapping("uploadIcon")
	@SysLog("字典的图标上传")
	public R uploadDictIcon(@RequestParam(name = "dictIcon", required = false) MultipartFile dictIcon) {
		return uploadFileUtils.uploadImage(dictIcon, "/dict", "1",0,0);
	}

	/**
	 * 获取字典详情信息
	 * 
	 * @param dictId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@GetMapping("/get/{dictId}")
	@PreAuthorize("hasAuthority('sys:tsysdict:view')")
	@SysLog("获取字典信息的详情")
	public R getDictInfo(@PathVariable("dictId") String dictId) {
		if (dictId == null || StringUtils.isBlank(dictId)) {
			return R.error("您的参数信息有误，请检查参数信息是否正确");
		}
		return tsysDictService.getDctInfo(dictId);
	}

	/**
	 * 查询操作
	 * 
	 * @param params (page页码,limit显示条数)
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@GetMapping("query")
	@PreAuthorize("hasAuthority('sys:tsysdict:query')")
	@SysLog("获取字典信息列表")
	public R query(@RequestParam(required = false) Map<String, Object> params) {
		return tsysDictService.query(params);
	}

	/**
	 * 根据条件查询数据 (字典左侧树)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@GetMapping("/dicttree")
	@PreAuthorize("hasAuthority('sys:tsysdict:query')")
	@SysLog("获取字典左侧数列表")
	public R dicttree(@RequestParam(required = false) String dictname) {
		return tsysDictService.dicttree(dictname);
	}
	
	/**
	 * 前端字典组件获取数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@GetMapping("/cbdict")
	@SysLog("前端字典组件获取数据")
	public R cbdict(@RequestParam(required = false) Map<String, Object> map) {
		return tsysDictService.selectListByMapNotZero(map);
	}
}
