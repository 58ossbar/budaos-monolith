package com.budaos.common.cbsecurity.dataprivilege.annotation;

import java.lang.annotation.*;

/**
 * Title: 数据过滤 Copyright: Copyright (c) 2017 Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {
	// 数据范围（1：所有数据；2：所在机构及以下数据；3：所在机构数据；4：仅本人数据；9：按明细设置）
	/** 表的别名 */
	String tableAlias() default "";

	/** true：1：所有数据权限 */
	boolean allOrg() default false;

	/** true：2：拥有本部门子部门数据权限 */
	boolean selfAndSubOrg() default false;

	/** true：3：拥有本部门数据权限 */
	boolean selfOrg() default false;

	/** true：4、没有本组织机构部门数据权限，也能查询本人数据 */
	boolean selfUser() default true;

	/** true：5：自定义数据权限，明细权限 */
	boolean customOrg() default false;

	/**
	 * 根据定义角色时设置的数据范围来定义。读角色的数据范围t_sys_role
	 */
	String customDataAuth() default "";;
}
