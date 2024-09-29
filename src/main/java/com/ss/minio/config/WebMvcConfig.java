package com.ss.minio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import java.util.concurrent.TimeUnit;

/**
 * web 配置类
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 解决 org.springframework.context.ApplicationContextException: Failed to start bean <br>
     * 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException
     * 发现如果继承了 WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。 需要重新指定静态资源
     *
     * @param registry ResourceHandlerRegistry
     * @see <a href="https://www.mobaijun.com/posts/3051425539.html"></a>
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html", "doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        /* swagger配置 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());
        super.addResourceHandlers(registry);
    }
}
