package com.budaos.modules.evgl.web.controller.forum;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.forum.api.TevglForumFriendTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 友情社区
 * @author huj
 *
 */
@RestController
@RequestMapping("/site/forum/friend")
public class FriendController {
	
	@Autowired
	private TevglForumFriendTypeService tevglForumFriendTypeService;
	
	/**
	 * 分类列表
	 * @param params
	 * @return
	 */
	@RequestMapping("/queryTypeList")
	public R queryTypeList(@RequestParam Map<String, Object> params) {
		return tevglForumFriendTypeService.queryTypeList(params);
	}
	
}
