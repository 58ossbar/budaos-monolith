package com.budaos.modules.evgl.common;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.medu.sys.api.TmeduApiTokenService;
import com.budaos.modules.evgl.medu.sys.domain.TmeduApiToken;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.im.core.annotation.CBImHandshake;
import com.budaos.modules.im.core.annotation.CBImService;
import com.budaos.modules.im.core.config.Const;
import com.budaos.modules.im.core.entity.ChannelType;
import com.budaos.modules.im.core.utils.CBImUtils;
import com.budaos.modules.im.domain.TimUserinfo;
import com.budaos.modules.im.service.TimUserinfoService;
import com.budaos.modules.sys.api.TsysUserinfoService;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建立连接处理类(握手)
 * @author zhuq
 *
 */
@CBImService
@Service
public class EvglHandShakeHandler {
	
	private static Logger log = LoggerFactory.getLogger(EvglHandShakeHandler.class);
	
	@Autowired
	private TimUserinfoService timUserinfoService;
	@Autowired
	private TsysUserinfoService tsysUserinfoService;
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
	@Autowired
	private TmeduApiTokenService tmeduApiTokenService;
	
	/**
	 * websocket握手处理类，即建立长连接(类似于新用户注册，老用户登陆操作)
	 * @param request
	 * @param response
	 * @param channelContext
	 */
	@CBImHandshake
	public HttpResponse handler(HttpRequest request, HttpResponse response, ChannelContext channelContext) {
		String token = request.getParam(Const.USER_TOKEN);
		String id = request.getParam(Const.ID);
		String channel = request.getParam(Const.USER_CHANNEL);
		log.debug("token:" + token);
		log.debug("id:" + id);
		log.debug("channel:" + channel);
		//系统用户
		if(ChannelType.SITE.equals(channel)) {
			TevglTraineeInfo info = tevglTraineeInfoService.selectObjectById(id);
			TmeduApiToken tokenInfo = tmeduApiTokenService.selectTokenByToken(token);
			if(info == null || tokenInfo == null 
					|| StrUtils.isEmpty(tokenInfo.getToken()) 
					|| !tokenInfo.getUserId().equals(id)) {
				CBImUtils.sendToSelf(channelContext, R.error(403, "无效的token"));
				return null;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("relateId", id);
			//map.put("relateType", channel);
			List<TimUserinfo> users = timUserinfoService.selectListByMapNoPage(map);
			TimUserinfo user = new TimUserinfo();
			if(users.size() > 0) {
				user.setUserId(users.get(0).getUserId());
			}else {
				user.setStatus("Y");
			}
			user.setBirthday(info.getTraineeBirthday());
			user.setRelateId(id);
			//user.setRelateType(channel);
			//user.setUsername(info.getTraineeName());
			user.setEmail(info.getEmail());
			user.setMobile(info.getMobile());
			user.setCreateTime(DateUtils.getNowTimeStamp());
			user.setUserimg(info.getTraineeHead());
			user.setUserRealname(info.getTraineeName());
			user.setSex(info.getTraineeSex());
			user.setUserCard(info.getTraineeCard());
			timUserinfoService.save(user);
			request.addParam(Const.USER_ID, user.getUserId());
			return response;
		}
		return null;
	}
}
