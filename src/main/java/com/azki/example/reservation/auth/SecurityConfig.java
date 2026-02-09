
package com.azki.example.reservation.auth;

import jakarta.servlet.DispatcherType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableConfigurationProperties({SecurityProperties.class})
@Configuration
public class SecurityConfig {

    private final static RequestMatcher ADMIN_REQUEST_MATCHER = PathPatternRequestMatcher.pathPattern("/admin/**");

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Order(1)
    @Bean
    SecurityFilterChain security(HttpSecurity http, JwtAuthFilter filter) {
        http.securityMatcher(new NegatedRequestMatcher(ADMIN_REQUEST_MATCHER))
                .csrf(CsrfConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/doc/**").permitAll()
                        .anyRequest().fullyAuthenticated())
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                })
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Order(2)
    @Bean
    SecurityFilterChain adminSecurity(HttpSecurity http, SecurityProperties securityProperties) {
        http.securityMatcher(ADMIN_REQUEST_MATCHER)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationManager(adminAuthenticationManager(securityProperties.admins()))
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .anyRequest().fullyAuthenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    private AuthenticationManager adminAuthenticationManager(SecurityProperties.AdminAreaProperties[] admins) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var userDetailsService = new InMemoryUserDetailsManager();
        for (SecurityProperties.AdminAreaProperties admin : admins) {
            var user = User.withUsername(admin.username())
                    .password(passwordEncoder.encode(admin.password()))
                    .roles(admin.roles())
                    .build();
            userDetailsService.createUser(user);
        }
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

}
