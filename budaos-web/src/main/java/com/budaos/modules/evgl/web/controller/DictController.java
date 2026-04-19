package com.budaos.modules.evgl.web.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.utils.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据字典接口
 * @author admin
 *
 */
@RestController
@RequestMapping("/dict-api")
public class DictController {

	@Autowired
	private DictService dictService;
	
	/**
	 * <p>资源分组</p>  
	 * @author huj
	 * @data 2019年11月25日	
	 * @return
	 */
	@GetMapping("/getResourceGroup")
	public R getResourceGroup() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("resourceGroup"));
	}
	

	/**
	 * <p>从字典获取,教学包使用层次</p>
	 * @author huj
	 * @data 2019年7月26日
	 * @return
	 */
	@GetMapping("/listPkgLevel")
	public R listPkgLevel() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("pkgLevel"));
	}
	
	/**
	 * <p>从字典获取,教学包使用限制</p>
	 * @author huj
	 * @data 2019年7月26日
	 * @return
	 */
	@GetMapping("/listPkgLimit")
	public R listPkgLimit() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("pkgLimit"));
	}
	
	/**
	 * <p>从字典获取，发布方大类(来源字典：学校，个人，创蓝)</p>
	 * @author huj
	 * @data 2019年7月26日
	 * @return
	 */
	@GetMapping("/listDeployMainType")
	public R listDeployMainType() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("deployMainType"));
	}
	
	/**
	 * 活动题目类型
	 * @return
	 */
	@GetMapping("/listQuestionType")
	public R listQuestionType() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("activityQuestionType"));
	}
	
	/**
	 * 教材类型
	 * @return
	 */
	@GetMapping("/listPkgBookType")
	public R listPkgBookType() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("pkg_book_type"));
	}
}
