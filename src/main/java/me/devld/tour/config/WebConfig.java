package me.devld.tour.config;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.CookieFilter;
import me.devld.tour.template.thymeleaf.PaginationDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", c -> c.isAnnotationPresent(ApiController.class));
    }

    @Bean
    public FilterRegistrationBean cookieFilter() {
        FilterRegistrationBean<CookieFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new CookieFilter());
        bean.addUrlPatterns("/*");
        bean.setName("cookieFilter");
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public PaginationDialect paginationDialect() {
        return new PaginationDialect();
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}
