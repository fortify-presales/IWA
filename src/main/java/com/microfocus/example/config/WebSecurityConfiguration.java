/*
        Insecure Web App (IWA)

        Copyright (C) 2020-2022 Micro Focus or one of its affiliates

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

import com.microfocus.example.config.handlers.*;
import com.microfocus.example.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configure Spring Security for custom application
 * @author Kevin A. Lee
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    public static final String REALM_NAME = "IWA";

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private BasicAuthenticationEntryPointCustom basicAuthenticationEntryPoint;

    @Autowired
    private ApiAccessDeniedHandler apiAccessDeniedHandler;

    @Autowired
    private AuthenticationEntryPointJwt unauthorizedHandler;

    @Autowired
    private PasswordConfiguration passwordEncoder;

    @Bean
    public AuthenticationTokenFilter authenticationJwtTokenFilter() {
        return new AuthenticationTokenFilter();
    }

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder.passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        /*http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .anyRequest().authenticated();*/

        httpSecurity.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/v3/site/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v3/users/unread-message-count").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/v3/users/*").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v3/products").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v3/products/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v3/reviews").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v3/reviews/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN", "API")
                .antMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN", "API")
                .antMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "API")
                .antMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("ADMIN", "API")
                .and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and().exceptionHandling().authenticationEntryPoint(basicAuthenticationEntryPoint)
                .and().exceptionHandling().accessDeniedHandler(apiAccessDeniedHandler)
                .and().csrf().disable();

        httpSecurity.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }

}
