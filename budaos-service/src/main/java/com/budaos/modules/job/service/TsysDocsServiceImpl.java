package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysDocsMapper;
import com.budaos.modules.sys.api.TsysDocsService;
import com.budaos.modules.sys.domain.TsysDocs;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>帮助文档api的实现类</p>
 * <p>Title: TsysDocsServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 布道师学习通</p> 
 * @author huj
 * @date 2019年5月9日
 */
@Service
public class TsysDocsServiceImpl implements TsysDocsService {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TsysDocsMapper tsysDocsMapper;

	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param map
	 * @return
	 */
	@Override
	public R query(Map<String, Object> map) {
		// todo
		List<TsysDocs> tsysDocsList = tsysDocsMapper.selectListByMap(map);
		return R.ok().put("page", tsysDocsList);
	}

	@Override
	public List<TsysDocs> tree() {
		List<TsysDocs> tsysDocsList = tsysDocsMapper.selectListByMap(new HashMap<String, Object>());
		for (int i = 0; i < tsysDocsList.size(); i++) {
			TsysDocs a = tsysDocsList.get(i);
			if ("01".equals(a.getDocType())) {
				continue;
			}
			StringBuffer sb = new StringBuffer();
			findAllParent(tsysDocsList, a, sb);
			if (a.getDocSort() != null) {
				if (sb.toString().length() > 0) {
					List<String> strings = Arrays.asList(sb.toString().split("\\."));
					Collections.reverse(strings);
					a.setDocName(
							StrUtils.join(strings.toArray(), ".", "") + "." + a.getDocSort() + " " + a.getDocName());
				} else {
					a.setDocName("第" + a.getDocSort() + "章 " + a.getDocName());
				}
			}
		}
		return tsysDocsList;
	}

	/**
	 * <p>注:原ModelAndView下进入新增页面的方法</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @return
	 */
	@Override
	public R add() {
		return R.ok().put("tsysDocs", new TsysDocs());
	}

	/**
	 * <p>注:原ModelAndView下进入修改页面的方法</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @return
	 */
	@Override
	public R edit() {
		Object repeattoken = request.getSession(true).getAttribute("repeattoken");
		TsysDocs tsysDocs = tsysDocsMapper.selectObjectById(request.getParameter("id"));
		return R.ok().put("repeattoken", repeattoken).put("tsysDocs", tsysDocs);
	}

	/**
	 * <p>执行数据保存和修改</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysDocs
	 * @return
	 */
	@Override
	public R saveOrUpdate(TsysDocs tsysDocs) {
		try {
			// 数据校验
			//ValidatorUtils.check(tsysDocs);
			 if ((tsysDocs.getDocId() == null) || ("").equals(tsysDocs.getDocId())) { //新增
		    	//TsysUserinfo userinfo = getUser();
				TsysUserinfo userinfo = new TsysUserinfo(); // 临时存在的，记得删除
		    	tsysDocs.setMenuId(tsysDocs.getSystemId());
		    	tsysDocs.setDocId(Identities.uuid());
		    	tsysDocs.setMainDoc(tsysDocs.getDocId());
		    	tsysDocs.setParentId("-1");
		    	tsysDocs.setDocClass("-1");
		    	tsysDocs.setOrgId(userinfo.getOrgId());
		    	tsysDocs.setDisplay("Y");
			    //如果是子系统帮助文档，根据子系统菜单结构自动生成文档结构
			    if("01".equals(tsysDocs.getDocType())){
			    	if(StrUtils.isNotEmpty(tsysDocs.getSystemId())){
			    		//获取子系统下的所有菜单节点
						/*List<TsysResource> ress = tsysResourceService.selectSubSysListWithMenu(userinfo.getUserId(), tsysDocs.getSystemId(), tsysDocs.getDocId());
						for(TsysResource res : ress){
							//如果该菜单类型是按钮或子系统，则跳过
							if(MenuType.BUTTON.equals(res.getType()) || MenuType.SYS.equals(res.getType())){
								continue;
							}else{
								TsysDocs doc = new TsysDocs();
								doc.setDocId(res.getParentName());
								doc.setMenuId(res.getMenuId());
								doc.setSystemId(tsysDocs.getSystemId());
								doc.setMainDoc(tsysDocs.getDocId());
								doc.setDisplay("Y");
								doc.setParentId(res.getParentId());
								doc.setOrgId(userinfo.getOrgId());
								doc.setDocName(res.getName());
								doc.setDocType(tsysDocs.getDocType());
								doc.setDocClass(String.valueOf(res.getType()));
								doc.setDocSort(res.getOrderNum()==null?null:res.getOrderNum());
								save(doc);
							}
						}*/
			    	}
			    }
				save(tsysDocs);
			} else {
				update(tsysDocs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof BudaosException){
				throw e;
			}else{
				throw new BudaosException(-1,"保存失败");
			}
		}
		String uid = UUID.randomUUID().toString();
		request.getSession(false).setAttribute(Constant.TOKEN_FORM, uid);
		return R.ok().put(Constant.TOKEN_FORM, uid).put("docId", tsysDocs.getDocId());
	}

	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysDocs
	 */
	@Override
	@SysLog(value="新增")
	public void save(TsysDocs tsysDocs) {
		if (StrUtils.isEmpty(tsysDocs.getDocId())) {
			tsysDocs.setDocId(Identities.uuid());
		}
		tsysDocs.setCreateTime(DateUtils.getNowTimeStamp());
		/*Object obj = SecurityUtils.getSubject().getPrincipal();
		if (obj != null) {
			tsysDocs.setCreateUserId(((TsysUserinfo) obj).getUserId());
		}*/
		tsysDocsMapper.insert(tsysDocs);

	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysDocs
	 */
	@Override
	@SysLog(value="修改")
	public void update(TsysDocs tsysDocs) {
		tsysDocs.setUpdateTime(DateUtils.getNowTimeStamp());
		/*Object obj = SecurityUtils.getSubject().getPrincipal();
		if(obj != null){
			tsysDocs.setUpdateUserId(((TsysUserinfo) obj).getUserId());
		}*/
		tsysDocsMapper.update(tsysDocs);
	}

	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	@Override
	@SysLog("删除帮助文档")
	@Transactional
	public R delete(String id) {
		try {
			if (StrUtils.isEmpty(id)) {
				return R.error("找不到要被删除的记录");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("parentId", id);
			List<TsysDocs> list = tsysDocsMapper.selectListByMap(map);
			for (TsysDocs doc : list) {
				delete(doc.getDocId());
			}
			tsysDocsMapper.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("删除失败");
		}
		return R.ok();
	}

	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param ids
	 * @return
	 */
	@Override
	@SysLog("删除帮助文档")
	public R deleteBatch(String[] ids) {
		try {
			tsysDocsMapper.deleteBatch(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("批量删除失败");
		}
		return R.ok();
	}
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	@Override
	public R view( String id) {
		TsysDocs tsysDocs = tsysDocsMapper.selectObjectById(id);
		return R.ok().put("tsysDocs", tsysDocs);
	}

	/**
	 * <p>重命名</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param docs
	 * @return
	 */
	@Override
	public R rename(TsysDocs docs) {
		String docName = docs.getDocName();
		if (StrUtils.isEmpty(docName)) {
			return R.ok();
		}
		String[] names = docName.split(" ");
		if (names.length > 1) {
			docs.setDocName(names[1]);
		}
		if (StrUtils.isEmpty(docs.getDocId())) {
			TsysDocs pDoc = tsysDocsMapper.selectObjectById(docs.getParentId());
			docs.setOrgId(pDoc.getOrgId());
			docs.setSystemId(pDoc.getSystemId());
			docs.setDocType(pDoc.getDocType());
			docs.setDocClass(String.valueOf(Integer.parseInt(pDoc.getDocClass()) + 1));
			docs.setDisplay("Y");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("parentId", docs.getParentId());
			docs.setDocSort(tsysDocsMapper.selectMaxSortByMap(map) + 1);
			save(docs);
		} else {
			update(docs);
		}
		return R.ok();
	}

	/**
	 * <p>移动</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param moveType
	 * @param docId
	 * @param tagDocId
	 * @return
	 */
	@Override
	public R move(String moveType, String docId, String tagDocId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>同步数据</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	@Override
	public R syndata(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	private void findAllParent(List<TsysDocs> tsysDocsList, TsysDocs doc, StringBuffer sb) {
		tsysDocsList.forEach(a -> {
			if (doc.getParentId().equals(a.getDocId()) && a.getDocSort() != null) {
				sb.append(a.getDocSort() + ".");
				findAllParent(tsysDocsList, a, sb);
			}
		});
	}

	/**
	 * <p>根据文档ID，查询文档所有节点数据</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param docId
	 * @return
	 */
	@Override
	public TsysDocs selectDocsDetailById(String docId) {
		TsysDocs doc = tsysDocsMapper.selectObjectById(docId);
		if (doc == null) {
			return null;
		}
		initChildren(doc);
		return doc;
	}

	/**
	 * <p>初始化子节点数据</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param doc
	 */
	private void initChildren(TsysDocs doc) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", doc.getDocId());
		List<TsysDocs> docs = tsysDocsMapper.selectListSimpleByMap(map);
		if (docs.size() > 0) {
			docs.forEach(a -> {
				initChildren(a);
			});
			doc.setChildrens(docs);
		}
	}


}
