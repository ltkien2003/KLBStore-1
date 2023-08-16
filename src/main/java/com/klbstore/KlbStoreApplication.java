package com.klbstore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.klbstore.config.NhomSanPhamInterceptor;
import com.klbstore.dao.NhomSanPhamDAO;
@EnableScheduling
@SpringBootApplication
public class KlbStoreApplication implements WebMvcConfigurer{
    private final NhomSanPhamDAO nhomSanPhamDAO;

    public KlbStoreApplication(NhomSanPhamDAO nhomSanPhamDAO) {
        this.nhomSanPhamDAO = nhomSanPhamDAO;
    }
	public static void main(String[] args) {
		SpringApplication.run(KlbStoreApplication.class, args);
	}
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/404").setViewName("user/404");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NhomSanPhamInterceptor(nhomSanPhamDAO));
    }
    @Bean
    public ErrorViewResolver customErrorViewResolver() {
        return (HttpServletRequest request, HttpStatus status, Map<String, Object> model) -> {
            if (status == HttpStatus.NOT_FOUND) {
                return new ModelAndView("user/404");
            }
            return null;
        };
    }

}