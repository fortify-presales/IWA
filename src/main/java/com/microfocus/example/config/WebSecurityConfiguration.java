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
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

    @Bean
    public AuthenticationTokenFilter authenticationJwtTokenFilter() {
        return new AuthenticationTokenFilter();
    }

    @Autowired
    private SessionRegistry sessionRegistry;

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Configuration
    @Order(1)
    public class ApiConfigurationAdapter extends WebSecurityConfigurerAdapter {

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
                    //.and().httpBasic().authenticationEntryPoint(basicAuthenticationEntryPoint)
                    .and().exceptionHandling().accessDeniedHandler(apiAccessDeniedHandler)
                    .and().csrf().disable();

            httpSecurity.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        }

    }

    @Configuration
    @Order(2)
    public class UserConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            if (activeProfile.contains("dev")) {
                log.info("Running development profile");
                httpSecurity.csrf().disable();
                httpSecurity.headers().frameOptions().disable();
                httpSecurity.cors().disable();
                httpSecurity.headers().xssProtection().disable();
            }

            /*
            http.headers()
           .contentSecurityPolicy("script-src 'self' https://trustedscripts.example.com; object-src https://trustedplugins.example.com; report-uri /csp-report-endpoint/");
             */

            httpSecurity.authorizeRequests()
                    .antMatchers("/",
                            "/products/**",
                            "/services/**",
                            "/prescriptions/**",
                            "/advice/**",
                            "/cart",
                            "/login",
                            "/logout",
                            "/user/register",
                            "/user/files/download/unverified",
                            "/backdoor",
                            "/swagger-resources/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/console/*",
                            "/favicon.ico",
                            "/js/**/*",
                            "/css/**/*",
                            "/fonts/**/*",
                            "/img/**/*",
                            "/webjars/**/*").permitAll()
                    // Only admin can access /admin portal
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().fullyAuthenticated();

            httpSecurity.authorizeRequests().and()
                    .exceptionHandling()
                    .accessDeniedPage("/access-denied");

            httpSecurity.authorizeRequests().and().formLogin()
                    .loginProcessingUrl("/j_spring_security_check")
                    .successHandler(CustomAuthenticationSuccessHandler())
                    .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .permitAll();

            httpSecurity.authorizeRequests().and().logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();

            httpSecurity.sessionManagement().maximumSessions(10)
                    .sessionRegistry(sessionRegistry())
                    .expiredUrl("/login?expire");

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
            return new CustomAuthenticationSuccessHandler();
        }

        @Bean
        public SessionRegistry sessionRegistry() {
            return new SessionRegistryImpl();
        }
        
        @Bean
        public HttpFirewall getHttpFirewall() {
            return new DefaultHttpFirewall();
        }

    }

    @Bean("WebSecurityConfigurationPasswordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
