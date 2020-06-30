/*
        Secure Web App

        Copyright (C) 2020 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.config;

import com.microfocus.example.service.CustomUserDetailsService;
import com.microfocus.example.web.BasicAuthenticationEntryPointCustom;
import com.microfocus.example.web.UrlAuthenticationSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configure Spring Security for custom application
 * @author Kevin A. Lee
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    public static final String REALM_NAME = "secure-web-app";

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private BasicAuthenticationEntryPointCustom authenticationEntryPoint;

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        if (activeProfile.contains("dev")) {
            log.info("Running development profile");
            httpSecurity.csrf().disable();
            httpSecurity.headers().frameOptions().disable();
        }

        httpSecurity
                .authorizeRequests()
                .antMatchers(
                    "/",
                    "/products/**",
                    "/services/**",
                    "/login",
                    "/logout",
                    "/register",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/v2/api-docs/**",
                    "/console/*",
                    "/site-message",
                    "/js/**/*",
                    "/css/**/*",
                    "/img/**/*",
                    "/webjars/**/*").permitAll()
                // Only admin can access /admin portal
                .antMatchers("/admin/**").hasRole("ADMIN")
                // Only admin can perform HTTP delete operation
                .antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                // any admin/api user can perform api operations
                .antMatchers("/api/**").hasAnyRole("ADMIN", "API")
                .anyRequest().fullyAuthenticated();

        httpSecurity.authorizeRequests().and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");

        httpSecurity.authorizeRequests().and().httpBasic()
                .realmName(REALM_NAME)
                .authenticationEntryPoint(authenticationEntryPoint);

        httpSecurity.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .successHandler(CustomAuthenticationSuccessHandler())
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password");

        httpSecurity.authorizeRequests().and().logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationSuccessHandler CustomAuthenticationSuccessHandler(){
        return new UrlAuthenticationSuccessHandler();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
