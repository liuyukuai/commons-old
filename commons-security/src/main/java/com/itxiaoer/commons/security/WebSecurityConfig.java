package com.itxiaoer.commons.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author : liuyk
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationEntryPoint unauthorizedHandler;

    @Resource
    private WebAuthProperties webAuthProperties;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private AbstractAuthenticationTokenFilter abstractAuthenticationTokenFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void config(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    @SuppressWarnings("unchecked")
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String permitAll = webAuthProperties.getPermitAll();

        Map<RoleData, Set<String>> roleDataListMap = new LinkedHashMap<>();

        Map<String, Object> roleMap = webAuthProperties.getRoles();

        if (roleMap != null && !roleMap.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = roleMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                // key  role
                String role = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    String urls = String.class.cast(value);

                    Stream.of(StringUtils.split(urls, ",")).forEach(e ->
                            roleDataListMap.computeIfAbsent(new RoleData(e, ""), i -> new HashSet<>()).add(role)
                    );
                }
                if (value instanceof LinkedHashMap) {
                    // GET -> URL
                    LinkedHashMap<String, String> urlMap = (LinkedHashMap<String, String>) value;
                    Set<Map.Entry<String, String>> urlEntries = urlMap.entrySet();
                    for (Map.Entry<String, String> urlRole : urlEntries) {
                        String urls = urlRole.getValue();
                        String method = urlRole.getKey();
                        Stream.of(StringUtils.split(urls, ",")).forEach(e ->
                                roleDataListMap.computeIfAbsent(new RoleData(e, method), i -> new HashSet<>()).add(role)
                        );
                    }
                }
            }
        }

        Set<Map.Entry<RoleData, Set<String>>> entries = roleDataListMap.entrySet();
        for (Map.Entry<RoleData, Set<String>> entry : entries) {
            RoleData key = entry.getKey();
            if (StringUtils.isBlank(key.getMethod())) {
                httpSecurity.authorizeRequests().antMatchers(key.getUrl()).hasAnyRole(entry.getValue().toArray(new String[]{}));
            } else {
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.valueOf(key.getMethod()), key.getUrl()).hasAnyRole(entry.getValue().toArray(new String[]{}));
            }
        }

        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                //ALWAYS 如果session不存在总是需要创建；
                //NEVER 框架从不创建session，但如果已经存在，会使用该session ；
                //IF_REQUIRED 仅当需要时，创建session(默认配置)；
                // Spring Security不会创建session，或使用session；
                // STATELESS 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(webAuthProperties.isEnableSession() ? SessionCreationPolicy.IF_REQUIRED : SessionCreationPolicy.STATELESS).and()

                .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().and()

                .authorizeRequests().antMatchers(StringUtils.isBlank(permitAll) ? new String[]{} : permitAll.split(",")).permitAll()

                .anyRequest().authenticated();

        // 添加JWT filter
        httpSecurity.addFilterBefore(abstractAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/",
                "/*.html",
                "/favicon.ico",
                "/webjars/**",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.ttf",
                "/**/*.woff2",
                "/**/*.woff",
                "/**/api-docs",
                "/swagger-resources");
    }
}