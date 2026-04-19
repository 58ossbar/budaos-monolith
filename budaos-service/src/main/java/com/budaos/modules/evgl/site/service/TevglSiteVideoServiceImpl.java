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
import com.budaos.modules.evgl.site.api.TevglSiteVideoService;
import com.budaos.modules.evgl.site.domain.TevglSiteVideo;
import com.budaos.modules.evgl.site.domain.TevglSiteVideoMgr;
import com.budaos.modules.evgl.site.domain.TevglSiteVideoRelation;
import com.budaos.modules.evgl.site.persistence.TevglSiteVideoMapper;
import com.budaos.modules.evgl.site.persistence.TevglSiteVideoMgrMapper;
import com.budaos.modules.evgl.site.persistence.TevglSiteVideoRelationMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Arrays;
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
public class TevglSiteVideoServiceImpl implements TevglSiteVideoService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteVideoServiceImpl.class);
	@Autowired
	private TevglSiteVideoMapper tevglSiteVideoMapper;
	@Autowired
	private TevglSiteVideoRelationMapper tevglSiteVideoRelationMapper;
	@Autowired
	private TevglSiteVideoMgrMapper tevglSiteVideoMgrMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
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
		List<TevglSiteVideo> tevglSiteVideoList = tevglSiteVideoMapper.selectListByMap(query);
		tevglSiteVideoList.stream().forEach(item -> {
			item.setName(uploadPathUtils.stitchingPath(item.getName(), "26"));
		});
		convertUtil.convertDict(tevglSiteVideoList, "type", "video_type");
		convertUtil.convertUserId2RealName(tevglSiteVideoList, "sysCreateUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglSiteVideoList,query.getPage(),query.getLimit());
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
		List<Map<String, Object>> tevglSiteVideoList = tevglSiteVideoMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteVideoList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglSiteVideoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteVideo
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save( TevglSiteVideo tevglSiteVideo) throws BudaosException {
		if (StrUtils.isEmpty(tevglSiteVideo.getVideoId())) {
			tevglSiteVideo.setVideoId(Identities.uuid());
		}
		tevglSiteVideo.setSysCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteVideo.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteVideo);
		tevglSiteVideoMapper.insert(tevglSiteVideo);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteVideo
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglSiteVideo tevglSiteVideo) throws BudaosException {
	    ValidatorUtils.check(tevglSiteVideo);
		tevglSiteVideoMapper.update(tevglSiteVideo);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglSiteVideoMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		Map<String, Object> map = new HashMap<>();
		map.put("videoIdList", Arrays.asList(ids));
		List<TevglSiteVideo> list = tevglSiteVideoMapper.selectListByMap(map);
		list.stream().forEach(tevglSiteVideo -> {
			doDeleteFile(tevglSiteVideo);
		});
		tevglSiteVideoMapper.deleteBatch(ids);
		return R.ok("删除成功");
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		TevglSiteVideo tevglSiteVideo = tevglSiteVideoMapper.selectObjectById(id);
		if (tevglSiteVideo != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("videoId", tevglSiteVideo.getVideoId());
			List<TevglSiteVideoMgr> list = tevglSiteVideoMgrMapper.selectListByMap(map);
			List<String> majorIdList = list.stream().map(a -> a.getMajorId()).collect(Collectors.toList());
			tevglSiteVideo.setMajorIdList(majorIdList);
			tevglSiteVideo.setMajorId(majorIdList.stream().collect(Collectors.joining(",")));
		}
		return R.ok().put(Constant.R_DATA, tevglSiteVideo);
	}

	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	@Override
	@SysLog(value="根据条件查询记录")
	public R queryVideoList( Map<String, Object> params) {
		Object createUserId = params.get("createUserId");
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglSiteVideoList = tevglSiteVideoMapper.queryVideoList(query);
		tevglSiteVideoList.stream().forEach(item -> {
			item.put("name", uploadPathUtils.stitchingPath(item.get("name"), "26"));
			item.put("isCreator", false);
			if (StrUtils.notNull(item.get("createUserId")) && StrUtils.notNull(createUserId) && createUserId.equals(item.get("createUserId"))) {
				item.put("isCreator", true);
			}
		});
		convertUtil.convertDict(tevglSiteVideoList, "type", "video_type");
		PageUtils pageUtil = new PageUtils(tevglSiteVideoList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	@Override
	public R saveVideo(TevglSiteVideo tevglSiteVideo) {
		if (StrUtils.isEmpty(tevglSiteVideo.getVideoId())) {
			tevglSiteVideo.setVideoId(Identities.uuid());
		}
		tevglSiteVideo.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteVideo);
		tevglSiteVideoMapper.insert(tevglSiteVideo);
		return R.ok();
	}

	/**
	 * 删除视频
	 * @param videoId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R deleteVideo(String videoId, String loginUserId) {
		if (StrUtils.isEmpty(videoId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("必传参数为空");
		}
		TevglSiteVideo tevglSiteVideo = tevglSiteVideoMapper.selectObjectById(videoId);
		if (tevglSiteVideo == null) {
			return R.error("删除成功");
		}
		if (!loginUserId.equals(tevglSiteVideo.getCreateUserId())) {
			return R.error("非法操作");
		}
		// 从磁盘上删除该视频
		doDeleteFile(tevglSiteVideo);
		// 数据库中删除记录
		tevglSiteVideoMapper.delete(videoId);
		// 删除关系
		Map<String, Object> map = new HashMap<>();
		map.put("videoId", videoId);
		List<TevglSiteVideoRelation> list = tevglSiteVideoRelationMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			List<String> idList = list.stream().map(a -> a.getVrId()).collect(Collectors.toList());
			tevglSiteVideoRelationMapper.deleteBatch(idList.stream().toArray(String[]::new));
		}
		return R.ok("删除成功");
	}
	
	private void doDeleteFile(TevglSiteVideo tevglSiteVideo) {
		if (StrUtils.isNotEmpty(tevglSiteVideo.getName())) {
			String path = uploadPathUtils.getAbsolutePath("26") + "/" + tevglSiteVideo.getName();
			File file = new File(path);
			if (file != null && file.exists() && file.isFile()) {
				file.delete();
			}
			// 删除第一帧图片
			String val = uploadPathUtils.getUploadPath().substring(0, uploadPathUtils.getUploadPath().indexOf("/uploads"));
			String uri = val + tevglSiteVideo.getFirstCaptureSavePath();
			File file2 = new File(uri);
			if (file2.exists() && file2.isFile()) {
				file2.delete();
			}
		}
	}

	@Override
	public R viewVideo(String videoId) {
		TevglSiteVideo tevglSiteVideo = tevglSiteVideoMapper.selectObjectById(videoId);
		if (tevglSiteVideo != null) {
			tevglSiteVideo.setName(uploadPathUtils.stitchingPath(tevglSiteVideo.getName(), "26"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("videoId", tevglSiteVideo.getVideoId());
			List<TevglSiteVideoRelation> list = tevglSiteVideoRelationMapper.selectListByMap(map);
			tevglSiteVideo.setCtIdList(list.stream().map(a -> a.getCtId()).collect(Collectors.toList()));
		}
		return R.ok().put(Constant.R_DATA, tevglSiteVideo);
	}

	@Override
	public TevglSiteVideo selectObjectById(Object id) {
		return tevglSiteVideoMapper.selectObjectById(id);
	}

	
}
