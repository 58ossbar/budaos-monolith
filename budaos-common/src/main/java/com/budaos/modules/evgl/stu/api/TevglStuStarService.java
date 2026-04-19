package com.budaos.modules.evgl.stu.api;

import com.budaos.core.baseclass.api.IBaseService;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.stu.domain.TevglStuStar;

/**
 * <p> Title:就业明星 </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public interface TevglStuStarService extends IBaseService<TevglStuStar>{
	 
	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019��7��26��
	 * @param tevglStuStar
	 * @param attachId
	 * @return
	 */
	R save2(TevglStuStar tevglStuStar, String attachId);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019��7��26��
	 * @param tevglStuStar
	 * @param attachId
	 * @return
	 */
	R update2(TevglStuStar tevglStuStar, String attachId);
	
	/**
	 * <p>更新状态̬</p>
	 * @author huj
	 * @data 2019��7��28��
	 * @param tevglStuStar
	 * @return
	 */
	R updateState(TevglStuStar tevglStuStar);
}