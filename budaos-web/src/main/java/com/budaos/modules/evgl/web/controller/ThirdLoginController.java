package com.budaos.modules.evgl.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.EvglGlobal;
import com.budaos.modules.evgl.common.LoginUtils;
import com.budaos.modules.evgl.tch.api.TevglTchTeacherService;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.budaos.modules.evgl.trainee.api.TevglTraineeSocialService;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeSocial;
import com.budaos.modules.im.domain.TimUserinfo;
import com.budaos.modules.im.service.TimUserinfoService;
import com.budaos.sms.service.AliyunSmsService;
import com.budaos.sms.service.TencentSmsService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.*;
import com.google.code.kaptcha.Constants;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2018 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 * @date 2018年7月3日 下午2:39:30
 */
@RestController
@RequestMapping("login-api")
public class ThirdLoginController {
	Logger log = LoggerFactory.getLogger(getClass());
	@Value("${com.budaos.loginBackUrl:}")
	public String loginBackUrl;
	@Value("${com.budaos.defaultHeadImg:}")
	public String defaultHeadImg;
	@Value("${com.budaos.defaultName:}")
	public String defaultName;
	@Value("${com.budaos.qqAppId:}")
	public String qqAppId;
	@Value("${com.budaos.qqAppKey:}")
	public String qqAppKey;
	@Value("${com.budaos.wxAppId:}")
	public String wxAppId;
	@Value("${com.budaos.wxAppKey:}")
	public String wxAppKey;
	@Value("${com.budaos.xlAppId:}")
	public String xlAppId;
	@Value("${com.budaos.xlAppKey:}")
	public String xlAppKey;
	@Value("${com.budaos.file-access-path}")
	public String budaosFieAccessPath;
	@Value("${com.budaos.gitUrl}")
	public String gitUrl;
	@Value("${com.budaos.gitTicket}")
	public String gitTicket;
    @Autowired
	private AliyunSmsService smsService;
	@Autowired
	private TencentSmsService tencentSmsService;
	@Autowired
	private TevglTraineeInfoService tevglTraineeInfoService;
    @Autowired
	private TevglTraineeSocialService tevglTraineeSocialService;
    @Autowired
    private TevglTchTeacherService tevglTchTeacherService;
    @Autowired
    private TimUserinfoService timUserinfoService;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${spring.profiles.active:}")
    private String active;
    
	@RequestMapping("login")
	public R login(HttpServletRequest request, HttpServletResponse response, String inputAccountLogin, String inputPasswordLogin, String inputYzcodeLogin) {
		TevglTraineeInfo user = tevglTraineeInfoService.selectByMobile(inputAccountLogin);
		if(user == null) {
			return R.error(202, "用户名或密码错误!");
		}
		if(StrUtils.isNotEmpty(inputYzcodeLogin)) {
			HttpSession session = request.getSession();
			Object yzCode = session.getAttribute("loginMobile_"+inputAccountLogin);
			session.removeAttribute("loginMobile_"+inputAccountLogin);
			//如果验证码正确
			if(inputYzcodeLogin == null || yzCode == null || !inputYzcodeLogin.equals(yzCode)) {
				return R.error(201, "验证码错误!");
			}
		}else if(StrUtils.isNotEmpty(inputPasswordLogin)) {
			if(StrUtils.isEmpty(user.getUserPasswd()) || !user.getUserPasswd().equals(TicketDesUtil.encryptWithMd5(inputPasswordLogin, user.getUserYan()))){
				return R.error(202, "用户名或密码错误!");
			}
		}
		String token = initLoginData(request, response, user);
		JSONObject obj = new JSONObject();
		obj.put("token", token);
		obj.put("traineeId", user.getTraineeId());
		obj.put("nickName", user.getNickName());
		obj.put("traineeName", user.getTraineeName());
		obj.put("traineeHead", StrUtils.isEmpty(user.getTraineePic()) ? user.getTraineeHead() : budaosFieAccessPath + "/trainee-data/" + user.getTraineePic());
		obj.put("traineeQq", StrUtils.isEmpty(user.getTraineeQq()) ? "" : user.getTraineeQq());
		obj.put("email", StrUtils.isEmpty(user.getEmail()) ? "" : user.getEmail());
		TevglTchTeacher tevglTchTeacher = tevglTchTeacherService.selectObjectByTraineeId(user.getTraineeId());
		obj.put("isTeacher", (tevglTchTeacher == null || !"Y".equals(tevglTchTeacher.getState())) ? false : true);
		if (tevglTchTeacher != null) {
			String pic = StrUtils.isEmpty(user.getTraineePic()) ? user.getTraineeHead() : budaosFieAccessPath + "/trainee-data/" + user.getTraineePic();
			log.debug("用户类型：" + user.getTraineeType());
			if ("4".equals(user.getTraineeType())) {
				if (tevglTchTeacher != null && "Y".equals(tevglTchTeacher.getState())) {
					if (StrUtils.isNotEmpty(tevglTchTeacher.getTeacherPic())) {
						pic = budaosFieAccessPath + "/teacher-img/" + tevglTchTeacher.getTeacherPic();
					}
				}
			}
			obj.put("traineeHead", pic);
		}
		return R.ok().put(Constant.R_DATA, obj);
	}
	
	@RequestMapping("user")
	//@CheckSession
	public R user(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object traineeInfo = session.getAttribute(EvglGlobal.LOGIN_SESSION_KEY);
		if(traineeInfo == null) {
			//return R.error("未登陆");
			traineeInfo = getTraineeInfoByToken(request);
			if (traineeInfo == null) {
				return R.error("无法获取登录信息");
			}
		}
		TevglTraineeInfo info = (TevglTraineeInfo)traineeInfo;
		String isBind = StrUtils.toString(session.getAttribute(EvglGlobal.LOGIN_SESSION_ISBIND));
		JSONObject obj = new JSONObject();
		obj.put("token", info.getToken());
		obj.put("traineeId", info.getTraineeId());
		obj.put("nickName", info.getNickName());
		String pic = StrUtils.isEmpty(info.getTraineePic()) ? info.getTraineeHead() : budaosFieAccessPath + "/trainee-data/" + info.getTraineePic();
		log.debug("用户类型：" + info.getTraineeType());
		if ("4".equals(info.getTraineeType())) {
			TevglTchTeacher tevglTchTeacher = tevglTchTeacherService.selectObjectById(info.getTraineeId());
			if (tevglTchTeacher != null && "Y".equals(tevglTchTeacher.getState())) {
				if (StrUtils.isNotEmpty(tevglTchTeacher.getTeacherPic())) {
					pic = budaosFieAccessPath + "/teacher-img/" + tevglTchTeacher.getTeacherPic();
				}
			}
		}
		obj.put("traineeHead", pic);
		obj.put("isBind", isBind);
		obj.put("traineeName", info.getTraineeName());
		obj.put("traineeQq", StrUtils.isEmpty(info.getTraineeQq()) ? "" : info.getTraineeQq());
		obj.put("email", StrUtils.isEmpty(info.getEmail()) ? "" : info.getEmail());
		TevglTchTeacher tevglTchTeacher = tevglTchTeacherService.selectObjectByTraineeId(info.getTraineeId());
		obj.put("isTeacher", (tevglTchTeacher == null || !"Y".equals(tevglTchTeacher.getState())) ? false : true);
		return R.ok().put(Constant.R_DATA, obj);
	}
	
	/**
	 * frp.budaos.com是其它域名内网穿透的，单独处理下，便于开发人员
	 * @param request
	 * @return
	 */
	private Object getTraineeInfoByToken(HttpServletRequest request) {
		String token = getMyToken(request);
		if (StrUtils.isNotEmpty(token)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("token", token);
			List<TevglTraineeInfo> list = tevglTraineeInfoService.queryByMap(map);
			if (list == null || list.isEmpty()) {
				return null;
			}
			return list.get(0);
		}
		return null;
	}
	
    /**
     * 获取token
     * @return
     */
    private String getMyToken(HttpServletRequest request) {
        // 获取token
        String token = request.getHeader(EvglGlobal.TOKEN_KEY_NAME);
        // 如果请求头中没有token,从cookie中获取一下
        if (StrUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (Cookie cookie : cookies) {
                    switch(cookie.getName()){
                        case EvglGlobal.TOKEN_KEY_NAME:
                            token = cookie.getValue();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return token;
    }
	
	@RequestMapping("qqlogin")
	public void qqLogin(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		String state = Identities.uuid();
		request.getSession().setAttribute("qqlogin_state", state);
		response.sendRedirect("https://graph.qq.com/oauth2.0/authorize?client_id="+qqAppId+"&response_type=code&scope=all&state="+state+"&redirect_uri="+loginBackUrl+"/login-api/qqloginback?url=" + URLEncoder.encode(URLEncoder.encode(url, "UTF-8"), "UTF-8"));
	}
	@RequestMapping("xllogin")
	public void xlLogin(HttpServletRequest request, HttpServletResponse response, String url)  throws IOException {
		String state = Identities.uuid();
		request.getSession().setAttribute("xllogin_state", state);
		response.sendRedirect("https://api.weibo.com/oauth2/authorize?client_id="+xlAppId+"&scope=&state="+state+"&redirect_uri="+loginBackUrl+"/login-api/xlloginback?url=" + URLEncoder.encode(URLEncoder.encode(url, "UTF-8"), "UTF-8"));
	}
	@RequestMapping("wxlogin")
	public void wxLogin(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		String state = Identities.uuid();
		request.getSession().setAttribute("wxlogin_state", state);
		response.sendRedirect("https://open.weixin.qq.com/connect/qrconnect?appid="+wxAppId+"&redirect_uri="+URLEncoder.encode(""+loginBackUrl+"/login-api/wxloginback?url="+URLEncoder.encode(url, "UTF-8"), "UTF-8")+"&response_type=code&scope=snsapi_login&state="+state+"#wechat_redirect");
	}
	@Transactional
	@RequestMapping("qqloginback")
	public void qqLoginBack(HttpServletRequest request, HttpServletResponse response, String code, String state, String url) throws IOException {
		HttpSession session = request.getSession();
		if(StrUtils.isEmpty(state) || !state.equals(session.getAttribute("qqlogin_state"))) {
			log.debug("state状态错误：" + state);
			response.sendRedirect(url);
			return;
		}else {
			session.removeAttribute("qqlogin_state");
		}
		String tokenResult = HttpUtils.sendGet("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id="+qqAppId+"&client_secret="+qqAppKey+"&code=" + code + "&redirect_uri="+loginBackUrl+"/login-api/qqloginback");
		log.debug("获取到token：" + tokenResult);
		String access_token = "";
		if(StrUtils.isNotEmpty(tokenResult)) {
			String[] kv = tokenResult.split("&");
			for(String temp : kv) {
				String[] k = temp.split("=");
				if("access_token".equals(k[0])) {
					access_token = k[1];
				}
			}
		}
		String openidResult = HttpUtils.sendGet("https://graph.qq.com/oauth2.0/me?access_token=" + access_token);
		log.debug("获取到openid:" + openidResult);
		String openid = "";
		if(StrUtils.isNotEmpty(openidResult)) {
			JSONObject obj = JSONObject.parseObject(openidResult.substring(9, openidResult.length() - 3));
			openid = obj.getString("openid");
		}
		
		String  userInfoResult = HttpUtils.sendGet("https://graph.qq.com/user/get_user_info?access_token=" + access_token + "&oauth_consumer_key=" + qqAppId + "&openid=" + openid);
		String nickname = "";//用户QQ昵称
		String image = "";//用户QQ头像
		String gender = "";//用户性别
		//String province = "";//用户所在省份
		//String city = "";//用户所在城市
		//String year = "";//用户出生年份
		log.debug("获取到用户资料:" + userInfoResult);
		if(StrUtils.isNotEmpty(userInfoResult)) {
			JSONObject obj = JSONObject.parseObject(userInfoResult);
			if(0 == obj.getIntValue("ret")) {
				nickname = obj.getString("nickname");
				image = obj.getString("figureurl_qq_1");
				gender = obj.getString("gender");
				//province = obj.get("province") == null ? "" : obj.getString("province");
				//city = obj.get("city") == null ? "" : obj.getString("city");
				//year = obj.get("year") == null ? "" : obj.getString("year");
			}
		}
		if(StrUtils.isEmpty(openid)) {
			response.sendRedirect(url);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("qqOpenid", openid);
		List<TevglTraineeInfo> list = tevglTraineeInfoService.queryByMap(map);
		//如果此QQ没有登陆过，则插入到学员表中
		if(list.size() == 0) {
			TevglTraineeInfo tevglTraineeInfo = new TevglTraineeInfo();
			tevglTraineeInfo.setTraineeSex("男".equals(gender)? "1" : "2");
			tevglTraineeInfo.setNickName(nickname);
			tevglTraineeInfo.setTraineeHead(image);
			tevglTraineeInfo.setQqOpenid(openid);
			tevglTraineeInfo.setTraineeType("1");
			tevglTraineeInfoService.save(tevglTraineeInfo);
			TevglTraineeSocial social = new TevglTraineeSocial();
			social.setNickName(tevglTraineeInfo.getNickName());
			social.setChannelId("1");
			social.setOpenId(tevglTraineeInfo.getQqOpenid());
			social.setTraineeId(tevglTraineeInfo.getTraineeId());
			tevglTraineeSocialService.save(social);
			initLoginData(request, response, tevglTraineeInfo);
		}else {
			initLoginData(request, response, list.get(0));
		}
		session.setAttribute("ep_usertype", "1");
		response.sendRedirect(url);
		return;
	}
	@Transactional
	@RequestMapping("xlloginback")
	public void xlLoginBack(HttpServletRequest request, HttpServletResponse response, String code, String state, String url) throws IOException {
		HttpSession session = request.getSession();
		if(StrUtils.isEmpty(state) || !state.equals(session.getAttribute("xllogin_state"))) {
			log.debug("state状态错误：" + state);
			response.sendRedirect(url);
			return;
		}else {
			session.removeAttribute("xllogin_state");
		}
		String tokenResult = HttpUtils.sendPost("https://api.weibo.com/oauth2/access_token", "grant_type=authorization_code&client_id="+xlAppId+"&client_secret="+xlAppKey+"&code=" + code + "&redirect_uri="+loginBackUrl+"/login-api/xlloginback");
		log.debug("获取到token：" + tokenResult);
		String uid = "";
		String access_token = "";
		if(StrUtils.isNotEmpty(tokenResult)) {
			JSONObject obj = JSONObject.parseObject(tokenResult);
			access_token = obj.getString("access_token");
			uid = obj.getString("uid");
		}
		
		String  userInfoResult = HttpUtils.sendGet("https://api.weibo.com/2/users/show.json?access_token=" + access_token + "&uid=" + uid);
		String nickname = "";//用户QQ昵称
		String image = "";//用户QQ头像
		String gender = "";//用户性别
		String openid = "";
		//String province = "";//用户所在省份
		//String city = "";//用户所在城市
		//String year = "";//用户出生年份
		log.debug("获取到用户资料:" + userInfoResult);
		if(StrUtils.isNotEmpty(userInfoResult)) {
			JSONObject obj = JSONObject.parseObject(userInfoResult);
			if(0 == obj.getIntValue("ret")) {
				nickname = obj.getString("screen_name");
				image = obj.getString("profile_image_url");
				gender = obj.getString("gender");
				openid = obj.getString("idstr");
				//province = obj.get("province") == null ? "" : obj.getString("province");
				//city = obj.get("city") == null ? "" : obj.getString("city");
				//year = obj.get("year") == null ? "" : obj.getString("year");
			}
		}
		if(StrUtils.isEmpty(openid)) {
			response.sendRedirect(url);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("weiboId", openid);
		List<TevglTraineeInfo> list = tevglTraineeInfoService.queryByMap(map);
		//如果此QQ没有登陆过，则插入到学员表中
		if(list.size() == 0) {
			TevglTraineeInfo tevglTraineeInfo = new TevglTraineeInfo();
			tevglTraineeInfo.setTraineeSex("m".equals(gender)? "1" : "f".equals(gender)? "2" : "0");
			tevglTraineeInfo.setNickName(nickname);
			tevglTraineeInfo.setTraineeHead(image);
			tevglTraineeInfo.setWeiboOpenid(openid);
			tevglTraineeInfo.setTraineeType("1");
			tevglTraineeInfoService.save(tevglTraineeInfo);
			TevglTraineeSocial social = new TevglTraineeSocial();
			social.setNickName(tevglTraineeInfo.getNickName());
			social.setChannelId("2");
			social.setOpenId(tevglTraineeInfo.getWeiboOpenid());
			social.setTraineeId(tevglTraineeInfo.getTraineeId());
			tevglTraineeSocialService.save(social);
			initLoginData(request, response, tevglTraineeInfo);
		}else {
			initLoginData(request, response, list.get(0));
		}
		session.setAttribute("ep_usertype", "2");
		//mv.addObject("showbind", "1");
		response.sendRedirect(url);
		return;
	}
	@Transactional
	@RequestMapping("wxloginback")
	public void wxLoginBack(HttpServletRequest request, HttpServletResponse response, String code, String state, String url) throws IOException {
		HttpSession session = request.getSession();
		if(StrUtils.isEmpty(state) || !state.equals(session.getAttribute("wxlogin_state"))) {
			log.debug("state状态错误：" + state);
			response.sendRedirect(url);
			return;
		}else {
			session.removeAttribute("wxlogin_state");
		}
		String tokenResult = HttpUtils.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wxAppId+"&secret="+wxAppKey+"&code="+code+"&grant_type=authorization_code");
		log.debug("获取到token：" + tokenResult);
		String openid = "";
		String access_token = "";
		if(StrUtils.isNotEmpty(tokenResult)) {
			JSONObject obj = JSONObject.parseObject(tokenResult);
			access_token = obj.getString("access_token");
			openid = obj.getString("openid");
		}
		
		String userInfoResult = HttpUtils.sendGet("https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid);
		String nickname = "";//用户QQ昵称
		String image = "";//用户QQ头像
		String gender = "";//用户性别
		//String province = "";//用户所在省份
		//String city = "";//用户所在城市
		//String year = "";//用户出生年份
		log.debug("获取到用户资料:" + userInfoResult);
		if(StrUtils.isNotEmpty(userInfoResult)) {
			JSONObject obj = JSONObject.parseObject(userInfoResult);
			nickname = obj.getString("nickname");
			image = obj.getString("headimgurl");
			gender = obj.getString("sex");
			//province = obj.get("province") == null ? "" : obj.getString("province");
			//city = obj.get("city") == null ? "" : obj.getString("city");
			//year = obj.get("year") == null ? "" : obj.getString("year");
		}
		if(StrUtils.isEmpty(openid)) {
			response.sendRedirect(url);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wxOpenid", openid);
		List<TevglTraineeInfo> list = tevglTraineeInfoService.queryByMap(map);
		//如果此QQ没有登陆过，则插入到学员表中
		if(list.size() == 0) {
			TevglTraineeInfo tevglTraineeInfo = new TevglTraineeInfo();
			tevglTraineeInfo.setTraineeSex(gender);
			tevglTraineeInfo.setNickName(nickname);
			tevglTraineeInfo.setTraineeHead(image);
			tevglTraineeInfo.setWxOpenid(openid);
			tevglTraineeInfo.setTraineeType("1");
			tevglTraineeInfoService.save(tevglTraineeInfo);
			TevglTraineeSocial social = new TevglTraineeSocial();
			social.setNickName(tevglTraineeInfo.getNickName());
			social.setChannelId("3");
			social.setOpenId(tevglTraineeInfo.getWxOpenid());
			social.setTraineeId(tevglTraineeInfo.getTraineeId());
			tevglTraineeSocialService.save(social);
			initLoginData(request, response, tevglTraineeInfo);
		}else {
			initLoginData(request, response, list.get(0));
		}
		session.setAttribute("ep_usertype", "3");
		response.sendRedirect(url);
		return;
	}
	@RequestMapping("bindmobile")
	@Transactional
	public R bindMobile(HttpServletRequest request, HttpServletResponse response, String inputAccountBind, String inputPasswordBind, String inputYzcodeBind, String inputMescodeBind) {
		HttpSession session = request.getSession();
		Object randCode = session.getAttribute("bindMobile_"+inputAccountBind);
		session.removeAttribute("bindMobile_"+inputAccountBind);
		Object yzCode = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
		if(inputYzcodeBind == null || yzCode == null || !inputYzcodeBind.equals(yzCode)) {
			//return R.error(203, "图形验证码错误!");
		}
		String redisCode = stringRedisTemplate.opsForValue().get(EvglGlobal.CODE_BIND + inputAccountBind);
		//短信验证码正确
		//if(inputMescodeBind != null && randCode != null && inputMescodeBind.equals(randCode)) {
		if (inputMescodeBind != null && inputMescodeBind.equals(redisCode)) {
			Object obj = session.getAttribute(EvglGlobal.LOGIN_SESSION_KEY);
			Object type = session.getAttribute("ep_usertype");
			if(obj == null) {
				return R.error(202, "绑定失败!");
			}else {
				//先查询该手机号码是否已经注册过
				TevglTraineeInfo old = tevglTraineeInfoService.selectByMobile(inputAccountBind);
				TevglTraineeInfo loginUser = (TevglTraineeInfo) obj;
				String salt = RandomStringUtils.randomAlphanumeric(24);
				//如果没注册过，则直接更新之前快捷登陆的数据
				if(old == null) {
					loginUser.setMobile(inputAccountBind);
					loginUser.setUserPasswd(TicketDesUtil.encryptWithMd5(inputPasswordBind, salt));
					loginUser.setUserYan(salt);
					tevglTraineeInfoService.update(loginUser);
					initLoginData(request, response, loginUser);
					tevglTraineeInfoService.doAfterBindMobile(loginUser);
				}else {//如果已经注册过，则删除本次快捷登陆信息，合并到之前注册过的数据中去
					if("1".equals(type)) {
						if(StrUtils.isNotEmpty(old.getQqOpenid()) && !loginUser.getQqOpenid().equals(old.getQqOpenid())) {
							return R.error(203, "该手机已经绑定了其他QQ号!");
						}
						old.setQqOpenid(loginUser.getQqOpenid());
					}else if("2".equals(type)) {
						if(StrUtils.isNotEmpty(old.getWeiboOpenid()) && !loginUser.getWeiboOpenid().equals(old.getWeiboOpenid())) {
							return R.error(203, "该手机已经绑定了其他微博账号!");
						}
						old.setWeiboOpenid(loginUser.getWeiboOpenid());
					}else if("3".equals(type)) {
						if(StrUtils.isNotEmpty(old.getWxOpenid()) && !loginUser.getWxOpenid().equals(old.getWxOpenid())) {
							return R.error(203, "该手机已经绑定了其他微信号!");
						}
						old.setWxOpenid(loginUser.getWxOpenid());
					}
					//如果是默认头像则替换成当前快捷登陆的头像
					if(defaultHeadImg.equals(old.getTraineeHead()) && StrUtils.isNotEmpty(loginUser.getTraineeHead())) {
						old.setTraineeHead(loginUser.getTraineeHead());
					}
					//如果是默认昵称则替换成当前快捷登陆的昵称
					if(defaultName.equals(old.getNickName()) && StrUtils.isNotEmpty(loginUser.getNickName())) {
						old.setNickName(loginUser.getNickName());
					}
					old.setUserPasswd(TicketDesUtil.encryptWithMd5(inputPasswordBind, salt));
					old.setUserYan(salt);
					tevglTraineeInfoService.update(old);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("traineeId", loginUser.getTraineeId());
					List<TevglTraineeSocial> list = tevglTraineeSocialService.queryByMap(map);
					if(list.size() > 0) {
						TevglTraineeSocial tevglTraineeSocial = list.get(0);
						tevglTraineeSocial.setTraineeId(old.getTraineeId());
						tevglTraineeSocialService.update(tevglTraineeSocial);
					}
					//删除之前先将当前一键登陆的用户所收藏的数据同步到老账号上去
					map.clear();
					map.put("traineeId", loginUser.getTraineeId());
					/*
					List<TmeduFavority> favoritys = tmeduFavorityService.selectListByMap(map);
					for(TmeduFavority fav : favoritys) {
						fav.setTraineeId(old.getTraineeId());
						tmeduFavorityService.update(fav);
					}
					*/
					List<TevglTraineeSocial> socials = tevglTraineeSocialService.queryByMap(map);
					for(TevglTraineeSocial soc : socials) {
						soc.setTraineeId(old.getTraineeId());
						tevglTraineeSocialService.update(soc);
					}
					//删除本次一键登陆的记录
					tevglTraineeInfoService.delete(loginUser.getTraineeId());
					//判断如果在小程序上没注册过，生成一个apitoken
					/*
					TmeduApiToken apiToken = tmeduApiTokenService.selectTokenByUserId(old.getTraineeId());
					if(apiToken == null) {
						TmeduApiToken newApiToken = new TmeduApiToken();
						newApiToken.setUserId(old.getTraineeId());
						newApiToken.setToken("1");
						newApiToken.setMobile(inputAccountBind);
						newApiToken.setUserType("1");
						tmeduApiTokenService.insert(newApiToken);
					}
					*/
					initLoginData(request, response, old);
					tevglTraineeInfoService.doAfterBindMobile(old);
				}
			}
		}else {
			return R.error(201, "短信验证码错误!");
		}
		return R.ok();
	}
	
	@RequestMapping("sendloginsms")
	public R sendLoginSms(HttpServletRequest request, String inputAccountBind, String inputYzcodeBind, String inputMescodeBind) {
		HttpSession session = request.getSession();
		//Object yzCode = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		//如果验证码正确
		//if(inputYzcodeBind != null && yzCode != null && inputYzcodeBind.equals(yzCode)) {
		 	String randCode = Integer.toString(new Random().nextInt(900000) + 100000);
			if(!smsService.sendSms(inputAccountBind, "SMS_126781251", "{\"code\":\""+randCode+"\"}")) {
				return R.error(202, "短信发送失败!");
			}
			session.setAttribute("loginMobile_"+inputAccountBind, randCode);
		//}else {
		//	return R.error(201, "验证码错误!");
		//}
		return R.ok();
	}
	
	@RequestMapping("sendbindsms")
	public R sendBindSms(HttpServletRequest request, String inputAccountBind, String inputYzcodeBind, String inputMescodeBind) {
		/*HttpSession session = request.getSession();
		Object yzCode = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		boolean flag = inputYzcodeBind != null && yzCode != null && inputYzcodeBind.equals(yzCode);
		if (!flag) {
			return R.error(201, "验证码错误!");
		}*/
		if (StrUtils.isEmpty(inputAccountBind)) {
			return R.error("手机号码不能为空");
		}
		String text = stringRedisTemplate.opsForValue().get(EvglGlobal.CODE_BIND + inputAccountBind);
		if (StrUtils.isNotEmpty(text)) {
			return R.error("短信验证码已发送，请注意查收！");
		}
		//如果验证码正确
	 	String randCode = Integer.toString(new Random().nextInt(900000) + 100000);
	 	boolean devTest = StrUtils.isNotEmpty(active) && (active.indexOf("dev") != -1 || active.indexOf("test") != -1);
	 	if (!devTest) {
			if(!smsService.sendSms(inputAccountBind, "SMS_126781251", "{\"code\":\""+randCode+"\"}")) {
				return R.error(202, "短信发送失败!");
			}
	 	}
	 	// 验证码存到redis中(5分钟失效)
        stringRedisTemplate.opsForValue().set(EvglGlobal.CODE_BIND + inputAccountBind, randCode, 5 * 60 * 1000, TimeUnit.MILLISECONDS);
		//session.setAttribute("bindMobile_"+inputAccountBind, randCode);
		return R.ok();
	}
	@RequestMapping("sendregsms")
	public R sendRegSms(HttpServletRequest request, String inputAccountReg, String inputPasswordReg, String inputYzcodeReg, String inputMescodeReg) {
		/*HttpSession session = request.getSession();
		Object yzCode = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		boolean flag = inputYzcodeReg != null && yzCode != null && inputYzcodeReg.equals(yzCode);
		if (!flag) {
			return R.error(201, "验证码错误!");
		}*/
		if (StrUtils.isEmpty(inputAccountReg)) {
			return R.error("手机号码不能为空");
		}
		//如果验证码正确
		TevglTraineeInfo user = tevglTraineeInfoService.selectByMobile(inputAccountReg);
		if(user != null && StrUtils.isNotEmpty(user.getUserPasswd())) {
			return R.error(202, "该手机号码已注册!");
		}
		// 
		String text = stringRedisTemplate.opsForValue().get(EvglGlobal.CODE_REGISTER + inputAccountReg);
		if (StrUtils.isNotEmpty(text)) {
			return R.error("短信验证码已发送，请注意查收！");
		}
	 	String randCode = Integer.toString(new Random().nextInt(900000) + 100000);
	 	boolean devTest = StrUtils.isNotEmpty(active) && (active.indexOf("dev") != -1 || active.indexOf("test") != -1);
	 	if (!devTest) {
	 		if(!smsService.sendSms(inputAccountReg, "SMS_126781251", "{\"code\":\""+randCode+"\"}")) {
				return R.error(202, "短信发送失败!");
			}
	 	}
		// 验证码存到redis中(5分钟失效)
		stringRedisTemplate.opsForValue().set(EvglGlobal.CODE_REGISTER + inputAccountReg, randCode, 5 * 60 * 1000, TimeUnit.MILLISECONDS);
		//session.setAttribute("regMobile_"+inputAccountReg, randCode);
		return R.ok(devTest ? randCode : null);
	}
	@RequestMapping("sendforgetsms")
	public R sendForgetSms(HttpServletRequest request, String inputAccountForget, String inputPasswordForget, String inputYzcodeForget, String inputMescodeForget) {
		HttpSession session = request.getSession();
		//Object yzCode = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		//如果验证码正确
		//if(inputYzcodeForget != null && yzCode != null && inputYzcodeForget.equals(yzCode)) {
			TevglTraineeInfo user = tevglTraineeInfoService.selectByMobile(inputAccountForget);
			if(user == null) {
				return R.error(202, "该手机号码不存在!");
			}
		 	String randCode = Integer.toString(new Random().nextInt(900000) + 100000);
			if(!smsService.sendSms(inputAccountForget, "SMS_126781251", "{\"code\":\""+randCode+"\"}")) {
				return R.error(202, "短信发送失败!");
			}
			session.setAttribute("forgetMobile_"+inputAccountForget, randCode);
		//}else {
		//	return R.error(201, "验证码错误!");
		//}
		return R.ok();
	}
	@RequestMapping("register")
	@Transactional
	public R register(HttpServletRequest request, String inputAccountReg, String inputPasswordReg, String inputYzcodeReg, String inputMescodeReg, String inputNickName) {
		/*HttpSession session = request.getSession();
		Object smsCode = session.getAttribute("regMobile_"+inputAccountReg);
		session.removeAttribute("regMobile_"+inputAccountReg);
		Object yzCode = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
		if(inputYzcodeReg == null || yzCode == null || !inputYzcodeReg.equals(yzCode)) {
			//return R.error(203, "图形验证码错误!");
		}*/
		String smsCode = stringRedisTemplate.opsForValue().get(EvglGlobal.CODE_REGISTER + inputAccountReg);
		//如果短信验证码正确
		if(inputMescodeReg != null && smsCode != null && inputMescodeReg.equals(smsCode)) {
			TevglTraineeInfo user = tevglTraineeInfoService.selectByMobile(inputAccountReg);
			if(user != null && StrUtils.isNotEmpty(user.getUserPasswd())) {
				return R.error(202, "该手机号码已注册!");
			}
			// 如果是先在小程序登录注册过了，再来网站这边注册，原来数据库就会存在两条同手机号码的记录，导致登录接口差错两个记录报错，所以额外判断下
			if (user == null) {
				TevglTraineeInfo info = new TevglTraineeInfo();
				info.setMobile(inputAccountReg);
				if(StrUtils.isNotEmpty(inputPasswordReg)) {
					String salt = RandomStringUtils.randomAlphanumeric(24);
					info.setUserPasswd(TicketDesUtil.encryptWithMd5(inputPasswordReg, salt));
					info.setUserYan(salt);
				}
				info.setNickName(StrUtils.isEmpty(inputNickName) ? defaultName : inputNickName);
				info.setTraineeHead(defaultHeadImg);
				info.setTraineeType("1");
				tevglTraineeInfoService.save(info);
				tevglTraineeInfoService.doAfterBindMobile(info);	
			} else {
				if(StrUtils.isNotEmpty(inputPasswordReg)) {
					String salt = RandomStringUtils.randomAlphanumeric(24);
					user.setUserPasswd(TicketDesUtil.encryptWithMd5(inputPasswordReg, salt));
					user.setUserYan(salt);
				}
				user.setNickName(StrUtils.isEmpty(inputNickName) ? user.getNickName() : inputNickName);
				user.setTraineeType("1");
				tevglTraineeInfoService.update(user);
				tevglTraineeInfoService.doAfterBindMobile(user);
			}
		}else {
			return R.error(201, "短信验证码错误!");
		}
		return R.ok();
	}
	@RequestMapping("forget")
	@Transactional
	public R forget(HttpServletRequest request, HttpServletResponse response, String inputAccountForget, String inputPasswordForget, String inputYzcodeForget, String inputMescodeForget) {
		HttpSession session = request.getSession();
		Object smsCode = session.getAttribute("forgetMobile_"+inputAccountForget);
		session.removeAttribute("forgetMobile_"+inputAccountForget);
		Object yzCode = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
		if(inputYzcodeForget == null || yzCode == null || !inputYzcodeForget.equals(yzCode)) {
			//return R.error(203, "图形验证码错误!");
		}
		//如果短信验证码正确
		if(inputMescodeForget != null && smsCode != null && inputMescodeForget.equals(smsCode)) {
			TevglTraineeInfo user = tevglTraineeInfoService.selectByMobile(inputAccountForget);
			if(user == null) {
				return R.error(202, "该手机号码不存在!");
			}
			user.setUserPasswd(TicketDesUtil.encryptWithMd5(inputPasswordForget, user.getUserYan()));
			initLoginData(request, response, user);
		}else {
			return R.error(201, "短信验证码错误!");
		}
		return R.ok();
	}
	@RequestMapping("loginout")
	public R loginOut(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.removeAttribute(EvglGlobal.LOGIN_SESSION_TOKEN);
		session.removeAttribute(EvglGlobal.LOGIN_SESSION_KEY);
		session.removeAttribute(EvglGlobal.LOGIN_SESSION_ISLOGIN);
		session.removeAttribute(EvglGlobal.LOGIN_SESSION_ISBIND);
		session.invalidate();
		Cookie imgCodeCookie = new Cookie(EvglGlobal.TOKEN_KEY_NAME, "");
		imgCodeCookie.setMaxAge(0);
	    imgCodeCookie.setPath("/");
		response.addCookie(imgCodeCookie);
		return R.ok();
	}
	
	//组织登陆信息
	private String initLoginData(HttpServletRequest request, HttpServletResponse response, TevglTraineeInfo tevglTraineeInfo) {
		HttpSession session = request.getSession();
		// 如果允许一个账号多端登录
        if ("Y".equals(EvglGlobal.MULTI_USER_LOGIN) && StrUtils.isNotEmpty(tevglTraineeInfo.getToken())) {
        	String token = tevglTraineeInfo.getToken();
        	Cookie imgCodeCookie = new Cookie(EvglGlobal.TOKEN_KEY_NAME, token);
    		imgCodeCookie.setMaxAge(3600*24*7);
    	    imgCodeCookie.setPath("/");
    		response.addCookie(imgCodeCookie);
    		session.setAttribute(EvglGlobal.LOGIN_SESSION_TOKEN, token);
    		session.setAttribute(EvglGlobal.LOGIN_SESSION_KEY, tevglTraineeInfo);
    		session.setAttribute(EvglGlobal.LOGIN_SESSION_ISLOGIN, "Y");
    		if(StrUtils.isNotEmpty(tevglTraineeInfo.getMobile())) {
    			session.setAttribute(EvglGlobal.LOGIN_SESSION_ISBIND, "Y");
    		}
            return token;
        }
		//签发免登陆token并保存到数据
		String token = Identities.uuid();
		tevglTraineeInfo.setToken(token);
		tevglTraineeInfoService.update(tevglTraineeInfo);
		Cookie imgCodeCookie = new Cookie(EvglGlobal.TOKEN_KEY_NAME, token);
		imgCodeCookie.setMaxAge(3600*24*7);
	    imgCodeCookie.setPath("/");
		response.addCookie(imgCodeCookie);
		log.info("签发免登录token => {} => {}", imgCodeCookie.getName(), token);
		session.setAttribute(EvglGlobal.LOGIN_SESSION_TOKEN, token);
		session.setAttribute(EvglGlobal.LOGIN_SESSION_KEY, tevglTraineeInfo);
		session.setAttribute(EvglGlobal.LOGIN_SESSION_ISLOGIN, "Y");
		if(StrUtils.isNotEmpty(tevglTraineeInfo.getMobile())) {
			session.setAttribute(EvglGlobal.LOGIN_SESSION_ISBIND, "Y");
		}
		//同步注册即时通讯用户信息
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("relateId", tevglTraineeInfo.getTraineeId());
			//map.put("relateType", channel);
			List<TimUserinfo> users = timUserinfoService.selectListByMapNoPage(map);
			TimUserinfo user = new TimUserinfo();
			if(users.size() > 0) {
				user.setUserId(users.get(0).getUserId());
			}else {
				user.setStatus("Y");
			}
			user.setBirthday(tevglTraineeInfo.getTraineeBirthday());
			user.setRelateId(tevglTraineeInfo.getTraineeId());
			//user.setRelateType(channel);
			//user.setUsername(info.getTraineeName());
			user.setEmail(tevglTraineeInfo.getEmail());
			user.setMobile(tevglTraineeInfo.getMobile());
			user.setCreateTime(DateUtils.getNowTimeStamp());
			user.setUserimg(tevglTraineeInfo.getTraineeHead());
			user.setUserRealname(tevglTraineeInfo.getTraineeName());
			user.setSex(tevglTraineeInfo.getTraineeSex());
			user.setUserCard(tevglTraineeInfo.getTraineeCard());
			timUserinfoService.save(user);
		}catch(Exception e) {
			e.printStackTrace();
			log.error("系统异常", e.toString());
		}
		return token;
	}
	
	/**
	 * <p>验证是否登录，且是否为教师</p>  
	 * @author huj
	 * @data 2020年1月13日	
	 * @param request
	 * @return
	 */
	@PostMapping("/checkIsTeacher")
	public R checkIsTeacher(HttpServletRequest request) {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if (traineeInfo == null) {
			return R.error("无法获取登录信息，请重新登录");
		}
		Map<String, Object> data = new HashMap<>();
		String isTeacher = "N";
		data.put("isTeacher", isTeacher);
		data.put("traineeId", traineeInfo.getTraineeId());
		data.put("nickName", traineeInfo.getNickName());
		data.put("traineeName", traineeInfo.getTraineeName());
		TevglTchTeacher teacher = tevglTchTeacherService.selectObjectById(traineeInfo.getTraineeId());
		if (teacher == null) {
			log.debug("未能找到用户["+traineeInfo.getTraineeId()+"]对应的教师信息");
			return R.ok().put(Constant.R_DATA, data);
		}
		if ("N".equals(teacher.getState())) {
			log.debug("用户["+traineeInfo.getTraineeId()+"]教师表记录已无效");
		}
		isTeacher = "Y";
		data.put("isTeacher", isTeacher);
		return R.ok().put(Constant.R_DATA, data);
	}
	
	/**
	 * <p>验证是否登录，且是否为教师</p>  
	 * @author huj
	 * @data 2020年1月13日	
	 * @param request
	 * @return
	 */
	@PostMapping("/checkToken")
	public R checkToken(HttpServletRequest request, String token) {
		if (LoginUtils.checkIsLogin(request)) {
			TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
			if ("Y".equals(EvglGlobal.MULTI_USER_LOGIN) && traineeInfo != null) {
				TevglTraineeInfo tevglTraineeInfo = tevglTraineeInfoService.selectObjectById(traineeInfo.getTraineeId());
				JSONObject obj = new JSONObject();
				obj.put("token", token);
				obj.put("traineeId", traineeInfo.getTraineeId());
				//String pic = StrUtils.isEmpty(traineeInfo.getTraineePic()) ? traineeInfo.getTraineeHead() : budaosFieAccessPath + "/trainee-data/" + traineeInfo.getTraineePic();
				String pic = traineeInfo.getTraineeHead();
				if (tevglTraineeInfo != null) {
					obj.put("nickName", tevglTraineeInfo.getNickName());
					pic = traineeInfo.getTraineeHead();
				}
				log.debug("用户类型：" + traineeInfo.getTraineeType());
				if ("4".equals(traineeInfo.getTraineeType())) {
					TevglTchTeacher tevglTchTeacher = tevglTchTeacherService.selectObjectById(traineeInfo.getTraineeId());
					if (tevglTchTeacher != null && "Y".equals(tevglTchTeacher.getState())) {
						if (StrUtils.isNotEmpty(tevglTchTeacher.getTeacherPic())) {
							pic = budaosFieAccessPath + "/teacher-img/" + tevglTchTeacher.getTeacherPic();
						}
					}
				}
				obj.put("traineeHead", pic);
				obj.put("traineeName", traineeInfo.getTraineeName());
				obj.put("traineeQq", StrUtils.isEmpty(traineeInfo.getTraineeQq()) ? "" : traineeInfo.getTraineeQq());
				obj.put("email", StrUtils.isEmpty(traineeInfo.getEmail()) ? "" : traineeInfo.getEmail());
				return R.ok().put("stats", true).put(Constant.R_DATA, obj);
			}
		}
		if(StrUtils.isEmpty(token)) {
			return R.ok().put("stats", false);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", token);
		List<TevglTraineeInfo> users = tevglTraineeInfoService.queryByMap(map);
		if(users.size() == 0) {
			return R.ok().put("stats", false);
		}
		JSONObject obj = new JSONObject();
		obj.put("token", token);
		obj.put("traineeId", users.get(0).getTraineeId());
		obj.put("nickName", users.get(0).getNickName());
		String pic = StrUtils.isEmpty(users.get(0).getTraineePic()) ? users.get(0).getTraineeHead() : budaosFieAccessPath + "/trainee-data/" + users.get(0).getTraineePic();
		if ("4".equals(users.get(0).getTraineeType())) {
			TevglTchTeacher tevglTchTeacher = tevglTchTeacherService.selectObjectById(users.get(0).getTraineeId());
			if (tevglTchTeacher != null && "Y".equals(tevglTchTeacher.getState())) {
				if (StrUtils.isNotEmpty(tevglTchTeacher.getTeacherPic())) {
					pic = budaosFieAccessPath + "/teacher-img/" + tevglTchTeacher.getTeacherPic();
				}
			}
		}
		obj.put("traineeHead", pic);
		return R.ok().put("stats", true).put(Constant.R_DATA, obj);
	}
	
	@RequestMapping("gotogit")
	public void gotogit(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
		if(traineeInfo != null) {
			Map<String, String> map = new HashMap<>();
			map.put("ticket", gitTicket);
			map.put("Content-Type", "application/x-www-form-urlencoded");
			String gets = HttpUtils.sendGet(gitUrl + "/open-api/user/query?username=" + traineeInfo.getMobile(), map, 15);
			JSONObject object = JSONObject.parseObject(gets);
			//如果没有注册，则注册
			if(object.getString("data") == null) {
				String sendPost = HttpUtils.sendPost(gitUrl + "/open-api/user/add", 
						"username=" + traineeInfo.getMobile() + 
						"&usernote=" + (StrUtils.isEmpty(traineeInfo.getTraineeName()) ? traineeInfo.getNickName() : traineeInfo.getTraineeName()) + 
						"&userpwd=" + traineeInfo.getMobile() + 
						"&email=" + traineeInfo.getEmail() + 
						"&cookie=" + traineeInfo.getToken(), map);
				log.debug("自动注册到git：" + sendPost);
			}
			String sendPost = HttpUtils.sendPost(gitUrl + "/open-api/user/autologin", "username=" + traineeInfo.getMobile() + "&cookie=" + traineeInfo.getToken(), map);
			log.debug("自动登陆到git：" + sendPost);
			object = JSONObject.parseObject(sendPost);
			String code = object.getString("git");
			String redirectUrl = gitUrl + "/open-api/user/redirect?code=" + code;
			if(url != null && url.length() > 0 && url.endsWith(".git") && url.indexOf("/r/") >= 0) {
				if(url.endsWith(".git")) {
					url = URLDecoder.decode(url, "utf-8");
					int pos = url.indexOf("/r/") + 3;
					String suf = url.substring(pos);
					url = url.substring(0, pos).replace("/r/", "/summary/") + URLEncoder.encode(URLEncoder.encode(suf, "utf-8"), "utf-8");
				}
				if(!url.startsWith("https")) {
					url = "https" + url.substring(4);
				}
				redirectUrl += "&url=" + URLEncoder.encode(url, "utf-8");
			}
			log.debug("git重定向地址：" + redirectUrl);
			response.sendRedirect(redirectUrl);
			return;
		}
		response.sendRedirect(gitUrl);
	}
}
