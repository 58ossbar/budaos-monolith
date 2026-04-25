package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysAttachMapper;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.modules.sys.domain.TsysAttach;
import com.budaos.modules.sys.query.AttachQuery;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 附件管理api的实现类
 * </p>
 * <p>
 * Title: TsysAttachServiceImpl.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company: 布道师学习通
 * </p>
 * 
 * @author huj
 * @date 2019年5月9日
 */
@Service
public class TsysAttachServiceImpl implements TsysAttachService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private TsysAttachMapper tsysAttachMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;

	/**
	 * <p>
	 * 根据条件查询记录
	 * </p>
	 * 
	 * @author huangwb
	 * @data 2019年5月29日
	 * @param params
	 * @return
	 */
	@Override
	public R query( Map<String, Object> params) {
		params.put("sidx", "create_time");
		params.put("order", "desc");
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TsysAttach> tsysDictList = tsysAttachMapper.selectListByMap(params);
		Map<String, String> map = new HashMap<String, String>();
		map.put("fileType", "fileType");
		map.put("fileState", "fileState");
		convertUtil.convertParam(tsysDictList, map);
		convertUtil.convertUserId2RealName(tsysDictList, "createUserId");
		PageUtils pageUtil = new PageUtils(tsysDictList, query.getPage(), query.getLimit());
		return R.ok().put("data", pageUtil);
	}

	/**
	 * <p>
	 * 执行数据保存和修改
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysAttach
	 * @return
	 */
	@Override
	public R saveOrUpdate(TsysAttach tsysAttach) {
		try {
			// 数据校验
			// ValidatorUtils.validateEntity(tsysAttach);
			if (Strings.isNullOrEmpty(tsysAttach.getAttachId())) { // 新增
				save(tsysAttach);
			} else {
				update(tsysAttach);
			}
		} catch (Exception e) {
			e.printStackTrace();
			R.error("操作失败");
		}
		return R.ok().put("data", tsysAttach);
	}

	/**
	 * <p>
	 * 新增
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysAttach
	 */
	@Override
	public void save(TsysAttach tsysAttach) {
		tsysAttach.setCreateTime(DateUtils.getNowTimeStamp());
		tsysAttach.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tsysAttachMapper.insert(tsysAttach);
	}

	/**
	 * <p>
	 * 修改
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysAttach
	 */
	@Override
	public void update(TsysAttach tsysAttach) {
		tsysAttach.setUpdateTime(DateUtils.getNowTimeStamp());
		tsysAttach.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		tsysAttachMapper.update(tsysAttach);
	}

	/**
	 * <p>
	 * 删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public R delete(String id) {
		TsysAttach attach = tsysAttachMapper.selectObjectById(id);
		if (!Strings.isNullOrEmpty(attach.getFileUrl())) {
			new File(attach.getFileUrl()).delete();
		}
		tsysAttachMapper.delete(id);
		return R.ok();
	}

	/**
	 * <p>
	 * 批量删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional
	public R deleteBatch(String[] ids) {
		Arrays.stream(ids).forEach(a -> {
			TsysAttach attach = tsysAttachMapper.selectObjectById(a);
			if (attach.getFileUrl() != null && StrUtils.isNotEmpty(attach.getFileUrl())) {
				new File(attach.getFileUrl()).delete();
			}
		});
		tsysAttachMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * <p>
	 * 查看明细
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	@Override
	public R view(String id) {
		return R.ok().put("data", tsysAttachMapper.selectObjectById(id));
	}

	/**
	 * 文件上传时候保存附件记录，根据前端组件的特性,需要在实际点击保存的时候获取attach对象进行id的绑定。
	 * 
	 * @author hwb
	 * @data 2019年5月30日
	 * @param oldNamePc      原始名称
	 * @param extensionPc    文件后缀
	 * @param fileSize       文件大小
	 * @param imgNamePc      链接地址
	 * @param longNamePc     文件名称
	 * @param fileType       文件类型 1、字典
	 * @return attachId 附件id
	 */
	@Override
	@Transactional(propagation = Propagation.NESTED)
	public String uploadFileInsertAttach(String oldNamePc, String extensionPc, String fileSize, String imgNamePc,
			String longNamePc, String fileType, String uploadUserId) {
		try {
			TsysAttach tsysAttach = new TsysAttach();
			// 文件后缀
			tsysAttach.setFileSuffix(extensionPc);
			// 文件名称
			tsysAttach.setFileUrl(longNamePc);
			// 链接地址
			tsysAttach.setLjUrl(imgNamePc);
			// 文件大小
			tsysAttach.setFileSize(fileSize);
			// 文件状态 0未绑定
			tsysAttach.setFileState("0");
			// 文件原始名称
			tsysAttach.setFileName(oldNamePc);
			// 设置文件为字典类型
			tsysAttach.setFileType(fileType);
			// 创建用户ID
			tsysAttach.setCreateUserId(uploadUserId);
			tsysAttach.setCreateTime(DateUtils.getNowTimeStamp());
			String uuid = Identities.uuid();
			tsysAttach.setAttachId(uuid);
			tsysAttachMapper.insert(tsysAttach);
			return uuid;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 资源上传成功后保存时修改文件绑定关联状态（旧版）
	 * 
	 * @param attachId 附件id
	 * @param type     删除还是修改 0为修改 1为添加
	 * @param fileType 附件类型 1为字典
	 * @param pkId     关联id
	 */
	@Override
	@Transactional(propagation = Propagation.NESTED)
	@Deprecated
	public void updateAttach(String attachId, String pkId, String type, String fileType) {
		if (attachId != null && StrUtils.isNotEmpty(attachId)) {
			TsysAttach attach = tsysAttachMapper.selectObjectById(attachId);
			if (attach == null) {
				return;
			}
			// 修改attach链接的id
			attach.setPkid(pkId);
			// 设置成绑定状态
			attach.setFileState("1");
			attach.setUpdateTime(DateUtils.getNowTimeStamp());
			tsysAttachMapper.update(attach);
			// 如果为修改方法 拿到绑定的id获取绑定id有关的文件
			if (!type.equals("1")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pkid", pkId);
				map.put("fileType", fileType);
				// 文件状态为绑定状态
				map.put("fileState", "1");
				List<TsysAttach> attachs = tsysAttachMapper.selectListByMap(map);
				attachs.stream().forEach(a -> {
					if (a.getAttachId().equals(attachId)) {
						return;
					}
					log.debug("被删除的文件：" + a.getFileUrl());
					if (StrUtils.isNotEmpty(a.getFileUrl())) {
						File file = new File(a.getFileUrl());
						if (file.exists() && file.isFile()) {
							file.delete();
						}
					}
					tsysAttachMapper.delete(a.getAttachId());
				});
			}
		}
	}

	/**
	 * （绑定操作）资源上传成功后保存时修改文件绑定关联状态
	 * @param attachId 必传参数，附件表t_sys_attach主键id
	 * @param pkId 必传参数，关联id，被关联对象表的主键id，示例：假如是新增学员时，且上传了附件，该值就是学员表主键id
	 * @param fileType 必传参数，下标位置，属性文件中，com.budaos.cb-upload-paths对应的下标
	 */
	@Override
	public void updateAttachForAdd(String attachId, String pkId, String fileType) {
		if (StrUtils.isEmpty(attachId) || StrUtils.isEmpty(pkId) || StrUtils.isEmpty(fileType)) {
			return;
		}
		TsysAttach attach = tsysAttachMapper.selectObjectById(attachId);
		if (attach == null) {
			return;
		}
		// 修改attach链接的id
		attach.setPkid(pkId);
		// 设置成绑定状态，0未绑定状态，1绑定状态
		attach.setFileState("1");
		// 更新入库
		attach.setUpdateTime(DateUtils.getNowTimeStamp());
		attach.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tsysAttachMapper.update(attach);
	}

	/**
	 * （绑定新的，解绑旧的）资源上传成功后保存时修改文件绑定关联状态
	 * @param attachId 附件表t_sys_attach主键id
	 * @param pkId 关联id，被关联对象表的主键id，示例：假如是新增学员时，且上传了附件，该值就是学员表主键id
	 * @param fileType 下标位置，属性文件中，com.budaos.cb-upload-paths对应的下标
	 */
	@Override
	public void updateAttachForEdit(String attachId, String pkId, String fileType) {
		if (StrUtils.isEmpty(attachId) || StrUtils.isEmpty(pkId) || StrUtils.isEmpty(fileType)) {
			return;
		}
		TsysAttach attach = tsysAttachMapper.selectObjectById(attachId);
		if (attach == null) {
			return;
		}
		// 修改attach链接的id
		attach.setPkid(pkId);
		// 设置成绑定状态，0未绑定状态，1绑定状态
		attach.setFileState("1");
		// 更新入库
		attach.setUpdateUserId(serviceLoginUtil.getLoginUserId());
		attach.setUpdateTime(DateUtils.getNowTimeStamp());
		tsysAttachMapper.update(attach);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkid", pkId);
		map.put("fileType", fileType);
		// 文件状态为绑定状态
		map.put("fileState", "1");
		List<TsysAttach> attachs = tsysAttachMapper.selectListByMap(map);
		attachs.stream().forEach(a -> {
			if (a.getAttachId().equals(attachId)) {
				return;
			}
			log.debug("被删除的文件：" + a.getFileUrl());
			if (StrUtils.isNotEmpty(a.getFileUrl())) {
				File file = new File(a.getFileUrl());
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}
			tsysAttachMapper.delete(a.getAttachId());
		});
	}
	

    /**
     * 解绑关系，未绑定的附件，就可以直接物理删除了
     *
     * @param pkId
     * @param fileType
     * @return
     */
    @Override
    public boolean unBind(String pkId, String fileType) {
        return unBind(Arrays.asList(pkId), fileType);
    }

    /**
     * 解绑关系，未绑定的附件，就可以直接物理删除了
     *
     * @param pkIdList
     * @param fileType
     * @return
     */
    @Override
    public boolean unBind(List<String> pkIdList, String fileType) {
        if (pkIdList == null || pkIdList.isEmpty() || StrUtils.isEmpty(fileType)) {
            return false;
        }
        AttachQuery query = new AttachQuery();
        query.setPkIds(pkIdList);
        query.setFileType(fileType);
        List<String> attachIds = tsysAttachMapper.findAttachIds(query);
        if (attachIds != null && !attachIds.isEmpty()) {
            tsysAttachMapper.unBind(attachIds);
        }
        return true;
    }

    /**
     * 解绑关系，未绑定的附件，就可以直接物理删除了
     *
     * @param pkIds
     * @param fileType
     * @return
     */
    @Override
    public boolean unBind(String[] pkIds, String fileType) {
        List<String> collect = Stream.of(pkIds).collect(Collectors.toList());
        return unBind(collect, fileType);
    }
}
