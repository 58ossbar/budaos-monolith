package com.budaos.modules.evgl.site.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.site.api.TevglSiteFeedbackService;
import com.budaos.modules.evgl.site.domain.TevglSiteFeedback;
import com.budaos.modules.evgl.site.domain.TevglSiteFeedbackReply;
import com.budaos.modules.evgl.site.persistence.TevglSiteFeedbackMapper;
import com.budaos.modules.evgl.site.persistence.TevglSiteFeedbackReplyMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> Title: 意见反馈表</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglSiteFeedbackServiceImpl implements TevglSiteFeedbackService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteFeedbackServiceImpl.class);
	@Autowired
	private TevglSiteFeedbackMapper tevglSiteFeedbackMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TevglSiteFeedbackReplyMapper tevglSiteFeedbackReplyMapper;
	
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
		List<TevglSiteFeedback> tevglSiteFeedbackList = tevglSiteFeedbackMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteFeedbackList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglSiteFeedbackList, "createUserId", "updateUserId");
		convertUtil.convertDict(tevglSiteFeedbackList, "state", "state"); 
		convertUtil.convertDict(tevglSiteFeedbackList, "type", "feedbackBigType"); // 反馈的大类型，1网站2小程序3APP
		convertUtil.convertDict(tevglSiteFeedbackList, "feedbackType", "feedbackType"); // 1功能异常2体验问题3新功能建议4其它
		convertUtil.convertDict(tevglSiteFeedbackList, "traineeType", "trainee_type"); // 用户类型1游客2系统用户3学员4教师
		PageUtils pageUtil = new PageUtils(tevglSiteFeedbackList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSiteFeedbackList = tevglSiteFeedbackMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteFeedbackList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglSiteFeedbackList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteFeedback
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglSiteFeedback tevglSiteFeedback) throws BudaosException {
		tevglSiteFeedback.setFeedbackId(Identities.uuid());
		tevglSiteFeedback.setState("Y");
		tevglSiteFeedback.setHasReplied("N"); // 是否已回复（Y已回复N未回复）
		tevglSiteFeedback.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteFeedback);
		tevglSiteFeedbackMapper.insert(tevglSiteFeedback);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteFeedback
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglSiteFeedback tevglSiteFeedback) throws BudaosException {
	    tevglSiteFeedback.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglSiteFeedback.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglSiteFeedback);
		tevglSiteFeedbackMapper.update(tevglSiteFeedback);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglSiteFeedbackMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		// 如果有回复则先删除回复表
		Map<String, Object> params = new HashMap<>();
		params.put("feedbackIds", Arrays.asList(ids));
		List<TevglSiteFeedbackReply> replyList = tevglSiteFeedbackReplyMapper.selectListByMap(params);
		if (replyList != null && replyList.size() > 0) {
			replyList.stream().forEach(a -> {
				tevglSiteFeedbackReplyMapper.delete(a.getReplyId());
			});	
		}
		tevglSiteFeedbackMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteFeedbackMapper.selectObjectById(id));
	}

	/**
	 * 查询意见反馈
	 * @param params
	 * @return
	 */
	@Override
	public R queryFeedbackInfo( Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> tevglSiteFeedbackList = tevglSiteFeedbackMapper.selectListMapByMap(query);
		convertUtil.convertDict(tevglSiteFeedbackList, "state", "state"); 
		convertUtil.convertDict(tevglSiteFeedbackList, "type", "feedbackBigType"); // 反馈的大类型，1网站2小程序3APP
		convertUtil.convertDict(tevglSiteFeedbackList, "feedbackType", "feedbackType"); // 1功能异常2体验问题3新功能建议4其它
		convertUtil.convertDict(tevglSiteFeedbackList, "traineeType", "trainee_type"); // 用户类型1游客2系统用户3学员4教师
		if (tevglSiteFeedbackList != null && tevglSiteFeedbackList.size() > 0) {
			SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
			// 查询回复内容
			List<Object> feedbackIds = tevglSiteFeedbackList.stream().map(a -> a.get("feedbackId")).collect(Collectors.toList());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("feedbackIds", feedbackIds);
			List<TevglSiteFeedbackReply> feedbackReplyList = tevglSiteFeedbackReplyMapper.selectListByMap(map);
			convertUtil.convertUserId2RealName(feedbackReplyList, "createUserId", "updateUserId");
			convertUtil.convertUserId2RealName(feedbackReplyList, "createUserId", "updateUserId");
			feedbackReplyList.stream().forEach(a -> {
				Date date = com.budaos.modules.common.DateUtils.convertStringToDate(a.getCreateTime());
				a.setCreateTime(format.format(date));
			});
			tevglSiteFeedbackList.stream().forEach(feedback -> {
				// 日期处理
				Date date = com.budaos.modules.common.DateUtils.convertStringToDate(feedback.get("createTime").toString());
				feedback.put("createTime", format.format(date));
				// 头像处理
				String traineePic = feedback.get("traineePic").toString();
				feedback.put("traineePic", uploadPathUtils.stitchingPath(traineePic, "16"));
				List<TevglSiteFeedbackReply> children = feedbackReplyList.stream().filter(a -> a.getFeedbackId().equals(feedback.get("feedbackId"))).collect(Collectors.toList());
				feedback.put("children", children);
			});
		}
		PageUtils pageUtil = new PageUtils(tevglSiteFeedbackList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
}
