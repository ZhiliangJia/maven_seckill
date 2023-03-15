package xyz.garbage.maven_seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import xyz.garbage.maven_seckill.config.param.UserArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Autowired
    UserArgumentResolver userArgumentResolver;

    public final static String queueName = "seckill_queue";

    /**
     * 自定义的参数解析器
     *
     * @param argumentResolvers
     */
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }

    /**
     * 用于释放资源
     *
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    /**
     * 消息队列
     */
    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }
}
