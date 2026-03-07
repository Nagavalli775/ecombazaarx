package com.example.ecombazaarx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.ecombazaarx.security.JwtFilter;


@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
               .requestMatchers(
                // "/",
                // "/*.html",
                "/registerSeller",
                "/registerUser",
                "/pageSignin",
                "/seller/dashboard",
                "/admin/dashboard",
                "/landing",
                "/uploads/**",
                "/css/**",
                "/js/**",
                "/assets/**"
               ).permitAll()

            // //    PAGE ACCESS
            //   .requestMatchers("/seller/dashboard").hasAnyRole("SELLER","ADMIN")
            //   .requestMatchers("/admin/dashboard").hasRole("ADMIN")

            //    APIs
               .requestMatchers("/api/products").permitAll()
              .requestMatchers("/signup/**", "/login").permitAll()
              .requestMatchers("/user/profile").hasRole("USER")
              .requestMatchers("/api/admin/**").hasRole("ADMIN")
              .requestMatchers("/api/seller/profile").hasRole("SELLER")
              .anyRequest().authenticated()
          )
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}