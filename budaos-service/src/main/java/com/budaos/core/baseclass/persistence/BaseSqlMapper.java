package com.budaos.core.baseclass.persistence;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Title: 数据库持久化基类 Description: Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Mapper
public interface BaseSqlMapper<T> {
	/**
	 * 新增記錄
	 * 
	 * @param t
	 */
	public void insert(T t);

	/**
	 * 添加 记录
	 * 
	 * @param map
	 */
	public void insert(Map<String, Object> map);

	/**
	 * 批量添加记录
	 * 
	 * @param list
	 */
	public void insertBatch(List<T> list);

	/**
	 * 修改記錄
	 * 
	 * @param t
	 * @return
	 */
	public int update(T t);

	/**
	 * 修改記錄
	 * 
	 * @param map
	 * @return
	 */
	public int update(Map<String, Object> map);

	/**
	 * 刪除記錄
	 * 
	 * @param id
	 * @return
	 */
	public int delete(Object id);

	/**
	 * 刪除記錄
	 * 
	 * @param map
	 * @return
	 */
	public int delete(Map<String, Object> map);

	/**
	 * 批量刪除記錄
	 * 
	 * @param id
	 * @return
	 */
	public int deleteBatch(Object[] id);

	/**
	 * 查詢數據
	 * 
	 * @param id
	 * @return
	 */
	public T selectObjectById(Object id);

	/**
	 * 查詢數據
	 * 
	 * @param map
	 * @return
	 */
	public List<T> selectListByMap(Map<String, Object> map);

	public List<T> selectListById(Object id);

	public int selectTotalByMap(Map<String, Object> map);

	public int selectTotal();

}
