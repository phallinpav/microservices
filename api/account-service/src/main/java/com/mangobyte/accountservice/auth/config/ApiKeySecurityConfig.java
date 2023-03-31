package com.mangobyte.accountservice.auth.config;

import com.mangobyte.accountservice.auth.filter.ApiKeyAuthFilter;
import com.mangobyte.accountservice.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@Order(2)
@RequiredArgsConstructor
public class ApiKeySecurityConfig extends WebSecurityConfigurerAdapter {
    private final ApiKeyAuthFilter apiKeyAuthFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .antMatcher("/**")
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/sys/**").hasAuthority(Role.ADMIN.toString())
            .antMatchers("/error").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(new BasicAuthenticationEntryPoint())
            .and()
            .addFilter(apiKeyAuthFilter);
    }

}
