package com.budaos.modules.job.controller;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.sys.api.TsysLoginLogService;
import com.budaos.modules.sys.api.TsysSettingsService;
import com.budaos.modules.sys.api.TsysUserinfoService;
import com.budaos.modules.sys.domain.TsysLoginLog;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.utils.redis.RedisUtils;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.IPUtils;
import com.budaos.utils.tool.StrUtils;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RefreshScope
@RequestMapping("/user")
public class LoginController {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private TsysSettingsService tsysSettingsService;
	@Autowired
	private Producer producer;
	@Autowired
	private TsysUserinfoService tsysUserinfoService;
	@Autowired
	private TsysLoginLogService tsysLoginLogService;
	@Autowired
	private RedisUtils redisUtils;

	/** 验证码 Redis key 前缀 */
	private static final String CAPTCHA_KEY_PREFIX = "captcha:";
	/** 验证码 Redis 过期时间（秒） */
	private static final long CAPTCHA_EXPIRE_SECONDS = 120;

	@Value("${security.oauth2.client.access-token-uri:}")
	private String url;
	@Value("${security.oauth2.client.logout:}")
	private String logoutUrl;
	@Value("${security.oauth2.client.client-id:}")
	private String clientId;
	@Value("${security.oauth2.client.client-secret:}")
	private String clientSecret;

	@RequestMapping("login")
	public R doLogin(@RequestBody(required = false) JSONObject data, HttpSession session, HttpServletRequest request) {
		String username = data.getString("username");
		String password = data.getString("password");
		String captcha = data.getString("captcha");
		String captchaKey = data.getString("captchaKey");
		log.info("[Login] 收到登录请求: username={}, captchaKey={}", username, captchaKey);
		// 优先从 Redis 读取（解决网关转发时 Session 不一致的问题）
		String captchaInRedis = null;
		if (StrUtils.isNotEmpty(captchaKey)) {
			try {
				captchaInRedis = redisUtils.getCacheObject(CAPTCHA_KEY_PREFIX + captchaKey);
				log.info("[Login] 从 Redis 获取验证码: key={}, value={}", captchaKey, captchaInRedis);
				redisUtils.deleteCache(CAPTCHA_KEY_PREFIX + captchaKey); // 一次性使用即删除
			} catch (Exception e) {
				// Redis 不可用时降级到 Session
				log.warn("[Login] Redis 不可用，降级到 Session 验证验证码: {}", e.getMessage());
			}
		}
		// 如果 Redis 中没有（兼容旧客户端），则回退到 Session 查询
		if (captchaInRedis == null) {
			captchaInRedis = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
			log.info("[Login] 从 Session 获取验证码: value={}", captchaInRedis);
			session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
		}
		if (StrUtils.isNull(captchaInRedis) || StrUtils.isNull(captcha)
				|| !captcha.equalsIgnoreCase(captchaInRedis)) {
			log.warn("[Login] 验证码错误: 输入={}, 正确={}", captcha, captchaInRedis);
			TsysLoginLog tsysLoginLog = new TsysLoginLog();
			tsysLoginLog.setLogname("系统登陆");
			tsysLoginLog.setCreateTime(DateUtils.getNowTimeStamp());
			tsysLoginLog.setSucceed("失败");
			tsysLoginLog.setMessage("验证码错误！");
			tsysLoginLog.setIp(IPUtils.getIpAddr(request));
			tsysLoginLogService.save(tsysLoginLog); // 保存登陆日志信息
			return R.error("验证码错误！");
		}
		log.info("[Login] 验证码验证通过");
		TsysUserinfo userInfo = tsysUserinfoService.selectObjectByUserName(username);
		if (userInfo == null) {
			TsysLoginLog tsysLoginLog = new TsysLoginLog();
			tsysLoginLog.setLogname("系统登陆");
			tsysLoginLog.setCreateTime(DateUtils.getNowTimeStamp());
			tsysLoginLog.setSucceed("失败");
			tsysLoginLog.setMessage("账号或密码错误！");
			tsysLoginLog.setIp(IPUtils.getIpAddr(request));
			tsysLoginLogService.save(tsysLoginLog); // 保存登陆日志信息
			return R.error("账号或密码错误！");
		}
		// 单体应用简化登录流程，跳过 OAuth2，直接生成 token
		try {
			// 记录登录日志
			tsysLoginLogService.saveSuccessMessage(request, "用户正常登录", userInfo.getUserRealname());
			
			// 将用户信息存入 Session（mgr 应用使用 Session 认证）
			session.setAttribute("LOGIN_USER", userInfo);
			log.info("[Login] 用户 {} 登录成功，Session ID: {}", username, session.getId());
			
			// 生成简单 token（使用 session id 作为 token）
			String token = session.getId();
			
			// 组装部分数据，满足前端需要
			Map<String, Object> m = new HashMap<>();
			m.put("userimg", userInfo.getUserimg()); // 用户头像
			m.put("userId", userInfo.getUserId()); // 用户ID
			m.put("userRealname", userInfo.getUserRealname()); // 用户真实姓名
			return R.ok().put("token", token).put("data", m);
		} catch (Exception e) {
			log.error("系统出现了问题！", e);
			tsysLoginLogService.saveFailMessage(request, "登录失败");
			return R.error("系统开了小差，请重新试一下！");
		}
	}

	@RequestMapping("captcha.jpg")
	public void captcha(@RequestParam(required = false) String key, HttpSession session, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 优先存入 Redis（解决网关转发时 Session 不一致的问题）
		if (StrUtils.isNotEmpty(key)) {
			try {
				redisUtils.setCacheObject(CAPTCHA_KEY_PREFIX + key, text, CAPTCHA_EXPIRE_SECONDS);
				log.info("[Captcha] 验证码已存入 Redis, key: {}", key);
			} catch (Exception e) {
				// Redis 不可用时降级到 Session
				log.warn("[Captcha] Redis 不可用，降级到 Session 存储验证码: {}", e.getMessage());
				session.setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
			}
		} else {
			// 兼容旧客户端：没有传 key 时仍保存到 Session
			session.setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
			log.info("[Captcha] 验证码已存入 Session (无 key 参数)");
		}
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
		log.info("[Captcha] 验证码图片已生成并输出");
	}

	@RequestMapping("logout")
	public R doLogout(HttpSession session, @RequestHeader("authorization") String authorization, String accessToken) {
		try {
			if (authorization != null && authorization.length() > 6) {
				accessToken = authorization.substring(6).trim();
			}
			// 开发阶段屏蔽注销token
			// restTemplate.getForObject(logoutUrl + "?accessToken=" + accessToken,
			// Object.class);
			session.invalidate();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return R.ok();
	}

	/**
	 * 查询设置 settingType 系统设置或用户设置（必填） settingUserId 用户设置则必填
	 * 
	 * @param map
	 * @return
	 */
	@GetMapping("/querySettings")
	@SysLog("查询")
	public R querySetting(@RequestParam(required = true) Map<String, Object> map) {
		return tsysSettingsService.querySetting(map);
	}
}
