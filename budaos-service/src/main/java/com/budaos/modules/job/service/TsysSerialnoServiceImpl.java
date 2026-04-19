package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysSerialnoMapper;
import com.budaos.modules.sys.api.TsysSerialnoService;
import com.budaos.modules.sys.domain.TsysSerialno;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务流水号自定义api的实现类
 * <p>Title: TsysSerialnoServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年5月6日
 */
@Service
public class TsysSerialnoServiceImpl implements TsysSerialnoService {

	@Autowired
	private TsysSerialnoMapper tsysSerialnoMapper;
	
	/**
	 * <p>根据条件查询站点列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param params
	 * @return
	 */
	@Override
	public R query( Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TsysSerialno> tsysserialnoList = tsysSerialnoMapper.selectListByMap(query);
		if (tsysserialnoList.size() > 0 && tsysserialnoList != null) {
			for (TsysSerialno tsysSerialno : tsysserialnoList) {
				tsysSerialno.setId(tsysSerialno.getSerialnoId());
			}
		}
		PageUtils pageUtil = new PageUtils(tsysserialnoList,query.getPage(),query.getLimit());
		return R.ok().put("data", pageUtil);
	}

	/**
	 * <p>注:原ModelAndView进入新增页面的方法</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	@Override
	public R add() {
		return R.ok().put("tsysserialno", new TsysSerialno());
	}

	/**
	 * <p>注:原ModelAndView进入修改页面的方法</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param params
	 * @return
	 */
	@Override
	public R edit( Map<String, Object> params) {
		String id = (String) params.get("id");
		if (id == null || "".equals(id) || "null".equals(id)) {
			return R.error("无法获取id");
		}
		TsysSerialno tsysserialno = tsysSerialnoMapper.selectObjectById(id);
		if (tsysserialno == null) {
			return R.error("无法获取数据");
		}
		return R.ok().put("data", tsysserialno);
	}

	/**
	 * <p>执行数据保存和修改</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysSerialno
	 * @return
	 */
	@Override
	public R saveOrUpdate(TsysSerialno tsysSerialno) {
		// 数据校验
		//ValidatorUtils.validateEntity(tsysSerialno);
		if ((tsysSerialno.getSerialnoId() == null) || ("").equals(tsysSerialno.getSerialnoId())) { // 新增
			try {
				save(tsysSerialno);
			} catch (Exception e) {
				e.printStackTrace();
				return R.error("新增业务流水号失败");
			}
		} else {
			try {
				update(tsysSerialno);
			} catch (Exception e) {
				e.printStackTrace();
				return R.error("修改业务流水号失败");
			}
		}
		return R.ok();
	}

	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param tsysSerialno
	 * @return
	 */
	@Override
	@SysLog("新增业务流水号")
	public R save( TsysSerialno tsysSerialno) {
		try {
			if (tsysSerialno.getSerialnoId() != null && !"".equals(tsysSerialno.getSerialnoId())) {
				return R.error("非新增操作");
			}
			tsysSerialno.setSerialnoId(Identities.uuid()); // 主键ID
			tsysSerialno.setCreateTime(DateUtils.getNowTimeStamp()); // 创建时间
			tsysSerialnoMapper.insert(tsysSerialno);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("新增流水号失败");
		}
		return R.ok("新增成功");
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param tsysSerialno
	 * @return
	 */
	@Override
	@SysLog("修改业务流水号")
	public R update( TsysSerialno tsysSerialno) {
		try {
			if (tsysSerialno.getSerialnoId() == null || "".equals(tsysSerialno.getSerialnoId())) {
				return R.error("非修改操作");
			}
			tsysSerialno.setUpdateTime(DateUtils.getNowTimeStamp());
			tsysSerialnoMapper.update(tsysSerialno);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("修改失败");
		}
		return R.ok("修改成功");
	}

	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @return
	 */
	@Override
	public R delete(String id) {
		if (id == null || "".equals(id) || "null".equals(id)) {
			return R.error("无法获取id");
		}
		tsysSerialnoMapper.delete(id);
		return R.ok("删除成功");
	}
	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param ids
	 * @return
	 */
	@Override
	@SysLog("批量删除业务流水号")
	public R deleteBatch(String[] ids) {
		if (ids == null || "".equals(ids) || "null".equals(ids)) {
			return R.error("无法获取ids");
		}
		tsysSerialnoMapper.deleteBatch(ids);
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
	public R view(String id) {
		if (id == null || "".equals(id) || "null".equals(id)) {
			return R.error("无法获取id");
		}
		TsysSerialno tsysSerialno = tsysSerialnoMapper.selectObjectById(id);
		if (tsysSerialno == null) {
			return R.error("无法获取数据");
		}
		return R.ok().put("data", tsysSerialno);
	}

}
