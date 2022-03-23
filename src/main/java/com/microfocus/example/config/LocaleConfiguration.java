package com.microfocus.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Configuration
public class LocaleConfiguration {

    public static final Locale DEFAULT_LOCALE = new Locale("en", "GB");

    @Bean
    public Locale getLocale() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
            final LocaleResolver localeResolver = RequestContextUtils
                    .getLocaleResolver(request);
            if (localeResolver != null) {
                Locale locale = localeResolver.resolveLocale(request);
                if (locale != null) {
                    return locale;
                }
            }
        }
        return DEFAULT_LOCALE;
    }
}
