package com.budaos.modules.evgl.book.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.book.api.TevglBookRegularService;
import com.budaos.modules.evgl.book.domain.TevglBookRegular;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.modules.evgl.book.persistence.TevglBookRegularMapper;
import com.budaos.modules.evgl.book.persistence.TevglBookSubjectMapper;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
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

@Service
public class TevglBookRegularServiceImpl implements TevglBookRegularService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglBookRegularServiceImpl.class);
	@Autowired
	private TevglBookRegularMapper tevglBookRegularMapper;
	@Autowired
	private TevglBookSubjectMapper tevglBookSubjectMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglBookRegular> tevglBookRegularList = tevglBookRegularMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglBookRegularList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * 查询列表(返回List<Map<String, Object>)）
	 * @param params
	 * @return
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglBookRegularList = tevglBookRegularMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglBookRegularList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglBookRegular
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglBookRegular tevglBookRegular) throws BudaosException {
		tevglBookRegular.setRegularId(Identities.uuid());
		ValidatorUtils.check(tevglBookRegular);
		tevglBookRegularMapper.insert(tevglBookRegular);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglBookRegular
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update(TevglBookRegular tevglBookRegular) throws BudaosException {
	    ValidatorUtils.check(tevglBookRegular);
		tevglBookRegularMapper.update(tevglBookRegular);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		tevglBookRegularMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglBookRegularMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		if (StrUtils.isEmpty(id)) {
			return R.error("必传参数为空");
		}
		TevglBookRegular tevglBookRegular = tevglBookRegularMapper.selectObjectById(id);
		if (tevglBookRegular == null) {
			return R.error("无法查看记录，请重试");
		}
		// 去掉BigDecimal 后面的0
		BigDecimal ok = new BigDecimal(tevglBookRegular.getRegularSum().stripTrailingZeros().toPlainString());
		tevglBookRegular.setRegularSum(ok);
		TevglBookSubject tevglBookSubject = tevglBookSubjectMapper.selectObjectById(tevglBookRegular.getSubjectId());
		Map<String, Object> data = new HashMap<>();
		// 父级ID为 -1 代表修改的是总规则
		// 父级ID不为 -1 则代表修改的总规则下的具体规则
		// 修改总规则
		/*if ("-1".equals(tevglBookRegular.getParentId())) {
			data.put("tevglBookRegular", tevglBookRegular);
			data.put("tevglBookSubject", tevglBookSubject);
		}
		if (!"-1".equals(tevglBookRegular.getParentId())) {
			
		}*/
		data.put("tevglBookRegular", tevglBookRegular);
		data.put("tevglBookSubject", tevglBookSubject);
		data.put("parentId", tevglBookRegular.getParentId());
		// 查出课程下的所有总规则
		Map<String, Object> map = new HashMap<>();
		map.put("subjectId", tevglBookRegular.getSubjectId());
		map.put("parentId", "-1");
		List<TevglBookRegular> zongRegularList = tevglBookRegularMapper.selectListByMap(map);
		data.put("zongRegularList", zongRegularList);
		return R.ok().put(Constant.R_DATA, data);
	}

	/**
     * 获取课程体系考核规则树
     * @param params
     * @return
     */
	@Override
	public List<Map<String, Object>> getSubjectRegularTree(Map<String, Object> params) {
		Object type = params.get("type");
		// 最终返回结果
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 查询条件
		Map<String, Object> map = new HashMap<String, Object>();
		if (StrUtils.isNull(type) || "1".equals(type)) {
			// 查询课程体系
			map.put("sidx", "create_time");
			map.put("order", "desc");
			map.put("isSubjectRefNull", "Y");
			List<TevglBookSubject> bookSubjectList = tevglBookSubjectMapper.selectListByMapForCommon(map);
			if (bookSubjectList == null || bookSubjectList.size() == 0) {
				return list;
			}
			// 查询课程体系的考核规则
			List<Object> subjectIds = bookSubjectList.stream().map(a -> a.getSubjectId()).collect(Collectors.toList());
			map.clear();
			map.put("subjectIds", subjectIds);
			map.put("order", "asc");
			map.put("sidx", "regular_sort");
			List<TevglBookRegular> regularList = tevglBookRegularMapper.selectListByMap(map);
			// 遍历课程体系，取出对应的考核规则，并计算总分
			for (TevglBookSubject sub : bookSubjectList) {
				Map<String, Object> m = new HashMap<String, Object>();
				BigDecimal total = BigDecimal.ZERO;
				m.put("id", sub.getSubjectId());
				m.put("level", 1);
				m.put("type", "01");
				m.put("name", sub.getSubjectName());
				// 取出课程对应的考核规则
				List<TevglBookRegular> tevglBookRegularList = regularList.stream().filter(a -> "-1".equals(a.getParentId()) && a.getSubjectId().equals(sub.getSubjectId())).collect(Collectors.toList());
				for (TevglBookRegular regular : tevglBookRegularList) {
					total = total.add(regular.getRegularSum() == null ? new BigDecimal("0") : regular.getRegularSum());
				}
				// 大于0时才去拼接
				if (total.compareTo(new BigDecimal("0")) == 1) {
					m.put("name", sub.getSubjectName() + "(总分" + total.stripTrailingZeros().toPlainString() + ")");
				}
				// 加入集合
				list.add(m);
				List<Map<String, Object>> childrenOne = new ArrayList<>();
				for (TevglBookRegular reg : tevglBookRegularList) {
					Map<String, Object> mm = new HashMap<String, Object>();
					mm.put("id", reg.getRegularId());
					mm.put("name", reg.getRegularSum() != null ? reg.getRegularName() + "(总分" + reg.getRegularSum().stripTrailingZeros().toPlainString() + ")" : reg.getRegularName());
					mm.put("parentId", reg.getSubjectId());
					mm.put("type", "02");
					mm.put("level", 2);
					childrenOne.add(mm);
					List<Map<String, Object>> childrenTwo = new ArrayList<>();
					List<TevglBookRegular> regulars = regularList.stream().filter(a -> a.getSubjectId().equals(reg.getSubjectId()) && a.getParentId().equals(reg.getRegularId())).collect(Collectors.toList());
					regulars.stream().forEach(regular -> {
						Map<String, Object> maps = new HashMap<>();
						maps.put("id", regular.getRegularId());
						maps.put("name", regular.getRegularName() + "(" + regular.getRegularSum().stripTrailingZeros().toPlainString() + "分)");
						maps.put("parentId", reg.getRegularId());
						maps.put("type", "03");
						maps.put("level", 3);
						childrenTwo.add(maps);
						mm.put("children", childrenTwo);
					});
				}
				m.put("children", childrenOne);
			}
		}
		return list;
	}
	
	/**
	 * 右键新增规则节点
	 * @param regular
	 * @return
	 */
	@Override
	public R saveRegular(TevglBookRegular regular) throws BudaosException {
		if (checkNameRegular(regular)) {
			return R.error("同课程下考核规则名称需唯一");
		}
		// 默认分值0分
		regular.setRegularSum(new BigDecimal(0));
		if (!"-1".equals(regular.getParentId())) {
			TevglBookRegular selectObjectById = tevglBookRegularMapper.selectObjectById(regular.getParentId());
			regular.setSubjectId(selectObjectById == null ? null : selectObjectById.getSubjectId());
		}
		regular.setRegularId(Identities.uuid());
		tevglBookRegularMapper.insert(regular);
		return R.ok("保存成功").put(Constant.R_DATA, regular);
	}
	
	/**
	 *  新增时，验证考核规则名称是否唯一
	 * @param tevglBookRegular
	 * @return
	 */
	private boolean checkNameRegular(TevglBookRegular tevglBookRegular) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subjectId", tevglBookRegular.getSubjectId());
		params.put("regularName", tevglBookRegular.getRegularName());
		List<TevglBookRegular> teaoScRegularList = tevglBookRegularMapper.selectListByMap(params);
		return teaoScRegularList.stream().anyMatch(a -> !tevglBookRegular.getRegularId().equals(a.getRegularId()) && a.getRegularName().equals(tevglBookRegular.getRegularName()));
	}
	
	/**
	 * 点击课程将显示出该课程对应的预览规则页面
	 * @param subjectId
	 * @return
	 */
	@Override
	public R viewRegular(String subjectId) {
		if (StrUtils.isEmpty(subjectId)) {
			return R.error("必传参数为空");
		}
		// 最终返回数据
		Map<String, Object> data = new HashMap<>();
		data.put("regularList", null);
		data.put("subjectName", null);
		// 查询本次考核的所有规则
		Map<String, Object> map = new HashMap<>();
		map.put("subjectId", subjectId);
		map.put("sidx", "regular_sort");
		map.put("order", "asc");
		List<Map<String,Object>> allList = tevglBookRegularMapper.selectListMapByMap(map);
		if (allList != null && allList.size() > 0) {
			// 本次考核的一级规则
			List<Map<String, Object>> regularList = allList.parallelStream().filter(a -> subjectId.equals(a.get("parent_id"))).collect(Collectors.toList());
			// 遍历填充一级规则所包括的二级规则
			regularList.stream().forEach(reg -> {
				List<Map<String, Object>> children = allList.stream().filter(a -> a.get("parent_id").equals(reg.get("regular_id"))).collect(Collectors.toList());
				reg.put("children", children);
			});
			data.put("regularList", regularList);
			data.put("subjectName", allList.get(0).get("subject_name"));
		}
		return R.ok().put(Constant.R_DATA, data);
	}

	/**
	 * 新增修改基本信息
	 * @param tevglBookRegular
	 * @param type 为1时标识是父节点
	 * @return
	 */
	@Override
	public R saveOrUpdate(TevglBookRegular tevglBookRegular, String type) {
		if (StrUtils.isEmpty(tevglBookRegular.getParentId())) {
			return R.error("必传参数为空");
		}
		// 合法性校验
		if (checkRegularScore(tevglBookRegular)) {
			return R.error("您输入的规则分数,与其他规则分数之和已经超过了总规则的分数,请重新输入");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("subjectId", tevglBookRegular.getSubjectId());
		if (StrUtils.isEmpty(tevglBookRegular.getRegularId())) {
			// 判断考核名称不能重复
			if (checkNameRegular(tevglBookRegular)) {
				throw new BudaosException(-1, "考核规则名称必须唯一");
			}
			if (tevglBookRegular.getParentId().equals("-1")) {
				tevglBookRegular.setRegularSum(new BigDecimal(0));
				params.put("parentId", "-1");
			} else {
				params.put("parentId", tevglBookRegular.getParentId());
				// 更新父节点的规则总分数
				TevglBookRegular selectObjectById = tevglBookRegularMapper.selectObjectById(tevglBookRegular.getParentId());
				if (selectObjectById != null) {
					if (selectObjectById.getRegularSum() != null && tevglBookRegular.getRegularSum() != null) {
						selectObjectById.setRegularSum(selectObjectById.getRegularSum().add(tevglBookRegular.getRegularSum()));
						tevglBookRegularMapper.update(selectObjectById);
					}
				}
			}
			tevglBookRegular.setRegularId(Identities.uuid());
			// 排序号
			Integer sortNum = tevglBookRegularMapper.getMaxSortNum(params);
			tevglBookRegular.setRegularSort(sortNum);
			// 数据校验
			ValidatorUtils.check(tevglBookRegular);
			tevglBookRegularMapper.insert(tevglBookRegular);
			R.ok().put(Constant.R_DATA, tevglBookRegular);
		} else {
			// 查询旧的规则节点
			TevglBookRegular oldRegularInfo = tevglBookRegularMapper.selectObjectById(tevglBookRegular.getRegularId());
			if (oldRegularInfo == null) {
				return R.error("保存失败");
			}
			if (checkNameRegular(tevglBookRegular)) {
				return R.error("考核规则名称必须唯一");
			}
			// 如果父级节点发生变化 修改移动节点父级节点的分值
			if (!oldRegularInfo.getParentId().equals(tevglBookRegular.getParentId())) {
				Map<String, Object> map = new HashMap<>();
				map.put("subjectId", oldRegularInfo.getSubjectId());
				map.put("parentId", oldRegularInfo.getParentId());
				List<TevglBookRegular> selectListByMap = tevglBookRegularMapper.selectListByMap(map);
				// 如果目标节点下面还有节点 则减去节点的值 否则就设置为0
				TevglBookRegular selectObjectById = tevglBookRegularMapper
						.selectObjectById(oldRegularInfo.getParentId());
				if (selectListByMap.size() > 0) {
					selectObjectById.setRegularSum(
							selectObjectById.getRegularSum().subtract(oldRegularInfo.getRegularSum()));
					tevglBookRegularMapper.update(selectObjectById);
				} else {
					selectObjectById.setRegularSum(BigDecimal.ZERO);
					tevglBookRegularMapper.update(selectObjectById);
				}
				// 更换用户填写选中的最新信息
				tevglBookRegularMapper.update(tevglBookRegular);
				// 更新目标父节点的总分
				// 修改之后的操作 再修改目标节点的父节点总分
				log.debug("更换了父节点为：" + tevglBookRegular.getParentId());
				TevglBookRegular targetSelectObjectById = tevglBookRegularMapper.selectObjectById(tevglBookRegular.getParentId());
				targetSelectObjectById.setRegularSum(targetSelectObjectById.getRegularSum().add(tevglBookRegular.getRegularSum()));
				tevglBookRegularMapper.update(targetSelectObjectById);
			} else {
				// 更新入库
				tevglBookRegularMapper.update(tevglBookRegular);
				// 修改之后的操作 再修改目标节点的父节点总分（修改子规则时）
				if (!"-1".equals(tevglBookRegular.getParentId())) {
					Map<String, Object> map = new HashMap<>();
					map.put("subjectId", tevglBookRegular.getSubjectId());
					map.put("parentId", tevglBookRegular.getParentId());
					List<TevglBookRegular> selectListByMap = tevglBookRegularMapper.selectListByMap(map);
					BigDecimal count = BigDecimal.ZERO;
					for (TevglBookRegular regular : selectListByMap) {
						if (!StrUtils.isNull(regular.getRegularSum())) {
							count = count.add(regular.getRegularSum());
						}
					}
					TevglBookRegular selectObjectById = tevglBookRegularMapper.selectObjectById(tevglBookRegular.getParentId());
					if (selectObjectById != null) {
						selectObjectById.setRegularSum(count);
						tevglBookRegularMapper.update(selectObjectById);
					}
				}
			}
		}
		return R.ok("保存成功").put(Constant.R_DATA, tevglBookRegular);
	}
	
	/**
	 * 验证 具体规则分 是否超过总规则分（总分最高为100）
	 * @param tevglBookRegular
	 */
	private boolean checkRegularScore(TevglBookRegular tevglBookRegular) {
		if ("-1".equals(tevglBookRegular.getParentId())) {
			return false;
		}
		String regularId = tevglBookRegular.getRegularId();
		int totalSum = 100;
		BigDecimal totalScore = BigDecimal.ZERO;
		Map<String, Object> params = new HashMap<>();
		// 用户最新录入的分数
		BigDecimal userRegularSum = tevglBookRegular.getRegularSum();
		// 查出总规则的分数
		params.put("subjectId", tevglBookRegular.getSubjectId());
		params.put("parentId", "-1");
		List<TevglBookRegular> regulars = tevglBookRegularMapper.selectListByMap(params);
		// 新增时校验
		if (StrUtils.isEmpty(regularId)) {
			// 已存在的相加
			for (TevglBookRegular regular : regulars) {
				totalScore = totalScore.add(regular.getRegularSum());
			}
			if (totalScore.intValue() > totalSum) {
				return true;
			}
			// 用户输入的相加
			totalScore = totalScore.add(userRegularSum);
			// 如果超过100
			if (totalScore.intValue() > totalSum) {
				return true;
			}
			return false;
		}
		if (StrUtils.isNotEmpty(regularId)) {
			TevglBookRegular oldRegular = tevglBookRegularMapper.selectObjectById(regularId);
			// 找到当前被修改节点的父节点的子节点们
			List<TevglBookRegular> collect = regulars.stream().filter(a -> a.getRegularId().equals(tevglBookRegular.getParentId())).collect(Collectors.toList());
			if (collect != null && collect.size() > 0) {
				BigDecimal tempValue = collect.get(0).getRegularSum().subtract(oldRegular.getRegularSum()).add(userRegularSum);
				collect.get(0).setRegularSum(tempValue);
			}
			// 累加
			for (TevglBookRegular regular : regulars) {
				totalScore = totalScore.add(regular.getRegularSum());
			}
			log.debug("计算后的值：" + totalScore);
			// 如果超过100
			if (totalScore.intValue() > totalSum) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 总规则下的具体规则的分数最多还能输入多少
	 * @param parentId
	 * @return
	 */
	@Override
	public R queryRemainingScore(String parentId) {
		if (StrUtils.isEmpty(parentId)) {
			return R.error("必传参数为空");
		}
		TevglBookRegular tevglBookRegular = tevglBookRegularMapper.selectObjectById(parentId);
		if (tevglBookRegular == null) {
			return R.error("无效的记录");
		}
		int sum = 0;
		BigDecimal total = BigDecimal.ZERO;
		total = tevglBookRegular.getRegularSum();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subjectId", tevglBookRegular.getSubjectId());
		map.put("parentId", tevglBookRegular.getRegularId());
		List<TevglBookRegular> list = tevglBookRegularMapper.selectListByMap(map);
		for (TevglBookRegular reg : list) {
			BigDecimal num = new BigDecimal(reg.getRegularSum().toString());
			int c = num.intValue();
			sum += c; // 具体规则合计分数
		}
		// 总规则分- 合计分数 = 当前最多输入的输入
		int currInputNum = total.intValue() - sum;
		return R.ok().put(Constant.R_DATA, currInputNum);
	}

	/**
	 * 复制粘贴
	 * @param copySubjectId
	 * @param pasteSubjectId
	 * @return
	 */
	@Override
	public R paste(String copySubjectId, String pasteSubjectId) {
		if (StrUtils.isEmpty(copySubjectId)) {
			return R.error("请选择需要复制的课程");
		}
		if (StrUtils.isEmpty(pasteSubjectId)) {
			return R.error("请选择需要粘贴的课程");
		}
		if (copySubjectId.equals(pasteSubjectId)) {
			return R.error("暂不支持在同课程下复制粘贴");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("subjectId", pasteSubjectId);
		List<TevglBookRegular> pasteRegulars = tevglBookRegularMapper.selectListByMap(params);
		BigDecimal pScore = new BigDecimal("0");
		List<TevglBookRegular> list = pasteRegulars.stream()
			.filter(a -> a.getParentId().equals("-1"))
			.collect(Collectors.toList());
		for (TevglBookRegular tevglBookRegular : list) {
			pScore = pScore.add(tevglBookRegular.getRegularSum());
		}
		if (pScore.intValue() >= 100) {
			return R.error("规则总分将超过100分，无法粘贴");
		}
		// 先查询拷贝课程的所有的规则节点
		params.clear();
		params.put("subjectId", copySubjectId);
		List<TevglBookRegular> copyRegulars = tevglBookRegularMapper.selectListByMap(params);
		// 拿到一级规则
		List<TevglBookRegular> regulars1 = copyRegulars.stream()
				.filter(a -> "-1".equals(a.getParentId()))
				.collect(Collectors.toList());
		// 如果两者分数超过100，则不允许复制
		BigDecimal score = new BigDecimal("0");
		for (TevglBookRegular tevglBookRegular : regulars1) {
			score = score.add(tevglBookRegular.getRegularSum());
		}
		if (score.add(pScore).intValue() > 100) {
			return R.error("规则总分将超过100分，无法粘贴");
		}
		// 遍历一级规则节点
		regulars1.forEach(a -> {
			// 获得未保存前的节点id
			String oldRegularId = a.getRegularId();
			a.setRegularId(Identities.uuid());
			a.setSubjectId(pasteSubjectId);
			a.setParentId("-1");
			tevglBookRegularMapper.insert(a);
			// 获取保存之后的节点id
			String newRegularId = a.getRegularId();
			// 从拷贝课程的所有规则节点下找出二级规则节点
			List<TevglBookRegular> regulars2 = copyRegulars.stream()
					.filter(b -> b.getParentId() != null && b.getParentId().equals(oldRegularId))
					.collect(Collectors.toList());
			regulars2.forEach(b -> {
				b.setRegularId(Identities.uuid());
				b.setSubjectId(pasteSubjectId);
				b.setParentId(newRegularId);
				tevglBookRegularMapper.insert(b);
			});
		});
		return R.ok("操作成功");
	}

	/**
	 * 删除规则
	 * @param regularId
	 * @param type
	 * @return
	 */
	@Override
	public R removeRegular(String regularId, String type) {
		if (StrUtils.isEmpty(regularId)) {
			return R.error("必传参数为空");
		}
		TevglBookRegular tevglBookRegular = tevglBookRegularMapper.selectObjectById(regularId);
		if (tevglBookRegular == null) {
			return R.error("无法删除");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("subjectId", tevglBookRegular.getSubjectId());
		params.put("parentId", tevglBookRegular.getRegularId());
		List<TevglBookRegular> tevglBookRegularList = tevglBookRegularMapper.selectListByMap(params);
		// 如果有子节点，则先删除子节点
		if (tevglBookRegularList != null && tevglBookRegularList.size() > 0) {
			List<String> ids = tevglBookRegularList.stream().map(a -> a.getRegularId()).collect(Collectors.toList());
			tevglBookRegularMapper.deleteBatch(ids.stream().toArray(String[]::new));
		}
		// 如果下面没有节点则只删除当前元素
		tevglBookRegularMapper.delete(regularId);
		// 如果删除的是子节点，则更新上一级规则总分
		if ("03".equals(type)) {
			TevglBookRegular selectObjectById = tevglBookRegularMapper.selectObjectById(tevglBookRegular.getParentId());
			if (selectObjectById != null) {
				selectObjectById.setRegularSum(selectObjectById.getRegularSum().subtract(tevglBookRegular.getRegularSum()));
				tevglBookRegularMapper.update(selectObjectById);
			}
		}
		return R.ok("删除成功");
	}

}
