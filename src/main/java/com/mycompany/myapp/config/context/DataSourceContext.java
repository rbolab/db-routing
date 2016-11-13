package com.mycompany.myapp.config.context;

public class DataSourceContext {
    private static ThreadLocal<Object> currentContext = new ThreadLocal<>();

    public static void setCurrentContext(Object tenant) {
        currentContext.set(tenant);
    }

    public static Object getCurrentContext() {
        return currentContext.get();
    }

    public static void clearCurrentContext() {
        currentContext.remove();
    }
}
