package com.budaos.modules.evgl.forum.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.forum.api.TevglForumFriendService;
import com.budaos.modules.evgl.forum.domain.TevglForumFriend;
import com.budaos.modules.evgl.forum.persistence.TevglForumFriendMapper;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/forum/tevglforumfriend")
public class TevglForumFriendServiceImpl implements TevglForumFriendService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglForumFriendServiceImpl.class);
	@Autowired
	private TevglForumFriendMapper tevglForumFriendMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TsysAttachService tsysAttachService;
	
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
		List<TevglForumFriend> tevglForumFriendList = tevglForumFriendMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglForumFriendList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglForumFriendList, "createUserId", "updateUserId");
		convertUtil.convertDict(tevglForumFriendList, "state", "state4");
		convertUtil.convertDict(tevglForumFriendList, "showIndex", "state3");
		if (tevglForumFriendList != null && tevglForumFriendList.size() > 0) {
			tevglForumFriendList.stream().forEach(tevglForumFriend -> {
				tevglForumFriend.setFriendLogo(uploadPathUtils.stitchingPath(tevglForumFriend.getFriendLogo(), "24"));
			});
		}
		PageUtils pageUtil = new PageUtils(tevglForumFriendList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglForumFriendList = tevglForumFriendMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglForumFriendList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglForumFriendList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglForumFriend
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglForumFriend tevglForumFriend) throws BudaosException {
		String uuid = Identities.uuid();
		tevglForumFriend.setFriendId(uuid);
		tevglForumFriend.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglForumFriend.setCreateTime(DateUtils.getNowTimeStamp());
		tevglForumFriend.setState("Y");
		tevglForumFriend.setShowIndex(StrUtils.isEmpty(tevglForumFriend.getShowIndex()) ? "N" : tevglForumFriend.getShowIndex());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("friendType", tevglForumFriend.getFriendType());
		Integer maxSortNum = tevglForumFriendMapper.getMaxSortNum(map);
		tevglForumFriend.setSortNum(maxSortNum);
		ValidatorUtils.check(tevglForumFriend);
		tevglForumFriendMapper.insert(tevglForumFriend);
		// 如果上传了附件
		String attachId = tevglForumFriend.getFriendLogoAttachId();
		if (StrUtils.isNotEmpty(attachId)) {
			tsysAttachService.updateAttach(attachId, uuid, "1", "24");
		}
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglForumFriend
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglForumFriend tevglForumFriend) throws BudaosException {
	    tevglForumFriend.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglForumFriend.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglForumFriend);
		tevglForumFriendMapper.update(tevglForumFriend);
		// 如果上传了附件
		String attachId = tevglForumFriend.getFriendLogoAttachId();
		if (StrUtils.isNotEmpty(attachId)) {
			tsysAttachService.updateAttach(attachId, tevglForumFriend.getFriendId(), "0", "24");
		}
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglForumFriendMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglForumFriendMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		TevglForumFriend tevglForumFriend = tevglForumFriendMapper.selectObjectById(id);
		if (tevglForumFriend != null) {
			tevglForumFriend.setFriendLogo(uploadPathUtils.stitchingPath(tevglForumFriend.getFriendLogo(), "24"));
		}
		return R.ok().put(Constant.R_DATA, tevglForumFriend);
	}

	/**
	 * 更新状态
	 * @param tevglForumFriend
	 * @return
	 */
	@Override
	public R updateState(TevglForumFriend tevglForumFriend) {
		if (StrUtils.isEmpty(tevglForumFriend.getFriendId()) && StrUtils.isEmpty(tevglForumFriend.getState())) {
			return R.error("必传参数为空");
		}
		tevglForumFriendMapper.update(tevglForumFriend);
		return R.ok("状态更新成功");
	}
}
