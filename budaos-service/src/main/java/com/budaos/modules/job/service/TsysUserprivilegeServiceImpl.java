package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysUserprivilegeMapper;
import com.budaos.modules.sys.api.TsysUserprivilegeService;
import com.budaos.modules.sys.domain.TsysUserprivilege;
import com.budaos.utils.tool.Identities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>用户特权api的实现类</p>
 * <p>Title: TsysUserprivilegeServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年5月8日
 */
@Service
public class TsysUserprivilegeServiceImpl implements TsysUserprivilegeService {

	@Autowired
	private TsysUserprivilegeMapper tsysUserprivilegeMapper;
	
	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param map
	 * @return
	 */
	@Override
	public R query( Map<String, Object> map) {
		List<TsysUserprivilege> list = tsysUserprivilegeMapper.selectListByMap(map);
		return R.ok().put("list", list);
	}

	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param tsysUserprivilege
	 * @return
	 */
	@Override
	@SysLog("新增用户特权")
	public R save(TsysUserprivilege tsysUserprivilege) {
		try {
			verifyForm(tsysUserprivilege); // 数据校验
			tsysUserprivilege.setUserprviid(Identities.uuid());
			tsysUserprivilegeMapper.insert(tsysUserprivilege);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("新增失败");
		}
		return R.ok();
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param tsysUserprivilege
	 * @return
	 */
	@Override

	@SysLog("修改用户特权")
	public R update(TsysUserprivilege tsysUserprivilege) {
		try {
			verifyForm(tsysUserprivilege); // 数据校验
			tsysUserprivilegeMapper.update(tsysUserprivilege);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("修改失败");
		}
		return R.ok();
	}

	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param id
	 * @return
	 */
	@Override
	@SysLog("单个删除用户特权")
	public R delete(String id) {
		try {
			tsysUserprivilegeMapper.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("删除失败");
		}
		return R.error();
	}

	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param ids
	 * @return
	 */
	@Override
	@SysLog("批量删除用户特权")
	public R deleteBatch(String[] ids) {
		try {
			tsysUserprivilegeMapper.deleteBatch(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("删除失败");
		}
		return R.ok();
	}

	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param id
	 * @return
	 */
	@Override
	public R view(String id) {
		TsysUserprivilege tsysUserprivilege = tsysUserprivilegeMapper.selectObjectById(id);
		return R.ok().put("tsysUserprivilege", tsysUserprivilege);
	}
	
	@Override
	public void saveOrUpdate(String userId, String[] menuIds) {
		TsysUserprivilege tsysUserprivilege = null;
		for(String menuId : menuIds){
			tsysUserprivilege = new TsysUserprivilege();
			tsysUserprivilege.setMenuId(menuId);
			tsysUserprivilege.setUserid(userId);
			save(tsysUserprivilege);
		}
	}
	
	/**
	 * <p>验证参数是否正确</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param tsysUserprivilege
	 */
	private void verifyForm(TsysUserprivilege tsysUserprivilege) {
		/*if (StringUtils.isBlank("")) {
			throw new BudaosException("****不能为空");
		}*/
	}


}
