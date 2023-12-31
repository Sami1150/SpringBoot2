package com.springboot.mapping.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableCaching
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

        /*
        * void customize(WebSecurity web)
        Performs the customizations on WebSecurity.
        Parameters:
            web - the instance of WebSecurity to apply to customizations to
        @Bean
        public WebSecurityCustomizer ignoringCustomizer()
             return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2");

        *** GPT ***
            * Configure custom exception handling for authentication and authorization failures.
            * Add or modify security filters in the request processing chain.
            * Define specific URL patterns that should be ignored or allowed without authentication.
            * Customize headers, CSRF protection, or other security-related settings.
        *** ***
        */

//        return web -> web.ignoring().requestMatchers(ignored).requestMatchers(HttpMethod.GET, ignoredGet);
        return web -> web.ignoring().requestMatchers(
//                new AntPathRequestMatcher("/api/v1/teacher", "GET"),
//                new AntPathRequestMatcher("/api/v1/teacher","POST"),
//                new AntPathRequestMatcher("/h2-console/**", "GET"), // Allow GET requests to h2-console
//                new AntPathRequestMatcher("/h2-console/**", "POST"),
                new AntPathRequestMatcher("/actuator", "GET")
//                ,new AntPathRequestMatcher("/actuator/**","GET")
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*Enable and customize  form based authentication

          Form-based authentication is a common way to authenticate users by presenting them with a login form where they can enter their credentials.

         */
//        http.formLogin(Customizer.withDefaults());

//        http.exceptionHandling(config -> config.defaultAuthenticationEntryPointFor(
//                (request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not Authorized"),
//                AntPathRequestMatcher.antMatcher(api)));

//        http.csrf(config -> config.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()));

//        http.authorizeHttpRequests(config -> config.anyRequest().authenticated());

//        Disable CSRF
//        http.csrf(csrf -> csrf.disable());



        return http.build();
    }

}