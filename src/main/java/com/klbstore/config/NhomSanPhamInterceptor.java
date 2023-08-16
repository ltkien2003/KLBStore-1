package com.klbstore.config;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.klbstore.dao.NhomSanPhamDAO;

public class NhomSanPhamInterceptor implements HandlerInterceptor {

    private final NhomSanPhamDAO nhomSanPhamDAO;

    public NhomSanPhamInterceptor(NhomSanPhamDAO nhomSanPhamDAO) {
        this.nhomSanPhamDAO = nhomSanPhamDAO;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("nhomSanPhamList", nhomSanPhamDAO.findAll());
            
        }
    }
}

