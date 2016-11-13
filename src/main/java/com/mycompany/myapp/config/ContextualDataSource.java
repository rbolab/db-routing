package com.mycompany.myapp.config;

import com.mycompany.myapp.config.context.DataSourceContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ContextualDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContext.getCurrentContext();
    }
}
