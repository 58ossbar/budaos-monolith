package com.budaos.modules.evgl.question.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.question.domain.TepExaminePaperInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:试卷表基本信息
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author zhujw
 * @version 1.0
 */

@Mapper
public interface TepExaminePaperInfoMapper extends BaseSqlMapper<TepExaminePaperInfo> {

	// 根据条件查询记录，返回List<Map>
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);

	/**
	 * 
	 * @desc //TODO 更新试卷的作答数
	 * @author huangwb
	 * 
	 * @data 2019年3月7日 下午4:20:54
	 */
	void plusNum(TepExaminePaperInfo tepExaminePaperInfo);
	
	/**
	 * <p>根据条件查询记录</p>  
	 * @author huj
	 * @data 2019年12月12日	
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListByMapForWeb(Map<String, Object> map);
	
	/**
	 * <p>根据条件获取日期</p>  
	 * @author huj
	 * @data 2020年1月11日	
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getDates(Map<String, Object> map);
	
	TepExaminePaperInfo selectObjectByIdAndPkgId(Map<String, Object> map);
	
	TepExaminePaperInfo selectByPaperId(String paperId);
	
	TepExaminePaperInfo selectByQuestions(String paperId);
	
	List<Map<String, Object>> selectTestPaperList(Map<String, Object> params);
	List<Map<String, Object>> selectTestPaperListNew(Map<String, Object> params);
	
	/**
	 * 截取试卷的创建时间并去重
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryTime(Map<String, Object> params);
	
	/**
	 * 查询试卷的查看答案时机
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectViewResultTime(Map<String, Object> params);
	
	/**
	 * 根据条件，仅查询主键id
	 * @param params
	 * @return
	 */
	List<String> selectPaperIdList(Map<String, Object> params);
	
	/**
	 * 试卷列表
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryPaperListByUnionAll(Map<String, Object> params);
}