package me.devld.tour.config;

import me.devld.tour.controller.ApiController;
import me.devld.tour.template.thymeleaf.MarkdownDialect;
import me.devld.tour.util.TextUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AppConfig.FileConfig fileConfig;

    public WebConfig(AppConfig appConfig) {
        this.fileConfig = appConfig.getFile();
    }

    @Bean
    public MarkdownDialect markdownDialect() {
        return new MarkdownDialect(fileConfig);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", c -> c.isAnnotationPresent(ApiController.class));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(fileConfig.getImageBasePath() + "/**")
                .addResourceLocations("file:///" + TextUtils.formatDirPath(new File(fileConfig.getPath(), fileConfig.getImageDir()).getAbsolutePath()));
    }

}
