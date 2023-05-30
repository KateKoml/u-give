package com.ugive.security.config;

import com.ugive.security.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(6);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/v3/api-docs/**", "/v2/api-docs", "/configuration/ui/**", "/swagger-resources/**",
                        "/configuration/security/**", "/swagger-ui/**", "/swagger-ui.html#", "/webjars/**"
                ).permitAll()
                .requestMatchers("/rest/auth").permitAll()
                .requestMatchers(HttpMethod.PUT, "/rest/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/rest/offers/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/rest/categories/**").permitAll()
                .requestMatchers("/rest/users/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/rest/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/rest/**").hasAnyRole("ADMIN", "USER", "MODERATOR")

                //hasRole("ADMIN")
                .anyRequest().authenticated();

        return http.build();
    }
}
