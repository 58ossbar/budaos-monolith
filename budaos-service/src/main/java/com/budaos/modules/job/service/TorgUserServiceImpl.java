package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TorgUserMapper;
import com.budaos.modules.sys.api.TorgUserService;
import com.budaos.modules.sys.domain.TorgUser;
import com.budaos.utils.tool.Identities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>机构用户api实现类</p>
 * <p>Title: TorgUserServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年5月13日
 */
@Service
public class TorgUserServiceImpl implements TorgUserService {

	@Autowired
	private TorgUserMapper torgUserMapper;
	
	@Override
	public List<TorgUser> selectListByMap(Map<String, Object> map) {
		List<TorgUser> torgUserList = torgUserMapper.selectListByMap(map);
		return torgUserList;
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param torgUser
	 * @return
	 */
	@Override
	public R update( TorgUser torgUser) {
		try {
			torgUserMapper.update(torgUser);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("修改失败");
		}
		return R.ok();
	}

	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param id
	 * @return
	 */
	@Override
	public R delete(String id) {
		try {
			torgUserMapper.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("删除失败");
		}
		return R.ok();
	}

	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param ids
	 * @return
	 */
	@Override
	public R deleteBatch(String[] ids) {
		try {
			torgUserMapper.deleteBatch(ids);
		} catch (Exception e) { 
			e.printStackTrace();
			return R.error("批量删除失败");
		}
		return R.ok();
	}

	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param id
	 * @return
	 */
	@Override
	public R selectObjectById(String id) {
		TorgUser torgUser = torgUserMapper.selectObjectById(id);
		return R.ok().put("torgUser", torgUser);
	}

	@Override
	public R saveOrUpdate(String userId, List<String> orgIdList) {
		try {
			if(orgIdList.size() == 0){
				return R.ok();
			}
			// 先删除用户与机构的关系
			torgUserMapper.delete(userId);
			int isMain = 1;//是否主机构
			// 列表中第一条为主机构
			for (String mid : orgIdList) {
				if(!mid.trim().isEmpty()){
					// 保存用户与机构关系
					Map<String, Object> map = new HashMap<>();
					map.put("orguserId", Identities.uuid());
			    	map.put("userId", userId);
					map.put("orgId", mid);
					map.put("isMain", isMain);
					torgUserMapper.insert(map);
					isMain = 0;
				}
			}
			/*
			torgUser.setOrguserId(Identities.uuid());
			torgUserMapper.insert(torgUser);
			*/
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("保存失败");
		}
		return R.ok();
	}

	@Override
	public int selectTotalByMap(Map<String, Object> map) {
		return torgUserMapper.selectTotalByMap(map);
	}

	/**
	 * <p>根据用户ID查询记录</p>
	 * @author huj
	 * @data 2019年5月13日
	 * @param userId
	 * @return
	 */
	@Override
	public List<String> selectListByUserId(String userId) {
		return torgUserMapper.selectListByUserId(userId);
	}

}
