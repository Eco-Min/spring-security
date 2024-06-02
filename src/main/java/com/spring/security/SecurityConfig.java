package com.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        // ExceptionTranslationFilter, sendStartAuthentication() -> exception filter 부분을 처리한다
                        /*.authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("auth Exception: " + authException.getMessage());
                            response.sendRedirect("/login"); // 해당 페이지는 생성 해줘야함 -> 기본이랑 다름.
                        })*/
                        // ExceptionTranslationFilter, sendStartAuthentication() -> exception filter 부분을 처리한다
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("accessDeniedException: " + accessDeniedException.getMessage());
                            response.sendRedirect("/denied");
                        })
                );
        return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}1111")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}