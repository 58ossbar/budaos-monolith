package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.core.common.utils.LoginUtils;
import com.budaos.modules.sys.api.TsysParameterService;
import com.budaos.modules.sys.domain.TsysParameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sys/parameter")
public class TsysParameterController {
	@Autowired
	private TsysParameterService tsysParameterService;
	@Autowired
	private LoginUtils loginUtils;

	/**
	 * 保存修改
	 * 
	 * @param tsysparameter
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@PostMapping("/saveOrUpdate")
	@PreAuthorize("hasAuthority('sys:tsysparameter:add') and hasAuthority('sys:tsysparameter:edit')")
	@SysLog("参数信息保存或修改")
	public R saveorupdate(@RequestBody(required = false) TsysParameter tsysparameter) {
		if (tsysparameter.getParaid() == null || StringUtils.isBlank(tsysparameter.getParaid())) {
			tsysparameter.setCreateUserId(loginUtils.getLoginUserId());
		} else {
			tsysparameter.setUpdateUserId(loginUtils.getLoginUserId());
		}
		return tsysParameterService.saveorupdate(tsysparameter);
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@DeleteMapping("/deletes")
	@PreAuthorize("hasAuthority('sys:tsysparameter:remove')")
	@SysLog("删除参数信息")
	public R delete(@RequestBody(required = false) String[] ids) {
		if (ids == null) {
			return R.error("您的参数信息有误，请检查参数信息是否正确");
		}
		return tsysParameterService.delete(ids);
	}

	/**
	 * 所有配置列表
	 * 
	 * @param params (page页码,limit显示条数，paraType字典类型)
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('sys:tsysparameter:query')")
	@SysLog("查询所有参数信息")
	public R list(@RequestParam(required = false) Map<String, Object> params) {
		return tsysParameterService.list(params);
	}

	/**
	 * 根据参数Id获取参数详情
	 * 
	 * @param parameterId
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@GetMapping("/get/{parameterId}")
	@PreAuthorize("hasAuthority('sys:tsysparameter:view')")
	@SysLog("获取指定参数详情")
	public R getParameterInfo(@PathVariable("parameterId") String parameterId) {
		return tsysParameterService.get(parameterId);
	}

	/**
	 * 根据参数管理左侧的树结构
	 * 
	 * @return R
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	@GetMapping("/paraTree")
	@PreAuthorize("hasAuthority('sys:tsysparameter:query')")
	@SysLog("参数管理左侧树结构")
	public R paraTree() {
		return tsysParameterService.paraTree();
	}

	@GetMapping("/getPara")
	@SysLog("查询下拉选项参数信息")
	public R getPara(@RequestParam("paraType")String paraType) {
		return tsysParameterService.getPara(paraType);
	}
}
