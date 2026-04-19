package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Title:定时任务日志
 * Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Mapper
public interface ScheduleJobLogMapper extends BaseSqlMapper<ScheduleJobLogEntity> {
	
}
