package com.budaos.modules.job.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.sys.domain.TuserToken;
import org.apache.ibatis.annotations.Mapper;

/**
 * Title: 系统用户TokenDescription: Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Mapper
public interface TuserTokenMapper extends BaseSqlMapper<TuserToken> {
    
    TuserToken selectObjectByUserId(String userId);

    TuserToken selectByToken(String token);
	
}
