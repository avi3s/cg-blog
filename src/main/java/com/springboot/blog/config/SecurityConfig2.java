package com.springboot.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.CustomUserDetailsService;
import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig2 {

	@Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(){
        return  new JwtAuthenticationFilter();
    }
	
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, CustomUserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
    	return new CustomUserDetailsService(userRepository);
    }

    @SuppressWarnings("deprecation")
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
        .csrf().disable()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
        .requestMatchers("/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html").permitAll()
        .requestMatchers("/api/auth/**").permitAll()
        .anyRequest()
        .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}