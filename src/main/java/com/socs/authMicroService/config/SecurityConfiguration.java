package com.socs.authMicroService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.socs.workersManagement.Permission.ADMIN_CREATE;
import static com.socs.workersManagement.Permission.ADMIN_DELETE;
import static com.socs.workersManagement.Permission.ADMIN_READ;
import static com.socs.workersManagement.Permission.ADMIN_UPDATE;
import static com.socs.workersManagement.Permission.MANAGER_CREATE;
import static com.socs.workersManagement.Permission.MANAGER_DELETE;
import static com.socs.workersManagement.Permission.MANAGER_READ;
import static com.socs.workersManagement.Permission.MANAGER_UPDATE;
import static com.socs.workersManagement.Role.ADMIN;
import static com.socs.workersManagement.Role.CM;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.List;
// import java.util.Arrays;
// import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

        private static final String[] WHITE_LIST_URL = {
                        "/cafeteria/**",
                        "/student/**",
                        "/api/v1/laptop/**",
                        "/api/v1/demo-controller",
                        "/socs/**",
                        "/uploads/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html" };
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final LogoutHandler logoutHandler;

        // @Bean
        // public CorsConfigurationSource corsConfigurationSource() {
        // CorsConfiguration configuration = new CorsConfiguration();
        // configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        // configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT",
        // "DELETE", "OPTIONS"));
        // configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        // configuration.setAllowCredentials(true);

        // UrlBasedCorsConfigurationSource source = new
        // UrlBasedCorsConfigurationSource();
        // source.registerCorsConfiguration("/**", configuration);
        // return source;
        // }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // .cors()

                                .csrf(AbstractHttpConfigurer::disable)
                                // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authorizeHttpRequests(req -> req
                                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                                .requestMatchers("/api/v1/management/**")
                                                .hasAnyRole(ADMIN.name(), CM.name())
                                                .requestMatchers(GET, "/api/v1/management/**")
                                                .hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                                                .requestMatchers(POST, "/api/v1/management/**")
                                                .hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                                                .requestMatchers(PUT, "/api/v1/management/**")
                                                .hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                                                .requestMatchers(DELETE, "/api/v1/management/**")
                                                .hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                                                .addLogoutHandler(logoutHandler)
                                                .logoutSuccessHandler(
                                                                (request, response,
                                                                                authentication) -> SecurityContextHolder
                                                                                                .clearContext()));

                return http.build();
        }

        // private CorsConfigurationSource corsConfigurationSource() {
        // return new CorsConfigurationSource() {
        // @Override
        // public CorsConfiguration getCorsConfiguration(@SuppressWarnings("null")
        // HttpServletRequest request) {
        // CorsConfiguration ccfg = new CorsConfiguration();
        // ccfg.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // ccfg.setAllowedMethods(Collections.singletonList("*"));
        // ccfg.setAllowCredentials(true);
        // ccfg.setAllowedHeaders(Collections.singletonList("*"));
        // ccfg.setExposedHeaders(Arrays.asList("Authorization"));
        // ccfg.setMaxAge(3600L);
        // return ccfg;

        // }
        // };

        // }
}
