package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TsysLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * Title: 系统日志 Description: Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Mapper
public interface TsysLogMapper extends BaseSqlMapper<TsysLog> {
	
}
