package com.nta.teabreakorder.config;

import com.nta.teabreakorder.service.impl.FilesStorageServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userDirectory = Paths.get("")
                .toAbsolutePath()
                .toString();
        userDirectory = userDirectory.replace("\\","/");
        String myExternalFilePath = "file:///" + userDirectory +"/";
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations(myExternalFilePath);
    }
}