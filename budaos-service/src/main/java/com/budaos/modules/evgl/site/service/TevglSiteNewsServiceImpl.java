package com.budaos.modules.evgl.site.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.*;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.site.api.TevglSiteNewsService;
import com.budaos.modules.evgl.site.domain.TevglSiteCheckinfo;
import com.budaos.modules.evgl.site.domain.TevglSiteNews;
import com.budaos.modules.evgl.site.persistence.TevglSiteCheckinfoMapper;
import com.budaos.modules.evgl.site.persistence.TevglSiteNewsMapper;
import com.budaos.modules.evgl.site.vo.TevglSiteNewsVo;
import com.budaos.modules.sys.api.TsysAttachService;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class TevglSiteNewsServiceImpl implements TevglSiteNewsService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteNewsServiceImpl.class);
	@Autowired
	private TevglSiteNewsMapper tevglSiteNewsMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private DictUtil dictUtil;
	@Autowired
	private TsysAttachService tsysAttachService;
	
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	
	@Autowired
	private TevglSiteCheckinfoMapper tevglSiteCheckinfoMapper;
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		params.put("scene", "0");
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglSiteNews> tevglSiteNewsList = tevglSiteNewsMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteNewsList, "createUserId", "updateUserId");
		convertUtil.convertOrgId(tevglSiteNewsList, "orgId"); // 所属机构
		convertUtil.convertDict(tevglSiteNewsList, "categoryId", "newsCategory"); // 类别
		convertUtil.convertDict(tevglSiteNewsList, "status", "newsStatus"); // 状态
		convertUtil.convertDict(tevglSiteNewsList, "deployed", "deployed"); // 发布方式
		convertUtil.convertDict(tevglSiteNewsList, "noone", "isHeadlineNews"); // 是否头条资讯
		// 图片处理
		tevglSiteNewsList.forEach(a -> {
			a.setNewsLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("6") + "/" + a.getNewsLogo());
		});
		PageUtils pageUtil = new PageUtils(tevglSiteNewsList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglSiteNewsList = tevglSiteNewsMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglSiteNewsList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglSiteNewsList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglSiteNews
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglSiteNews tevglSiteNews) throws BudaosException {
		tevglSiteNews.setNewsid(Identities.uuid());
		tevglSiteNews.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteNews.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglSiteNews);
		tevglSiteNewsMapper.insert(tevglSiteNews);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglSiteNews
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglSiteNews tevglSiteNews) throws BudaosException {
	    tevglSiteNews.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglSiteNews.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglSiteNews);
		tevglSiteNewsMapper.update(tevglSiteNews);
		return R.ok();
	}
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @param tevglSiteNews
	 * @param attachId
	 * @return
	 * @throws BudaosException
	 */
	@Override
	@SysLog(value="新增")
	public R save2( TevglSiteNews tevglSiteNews, String attachId) throws BudaosException {
		String id = Identities.uuid();
		tevglSiteNews.setNewsid(id);
		tevglSiteNews.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteNews.setCreateTime(DateUtils.getNowTimeStamp());
		tevglSiteNews.setUpdateTime(DateUtils.getNowTimeStamp());
		// TODO 由于暂时未有审核功能，状态直接默认为已发布，状态1待审2已发布 3删除
		// 发布方式来源字段 deployed
		if ("2".equals(tevglSiteNews.getDeployed())) {
			tevglSiteNews.setStatus("1"); // 0810调整为待审
		} else {
			tevglSiteNews.setStatus("2");
		}
		ValidatorUtils.check(tevglSiteNews);
		tevglSiteNews.setScene("0");
		tevglSiteNewsMapper.insert(tevglSiteNews);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, id, "1", "6");
		}
		return R.ok();
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @param tevglSiteNews
	 * @param attachId
	 * @return
	 * @throws BudaosException
	 */
	@Override
	@SysLog(value="修改")
	public R update2(TevglSiteNews tevglSiteNews, String attachId) throws BudaosException {
		tevglSiteNews.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglSiteNews.setUpdateTime(DateUtils.getNowTimeStamp());
	    // TODO 由于暂时未有审核功能，状态直接默认为已发布，状态1待审2已发布 3删除
	    if ("2".equals(tevglSiteNews.getDeployed())) {
			tevglSiteNews.setStatus("1"); // 0810调整为待审
		} else {
			tevglSiteNews.setStatus("2");
		}
	    ValidatorUtils.check(tevglSiteNews);
		tevglSiteNewsMapper.update(tevglSiteNews);
		// 如果上传了资源文件
		if (attachId != null && !"".equals(attachId)) {
			tsysAttachService.updateAttach(attachId, tevglSiteNews.getNewsid(), "0", "6");
		}
		return R.ok();
	}
	
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete( String id) throws BudaosException {
		tevglSiteNewsMapper.delete(id);
		return R.ok();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch( String[] ids) throws BudaosException {
		tevglSiteNewsMapper.deleteBatch(ids);
		for (int i = 0; i < ids.length; i++) {			
			List<String> list = tevglSiteNewsMapper.selectIdListByParentId(ids[i]);
			if (list != null && list.size() > 0) {
				tevglSiteNewsMapper.deleteBatch(list.stream().toArray(String[]::new));
			}
		}
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view( String id) {
		TevglSiteNews news = tevglSiteNewsMapper.selectObjectById(id);
		if (news == null) {
			return R.ok().put(Constant.R_DATA, new TevglSiteNews());
		}
		// 浏览量+1
		TevglSiteNews t = new TevglSiteNews();
		t.setNewsid(news.getNewsid());
		t.setViewNum(1);
		tevglSiteNewsMapper.plusNum(t);
		news.setNewsLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("6") + "/" + news.getNewsLogo());
		return R.ok().put(Constant.R_DATA, news)
				.put("previous", selectupNewsInfo(news) == null || selectupNewsInfo(news).size() == 0 ? new TevglSiteNews() : selectupNewsInfo(news).get(0))
				.put("next", selectdownNewsInfo(news) == null || selectdownNewsInfo(news).size() == 0 ? new TevglSiteNews() : selectdownNewsInfo(news).get(0));
	}
	
	@Override
	public R viewForWx(Object id) {
		TevglSiteNews news = tevglSiteNewsMapper.selectObjectById(id);
		if (news == null) {
			return R.ok().put(Constant.R_DATA, null);
		}
		// 浏览量+1
		TevglSiteNews t = new TevglSiteNews();
		t.setNewsid(news.getNewsid());
		t.setViewNum(1);
		tevglSiteNewsMapper.plusNum(t);
		news.setNewsLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("6") + "/" + news.getNewsLogo());
		// 不需要这个条件
		news.setCategoryId(null);
		return R.ok().put(Constant.R_DATA, news)
				.put("previous", selectupNewsInfo(news) == null || selectupNewsInfo(news).size() == 0 ? new TevglSiteNews() : selectupNewsInfo(news).get(0))
				.put("next", selectdownNewsInfo(news) == null || selectdownNewsInfo(news).size() == 0 ? new TevglSiteNews() : selectdownNewsInfo(news).get(0));
	}
	
	public List<TevglSiteNews> selectupNewsInfo(TevglSiteNews tevglSiteNews){
		List<TevglSiteNews> list = tevglSiteNewsMapper.selectupNewsInfo(tevglSiteNews);
		if (list.size() > 0) {
			list.forEach(a -> {
				a.setNewsLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("6") + "/" + a.getNewsLogo());
			});
		}
		return list;
	}
	
	public List<TevglSiteNews> selectdownNewsInfo(TevglSiteNews tevglSiteNews){
		List<TevglSiteNews> list = tevglSiteNewsMapper.selectdownNewsInfo(tevglSiteNews);
		if (list.size() > 0) {
			list.forEach(a -> {
				a.setNewsLogo(budaosFieAccessPath + uploadPathUtils.getPathByParaNo("6") + "/" + a.getNewsLogo());
			});
		}
		return list;
	}

	@Override
	@SysLog(value="查看明细")
	public R toEdit( String id) {
		TevglSiteNews tevglSiteNews = tevglSiteNewsMapper.selectObjectById(id);
		String newsLogo = "";
		if (tevglSiteNews != null) {
			newsLogo = budaosFieAccessPath + uploadPathUtils.getPathByParaNo("6") + "/"  + tevglSiteNews.getNewsLogo();	
		}
		return R.ok().put(Constant.R_DATA, tevglSiteNews)
				.put("newsLogo", newsLogo);
	}

	
	/**
	 * <p>从字典获取新增资讯分类,如企业新闻,行业新闻等</p>
	 * @author huj
	 * @data 2019年7月16日
	 * @return
	 */
	@Override
	public R getCategory() {
		List<TsysDict> list = new ArrayList<TsysDict>();
		TsysDict t = new TsysDict();
		t.setDictId("1");
		t.setDictName("全部");
		t.setDictValue("全部");
		t.setDictCode("");
		t.setIsdefault("1");
		list.add(t);
		List<TsysDict> dictList = dictUtil.getByType("newsCategory");
		list.addAll(dictList);
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * <p>从字典获取发布方式,如直接发布、审核发布</p>
	 * @author huj
	 * @data 2019年7月20日
	 * @return
	 */
	@Override
	public List<TsysDict> getDeployed() {
		List<TsysDict> list = dictUtil.getByType("deployed");
		return list;
	}

	/**
	 * <p>从字典获取是否头条咨询</p>
	 * @author huj
	 * @data 2019年7月20日
	 * @return
	 */
	@Override
	public List<TsysDict> getIsHeadlineNews() {
		List<TsysDict> list = dictUtil.getByType("isHeadlineNews");
		return list;
	}

	/**
	 * <p>从字典获取状态，状态1待审2已发布 3删除</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @return
	 */
	@Override
	public List<TsysDict> getNewsStatus() {
		List<TsysDict> list = dictUtil.getByType("newsStatus");
		return list;
	}

	/**
	 * <p>从字典获取是否展示Y显示N隐藏</p>
	 * @author huj
	 * @data 2019年7月23日
	 * @return
	 */
	@Override
	public List<TsysDict> getIsShow() {
		List<TsysDict> list = dictUtil.getByType("isShow");
		return list;
	}

	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月28日
	 * @param tevglSiteNews
	 * @return
	 */
	@Override
	public R updateState(TevglSiteNews tevglSiteNews) {
		if (tevglSiteNews == null) {
			return R.error("操作失败");
		}
		if (tevglSiteNews.getNewsid() == null || "".equals(tevglSiteNews.getNewsid())) {
			return R.error("操作失败");
		}
		tevglSiteNewsMapper.update(tevglSiteNews);
		return R.ok();
	}

	/**
	 * <p>审核</p>  
	 * @author huj
	 * @data 2019年8月10日	
	 * @param ids
	 * @return
	 */
	@Override
	public R check( String[] ids, String state, String reason) {
		if (ids == null || ids.length == 0) {
			return R.ok();
		}
		if (StrUtils.isEmpty(state)) {
			return R.ok("请选择是否通过");
		}
		List<String> newsid = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			newsid.add(ids[i]);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("newsid", newsid);
		map.put("status", state); // 状态1待审2已发布 3删除 4未通过
		tevglSiteNewsMapper.check(map);
		// 保存审核记录
		if (newsid.size() > 0) {
			newsid.forEach(a -> {
				TevglSiteCheckinfo t = new TevglSiteCheckinfo();
				t.setId(Identities.uuid());
				t.setCreateUserId(serviceLoginUtil.getLoginUserId());
				t.setCreateTime(DateUtils.getNowTimeStamp());
				t.setRefId(a);
				t.setType("1"); // 审核类型(1新闻2广告3等等等)
				t.setState("Y"); // 状态(Y有效N无效)
				t.setReason(reason); // 理由
				if ("4".equals(state)) {
					t.setPass("N");
				} else if ("2".equals(state)) {
					t.setPass("Y");
				}
				tevglSiteCheckinfoMapper.insert(t);
			});
		}
		return R.ok();
	}

	/**
	 * 查询部分信息，且对象为驼峰命名
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectSimpleListByMap(Map<String, Object> params) {
		List<Map<String,Object>> list = tevglSiteNewsMapper.selectSimpleListByMap(params);
		if (list != null && list.size() > 0) {
			// 图片处理
			list.forEach(news -> {
				news.put("newsLogo", budaosFieAccessPath + uploadPathUtils.getPathByParaNo("6") + "/" + news.get("newsLogo"));
			});
		}
		return list;
	}

	/**
	 * 新闻列表（公众号情况下使用）
	 *
	 * @param params
	 * @return
	 */
	@Override
	public R queryForOfficial(Map<String, Object> params) {
		params.put("scene", "1");
		params.put("parentId", "-1");
		Query query = new Query(params);
		List<TevglSiteNewsVo> list = tevglSiteNewsMapper.selectListByMapForOfficial(params);
		convertUtil.convertDict(list, "statusName", "newsStatus"); // 状态
		convertUtil.convertDict(list, "officialLinkType", "official_link_type"); // 类型
		list.stream().forEach(item -> {
			params.put("scene", "1");
			params.put("parentId", item.getNewsid());
			params.put("sidx", "t1.sort_num");
			params.put("order", "asc");
			List<TevglSiteNewsVo> children = tevglSiteNewsMapper.selectListByMapForOfficial(params);
			children.stream().forEach(childrenItem -> {
				childrenItem.setNewsLogo(uploadPathUtils.stitchingPath(childrenItem.getNewsLogo(), "6"));
			});
			item.setChildren(children);
			item.setNewsLogo(uploadPathUtils.stitchingPath(item.getNewsLogo(), "6"));
			// 处理时间
			Date date = com.budaos.modules.common.DateUtils.convertStringToDate(item.getCreateTime());
			item.setTime(com.budaos.modules.common.DateUtils.getTime(date));
		});
		PageUtils pageUtils = new PageUtils(list, query.getPage(), query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtils);
	}

	/**
	 * 新增（公众号情况下使用）
	 *
	 * @param vo
	 * @return
	 */
	@Override
	@SysLog("新增资讯（公众号情况下使用）")
	@Transactional
	public R saveForOfficial(TevglSiteNewsVo vo) {
		String status = StrUtils.isEmpty(vo.getStatus()) ? "1" : vo.getStatus();
		vo.setNewsid(Identities.uuid());
		ValidatorUtils.check(vo);
		List<TevglSiteNewsVo> children = vo.getChildren();
		for (int j = 0; j < children.size(); j++) {
			if (StrUtils.isEmpty(children.get(j).getNewsLogo())) {
				return R.error("当前有封面为空，请上传");
			}
		}
		List<TevglSiteNews> list = new ArrayList<>();
		Stream.iterate(0, i -> i + 1).limit(children.size()).forEach(i -> {
			TevglSiteNewsVo item = children.get(i);
			item.setNewTitle(StrUtils.isEmpty(item.getNewTitle()) ? "标题" : item.getNewTitle());
			TevglSiteNews t = new TevglSiteNews();
			t.setNewsid(Identities.uuid());
			item.setNewsid(t.getNewsid());
			t.setParentId(vo.getNewsid());
			t.setNewTitle(item.getNewTitle());
			t.setAuthor(item.getAuthor());
			t.setNewsLogo(item.getNewsLogo());
			t.setContent(item.getContent());
			t.setSortNum(i);
			t.setStatus(status);
			t.setScene("1");
			t.setViewNum(1);
			t.setOfficialLinkType(vo.getOfficialLinkType());
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setCreateUserId(serviceLoginUtil.getLoginUserId());
			list.add(t);
		});
		TevglSiteNews t = new TevglSiteNews();
		t.setNewsid(vo.getNewsid());
		t.setParentId("-1");
		t.setNewTitle(vo.getNewTitle());
		t.setNewsLogo(vo.getNewsLogo());
		t.setAuthor(vo.getAuthor());
		t.setContent(vo.getContent());
		t.setStatus(status);
		t.setScene("1");
		t.setViewNum(1);
		t.setOfficialLinkType(vo.getOfficialLinkType());
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setCreateUserId(serviceLoginUtil.getLoginUserId());
		list.add(t);
		if (list.size() > 0) {
			tevglSiteNewsMapper.insertBatch(list);
		}
		// 原样返回数据，便于前端回显数据
		return R.ok("操作成功").put(Constant.R_DATA, vo);
	}


	/**
	 * 修改（公众号情况下使用）
	 *
	 * @param vo
	 * @return
	 */
	@Override
	@SysLog("新增资讯（公众号情况下使用）")
	@Transactional
	public R updateForOfficial(TevglSiteNewsVo vo) {
		String status = StrUtils.isEmpty(vo.getStatus()) ? "1" : vo.getStatus();
		ValidatorUtils.check(vo);
		// 先找到已经存在的
		List<String> existedIdList = tevglSiteNewsMapper.selectIdListByParentId(vo.getNewsid());
		List<String> userInputIdList = new ArrayList<String>();
		List<TevglSiteNewsVo> children = vo.getChildren();
		for (int j = 0; j < children.size(); j++) {
			if (StrUtils.isEmpty(children.get(j).getNewsLogo())) {
				return R.error("当前有封面为空，请上传");
			}
		}
		List<TevglSiteNews> list = new ArrayList<>();
		Stream.iterate(0, i -> i + 1).limit(children.size()).forEach(i -> {
			TevglSiteNewsVo item = children.get(i);
			item.setNewTitle(StrUtils.isEmpty(item.getNewTitle()) ? "标题" : item.getNewTitle());
			TevglSiteNews t = new TevglSiteNews();
			t.setParentId(vo.getNewsid());
			t.setNewTitle(item.getNewTitle());
			t.setAuthor(item.getAuthor());
			t.setNewsLogo(item.getNewsLogo());
			t.setContent(item.getContent());
			t.setSortNum(i);
			t.setStatus(status);
			t.setScene("1");
			t.setOfficialLinkType(vo.getOfficialLinkType());
			if (StrUtils.isEmpty(item.getNewsid())) {
				t.setNewsid(Identities.uuid());
				t.setCreateTime(DateUtils.getNowTimeStamp());
				t.setCreateUserId(serviceLoginUtil.getLoginUserId());
				list.add(t);
			} else {
				t.setNewsid(item.getNewsid());
				item.setNewsid(t.getNewsid());
				t.setUpdateTime(DateUtils.getNowTimeStamp());
				tevglSiteNewsMapper.update(t);
				userInputIdList.add(item.getNewsid());
			}
		});
		if (list.size() > 0) {
			tevglSiteNewsMapper.insertBatch(list);
		}
		if (existedIdList.size() > 0) {
			existedIdList.removeAll(userInputIdList);
			if (existedIdList.size() > 0) {
				tevglSiteNewsMapper.deleteBatch(existedIdList.stream().toArray(String[]::new));
			}
		}
		TevglSiteNews t = new TevglSiteNews();
		t.setNewsid(vo.getNewsid());
		t.setParentId("-1");
		t.setNewTitle(vo.getNewTitle());
		t.setNewsLogo(vo.getNewsLogo());
		t.setAuthor(vo.getAuthor());
		t.setContent(vo.getContent());
		t.setOfficialLinkType(vo.getOfficialLinkType());
		t.setStatus(status);
		t.setScene("1");
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglSiteNewsMapper.update(t);
		// 原样返回数据，便于前端回显数据
		return R.ok("操作成功").put(Constant.R_DATA, vo);
	}

	/**
	 * 查看（公众号情况下使用）
	 *
	 * @param newsid
	 * @return
	 */
	@Override
	public R viewForOfficial(String newsid) {
		TevglSiteNewsVo tevglSiteNewsVo = tevglSiteNewsMapper.selectTevglSiteNewsVoById(newsid);
		if (tevglSiteNewsVo == null) {
			return R.error("该记录已不存在！");
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("scene", "1");
		params.put("parentId", tevglSiteNewsVo.getNewsid());
		params.put("sidx", "sort_num");
		params.put("order", "asc");
		List<TevglSiteNewsVo> children = tevglSiteNewsMapper.selectListByMapForOfficial(params);
		children.stream().forEach(childrenItem -> {
			childrenItem.setNewsLogo(uploadPathUtils.stitchingPath(childrenItem.getNewsLogo(), "6"));
		});
		tevglSiteNewsVo.setChildren(children);
		tevglSiteNewsVo.setNewsLogo(uploadPathUtils.stitchingPath(tevglSiteNewsVo.getNewsLogo(), "6"));
		return R.ok().put(Constant.R_DATA, tevglSiteNewsVo);
	}

	@Override
	public R viewDetailForOfficial(String newsid, String traineeId) {
		TevglSiteNewsVo tevglSiteNewsVo = tevglSiteNewsMapper.selectTevglSiteNewsVoById(newsid);
		if (tevglSiteNewsVo == null) {
			return R.error("该记录已不存在！");
		}
		if (!"2".equals(tevglSiteNewsVo.getStatus())) {
			return R.error("暂时无法查看");
		}
		String key = "news:" + newsid + ":" + traineeId;
		String s = redisTemplate.opsForValue().get(key);
		if (StrUtils.isEmpty(s)) {
			redisTemplate.opsForValue().set(key, "1");
			TevglSiteNews t = new TevglSiteNews();
			t.setNewsid(tevglSiteNewsVo.getNewsid());
			t.setViewNum(1);
			tevglSiteNewsMapper.plusNum(t);
		}
		tevglSiteNewsVo.setNewsLogo(uploadPathUtils.stitchingPath(tevglSiteNewsVo.getNewsLogo(), "6"));
		return R.ok().put(Constant.R_DATA, tevglSiteNewsVo);
	}

	/**
	 * 更新状态
	 * @param newsid
	 * @param status
	 * @return
	 */
	@Override
	public R release(String newsid, String status) {
		if (StrUtils.isEmpty(newsid) || StrUtils.isEmpty(status)) {
			return R.error("必传参数为空");
		}
		List<String> stringList = tevglSiteNewsMapper.selectIdListByParentId(newsid);
		if (stringList == null) {
			stringList = new ArrayList<>();
		}
		stringList.add(newsid);
		tevglSiteNewsMapper.updateStateBatch(stringList, status);
		return R.ok("操作成功");
	}

	/**
	 * 批量更新状态
	 * @param newidList
	 * @param status
	 * @return
	 */
	@Override
	public R releaseBatch(String[] newidList, String status) {
		if (newidList == null || newidList.length <= 0 || StrUtils.isEmpty(status)) {
			return R.error("必传参数为空");
		}
		tevglSiteNewsMapper.updateStateBatch(Stream.of(newidList).collect(Collectors.toList()), status);
		return R.ok("操作成功");
	}
	

}
