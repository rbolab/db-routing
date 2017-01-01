package com.mycompany.myapp.config.hazelcast;

import com.mycompany.myapp.config.context.DataSourceContext;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;


public class AmethystKeyGenerator extends SimpleKeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return super.generate(target, method, params, DataSourceContext.getCurrentContext());
    }
}
