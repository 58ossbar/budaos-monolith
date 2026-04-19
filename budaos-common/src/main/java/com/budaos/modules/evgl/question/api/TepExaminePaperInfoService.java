package com.budaos.modules.evgl.question.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.question.domain.TepExaminePaperInfo;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 试卷表基本信息</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TepExaminePaperInfoService extends IBaseService<TepExaminePaperInfo> {

	/**
	 * <p>试卷列表</p>  
	 * @author huj
	 * @data 2019年12月12日	
	 * @param params
	 * @return
	 */
	R queryPapers(Map<String, Object> params);

	/**
	 * <p>明细</p>  
	 * @author huj
	 * @data 2019年12月30日	
	 * @param paperId 试卷ID
	 * @return
	 */
	TepExaminePaperInfo selectObjectById(String paperId);
	
	/**
	 * <p>更新试卷作答数</p>  
	 * @author huj
	 * @data 2020年1月6日	
	 * @param tepExaminePaperInfo
	 */
	void plusNum(TepExaminePaperInfo tepExaminePaperInfo);
	
	List<TepExaminePaperInfo> selectListByMap(Map<String, Object> map);
	
	/**
	 * <p>根据条件获取日期</p>  
	 * @author huj
	 * @data 2020年1月11日	
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getDates(Map<String, Object> map);
	
}
