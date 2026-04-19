package com.budaos.modules.job.service;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysRoleprivilegeMapper;
import com.budaos.modules.sys.api.TsysRoleprivilegeService;
import com.budaos.modules.sys.domain.TsysRoleprivilege;
import com.budaos.utils.tool.Identities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Service
public class TsysRoleprivilegeServiceImpl implements TsysRoleprivilegeService {

	@Autowired
	private TsysRoleprivilegeMapper tsysRoleprivilegeMapper;
	
	@Override
	@Transactional
	public R saveOrUpdate(String roleId, List<String> menuIdList) {
		// 先删除角色与菜单关系
        tsysRoleprivilegeMapper.delete(roleId);
        if (menuIdList == null || menuIdList.size() == 0) {
            return R.ok();
        }
        List<TsysRoleprivilege> insertList = new ArrayList<>();
        menuIdList.stream().forEach(menuId -> {
            TsysRoleprivilege t = new TsysRoleprivilege();
            t.setId(Identities.uuid());
            t.setRoleId(roleId);
            t.setMenuId(menuId);
            insertList.add(t);
        });
        if (insertList != null && insertList.size() > 0) {
            tsysRoleprivilegeMapper.insertBatch(insertList);
        }
        return R.ok();
	}

	/**
	 * <p>根据角色ID,查询菜单ID列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param roleId
	 * @return
	 */
	@Override
	public List<String> selectMenuListByRoleId(String roleId) {
		return tsysRoleprivilegeMapper.selectMenuListByRoleId(roleId);
	}

}
