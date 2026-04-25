package com.budaos.modules.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置类
 * 放行登录相关接口，允许匿名访问
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（开发环境可禁用，生产环境根据需要开启）
            .csrf().disable()
            // 配置请求授权
            .authorizeRequests()
                // 放行验证码接口
                .antMatchers("/user/captcha.jpg").permitAll()
                // 放行登录接口
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/doLogin").permitAll()
                // 放行查询设置接口
                .antMatchers("/user/querySettings").permitAll()
                // 放行所有API接口（开发环境临时放行）
                .antMatchers("/api/**").permitAll()
                // 放行 evgl 相关接口（布道师学习通前端使用）
                .antMatchers("/login-api/**").permitAll()
                .antMatchers("/index-api/**").permitAll()
                .antMatchers("/evgl/**").permitAll()
                .antMatchers("/classroom-api/**").permitAll()
                .antMatchers("/managementPanel-api/**").permitAll()
                .antMatchers("/pkginfo-api/**").permitAll()
                .antMatchers("/resourceCenter-api/**").permitAll()
                .antMatchers("/questions-api/**").permitAll()
                .antMatchers("/cloud-api/**").permitAll()
                .antMatchers("/teachingCenter-api/**").permitAll()
                .antMatchers("/subject-api/**").permitAll()
                .antMatchers("/major-api/**").permitAll()
                .antMatchers("/examCenter-api/**").permitAll()
                .antMatchers("/testPaper-api/**").permitAll()
                .antMatchers("/teacher-api/**").permitAll()
                .antMatchers("/score-api/**").permitAll()
                .antMatchers("/dict-api/**").permitAll()
                .antMatchers("/news-api/**").permitAll()
                .antMatchers("/partner-api/**").permitAll()
                .antMatchers("/classroom-trainee-api/**").permitAll()
                .antMatchers("/classroom-group-api/**").permitAll()
                .antMatchers("/classroom-trainee-check-api/**").permitAll()
                .antMatchers("/sign-api/**").permitAll()
                .antMatchers("/activity/**").permitAll()
                .antMatchers("/site/**").permitAll()
                .antMatchers("/paperExamine/**").permitAll()
                .antMatchers("/trainee-story/**").permitAll()
                .antMatchers("/cbim/**").permitAll()
                // 放行静态资源
                .antMatchers("/static/**").permitAll()
                .antMatchers("/uploads/**").permitAll()
                .antMatchers("/*.html").permitAll()
                // 放行 Swagger 相关
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            .and()
            // 禁用表单登录跳转
            .formLogin().disable()
            .httpBasic().disable();
        
        return http.build();
    }
}
