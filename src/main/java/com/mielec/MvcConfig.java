package com.mielec;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;

@Configuration
@EnableAutoConfiguration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("hello");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/403").setViewName("403");
        registry.addViewController("/project").setViewName("project");
        registry.addViewController("/user").setViewName("user");
        registry.addViewController("/userpanel").setViewName("userpanel");
        registry.addViewController("/showrecords").setViewName("showrecords");
        registry.addViewController("/addproject").setViewName("addproject");
        registry.addViewController("/adduser").setViewName("adduser");
        registry.addViewController("/adddepartment").setViewName("adddepartment");
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
