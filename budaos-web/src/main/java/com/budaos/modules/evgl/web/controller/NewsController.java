package com.budaos.modules.evgl.web.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.site.api.TevglSiteNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>新闻资讯</p>
 * <p>Title: NewsController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年7月10日
 */
@RestController
@RequestMapping("news-api")
public class NewsController {

	@Autowired
	private TevglSiteNewsService tevglSiteNewsService;
	
	/**
	 * <p>查询列表</p>
	 * @author huj
	 * @data 2019年7月10日
	 * @param params
	 * @return
	 */
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		params.put("scene", "0");
		params.put("status", "2"); // 状态1待审2已发布 3删除
		params.put("sidx", "create_time");
		params.put("order", "desc");
		return tevglSiteNewsService.query(params);
	}
	
	/**
	 * <p>查看新闻资讯明细</p>
	 * @author huj
	 * @data 2019年7月10日
	 * @param id
	 * @return
	 */
	@GetMapping("/view/{id}")
	public R view(@PathVariable("id") String id) {
		return tevglSiteNewsService.view(id);
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
}
