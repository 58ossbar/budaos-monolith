package com.budaos.modules.evgl.web.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.stu.api.TevglStuStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>就业明星、学员感言、优秀学生</p>
 * <p>Title: TraineeStory.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖布道师学习通</p> 
 * @author huj
 * @date 2019年7月4日
 */
@RestController
@RequestMapping("trainee-story")
public class TraineeStoryController {
	
	@Autowired
	private TevglStuStarService tevglStuStarService;
	
	/**
	 * <p>查看列表</p>
	 * @author huj
	 * @data 2019年7月10日
	 * @param parmas
	 * @return
	 */
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> parmas) {
		return tevglStuStarService.query(parmas);
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
		return tevglStuStarService.view(id);
	}

}
