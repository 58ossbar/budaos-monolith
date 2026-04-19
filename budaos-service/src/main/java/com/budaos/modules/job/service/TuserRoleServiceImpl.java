package com.budaos.modules.job.service;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TuserRoleMapper;
import com.budaos.modules.sys.api.TuserRoleService;
import com.budaos.modules.sys.domain.TuserRole;
import com.budaos.utils.tool.Identities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户与角色对应关系api的实现类
 * <p>Title: TuserRoleServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年5月6日
 */
@Service
public class TuserRoleServiceImpl implements TuserRoleService {

	@Autowired
	private TuserRoleMapper tuserRoleMapper;
	
	/**
     * 保存用户与角色关系
     *
     * @param roleIdList
     * @param userIdList
     * @return
     * @author huj
     * @data 2019年5月6日
     */
    @Override
    public R saveOrUpdate(List<String> roleIdList, List<String> userIdList) {
        userIdList.stream().forEach(userId -> {
            // 先删除用户与角色关系
            tuserRoleMapper.delete(userId);
            // 批量保存
            roleIdList.stream().forEach(roleId -> {
                if (!roleId.trim().isEmpty()) {
                    // 批量保存
                    List<TuserRole> insertList = new ArrayList<>();
                    TuserRole t = new TuserRole();
                    t.setId(Identities.uuid());
                    t.setRoleId(roleId);
                    t.setUserId(userId);
                    insertList.add(t);
                    if (insertList.size() > 0) {
                        tuserRoleMapper.insertBatch(insertList);
                    }
                }
            });
        });
        return R.ok("保存成功");
    }
	
	/**
	 * <p>保存用户与角色关系</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @param roleIdList
	 * @return
	 */
	@Override
	public R saveOrUpdate(String userId, List<String> roleIdList) {
		if(roleIdList.size() == 0 || roleIdList == null){
			return R.ok();
		}
		// 先删除用户与角色关系
		tuserRoleMapper.delete(userId);
		try {
			for (String mid : roleIdList) { // 保存菜单权限
				if(!mid.trim().isEmpty()){
					// 保存用户与角色关系
					Map<String, Object> map = new HashMap<>();
					map.put("id", Identities.uuid());
			    	map.put("userId", userId);
					map.put("roleId", mid);
					tuserRoleMapper.insert(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("用户与角色关系， 保存失败");
		}
		return R.ok("分配角色成功");
	}

	/**
	 * <p>保存用户与角色关系</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param roleId
	 * @param userIdList
	 * @return
	 */
	@Override
	public R saveOrUpdateForRole(String roleId, List<String> userIdList) {
		try {
			// 先删除用户与角色关系
			tuserRoleMapper.deleteByRole(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("先删除用户与角色关系，失败了");
		}
		try {
			for (String mid : userIdList) { // 保存菜单权限
				// 保存用户与角色关系
				Map<String, Object> map = new HashMap<>();
				map.put("id", Identities.uuid());
				map.put("userId", mid);
				map.put("roleId", roleId);
				tuserRoleMapper.insert(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("用户与角色关系保存失败");
		}
		return R.ok();
	}

	/**
	 * <p>根据用户ID，获取角色ID列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @return
	 */
	@Override
	public List<String> selectRoleListByUserId(String userId) {
		List<String> list = tuserRoleMapper.selectRoleListByUserId(userId);
		return list;
	}

	/**
	 * <p>根据角色ID,查询用户ID列表</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param roleId
	 * @return
	 */
	@Override
	public R selectUserListByRoleId(String roleId) {
		List<String> list = tuserRoleMapper.selectUserListByRoleId(roleId);
		return R.ok().put("list", list);
	}

	/**
	 * <p>根据用户ID,删除记录</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @return
	 */
	@Override
	public R delete(String userId) {
		try {
			tuserRoleMapper.delete(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok();
	}

	/**
	 * <p>根据用户ID,批量删除记录</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @return
	 */
	@Override
	public R deleteBatch(String[] userId) {
		try {
			tuserRoleMapper.deleteBatch(userId);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return R.ok();
	}

}
