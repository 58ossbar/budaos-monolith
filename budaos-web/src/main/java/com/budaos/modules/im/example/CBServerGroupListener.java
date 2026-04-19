package com.budaos.modules.im.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.common.starter.annotation.TioServerGroupListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.GroupListener;
/**
 * @author zhuq(示例代码)
 * @date 2020年5月27日
 */

@TioServerGroupListener
public class CBServerGroupListener implements  GroupListener{
	
    private static Logger log = LoggerFactory.getLogger(CBServerGroupListener.class);

	@Override
	public void onAfterBind(ChannelContext arg0, String arg1) throws Exception {
		log.info("onAfterBind{}", arg1);
	}

	@Override
	public void onAfterUnbind(ChannelContext arg0, String arg1) throws Exception {
		log.info("onAfterUnbind{}", arg1);
	}
}
