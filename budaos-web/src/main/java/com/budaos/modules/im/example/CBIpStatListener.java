package com.budaos.modules.im.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.common.starter.annotation.TioServerIpStatListener;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.intf.Packet;
import org.tio.core.stat.IpStat;
import org.tio.core.stat.IpStatListener;
import org.tio.utils.json.Json;

/**
 * @author zhuq(示例代码)
 * @date 2020年5月27日
 */

@TioServerIpStatListener
public class CBIpStatListener implements IpStatListener {

	private static Logger log = LoggerFactory.getLogger(CBIpStatListener.class);

	@Override
	public void onExpired(TioConfig tioConfig, IpStat ipStat) {
		// 在这里把统计数据入库中或日志
		log.info("可以把统计数据入库\r\n{}", Json.toFormatedJson(ipStat));
	}

	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect, IpStat ipStat)
			throws Exception {
		log.info("onAfterConnected\r\n{}", Json.toFormatedJson(ipStat));
	}

	@Override
	public void onDecodeError(ChannelContext channelContext, IpStat ipStat) {
		log.info("onDecodeError\r\n{}", Json.toFormatedJson(ipStat));
	}

	@Override
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess, IpStat ipStat)
			throws Exception {
		log.info("onAfterSent\r\n{}\r\n{}", packet.logstr(), Json.toFormatedJson(ipStat));
	}

	@Override
	public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize, IpStat ipStat)
			throws Exception {
		log.info("onAfterDecoded\r\n{}\r\n{}", packet.logstr(), Json.toFormatedJson(ipStat));
	}

	@Override
	public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes, IpStat ipStat) throws Exception {
		log.info("onAfterReceivedBytes\r\n{}", Json.toFormatedJson(ipStat));
	}

	@Override
	public void onAfterHandled(ChannelContext channelContext, Packet packet, IpStat ipStat, long cost)
			throws Exception {
		log.info("onAfterHandled\r\n{}\r\n{}", packet.logstr(), Json.toFormatedJson(ipStat));
	}
}
