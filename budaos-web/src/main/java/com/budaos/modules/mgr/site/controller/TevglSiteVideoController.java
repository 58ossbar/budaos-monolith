package com.budaos.modules.mgr.site.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadUtils;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.site.api.TevglSiteVideoMgrService;
import com.budaos.modules.evgl.site.api.TevglSiteVideoService;
import com.budaos.modules.evgl.site.domain.TevglSiteVideo;
import com.budaos.modules.evgl.site.domain.TevglSiteVideoMgr;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

@RestController
@RequestMapping("/api/site/tevglsitevideo")
public class TevglSiteVideoController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteVideoController.class);
	@Autowired
	private TevglSiteVideoService tevglSiteVideoService;
	@Autowired
	private TevglSiteVideoMgrService tevglSiteVideoMgrService;
	@Autowired
	private UploadUtils uploadUtils;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('site:tevglsitevideo:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglSiteVideoService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('site:tevglsitevideo:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglSiteVideoService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitevideo:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglSiteVideoService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('site:tevglsitevideo:add') or hasAuthority('site:tevglsitevideo:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestPart(name="file") MultipartFile file, TevglSiteVideo tevglSiteVideo) {
		R r = checkIsPass(file, tevglSiteVideo);
		if (!r.get("code").equals(0)) {
			return r;
		}
		if(StrUtils.isEmpty(tevglSiteVideo.getVideoId())) { //新增
			return doSave(file, tevglSiteVideo);
		} else {
			return doUpdate(file, tevglSiteVideo);
		}
	}
	
	/**
	 * 实际新增
	 * @param multipartFile
	 * @param tevglSiteVideo
	 * @return
	 */
	private R doSave(MultipartFile multipartFile, TevglSiteVideo tevglSiteVideo) {
		R r = uploadUtils.uploads(multipartFile, 26, false);
		if (!r.get("code").equals(0)) {
			return r;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>)r.get(Constant.R_DATA);
		String fileName = (String) data.get("fileName");
		String originalFilename = (String) data.get("originalFilename");
		Long fileSize = (Long)data.get("fileSize");
		String fileSuffix = (String) data.get("fileSuffix");
		String firstCaptureAccessUrl = StrUtils.isNull(data.get("firstCaptureAccessUrl")) ? null : data.get("firstCaptureAccessUrl").toString();
		String firstCaptureSavePath = StrUtils.isNull(data.get("firstCaptureSavePath")) ? null : data.get("firstCaptureSavePath").toString();
		tevglSiteVideo.setVideoId(Identities.uuid());
		tevglSiteVideo.setTitle(StrUtils.isEmpty(tevglSiteVideo.getTitle()) ? originalFilename : tevglSiteVideo.getTitle());
		tevglSiteVideo.setType("2");
		tevglSiteVideo.setName(fileName);
		tevglSiteVideo.setFileSuffix(fileSuffix);
		tevglSiteVideo.setFileSize(fileSize);
		tevglSiteVideo.setOriginalFilename(originalFilename);
		tevglSiteVideo.setCheckState("0");
		tevglSiteVideo.setState("Y");
		tevglSiteVideo.setCreateTime(DateUtils.getNowTimeStamp());
		tevglSiteVideo.setFirstCaptureAccessUrl(firstCaptureAccessUrl);
		tevglSiteVideo.setFirstCaptureSavePath(firstCaptureSavePath);
		// 入库
		tevglSiteVideoService.save(tevglSiteVideo);
		// 视频与职业路径关系入库
		List<TevglSiteVideoMgr> insertList = new ArrayList<>();
		tevglSiteVideo.getMajorIdList().stream().forEach(majorId -> {
			TevglSiteVideoMgr t = new TevglSiteVideoMgr();
			t.setVmId(Identities.uuid());
			t.setVideoId(tevglSiteVideo.getVideoId());
			t.setMajorId(majorId);
			insertList.add(t);
		});
		if (insertList.size() > 0) {
			tevglSiteVideoMgrService.insertBatch(insertList);
		}
		return R.ok("保存成功");
	}
	
	private R doUpdate(MultipartFile multipartFile, TevglSiteVideo tevglSiteVideo){
		if (multipartFile != null) {
			R r = uploadUtils.uploads(multipartFile, 26, false);
			if (!r.get("code").equals(0)) {
				return r;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>)r.get(Constant.R_DATA);
			String fileName = (String) data.get("fileName");
			String originalFilename = (String) data.get("originalFilename");
			Long fileSize = (Long)data.get("fileSize");
			String fileSuffix = (String) data.get("fileSuffix");
			String firstCaptureAccessUrl = StrUtils.isNull(data.get("firstCaptureAccessUrl")) ? null : data.get("firstCaptureAccessUrl").toString();
			String firstCaptureSavePath = StrUtils.isNull(data.get("firstCaptureSavePath")) ? null : data.get("firstCaptureSavePath").toString();
			tevglSiteVideo.setTitle(StrUtils.isEmpty(tevglSiteVideo.getTitle()) ? originalFilename : tevglSiteVideo.getTitle());
			tevglSiteVideo.setName(fileName);
			tevglSiteVideo.setFileSuffix(fileSuffix);
			tevglSiteVideo.setFileSize(fileSize);
			tevglSiteVideo.setOriginalFilename(originalFilename);
			tevglSiteVideo.setCheckState("0");
			tevglSiteVideo.setState("Y");
			tevglSiteVideo.setCreateTime(DateUtils.getNowTimeStamp());
			tevglSiteVideo.setFirstCaptureAccessUrl(firstCaptureAccessUrl);
			tevglSiteVideo.setFirstCaptureSavePath(firstCaptureSavePath);
			// TODO 删除原来的视频与第一帧图片
			TevglSiteVideo oldVideo = tevglSiteVideoService.selectObjectById(tevglSiteVideo.getVideoId());
			if (oldVideo != null) {
				String videUrl = uploadUtils.getUploadPath() + uploadUtils.getPathByParaNo("26") + "/" + oldVideo.getName();
				String uploadPath = uploadUtils.getUploadPath();
				String val = uploadPath.substring(0, uploadPath.indexOf("/uploads"));
				String oldFirstCaptureSavePath = val + oldVideo.getFirstCaptureSavePath();
				File oldVideoFile = new File(videUrl);
				File oldFirstCaptureFile = new File(oldFirstCaptureSavePath);
				log.debug("删除videUrl：" + videUrl);
				log.debug("oldFirstCaptureSavePath:" + oldFirstCaptureSavePath);
				// 磁盘上删除
				if (oldVideoFile.exists() && oldVideoFile.isFile()) {
					oldVideoFile.delete();
				}
				if (oldFirstCaptureFile.exists() && oldFirstCaptureFile.isFile()) {
					oldFirstCaptureFile.delete();
				}
			}
		}
		tevglSiteVideo.setState("Y");
		tevglSiteVideo.setCheckState("0");
		tevglSiteVideo.setUpdateTime(DateUtils.getNowTimeStamp());
		tevglSiteVideoService.update(tevglSiteVideo);
		// 删除旧关系
		Map<String, Object> map = new HashMap<>();
		map.put("videoId", tevglSiteVideo.getVideoId());
		List<TevglSiteVideoMgr> list = tevglSiteVideoMgrService.selectListByMap(map);
		if (list != null && list.size() > 0) {
			List<String> idList = list.stream().map(a -> a.getVmId()).collect(Collectors.toList());
			tevglSiteVideoMgrService.deleteBatch(idList.stream().toArray(String[]::new));
		}
		// 再建立新的
		if (tevglSiteVideo.getCtIdList() != null && tevglSiteVideo.getCtIdList().size() > 0) {
			// 视频与职业路径关系入库
			List<TevglSiteVideoMgr> insertList = new ArrayList<>();
			tevglSiteVideo.getMajorIdList().stream().forEach(majorId -> {
				TevglSiteVideoMgr t = new TevglSiteVideoMgr();
				t.setVmId(Identities.uuid());
				t.setVideoId(tevglSiteVideo.getVideoId());
				t.setMajorId(majorId);
				insertList.add(t);
			});
			tevglSiteVideoMgrService.insertBatch(insertList);
		}
		return R.ok();
	}
	
	/**
	 * 合法性校验
	 * @param multipartFile
	 * @param tevglSiteVideo
	 * @return
	 */
	private R checkIsPass(MultipartFile multipartFile, TevglSiteVideo tevglSiteVideo) {
		if (multipartFile == null || multipartFile.isEmpty()) {
			return R.error("请上传视频");
		}
		if (tevglSiteVideo.getMajorIdList() == null && tevglSiteVideo.getMajorIdList().size() == 0) {
			return R.error("请选择职业路径");
		}
		return R.ok();
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('site:tevglsitevideo:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglSiteVideoService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('site:tevglsitevideo:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglSiteVideoService.deleteBatch(ids);
	}
}
