package com.mycompany.myapp.config;

import com.mycompany.myapp.aop.context.ContextAspect;
import com.mycompany.myapp.aop.logging.LoggingAspect;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    @Bean
    public ContextAspect contextAspect() {
        return new ContextAspect();
    }
}
