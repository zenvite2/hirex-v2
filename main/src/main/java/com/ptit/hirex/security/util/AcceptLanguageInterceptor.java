package com.ptit.hirex.security.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;


@AllArgsConstructor
public class AcceptLanguageInterceptor implements HandlerInterceptor {
    private LocaleResolver resolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String acceptLanguageHeader = request.getHeader("X-Accept-Language");
        acceptLanguageHeader = !Util.isNullOrEmpty(acceptLanguageHeader) ? acceptLanguageHeader : "en";
        request.setAttribute("X-Accept-Language", acceptLanguageHeader);
        LocaleContextHolder.setLocale(Locale.forLanguageTag(acceptLanguageHeader));
        resolver.setLocale(request, response, Locale.forLanguageTag(acceptLanguageHeader));
        return true;
    }
}
