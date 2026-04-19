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
import com.budaos.modules.evgl.site.api.TevglSiteFeedbackReplyService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: 意见反馈回复</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglSiteFeedbackReplyServiceImpl implements TevglSiteFeedbackReplyService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteFeedbackReplyServiceImpl.class);
	@Autowired
	private TevglSiteFeedbackReplyMapper tevglSiteFeedbackReplyMapper;
	@Autowired
	private TevglSiteFeedbackMapper tevglSiteFeedbackMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglSiteFeedbackReply> tevglSiteFeedbackReplyList = tevglSiteFeedbackReplyMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteFeedbackReplyList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglSiteFeedbackReplyList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglSiteFeedbackReplyList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglSiteFeedbackReplyList = tevglSiteFeedbackReplyMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteFeedbackReplyList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglSiteFeedbackReplyList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteFeedbackReply
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglSiteFeedbackReply tevglSiteFeedbackReply) throws BudaosException {
		tevglSiteFeedbackReply.setReplyId(Identities.uuid());
		tevglSiteFeedbackReply.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteFeedbackReply.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteFeedbackReply);
		tevglSiteFeedbackReplyMapper.insert(tevglSiteFeedbackReply);
		// 更新为已回复
		TevglSiteFeedback t = new TevglSiteFeedback();
		t.setFeedbackId(tevglSiteFeedbackReply.getFeedbackId());
		t.setHasReplied("Y");
		tevglSiteFeedbackMapper.update(t);
		return R.ok("保存成功");
	}
	/**
	 * 修改
	 * @param tevglSiteFeedbackReply
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglSiteFeedbackReply tevglSiteFeedbackReply) throws BudaosException {
	    tevglSiteFeedbackReply.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglSiteFeedbackReply.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglSiteFeedbackReply);
		tevglSiteFeedbackReplyMapper.update(tevglSiteFeedbackReply);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglSiteFeedbackReplyMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglSiteFeedbackReplyMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteFeedbackReplyMapper.selectObjectById(id));
	}

	@Override
	public R viewFeedbackReplyInfo(String feedbackId) {
		Map<String, Object> map = new HashMap<>();
		map.put("feedbackId", feedbackId);
		map.put("createUserId", serviceLoginUtil.getLoginUserId());
		List<TevglSiteFeedbackReply> list = tevglSiteFeedbackReplyMapper.selectListByMap(map);
		return R.ok().put(Constant.R_DATA, list);
	}
}
