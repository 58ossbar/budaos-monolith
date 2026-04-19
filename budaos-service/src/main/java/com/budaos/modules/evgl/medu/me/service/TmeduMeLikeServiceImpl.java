package com.budaos.modules.evgl.medu.me.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.GlobalLike;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstorming;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstormingTraineeAnswer;
import com.budaos.modules.evgl.activity.persistence.TevglActivityBrainstormingMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivityBrainstormingTraineeAnswerMapper;
import com.budaos.modules.evgl.forum.domain.TevglForumBlogPost;
import com.budaos.modules.evgl.forum.domain.TevglForumCommentInfo;
import com.budaos.modules.evgl.forum.persistence.TevglForumBlogPostMapper;
import com.budaos.modules.evgl.forum.persistence.TevglForumCommentInfoMapper;
import com.budaos.modules.evgl.medu.me.api.TmeduMeLikeService;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeLike;
import com.budaos.modules.evgl.medu.me.persistence.TmeduMeLikeMapper;
import com.budaos.modules.im.domain.TimGroupMsg;
import com.budaos.modules.im.persistence.TimGroupMsgMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> Title: 点赞表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TmeduMeLikeServiceImpl implements TmeduMeLikeService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TmeduMeLikeServiceImpl.class);
	@Autowired
	private TmeduMeLikeMapper tmeduMeLikeMapper;
	@Autowired
	private TevglActivityBrainstormingTraineeAnswerMapper tevglActivityBrainstormingTraineeAnswerMapper;
	@Autowired
	private TevglActivityBrainstormingMapper tevglActivityBrainstormingMapper;
	@Autowired
	private TimGroupMsgMapper timGroupMsgMapper;
	@Autowired
	private TevglForumBlogPostMapper tevglForumBlogPostMapper;
	@Autowired
	private TevglForumCommentInfoMapper tevglForumCommentInfoMapper;
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
		List<TmeduMeLike> tmeduMeLikeList = tmeduMeLikeMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tmeduMeLikeList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tmeduMeLikeList = tmeduMeLikeMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tmeduMeLikeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tmeduMeLike
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TmeduMeLike tmeduMeLike) throws BudaosException {
		tmeduMeLike.setLikeId(Identities.uuid());
		ValidatorUtils.check(tmeduMeLike);
		tmeduMeLikeMapper.insert(tmeduMeLike);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tmeduMeLike
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TmeduMeLike tmeduMeLike) throws BudaosException {
	    //ValidatorUtils.check(tmeduMeLike);
		tmeduMeLikeMapper.update(tmeduMeLike);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tmeduMeLikeMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tmeduMeLikeMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tmeduMeLikeMapper.selectObjectById(id));
	}

	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	@Override
	public List<TmeduMeLike> selectListByMap(Map<String, Object> map) {
		return tmeduMeLikeMapper.selectListByMap(map);
	}

	/**
	 * 别人给我点的赞
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryPeopleGiveMeLikes( Map<String, Object> params, String loginUserId) {
		// 构建查询条件对象Query
		params.put("targetTraineeId", loginUserId);
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String,Object>> list = tmeduMeLikeMapper.queryPeopleGiveMeLikes(query);
		// 更新点赞记录为已读
		List<Object> likeIds = list.stream().filter(a -> a.get("readState").equals("N"))
			.map(a -> a.get("likeId")).collect(Collectors.toList());
		if (likeIds != null && likeIds.size() > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("likeIds", likeIds);
			map.put("readState", "Y");
			tmeduMeLikeMapper.updateReadState(map);	
		}
		// 处理不同情况
		list.stream().forEach(info -> {
			// 头像处理
			info.put("traineePic", uploadPathUtils.stitchingPath(info.get("traineePic"), "16"));
			info.put("fromTraineePic", uploadPathUtils.stitchingPath(info.get("fromTraineePic"), "16"));
			Object targetId = info.get("targetId");
			// 来自
			String fromTitle = "";
			String content = "";
			// 不同点赞类型
			if (GlobalLike.LIKE_1_SUBJECT.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_2_VIDEO.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_3_NEWS.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_4_DOCS.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_5_DOCS_OPEN.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_6_QUESTION.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_7_BLOG.equals(info.get("likeType"))) {
				TevglForumBlogPost blogPost = tevglForumBlogPostMapper.selectObjectById(targetId);
				if (blogPost != null) {
					fromTitle = "[博客] " + blogPost.getPostTitle();  // 博客标题
					content = blogPost.getPostContent(); 			// 博客内容
				}
			} else if (GlobalLike.LIKE_8_POST.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_10_SUBJECT_LIVE.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_11_CLASSROOM.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_12_ACTIVITY_VOTE_QUESTIONNAIRE.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_13_ACTIVITY_BRAINSTORMING.equals(info.get("likeType"))) {
				
			} else if (GlobalLike.LIKE_14_ACTIVITY_BRAINSTORMING_TRAINEE_ANSWER.equals(info.get("likeType"))) {
				// 找到自己头脑风暴提交的作答内容
				TevglActivityBrainstormingTraineeAnswer traineeAnswer = tevglActivityBrainstormingTraineeAnswerMapper.selectObjectById(targetId);
				if (traineeAnswer != null) {
					TevglActivityBrainstorming brainstormingActivity = tevglActivityBrainstormingMapper.selectObjectById(traineeAnswer.getActivityId());
					if (brainstormingActivity != null) {
						fromTitle = "[活动 答疑讨论]" + brainstormingActivity.getActivityTitle();
					}
					content = traineeAnswer.getContent();
				}
			} else if (GlobalLike.LIKE_15_ACTIVITY_ANSWER_DISCUSS_TRAINEE_ANSWER.equals(info.get("likeType"))) {
				// 找到自己发送的消息
				fromTitle = "[活动 答疑讨论]";
				TimGroupMsg msg = timGroupMsgMapper.selectObjectById(targetId);
				if (msg != null) {
					content = msg.getMsgContent();
				}
			}else if (GlobalLike.LIKE_16_COMMENT_LIKE.equals(info.get("likeType"))) {
				TevglForumCommentInfo commentInfo = tevglForumCommentInfoMapper.selectObjectById(targetId);
				if (commentInfo != null) {
					fromTitle = "[博客评论] " + commentInfo.getCiContent();  // 评论内容
				}
			}
			info.put("fromTitle", fromTitle);
			info.put("content", content);
		});
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
}
