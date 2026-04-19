package com.budaos.modules.evgl.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2018 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 * @date 2018年8月13日 上午9:56:57
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**").excludePathPatterns("/open-api/**");
        registry.addInterceptor(new UploadInterceptor()).addPathPatterns("/cbeditor/**");
        registry.addInterceptor(new UploadInterceptor()).addPathPatterns("/typora/**");
    }
}
