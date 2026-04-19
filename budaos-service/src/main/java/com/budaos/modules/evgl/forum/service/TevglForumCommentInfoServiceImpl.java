package com.budaos.modules.evgl.forum.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalLike;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.forum.api.TevglForumCommentInfoService;
import com.budaos.modules.evgl.forum.domain.TevglForumBlogPost;
import com.budaos.modules.evgl.forum.domain.TevglForumCommentInfo;
import com.budaos.modules.evgl.forum.persistence.TevglForumBlogPostMapper;
import com.budaos.modules.evgl.forum.persistence.TevglForumCommentInfoMapper;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeLike;
import com.budaos.modules.evgl.medu.me.persistence.TmeduMeLikeMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglForumCommentInfoServiceImpl implements TevglForumCommentInfoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglForumCommentInfoServiceImpl.class);
	@Autowired
	private TevglForumCommentInfoMapper tevglForumCommentInfoMapper;
	@Autowired
	private TevglForumBlogPostMapper tevglForumBlogPostMapper;
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	@Autowired
	private TmeduMeLikeMapper tmeduMeLikeMapper;
	
	@Autowired
	private UploadPathUtils uploadPathUtils;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglForumCommentInfo> tevglForumCommentInfoList = tevglForumCommentInfoMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglForumCommentInfoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglForumCommentInfoList = tevglForumCommentInfoMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglForumCommentInfoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglForumCommentInfo
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglForumCommentInfo tevglForumCommentInfo) throws BudaosException {
		tevglForumCommentInfo.setCiId(Identities.uuid());
		ValidatorUtils.check(tevglForumCommentInfo);
		tevglForumCommentInfoMapper.insert(tevglForumCommentInfo);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglForumCommentInfo
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglForumCommentInfo tevglForumCommentInfo) throws BudaosException {
	    ValidatorUtils.check(tevglForumCommentInfo);
		tevglForumCommentInfoMapper.update(tevglForumCommentInfo);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglForumCommentInfoMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglForumCommentInfoMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglForumCommentInfoMapper.selectObjectById(id));
	}
	
	/**
	 * 查询博客评论
	 * @param postId
	 * @param pageNum
	 * @param pageSize
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryBlogCommentList(String postId, Integer pageNum, Integer pageSize, String loginUserId) {
		if (StrUtils.isEmpty(postId)) {
			return R.error("必传参数为空");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("state", "Y");
		params.put("parentId", postId);
		params.put("sidx", "ci_time");
		params.put("order", "asc");
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String,Object>> list = tevglForumCommentInfoMapper.queryBlogCommentList(query);
		if (list == null || list.size() == 0) {
			PageUtils pageUtil = new PageUtils(new ArrayList<>(), query.getPage(), query.getLimit());
			return R.ok().put(Constant.R_DATA, pageUtil);
		}
		// 取出所有的人
		params.clear();
		params.put("targetId", postId);
		List<Map<String,Object>> traineeList = tevglForumCommentInfoMapper.queryTraineeList(params);
		traineeList.stream().forEach(traineeInfo -> {
			traineeInfo.put("trainee_pic", uploadPathUtils.stitchingPath(traineeInfo.get("trainee_pic"), "16"));
		});
		
		list.stream().forEach(commentInfo -> {
			// 实例化回复的集合
			List<Map<String, Object>> replys = new ArrayList<>();
			// 构建评论与回复的信息
			buildReplyComment(loginUserId, commentInfo, replys, params, traineeList);
			// 排序
			List<Map<String, Object>> collect = replys.stream().sorted((h1, h2) -> h1.get("ci_time").toString().compareTo(h2.get("ci_time").toString())).collect(Collectors.toList());
			commentInfo.put("replyCommentList", collect);
			// 标识当前登录人是不是该评论的评论人，只有评论人才有删除按钮
			if (StrUtils.isNotEmpty(loginUserId) && loginUserId.equals(commentInfo.get("cust_id"))) {
				commentInfo.put("isCreator", true);
			}else {
				commentInfo.put("isCreator", false);
			}
		});
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 构建评论与回复之间的关系
	 * @param tepCommentInfo
	 * @param replys
	 * @param map
	 * @param traineeList
	 */
	private void buildReplyComment(String loginUserId, Map<String, Object> tepCommentInfo, List<Map<String, Object>> replys, Map<String, Object> map, List<Map<String,Object>> traineeList) {
		map.clear();
		map.put("traineeId", loginUserId);
		map.put("targetId", tepCommentInfo.get("ci_id"));
		map.put("likeType", GlobalLike.LIKE_16_COMMENT_LIKE);
		List<TmeduMeLike> commentLikes = tmeduMeLikeMapper.selectListByMap(map);
		if (commentLikes != null && commentLikes.size() > 0) {
			tepCommentInfo.put("isCommentLike", true);
		}else {
			tepCommentInfo.put("isCommentLike", false);
		}
		// 获取此评论下所有的回复
		map.clear();
		map.put("state", "Y"); // Y有效N无效
		map.put("parentId", tepCommentInfo.get("ci_id"));
		map.put("sidx", "ci_time");
		map.put("order", "asc");
		List<Map<String,Object>> replyCommentList = tevglForumCommentInfoMapper.queryBlogCommentList(map);
		// 加入集合
		replys.addAll(replyCommentList);
		// 循环
		for (Map<String, Object> info : replyCommentList) {
			// 当前评论的创建人
			String thisCommentInfoTraineeId = tepCommentInfo.get("cust_id").toString();
			List<Map<String, Object>> collect = traineeList.stream().filter(a -> a.get("trainee_id").equals(thisCommentInfoTraineeId)).collect(Collectors.toList());
			if (collect != null && collect.size() > 0) {
				Map<String, Object> toTraineeInfo = collect.get(0);
				info.put("toTraineeInfo", toTraineeInfo);
			}
			// 标识当前登录人是不是该评论的评论人，只有评论人才有删除按钮
			if (loginUserId.equals(info.get("cust_id"))) {
				info.put("isCreator", true);
			}else {
				info.put("isCreator", false);
			}
			// 递归调用
			buildReplyComment(loginUserId, info, replys, map, traineeList);
		}
	}

	/**
	 * 保存评论信息
	 * @param postId 博客
	 * @param parentId 父ID
	 * @param content 评论内容
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R saveCommentInfo(String postId, String parentId, String content, String loginUserId) {
		if (StrUtils.isEmpty(content)) {
			return R.error("内容不能为空");
		}
		content = content.trim();
		if (StrUtils.isEmpty(content)) {
			return R.error("内容不能为空");
		}
		if (content.length() > 3000) {
			return R.error("内容不能超过3000个字符");
		}
		if (StrUtils.isEmpty(postId) || StrUtils.isEmpty(parentId) 
				|| StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglForumBlogPost tevglForumBlogPost = tevglForumBlogPostMapper.selectObjectById(postId);
		if (tevglForumBlogPost == null || !"Y".equals(tevglForumBlogPost.getState())) {
			return R.error("评论未成功，博客已无效，请刷新后重试");
		}
		// 是否允许评论
		if ("N".equals(tevglForumBlogPost.getPostIsReply())) {
			return R.error("此博客已不允许评论");
		}
		// 查询当前登录用户信息
		TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(loginUserId);
		if (traineeInfo == null) {
			return R.error("评论失败");
		}
		// 如果是
		String toTraineeId = "";
		if (postId.equals(parentId)) {
			toTraineeId = tevglForumBlogPost.getTraineeId();
		} else {
			TevglForumCommentInfo commentInfo = tevglForumCommentInfoMapper.selectObjectById(parentId);
			if (commentInfo == null) {
				return R.error("评论失败");
			}
			toTraineeId = commentInfo.getCustId();
		}
		// 实例化并填充数据
		TevglForumCommentInfo t = new TevglForumCommentInfo();
		t.setCiId(Identities.uuid());
		t.setCustId(loginUserId);
		t.setCustHeadimg(traineeInfo.getTraineeHead());
		t.setCustNickname(StrUtils.isEmpty(traineeInfo.getTraineeName()) ? traineeInfo.getNickName() : traineeInfo.getTraineeName());
		t.setParentId(parentId);
		t.setParentType("1");
		t.setCiContent(content);
		t.setCiTime(DateUtils.getNowTimeStamp());
		t.setCiTotal(0);
		t.setCiReport(0);
		t.setCiLike(0);
		t.setState("Y");
		t.setTargetId(postId);
		// 如果评论的博客
		if (postId.equals(parentId)) {
			// 设置主评论的楼层
			Map<String, Object> params = new HashMap<>();
			params.put("state", "1");
			params.put("parentId", postId);
			Integer floor = tevglForumCommentInfoMapper.getLastCiFloor(params);
			t.setCiFloor(floor);
			// 对博客的总留言数进行更新
			TevglForumBlogPost post = new TevglForumBlogPost();
			post.setPostId(postId);
			post.setReplyNum(1);
			tevglForumBlogPostMapper.plusNum(post);
		} else {
			// 所有子评论默认为0层
			t.setCiFloor(0);
			// 对博客的总留言数进行更新
			TevglForumBlogPost post = new TevglForumBlogPost();
			post.setPostId(postId);
			post.setReplyNum(1);
			tevglForumBlogPostMapper.plusNum(post);
			// 调用方法，对评论的留言数进行更新
			updateCiTotal(parentId); 
		}
		tevglForumCommentInfoMapper.insert(t);
		// 修改博客的回复时间
		TevglForumBlogPost blogPost = new TevglForumBlogPost();
		blogPost.setPostId(postId);
		blogPost.setLastReplyTime(DateUtils.getNowTimeStamp());
		tevglForumBlogPostMapper.update(blogPost);
		// TODO 别人评论自己的帖子或评论才去推送消息
		/*if (!toTraineeId.equals(userInfo.getTraineeId())) {
			tepForumMsgService.sendPostReplayMsg(toTraineeId, userInfo.getTraineeId(), tepCommentInfo.getCiId());
		}*/
		// 查询刚刚保存的评论
		Map<String, Object> data = new HashMap<>();
		data.put("ci_id", t.getCiId());
		data.put("cust_id", t.getCustId());
		data.put("trainee_id", t.getCustId());
		data.put("trainee_name", t.getCustNickname());
		data.put("trainee_pic", t.getCustHeadimg());
		return R.ok("评论成功").put(Constant.R_DATA, data);
	}

	/**
	 * 递归更新，评论数量
	 * @param id
	 */
	private void updateCiTotal(String id) {
		TevglForumCommentInfo commentInfo = tevglForumCommentInfoMapper.selectObjectById(id);
		if (commentInfo != null) {
			TevglForumCommentInfo t = new TevglForumCommentInfo();
			t.setCiId(id);
			t.setCiTotal(1);
			tevglForumCommentInfoMapper.plusNum(t);
			id = commentInfo.getParentId();
			updateCiTotal(id); // 递归调用
		}
	}
	
	private void deleteCiTotal(String id) {
		TevglForumCommentInfo commentInfo = tevglForumCommentInfoMapper.selectObjectById(id);
		if (commentInfo != null) {
			TevglForumCommentInfo t = new TevglForumCommentInfo();
			t.setCiId(id);
			t.setCiTotal(-1);
			tevglForumCommentInfoMapper.plusNum(t);
			id = commentInfo.getParentId();
			// 递归调用
			deleteCiTotal(id); 
		}
	}

	/**
	 * 点赞
	 * @author zhouyl加
	 * @date 2021年3月10日
	 * @param ciId 评论id
	 * @param loginUserId
	 * @return
	 */	
	@Override
	public R clickLike(String ciId, String loginUserId) {
		TevglForumCommentInfo commentInfo = tevglForumCommentInfoMapper.selectObjectById(ciId);
		if (commentInfo == null) {
			return R.error("评论已删除，请选择其它需要删除的评论");
		}
		TmeduMeLike commentLike = new TmeduMeLike();
		commentLike.setLikeId(Identities.uuid());
		commentLike.setTraineeId(loginUserId);
		commentLike.setLikeType(GlobalLike.LIKE_16_COMMENT_LIKE);
		commentLike.setTargetId(ciId);
		commentLike.setLikeTime(DateUtils.getNowTimeStamp());
		commentLike.setReadState("Y");
		commentLike.setTargetTraineeId(commentInfo.getCustId());
		
		// 查询数据库中是否有该记录
		Map<String, Object> params = new HashMap<>();
		params.put("traineeId", loginUserId);
		params.put("likeType", GlobalLike.LIKE_16_COMMENT_LIKE);
		params.put("targetId", ciId);
		List<TmeduMeLike> commentList = tmeduMeLikeMapper.selectListByMap(params);
		if (commentList != null && commentList.size() > 0) {
			// 如果当前登录用户是评论的点赞用户则取消点赞
			if (loginUserId.equals(commentList.get(0).getTraineeId())) { 
				for (TmeduMeLike tmeduMeLike : commentList) {
					tmeduMeLikeMapper.delete(tmeduMeLike.getLikeId());
				}
				// 更新评论的点赞数
				TevglForumCommentInfo comment = new TevglForumCommentInfo();
				comment.setCiId(ciId);
				comment.setCiLike(-1);
				tevglForumCommentInfoMapper.plusNum(comment);
				return R.ok("取消评论点赞");
			}else { // 如果不是，则点赞
				tmeduMeLikeMapper.insert(commentLike);
				// 更新评论的点赞数
				TevglForumCommentInfo comment = new TevglForumCommentInfo();
				comment.setCiId(ciId);
				comment.setCiLike(1);
				tevglForumCommentInfoMapper.plusNum(comment);
				return R.ok("成功点赞评论");
			}
		}else {
			tmeduMeLikeMapper.insert(commentLike);
			// 更新评论的点赞数
			TevglForumCommentInfo comment = new TevglForumCommentInfo();
			comment.setCiId(ciId);
			comment.setCiLike(1);
			tevglForumCommentInfoMapper.plusNum(comment);
			return R.ok("成功点赞评论");
		}
	}
	
	/**
	 * 删除回复记录
	 * @author zhouyl加
	 * @date 2021年3月3日
	 * @param postId 博客id
	 * @param ciId 评论id
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteReply(String postId, String ciId, String loginUserId) {
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(ciId)) {
			return R.error("必传参数为空");
		}
		TevglForumBlogPost blogPost = tevglForumBlogPostMapper.selectObjectById(postId);
		if (blogPost == null) {
			return R.error("博客不存在");
		}
		TevglForumCommentInfo commentInfo = tevglForumCommentInfoMapper.selectObjectById(ciId);
		if (commentInfo != null) {
			if (!loginUserId.equals(commentInfo.getCustId())) {
				return R.error("非法操作，不能删除");
			}
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("ciId", ciId);
		params.put("custId", loginUserId);
		List<TevglForumCommentInfo> commentInfos = tevglForumCommentInfoMapper.selectListByMap(params);
		if (commentInfos != null && commentInfos.size() > 0) {
			// 存放子评论的集合
			List<TevglForumCommentInfo> subComments = new ArrayList<>();
			
			List<String> ciIds = commentInfos.stream().map(a -> a.getCiId()).collect(Collectors.toList());
			List<TevglForumCommentInfo> comments = recursion(ciIds, commentInfos, subComments, params);
			
			if (comments != null && comments.size() > 0) {
				comments.addAll(commentInfos);
				// 取出所有子评论的parentId
				List<String> parentIds = comments.stream().map(a -> a.getParentId()).collect(Collectors.toList());
				// 去除重复元素
				for (int i = 0; i < comments.size(); i++) {// 循环list
					for (int j = i + 1; j < comments.size(); j++) {
						if (comments.get(i).equals(comments.get(j))) {
							comments.remove(i);// 删除一样的元素
							i--;
							break;
						}
					}
				}
				// 删除子评论
				for (TevglForumCommentInfo tevglForumCommentInfo : comments) {
					tevglForumCommentInfoMapper.delete(tevglForumCommentInfo.getCiId());
				}
				// 对博客的总留言数进行更新
				TevglForumBlogPost post = new TevglForumBlogPost();
				post.setPostId(postId);
				post.setReplyNum(-comments.size());
				tevglForumBlogPostMapper.plusNum(post);
				
				// 更新评论数
				TevglForumCommentInfo comment = new TevglForumCommentInfo();
				comment.setCiId(commentInfo.getParentId());
				comment.setCiTotal(-comments.size());
				tevglForumCommentInfoMapper.plusNum(comment);
				
				// 通过parentId查找数据库中还存在的记录
				params.clear();
				params.put("ciIds", parentIds);
				List<Map<String, Object>> parentComments = tevglForumCommentInfoMapper.selectListMapByMap(params);
				
				if (parentComments != null) {
					subcomment(parentComments, comments, params);
				}
			} 
		}
		return R.ok("删除成功");
	}

	/**
	 * 查找父评论，更新父评论的评论数
	 * @param parentComments 存放父评论的集合 
	 * @param comments 删除的子评论数量
	 * @param params
	 */
	private void subcomment(List<Map<String, Object>> parentComments, List<TevglForumCommentInfo> comments, Map<String, Object> params) {
		for (Map<String, Object> parent : parentComments) {
			// 查找父评论
			params.clear();
			params.put("ciId", parent.get("parent_id"));
			List<Map<String, Object>> parentCommentList = tevglForumCommentInfoMapper.selectListMapByMap(params);
			if (parentCommentList != null && parentCommentList.size() > 0) {
				// 更新评论数
				TevglForumCommentInfo comment = new TevglForumCommentInfo();
				comment.setCiId(parentCommentList.get(0).get("ci_id").toString());
				comment.setCiTotal(-comments.size());
				tevglForumCommentInfoMapper.plusNum(comment);
				// 递归调用
				subcomment(parentCommentList, comments, params);
			}
		}
	}

	/**
	 * 递归子评论
	 * @param ciIds
	 * @param commentInfos
	 * @param subComments
	 * @param params
	 * @return
	 */
	private List<TevglForumCommentInfo> recursion(List<String> ciIds, List<TevglForumCommentInfo> commentInfos,
			List<TevglForumCommentInfo> subComments, Map<String, Object> params) {
		params.clear();
		params.put("parentIds", ciIds);
		List<TevglForumCommentInfo> commentList = tevglForumCommentInfoMapper.selectListByMap(params);
		if (commentList == null || commentList.size() == 0) {
			return commentInfos;
		}
		subComments.addAll(commentList);
		List<String> ciIdList = commentList.stream().map(a -> a.getCiId()).collect(Collectors.toList());
		recursion(ciIdList, commentInfos, subComments, params);
		return subComments;
	}
}
