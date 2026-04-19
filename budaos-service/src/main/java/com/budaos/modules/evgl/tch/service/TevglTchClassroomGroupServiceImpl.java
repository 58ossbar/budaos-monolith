package com.budaos.modules.evgl.tch.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CbRoomUtils;
import com.budaos.modules.common.GlobalRoomPermission;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomGroupService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomGroup;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomGroupMember;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomGroupMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomGroupMemberMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomRoleprivilegeMapper;
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
 * <p> Title: 课堂小组</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
public class TevglTchClassroomGroupServiceImpl implements TevglTchClassroomGroupService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassroomGroupServiceImpl.class);
	@Autowired
	private TevglTchClassroomGroupMapper tevglTchClassroomGroupMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglTchClassroomGroupMemberMapper tevglTchClassroomGroupMemberMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TevglTchClassroomRoleprivilegeMapper tevglTchClassroomRoleprivilegeMapper;
	@Autowired
	private CbRoomUtils cbRoomUtils;
	
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
		List<TevglTchClassroomGroup> tevglTchClassroomGroupList = tevglTchClassroomGroupMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglTchClassroomGroupList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglTchClassroomGroupList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglTchClassroomGroupList = tevglTchClassroomGroupMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglTchClassroomGroupList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglTchClassroomGroupList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTchClassroomGroup
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglTchClassroomGroup tevglTchClassroomGroup) throws BudaosException {
		tevglTchClassroomGroup.setGpId(Identities.uuid());
		tevglTchClassroomGroup.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglTchClassroomGroup.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglTchClassroomGroup);
		tevglTchClassroomGroupMapper.insert(tevglTchClassroomGroup);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchClassroomGroup
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglTchClassroomGroup tevglTchClassroomGroup) throws BudaosException {
	    ValidatorUtils.check(tevglTchClassroomGroup);
		tevglTchClassroomGroupMapper.update(tevglTchClassroomGroup);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglTchClassroomGroupMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglTchClassroomGroupMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglTchClassroomGroupMapper.selectObjectById(id));
	}

	/**
	 * 保存课堂小组信息
	 * @param tevglTchClassroomGroup
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R saveClassroomGroupInfo(TevglTchClassroomGroup tevglTchClassroomGroup,
			String loginUserId) throws BudaosException {
		String ctId = tevglTchClassroomGroup.getCtId();
		String groupName = tevglTchClassroomGroup.getGroupName();
		groupName = groupName.trim();
		if (StrUtils.isEmpty(groupName)) {
			return R.error("名称不能为空");
		}
		if (groupName.length() > 20) {
			return R.error("名称不能超过20个字符");
		}
		if (StrUtils.isEmpty(ctId)) {
			return R.error("参数ctId为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		// 如果当前登录用户不是此课堂的创建者
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ADD_ROOM_GROUP);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 同课堂不允许重名的小组
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ctId", ctId);
		List<TevglTchClassroomGroup> list = tevglTchClassroomGroupMapper.selectListByMap(map);
		boolean flag = list.stream().anyMatch(a -> a.getGroupName().equals(tevglTchClassroomGroup.getGroupName()));
		if (flag) {
			return R.error("不允许有重名的小组名称");
		}
		// 填充信息
		tevglTchClassroomGroup.setGpId(Identities.uuid());
		tevglTchClassroomGroup.setNumber(0);
		tevglTchClassroomGroup.setCreateTime(DateUtils.getNowTimeStamp());
		tevglTchClassroomGroup.setCreateUserId(loginUserId);
		tevglTchClassroomGroup.setState("Y");
		// 排序号处理
		map.put("ctId", ctId);
		Integer sortNum = tevglTchClassroomGroupMapper.getMaxSortNum(map);
		tevglTchClassroomGroup.setSortNum(sortNum);
		// 保存至数据库
		tevglTchClassroomGroupMapper.insert(tevglTchClassroomGroup);
		return R.ok("添加成功");
	}

	/**
	 * 查询课堂的小组信息
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R listClassroomGroupSimpleInfo(String ctId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("ctId", ctId);
		map.put("state", "Y");
		map.put("sidx", "sort_num");
		map.put("order", "asc");
		List<TevglTchClassroomGroup> list = tevglTchClassroomGroupMapper.selectListByMap(map);
		if (list == null || list.size() == 0) {
			return R.ok().put(Constant.R_DATA, new ArrayList<>())
					.put(GlobalRoomPermission.LICENSE, getLicenseMap(tevglTchClassroom, loginUserId));
		}
		// 查询课堂小组成员
		List<String> gpIds = list.stream().map(a -> a.getGpId()).collect(Collectors.toList());
		map.clear();
		map.put("gpIds", gpIds);
		List<TevglTchClassroomGroupMember> groupMemberList = tevglTchClassroomGroupMemberMapper.selectListByMap(map);
		// 取部分属性
		List<Map<String,Object>> collect = list.stream().map(a -> {
			Map<String, Object> info = new HashMap<>();
			info.put("gpId", a.getGpId());
			info.put("ctId", a.getCtId());
			info.put("groupName", a.getGroupName());
			info.put("number", a.getNumber());
			info.put("isItHere", groupMemberList.stream().anyMatch(groupMember -> groupMember.getTraineeId().equals(loginUserId) && groupMember.getGpId().equals(a.getGpId())));
			return info;
		}).collect(Collectors.toList());
		return R.ok().put(Constant.R_DATA, collect)
				.put(GlobalRoomPermission.LICENSE, getLicenseMap(tevglTchClassroom, loginUserId));
	}
	
	/**
	 * 处理权限标识
	 * @param tevglTchClassroom
	 * @param loginUserId
	 * @return
	 */
	private Map<String, Object> getLicenseMap(TevglTchClassroom tevglTchClassroom, String loginUserId){
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("ctId", tevglTchClassroom.getCtId());
		List<String> permissionList = tevglTchClassroomRoleprivilegeMapper.queryPermissionList(info);
		info.put(GlobalRoomPermission.ADD_ROOM_GROUP_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ADD_ROOM_GROUP, permissionList));
		info.put(GlobalRoomPermission.RENAME_ROOM_GROUP_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.RENAME_ROOM_GROUP, permissionList));
		info.put(GlobalRoomPermission.DELETE_ROOM_GROUP_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.DELETE_ROOM_GROUP, permissionList));
		info.put(GlobalRoomPermission.ADD_ROOM_GROUP_TRAINEE_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ADD_ROOM_GROUP_TRAINEE, permissionList));
		info.put(GlobalRoomPermission.SET_ROOM_GROUP_LEADER_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.SET_ROOM_GROUP_LEADER, permissionList));
		info.put(GlobalRoomPermission.DELETE_ROOM_GROUP_TRAINEE_KEY, cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.DELETE_ROOM_GROUP_TRAINEE, permissionList));
		return info;
	}

	/**
	 * 查看课堂分组信息
	 * @param gpId
	 * @param type 0默认查询基本信息，1查询基本信息和小组下的成员信息
	 * @return
	 */
	@Override
	public R viewClassroomGroupInfo(String gpId, int type, Map<String, Object> params, String loginUserId) {
		if (StrUtils.isEmpty(gpId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		type = StrUtils.isNull(type) ? 0 : type;
		TevglTchClassroomGroup tevglTchClassroomGroup = tevglTchClassroomGroupMapper.selectObjectById(gpId);
		if (tevglTchClassroomGroup == null) {
			return R.error("无效的记录");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(tevglTchClassroomGroup.getCtId());
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		params.put("gpId", gpId);
		PageUtils pageUtil = null;
		// 课堂小组下的成员信息
		if (1 == type) {
			// 构建查询条件对象Query
			params.put("sidx", "t1.dict_code");
			params.put("order", "desc, t1.sort_num asc");
			// 查询没有设置过身份的人
			if ("0".equals(params.get("dictCode"))) {
				params.put("dictCodeWithNull", "Y");
				params.remove("dictCode");
			}
			Query query = new Query(params);
			PageHelper.startPage(query.getPage(),query.getLimit());
			List<Map<String,Object>> list = tevglTchClassroomGroupMemberMapper.selectListMapForCommon(query);
			if (list != null && list.size() > 0) {
				// 转换性别
				convertUtil.convertDict(list, "traineeSex", "sex");
				list.stream().forEach(a -> {
					a.put("traineePic", uploadPathUtils.stitchingPath(a.get("traineePic") , "16"));
				});
			}
			pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		}
		// 课堂小组基本信息
		Map<String, Object> map = new HashMap<>();
		map.put("gpId", tevglTchClassroomGroup.getGpId());
		map.put("ctId", tevglTchClassroomGroup.getCtId());
		map.put("groupName", tevglTchClassroomGroup.getGroupName());
		map.put("number", tevglTchClassroomGroup.getNumber());
		return R.ok().put(Constant.R_DATA, pageUtil)
				.put("baseInfo", map)
				.put(GlobalRoomPermission.LICENSE, getLicenseMap(tevglTchClassroom, loginUserId));
	}

	/**
	 * 将学员加入某小组
	 * @param jsonObject {'ctId':'课堂主键', 'gpId':'小组主键必传','traineeIds':['1', '2'], 'token':''}
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R setTraineeToGroup(JSONObject jsonObject, String loginUserId) throws BudaosException {
		String ctId = jsonObject.getString("ctId");
		String gpId = jsonObject.getString("gpId");
		JSONArray jsonArray = jsonObject.getJSONArray("traineeIds");
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(gpId)) {
			return R.error("必传参数为空");
		}
		if (jsonArray == null || jsonArray.size() == 0) {
			return R.ok("没有选择成员");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的课堂");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ADD_ROOM_GROUP_TRAINEE);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 获取排序号
		Map<String, Object> map = new HashMap<>();
		map.put("gpId", gpId);
		Integer sortNum = tevglTchClassroomGroupMemberMapper.getMaxSortNum(map);
		// 已经在小组的成员
		List<TevglTchClassroomGroupMember> groupMemberList = tevglTchClassroomGroupMemberMapper.selectListByMap(map);
		List<String> existTraineeIds = groupMemberList.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
		// 待保存的数据
		List<TevglTchClassroomGroupMember> list = new ArrayList<TevglTchClassroomGroupMember>();
		List<String> traineeIds = new ArrayList<String>();
		for (int i = 0; i < jsonArray.size(); i++) {
			traineeIds.add(jsonArray.get(i).toString());
		}
		// 已经存在小组的成员不重复添加进去
		traineeIds.removeAll(existTraineeIds);
		for (int j = 0; j < traineeIds.size(); j++) {
			TevglTchClassroomGroupMember t = new TevglTchClassroomGroupMember();
			t.setGmId(Identities.uuid());
			t.setGpId(gpId);
			t.setTraineeId(traineeIds.get(j));
			t.setSortNum(j + sortNum);
			t.setState("Y");
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setCreateUserId(loginUserId);
			list.add(t);
		}
		if (list.size() > 0) {
			tevglTchClassroomGroupMemberMapper.insertBatch(list);
			// 更新小组人数
			TevglTchClassroomGroup tevglTchClassroomGroup = new TevglTchClassroomGroup();
			tevglTchClassroomGroup.setGpId(gpId);
			tevglTchClassroomGroup.setNumber(jsonArray.size());
			tevglTchClassroomGroupMapper.plusNum(tevglTchClassroomGroup);
		}
		return R.ok("添加成功");
	}

	/**
	 * 将成员移出小组
	 * @param gmId
	 * @param loginUserId
	 * @return
	 * @throws BudaosException
	 */
	@Override
	public R deleteClassroomGroupMember(String gmId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(gmId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroomGroupMember groupMember = tevglTchClassroomGroupMemberMapper.selectObjectById(gmId);
		if (groupMember == null) {
			return R.ok();
		}
		if (!loginUserId.equals(groupMember.getCreateUserId())) {
			return R.error("非法操作，没有权限");
		}
		tevglTchClassroomGroupMemberMapper.delete(gmId);
		// 更新小组人数-1
		TevglTchClassroomGroup tevglTchClassroomGroup = new TevglTchClassroomGroup();
		tevglTchClassroomGroup.setGpId(groupMember.getGpId());
		tevglTchClassroomGroup.setNumber(-1);
		tevglTchClassroomGroupMapper.plusNum(tevglTchClassroomGroup);
		return R.ok("移出小组");
	}
	
	/**
	 * 批量保存小组
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R saveGroupInfo( JSONObject jsonObject, String loginUserId) {
		String ctId = jsonObject.getString("ctId");
		JSONArray list = jsonObject.getJSONArray("list");
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (list == null || list.size() == 0) {
			return R.error("请至少填写一个小组名称");
		}
		// 长度判断
		for (int i = 0; i < list.size(); i++) {
			if (list.getString(i).length() > 20) {
				return R.error("第" + (i + 1) + "行名称不能超过20个字符，请自行调整");
			}
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("无效的记录");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.ADD_ROOM_GROUP);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 助教创建也认为是课堂创建者添加的
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && tevglTchClassroom.getTraineeId().equals(loginUserId);
		if (isTeachingAssistant) {
			loginUserId = tevglTchClassroom.getCreateUserId();
		}
		String msg = "";
		List<String> inputListName = list.stream().map(a -> a.toString()).distinct().collect(Collectors.toList());
		// 等待入库的名称
		List<String> waitingList = new ArrayList<String>();
		// 查出本课堂已经存在的课堂小组
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		List<TevglTchClassroomGroup> groupList = tevglTchClassroomGroupMapper.selectListByMap(params);
		int sortNum = 0;
		if (groupList != null && groupList.size() > 0) {
			//List<String> existedNames = groupList.stream().map(a -> "\""+a.getGroupName() + "\"").collect(Collectors.toList());
			List<String> existedNames = groupList.stream().map(a -> a.getGroupName()).collect(Collectors.toList());
			sortNum = groupList.size();
			// 只添加了一个小组时
			if (inputListName.size() == 0) {
				if (groupList.stream().anyMatch(a -> a.getGroupName().equals(list.get(0)))) {
					return R.error("已经存在了相同的小组名称，请修改");
				}
			} else {
				for (int i=0; i<inputListName.size(); i++) {
					for (int j=0; j<existedNames.size(); j++) {
						if (inputListName.get(i).equals(existedNames.get(j))) {
							msg = "重复的名称已经忽略";
							continue;
						}
						if (!existedNames.contains(inputListName.get(i))) {
							if (!waitingList.contains(inputListName.get(i))) {
								waitingList.add(inputListName.get(i));
							}
						}
					}
				}
			}
		} else {
			waitingList = list.stream().distinct().map(a -> a.toString()).collect(Collectors.toList());
		}
		// 入库
		if (waitingList.size() > 0) {
			List<TevglTchClassroomGroup> insertList = new ArrayList<>();
			for (int i = 0; i < waitingList.size(); i++) {
				TevglTchClassroomGroup t = new TevglTchClassroomGroup();
				t.setGpId(Identities.uuid());
				t.setCtId(ctId);
				t.setGroupName(waitingList.get(i));
				t.setSortNum(sortNum + i);
				t.setState("Y");
				t.setCreateTime(DateUtils.getNowTimeStamp());
				t.setCreateUserId(loginUserId);
				t.setNumber(0);
				insertList.add(t);
			}
			tevglTchClassroomGroupMapper.insertBatch(insertList);
		} else {
			msg = "重复的名称已经忽略";
		}
		return R.ok("保存成功 " + msg).put(Constant.R_DATA, null);
	}

	/**
	 * 将成员设为组长
	 * @param gpId
	 * @param traineeId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R setTraineeToLeader(String gpId, String traineeId, String loginUserId) {
		if (StrUtils.isEmpty(gpId) || StrUtils.isEmpty(traineeId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroomGroup group = tevglTchClassroomGroupMapper.selectObjectById(gpId);
		if (group == null) {
			return R.error("无效的小组");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(group.getCtId());
		if (tevglTchClassroom == null) {
			return R.error("无效的记录");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.SET_ROOM_GROUP_LEADER);
		if (!hasOperationBtnPermission) {
			return R.error(GlobalRoomPermission.NO_AUTH_SHOW_TEXT);
		}
		// 查询小组组长
		Map<String, Object> params = new HashMap<>();
		params.put("gpId", gpId);
		params.put("isLeader", "Y");
		List<TevglTchClassroomGroupMember> list = tevglTchClassroomGroupMemberMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			list.stream().forEach(a -> {
				a.setIsLeader("N");
				tevglTchClassroomGroupMemberMapper.update(a);
			});
		}
		// 找到此人
		params.clear();
		params.put("gpId", gpId);
		params.put("traineeId", traineeId);
		List<TevglTchClassroomGroupMember> memberList = tevglTchClassroomGroupMemberMapper.selectListByMap(params);
		if (memberList != null && memberList.size() > 0) {
			TevglTchClassroomGroupMember member = memberList.get(0);
			member.setIsLeader("Y");
			tevglTchClassroomGroupMemberMapper.update(member);
		}
		return R.ok("组长设置成功");
	}
	
	/**
	 * 删除小组
	 * @param gpId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteClassroomGroup(String gpId, String loginUserId) throws BudaosException {
		if (StrUtils.isEmpty(gpId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroomGroup group = tevglTchClassroomGroupMapper.selectObjectById(gpId);
		if (group == null) {
			return R.error("无效的记录");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(group.getCtId());
		if (tevglTchClassroom == null) {
			return R.error("无效的记录");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.DELETE_ROOM_GROUP);
		if (!hasOperationBtnPermission) {
			return R.error("没有权限，非法操作！");
		}
		// 先删除小组成员
		Map<String, Object> params = new HashMap<>();
		params.put("gpId", gpId);
		List<TevglTchClassroomGroupMember> list = tevglTchClassroomGroupMemberMapper.selectListByMap(params);
		if (list != null && list.size() > 0) {
			List<String> gmIds = list.stream().map(a -> a.getGmId()).collect(Collectors.toList());
			String[] array = gmIds.stream().toArray(String[]::new);
			tevglTchClassroomGroupMemberMapper.deleteBatch(array);
		}
		tevglTchClassroomGroupMapper.delete(gpId);
		return R.ok("删除成功");
	}

	/**
	 * 重命名小组
	 * @param gpId
	 * @param name
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R rename(String gpId, String name, String loginUserId) {
		if (StrUtils.isEmpty(gpId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		if (StrUtils.isEmpty(name)) {
			return R.error("小组名称不能为空");
		}
		if (name.length() > 20) {
			return R.error("不能超过20个字符");
		}
		TevglTchClassroomGroup group = tevglTchClassroomGroupMapper.selectObjectById(gpId);
		if (group == null) {
			return R.error("无效的记录");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(group.getCtId());
		if (tevglTchClassroom == null) {
			return R.error("无效的记录");
		}
		boolean hasOperationBtnPermission = cbRoomUtils.hasOperationBtnPermission(tevglTchClassroom, loginUserId, GlobalRoomPermission.RENAME_ROOM_GROUP);
		if (!hasOperationBtnPermission) {
			return R.error("没有权限，非法操作！");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", group.getCtId());
		List<TevglTchClassroomGroup> groupList = tevglTchClassroomGroupMapper.selectListByMap(params);
		if (groupList != null && groupList.size() > 0) {
			boolean flag = groupList.stream().anyMatch(a -> !a.getGpId().equals(gpId) && a.getGroupName().equals(name));
			if (flag) {
				return R.error("小组名称不能重复，请重新命名");
			}
		}
		TevglTchClassroomGroup t = new TevglTchClassroomGroup();
		t.setGpId(gpId);
		t.setGroupName(name);
		tevglTchClassroomGroupMapper.update(t);
		return R.ok("重命名成功");
	}
}
