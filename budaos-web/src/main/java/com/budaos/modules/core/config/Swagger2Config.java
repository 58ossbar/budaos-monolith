package com.budaos.modules.core.config;

import com.google.common.base.Predicate;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
	/**
	 * 定义api组，
	 */
    @Bean
	public Docket innerApi() {

		return new Docket(DocumentationType.SWAGGER_2).groupName("innerApi")
				.genericModelSubstitutes(DeferredResult.class)
				// .genericModelSubstitutes(ResponseEntity.class)
				.useDefaultResponseMessages(false).forCodeGeneration(true).select()
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) // 采用注解的方式
				//.apis(RequestHandlerSelectors.basePackage("com.budaos.modules.sys.service"))//这里采用包扫描的方式来确定要显示的接口
				.paths(PathSelectors.any()).build().apiInfo(innerApiInfo());
	}

	@Bean
	public Docket openApi() {

		@SuppressWarnings("unused")
		Predicate<RequestHandler> predicate = new Predicate<RequestHandler>() {
			@Override
			public boolean apply(RequestHandler input) {
				// Class<?> declaringClass = input.declaringClass();
				// if (declaringClass == BasicErrorController.class)// 排除
				// return false;
				// if(declaringClass.isAnnotationPresent(ApiOperation.class)) // 被注解的类
				// return true;
				// if(input.isAnnotatedWith(ResponseBody.class)) // 被注解的方法
				// return true;
				if (input.isAnnotatedWith(ApiOperation.class))// 只有添加了ApiOperation注解的method才在API中显示
					return true;
				return false;
			}
		};

		return new Docket(DocumentationType.SWAGGER_2).groupName("openApi")
				.genericModelSubstitutes(DeferredResult.class)
				// .genericModelSubstitutes(ResponseEntity.class)
				.useDefaultResponseMessages(false).forCodeGeneration(true).select()
				//.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) // 采用注解的方式
				.apis(RequestHandlerSelectors.basePackage("com.budaos.modules.sys.controller"))//这里采用包扫描的方式来确定要显示的接口
				.paths(PathSelectors.any()).build().apiInfo(openApiInfo());
	}

	@SuppressWarnings("deprecation")
	private ApiInfo innerApiInfo() {
		return new ApiInfoBuilder().title("创蓝基础平台").description("内部API：http://www.budaos.com")
				.termsOfServiceUrl("http://www.budaos.com").contact(new Contact("湖南创蓝信息科技有限公司", "http://www.budaos.com", ""))
				.version("1.0").build();
	}

	@SuppressWarnings("deprecation")
	private ApiInfo openApiInfo() {
		return new ApiInfoBuilder().title("创蓝基础平台")// 大标题
				.description("创蓝提供的OpenAPI")// 详细描述
				.termsOfServiceUrl("http://www.budaos.com").contact(new Contact("湖南创蓝信息科技有限公司", "http://www.budaos.com", ""))
				.version("1.0").build();
	}

}