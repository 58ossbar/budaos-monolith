package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysDictMapper;
import com.budaos.modules.job.persistence.TsysOrgMapper;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.modules.sys.api.TsysDictService;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典管理接口实现类
 * 
 * @author huangwb
 * @data 2019-05-06 8:44
 *
 */
@Service
public class TsysDictServiceImpl implements TsysDictService {
	@Autowired
	private TsysDictMapper tsysDictMapper;

	@Autowired
	private ConvertUtil convertUtil;

	@Autowired
	private TsysAttachService tsysAttachService;

	@Autowired
	private TsysOrgMapper tsysOrgMapper;

	private final String FILE_TYPE = "1";

	@Autowired
	private ServiceLoginUtil serviceLoginUtil;

	/**
	 * 根据条件查询数据,用于前端数据操作
	 * 
	 * @author huangwb
	 * @param parentType
	 * @return
	 * @throws Exception
	 */
	@Override
	@Cacheable(value = "dict_cache", key = "'dictByDictType_'+#parentType")
	public R dictByDictType(String parentType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentType", parentType);
		//map.put("displaySort", "2");// 树型结构
		return R.ok().put("data", tsysDictMapper.selectListByMap(map));
	}

	/**
	 * 
	 * 删除
	 * 
	 * @author huangwb
	 * @param ids
	 * @return R
	 */
	@Transactional
	@Override
	@CacheEvict(value = "dict_cache", allEntries = true)
	public R deleteType(@RequestBody String[] ids) {
		// 判断是否有字典值或按钮
		List<TsysDict> dictList = tsysDictMapper.selectListParentId(ids[0]);
		if (dictList.size() > 0) {
			return R.error("请先删除目录下的所有字典");
		}
		tsysDictMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 
	 * 保存修改操作
	 * 
	 * @author huangwb
	 * @param tsysDict
	 * @return R
	 */
	@Transactional
	@Override
	@CacheEvict(value = "dict_cache", allEntries = true)
	public R saveOrUpdate(TsysDict tsysDict, String attachId) {
		try {
			String updateState = "1";
			String pkId = null;
			if (tsysDict.getDictId() != null && StrUtils.isNotEmpty(tsysDict.getDictId())) {
				tsysDict.setUpdateTime(DateUtils.getNowTimeStamp());
				TsysDict dict = tsysDictMapper.selectObjectById(tsysDict.getDictId());
				pkId = tsysDict.getDictId();
				tsysDict.setUpdateUserId(serviceLoginUtil.getLoginUserId());
				tsysDictMapper.update(tsysDict);
				// 如果是左侧字典目录的修改 则将字典目录下的数据dictName也同时修改
				if (tsysDict.getParentType().equals("0") && !dict.getDictName().equals(tsysDict.getDictName())
						|| !dict.getDictType().equals(tsysDict.getDictType())) {
					tsysDictMapper.selectListParentId(dict.getDictId()).stream().forEach(d -> {
						d.setUpdateTime(DateUtils.getNowTimeStamp());
						if (tsysDict.getParentType().equals("0")
								&& !dict.getDictName().equals(tsysDict.getDictName())) {
							d.setDictName(tsysDict.getDictName());
						}
						if (!dict.getDictType().equals(tsysDict.getDictType())) {
							d.setDictType(tsysDict.getDictType());
						}
						tsysDictMapper.update(d);
					});
				}
				updateState = "0";
			} else {
				tsysDict.setCreateUserId(serviceLoginUtil.getLoginUserId());
				pkId = Identities.uuid();
				tsysDict.setDictId(pkId);
				tsysDict.setCreateTime(DateUtils.getNowTimeStamp());
				tsysDictMapper.insert(tsysDict);
			}
			// 保证附件的异常不会事务回滚用户字典保存操作
			try {
				tsysAttachService.updateAttach(attachId, pkId, updateState, FILE_TYPE);
			} catch (Exception e) {
			}
			if (tsysDict.getOrgId() != null && StringUtils.isNotBlank(tsysDict.getOrgId())) {
				tsysDict.setOrgName(tsysOrgMapper.selectObjectById(tsysDict.getOrgId()).getOrgName());
			}
			return R.ok().put("data", tsysDict);
		} catch (Exception e) {
			return R.error(e.getMessage());
		}
	}

	/**
	 * 
	 * 查询操作
	 * 
	 * @author huangwb
	 * @param params (page页码,limit显示条数)
	 * 
	 * @return R
	 */
	@Override
	@Cacheable(value="dict_cache", key = "'cb_' + #params.get('parentType')")
	public R query(@RequestParam Map<String, Object> params) {
		params.put("sidx", "update_time");
		params.put("order", "desc");
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TsysDict> tsysDictList = tsysDictMapper.selectListByMapNotZero(query);
		tsysDictList.stream().forEach(a -> {
			if (a.getOrgId() != null && StringUtils.isNotBlank(a.getOrgId())) {
				a.setOrgName(tsysOrgMapper.selectObjectById(a.getOrgId()).getOrgName());
			}
		});
		Map<String, String> map = new HashMap<String, String>();
		map.put("displaying", "displaying");
		map.put("displaySort", "displaySort");
		map.put("isdefault", "isdefault");
		convertUtil.convertParam(tsysDictList, map);
		PageUtils pageUtil = new PageUtils(tsysDictList, query.getPage(), query.getLimit());
		return R.ok().put("data", pageUtil);
	}

	/**
	 * 
	 * 获取字典详情信息
	 * 
	 * @author huangwb
	 * @param dictId
	 * @return R
	 */
	@Override
	public R getDctInfo(String dictId) {
		return R.ok().put("data", tsysDictMapper.selectObjectById(dictId));
	}

	/**
	 * 获取字典表中系统的所有字典信息
	 * 
	 * @return
	 */
	@Override
	public R selectAllTsysDict() {
		return R.ok().put("data", tsysDictMapper.selectAllTsysDict());
	}

	@Override
	public R selectListByMapNotZero(Map<String, Object> map) {
		return R.ok().put("data", tsysDictMapper.selectListByMapNotZero(map));
	}

	/**
	 * 查询指定父字典id的所有数据
	 * 
	 * @param parentId
	 * @return
	 */
	@Override
	public R selectListParentId(String parentId) {
		return R.ok().put("data", tsysDictMapper.selectListParentId(parentId));
	}

	/**
	 * 根据条件查询数据
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public R dicttree(String dictname) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentType", "0");
		map.put("dictName", dictname);
		return R.ok().put("data", tsysDictMapper.selectListByMap(map));
	}
}
