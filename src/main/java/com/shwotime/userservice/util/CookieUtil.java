package com.shwotime.userservice.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public static void createCookie(HttpServletResponse response, String cookieName, String domain, String cookieValue, String path, Integer lifeTime){
        Cookie cookie = new Cookie(cookieName,cookieValue);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        //cookie.setAttribute("SameSite","None");

        cookie.setMaxAge(lifeTime);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response,String CookieName){

        Cookie cookie = new Cookie(CookieName,"");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);


    }


}
