package com.budaos.core.baseclass.api;

import com.budaos.core.baseclass.domain.R;

import java.util.Map;

/**
 * 所有微服务的接口的父接口
 * 
 * @author zhuq
 *
 * @param <T>
 */
public interface IBaseService<T> {

	/**
	 * <p>
	 * 根据条件查询记录
	 * </p>
	 * 
	 * @param Map
	 * @return R
	 */
	R query(Map<String, Object> map);

	/**
	 * <p>
	 * 根据条件查询记录
	 * </p>
	 * 
	 * @param Map
	 * @return R
	 */
	R queryForMap(Map<String, Object> map);

	/**
	 * <p>
	 * 新增
	 * </p>
	 * 
	 * @param T
	 * @return R
	 */
	R save(T t);

	/**
	 * <p>
	 * 修改
	 * </p>
	 * 
	 * @param T
	 * @return R
	 */
	R update(T t);

	/**
	 * <p>
	 * 删除
	 * </p>
	 * 
	 * @param id
	 * @return R
	 */
	R delete(String id);

	/**
	 * <p>
	 * 批量删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param ids
	 * @return
	 */
	R deleteBatch(String[] ids);

	/**
	 * <p>
	 * 查看明细
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	R view(String id);
}
