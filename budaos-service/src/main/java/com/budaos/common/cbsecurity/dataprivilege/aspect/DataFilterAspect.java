package com.budaos.common.cbsecurity.dataprivilege.aspect;

import com.budaos.common.cbsecurity.dataprivilege.annotation.DataFilter;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.modules.sys.api.TsysDataprivilegeService;
import com.budaos.utils.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Title: 数据过滤，切面处理类 Copyright: Copyright (c) 2017 Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Aspect
@Component
public class DataFilterAspect {
	private static Logger log = LoggerFactory.getLogger(DataFilterAspect.class);
	@Autowired
	private TsysDataprivilegeService tsysDataprivilegeService;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Pointcut("@annotation(com.budaos.common.cbsecurity.dataprivilege.annotation.DataFilter)")
	public void dataFilterCut() {

	}

	@SuppressWarnings("unchecked")
	@Before("dataFilterCut()")
	public void dataFilter(JoinPoint point) throws Throwable {
		Object params = point.getArgs()[0];
		if (params != null && params instanceof Map) {
			String userId = serviceLoginUtil.getLoginUserId();
			log.debug("进行数据权限控制的用户：" + userId);
			// 如果不是超级管理员，则进行数据过滤查询本部门及子部门数据
			if (!Constant.SUPER_ADMIN.equals(userId)) {
				MethodSignature signature = (MethodSignature) ((JoinPoint)point).getSignature();
				DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
				// 获取表的别名
				String tableAlias = dataFilter.tableAlias();
				if (StringUtils.isNotBlank(tableAlias)) {
					tableAlias += ".";
				}
				@SuppressWarnings("rawtypes")
				Map map = (Map) params;
				String sqlFilter = tsysDataprivilegeService.getSQLFilter(userId, tableAlias, point);
				log.debug("数据权限sql0：" + sqlFilter);
				map.put(Constant.SQL_FILTER, sqlFilter);
			}
			return;
		}
		throw new BudaosException("数据权限接口，只能是Map类型参数，且不能为NULL");
	}

	
}
