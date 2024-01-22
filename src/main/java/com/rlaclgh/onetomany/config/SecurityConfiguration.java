package com.rlaclgh.onetomany.config;


import com.rlaclgh.onetomany.filter.ExceptionHandlerFilter;
import com.rlaclgh.onetomany.filter.JWTValidatorFilter;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Slf4j
public class SecurityConfiguration {


  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
        Arrays.asList("http://127.0.0.1:3000", "https://onetomany.site",
            "https://www.onetomany.site"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }


  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.POST, "/auth/sign-in", "/auth/sign-up").permitAll()
        .requestMatchers(HttpMethod.GET, "/chat_room/*", "/chat_room").permitAll()
        .requestMatchers("/").permitAll()

        .anyRequest().authenticated()
    );

    http.addFilterBefore(new JWTValidatorFilter(), UsernamePasswordAuthenticationFilter.class);

    http.addFilterBefore(
        new ExceptionHandlerFilter(),
        JWTValidatorFilter.class
    );

    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.csrf(AbstractHttpConfigurer::disable);

    http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
        corsConfigurationSource()));

    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

    return http.build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


}
