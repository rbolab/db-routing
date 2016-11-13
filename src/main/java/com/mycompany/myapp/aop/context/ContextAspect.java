package com.mycompany.myapp.aop.context;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.config.ContextualDataSource;
import com.mycompany.myapp.config.context.DataSourceContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Aspect
public class ContextAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private Environment env;

    @Pointcut("within(com.mycompany.myapp.web.rest.AccountResource)")
    public void contextPointcut() {
    }

    @Pointcut("within(com.mycompany.myapp.repository.OrderRepository)")
    public void orderPointcut() {
    }

    @Around("contextPointcut()")
    public Object contextAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String currentContext = (String)DataSourceContext.getCurrentContext();
        DataSourceContext.setCurrentContext("online");
        try {
            Object result = joinPoint.proceed();
            DataSourceContext.setCurrentContext(currentContext);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }

    @Around("orderPointcut()")
    public Object orderAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String currentContext = (String)DataSourceContext.getCurrentContext();
        DataSourceContext.setCurrentContext("draft");
        try {
            Object result = joinPoint.proceed();
            DataSourceContext.setCurrentContext(currentContext);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }


}
