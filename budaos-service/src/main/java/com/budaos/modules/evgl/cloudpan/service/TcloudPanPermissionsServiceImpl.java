package com.budaos.modules.evgl.cloudpan.service;

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
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.cloudpan.api.TcloudPanPermissionsService;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanPermissions;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanDirectoryMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanFileMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanNavigationBarMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanPermissionsMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.budaos.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
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
 * 文件权限业务逻辑实现类
 */

@Service
public class TcloudPanPermissionsServiceImpl implements TcloudPanPermissionsService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TcloudPanPermissionsServiceImpl.class);
	@Autowired
	private TcloudPanPermissionsMapper tcloudPanPermissionsMapper;
	@Autowired
	private TcloudPanDirectoryMapper tcloudPanDirectoryMapper;
	@Autowired
	private TcloudPanFileMapper tcloudPanFileMapper;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TcloudPanNavigationBarMapper tcloudPanNavigationBarMapper;
	@Autowired
	private TevglTraineeInfoMapper tevglTraineeInfoMapper;
	private static String is_cloud_permission = "isCloudPermission";
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private CbRoomUtils roomUtils;

	@Override
	public R setPermissionsNew( JSONObject jsonObject, String loginUserId) {
		JSONArray traineeIds = jsonObject.getJSONArray("traineeIds"); // 权限拥有者(学员)
		String fileId = jsonObject.getString("fileId"); // 文件id
		String folderId = jsonObject.getString("folderId"); // 文件夹id
		// String isShare = jsonObject.getString("isShare"); // 是否共享文件/文件夹
		String pkgId = jsonObject.getString("pkgId"); // 教学包id
		// 查询对象
		Map<String, Object> params = new HashMap<String, Object>();
		//
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无法授权");
		}
		// 当前端没传fileId时默认fileId为空字符串
		if (fileId == null) {
			fileId = "";
		}
		String okPkgId = null;
		String refPkgId = null;
		if (tevglPkgInfo != null) {
			refPkgId = tevglPkgInfo.getRefPkgId();
		}
		List<String> pkgIdList = new ArrayList<String>();
		String dirId = null;
		// fileId为空时，folderId不为空，则是对文件夹进行授权
		if (StrUtils.isEmpty(fileId) && StrUtils.isNotEmpty(folderId)) {
			TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(folderId);
			if (tcloudPanDirectory == null) {
				return R.error("无效的目录");
			}
			okPkgId = tcloudPanDirectory.getPkgId();
		}
		// fileId不为空时，folderId为空，则是对文件进行授权
		if (StrUtils.isNotEmpty(fileId) && StrUtils.isEmpty(folderId)) {
			TcloudPanFile cloudFile = tcloudPanFileMapper.selectObjectById(fileId);
			if (cloudFile == null) {
				return R.error("无效的文件");
			}
			dirId = cloudFile.getDirId();
			if (pkgId.equals(cloudFile.getDirId()) || tevglPkgInfo.getRefPkgId().equals(cloudFile.getDirId())) {
				okPkgId = cloudFile.getDirId();
			} else {
				TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(cloudFile.getDirId());
				if (tcloudPanDirectory == null) {
					return R.error("无效的目录");
				}
				okPkgId = tcloudPanDirectory.getPkgId();
			}
		}
		pkgIdList.add(okPkgId);
		params.put("pkgIds", pkgIdList);
		List<Map<String, Object>> allList = tcloudPanDirectoryMapper.selectListByUnionAllNew(params);
		// 记录当前目录及子目录的集合
		List<Map<String, Object>> resultMap = new ArrayList<>();
		// 子级目录
		List<Map<String, Object>> childrenDir = new ArrayList<>();

		if (traineeIds == null || traineeIds.size() == 0) { // 清空权限
			R empty = emptyPermissions(fileId, folderId, pkgId, resultMap, childrenDir, refPkgId, params);
			if ((Integer) empty.get("code") != 0) {
				return empty;
			}
		} else { // 授权
			// fileId为空时，folderId不为空，则是对文件夹进行授权
			if (StrUtils.isEmpty(fileId) && StrUtils.isNotEmpty(folderId)) {
				authForDir(tevglPkgInfo, okPkgId, folderId, traineeIds, allList, loginUserId);
			}
			// fileId不为空时，folderId为空，则是对文件进行授权
			if (StrUtils.isNotEmpty(fileId) && StrUtils.isEmpty(folderId)) {
				authForFile(tevglPkgInfo, fileId, traineeIds, allList);
			}
			// 清空部分学员的云盘权限
			emptySomestudent(folderId, dirId, fileId, childrenDir, traineeIds, params, allList);
		}

		return R.ok("设置成功");

	}

	private void authForDir(TevglPkgInfo tevglPkgInfo, String pkgId, String folderId, JSONArray traineeIds,
			List<Map<String, Object>> allList, String loginUserId) {
		// 找到此被授权目录所在的教学包的全部文件夹和文件
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		// 找到父级
		List<Map<String, Object>> parentList = new ArrayList<Map<String, Object>>();
		buildParentDirectory(folderId, allList, parentList);
		if (parentList.size() > 0) {
			dataList.addAll(parentList);
		}
		// 找到子级目录和子级文件
		List<Map<String, Object>> childrenDir = new ArrayList<Map<String, Object>>();
		buildChildDirectory(folderId, allList, childrenDir);
		if (childrenDir.size() > 0) {
			dataList.addAll(childrenDir);
		}
		// 找到已有的权限
		map.clear();
		map.put("currentPkgId", pkgId);
		List<TcloudPanPermissions> allPermissionsList = tcloudPanPermissionsMapper.selectListByMap(map);
		// 等待入库的集合
		List<TcloudPanPermissions> insertList = new ArrayList<TcloudPanPermissions>();
		// 遍历
		for (Object traineeId : traineeIds) {
			// 找到这个人已有的权限
			List<TcloudPanPermissions> thisUserHasPermissionsList = allPermissionsList.stream()
					.filter(a -> a.getOwner().equals(traineeId)).collect(Collectors.toList());
			// 遍历
			for (Map<String, Object> m : dataList) {
				String dirId = null;
				String fileId = null;
				boolean flag = false;
				// 目录的情况
				if ("1".equals(m.get("type"))) {
					dirId = m.get("id").toString();
					flag = thisUserHasPermissionsList.stream()
							.anyMatch(a -> StrUtils.isNotEmpty(a.getDirId()) && a.getDirId().equals(m.get("id")));
				}
				// 文件的情况
				if ("2".equals(m.get("type"))) {
					dirId = m.get("parentId").toString();
					fileId = m.get("id").toString();
					flag = thisUserHasPermissionsList.stream()
							.anyMatch(a -> StrUtils.isNotEmpty(a.getFileId()) && a.getFileId().equals(m.get("id")));
				}
				// 如果没有记录，才需要入库
				if (!flag) {
					TcloudPanPermissions t = new TcloudPanPermissions();
					t.setPerId(Identities.uuid());
					t.setOwner(traineeId.toString());
					t.setPkgId(pkgId); // 注意点，这里存的是目录对应的教学包id
					t.setCurrentPkgId(tevglPkgInfo.getPkgId()); // 这里存的是当前课堂的教学包id
					t.setDirId(dirId);
					t.setFileId(fileId);
					t.setCreateTime(loginUserId);
					t.setCreateTime(DateUtils.getNowTimeStamp());
					insertList.add(t);
				}
			}
		}
		if (insertList.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("list", insertList);
			tcloudPanPermissionsMapper.insertBatch(params);
		}
	}

	/**
	 * 针对文件授权
	 * 
	 * @param tevglPkgInfo 教学包信息
	 * @param fileId       文件id
	 * @param traineeIds   学员
	 * @param allList      所有的目录及文件
	 */
	private void authForFile(TevglPkgInfo tevglPkgInfo, String fileId, JSONArray traineeIds,
			List<Map<String, Object>> allList) {
		TcloudPanFile tcloudPanFile = tcloudPanFileMapper.selectObjectById(fileId);
		if (tcloudPanFile == null) {
			return;
		}
		String pkgId = null;
		// 某些文件可能直接属于教学包id下，即：dir_id字段的值为教学包id的值
		if (tevglPkgInfo.getPkgId().equals(tcloudPanFile.getDirId())
				|| tevglPkgInfo.getRefPkgId().equals(tcloudPanFile.getDirId())) {
			pkgId = tcloudPanFile.getDirId();
			log.debug("对直属于教学包id的文件，进行授权操作...");
		} else {
			TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(tcloudPanFile.getDirId());
			if (tcloudPanDirectory == null) {
				return;
			}
			pkgId = tcloudPanDirectory.getPkgId();
			log.debug("对某文件夹下的的文件，进行授权操作...");
		}
		// 记录当前目录及子目录的集合
		List<Map<String, Object>> resultMap = new ArrayList<>();
		// 1.找到已存在于的授权记录
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentPkgId", tevglPkgInfo.getPkgId());
		map.put("fileId", tcloudPanFile.getFileId());
		List<TcloudPanPermissions> allPermissionsList = tcloudPanPermissionsMapper.selectListByMap(map);
		List<String> fileIdList = allPermissionsList.stream().filter(a -> StrUtils.isNotEmpty(a.getFileId()))
				.map(a -> a.getFileId()).distinct().collect(Collectors.toList());
		buildParentDirectory(tcloudPanFile.getDirId(), allList, resultMap);
		// 等待入库的集合
		List<TcloudPanPermissions> insertList = new ArrayList<TcloudPanPermissions>();
		for (Object traineeId : traineeIds) {
			// 找到这个人已有的权限
			List<TcloudPanPermissions> thisUserHasPermissionsList = allPermissionsList.stream()
					.filter(a -> a.getOwner().equals(traineeId)).collect(Collectors.toList());
			for (Map<String, Object> pNode : resultMap) {
				boolean flag1 = thisUserHasPermissionsList.stream().anyMatch(a -> StrUtils.isNotEmpty(a.getDirId()) && a.getDirId().equals(pNode.get("id").toString()));
				boolean flag2 = thisUserHasPermissionsList.stream().anyMatch(a -> StrUtils.isNotEmpty(a.getFileId()) && a.getFileId().equals(pNode.get("id").toString()));
				if (!flag1 && !flag2) {
					TcloudPanPermissions t = new TcloudPanPermissions();
					t.setPerId(Identities.uuid());
					t.setOwner(traineeId.toString());
					t.setPkgId(pkgId); // 注意点
					t.setCurrentPkgId(tevglPkgInfo.getPkgId());
					t.setDirId(pNode.get("id").toString());
					t.setCreateTime(tevglPkgInfo.getCreateUserId());
					t.setCreateTime(DateUtils.getNowTimeStamp());
					insertList.add(t);
				}
			}
			if (fileIdList == null || fileIdList.size() == 0) {
				// TDDO
				TcloudPanPermissions t = new TcloudPanPermissions();
				t.setPerId(Identities.uuid());
				t.setOwner(traineeId.toString());
				t.setPkgId(pkgId); // 注意点
				t.setCurrentPkgId(tevglPkgInfo.getPkgId());
				t.setDirId(tcloudPanFile.getDirId());
				t.setFileId(fileId);
				t.setCreateTime(tevglPkgInfo.getCreateUserId());
				t.setCreateTime(DateUtils.getNowTimeStamp());
				insertList.add(t);
			} else {
				// 遍历已存在的权限
				for (int i = 0; i < fileIdList.size(); i++) {
					String fwId = fileIdList.get(i);
					boolean flag = thisUserHasPermissionsList.stream().anyMatch(a -> a.getFileId().equals(fwId));
					// 如果没有记录，才需要入库
					if (!flag) {
						// TDDO
						TcloudPanPermissions t = new TcloudPanPermissions();
						t.setPerId(Identities.uuid());
						t.setOwner(traineeId.toString());
						t.setPkgId(pkgId); // 注意点
						t.setCurrentPkgId(tevglPkgInfo.getPkgId());
						// t.setDirId(value);
						t.setFileId(fwId);
						t.setCreateTime(tevglPkgInfo.getCreateUserId());
						t.setCreateTime(DateUtils.getNowTimeStamp());
						insertList.add(t);
					}
				}
			}
		}
		if (insertList.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("list", insertList);
			tcloudPanPermissionsMapper.insertBatch(params);
		}
	}

	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return
	 */
	@SysLog(value = "查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TcloudPanPermissions> tcloudPanPermissionsList = tcloudPanPermissionsMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tcloudPanPermissionsList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tcloudPanPermissionsList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tcloudPanPermissionsList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>>)
	 * @param params
	 * @return
	 */
	@SysLog(value = "查询列表(返回List<Map<String, Object>)")
	public R queryForMap(Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> tcloudPanPermissionsList = tcloudPanPermissionsMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tcloudPanPermissionsList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tcloudPanPermissionsList, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 新增
	 * 
	 * @param tcloudPanPermissions
	 * @throws BudaosException
	 */
	@SysLog(value = "新增")
	public R save(TcloudPanPermissions tcloudPanPermissions)
			throws BudaosException {
		tcloudPanPermissions.setPerId(Identities.uuid());
		tcloudPanPermissions.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tcloudPanPermissions.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tcloudPanPermissions);
		tcloudPanPermissionsMapper.insert(tcloudPanPermissions);
		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param tcloudPanPermissions
	 * @throws BudaosException
	 */
	@SysLog(value = "修改")
	public R update(TcloudPanPermissions tcloudPanPermissions)
			throws BudaosException {
		tcloudPanPermissions.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		tcloudPanPermissions.setUpdateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tcloudPanPermissions);
		tcloudPanPermissionsMapper.update(tcloudPanPermissions);
		return R.ok();
	}

	/**
	 * 单条删除
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "单条删除")
	public R delete( String id) throws BudaosException {
		tcloudPanPermissionsMapper.delete(id);
		return R.ok();
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value = "批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tcloudPanPermissionsMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 查看明细
	 * 
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value = "查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tcloudPanPermissionsMapper.selectObjectById(id));
	}

	/**
	 * 设置云盘权限
	 * 
	 * @author zhouyl加
	 * @date 2021年1月25日
	 * @param jsonObject
	 * @param loginUserId 授权操作有以下几种情况： ①如果没有学员的情况 ②如果有学员且数据库没有这些人的记录的情况
	 *                    ③如果有学员但要清空有此文件/文件夹共享权限的学员的情况
	 * @return
	 */
	@Override
	public R setPermissions(JSONObject jsonObject, String loginUserId) {
		JSONArray traineeIds = jsonObject.getJSONArray("traineeIds"); // 权限拥有者(学员)
		String fileId = jsonObject.getString("fileId"); // 文件id
		String folderId = jsonObject.getString("folderId"); // 文件夹id
		String isShare = jsonObject.getString("isShare"); // 是否共享文件/文件夹
		String pkgId = jsonObject.getString("pkgId"); // 教学包id
		// 当前端没传fileId时默认fileId为空字符串
		if (fileId == null) {
			fileId = "";
		}
		R r = permissionCheckIsPass(folderId, pkgId, loginUserId);
		if ((Integer) r.get("code") != 0) {
			return r;
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		String refPkgId = null;
		if (pkgInfo != null) {
			refPkgId = pkgInfo.getRefPkgId();
		}
		// 记录当前目录及子目录的集合
		List<Map<String, Object>> resultMap = new ArrayList<>();
		// 记录父级目录的集合
		List<Map<String, Object>> directoryMap = new ArrayList<>();
		// 子级目录
		List<Map<String, Object>> childrenDir = new ArrayList<>();
		// 查询对象
		Map<String, Object> params = new HashMap<String, Object>();
		// ①如果没有学员的情况则删除云盘权限操作
		if (traineeIds == null || traineeIds.size() == 0) {
			R empty = emptyPermissions(fileId, folderId, pkgId, resultMap, childrenDir, refPkgId, params);
			if ((Integer) empty.get("code") != 0) {
				return empty;
			}
		}
		// 给学员授予云盘权限
		resultMap = authorization(traineeIds, refPkgId, fileId, folderId, pkgId, isShare, loginUserId, params,
				resultMap, directoryMap);

		return R.ok("授权成功").put("resultMap", resultMap);
	}

	/**
	 * 删除云盘权限
	 * @param fileId
	 * @param folderId
	 * @param pkgId
	 * @param resultMap
	 * @param childrenDir
	 * @param refPkgId
	 * @param params
	 * @return
	 */
	private R emptyPermissions(String fileId, String folderId, String pkgId, List<Map<String, Object>> resultMap,
			List<Map<String, Object>> childrenDir, String refPkgId, Map<String, Object> params) {
		if (fileId == null || fileId == "") { // 删除文件夹的共享权限
			deleteDirAndSubdirectory(folderId, pkgId, resultMap, childrenDir, params);
		} else { // 删除文件的共享权限
			/**
			 * 删除文件的共享权限有以下几种情况 ① 删除当前课堂且在全部目录下的文件权限的情况 ② 删除教学包带过来的文件且在全部目录下的文件权限的情况 ③
			 * 删除当前课堂不在全部目录下的文件权限的情况
			 */
			TcloudPanFile cloudFile = tcloudPanFileMapper.selectObjectById(fileId);
			if (cloudFile != null) {
				params.clear();
				if (pkgId.equals(cloudFile.getDirId())) { // 取消给全部目录上传的文件授权的情况
					// 注释代码，可删除，亦可供查看功能的执行流程或结果
					System.out.println("删除全部目录上传文件的操作：");
					params.put("dirId", pkgId);
					params.put("fileId", fileId);
					List<Map<String, Object>> fileList = tcloudPanFileMapper.selectSimpleInfoList(params);
					if (fileList != null && fileList.size() > 0) {
						params.clear();
						params.put("dirId", pkgId);
						params.put("fileId", fileId);
					}
				}
				if (refPkgId.equals(cloudFile.getDirId())) { // 取消教学包带过来的文件且在全部目录下的权限的情况
					System.out.println("删除教学包带过来的文件且在全部目录下的操作：");
					params.put("dirId", refPkgId);
					params.put("fileId", fileId);
					List<Map<String, Object>> fileList = tcloudPanFileMapper.selectSimpleInfoList(params);
					if (fileList != null && fileList.size() > 0) {
						params.clear();
						params.put("dirId", refPkgId);
						params.put("fileId", fileId);
					}
				}
				// 如果是第一种或第二种情况才执行以下操作
				if (!StrUtils.isNull(params.get("fileId")) && !StrUtils.isNull(params.get("dirId"))) {
					List<Map<String, Object>> delTotalDirUploadFile = tcloudPanFileMapper
							.selectFileIdByDirIdAndFileIds(params);
					if (delTotalDirUploadFile != null && delTotalDirUploadFile.size() > 0) {
						List<String> collect = delTotalDirUploadFile.stream().map(a -> a.get("perId").toString())
								.collect(Collectors.toList());
						tcloudPanPermissionsMapper.deleteBatch(collect.stream().toArray(String[]::new));
					}
				}

				if (!pkgId.equals(cloudFile.getDirId()) && !refPkgId.equals(cloudFile.getDirId())) { // 取消不是全部目录上传的文件授权的情况
					System.out.println("删除不是全部目录上传文件的操作：");
					params.put("dirId", cloudFile.getDirId());
					params.put("fileId", fileId);
					List<Map<String, Object>> fileList = tcloudPanFileMapper.selectParentIdByfileIdAnddirId(params);
					if (fileList != null && fileList.size() > 0) {
						List<Object> dirIds = fileList.stream().map(a -> a.get("dirId")).collect(Collectors.toList());
						// 根据文件夹id和文件id删除授了权的文件
						params.clear();
						params.put("dirIds", dirIds);
						params.put("fileId", fileId);
						List<Map<String, Object>> cloudpanPersList = tcloudPanPermissionsMapper
								.selectPerIdByDirIds(params);
						if (cloudpanPersList != null && cloudpanPersList.size() > 0) {
							List<String> collect = cloudpanPersList.stream().map(a -> a.get("perId").toString())
									.collect(Collectors.toList());
							tcloudPanPermissionsMapper.deleteBatch(collect.stream().toArray(String[]::new));
						}
					}
				}
			}
		}
		return R.ok("设置成功");
	}

	/**
	 * 删除当前目录及子目录的记录
	 * 
	 * @param folderId    文件夹id
	 * @param resultMap   记录当前目录及子目录的集合
	 * @param childrenDir 存放子级目录的集合
	 * @param params
	 */
	public void deleteDirAndSubdirectory(String folderId, String pkgId, List<Map<String, Object>> resultMap,
			List<Map<String, Object>> childrenDir, Map<String, Object> params) {
		params.clear();
		params.put("pkgId", pkgId);
		List<Map<String, Object>> allList = tcloudPanDirectoryMapper.queryAllDirAndFile(params);
		// 有云盘权限的文件夹
		params.clear();
		params.put("dirId", folderId);
		List<Map<String, Object>> cloudpanPersList = tcloudPanPermissionsMapper.selectPerIdByDirIds(params);
		if (cloudpanPersList.size() > 0) {
			// 将有共享权限的文件夹添加到集合中
			resultMap.addAll(cloudpanPersList);
		}
		// 处理教学包带过来的目录(兼容以前版本，代码可保留亦可删除)
		/*List<Map<String, Object>> refDirectory = handleRefDirectory(folderId, childrenList, params);
		if (refDirectory != null && refDirectory.size() > 0) {
			resultMap.addAll(refDirectory);
		}*/
		TcloudPanDirectory panDirectory = tcloudPanDirectoryMapper.selectObjectById(folderId);
		// 存放来源目录及文件的集合
		List<Map<String, Object>> refDirectory = new ArrayList<>();
		// 根据来源目录id查找目录信息
		// TcloudPanDirectory directory =
		// tcloudPanDirectoryMapper.selectObjectById(panDirectory.getRefDirId());
		if (panDirectory != null) {
			params.clear();
			// 根据目录信息得到教学包id，搜索教学包下的所有目录及文件
			params.put("pkgId", panDirectory.getPkgId());
			refDirectory = tcloudPanDirectoryMapper.queryAllDirAndFile(params);
		}
		if (StrUtils.isNotEmpty(panDirectory.getRefDirId())) {
			// 递归查询子目录
			buildChildDirectory(panDirectory.getRefDirId(), refDirectory, childrenDir);
			if (childrenDir != null && childrenDir.size() > 0) {
				resultMap.addAll(childrenDir);
			}
		} else {
			// 递归查询子目录
			buildChildDirectory(folderId, refDirectory, childrenDir);
			if (childrenDir != null && childrenDir.size() > 0) {
				resultMap.addAll(childrenDir);
			}
		}

		/*// 记录父级目录的集合
		List<Map<String,Object>> directoryMap = new ArrayList<>();
		params.clear();
		params.put("dirId", folderId);
		List<Map<String, Object>> directoryList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
		// 递归查询目录的父级目录
		List<Map<String,Object>> parentdirectory = handleParentdirectory(folderId, directoryList, directoryMap, params);
		if (parentdirectory != null && parentdirectory.size() > 0) {
			resultMap.addAll(parentdirectory);
		}*/

		// 处理有共享权限的子集目录或文件
		// childrenDir = handleChildrendirectory(folderId, childrenList ,params);
		buildChildDirectory(folderId, allList, childrenDir);
		if (childrenDir != null && childrenDir.size() > 0) {
			System.out.println("执行删除子目录的操作：");
			// 将有云盘权限的子目录及文件添加到集合中
			resultMap.addAll(childrenDir);
		} else {// 没有子目录
			System.out.println("执行删除目录的操作：");
			if (cloudpanPersList != null && cloudpanPersList.size() > 0) {
				List<String> collect = cloudpanPersList.stream().map(a -> a.get("perId").toString())
						.collect(Collectors.toList());
				tcloudPanPermissionsMapper.deleteBatch(collect.stream().toArray(String[]::new));
				/*for (Map<String,Object> cloudPanPermissions : cloudpanPersList) {
				    // 删除授了权的目录
				    tcloudPanPermissionsMapper.delete(cloudPanPermissions.get("perId"));
				}*/
			}
		}

		// 有子目录
		if (resultMap != null && resultMap.size() > 0) {
			List<Object> dirIds = resultMap.stream().map(a -> a.get("dirId")).collect(Collectors.toList());
			params.clear();
			params.put("dirIds", dirIds);
			params.put("currentPkgId", pkgId);
			cloudpanPersList = tcloudPanPermissionsMapper.selectPerIdByDirIds(params);
			if (cloudpanPersList != null && cloudpanPersList.size() > 0) {
				List<String> collect = cloudpanPersList.stream().map(a -> a.get("perId").toString())
						.collect(Collectors.toList());
				tcloudPanPermissionsMapper.deleteBatch(collect.stream().toArray(String[]::new));
				/*for (Map<String,Object> cloudPanPermissions : cloudpanPersList) {
				    // 删除授了权的目录以及子目录
				    tcloudPanPermissionsMapper.delete(cloudPanPermissions.get("perId"));
				}*/
			}
		}
	}

	/**
	 * 处理教学包带过来的目录
	 * 
	 * @param folderId     目录
	 * @param childrenList 存储教学包带过来的目录集合
	 * @param params
	 */
	/* private List<Map<String, Object>> handleRefDirectory(String folderId, List<Map<String, Object>> childrenList,
			Map<String, Object> params) {
		// 根据目录id查询目录
		params.clear();
		params.put("dirId", folderId);
		List<Map<String, Object>> refDirectory = tcloudPanDirectoryMapper.selectSimpleListMap(params);
		// 取出来源目录id
		String refDirId = StrUtils.isNull(refDirectory.get(0).get("refDirId")) ? null : refDirectory.get(0).get("refDirId").toString();
		if (!StrUtils.isNull(refDirId)) {
			params.clear();
			// 查询来源目录下的子文件
			params.put("dirId", refDirId);
			List<Map<String, Object>> refFileList = tcloudPanFileMapper.selectParentIdByfileIdAnddirId(params);
			if (refFileList != null && refFileList.size() > 0) {
				childrenList.addAll(refFileList);
			}
			params.clear();
			params.put("dirId", refDirId);
			List<Map<String, Object>> directoryList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
			if (directoryList.size() > 0) {
				// 把教学包主目录添加到集合中
				childrenList.addAll(directoryList);
			}
			
			params.clear();
			// 根据来目录id查询来源目录下的子目录
			params.put("parentId", refDirId);
			List<Map<String, Object>> refDirList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
			List<Object> refDirIds = refDirList.stream().map(a -> a.get("dirId")).collect(Collectors.toList());
			if (refDirList != null && refDirList.size() > 0) {
				childrenList.addAll(refDirList);
			}
			if (refDirIds.size() > 0) {
				// 递归查询来源目录下的子目录
				subrecursion(refDirIds, childrenList, params);
			}
		}
		return childrenList;
	}*/

	/**
	 * 设置云盘权限
	 * 
	 * @param traineeIds   授权的学员
	 * @param fileId       文件id
	 * @param folderId     文件夹id
	 * @param pkgId        教学包id
	 * @param isShare
	 * @param loginUserId
	 * @param params
	 * @param resultMap    记录当前目录及子目录的集合
	 * @param directoryMap 云盘权限的父级目录
	 * @return
	 */
	private List<Map<String, Object>> authorization(JSONArray traineeIds, String refPkgId, String fileId,
			String folderId, String pkgId, String isShare, String loginUserId, Map<String, Object> params,
			List<Map<String, Object>> resultMap, List<Map<String, Object>> directoryMap) {
		String dirId = null;
		TcloudPanFile cloudFile = new TcloudPanFile();
		if (folderId == null || folderId == "") {
			cloudFile = tcloudPanFileMapper.selectObjectById(fileId);
			dirId = cloudFile.getDirId();
		}
		// 存储云盘权限的记录
		List<TcloudPanPermissions> list = new ArrayList<>();
		// 记录子级目录的集合
		List<Map<String, Object>> childrenList = new ArrayList<>();
		// 记录子级目录的返回值
		List<Map<String, Object>> childrenDir = new ArrayList<>();
		//
		List<Map<String, Object>> directoryList = new ArrayList<>();
		// 来源教学包的目录及文件集合
		// List<Map<String,Object>> refDirectory = new ArrayList<>();
		// 当前课堂的所有目录及文件
		List<Map<String, Object>> allList = new ArrayList<>();
		TcloudPanDirectory directory = tcloudPanDirectoryMapper.selectObjectById(folderId);
		// 查询权限表这个课堂下目录文件被授权的所有数据
		params.clear();
		params.put("pkgId", pkgId);
		List<TcloudPanPermissions> panList = tcloudPanPermissionsMapper.selectListByMap(params);
		// 文件夹有共享权限的情况
		if (fileId == null || fileId == "") {
			if (directory.getPkgId().equals(pkgId)) {
				// 根据教学包id查找当前课堂的所有目录及文件
				params.clear();
				params.put("pkgId", pkgId);
				allList = tcloudPanDirectoryMapper.queryAllDirAndFile(params);
				/*if (StrUtils.isNotEmpty(directory.getRefDirId())) {
					params.clear();
					// 根据refDirId查询对应的目录信息，得到教学包id，然后通过教学包id查询教学包下面的所有的目录及文件
					TcloudPanDirectory panDirectory = tcloudPanDirectoryMapper.selectObjectById(directory.getRefDirId());
					if (panDirectory != null) {
						params.clear();
						params.put("pkgId", panDirectory.getPkgId());
						refDirectory = tcloudPanDirectoryMapper.queryAllDirAndFile(params);
					}
				}*/
			} else { // 查找来源教学包下的目录及文件
				params.clear();
				params.put("pkgId", refPkgId);
				allList = tcloudPanDirectoryMapper.queryAllDirAndFile(params);
			}
			// 根据目录ID查询此目录是否有父级目录，如果有则给父级目录授权，如果没有则给自己授权
			/*params.clear();
			params.put("dirId", folderId);
			directoryList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
			System.out.println("执行新增目录的操作： " + directoryList.size());
			// 处理有共享权限的文件夹的父级目录
			resultMap = handleParentdirectory(folderId, directoryList, directoryMap, params);*/
			// 递归查找所有的父级目录
			buildParentDirectory(folderId, allList, resultMap);
		} else {
			params.clear();

			/*if (dirId.equals(pkgId)) { // 给当前课堂全部目录下的文件授权
				params.put("dirId", pkgId);
				params.put("fileId", fileId);
			    directoryList = tcloudPanFileMapper.selectSimpleInfoList(params);
			}
			if (dirId.equals(refPkgId)) { // 给教学包带过来的文件且在全部目录下的文件授权
				params.put("dirId", refPkgId);
				params.put("fileId", fileId);
				directoryList = tcloudPanFileMapper.selectSimpleInfoList(params);
			}
			if (!dirId.equals(pkgId) && !dirId.equals(refPkgId)) { // 给当前课堂非全部目录下的文件授权
				params.put("dirId", dirId);
				params.put("fileId", fileId);
			    directoryList = tcloudPanFileMapper.selectParentIdByfileIdAnddirId(params);
			}
			
			System.out.println("执行新增文件的操作： " + directoryList.size());
			// 处理有共享权限的文件的父级目录
			resultMap = handleParentdirectory(dirId, directoryList, directoryMap, params);*/
			/**
			 * 给文件授权操作有三种情况 ① 给当前课堂全部目录下的文件授权 ② 给教学包带过来的文件且在全部目录下的文件授权 ③ 给当前课堂非全部目录下的文件授权
			 */
			if (dirId.equals(pkgId)) { // 给当前课堂全部目录下的文件授权
				params.put("dirId", pkgId);
				params.put("fileId", fileId);
				directoryList = tcloudPanFileMapper.selectSimpleInfoList(params);
			}
			if (dirId.equals(refPkgId)) { // 给教学包带过来的文件且在全部目录下的文件授权
				params.put("dirId", refPkgId);
				params.put("fileId", fileId);
				directoryList = tcloudPanFileMapper.selectSimpleInfoList(params);
			}
			if (!dirId.equals(pkgId) && !dirId.equals(refPkgId)) { // 给当前课堂非全部目录下的文件授权
				TcloudPanDirectory panDirectory = tcloudPanDirectoryMapper.selectObjectById(dirId);
				params.put("pkgId", panDirectory.getPkgId());
				allList = tcloudPanDirectoryMapper.queryAllDirAndFile(params);
			}
			if (allList != null && allList.size() > 0) {
				System.out.println("给当前课堂非全部目录下的文件授权：");
				buildParentDirectory(cloudFile.getDirId(), allList, resultMap);
			}
			if (directoryList.size() > 0) {
				resultMap.addAll(directoryList);
			}
		}

		if (fileId == null || fileId == "") { // 给子级目录授权
			System.out.println("给子级目录授权的操作： ");
			// 处理教学包带过来的目录
			// refDirectory = handleRefDirectory(folderId, childrenList, params);

			/**
			 * 这段代码是给当前目录授权时教学包的目录也要授权：适用于以前的版本， 现在的版本是把当前课堂的、教学包的分开授权，可以不用删除，版本如果
			 * 要兼容给当前课堂目录授权时教学包目录也要授权，放开即可。
			 */
			/*if (StrUtils.isNotEmpty(directory.getRefDirId())) {
				System.out.println("执行给教学包目录授权的操作： ");
				buildChildDirectory(directory.getRefDirId(), refDirectory, childrenDir);
			}*/
			// 处理有共享权限的子级目录或文件
			// 第一种:递归循环查数据库
			// childrenDir = handleChildrendirectory(folderId, childrenList ,params);
			/*if (childrenDir.size() > 0 && refDirectory.size() > 0) {
			childrenDir.addAll(refDirectory);
			}*/
			// 第二种:先查询全部目录及文件,递归构建目录结构树
			buildChildDirectory(folderId, allList, childrenDir);

		}
		// ②如果有学员且数据库没有这些人的记录的情况下执行授权操作
		if (traineeIds != null && traineeIds.size() > 0) {
			for (int i = 0; i < traineeIds.size(); i++) {
				String traineeId = (String) traineeIds.get(i);
				if (resultMap != null && resultMap.size() > 0) {
					// 遍历填充
					for (Map<String, Object> map : resultMap) { // 给当前文件或者目录以及父级目录授权
						TcloudPanPermissions permissions = new TcloudPanPermissions();
						permissions.setPerId(Identities.uuid());
						permissions.setOwner(traineeId);
						permissions.setPkgId(pkgId);
						permissions.setCurrentPkgId(pkgId);
						if (fileId == null) { // 给文件夹授权
							permissions.setDirId(map.get("dirId").toString());
							permissions.setFileId(null);
						} else { // 给文件授权
							/*// 根据文件夹id和文件id查询，fileId只填充当前文件所在的文件夹，父级目录不填充
							params.clear();
							params.put("dirId", map.get("dirId"));
							params.put("fileId", fileId);
							List<Map<String, Object>> listMap = tcloudPanFileMapper.selectParentIdByfileIdAnddirId(params);
							if (listMap != null && listMap.size() > 0) { // 给非全部目录下的文件授权
								permissions.setFileId(fileId);
							} else { // 给全部目录下的文件授权
								if (!StrUtils.isNull(map.get("fileId"))) {
									permissions.setFileId(map.get("fileId").toString());
								}
							}*/
							if (!StrUtils.isNull(map.get("fileId"))) {
								permissions.setFileId(map.get("fileId").toString());
							} else {
								permissions.setFileId(fileId);
							}
							permissions.setDirId(map.get("dirId").toString());
						}
						permissions.setIsPermission(isShare);
						permissions.setCreateUserId(loginUserId);
						permissions.setCreateTime(DateUtils.getNowTimeStamp());
						/*// 查询这些人的所有父级目录，控制不重复插入记录
						params.clear();
						params.put("owner", traineeId);
						params.put("pkgId", pkgId);
						params.put("fileId", fileId);
						params.put("dirId", map.get("dirId"));
						List<Map<String, Object>> parentCatalog = tcloudPanPermissionsMapper.selectPerIdByDirIds(params);
						if (parentCatalog == null || parentCatalog.size() == 0) {
						    list.add(permissions);
						}*/
						// 不查数据库，控制不重复插入记录
						List<TcloudPanPermissions> collect = new ArrayList<>();
						if (fileId == null) {
							collect = panList.stream().filter(a -> a.getOwner().equals(permissions.getOwner())
									&& a.getCurrentPkgId().equals(permissions.getCurrentPkgId())
									&& StrUtils.isNotEmpty(a.getDirId()) && a.getDirId().equals(permissions.getDirId()))
									.collect(Collectors.toList());
						} else {
							collect = panList.stream()
									.filter(a -> a.getOwner().equals(permissions.getOwner())
											&& a.getCurrentPkgId().equals(permissions.getCurrentPkgId())
											&& StrUtils.isNotEmpty(a.getFileId())
											&& a.getFileId().equals(permissions.getFileId()))
									.collect(Collectors.toList());
						}
						if (collect == null || collect.size() == 0) {
							list.add(permissions);
						}
						// 如果没有记录则新增
						if (list != null && list.size() != 0) {
							params.clear();
							params.put("list", list);
							tcloudPanPermissionsMapper.insertBatch(params);
						}
						// 清空集合
						list.clear();
					}
				}

				if (fileId == null || fileId == "") { // 给子级目录授权
					if (childrenDir != null && childrenDir.size() > 0) {
						for (Map<String, Object> child : childrenDir) {
							TcloudPanPermissions permissions = new TcloudPanPermissions();
							permissions.setPerId(Identities.uuid());
							permissions.setOwner(traineeId);
							if (!pkgId.equals(child.get("pkgId"))) {
								permissions.setPkgId(child.get("pkgId").toString());
							} else {
								permissions.setPkgId(pkgId);
							}
							permissions.setCurrentPkgId(pkgId);
							if (fileId == null) { // 给文件夹授权
								if (child.get("dirId").equals(child.get("id"))) {
									// 如果dirId和id相同，填充dirId，否则填充id
									permissions.setDirId(child.get("dirId").toString());
								} else {
									permissions.setFileId(child.get("id").toString());
								}
							} else { // 给文件授权
								permissions.setDirId(child.get("dirId").toString());
								if (child.get("fileId") != null) {
									permissions.setFileId(child.get("fileId").toString());
								} else {
									permissions.setFileId(fileId);
								}
							}
							permissions.setIsPermission(isShare);
							permissions.setCreateUserId(loginUserId);
							permissions.setCreateTime(DateUtils.getNowTimeStamp());

							// 根据文件夹id和用户id查询云盘权限表中是否有这条记录
							/*params.clear();
							params.put("dirId", child.get("dirId"));
							params.put("owner", traineeId);
							if (child.get("fileId") != null) {
								params.put("fileId", child.get("fileId"));
							}else {
								params.put("fileId", fileId);
							}
							List<Map<String, Object>> subDirectory = tcloudPanPermissionsMapper.selectPerIdByDirIds(params);
							if (subDirectory == null || subDirectory.size() == 0) {
								list.add(permissions);
							}*/
							// 不查数据库，控制不重复插入记录
							List<TcloudPanPermissions> collect = new ArrayList<>();
							if (fileId == null) {
								collect = panList.stream()
										.filter(a -> a.getOwner().equals(permissions.getOwner())
												&& a.getCurrentPkgId().equals(permissions.getCurrentPkgId())
												&& StrUtils.isNotEmpty(a.getDirId())
												&& a.getDirId().equals(permissions.getDirId()))
										.collect(Collectors.toList());
							} else {
								collect = panList.stream()
										.filter(a -> a.getOwner().equals(permissions.getOwner())
												&& a.getCurrentPkgId().equals(permissions.getCurrentPkgId())
												&& StrUtils.isNotEmpty(a.getFileId())
												&& a.getFileId().equals(permissions.getFileId()))
										.collect(Collectors.toList());
							}
							if (collect == null || collect.size() == 0) {
								list.add(permissions);
							}
							// 如果没有记录则新增
							if (list != null && list.size() != 0) {
								params.clear();
								params.put("list", list);
								tcloudPanPermissionsMapper.insertBatch(params);
							}
							// 清空集合
							list.clear();
						}
					} else {
						/*// 给引用了免费的教学包的目录授权且没有子目录的情况(兼容以前版本，可保留)
						if (refDirectory != null && refDirectory.size() > 0) {
							for (Map<String, Object> refDir : refDirectory) {
								TcloudPanPermissions permissions = new TcloudPanPermissions();
						        permissions.setPerId(Identities.uuid());
						        permissions.setOwner(traineeId);
						        if (!pkgId.equals(refDir.get("pkgId"))) {
						        	permissions.setPkgId(refDir.get("pkgId").toString());
								}else {
									permissions.setPkgId(pkgId);
								}
						        permissions.setCurrentPkgId(pkgId);
						        if (fileId == null) { // 给文件夹授权
						            permissions.setDirId(refDir.get("dirId").toString());
						            permissions.setFileId(null);
						            
						        }else { // 给文件授权
						            permissions.setDirId(refDir.get("dirId").toString());
						            if (refDir.get("fileId") != null) {
						            	permissions.setFileId(refDir.get("fileId").toString());
									}else {
										permissions.setFileId(fileId);
									}
						        }
						        permissions.setIsPermission(isShare);
						        permissions.setCreateUserId(loginUserId);
						        permissions.setCreateTime(DateUtils.getNowTimeStamp());
						        
						        // 根据文件夹id和用户id查询云盘权限表中是否有这条记录
						        params.clear();
								params.put("dirId", refDir.get("dirId"));
								params.put("owner", traineeId);
								if (refDir.get("fileId") != null) {
									params.put("fileId", refDir.get("fileId"));
								}else {
									params.put("fileId", fileId);
								}
								List<Map<String, Object>> subDirectory = tcloudPanPermissionsMapper.selectPerIdByDirIds(params);
								if (subDirectory == null || subDirectory.size() == 0) {
									list.add(permissions);
								}
						        // 如果没有记录则新增
						        if (list != null && list.size() != 0) {
						            params.clear();
						            params.put("list", list);
						            tcloudPanPermissionsMapper.insertBatch(params);
						        }
						        // 清空集合
						        list.clear();
							}
						}*/

						// 当前文件夹下只有文件的情况
						params.clear();
						params.put("dirId", folderId);
						List<Map<String, Object>> catalogFiles = tcloudPanFileMapper
								.selectParentIdByfileIdAnddirId(params);
						// 取出所有文件id
						List<Object> fileIds = catalogFiles.stream().map(a -> a.get("fileId"))
								.collect(Collectors.toList());
						if (fileIds != null && fileIds.size() > 0) {
							for (int j = 0; j < fileIds.size(); j++) {
								String dirFileId = (String) fileIds.get(j);
								TcloudPanPermissions permissions = new TcloudPanPermissions();
								permissions.setPerId(Identities.uuid());
								permissions.setOwner(traineeId);
								permissions.setPkgId(pkgId);
								permissions.setDirId(folderId);
								permissions.setFileId(dirFileId);
								permissions.setIsPermission(isShare);
								permissions.setCurrentPkgId(pkgId);
								permissions.setCreateUserId(loginUserId);
								permissions.setCreateTime(DateUtils.getNowTimeStamp());
								params.clear();
								// 根据文件夹id和用户id查询云盘权限表中是否有这条记录
								params.put("dirId", folderId);
								params.put("traineeId", traineeId);
								params.put("fileId", dirFileId);
								List<Map<String, Object>> subFile = tcloudPanFileMapper
										.selectFileIdByDirIdAndFileIds(params);
								if (subFile == null || subFile.size() == 0) {
									list.add(permissions);
								}
								// 如果没有记录则新增
								if (list != null && list.size() != 0) {
									params.clear();
									params.put("list", list);
									tcloudPanPermissionsMapper.insertBatch(params);
								}
								// 清空集合
								list.clear();
							}
						}
					}
				}
			}
			if (resultMap.size() == 0 || childrenDir.size() == 0) { // 没有父级目录也没有子级目录的情况
				// 如果没有记录则新增
				if (list != null && list.size() != 0) {
					params.clear();
					params.put("list", list);
					tcloudPanPermissionsMapper.insertBatch(params);
				}
			}

			// 清空部分学员的云盘权限
			emptySomestudent(folderId, dirId, fileId, childrenDir, traineeIds, params, allList);

		}
		return resultMap;
	}

	/**
	 * 构建递归数：列出当前目录的所有子级目录
	 * @param parentId
	 * @param directoryList
	 * @param childrenDir
	 * @return
	 */
	private List<Map<String, Object>> buildChildDirectory(String parentId, List<Map<String, Object>> directoryList,
			List<Map<String, Object>> childrenDir) {
		if (directoryList == null || directoryList.size() == 0) {
			return directoryList;
		}
		List<Map<String, Object>> nodeList = directoryList.stream().filter(a -> a.get("parentId").equals(parentId))
				.collect(Collectors.toList());
		if (nodeList == null || nodeList.size() == 0) {
			return nodeList;
		}
		for (int i = 0; i < nodeList.size(); i++) {
			Map<String, Object> node = nodeList.get(i);
			childrenDir.add(node);
			List<Map<String, Object>> list = buildChildDirectory(node.get("id").toString(), directoryList, childrenDir);
			if (list != null && list.size() > 0) {
				node.put("children", list);
			} else {
				node.put("children", null);
			}
		}
		return nodeList;
	}

	/**
	 * 构建递归数：列出当前目录的所有上级目录
	 * 
	 * @param folderId      目录id
	 * @param directoryList 所有目录的集合
	 * @param resultMap     存放所有父级节点
	 * @return
	 */
	private void buildParentDirectory(String folderId, List<Map<String, Object>> directoryList,
			List<Map<String, Object>> resultMap) {
		if (directoryList == null || directoryList.size() == 0) {
			return;
		}
		System.out.println("目录id： " + folderId);
		// 取出当前节点
		List<Map<String, Object>> nodeList = directoryList.stream().filter(a -> a.get("id").equals(folderId))
				.collect(Collectors.toList());
		for (Map<String, Object> map : nodeList) {
			System.out.println("nodeList: " + map);
		}
		if (nodeList == null || nodeList.size() == 0) {
			return;
		}
		Map<String, Object> node = nodeList.get(0);
		if (!"0".equals(node.get("parentId"))) { // 当前父级节点不为零就递归
			resultMap.add(node);
			buildParentDirectory(node.get("parentId").toString(), directoryList, resultMap);
		} else { // 将顶级目录添加到集合中
			resultMap.add(node);
		}
	}

	/**
	 * 删除云盘权限
	 * @param folderId
	 * @param dirId
	 * @param fileId
	 * @param childrenDir
	 * @param traineeIds
	 * @param params
	 * @param allList
	 */
	private void emptySomestudent(String folderId, String dirId, String fileId, List<Map<String, Object>> childrenDir,
			JSONArray traineeIds, Map<String, Object> params, List<Map<String, Object>> allList) {
		// ③如果有学员但要清空有此文件/文件夹共享权限的学员的情况
		List<Map<String, Object>> traineeIdList = new ArrayList<>();
		if (folderId != null && folderId != "") {
			params.clear();
			params.put("dirId", folderId);
			// 首先查找文件夹的父级文件夹
			traineeIdList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
		} else {
			params.clear();
			params.put("dirId", dirId);
			// 首先查找文件的父级文件夹
			traineeIdList = tcloudPanFileMapper.selectParentIdByfileIdAnddirId(params);
			System.out.println("删除文件的操作： ");
		}

		if (fileId == null || fileId == "") {
			// 处理有共享权限的子集目录或文件
			// childrenDir = handleChildrendirectory(folderId, childrenList ,params);
			buildChildDirectory(folderId, allList, childrenDir);
			for (Map<String, Object> map : childrenDir) {
				System.out.println("子级目录及文件：" + map);
			}
			if (childrenDir != null && childrenDir.size() > 0) { // 有子级目录
				// 将有共享权限的文件夹添加到集合中
				childrenDir.addAll(traineeIdList);
			} else { // 没有子级目录
				handleDeleteDir(traineeIdList, params, traineeIds);
			}
		} else { // 删除当前文件的权限
			params.clear();
			params.put("fileId", fileId);
			params.put("traineeIds", traineeIds);
			params.put("emptyFileId", "fileId");
			List<Map<String, Object>> deleteFiles = tcloudPanPermissionsMapper.selectByTraineeIds(params);
			if (deleteFiles != null && deleteFiles.size() > 0) {
				List<Object> perIds = deleteFiles.stream().map(a -> a.get("perId")).collect(Collectors.toList());
				String[] perIdArray = perIds.stream().toArray(String[]::new);
				tcloudPanPermissionsMapper.deleteBatch(perIdArray);
			}
		}
		// 删除当前目录及子目录的记录
		if (childrenDir != null && childrenDir.size() > 0) {
			System.out.println("删除子级目录及文件的操作： ");
			for (Map<String, Object> map : childrenDir) {
				System.out.println("要删除的子级目录及文件：" + map);
			}
			handleDeleteDir(childrenDir, params, traineeIds);
		}
	}

	/**
	 * 有子级目录的删除子级目录，没有子级目录的删除当前目录
	 * 
	 * @param params
	 * @param traineeIds 学员id
	 */
	private void handleDeleteDir(List<Map<String, Object>> deleteDirList, Map<String, Object> params,
			JSONArray traineeIds) {
		List<Object> ids = deleteDirList.stream().map(a -> a.get("id")).collect(Collectors.toList());
		params.clear();
		params.put("dirIds", ids);
		params.put("traineeIds", traineeIds);
		// 查找除这些学员之外的学员的云盘权限的记录
		List<Map<String, Object>> cloudpansList = tcloudPanPermissionsMapper.selectPerIdByDirIds(params);
		if (cloudpansList != null && cloudpansList.size() > 0) {
			List<Object> perIds = cloudpansList.stream().map(a -> a.get("perId")).collect(Collectors.toList());
			String[] perIdArray = perIds.stream().toArray(String[]::new);
			// 最后删除这些记录
			tcloudPanPermissionsMapper.deleteBatch(perIdArray);
		}
	}

	/**
	 * 找到此目录的所有子级目录
	 * 
	 * @param folderId     父级目录id
	 * @param childrenList 存储子目录集合
	 * @param params       查询参数
	 * @return
	 */
	/*private List<Map<String, Object>> handleChildrendirectory(String folderId, List<Map<String,Object>> childrenList, Map<String, Object> params) {
		List<Object> parentIds = new ArrayList<>();
		parentIds.add(folderId);
		return subrecursion(parentIds, childrenList, params);
	}*/

	/**
	 * 递归处理子级目录
	 * 
	 * @param dirId        父级目录id
	 * @param childrenList 存储子目录集合
	 * @param params
	 * @return
	 */
	/*private List<Map<String, Object>> subrecursion(List<Object> dirIds,
			List<Map<String, Object>> childrenList, Map<String, Object> params) {
		// 通过目录id查找父级目录是目录id的文件及文件夹
		params.clear();
		params.put("parentIds", dirIds);
		List<Map<String, Object>> childNodeList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
		if (childNodeList == null || childNodeList.size() == 0) {
			return childNodeList;
		}
		// 通过子目录id查找子目录下的文件
		params.clear();
		params.put("dirIds", dirIds);
		List<Map<String, Object>> fileList = tcloudPanFileMapper.selectParentIdByfileIdAnddirId(params);
		if (fileList != null && fileList.size() > 0) {
			childrenList.addAll(fileList);
		}
		childrenList.addAll(childNodeList);
		// 查找子目录下是否有子目录,如果有,把子目录下的文件查出来
		if (childNodeList.size() > 0) {
			List<Object> dirIdList = childNodeList.stream().map(a -> a.get("dirId")).collect(Collectors.toList());
			params.clear();
			params.put("dirIds", dirIdList);
			List<Map<String, Object>> subdirectoryFile = tcloudPanFileMapper.selectParentIdByfileIdAnddirId(params);
			if (subdirectoryFile != null && subdirectoryFile.size() > 0) {
				childrenList.addAll(subdirectoryFile);
			}
		}
		List<Object> parentIds = childNodeList.stream().map(a -> a.get("dirId")).distinct().collect(Collectors.toList());
		// 递归查找子目录下的目录
		if (parentIds.size() > 0) {
			subrecursion(parentIds, childrenList, params);
		}
		
		return childrenList;
	}*/

	/**
	 * 找到此目录或者此文件的所有上级目录
	 * 
	 * @param directoryList 此目录或者此文件的上一级目录
	 * @param params
	 */
	/*private List<Map<String, Object>> handleParentdirectory(String dirId, List<Map<String, Object>> directoryList, List<Map<String,Object>> directoryMap, Map<String, Object> params) {
	    if (directoryList == null || directoryList.size() == 0) {
	        return directoryList;
	    }
	    // 取出当前目录节点
	    List<Map<String, Object>> directoryNode = directoryList.stream().filter(a -> a.get("dirId").equals(dirId)).distinct().collect(Collectors.toList());
	    if (directoryNode == null || directoryNode.size() == 0) {
	        return directoryNode;
	    }
	    Map<String, Object> node = directoryNode.get(0);
	    String parentId = node.get("dirId").toString();
	    return recursion(parentId, directoryList, directoryMap, params);
	}*/

	/**
	 * 递归处理父级目录
	 * 
	 * @param parentId
	 * @param directoryList
	 * @param directoryMap
	 * @param params
	 * @return
	 */
	/*private List<Map<String, Object>> recursion(String parentId, List<Map<String, Object>> directoryList, List<Map<String,Object>> directoryMap, Map<String, Object> params) {
		// 把父级文件夹id当前文件夹id，查询对应的文件夹的记录
	    params.clear();
	    params.put("dirId", parentId);
	    List<Map<String, Object>> parentNodeList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
	    if (parentNodeList == null || parentNodeList.size() == 0) {
	        return directoryList;
	    }
	    Map<String, Object> parentNode = parentNodeList.get(0);
	    directoryMap.add(parentNode);
	    recursion(parentNode.get("parentId").toString(), directoryList, directoryMap, params);
	    return directoryMap;
	}*/

	/**
	 * 权限校验
	 * @param folderId
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	private R permissionCheckIsPass(String folderId, String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId)) {
			return R.error("必传参数为空");
		}

		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("无效的教学包，操作失败");
		}
		if (!"Y".equals(pkgInfo.getState())) {
			return R.error("该教学包已失效，操作失败");
		}
		if (!loginUserId.equals(pkgInfo.getCreateUserId())) {
			return R.error("当前登录人没有权限，无效操作");
		}
		if ("Y".equals(pkgInfo.getPrivateUse())) {
			return R.error("抱歉，您只能自己使用，而不能再次进行授权");
		}
		return R.ok();
	}

	/**
	 * 查看云盘权限
	 * 
	 * @author zhouyl加
	 * @date 2021年1月26日
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryPermissions(JSONObject jsonObject, String loginUserId) {
		// 当前课堂
		String ctId = jsonObject.getString("ctId");
		// 目录id或文件id
		String id = jsonObject.getString("id");
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(ctId) || StrUtils.isEmpty(ctId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("数据获取未成功");
		}
		/*if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
			return R.error("非法操作");
		}*/
		boolean hasAuth = roomUtils.hasOperatingAuthorization(tevglTchClassroom, loginUserId);
		if (!hasAuth) {
			return R.error("非法操作，没有权限");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(tevglTchClassroom.getPkgId());
		if (tevglPkgInfo == null) {
			return R.error("数据获取失败");
		}
		// 查询参数
		Map<String, Object> params = new HashMap<>();
		// 搜索条件
		String traineeName = jsonObject.getString("traineeName");
		String mobile = jsonObject.getString("mobile");
		// 查当前课堂的审核通过的学生
		params.put("ctId", ctId);
		params.put("traineeName", traineeName);
		params.put("mobile", mobile);
		params.put("loginUserId", loginUserId);
		List<Map<String,Object>> traineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 权限
		List<String> pkgIdList = new ArrayList<>();
		pkgIdList.add(tevglPkgInfo.getPkgId());
		if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
			if (!pkgIdList.contains(tevglPkgInfo.getRefPkgId())) {
				pkgIdList.add(tevglPkgInfo.getRefPkgId());
			}
		}
		/*TcloudPanDirectory tcloudPanDirectory = tcloudPanDirectoryMapper.selectObjectById(id);
		List<String> ids = new ArrayList<>();
		if (tcloudPanDirectory != null) {
			ids.add(id);
			if (StrUtils.isNotEmpty(tcloudPanDirectory.getRefDirId())) {
				ids.add(tcloudPanDirectory.getRefDirId());
			}
		}*/
		params.clear();
		params.put("id", id);
		// params.put("ids", ids);
		params.put("pkgIds", pkgIdList);
		List<Map<String, Object>> list = tcloudPanPermissionsMapper.selectListMapByMap(params);
		// 处理当前文件/文件夹是否授权给了学员
		if (list != null && list.size() > 0) {
			list.stream().forEach(permission -> {
				if (permission.get("owner") != null) {
					permission.put(is_cloud_permission, true);
				} else {
					permission.put(is_cloud_permission, false);
				}
			});
		}
		List<Object> traineeIdList = list.stream().map(a -> a.get("owner")).distinct().collect(Collectors.toList());
		List<Object> dirIds = list.stream().map(a -> a.get("dir_id")).distinct().collect(Collectors.toList());
		data.put("permissionDirectory", dirIds);
		data.put("traineeIdList", traineeIdList);
		if (traineeList != null && traineeList.size() > 0) {
			for (Map<String, Object> a : traineeList) {
				a.put("traineePic", uploadPathUtils.stitchingPath(a.get("traineePic") , "16"));
				traineeIdList.stream().forEach(traineeId -> {
					if (a.get("traineeId").equals(traineeId)) {
						a.put("isSelected", true);
					}
				});
			}
		}
		data.put("traineeList", traineeList);
		return R.ok().put(Constant.R_DATA, data);
	}

	/**
	 * 设置学员不可见
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R setStudentNotVisible( JSONObject jsonObject, String loginUserId) {
		String ctId = jsonObject.getString("ctId");
		String pkgId = jsonObject.getString("pkgId");
		JSONArray dirList = jsonObject.getJSONArray("dirList");
		if (dirList == null || dirList.size() == 0) {
			return R.error("请选择要授权的目录");
		}
		TevglPkgInfo pkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (pkgInfo == null) {
			return R.error("教学包不存在");
		}
		// 存储云盘权限的记录
		List<TcloudPanPermissions> list = new ArrayList<>();
		// 记录当前目录及子目录的集合
		List<Map<String, Object>> resultMap = new ArrayList<>();
		// 子级目录
		List<Map<String, Object>> childrenDir = new ArrayList<>();
		// 查询对象
		Map<String, Object> params = new HashMap<>();
		// 通过课堂id查找课堂信息，从而找到课堂下的所有学员
		params.put("ctId", ctId);
		List<TevglTchClassroomTrainee> classroomTrainees = tevglTchClassroomTraineeMapper.selectListByMap(params);
		List<String> traineeIds = new ArrayList<>();
		if (classroomTrainees.size() > 0) {
			traineeIds = classroomTrainees.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
		}
		if (traineeIds == null || traineeIds.size() == 0) {
			return R.error("该课堂没有学员，请添加学员后重试");
		}
		for (int i = 0; i < dirList.size(); i++) {
			JSONObject dirJSON = dirList.getJSONObject(i);
			String dirId = dirJSON.getString("dirId"); // 目录id
			String isTraineeVisible = dirJSON.getString("isTraineeVisible"); // 是否允许可见
			Boolean refPkgDir = dirJSON.getBooleanValue("refPkgDir"); // 是否是教学包的目录true：是false：否
			if ("N".equals(isTraineeVisible)) { // 设置学员不可见
				deleteDirAndSubdirectory(dirId, pkgId, resultMap, childrenDir, params);
				// 修改字段为不可见
				TcloudPanDirectory directory = new TcloudPanDirectory();
				directory.setDirId(dirId);
				directory.setIsTraineeVisible("N");
				tcloudPanDirectoryMapper.updateDirectory(directory);
			} else if ("Y".equals(isTraineeVisible)) {
				/*// 根据目录id和教学包id查找目录信息
				if (refPkgDir == true) {
					params.put("pkgId", pkgInfo.getRefPkgId());
				}else {
					params.put("pkgId", pkgId);
				}
				if (childrenList.size() > 0) { // 如果集合不为空则清空,避免数据冗余
					childrenList.clear();
				}
				// 查找此目录下的所有子目录及文件
				List<Map<String, Object>> childrendirectoryList = handleChildrendirectory(dirId, childrenList, params);*/
				// 根据当前课堂的教学包id查询当前课堂下的所有目录及文件
				params.clear();
				params.put("pkgId", pkgId);
				List<Map<String, Object>> allList = tcloudPanDirectoryMapper.queryAllDirAndFile(params);
				buildChildDirectory(dirId, allList, childrenDir);

				// 来源教学包的目录及文件
				List<Map<String, Object>> refDirectory = new ArrayList<>();
				TcloudPanDirectory panDirectory = tcloudPanDirectoryMapper.selectObjectById(dirId);
				if (StrUtils.isNotEmpty(panDirectory.getRefDirId())) {
					TcloudPanDirectory directory = tcloudPanDirectoryMapper
							.selectObjectById(panDirectory.getRefDirId());
					params.clear();
					// 根据来源教学包id查询来源目录及文件
					params.put("pkgId", directory.getPkgId());
					refDirectory = tcloudPanDirectoryMapper.queryAllDirAndFile(params);
					if (StrUtils.isNotEmpty(panDirectory.getRefDirId())) {
						buildChildDirectory(panDirectory.getRefDirId(), refDirectory, childrenDir);
					}
				}
				if (traineeIds != null && traineeIds.size() > 0) {
					for (int j = 0; j < traineeIds.size(); j++) {
						String traineeId = traineeIds.get(j);
						// 给当前文件/文件夹填充信息
						TcloudPanPermissions t = new TcloudPanPermissions();
						t.setPerId(Identities.uuid());
						t.setOwner(traineeId);
						if (refPkgDir == true) {
							t.setPkgId(pkgInfo.getRefPkgId());
						} else {
							t.setPkgId(pkgId);
						}
						t.setDirId(dirId);
						t.setCurrentPkgId(pkgId);
						t.setFileId(null);
						t.setIsPermission("Y");
						t.setCreateUserId(loginUserId);
						t.setCreateTime(DateUtils.getNowTimeStamp());
						// 查询这些人的云盘权限的文件或文件夹，控制不重复插入记录
						params.clear();
						params.put("owner", traineeId);
						if (refPkgDir == true) {
							params.put("pkgId", pkgInfo.getRefPkgId());
						} else {
							params.put("pkgId", pkgId);
						}
						params.put("dirId", dirId);
						List<Map<String, Object>> cloudPanCatalog = tcloudPanPermissionsMapper
								.selectPerIdByDirIds(params);
						if (cloudPanCatalog == null || cloudPanCatalog.size() == 0) {
							// 将授权的文件/文件夹添加到集合中
							list.add(t);
						}

						if (childrenDir != null && childrenDir.size() > 0) {
							/*if (refDirectoryList.size() > 0) {
								childrenDir.addAll(refDirectoryList);
							}*/
							handleFillData(childrenDir, traineeId, refPkgDir, pkgInfo, loginUserId, params, list, dirId,
									pkgId);
						} /*else if (childrenDir.size() == 0 && refDirectoryList.size() > 0) { // 当前课堂目录没有子目录但教学包带过来的目录有
							handleFillData(refDirectoryList, traineeId, refPkgDir, pkgInfo, loginUserId, params, list, dirId, pkgId);
							} */
						else { // 没有子目录的情况
								// 如果没有记录则新增
							if (list != null && list.size() != 0) {
								// 去除重复元素
								for (int m = 0; m < list.size(); m++) {// 循环list
									for (int n = m + 1; n < list.size(); n++) {
										if (list.get(m).getDirId().equals(list.get(n).getDirId())) {
											list.remove(m);// 删除一样的元素
											m--;
											break;
										}
									}
								}
								params.clear();
								params.put("list", list);
								tcloudPanPermissionsMapper.insertBatch(params);
								// 修改字段为可见
								TcloudPanDirectory directory = new TcloudPanDirectory();
								directory.setDirId(dirId);
								directory.setIsTraineeVisible("Y");
								tcloudPanDirectoryMapper.updateDirectory(directory);
							}

							list.clear();
						}
					}
				}
			}
		}
		return R.ok("设置成功");
	}

	/**
	 * 填充数据
	 * 
	 * @param directoryList 当前目录及子目录的集合
	 * @param traineeId     学员id
	 * @param refPkgDir     是否是教学包带过来的目录 Y是N否
	 * @param pkgInfo       教学包信息
	 * @param loginUserId
	 * @param params        查询参数
	 * @param list          目录权限数据
	 * @param dirId         目录id
	 * @param pkgId         教学包id
	 */
	private void handleFillData(List<Map<String, Object>> directoryList, String traineeId, Boolean refPkgDir,
			TevglPkgInfo pkgInfo, String loginUserId, Map<String, Object> params, List<TcloudPanPermissions> list,
			String dirId, String pkgId) {
		for (Map<String, Object> child : directoryList) {
			TcloudPanPermissions permissions = new TcloudPanPermissions();
			permissions.setPerId(Identities.uuid());
			permissions.setOwner(traineeId);
			if (refPkgDir == true) {
				permissions.setPkgId(pkgInfo.getRefPkgId());
			} else {
				permissions.setPkgId(pkgId);
			}
			permissions.setDirId(child.get("dirId").toString());
			if (child.get("fileId") == null) { // 给文件夹授权
				permissions.setFileId(null);
			} else { // 给文件授权
				permissions.setFileId(child.get("fileId").toString());
			}
			permissions.setCurrentPkgId(pkgId);
			permissions.setIsPermission("Y");
			permissions.setCreateUserId(loginUserId);
			permissions.setCreateTime(DateUtils.getNowTimeStamp());
			// 根据文件夹id和用户id查询云盘权限表中是否有这条记录
			params.clear();
			params.put("dirId", child.get("dirId"));
			params.put("owner", traineeId);
			if (refPkgDir == true) {
				params.put("pkgId", pkgInfo.getRefPkgId());
			} else {
				params.put("pkgId", pkgId);
			}
			if (child.get("fileId") != null) {
				params.put("fileId", child.get("fileId"));
			}
			List<Map<String, Object>> subDirectory = tcloudPanPermissionsMapper.selectPerIdByDirIds(params);
			if (subDirectory == null || subDirectory.size() == 0) {
				list.add(permissions);
			}
			// 如果没有记录则新增
			if (list != null && list.size() != 0) {
				// 去除重复元素
				for (int m = 0; m < list.size(); m++) {// 循环list
					for (int n = m + 1; n < list.size(); n++) {
						if (list.get(m).getDirId().equals(list.get(n).getDirId())) {
							list.remove(m);// 删除一样的元素
							m--;
							break;
						}
					}
				}
				params.clear();
				params.put("list", list);
				tcloudPanPermissionsMapper.insertBatch(params);
				// 修改子目录字段为可见
				TcloudPanDirectory directory = new TcloudPanDirectory();
				directory.setDirId(child.get("dirId").toString());
				directory.setIsTraineeVisible("Y");
				tcloudPanDirectoryMapper.updateDirectory(directory);
				// 修改主目录字段为可见
				TcloudPanDirectory d = new TcloudPanDirectory();
				d.setDirId(dirId);
				d.setIsTraineeVisible("Y");
				tcloudPanDirectoryMapper.updateDirectory(d);
			}
			// 清空集合
			list.clear();
		}
	}

	/**
	 * 查看有权限的目录
	 * 
	 * @author zhouyl加
	 * @date 2021年3月9日
	 * @param ctId        课堂id
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryDirectoryDisplay(String ctId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("数据异常，请重试");
		}
		// 不是课堂创建者，直接返回
		if (!loginUserId.equals(classroom.getCreateUserId())) {
			return R.ok().put(Constant.R_DATA, new ArrayList<>());
		}
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 查询对象
		Map<String, Object> params = new HashMap<>();
		params.put("pkgId", classroom.getPkgId());
		List<Map<String, Object>> directoryDisplayList = tcloudPanDirectoryMapper.selectTopDirectoryList(params);
		List<TevglPkgInfo> refPkgList = tevglPkgInfoMapper.selectListByMap(params);
		List<Map<String, Object>> refPkgDirList = new ArrayList<>();
		if (refPkgList != null && refPkgList.size() > 0) {
			params.clear();
			params.put("pkgId", refPkgList.get(0).getRefPkgId());
			refPkgDirList = tcloudPanDirectoryMapper.selectTopDirectoryList(params);
		}
		refPkgDirList.addAll(directoryDisplayList);
		// 去除重复元素
		if (refPkgDirList != null && refPkgDirList.size() > 0) {
			for (int i = 0; i < refPkgDirList.size(); i++) {// 循环list
				for (int j = i + 1; j < refPkgDirList.size(); j++) {
					if (refPkgDirList.get(i).get("name").equals(refPkgDirList.get(j).get("name"))) {
						refPkgDirList.remove(i);// 删除一样的元素
						i--;
						break;
					}
				}
			}
		}
		refPkgDirList.stream().forEach(refDir -> {
			if (classroom.getPkgId().equals(refDir.get("pkgId"))) {
				refDir.put("refPkgDir", false);
			} else {
				refDir.put("refPkgDir", true);
			}
		});

		params.clear();
		params.put("pkgId", classroom.getPkgId());
		params.put("ctId", ctId);
		List<Map<String, Object>> list = tcloudPanNavigationBarMapper.querNavBarList(params);
		data.put("directoryDisplayList", directoryDisplayList);
		data.put("fastList", list.stream().map(a -> a.get("dirId")).distinct().collect(Collectors.toList()));
		if (refPkgDirList != null && refPkgDirList.size() > 0) {
			data.put("refPkgDirList", refPkgDirList);
		}
		return R.ok().put(Constant.R_DATA, data);
	}

	@Override
	public R queryPermissionForWx(String ctId, String id, String traineeName, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(id) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("数据获取未成功");
		}
		/*if (!loginUserId.equals(tevglTchClassroom.getCreateUserId())) {
			return R.error("非法操作");
		}*/
		boolean hasAuth = roomUtils.hasOperatingAuthorization(tevglTchClassroom, loginUserId);
		if (!hasAuth) {
			return R.error("非法操作，没有权限");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(tevglTchClassroom.getPkgId());
		if (tevglPkgInfo == null) {
			return R.error("数据获取失败");
		}
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 查询条件
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("state", "Y");
		if (!StrUtils.isNull(traineeName)) {
			params.put("traineeName", traineeName);
		}
		List<Map<String, Object>> traineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		// 权限
		List<String> pkgIdList = new ArrayList<>();
		pkgIdList.add(tevglPkgInfo.getPkgId());
		if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
			if (!pkgIdList.contains(tevglPkgInfo.getRefPkgId())) {
				pkgIdList.add(tevglPkgInfo.getRefPkgId());
			}
		}
		params.clear();
		params.put("id", id);
		params.put("pkgIds", pkgIdList);
		List<TcloudPanPermissions> list = tcloudPanPermissionsMapper.selectListByMap(params);
		log.debug("是否有权限呢？：" + list.size());
		List<String> traineeIdList = list.stream().map(a -> a.getOwner()).distinct().collect(Collectors.toList());
		data.put("traineeList", traineeList); // 课堂全部有效学员
		data.put("traineeIdList", traineeIdList); // 拥有权限的学员
		return R.ok().put(Constant.R_DATA, data);
	}

	/**
	 * 获取树形的目录结构
	 * 
	 * @author zhouyl加
	 * @date 2021年4月19日
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R getTreeDataForAuth(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return null;
		}
		// 查询归属在教学包目录下的文件
		List<String> pkgIdList = new ArrayList<>();
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		// 子级目录及文件集合
		List<Map<String, Object>> childrenDir = new ArrayList<>();
		// 查询对象
		Map<String, Object> params = new HashMap<>();
        // 获取当前课堂的权限权限记录
        params.put("currentPkgId", pkgId);
        List<TcloudPanPermissions> allPermissionList = tcloudPanPermissionsMapper.selectListByMap(params);
        params.clear();
		params.put("pkgId", pkgId);
		params.put("createUserId", loginUserId);
		List<TevglTchClassroom> classrooms = tevglTchClassroomMapper.selectListByMap(params);
		params.clear();
		List<TevglTraineeInfo> trainees = tevglTraineeInfoMapper.selectListByMap(params);
		if (classrooms != null && classrooms.size() > 0) {
			if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
				handlePkgDirectoryAndFile(pkgIdList, tevglPkgInfo, childrenDir, trainees, data, params);
			}
		}
		// 查找教学包下的所有文件
		params.clear();
		params.put("dirId", pkgId);
		List<Map<String, Object>> topFileList = tcloudPanFileMapper.selectSimpleInfoList(params);
		if (topFileList != null && topFileList.size() > 0) {
			// 如果文件在全部目录下，则直接归属到教学包下
			topFileList.stream().forEach(currentFile -> {
				if (pkgId.equals(currentFile.get("parentId"))) {
					currentFile.put("pkgId", currentFile.get("parentId"));
				}
			});
		}
		// 查找用户创建的所有目录及文件
		params.clear();
		params.put("createUserId", loginUserId);
		params.put("pkgId", pkgId);
		params.put("sidx", "t.dictCode");
		params.put("order", "asc, t.updateTime desc");
		List<Map<String, Object>> currentDirectoryList = tcloudPanDirectoryMapper.selectListByUnionAll(params);
		if (currentDirectoryList != null && currentDirectoryList.size() > 0) {
			currentDirectoryList.stream().forEach(node -> {
				// 标识是当前课堂的目录及文件还是教学包带过来的
				if (pkgId.equals(node.get("pkgId"))) {
					node.put("isCurrent", true);
				} else {
					node.put("isCurrent", false);
				}
				// 处理当前授权了哪些人
	            if ("1".equals(node.get("type"))) {
	                List<TcloudPanPermissions> thisNodePermissionList = allPermissionList.stream().filter(a -> StrUtils.isNotEmpty(a.getDirId()) && a.getDirId().equals(node.get("id"))).collect(Collectors.toList());
	                List<String> traineeIdLsit = thisNodePermissionList.stream().map(a -> a.getOwner()).distinct().collect(Collectors.toList());
	                node.put("traineeIdLsit", traineeIdLsit);
	            }
	            if ("2".equals(node.get("type"))) {
	                List<TcloudPanPermissions> thisNodePermissionList = allPermissionList.stream().filter(a ->  StrUtils.isNotEmpty(a.getFileId()) &&  a.getFileId().equals(node.get("id"))).collect(Collectors.toList());
	                List<String> traineeIdLsit = thisNodePermissionList.stream().map(a -> a.getOwner()).distinct().collect(Collectors.toList());
	                node.put("traineeIdLsit", traineeIdLsit);
	            }
			});
			// 递归构建目录结构树
			List<Map<String, Object>> result = buildChildDirectory("0", currentDirectoryList, childrenDir, trainees, pkgId);
			if (topFileList.size() > 0) {
				topFileList.stream().forEach(node -> {
					node.put("isCurrent", true);
					if ("2".equals(node.get("type"))) {
		                List<TcloudPanPermissions> thisNodePermissionList = allPermissionList.stream().filter(a ->  StrUtils.isNotEmpty(a.getFileId()) &&  a.getFileId().equals(node.get("id"))).collect(Collectors.toList());
		                List<String> traineeIdLsit = thisNodePermissionList.stream().map(a -> a.getOwner()).distinct().collect(Collectors.toList());
		                node.put("traineeIdLsit", traineeIdLsit);
		            }
				});
				result.addAll(topFileList);
			}
			data.put("currentDirectoryList", result);
		}
		return R.ok().put(Constant.R_DATA, data);
	}

	/**
	 * 处理教学包的目录和文件
	 * 
	 * @param pkgIdList    教学包id的集合
	 * @param tevglPkgInfo 当前教学包的信息
	 * @param childrenDir  子级目录及文件
	 * @param trainees     所有学员
	 * @param data         返回的数据
	 * @param params
	 */
	private Map<String, Object> handlePkgDirectoryAndFile(List<String> pkgIdList, TevglPkgInfo tevglPkgInfo,
			List<Map<String, Object>> childrenDir, List<TevglTraineeInfo> trainees, Map<String, Object> data,
			Map<String, Object> params) {
		pkgIdList.add(tevglPkgInfo.getRefPkgId());
		// 查找来源教学包全部目录下的文件
		params.clear();
		params.put("dirIds", pkgIdList);
		List<Map<String, Object>> refTopFileList = tcloudPanFileMapper.selectSimpleInfoList(params);
		if (refTopFileList != null && refTopFileList.size() > 0) {
			// 如果文件在全部目录下，则直接归属到教学包下
			refTopFileList.stream().forEach(refFile -> {
				if (refFile.get("parentId").equals(tevglPkgInfo.getRefPkgId())) {
					refFile.put("pkgId", refFile.get("parentId"));
				}
			});
		}
		// 查询来源教学包非全部目录下的目录及文件
		params.clear();
		params.put("pkgId", tevglPkgInfo.getRefPkgId());
		params.put("sidx", "t.dictCode");
		params.put("order", "asc, t.updateTime desc");
		List<Map<String, Object>> refDirectoryList = tcloudPanDirectoryMapper.selectListByUnionAll(params);
		/*params.clear();
		params.put("pkgId", tevglPkgInfo.getRefPkgId());
		List<Map<String, Object>> panList = tcloudPanPermissionsMapper.selectOwnerByFileId(params);*/
		if (refDirectoryList != null && refDirectoryList.size() > 0) {
			List<Map<String, Object>> directoryList = buildChildDirectory("0", refDirectoryList, childrenDir, trainees,
					tevglPkgInfo.getPkgId());
			if (directoryList.size() > 0 && refTopFileList.size() > 0) {
				directoryList.addAll(refTopFileList);
				directoryList.stream().forEach(ref -> {
					// 标识是当前课堂的目录及文件还是教学包带过来的
					if (!ref.get("pkgId").equals(tevglPkgInfo.getPkgId())) {
						ref.put("isCurrent", false);
					} else {
						ref.put("isCurrent", true);
					}
				});
			}

			data.put("refDirectoryList", directoryList);
		}
		return data;
	}

	/**
	 * 构建递归数：列出当前目录的所有子级目录
	 * @param parentId
	 * @param directoryList
	 * @param childrenDir
	 * @param trainees
	 * @param pkgId
	 * @return
	 */
	private List<Map<String, Object>> buildChildDirectory(String parentId, List<Map<String, Object>> directoryList,
			List<Map<String, Object>> childrenDir, List<TevglTraineeInfo> trainees, String pkgId) {
		// 存储有权限的学员
		// List<Map<String, Object>> traineeList = new ArrayList<>();
		if (directoryList == null || directoryList.size() == 0) {
			return directoryList;
		}
		// 取出父级目录节点
		List<Map<String, Object>> nodeList = directoryList.stream().filter(a -> a.get("parentId").equals(parentId))
				.collect(Collectors.toList());
		if (nodeList == null || nodeList.size() == 0) {
			return nodeList;
		}

		for (int i = 0; i < nodeList.size(); i++) {
			Map<String, Object> node = nodeList.get(i);
			childrenDir.add(node);
			List<Map<String, Object>> list = buildChildDirectory(node.get("id").toString(), directoryList, childrenDir,
					trainees, pkgId);
			if (list != null && list.size() > 0) {
				node.put("children", list);
			} else {
				node.put("children", null);
			}
			/*if (panList != null && panList.size() > 0) {
				// 取出所有的目录及文件
				List<Map<String, Object>> collect = panList.stream().filter(a -> a.get("dirId").equals(node.get("id"))).distinct().collect(Collectors.toList());
				
				// 递归查找子节点
				List<Map<String,Object>> list = buildChildDirectory(node.get("id").toString(), directoryList, panList, childrenDir, trainees, pkgId);
				if (list != null && list.size() > 0) {
					if (collect.size() > 0) {
						for (Map<String,Object> tcloudPanPermissions : collect) {
							// 存储学员的对象
							Map<String, Object> data = new HashMap<>();
							data.put("traineeId", tcloudPanPermissions.get("owner"));
							List<TevglTraineeInfo> result = trainees.stream().filter(a -> a.getTraineeId().equals(tcloudPanPermissions.get("owner"))).distinct().collect(Collectors.toList());
							String traineeName = result.get(0).getTraineeName() == null ? result.get(0).getNickName() : result.get(0).getTraineeName();
							data.put("traineeName", traineeName);
							if (!traineeList.contains(data)) {
								traineeList.add(data);
							}
						}
					}
					list.stream().forEach(a -> {
						a.put("traineeList", traineeList);
						if (pkgId.equals(a.get("pkgId"))) {
							a.put("isCurrent", true);
						} else {
							a.put("isCurrent", false);
						}
					});
					node.put("children", list);
				} else {
					node.put("children", null);
				}
				
				if (collect.size() > 0) {
					node.put("traineeList", traineeList);
				}
			}*/

		}
		return nodeList;
	}

	/**
	 * @author zhouyl加
	 * @date 2021年4月19日 保存设置
	 * @param jsonObject  示例 {"currentPkgId": "", "traineeIdList" :
	 *                    ["03e9aebed6c14545b3b73f105f734da6",
	 *                    "033ccc3e7bf1491b8ae0b6b0d39f00d0"] "list": [{"type": "1",
	 *                    "id": "2997b2d8df5f434c9fe7ac31393674e8", "pkgId": ""},
	 *                    {"type": "1", "id": "3b4153b849f94c428e5d31592ee417b1",
	 *                    "pkgId": ""}, {"type": "1", "id":
	 *                    "99243b55e6b446fc9d936ed00555dd13", "pkgId": ""}, {"type":
	 *                    "2", "id": "58a79c286583412ab38eb8a4ac9de176", "pkgId":
	 *                    ""}] }
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R saveAuth( JSONObject jsonObject, String loginUserId) {
		// 当前课堂教学包id
		String currentPkgId = jsonObject.getString("pkgId");
		// 被授权的学生
		JSONArray traineeIdList = jsonObject.getJSONArray("traineeIdList"); // 学员id集合
		JSONArray list = jsonObject.getJSONArray("list");
		if (traineeIdList == null || traineeIdList.size() == 0) {
			return R.error("请选择授权的目录给哪些人");
		}
		// 存储权限数据
		List<TcloudPanPermissions> panList = new ArrayList<>();
		// 查询对象
		Map<String, Object> params = new HashMap<>();

		System.out.println("traineeIdList: " + traineeIdList);
		for (int i = 0; i < traineeIdList.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				String traineeId = traineeIdList.getString(i); // 学员id
				JSONObject authorizeData = list.getJSONObject(j);
				String type = authorizeData.getString("type"); // 要授权的是目录还是文件 1 目录 2 文件
				String id = authorizeData.getString("id"); // 目录id或文件id
				String pkgId = authorizeData.getString("pkgId"); // 当前教学包id或来源教学包

				if ("1".equals(type)) { // 给目录授权
					// 填充信息
					TcloudPanPermissions panPermissions = new TcloudPanPermissions();
					panPermissions.setPerId(Identities.uuid());
					panPermissions.setOwner(traineeId);
					panPermissions.setPkgId(pkgId);
					panPermissions.setCurrentPkgId(currentPkgId);
					panPermissions.setDirId(id);
					panPermissions.setIsPermission("Y");
					panPermissions.setCreateUserId(loginUserId);
					panPermissions.setCreateTime(DateUtils.getNowTimeStamp());
					panList.add(panPermissions);
					params.put("dirId", id);
					params.put("pkgId", pkgId);
					params.put("owner", traineeId);
					List<TcloudPanPermissions> data = tcloudPanPermissionsMapper.selectListByMap(params);
					if (data == null || data.size() == 0) {
						// 入库
						params.clear();
						params.put("list", panList);
						tcloudPanPermissionsMapper.insertBatch(params);
					}
					// 清空集合
					panList.clear();
				} else { // 给文件授权
					// 填充信息
					TcloudPanPermissions panPermissions = new TcloudPanPermissions();
					panPermissions.setPerId(Identities.uuid());
					panPermissions.setOwner(traineeId);
					panPermissions.setPkgId(pkgId);
					panPermissions.setCurrentPkgId(currentPkgId);
					panPermissions.setFileId(id);
					panPermissions.setIsPermission("Y");
					panPermissions.setCreateUserId(loginUserId);
					panPermissions.setCreateTime(DateUtils.getNowTimeStamp());
					panList.add(panPermissions);
					params.put("dirId", id);
					params.put("pkgId", pkgId);
					params.put("owner", traineeId);
					List<TcloudPanPermissions> data = tcloudPanPermissionsMapper.selectListByMap(params);
					if (data == null || data.size() == 0) {
						// 入库
						params.clear();
						params.put("list", panList);
						tcloudPanPermissionsMapper.insertBatch(params);
					}
					// 清空集合
					panList.clear();
				}
			}
		}

		return R.ok("授权成功");
	}
	
	@Override
    public R saveAuthNew(JSONObject jsonObject, String loginUserId){
        // 当前课堂教学包id
        String currentPkgId = jsonObject.getString("pkgId");
        // 当前选择的节点们
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray == null || jsonArray.size() == 0) {
            return R.error("请选择");
        }
        TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(currentPkgId);
        if (tevglPkgInfo == null) {
            return R.error("无法授权");
        }
        // 先获取当前课堂的权限权限记录
        Map<String, Object> params = new HashMap<>();
        params.put("currentPkgId", currentPkgId);
        List<TcloudPanPermissions> allPermissionList = tcloudPanPermissionsMapper.selectListByMap(params);
        // 先查出所有目录和文件
        List<Map<String, Object>> allDataList = getAllDataList(tevglPkgInfo);
        // 等待入库的权限
        List<TcloudPanPermissions> insertList = new ArrayList<>();
        // 遍历用户选择的
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject node = jsonArray.getJSONObject(i);
            // 类型：1目录2文件
            String type = node.getString("type");
            // 主键：目录id或文件id
            String id = node.getString("id");
            // 当前节点所设置的学生
            JSONArray traineeIdArray = node.getJSONArray("traineeIds");
            // 标识
            String savePkgId = null;
            String dirId = null;
            String fileId = null;
            // 取出当前节点的完整信息
            List<Map<String, Object>> dataList = allDataList.stream().filter(a -> a.get("id").equals(id)).collect(Collectors.toList());
            log.debug("是否有完整的节点信息：" + dataList.size());
            if (dataList == null || dataList.size() == 0) {
                continue;
            }
            Map<String, Object> data = dataList.get(0);
            // 先清除该节点的权限
            // 再重新为选择的学员生成该节点的权限（因为权限树形组件，会传递父子数据，无需递归）
            log.debug("类型{}", type);
            // 目录的情况
            if ("1".equals(type)) {
                List<TcloudPanPermissions> thisNodePermissionList = allPermissionList.stream().filter(a -> StrUtils.isNotEmpty(a.getDirId()) && a.getDirId().equals(node.get("id"))).collect(Collectors.toList());
                if (thisNodePermissionList != null && thisNodePermissionList.size() > 0) {
                    List<String> perIdList = thisNodePermissionList.stream().map(a -> a.getPerId()).distinct().collect(Collectors.toList());
                    tcloudPanPermissionsMapper.deleteBatch(perIdList.stream().toArray(String[]::new));
                }
                dirId = id;
                savePkgId = data.get("pkgId").toString();
            }
            // 文件的情况
            if ("2".equals(type)) {
                List<TcloudPanPermissions> thisNodePermissionList = allPermissionList.stream().filter(a ->  StrUtils.isNotEmpty(a.getFileId()) &&  a.getFileId().equals(node.get("id"))).collect(Collectors.toList());
                if (thisNodePermissionList != null && thisNodePermissionList.size() > 0) {
                    List<String> perIdList = thisNodePermissionList.stream().map(a -> a.getPerId()).distinct().collect(Collectors.toList());
                    tcloudPanPermissionsMapper.deleteBatch(perIdList.stream().toArray(String[]::new));
                }
                fileId = id;
                dirId = data.get("parentId").toString();
                savePkgId = data.get("pkgId").toString();
                //log.debug("主键{},父级{},名称：{}", data.get("id"), data.get("parentId"), data.get("name"));
            }
            // 遍历学生
            if (traineeIdArray != null) {
                for (int j = 0; j < traineeIdArray.size(); j++) {
                    String traineeId = traineeIdArray.getString(j);
                    // 填充信息
                    TcloudPanPermissions panPermissions = new TcloudPanPermissions();
                    panPermissions.setPerId(Identities.uuid());
                    panPermissions.setOwner(traineeId);
                    panPermissions.setPkgId(savePkgId);
                    panPermissions.setCurrentPkgId(currentPkgId);
                    panPermissions.setDirId(dirId);
                    panPermissions.setFileId(fileId);
                    panPermissions.setIsPermission("Y");
                    panPermissions.setCreateUserId(loginUserId);
                    panPermissions.setCreateTime(DateUtils.getNowTimeStamp());
                    insertList.add(panPermissions);
                }
            }
        }
        if (insertList.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("list", insertList);
            tcloudPanPermissionsMapper.insertBatch(map);
        }
        return R.ok("设置成功");
    }
	
	private List<Map<String, Object>> getAllDataList(TevglPkgInfo tevglPkgInfo){
        List<String> pkgIdList = new ArrayList<>();
        pkgIdList.add(tevglPkgInfo.getPkgId());
        if (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId())) {
            pkgIdList.add(tevglPkgInfo.getRefPkgId());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("pkgIds", pkgIdList);
        params.put("sidx", "t.dictCode");
        params.put("order", "asc, t.updateTime desc");
        params.put("queryTopFile", "Y");
        List<Map<String, Object>> dataList = tcloudPanDirectoryMapper.selectListByUnionAllNew(params);
        dataList.stream().forEach(node -> {
            if (node.get("parentId").equals(tevglPkgInfo.getPkgId()) || (StrUtils.isNotEmpty(tevglPkgInfo.getRefPkgId()) && node.get("parentId").equals(tevglPkgInfo.getRefPkgId()))) {
                node.put("pkgId", node.get("parentId"));
            }
        });
        return dataList;
    }
}
