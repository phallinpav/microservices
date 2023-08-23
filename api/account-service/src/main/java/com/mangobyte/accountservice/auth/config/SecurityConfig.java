package com.mangobyte.accountservice.auth.config;

import com.mangobyte.accountservice.auth.filter.ApiKeyAuthFilter;
import com.mangobyte.accountservice.auth.provider.UsernamePasswordAuthenticationProvider;
import com.mangobyte.accountservice.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UsernamePasswordAuthenticationProvider provider;
    private final ApiKeyAuthFilter apiKeyAuthFilter;

    @Value("${apikey.header}")
    private String apiKeyHeaderName;

    // Open this code, if you want to request from frontend directly to backend server without kong as gateway (which support cors)
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization", apiKeyHeaderName));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        config.setMaxAge(Duration.ofMinutes(10));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChainUserPass(HttpSecurity http) throws Exception {
        final AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.authenticationProvider(provider);

        http.antMatcher("/token/**")
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChainApi(HttpSecurity http) throws Exception {
        http
            .antMatcher("/**")
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/sys/**").hasAuthority(Role.ADMIN.toString())
            .antMatchers("/account/verified").permitAll()
            .antMatchers(HttpMethod.POST, "/account").permitAll()
            .antMatchers("/error").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(new BasicAuthenticationEntryPoint())
            .and()
            .addFilter(apiKeyAuthFilter);

        return http.build();
    }

}
