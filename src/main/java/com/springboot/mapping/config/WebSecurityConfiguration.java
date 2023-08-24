package com.springboot.mapping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@EnableMethodSecurity
@Configuration
public class WebSecurityConfiguration {

//    @Value("${spring.web.security.ignored:/error,/ui/**}")
//    private String[] ignored = { "/error", "/ui/**" };
//
//    @Value("${spring.web.security.ignored.get:/api/v1/teacher}")
//    private String[] ignoredGet = { "/api/v1/teacher" };
//
//    @Value("${spring.web.security.api:/api/**}")
//    private String api = "/api/**";

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers(ignored).requestMatchers(HttpMethod.GET, ignoredGet);
        return web -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/api/v1/teacher", "GET"),
                new AntPathRequestMatcher("/h2-console/**", "GET"), // Allow GET requests to h2-console
                new AntPathRequestMatcher("/h2-console/**", "POST")
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(Customizer.withDefaults());
//        http.exceptionHandling(config -> config.defaultAuthenticationEntryPointFor(
//                (request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not Authorized"),
//                AntPathRequestMatcher.antMatcher(api)));
        http.csrf(config -> config.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()));

        http.authorizeHttpRequests(config -> config.anyRequest().authenticated());
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}