package com.cxy.speedkill.config;

import com.cxy.speedkill.domain.SpeedKillUser;
import com.cxy.speedkill.service.SpeedKillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    SpeedKillUserService speedKillUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
      Class<?> clazz = methodParameter.getParameterType() ;
      return clazz == SpeedKillUser.class ;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = webRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = httpServletRequest.getParameter(SpeedKillUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(httpServletRequest, SpeedKillUserService.COOKIE_NAME_TOKEN);

        if(StringUtils.isEmpty(paramToken)&&StringUtils.isEmpty(cookieToken)){
            return null;
        }

        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return speedKillUserService.getToken(httpServletResponse,token);
    }

    private String getCookieValue(HttpServletRequest httpServletRequest, String cookieNameToken) {
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie:cookies) {
            if(cookie.getName().equals(cookieNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }

}
