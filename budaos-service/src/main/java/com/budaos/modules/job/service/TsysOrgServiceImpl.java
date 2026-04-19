package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.dataprivilege.annotation.DataFilter;
import com.budaos.common.utils.ParamUtil;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysOrgMapper;
import com.budaos.modules.sys.api.TsysOrgService;
import com.budaos.modules.sys.domain.TsysOrg;
import com.budaos.modules.sys.domain.TsysParameter;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.SpringContextUtils;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * 机构管理
 * 
 * @author huangwb
 *
 */
@Service
public class TsysOrgServiceImpl implements TsysOrgService {
	//private Logger Log = LoggerFactory.getLogger(getClass());
	@Autowired
	private TsysOrgMapper tsysOrgMapper;
	@Autowired
	private ServiceLoginUtil loginUtils;

	/**
	 * 保存机构
	 * 
	 * @param tsysOrg
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	@Override
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R save(TsysOrg tsysOrg) {
		try {
			if (!validateOrgName(tsysOrg)) {
				return R.error("在当前节点下,您输入的机构名称有重复,请重新输入");
			}
			tsysOrg.setOrgId(Identities.uuid());
			String orgCode = buildCode(tsysOrg);
			tsysOrg.setOrgCode(orgCode);
			tsysOrg.setCreateTime(DateUtils.getNowTimeStamp());
			tsysOrgMapper.insert(tsysOrg);
			return R.ok().put("data", tsysOrg);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
	}

	/**
	 * 修改机构
	 * 
	 * @param tsysOrg
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	@Override
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R update(@RequestBody TsysOrg tsysOrg) {
		try {
			if (!validateOrgName(tsysOrg)) {
				return R.error("在当前机构下,您输入的机构名称有重复值,请重新输入");
			}
			// 取得修改前的的机构，比较是否有修改父机构，如果有修改则重新生成机构
			TsysOrg oldOrg = tsysOrgMapper.selectObjectById(tsysOrg.getOrgId());
			if (!Objects.equal(oldOrg.getParentId(), tsysOrg.getParentId())) {
				String orgCode = buildCode(tsysOrg);
				tsysOrg.setOrgCode(orgCode);
				// 修改子机构编码
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("parentCode", oldOrg.getOrgCode());
				List<TsysOrg> orgList = tsysOrgMapper.selectListByMap(m);
				for (TsysOrg org : orgList) {
					if (!Objects.equal(org.getOrgId(), tsysOrg.getOrgId())) {
						org.setOrgCode(org.getOrgCode().replace(oldOrg.getOrgCode(), orgCode));
						tsysOrgMapper.update(org);
					}
				}
			}
			tsysOrg.setUpdateTime(DateUtils.getNowTimeStamp());
			tsysOrgMapper.update(tsysOrg);
			return R.ok().put("data", tsysOrg);
		} catch (Exception e) {
			return R.error(e.getMessage());
		}
	}

	/**
	 * 验证输入的机构名称在同辈节点下是否有重复
	 * 
	 * @param tsysOrg
	 * @author huangwb
	 * @date 2019-06-15 09:18
	 */
	private Boolean validateOrgName(TsysOrg tsysOrg) {
		if (!Strings.isNullOrEmpty(tsysOrg.getParentId())) {
			List<TsysOrg> parents = tsysOrgMapper.selectListByParentId(tsysOrg.getParentId()).stream()
					.filter(a -> !a.getOrgId().equals(tsysOrg.getOrgId())).collect(Collectors.toList());
			for (TsysOrg org : parents) {
				if (Objects.equal(org.getOrgName(), tsysOrg.getOrgName())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	@Transactional
	@Override
	@CacheEvict(value = "authorization_cache", allEntries = true)
	public R delete(@PathVariable("deleteId") String id) {
		List<TsysOrg> datas = tsysOrgMapper.selectListByParentId(id);
		if (datas != null && !datas.isEmpty()) {
			return R.error("此结点下存在子结点，请先删除子结点，再执行删除！");
		}
		tsysOrgMapper.delete(id);
		return R.ok();
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	@Transactional
	@Override
	public R deleteBatch(String[] ids) {
		List<TsysOrg> datas = null;
		for (int i = 0; i < ids.length; i++) {
			datas = tsysOrgMapper.selectListByParentId(ids[i]);
			if (datas != null && !datas.isEmpty()) {
				return R.error("此结点下存在子结点，请先删除子结点，再执行删除！");
			}
		}
		tsysOrgMapper.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 移动机构管理操作
	 * 
	 * 
	 * @param moveId   移动对象的id
	 * @param targetId 目标对象的id
	 * @param moveType 移动的类型
	 * @return
	 * @author huangwb
	 * @date 2019-06-03 17:18
	 */
	@Override
	public R drag(String moveId, String targetId, String moveType) {
		TsysOrg move = tsysOrgMapper.selectObjectById(moveId);
		TsysOrg target = tsysOrgMapper.selectObjectById(targetId);
		// 如果是移动到内部
		if (Objects.equal(moveType, "inner")) {
			// 获取最大的排序号
			move.setOrgSn(tsysOrgMapper.selectMaxOrderSnByParentId(target.getOrgId()) + 1);
			move.setParentId(target.getOrgId());
			String orgCode = buildCode(move);
			move.setOrgCode(orgCode);
		} else if (Objects.equal(moveType, "before")) {
			// 获取目标元素的所有同辈元素 如果目标元素和移动元素同级则移除移动元素
			List<TsysOrg> targetParentOrgs = tsysOrgMapper.selectListByParentId(target.getParentId()).stream()
					.filter(org -> !org.getOrgId().equals(move.getOrgId())).collect(Collectors.toList());
			// 计算排序号
			int k = 0;
			String orgCode = "";
			if (!Objects.equal(target.getParentId(), "-1")) {
				TsysOrg parentOrg = tsysOrgMapper.selectObjectById(target.getParentId());
				orgCode = parentOrg.getOrgCode();
			} else {
				orgCode = "0";
			}
			// 遍历目标元素所有同辈元素
			for (int i = 0; i < targetParentOrgs.size(); i++) {
				TsysOrg tsysOrg = targetParentOrgs.get(i);
				// 排序号从1开始
				k++;
				// 由于是before 所以找到元素之后要在找到的元素将他的排序号再次+1 并将未+1的排序号给move对象
				// 留出一个空闲的排序号位置给move对象
				if (Objects.equal(tsysOrg.getOrgId(), target.getOrgId())) {
					move.setOrgSn(k);
					move.setOrgCode(orgCode + "0" + move.getOrgSn());
					tsysOrg.setOrgSn(k + 1);
				} else {
					// 否则k就是自然排序号
					tsysOrg.setOrgSn(k);
				}
				tsysOrg.setOrgCode(orgCode + "0" + tsysOrg.getOrgSn());
				update(tsysOrg);
			}
			// 讲move的父id设置成target目标对象的父id
			move.setParentId(target.getParentId());
		} else {
			// 获取目标元素的所有同辈元素 如果目标元素和移动元素同级则移除移动元素
			List<TsysOrg> targetParentOrgs = tsysOrgMapper.selectListByParentId(target.getParentId()).stream()
					.filter(org -> !org.getOrgId().equals(move.getOrgId())).collect(Collectors.toList());
			int k = 0;
			String orgCode = "";
			if (!Objects.equal(target.getParentId(), "-1")) {
				TsysOrg parentOrg = tsysOrgMapper.selectObjectById(target.getParentId());
				orgCode = parentOrg.getOrgCode();
			} else {
				orgCode = "0";
			}
			for (int i = 0; i < targetParentOrgs.size(); i++) {
				TsysOrg tsysOrg = targetParentOrgs.get(i);
				k++;
				// 由于我们是after操作 所以先自然排序
				tsysOrg.setOrgSn(k);
				// 当找到目标元素的时候 此时k再+1 留出一个空闲的排序号位置给move对象
				if (Objects.equal(tsysOrg.getOrgId(), target.getOrgId())) {
					k++;
					move.setOrgSn(k);
					move.setOrgCode(orgCode + "0" + move.getOrgSn());
				}
				tsysOrg.setOrgCode(orgCode + "0" + tsysOrg.getOrgSn());
				update(tsysOrg);
			}
			move.setParentId(target.getParentId());
		}
		update(move);
		return R.ok().put("data", move);
	}

	/**
	 * 点击上下按钮移动机构管理节点操作
	 * 
	 * @param moveId   移动对象的id
	 * @param targetId 目标对象的id
	 * @return
	 * @author huangwb
	 * @date 2019-06-17 14:18
	 */
	@Override
	public R clickDrag(String moveId, String targetId) {
		// 思路：交换两节点的OrgSN和OrgCode便可行
		TsysOrg move = tsysOrgMapper.selectObjectById(moveId);
		TsysOrg target = tsysOrgMapper.selectObjectById(targetId);
		Integer targetSn = target.getOrgSn();
		String targetCode = target.getOrgCode();
		target.setOrgSn(move.getOrgSn());
		target.setOrgCode(move.getOrgCode());
		move.setOrgSn(targetSn);
		move.setOrgCode(targetCode);
		tsysOrgMapper.update(target);
		tsysOrgMapper.update(move);
		move.setUpdateUserId(loginUtils.getLoginUserId());
		move.setUpdateTime(DateUtils.getNow());
		target.setUpdateUserId(loginUtils.getLoginUserId());
		target.setUpdateTime(DateUtils.getNow());
		recursionOrgDrag(target);
		recursionOrgDrag(move);
		return R.ok();
	}

	/**
	 * 递归设置上移下移节点的子节点的orgCode
	 * 
	 * @param tsysOrg
	 * @author huangwb
	 * @date 2019-06-18 10:18
	 */
	private void recursionOrgDrag(TsysOrg tsysOrg) {
		List<TsysOrg> parents = tsysOrgMapper.selectListByParentId(tsysOrg.getOrgId());
		if (parents != null && !parents.isEmpty()) {
			for (int i = 0; i < parents.size(); i++) {
				parents.get(i).setOrgCode(tsysOrg.getOrgCode() + "0" + (i + 1));
				tsysOrgMapper.update(parents.get(i));
				recursionOrgDrag(parents.get(i));
			}
		}
	}

	/**
	 * 获取所有机构id
	 * 
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	@Override
	public R getAllOrgIds() {
		return R.ok().put("data", tsysOrgMapper.getAllOrgIds());
	}

	/**
	 * 获取子部门ID，用于数据过滤
	 * 
	 * @param orgId
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	@Override
	public R getSubOrgIdList(String orgId) {
		// 部门及子部门ID列表
		List<String> orgIdList = new ArrayList<>();
		// 获取子部门ID
		List<String> subIdList = selectOrgIdList(orgId);
		getOrgTreeList(subIdList, orgIdList);
		return R.ok().put("data", orgIdList);
	}

	/**
	 * 获取机构信息
	 * 
	 * @param orgId
	 * @author huangwb
	 * @date 2019-05-05 15:18
	 */
	@Override
	public R selectListById( String orgId) {
		return R.ok().put("data", tsysOrgMapper.selectListById(orgId));
	}

	private List<String> selectOrgIdList(String parentId) {
		return tsysOrgMapper.selectOrgIdList(parentId);
	}

	// 生成新的机构编码
	private synchronized String buildCode(TsysOrg tsysOrg) {
		// 获取机构编码每级的长度
		List<TsysParameter> paraList = SpringContextUtils.getBean(ParamUtil.class).getByType("codeLength"); // 从数据库中获取数据
		int length = 0;
		for (TsysParameter para : paraList) {
			if ("orgCodeLength".equals(para.getParaKey())) {
				length = Integer.parseInt(para.getParano());
				break;
			}
		}
		// 取得兄弟机构编码的最大值
		String orgCode = "";
		String maxCode = tsysOrgMapper.selectMaxCodeByParenId(tsysOrg.getParentId());
		if (!tsysOrg.getParentId().equals("-1")) {
			// 计算编码
			if (maxCode == null) {// 如果没有兄弟机构，则表示是当前是父机构中的第一个子机构，编码通过父机构的编码计算
				// 取得父机构对象
				TsysOrg parent = tsysOrgMapper.selectObjectById(tsysOrg.getParentId());
				// orgCode = String.format("%0" + length + "d", 1);
				if (parent != null) {// 如果有父机构，则编码为父机构的编码+初始编码
					orgCode = parent.getOrgCode() + "01";
				}
			} else {// 否则在兄弟机构的最大值上加1
					// 得到父编码
				String parentCode = maxCode.substring(0, maxCode.length() - length);
				// 得到子编码
				orgCode = maxCode.substring(maxCode.length() - length);
				// 在原子编码的基础上加1
				orgCode = parentCode + String.format("%0" + length + "d", Integer.parseInt(orgCode) + 1);
			}
		} else {
			// 取得父机构对象
			int num = Integer.valueOf(maxCode.substring(maxCode.length() - 1)) + 1;
			orgCode = "0" + num;
		}
		return orgCode;
	}

	/**
	 * 递归
	 */
	private void getOrgTreeList(List<String> subIdList, List<String> orgIdList) {
		for (String orgId : subIdList) {
			List<String> list = selectOrgIdList(orgId);
			if (list.size() > 0) {
				getOrgTreeList(list, orgIdList);
			}
			orgIdList.add(orgId);
		}
	}

	@Override
	public R query(String parentId, String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (parentId == null || StringUtils.isEmpty(parentId)) {
			map.put("parentId", "-1");
		} else {
			map.put("parentId", parentId);
		}
		List<TsysOrg> tsysOrgList = tsysOrgMapper.selectListByMap(map);
		if (!Constant.SUPER_ADMIN.equals(userId)) {
			List<TsysOrg> allList = tsysOrgMapper.selectListByMap(new HashMap<String, Object>());
			tsysOrgList.forEach(a -> {
				List<String> parentIds = new LinkedList<String>();
				initParantNode(allList, parentIds, a);
				a.setParentId(getParantId(tsysOrgList, parentIds));
			});
		}
		return R.ok().put("data", tsysOrgList);
	}

	/**
	 * 权限过滤查询机构列表
	 * 
	 * @param parentId 父机构节点ID orgName机构名称模糊查询条件 type=1代表树形菜单展开 type==null为树模糊查询情况
	 * @return
	 * @author huangwb
	 * @date 2019-5-16 11:30
	 */
	@DataFilter(tableAlias = "o", customDataAuth = "", selfUser = false)
	@Override
	public R queryByMap(Map<String, Object> map, String userId) {
		List<TsysOrg> tsysOrgList = tsysOrgMapper.selectListByMap(map);
		Map<String, TsysOrg> dataMap = new HashMap<String, TsysOrg>();
		for (TsysOrg tsysOrg : tsysOrgList) {
			dataMap.put(tsysOrg.getOrgId(), tsysOrg);
		}
		// 排序操作
		Map<String, TsysOrg> sortMap = new TreeMap<String, TsysOrg>(new MapValueComparaor(dataMap));
		sortMap.putAll(dataMap);
		// 充分利用对象引用之间的关系 即便已经是Map集合 但数据来源引用还是collect的数据
		sortMap.forEach((k, v) -> {
			// 如果不是根节点
			if (!v.getParentId().equals("-1")) {
				sortMap.get(v.getParentId()).getChildren().add(v);
			}
		});
		return R.ok().put("data",
				tsysOrgList.stream().filter(a -> a.getParentId().equals("-1")).collect(Collectors.toList()));
	}

	/**
	 * 无权限过滤查询机构列表
	 * 
	 * @param userId 父机构节点ID orgName机构名称模糊查询条件 type=1代表树形菜单展开 type==null为树模糊查询情况
	 * @return
	 * @author huangwb
	 * @date 2019-5-16 11:30
	 */
	@Override
	public R queryByMapWithAll(Map<String, Object> map, String userId) {
		List<TsysOrg> tsysOrgList = tsysOrgMapper.selectListByMap(map);
		Map<String, TsysOrg> dataMap = new HashMap<String, TsysOrg>();
		for (TsysOrg tsysOrg : tsysOrgList) {
			dataMap.put(tsysOrg.getOrgId(), tsysOrg);
		}
		// 排序操作
		Map<String, TsysOrg> sortMap = new TreeMap<String, TsysOrg>(new MapValueComparaor(dataMap));
		sortMap.putAll(dataMap);
		// 充分利用对象引用之间的关系 即便已经是Map集合 但数据来源引用还是collect的数据
		sortMap.forEach((k, v) -> {
			// 如果不是根节点
			if (!v.getParentId().equals("-1")) {
				sortMap.get(v.getParentId()).getChildren().add(v);
			}
		});
		return R.ok().put("data",
				tsysOrgList.stream().filter(a -> a.getParentId().equals("-1")).collect(Collectors.toList()));
	}
	
	class MapValueComparaor implements Comparator<String> {
		Map<String, TsysOrg> map;

		public MapValueComparaor(Map<String, TsysOrg> map) {
			this.map = map;
		}

		@Override
		public int compare(String o1, String o2) {
			if (map.get(map.get(o1).getParentId()) == null) {
				map.get(o1).setParentId("-1");
			} else if (map.get(map.get(o2).getParentId()) == null) {
				map.get(o2).setParentId("-1");
			}
			return map.get(o1).getOrgCode().compareTo(map.get(o2).getOrgCode());
		}

	}

	/**
	 * 查询机构列表
	 * 
	 * @param userId 父机构节点ID orgName机构名称模糊查询条件 type=1代表树形菜单展开 type==null为树模糊查询情况
	 * @return
	 * @author huangwb
	 * @date 2019-5-16 11:30
	 */
	@Override
	public R queryByMap2(Map<String, Object> map, String userId) {
		List<TsysOrg> tsysOrgList = null;
		// 如果展开为树结构同时parentId为空时候则查出根节点
		if ((map.get("type") != null && map.get("type").equals("1"))
				&& (map.get("parentId") == null || StringUtils.isEmpty(map.get("parentId").toString()))) { // tsysOrgList
			tsysOrgMapper.selectListByParentId("-1");
		}
		tsysOrgList = tsysOrgList != null ? tsysOrgList : tsysOrgMapper.selectListByMap(map);
		if (map.get("type") == null) {
			// 递归存储机构集合
			List<TsysOrg> list = new ArrayList<TsysOrg>();
			// 缓存操作 保存已经查询过的menu_id 防止重复查询同一个父节点
			Map<String, Object> params = new HashMap<String, Object>();
			// 遍历查询的数据
			tsysOrgList.forEach(org -> {
				org.setLeaf(tsysOrgMapper.selectCountParentId(org.getOrgId()) > 0 ? false : true);
				recursionMenuTree(list, org, params);
			});
			tsysOrgList.addAll(list);
			// 清空map方便GC回收
			params.clear();
		} else {
			tsysOrgList.forEach(org -> {
				org.setLeaf(tsysOrgMapper.selectCountParentId(org.getOrgId()) > 0 ? false : true);
			});
		}
		return R.ok().put("data", tsysOrgList);
	}

	/**
	 * 递归资源菜单 查询出数据的所有父菜单
	 * 
	 * @param datas    递归存储资源菜单集合
	 * @param tsysOrg 递归判断因素
	 * @param params   缓存操作 保存已经查询过的menu_id 防止重复查询同一个父菜单
	 * @author huangwb
	 * @date 2019-05-20 15:18
	 */
	private void recursionMenuTree(List<TsysOrg> datas, TsysOrg tsysOrg, Map<String, Object> params) {
		if (tsysOrg.getParentId() != null && !tsysOrg.getParentId().equals("-1")) {
			// 判断缓存map中是否存在已经查询过的父菜单id
			if (params != null && !params.containsKey(tsysOrg.getParentId())) {
				TsysOrg selectObjectById = tsysOrgMapper.selectObjectById(tsysOrg.getParentId());
				datas.add(selectObjectById);
				selectObjectById.setLeaf(false);
				params.put(tsysOrg.getParentId(), selectObjectById);
				recursionMenuTree(datas, selectObjectById, params);
			}
		}
	}

	/**
	 * <p>
	 * 重新获取节点的父级节点
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @param list
	 * @param ids
	 * @return
	 */
	private String getParantId(List<TsysOrg> list, List<String> ids) {
		for (int i = 0; i < ids.size(); i++) {
			for (TsysOrg org : list) {
				if (org.getOrgId().equals(ids.get(i))) {
					return ids.get(i);
				}
			}
		}
		return "-1";
	}

	/**
	 * 递归节点的所有父级节点
	 * 
	 * @param allList
	 * @param parentIds
	 * @param org
	 */
	private void initParantNode(List<TsysOrg> allList, List<String> parentIds, TsysOrg org) {
		// 保留父节点
		List<TsysOrg> parentNode = allList.stream().filter(a -> a.getOrgId().equals(org.getParentId()))
				.collect(Collectors.toList());
		if (parentNode.size() > 0) {
			parentIds.add(parentNode.get(0).getOrgId());
			initParantNode(allList, parentIds, parentNode.get(0));
		}
	}

	/**
	 * <p>
	 * 访问列表(构建机构树)
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月14日
	 * @return R.ok().put("data", tsysOrgList);
	 */
	@Override
	@DataFilter(tableAlias = "o", customDataAuth = "", selfUser = false)
	public R query2(@RequestParam Map<String, Object> params) {
		params.put("state", "1");
		TsysUserinfo userInfo = loginUtils.getLoginUser();
		if (userInfo == null) {
			return R.error("无法获取登陆信息，请尝试重新登陆");
		}
		List<TsysOrg> tsysOrgList = tsysOrgMapper.selectListByMap(params);
		if (!Constant.SUPER_ADMIN.equals(userInfo.getUserId())) {
			List<TsysOrg> allList = tsysOrgMapper.selectListByMap(params);
			tsysOrgList.forEach(a -> {
				List<String> parentIds = new LinkedList<String>();
				initParantNode(allList, parentIds, a);
				a.setParentId(getParantId(tsysOrgList, parentIds));
			});
		}
		return R.ok().put("data", tsysOrgList);
	}

	/**
	 * 根据orgId递归查询所有父级id
	 * @param orgId
	 * @return
	 */
	@Override
	@Cacheable(value = "authorization_cache", key = "'queryOrgsBySubid_' +#orgId")
	public List<String> queryOrgsBySubid(String orgId){
		List<String> orgIds = new ArrayList<>();
		Map<String, Object> params = new HashMap<>();
		params.put("state", "1");
		List<TsysOrg> tsysOrgList = tsysOrgMapper.selectListByMap(params);
		List<TsysOrg> list = tsysOrgList.parallelStream().filter(a -> orgId.equals(a.getOrgId())).collect(Collectors.toList());
		if(list.size() == 0) {
			return orgIds;
		}
		orgIds.add(orgId);
		String tempId = list.get(0).getParentId();
		for(int i=0; i<tsysOrgList.size(); i++) {
			if(tempId.equals(tsysOrgList.get(i).getOrgId())) {
				orgIds.add(tempId);
				tempId = tsysOrgList.get(i).getParentId();
				i = -1;
			}
		}
		return orgIds;
	}

	@Override
    @DataFilter(tableAlias = "o", customDataAuth = "", selfUser = false)
	public R getOrgTree(Map<String, Object> map) {
		map.put("state", "1");
        Query query = new Query(map);
        List<TsysOrg> tsysOrgList = tsysOrgMapper.selectListByMap(query);
        // 如果不是超级管理员
        if (!Constant.SUPER_ADMIN.equals(loginUtils.getLoginUserId())) {
            List<TsysOrg> allList = tsysOrgMapper.selectListByMap(query);
            tsysOrgList.forEach(a -> {
                List<String> parentIds = new LinkedList<String>();
                initParantNode(allList, parentIds, a);
                a.setParentId(getParantId(tsysOrgList, parentIds));
            });
        }
        return R.ok().put(Constant.R_DATA, tsysOrgList);
	}
}
