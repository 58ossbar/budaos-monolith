package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysDocs;

import java.util.List;
import java.util.Map;

/**
 * <p>帮助文档api</p>
 * <p>Title: TsysDocsService.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖布道师学习通</p> 
 * @author huj
 * @date 2019年5月9日
 */
public interface TsysDocsService {

	/**
	 * <p>根据条件查询记录</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param map
	 * @return
	 */
	R query(Map<String, Object> map);
	List<TsysDocs> tree();
	
	/**
	 * <p>注:原ModelAndView下进入新增页面的方法</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @return
	 */
	R add();
	
	/**
	 * <p>注:原ModelAndView下进入修改页面的方法</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @return
	 */
	R edit();
	
	/**
	 * <p>执行数据保存和修改</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysDocs
	 * @return
	 */
	R saveOrUpdate(TsysDocs tsysDocs);
	
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysDocs
	 * @return
	 */
	void save(TsysDocs tsysDocs);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysDocs
	 * @return
	 */
	void update(TsysDocs tsysDocs);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	R delete(String id);
	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param ids
	 * @return
	 */
	R deleteBatch(String[] ids);
	
	/**
	 * <p>查看明细</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	R view(String id);
	
	/**
	 * <p>重命名</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param docs
	 * @return
	 */
	R rename(TsysDocs docs);
	
	/**
	 * <p>移动</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param moveType
	 * @param docId
	 * @param tagDocId
	 * @return
	 */
	R move(String moveType, String docId, String tagDocId);
	
	/**
	 * <p>同步数据</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	R syndata(String id);
	
	/**
	 * <p>根据文档ID，查询文档所有节点数据</p>
	 * @author huj
	 * @data 2019年5月9日
	 * @return
	 */
	TsysDocs selectDocsDetailById(String docId);
}
