package com.jarmak.fargartest;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TomcatConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(FargartestApplication.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatWebServer(tomcat);
            }

            @Override
            protected void postProcessContext(Context context) {
                logger.info("Tomcat config runs");

                // context
                ContextResource resource = new ContextResource();
                resource.setName("jdbc/CasperDataSource");
                resource.setType(DataSource.class.getName());
                resource.setProperty("driverClassName", "oracle.jdbc.OracleDriver");
                resource.setProperty("url", url);
                resource.setProperty("username", username);
                resource.setProperty("password", password);
//                resource.setProperty("jndi-name","java:comp/env/jdbc/CasperDataSource");
//                resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
                context.getNamingResources()
                        .addResource(resource);
            }
        };
    }

}
