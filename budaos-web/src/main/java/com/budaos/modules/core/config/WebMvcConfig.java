package com.budaos.modules.core.config;

import com.budaos.modules.core.common.cbsecurity.repeatsubmit.TokenInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${com.budaos.file-upload-path:/mnt/budaos/uploads}")
	private String filePath;

	@Value("${com.budaos.is-cors:false}")
	private boolean isCors;

	@Value("${com.budaos.cors-url:*}")
	private String corsUrl;
	
	@Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 始终启用跨域，允许前端开发服务器访问
    	registry.addMapping("/**")
        .allowedOriginPatterns("*")
        .allowedMethods("*")
        .allowedHeaders("*")
        .maxAge(3600);
	}

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        // Swagger UI 重定向
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html");
        // 安装向导页面
        registry.addRedirectViewController("/install", "/install.html");
        //registry.addViewController("/").setViewName("forward:/index");
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    	// Swagger 资源映射
    	registry.addResourceHandler("/swagger-ui/**")
    		.addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
    	registry.addResourceHandler("/webjars/**")
    		.addResourceLocations("classpath:/META-INF/resources/webjars/");
    	// 确保路径格式正确（Windows 路径兼容处理）
    	String uploadLocation = filePath;
    	if (!uploadLocation.endsWith("/") && !uploadLocation.endsWith("\\")) {
    		uploadLocation = uploadLocation + "/";
    	}
    	registry.addResourceHandler("/uploads/**")
		.addResourceLocations("file:" + uploadLocation);
    	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    	// 添加 public 目录映射（用于 install.html 等静态页面）
    	registry.addResourceHandler("/**").addResourceLocations("classpath:/public/");
    }
    /**
	 * 添加多个拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 国际化支持
		registry.addInterceptor(initializingLocaleChangeInterceptor());
		// 防止页面重复提交
		registry.addInterceptor(initializingTokenInterceptor()).addPathPatterns("/**");
	}
	@Bean
	public TokenInterceptor initializingTokenInterceptor() {
		logger.info("防止页面重复提交，确保数据安全！");
		return new TokenInterceptor();
	}
    /**
	 * 字符串消息转换器
	 */
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		stringHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
		return stringHttpMessageConverter;
	}

	/**
	 * JSON 数据处理
	 */
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		return mappingJackson2HttpMessageConverter;
	}
	
	// 解决中文乱码问题
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		return converter;
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(stringHttpMessageConverter());
		converters.add(mappingJackson2HttpMessageConverter());
	}


	@Bean
	public LocaleChangeInterceptor initializingLocaleChangeInterceptor() {
		logger.info("国际化支持！");
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		// 参数名
		lci.setParamName("lang");
		return lci;
	}
	@Bean 
	public RestTemplate restTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new CbMappingJackson2HttpMessageConverter());
		return restTemplate;
	}
}