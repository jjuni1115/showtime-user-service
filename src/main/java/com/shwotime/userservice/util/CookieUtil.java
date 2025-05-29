package com.shwotime.userservice.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public static void createCookie(HttpServletResponse response, String cookieName, String domain, String cookieValue, String path, Integer lifeTime){
        ResponseCookie cookie = ResponseCookie.from(cookieName, cookieValue)
                .domain(domain)
                .path(path)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 여기서 설정 가능
                .maxAge(lifeTime)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletResponse response,String CookieName){

        Cookie cookie = new Cookie(CookieName,"");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);


    }


}
