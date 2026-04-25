package com.budaos.modules.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

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
	 * 定义api组，扫描所有 REST Controller
	 */
	@Bean
	public Docket innerApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("innerApi")
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(true)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(innerApiInfo());
	}

	@SuppressWarnings("deprecation")
	private ApiInfo innerApiInfo() {
		return new ApiInfoBuilder()
				.title("布道师学习通")
				.description("内部API：http://www.budaos.com")
				.termsOfServiceUrl("http://www.budaos.com")
				.contact(new Contact("布道师学习通", "http://www.budaos.com", ""))
				.version("1.0")
				.build();
	}

}