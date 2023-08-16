package com.klbstore.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.model.NguoiDung;

public class User implements HandlerInterceptor {

    private final NguoiDungDAO nguoiDungDAO;

    public User(NguoiDungDAO nguoiDungDAO) {
        this.nguoiDungDAO = nguoiDungDAO;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            NguoiDung nguoiDung = nguoiDungDAO.findByTenDangNhap(authentication.getName());
            if (nguoiDung != null) {
                modelAndView.addObject("userLogin", nguoiDung);
            }
        }
    }

}
