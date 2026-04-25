package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysLogMapper;
import com.budaos.modules.sys.api.TsysLogService;
import com.budaos.modules.sys.domain.TsysLog;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统日志api的实现类
 * <p>Title: TsysLogServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p>
 * @author huj
 * @date 2019年5月6日
 */
@Service
public class TsysLogServiceImpl implements TsysLogService {

	@Autowired
	private TsysLogMapper tsysLogMapper;
	
	/**
	 * <p>根据条件查询站点列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param params
	 * @return
	 */
	@Override
	public R query( Map<String, Object> params) {
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(), query.getLimit());
		List<TsysLog> list = tsysLogMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put("data", pageUtil);
	}

	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param sysLog
	 * @return
	 */
	@Override
	public R save(TsysLog sysLog) {
		try {
			sysLog.setId(Identities.uuid());
			tsysLogMapper.insert(sysLog);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("新增失败");
		}
		return R.ok("新增成功");
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param sysLog
	 * @return
	 */
	@Override
	public R update(TsysLog sysLog) {
		try {
			tsysLogMapper.update(sysLog);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("修改失败");
		}
		return R.ok("修改成功");
	}

	/**
	 * <p>单挑删除</p>
	 * @author huj
	 * @data 2019年5月23日
	 * @param id
	 * @return
	 */
	@Override
	public R delete( String id) {
		tsysLogMapper.delete(id);
		return R.ok("删除成功");
	}

	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param ids
	 * @return
	 */
	@Override
	public R deleteBatch( String[] ids) {
		tsysLogMapper.deleteBatch(ids);
		return R.ok("删除成功");
	}

	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param id
	 * @return
	 */
	@Override
	public R view( String id) {
		TsysLog tsysLog = tsysLogMapper.selectObjectById(id);
		return R.ok().put("data", tsysLog);
	}

}
