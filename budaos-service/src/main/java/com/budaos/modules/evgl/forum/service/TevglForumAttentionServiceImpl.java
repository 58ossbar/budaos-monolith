package com.budaos.modules.evgl.forum.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.forum.api.TevglForumAttentionService;
import com.budaos.modules.evgl.forum.domain.TevglForumAttention;
import com.budaos.modules.evgl.forum.persistence.TevglForumAttentionMapper;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class TevglForumAttentionServiceImpl implements TevglForumAttentionService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglForumAttentionServiceImpl.class);
	@Autowired
	private TevglForumAttentionMapper tevglForumAttentionMapper;
	@Autowired
	private TevglTraineeInfoMapper traineeInfoMapper;
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
		List<TevglForumAttention> tevglForumAttentionList = tevglForumAttentionMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglForumAttentionList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglForumAttentionList = tevglForumAttentionMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglForumAttentionList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglForumAttention
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglForumAttention tevglForumAttention) throws BudaosException {
		tevglForumAttention.setAttentionId(Identities.uuid());
		ValidatorUtils.check(tevglForumAttention);
		tevglForumAttentionMapper.insert(tevglForumAttention);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglForumAttention
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglForumAttention tevglForumAttention) throws BudaosException {
	    ValidatorUtils.check(tevglForumAttention);
		tevglForumAttentionMapper.update(tevglForumAttention);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglForumAttentionMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglForumAttentionMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglForumAttentionMapper.selectObjectById(id));
	}

	/**
	 * 我的关注列表
	 * @param params
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R queryMyFollowList( Map<String, Object> params, String loginUserId) {
		params.put("followId", loginUserId);
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String,Object>> queryFollowList = tevglForumAttentionMapper.queryFollowList(query);
		if (queryFollowList != null && queryFollowList .size() > 0) {
			queryFollowList.stream().forEach(follow -> {
				params.clear();
				params.put("traineeId", loginUserId);
				params.put("followId", follow.get("traineeId"));
				List<Map<String,Object>> followList = tevglForumAttentionMapper.queryFollowList(params);
				// 标识博主是否互相关注了
				if (followList != null && followList.size() > 0) {
					follow.put("mutualConcern", true);
				}else {
					follow.put("mutualConcern", false);
				}
				// 处理博主头像
				//follow.put("blogHead", uploadPathUtils.stitchingPath(follow.get("blogHead"), "16"));
			});
		}
		PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(queryFollowList);
		return R.ok().put(Constant.R_DATA, pageInfo);
	}

	/**
	 * 我的粉丝列表
	 * @param params
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R queryMyFansList(Map<String, Object> params, String loginUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 关注某人
	 * @param traineeId 当前被关注的人
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R follow(String traineeId, String loginUserId) {
		if (StrUtils.isEmpty(traineeId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数不能为空");
		}
		TevglForumAttention attention = new TevglForumAttention();
		attention.setAttentionId(Identities.uuid());
		attention.setTraineeId(traineeId);  // 被关注的博主
		attention.setFollowId(loginUserId);  // 粉丝
		attention.setAttentionType("2");
		attention.setState("Y");
		attention.setAttentionTime(DateUtils.getNowTimeStamp());
		attention.setCancelTime(null);
		
		// 查找当前博主是否被关注,如果已被关注，再次点击，取消关注，反之
		Map<String, Object> params = new HashMap<>();
		params.put("traineeId", traineeId);
		params.put("followId", loginUserId);
		List<TevglForumAttention> attentions = tevglForumAttentionMapper.selectListByMap(params);
		if (attentions != null && attentions.size() > 0) {
			attentions.stream().forEach(forumAttention -> {
				tevglForumAttentionMapper.delete(forumAttention.getAttentionId());
			});
			// 更新关注博主的关注数
			TevglTraineeInfo traineeInfo = new TevglTraineeInfo();
			traineeInfo.setTraineeId(loginUserId);
			traineeInfo.setFollowNum(-1);
			traineeInfoMapper.plusNum(traineeInfo);
			// 更新被关注博主的粉丝数
			TevglTraineeInfo t = new TevglTraineeInfo();
			t.setTraineeId(traineeId);
			t.setFansNum(-1);
			traineeInfoMapper.plusNum(t);
			return R.ok("取消关注");
		}
		// 入库
		tevglForumAttentionMapper.insert(attention);
		// 更新关注博主的关注数
		TevglTraineeInfo traineeInfo = new TevglTraineeInfo();
		traineeInfo.setTraineeId(loginUserId);
		traineeInfo.setFollowNum(1);
		traineeInfoMapper.plusNum(traineeInfo);
		// 更新被关注博主的粉丝数
		TevglTraineeInfo t = new TevglTraineeInfo();
		t.setTraineeId(traineeId);
		t.setFansNum(1);
		
		traineeInfoMapper.plusNum(t);
		return R.ok("成功关注").put(Constant.R_DATA, attention);
	}

	/**
	 * 取关某人
	 * @param traineeId 当前被取消关注的人
	 * @param loginUserId 当前登录用户
	 * @return
	 */
	@Override
	public R unfollow(String traineeId, String loginUserId) {
		// TODO Auto-generated method stub
		return null;
	}
}
