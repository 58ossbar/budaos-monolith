package com.budaos.modules.evgl.forum.service;

import com.alibaba.druid.util.StringUtils;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.*;
import com.budaos.modules.common.enums.CommonEnum;
import com.budaos.modules.evgl.forum.api.TevglForumBlogPostService;
import com.budaos.modules.evgl.forum.domain.TevglForumAttention;
import com.budaos.modules.evgl.forum.domain.TevglForumBlogPost;
import com.budaos.modules.evgl.forum.persistence.TevglForumAttentionMapper;
import com.budaos.modules.evgl.forum.persistence.TevglForumBlogPostMapper;
import com.budaos.modules.evgl.forum.persistence.TevglForumCommentInfoMapper;
import com.budaos.modules.evgl.forum.persistence.TevglForumFriendMapper;
import com.budaos.modules.evgl.forum.vo.TevglForumBlogPostVo;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeFavority;
import com.budaos.modules.evgl.medu.me.domain.TmeduMeLike;
import com.budaos.modules.evgl.medu.me.persistence.TmeduMeFavorityMapper;
import com.budaos.modules.evgl.medu.me.persistence.TmeduMeLikeMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.tch.persistence.TevglTchTeacherMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeEmpiricalValueLog;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeEmpiricalValueLogMapper;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class TevglForumBlogPostServiceImpl implements TevglForumBlogPostService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglForumBlogPostServiceImpl.class);
	@Autowired
	private TevglForumBlogPostMapper tevglForumBlogPostMapper;
	@Autowired
	private TmeduMeFavorityMapper tmeduMeFavorityMapper;
	@Autowired
	private TmeduMeLikeMapper tmeduMeLikeMapper;
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	@Autowired
	private TevglForumFriendMapper tevglForumFriendMapper;
	@Autowired
	private TevglForumAttentionMapper tevglForumAttentionMapper;
	@Autowired
	private TevglForumCommentInfoMapper tevglForumCommentInfoMapper;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglTchTeacherMapper tevglTchTeacherMapper;
	@Autowired
	private TevglTraineeEmpiricalValueLogMapper tevglTraineeEmpiricalValueLogMapper;
	
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		query.put("state", "Y");
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglForumBlogPostVo> tevglForumBlogPostList = tevglForumBlogPostMapper.selectVoListByMapForMgr(query);
		tevglForumBlogPostList.stream().forEach(item -> {
			item.setTraineePic(uploadPathUtils.stitchingPath(item.getTraineePic(), "16"));
		});
		PageUtils pageUtil = new PageUtils(tevglForumBlogPostList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglForumBlogPostList = tevglForumBlogPostMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglForumBlogPostList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglForumBlogPost
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	@Override
	public R save( TevglForumBlogPost tevglForumBlogPost) throws BudaosException {
		// 合法性校验
		R r = checkIsPass(tevglForumBlogPost);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 处理标签
		tevglForumBlogPost.setPostLable(handlePostLabel(tevglForumBlogPost.getDictCode()));
		// 填充其它信息
		tevglForumBlogPost.setPostId(Identities.uuid());
		tevglForumBlogPost.setPostTime(DateUtils.getNowTimeStamp());
		tevglForumBlogPost.setPostIsReply("Y");
		tevglForumBlogPost.setPostIsRecommend("N");
		tevglForumBlogPost.setPostIsChoice("N");
		tevglForumBlogPost.setPostIsStick("N");
		tevglForumBlogPost.setPostIsResolve("Y");
		tevglForumBlogPost.setViewNum(0);
		tevglForumBlogPost.setStoreNum(0);
		tevglForumBlogPost.setReplyNum(0);
		tevglForumBlogPost.setLikeNum(0);
		tevglForumBlogPost.setReportNum(0);
		tevglForumBlogPost.setState("Y");
		ValidatorUtils.check(tevglForumBlogPost);
		tevglForumBlogPostMapper.insert(tevglForumBlogPost);
		// 更新博主的博客数
		TevglTraineeInfo traineeInfo = new TevglTraineeInfo();
		traineeInfo.setTraineeId(tevglForumBlogPost.getTraineeId());
		traineeInfo.setBlogsNum(1);
		tevglTraineeInfoMapper.plusNum(traineeInfo);
		// 如果是老师
		TevglTchTeacher tevglTchTeacher = tevglTchTeacherMapper.selectObjectByTraineeId(tevglForumBlogPost.getTraineeId());
		if (tevglTchTeacher != null && CommonEnum.STATE_YES.getCode().equals(tevglTchTeacher.getState())) {
			// 记录老师获取的经验值
			TevglTraineeEmpiricalValueLog t = new TevglTraineeEmpiricalValueLog();
			t.setEvId(Identities.uuid());
			t.setType(GlobalEmpiricalValueGetType.TEACHER_BLOGGING_16);
			t.setTraineeId(tevglForumBlogPost.getTraineeId());
			t.setEmpiricalValue(findEmpiricalValue());
			t.setState("Y");
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setMsg("写博客，获得" + t.getEmpiricalValue() + "经验值");
			tevglTraineeEmpiricalValueLogMapper.insert(t);
		}
		return R.ok("保存成功").put(Constant.R_DATA, tevglForumBlogPost.getPostId());
	}
	
	/**
	 * 从字典 teach_exp_type 获取老师获取经验值的方式
	 * @return
	 */
	private Integer findEmpiricalValue() {
		List<TsysDict> list = dictService.getTsysDictListByType("teach_exp_type");
		if (list != null && list.size() > 0) {
			List<TsysDict> collect = list.stream().filter(a -> a.getDictCode().equals(GlobalEmpiricalValueGetType.TEACHER_BLOGGING_16)).collect(Collectors.toList());
			if (collect != null && collect.size() > 0) {
				return Integer.valueOf(collect.get(0).getDictValue());
			}
		}
		return 5;
	}
	
	private R checkIsPass(TevglForumBlogPost tevglForumBlogPost) {
		// 校验前端传入的标签字典值（多个会用英文逗号分隔），只允许为正整数
		String dictCodes = tevglForumBlogPost.getDictCode();
		if (StrUtils.isNotEmpty(dictCodes)) {
			String[] split = dictCodes.split(",");
			for (int i = 0; i < split.length; i++) {
				if (!isNumber(split[i])) {
					return R.error("标签字典值不合法，请联系管理员");
				}
			}
			if (split.length > 5) {
				return R.error("最多只能选择5个标签");
			}
		}
		return R.ok("校验通过");
	}
	
	/**
	 * 处理标签的显示值
	 * @param dictCodes
	 * @return
	 */
	private String handlePostLabel(String dictCodes) {
		if (StrUtils.isEmpty(dictCodes)) {
			return "";
		}
		String str = "";
		List<Map<String, Object>> dictList = dictService.getDictList("subjectType");
		if (dictList != null && dictList.size() > 0) {
			String[] split = dictCodes.split(",");
			for (String val : split) {
				List<Map<String, Object>> list = dictList.stream().filter(a -> a.get("dictCode").equals(val)).collect(Collectors.toList());
				if (list != null && list.size() > 0) {
					str += list.get(0).get("dictValue") + ",";
				}
			}
		}
		if (StrUtils.isNotEmpty(str)) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	/**
	 * 判断字符是不是数字
	 * @param str
	 * @return
	 */
	private boolean isNumber(String str) {
		Pattern p = Pattern.compile("^[1-9]\\d*$");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	/**
	 * 修改
	 * @param tevglForumBlogPost
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	@Override
	public R update(TevglForumBlogPost tevglForumBlogPost) throws BudaosException {
		TevglForumBlogPost existedPost = tevglForumBlogPostMapper.selectObjectById(tevglForumBlogPost.getPostId());
		if (existedPost == null) {
			return R.error("保存失败");
		}
		if (!existedPost.getTraineeId().equals(tevglForumBlogPost.getTraineeId())) {
			return R.error("非法操作！");
		}
		// 合法性校验
		R r = checkIsPass(tevglForumBlogPost);
		if (!r.get("code").equals(0)) {
			return r;
		}
		// 处理标签
		tevglForumBlogPost.setPostLable(handlePostLabel(tevglForumBlogPost.getDictCode()));
		// 填充其它信息
		tevglForumBlogPost.setPostTime(existedPost.getPostTime());
		tevglForumBlogPost.setPostIsReply(existedPost.getPostIsReply());
		tevglForumBlogPost.setPostIsRecommend(existedPost.getPostIsRecommend());
		tevglForumBlogPost.setPostIsChoice(existedPost.getPostIsChoice());
		tevglForumBlogPost.setPostIsStick(existedPost.getPostIsStick());
		tevglForumBlogPost.setPostIsResolve(existedPost.getPostIsResolve());
		tevglForumBlogPost.setState("Y");
		tevglForumBlogPost.setUpdateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglForumBlogPost);
		tevglForumBlogPostMapper.update(tevglForumBlogPost);
		return R.ok("保存成功");
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglForumBlogPostMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglForumBlogPostMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglForumBlogPostMapper.selectObjectById(id));
	}

	/**
	 * 博客列表
	 * @param params 诸多查询条件
	 * @param loginUserId 当前登录用户
	 * @param filterType 为空查全部，为1查个人博客，为2查收藏博客，为3查关注博主的博客
	 * @return
	 */

	@Override
	public R queryBlogList( Map<String, Object> params, String loginUserId, String filterType) {
		// 处理排序字段
		params.put("postTitle", params.get("title"));
        handleSortField(params);
        // 处理博客背景图和描述
        Map<String, Object> otherInfo = handlePic(params.get("traineeId"), loginUserId);
        // 最终返回数据
		List<Map<String, Object>> blogPostList = new ArrayList<Map<String,Object>>();
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		switch (filterType) {
	        // 为空查询全部博客信息
	        case "":
	            blogPostList = tevglForumBlogPostMapper.selectSimpleListMap(params);
	            break;
	        // 查询个人博客
	        case "1":
	            params.put("traineeId", StrUtils.isNull(params.get("traineeId")) ? loginUserId : params.get("traineeId"));
	            blogPostList = tevglForumBlogPostMapper.selectSimpleListMap(params);
	            break;
	        // 查询收藏博客
	        case "2":
	            params.put("traineeId", loginUserId);
	            params.put("favorityType", "7");
	            blogPostList = tevglForumBlogPostMapper.collectBlog(params);
	            break;
	        // 查询关注的博主的博客
	        case "3":
	            // 先查找当前登录用户关注过的博主
	            params.put("followId", loginUserId);
	            List<String> traineeIds = tevglForumAttentionMapper.queryFollowTraineeIdList(params);
	            // 有关注过，才去查
	            if (traineeIds.size() > 0) {
	                params.put("traineeIds", traineeIds);
	                blogPostList = tevglForumBlogPostMapper.selectSimpleListMap(params);
	            }
	            break;
	        default:
	            break;
	    }
		params.clear();
		params.put("traineeId", loginUserId);
		params.put("favorityType", GlobalFavority.FAVORITY_7_BLOG);
		List<TmeduMeFavority> favorityList = tmeduMeFavorityMapper.selectListByMap(params);
		// 处理部分数据
		blogPostList.stream().forEach(blog -> {
			// 处理是否收藏过
			if (favorityList.stream().anyMatch(a -> a.getTargetId().equals(blog.get("postId")))) {
				blog.put("isCollected", true);
			} else {
				blog.put("isCollected", false);
			}
			// 处理时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			try {
				date = sdf.parse(blog.get("postTime").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			blog.put("postTime", sdf.format(date));
			
			// 处理最新回复记录
			params.clear();
			params.put("targetId", blog.get("postId"));
			params.put("sidx", "t1.ci_time");
			params.put("order", "desc");
			List<Map<String, Object>> commentList = tevglForumCommentInfoMapper.selectCommentList(params);
			if (commentList != null && commentList.size() > 0) {
				for (Map<String, Object> comment : commentList) {
					blog.put("replyContent", comment.get("ci_content"));  // 回复内容
					blog.put("ciTime", comment.get("ci_time"));
					blog.put("replier", comment.get("cust_nickname"));  // 回复人
				}
			}
		});
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(blogPostList);
		return R.ok().put(Constant.R_DATA, pageInfo)
                .put("backgroundImage", otherInfo.get("backgroundImage"))
                .put("blogRemark", otherInfo.get("blogRemark"));
	}
	
	/**
     * 处理背景图和备注
     * @param traineeId 目标用户
     * @param loginUserId 当前登陆用户
     * @return
     */
    private Map<String, Object> handlePic(Object traineeId, String loginUserId) {
        Map<String, Object> info = new HashMap<>();
        String backgroundImage = null; // 博客背景图
        String blogRemark = null;
        if (!StrUtils.isNull(traineeId)) {
            TevglTraineeInfo tevglTraineeInfo = tevglTraineeInfoMapper.selectObjectById(traineeId);
            if (tevglTraineeInfo != null) {
            	backgroundImage = uploadPathUtils.stitchingPath(tevglTraineeInfo.getBlogBgPic(), "25");
                blogRemark = tevglTraineeInfo.getBlogRemark();
            }
        } else {
            if (loginUserId != null) {
                TevglTraineeInfo tevglTraineeInfo = tevglTraineeInfoMapper.selectObjectById(loginUserId);
                if (tevglTraineeInfo != null) {
                	backgroundImage = uploadPathUtils.stitchingPath(tevglTraineeInfo.getBlogBgPic(), "25");
                    blogRemark = tevglTraineeInfo.getBlogRemark();
                }
            }
        }
        info.put("backgroundImage", backgroundImage);
        info.put("blogRemark", blogRemark);
        return info;
    }
	
	/**
	 * 获取排序字段
	 * @param params
	 * @return
	 */
    private String handleSortField(Map<String, Object> params){
        String sortField = StrUtils.isNull(params.get("sidx")) ? "1" : params.get("sidx").toString();
        String sidx = "t1.last_reply_time";
        switch (sortField) {
            case "0":
                //sidx = "t1.last_reply_time";
                break;
            case "1":
                sidx = "t1.post_time";
                break;
            case "2":
                sidx = "t1.like_num";
                break;
            default:
                break;
        }
        // 排序方式
        params.put("sidx", sidx);
        params.put("order", "desc");
        if ("0".equals(sidx)) {
            sidx = "t1.post_time";
            params.put("order", "desc, t1.last_reply_time desc, t1.update_time desc");
        }
        return sidx;
    }

	/**
	 * 删除博客
	 * @param postId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteBlog(String postId, String loginUserId) {
		// 将要删除的博客id存入集合中
		List<String> postIds = Arrays.asList(postId.split(","));
		if (postIds != null && postIds.size() > 0) {
			for (String blogId : postIds) {
				TevglForumBlogPost blogPost = tevglForumBlogPostMapper.selectObjectById(blogId);
				if (blogPost == null) {
					return R.error("博客不存在，删除无效");
				}
				if (!loginUserId.equals(blogPost.getTraineeId())) {
					return R.error("非法操作，不允许删除");
				}
			}
			
			// 将集合转成数组，最后执行删除操作
			String[] blogPostIds = postIds.stream().toArray(String[]::new);
			tevglForumBlogPostMapper.deleteBatch(blogPostIds);
			// 更新博主的博客数
			TevglTraineeInfo traineeInfo = new TevglTraineeInfo();
			traineeInfo.setTraineeId(loginUserId);
			traineeInfo.setBlogsNum(-postIds.size());
			tevglTraineeInfoMapper.plusNum(traineeInfo);
		}
		
		
		return R.ok("成功删除博客");
	}

	/**
	 * 收藏博客
	 * @author zhouyl加
	 * @date 2021年2月27日
	 * @param postId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R collect(String postId, String loginUserId) {
		TevglForumBlogPost blogPost = tevglForumBlogPostMapper.selectObjectById(postId);
		if (blogPost == null) {
			return R.error("请选择你要收藏博客的记录");
		}
		// 填充信息
		TmeduMeFavority favority = new TmeduMeFavority();
		favority.setFavorityId(Identities.uuid());
		favority.setTraineeId(loginUserId);
		favority.setClassId(null);
		favority.setFavorityTime(DateUtils.getNowTimeStamp());
		favority.setMajorId(null);
		favority.setFavorityType(GlobalFavority.FAVORITY_7_BLOG);
		favority.setTargetId(postId);
		
		Map<String, Object> params = new HashMap<>();
		params.put("targetId", postId);
		params.put("traineeId", loginUserId);
		params.put("favorityType", GlobalFavority.FAVORITY_7_BLOG);
		// 查询数据库中是否存在该记录,如果存在就删除,如果不存在就添加
		List<TmeduMeFavority> medFavorities = tmeduMeFavorityMapper.selectListByMap(params);
		if (medFavorities != null && medFavorities.size() > 0) {
			List<String> favorityIds = medFavorities.stream().map(a -> a.getFavorityId()).collect(Collectors.toList());
			String[] deleteFavoritys = favorityIds.stream().toArray(String[]::new);
			tmeduMeFavorityMapper.deleteBatch(deleteFavoritys);
			// 更新博主博客的收藏数
			TevglForumBlogPost forumBlogPost = new TevglForumBlogPost();
			forumBlogPost.setPostId(postId);
			forumBlogPost.setStoreNum(-1);
			tevglForumBlogPostMapper.plusNum(forumBlogPost);
			return R.ok("取消收藏");
		}
		tmeduMeFavorityMapper.insert(favority);
		// 更新博主博客的收藏数
		TevglForumBlogPost forumBlogPost = new TevglForumBlogPost();
		forumBlogPost.setPostId(postId);
		forumBlogPost.setStoreNum(1);
		tevglForumBlogPostMapper.plusNum(forumBlogPost);
		
		return R.ok("收藏成功").put(Constant.R_DATA, favority);
	}

	/**
	 * 点赞
	 * @param postId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R like(String postId, String loginUserId) {
		if (StrUtils.isEmpty(postId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		// 填充信息
		TevglForumBlogPost forumBlogPost = tevglForumBlogPostMapper.selectObjectById(postId);
		TmeduMeLike thumbsUp = new TmeduMeLike();
		thumbsUp.setLikeId(Identities.uuid());
		thumbsUp.setTraineeId(loginUserId);  // 点赞人
		thumbsUp.setLikeType(GlobalLike.LIKE_7_BLOG);  // 点赞类型
		thumbsUp.setTargetId(postId);
		thumbsUp.setLikeTime(DateUtils.getNowTimeStamp());
		thumbsUp.setReadState("Y");  // 是否已读
		thumbsUp.setTargetTraineeId(forumBlogPost.getTraineeId()); // 被点赞记录的所属人
		
		// 查找点赞表中是否有这条记录,如果有,则删除,并更新博客的点赞数
		Map<String, Object> params = new HashMap<>();
		params.put("targetId", postId);
		params.put("traineeId", loginUserId);
		List<TmeduMeLike> meLikes = tmeduMeLikeMapper.selectListByMap(params);
		if (meLikes != null && meLikes.size() > 0) {
			List<String> likeIds = meLikes.stream().map(a -> a.getLikeId()).collect(Collectors.toList());
			String[] deletelikes = likeIds.stream().toArray(String[]::new);
			tmeduMeLikeMapper.deleteBatch(deletelikes);
			// 更新博主博客的点赞数
			TevglForumBlogPost t = new TevglForumBlogPost();
			t.setPostId(postId);
			t.setLikeNum(-1);
			tevglForumBlogPostMapper.plusNum(t);
			return R.ok("取消点赞");
		}
		// 入库
		tmeduMeLikeMapper.insert(thumbsUp);
		// 更新博客的点赞数
		TevglForumBlogPost blogPost = new TevglForumBlogPost();
		blogPost.setPostId(postId);
		blogPost.setLikeNum(1);
		tevglForumBlogPostMapper.plusNum(blogPost);
		return R.ok("点赞成功");
	}

	/**
	 * 查找推荐标签
	 * @author zhouyl加
	 * @date 2021年3月2日
	 */
	@Override
	public R recommendedLabel() {
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> recommendedLabel = tevglForumBlogPostMapper.recommendedLabel(params);
		Map<String, Object> data = new HashMap<>();
		if (recommendedLabel != null && recommendedLabel.size() > 0) {
			for (int i = 0; i < recommendedLabel.size(); i++) {
				// 取出所有的标签，多个标签以逗号分隔
				String[] labels = (recommendedLabel.get(i).get("postLable").toString()).split(",");
				if (labels.length > 1) { // 多个标签的情况
					for (String label : labels) {
						// 如果map里边没有标签则不统计标签的次数，默认为1，如果有同一标签则累加标签出现的次数
						if (StrUtils.isNull(data.get(label))) {
							data.put(label, 1);
						} else {
							int num = Integer.valueOf(data.get(label).toString());
							data.put(label, ++num);
						}
					}
				} else { // 单个标签的情况
					String label = recommendedLabel.get(i).get("postLable").toString();
					if (StrUtils.isNull(data.get(label))) {
						data.put(label, 1);
					} else {
						int num = Integer.valueOf(data.get(label).toString());
						data.put(label, ++num);
					}
				}
			}
		}
		return R.ok().put(Constant.R_DATA, getSortedHashtableByValue(data));
	}
	
	/**
	 * 响应时间：112.64s
	 * 将map集合的元素根据值降序排
	 * @param h
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map.Entry[] getSortedHashtableByValue(Map h) {
		Set set = h.entrySet();
		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		Arrays.sort(entries, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Double key1 = Double.valueOf(((Map.Entry) arg0).getValue().toString());
				Double key2 = Double.valueOf(((Map.Entry) arg1).getValue().toString());
				if (key1 < key2)
					return 1; // Neither val is NaN, thisVal is smaller
				if (key1 > key2)
					return -1; // Neither val is NaN, thisVal is larger
				long thisBits = Double.doubleToLongBits(key1);
				long anotherBits = Double.doubleToLongBits(key2);
				return (thisBits == anotherBits ? 0 : // Values are equal
				(thisBits < anotherBits ? 1 : // (-0.0, 0.0) or (!NaN, NaN)
				-1)); // (0.0, -0.0) or (NaN, !NaN)
			}
		});
		return entries;
	}
	
	/**
	 * 查找热门博主
	 * @author zhouyl加
	 * @date 2021年3月2日
	 */
	@Override
	public R popularBloggers() {
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> popularBloggers = tevglTraineeInfoMapper.popularBloggers(params);
		if (popularBloggers != null && popularBloggers.size() > 0) {
			popularBloggers.stream().forEach(blog -> {
				// 处理博主头像
				if (blog.get("blogHead") == null) { 
					blog.put("blogHead", "/uploads/defaulthead.png");
				}
			});
		}
		
		return R.ok().put(Constant.R_DATA, popularBloggers);
	}
	
	/**
	 * 查找友情社区
	 * @author zhouyl加
	 * @date 2021年3月2日
	 */
	@Override
	public R friendshipCommunity() {
		Map<String, Object> params = new HashMap<>();
		List<Map<String, Object>> friendshipCommunity = tevglForumFriendMapper.friendshipCommunity(params);
		friendshipCommunity.stream().forEach(friend -> {
			// 头像处理
			friend.put("friendLogo", uploadPathUtils.stitchingPath(friend.get("friendLogo"), "24"));
		});
		return R.ok().put(Constant.R_DATA, friendshipCommunity);
	}
	
	/**
	 * 根据博客id查找博客详情
	 * @author zhouyl加
	 * @date 2021年3月3日
	 * @param postId 
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R viewBlogDetails(String postId, String loginUserId) {
		if (StrUtils.isEmpty(postId)) {
			return R.error("必传参数为空");
		}
		TevglForumBlogPost blogPost = tevglForumBlogPostMapper.selectObjectById(postId);
		if (blogPost == null) {
			return R.error("该博客已找不到");
		}
		List<TmeduMeLike> meLikes = new ArrayList<>();
		List<TmeduMeFavority> meFavorities = new ArrayList<>();
		List<TevglForumAttention> forumAttentions = new ArrayList<>();
		Map<String, Object> params = new HashMap<>();
		// 查询当前登录人是否点赞过该博客
		if (StrUtils.isNotEmpty(loginUserId)) {
			params.put("targetId", postId);
			params.put("traineeId", loginUserId);
			meLikes = tmeduMeLikeMapper.selectListByMap(params);
		}
		// 查询当前登录人是否收藏过该博客
		if (StrUtils.isNotEmpty(loginUserId)) {
			params.clear();
			params.put("traineeId", loginUserId);
			params.put("favorityType", GlobalFavority.FAVORITY_7_BLOG);
			meFavorities = tmeduMeFavorityMapper.selectListByMap(params);
		}
		// 先根据博客id查询博主，然后查询当前登录人是否关注过该博主
		if (StrUtils.isNotEmpty(loginUserId)) {
			params.clear();
			params.put("traineeId", blogPost.getTraineeId());
			params.put("followId", loginUserId);
			forumAttentions = tevglForumAttentionMapper.selectListByMap(params);
		}
		params.clear();
		params.put("postId", postId);
		List<Map<String,Object>> blogDetails = tevglForumBlogPostMapper.getBlogDetails(params);
		for (Map<String, Object> blog : blogDetails) {
			blog.put("blogHead", uploadPathUtils.stitchingPath(blog.get("blogHead"), "16"));
			// 标识博客是否被当前登录用户点赞
			if (meLikes != null && meLikes.size() > 0) {
				blog.put("isThumbsUp", true);
			}else {
				blog.put("isThumbsUp", false);
			}
			// 标识博客是否被当前登录用户收藏
			if (meFavorities != null && meFavorities.size() > 0) {
				blog.put("isCollect", true);
			}else {
				blog.put("isCollect", false);
			}
			// 标识博主是否被当前登录用户关注
			if (forumAttentions != null && forumAttentions.size() > 0) {
				blog.put("isFollow", true);
			}else {
				blog.put("isFollow", false);
			}
			// 标识当前登录人是不是博主
			if (StrUtils.isNotEmpty(loginUserId) && loginUserId.equals(blog.get("traineeId"))) {
				blog.put("isCreator", true);
			}else {
				blog.put("isCreator", false);
			}
		}
		// 更新博客的查阅数
		TevglForumBlogPost forumBlogPost = new TevglForumBlogPost();
		forumBlogPost.setPostId(postId);
		forumBlogPost.setViewNum(1);
		tevglForumBlogPostMapper.plusNum(forumBlogPost);
		return R.ok().put(Constant.R_DATA, blogDetails);
	}
	
	/** 
     * 根据指定大小和指定精度压缩图片 
     *  
     * @param srcPath 
     *            源图片地址 
     * @param desPath 
     *            目标图片地址 
     * @param desFileSize
     *            指定图片大小，单位kb 
     * @param accuracy 
     *            精度，递归压缩的比率，建议小于0.9 
     * @return 
     */
	public static String commpressPicForSize(String srcPath, String desPath,  
            long desFileSize, double accuracy) {  
        if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(srcPath)) {  
            return null;  
        }  
        if (!new File(srcPath).exists()) {  
            return null;  
        }  
        try {  
            File srcFile = new File(srcPath);  
            long srcFileSize = srcFile.length();  
            System.out.println("源图片：" + srcPath + "，大小：" + srcFileSize / 1024  
                    + "kb");  
  
            // 1、先转换成jpg  
            Thumbnails.of(srcPath).scale(1f).toFile(desPath);  
            // 递归压缩，直到目标文件大小小于desFileSize  
            commpressPicCycle(desPath, desFileSize, accuracy);
  
            File desFile = new File(desPath);  
            System.out.println("目标图片：" + desPath + "，大小" + desFile.length()  
                    / 1024 + "kb");  
            System.out.println("图片压缩完成！");  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
        return desPath;  
    }
    /**
     * 图片压缩:按指定大小把图片进行缩放（会遵循原图高宽比例）
     * 并设置图片文件大小
     */
    private static void commpressPicCycle(String desPath, long desFileSize,  
            double accuracy) throws IOException {  
        File srcFileJPG = new File(desPath);  
        long srcFileSizeJPG = srcFileJPG.length();  
        // 2、判断大小，如果小于指定大小，不压缩；如果大于等于指定大小，压缩  
        if (srcFileSizeJPG <= desFileSize * 1024) {
            return;  
        }  
        // 计算宽高  
        BufferedImage bim = ImageIO.read(srcFileJPG);  
        int srcWdith = bim.getWidth();  
        int srcHeigth = bim.getHeight();  
        int desWidth = new BigDecimal(srcWdith).multiply(  
                new BigDecimal(accuracy)).intValue();  
        int desHeight = new BigDecimal(srcHeigth).multiply(  
                new BigDecimal(accuracy)).intValue();  
  
        Thumbnails.of(desPath).size(desWidth, desHeight)  
                .outputQuality(accuracy).toFile(desPath);  
        commpressPicCycle(desPath, desFileSize, accuracy);  
    }
    
    // 测试文件压缩
	/*public static void main(String[] args) {
		TevglForumBlogPostServiceImpl.commpressPicForSize("D:\\image\\1200326422-0.jpg", "D:\\image\\data\\1200326422-0.jpg", 100, 0.3);
	}*/
}
