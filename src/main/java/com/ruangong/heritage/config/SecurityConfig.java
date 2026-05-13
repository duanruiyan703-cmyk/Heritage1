package com.ruangong.heritage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 企业级配置类
 *
 * 🎯 核心职责：
 * 1. 统一认证授权管理 - Spring Security统一处理所有安全相关功能
 * 2. JWT过滤器集成 - 自定义JWT认证过滤器集成到Spring Security过滤器链
 * 3. 无状态会话管理 - 适合微服务和分布式架构
 * 4. 方法级安全支持 - 支持@PreAuthorize等注解
 * 5. 一处配置全局生效 - 消除重复配置，统一维护
 *
 * 🚀 架构优化：
 * - 解决循环依赖问题：通过延迟注入和职责分离
 * - 提高代码可维护性：清晰的依赖关系
 * - 符合Spring最佳实践：避免复杂的Bean依赖关系
 *
 * @author system
 * @date 2025-01-27
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // 启用方法级安全，支持@PreAuthorize等注解
public class SecurityConfig {

    /**
     * 定义公开访问路径常量
     * 这些路径不需要JWT验证，可以直接访问
     */
    private static final String[] PUBLIC_PATHS = {
        // 系统基础路径
        "/",
        "/health",
        "/favicon.ico",

        // API文档相关
        "/doc.html",
        "/webjars/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-resources/**",

        // 认证相关接口（必须公开）
        "/api/user/auth",        // 匿名用户认证（注册/登录）
        "/api/user/login",       // 用户登录
        "/api/user/register",    // 用户注册
        "/api/user/forget",      // 忘记密码
        "/api/user/add",         // 用户添加
        "/api/file/**",          // 临时公开
        "/api/**",
        // 公开信息接口
        "/api/user/{id}",        // 用户信息查询（公开）

        // 静态资源（与实际目录结构一致）
        "/static/**",           // 项目静态资源统一路径
        "/files/**",            // 文件上传目录访问
        "/*.html",              // 根路径下的HTML文件
        "/file-test.html"       // 文件测试页面
    };

    /**
     * 密码编码器Bean
     *
     * 🎯 职责分离：
     * - 独立定义，避免与其他Bean形成循环依赖
     * - 使用BCrypt加密算法，安全性高
     * - 全局共享，其他服务可以直接注入使用
     *
     * @return PasswordEncoder BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT认证过滤器Bean
     *
     * 🎯 解决循环依赖：
     * - 通过@Bean方式创建，而不是@Resource注入
     * - Spring容器会自动处理依赖关系
     * - 避免SecurityConfig直接依赖JwtAuthenticationFilter
     *
     * @return JwtAuthenticationFilter JWT认证过滤器实例
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * 配置Spring Security过滤器链
     *
     * 核心功能：
     * 1. 禁用CSRF - 适合API服务
     * 2. 无状态会话 - 适合JWT认证
     * 3. 路径权限配置 - 公开路径vs受保护路径
     * 4. JWT过滤器集成 - 自定义认证逻辑
     *
     *  安全策略：
     * - 默认所有请求需要认证
     * - 公开路径允许匿名访问
     * - JWT过滤器在用户名密码认证之前执行
     *
     * @param http HttpSecurity配置对象
     * @return SecurityFilterChain 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护（API服务通常不需要）
            .csrf(csrf -> csrf.disable())

            // 配置会话管理为无状态（适合JWT）
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 配置请求授权规则
            .authorizeHttpRequests(auth -> auth
                // 公开路径，允许匿名访问
                .requestMatchers(PUBLIC_PATHS).permitAll()
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )

            // 添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}