package com.budaos.modules.evgl.web.controller.forum;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.evgl.common.CbUploadUtils;
import com.budaos.modules.evgl.common.CheckSession;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.forum.api.TevglForumBlogPostService;
import com.budaos.modules.evgl.forum.domain.TevglForumBlogPost;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 博客控制类，增删改查，点赞，收藏
 * @author huj
 *
 */
@RestController
@RequestMapping("/site/forum/blog")
public class BlogController {
	
	@Autowired
	private TevglForumBlogPostService tevglForumBlogPostService;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private CbUploadUtils cbUploadUtils;
	
	/**
	 * 博客列表
	 * @author zhouyl加
	 * @date 2021年2月27日
	 * @param request
	 * @param params
	 * @param filterType
	 * @return
	 */
	@GetMapping("queryBlogList")
	//@CheckSession
	public R queryBlogList(HttpServletRequest request, @RequestParam Map<String, Object> params, String filterType) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		/*if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}*/
		return tevglForumBlogPostService.queryBlogList(params, traineeInfo == null ? null : traineeInfo.getTraineeId(), filterType);
	}
	
	/**
	 * 写博客
	 * @param request
	 * @param tevglForumBlogPost
	 * @return
	 */
	@PostMapping("writeBlogInfo")
	@CheckSession
	public R writeBlogInfo(HttpServletRequest request, @RequestBody TevglForumBlogPost tevglForumBlogPost) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		tevglForumBlogPost.setTraineeId(traineeInfo.getTraineeId());
		if (StrUtils.isEmpty(tevglForumBlogPost.getPostId())) {
			return tevglForumBlogPostService.save(tevglForumBlogPost);
		} else {
			return tevglForumBlogPostService.update(tevglForumBlogPost);
		}
	}
	
	/**
	 * 批量删除博客
	 * @author zhouyl加
	 * @date 2021年2月26日
	 * @param request
	 * @param postId
	 * @return
	 */
	@PostMapping("deleteBlog")
	@CheckSession
	public R deleteBlog(HttpServletRequest request, String postId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglForumBlogPostService.deleteBlog(postId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 收藏以及取消收藏
	 * @author zhouyl加
	 * @date 2021年3月1日
	 * @param request
	 * @param postId
	 * @return
	 */
	@PostMapping("collect")
	@CheckSession
	public R collect(HttpServletRequest request, String postId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglForumBlogPostService.collect(postId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 点赞以及取消点赞
	 * @author zhouyl加
	 * @date 2021年3月1日
	 * @param request
	 * @param postId
	 * @return
	 */
	@PostMapping("like")
	@CheckSession
	public R like(HttpServletRequest request, String postId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		return tevglForumBlogPostService.like(postId, traineeInfo.getTraineeId());
	}
	
	/**
	 * 查找推荐标签
	 * @author zhouyl加
	 * @date 2021年3月2日
	 * @return
	 */
	@GetMapping("recommendedLabel")
	public R recommendedLabel() {
		return tevglForumBlogPostService.recommendedLabel();
	}
	
	/**
	 * 查找热门博主
	 * @author zhouyl加
	 * @date 2021年3月2日
	 * @return
	 */
	@GetMapping("popularBloggers")
	public R popularBloggers() {
		return tevglForumBlogPostService.popularBloggers();
	}
	
	/**
	 * 查找友情社区
	 * @author zhouyl加
	 * @date 2021年3月2日
	 * @return
	 */
	@GetMapping("friendshipCommunity")
	public R friendshipCommunity() {
		return tevglForumBlogPostService.friendshipCommunity();
	}
	
	/**
	 * 获取博客详情
	 * @author zhouyl加
	 * @date 2021年3月3日
	 * @param request
	 * @param postId
	 * @return
	 */
	@GetMapping("queryBlogDetails")
	public R queryBlogDetails(HttpServletRequest request, String postId) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		String traineeId = "";
		if (traineeInfo != null) {
			traineeId = traineeInfo.getTraineeId();
		}
		return tevglForumBlogPostService.viewBlogDetails(postId, traineeId);
	}
	
	/**
	 * 获取技术标签云
	 * @return
	 */
	@GetMapping("/getSubjectTypeList")
	public R getSubjectTypeList() {
		return R.ok().put(Constant.R_DATA, dictService.getDictList("subjectType"));
	}
	
	/**
	 * 更换博客背景图
	 * @param request
	 * @param file
	 * @return
	 */
	@PostMapping("/changeBackgroundImage")
	@CheckSession
	public R changeBackgroundImage(HttpServletRequest request, @RequestPart(required = true) MultipartFile file) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		if (file == null || file.isEmpty()) {
			return R.ok();
		}
		R r = cbUploadUtils.uploads(file, "25", null, "image");
		if (!r.get("code").equals(0)) {
			return r;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) r.get(Constant.R_DATA);
		String fileName = data.get("fileName").toString();
		TevglTraineeInfo t = new TevglTraineeInfo();
		t.setTraineeId(traineeInfo.getTraineeId());
		t.setBlogBgPic(fileName);
		tevglTraineeInfoService.update(t);
		return R.ok("更换背景成功");
	}
	
	/**
	 * 修改博客中个人信息
	 * @param request
	 * @param blogRemark
	 * @return
	 */
	@PostMapping("/updateInfo")
	@CheckSession
	public R updateInfo(HttpServletRequest request, String blogRemark) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
		}
		blogRemark = blogRemark.trim();
		if (StrUtils.isEmpty(blogRemark)) {
			return R.error("描述不可为空");
		}
		if (blogRemark.length() > 200) {
			return R.error("不得超过100个字符");
		}
		TevglTraineeInfo t = new TevglTraineeInfo();
		t.setTraineeId(traineeInfo.getTraineeId());
		t.setBlogRemark(blogRemark);
		tevglTraineeInfoService.update(t);
		return R.ok();
	}
	
}
