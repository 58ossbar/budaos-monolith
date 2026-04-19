package com.budaos.utils.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvUtils {

	@Value("${spring.profiles.active:}")
	private String env;
	
	/**
	 * 获取当前运行环境，值与spring.profiles.active参数一致
	 * @return
	 */
	public String getEnv() {
		return env;
	}
	
	/**
	 * 是否是开发环境
	 * @return
	 */
	public boolean isDev() {
		return "dev".equals(env);
	}
	
	/**
	 * 是否是测试环境
	 * @return
	 */
	public boolean isTest() {
		return "test".equals(env);
	}
	
	/**
	 * 是否是生产环境
	 * @return
	 */
	public boolean isProd() {
		return "prod".equals(env);
	}
}
