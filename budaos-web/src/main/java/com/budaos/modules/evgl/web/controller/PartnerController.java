package com.budaos.modules.evgl.web.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.api.TevglSitePartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>校企合作、合作企业</p>
 * <p>Title: PartnerController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年7月10日
 */
@RestController
@RequestMapping("/partner-api")
public class PartnerController {

	@Autowired
	private TevglSitePartnerService tevglSitePartnerService;
	
	/**
	 * <p>查询列表</p>
	 * @author huj
	 * @data 2019年7月10日
	 * @param parmas
	 * @return
	 */
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> parmas) {
		parmas.put("state", "Y");
		parmas.put("sidx", "cooperation_time");
		parmas.put("order", "desc");
		return tevglSitePartnerService.query(parmas);
	}
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年7月4日
	 * @param id
	 * @return
	 */
	@GetMapping("/view/{id}")
	public R view(@PathVariable("id") String id) {
		return tevglSitePartnerService.view(id);
	}
	
}
