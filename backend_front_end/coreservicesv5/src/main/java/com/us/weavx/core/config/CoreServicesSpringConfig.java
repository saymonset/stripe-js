package com.us.weavx.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@EnableWebMvc
@Configuration
@ComponentScan({"com.us.weavx"})
public class CoreServicesSpringConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean(name = "dataSource")
    public DataSource getDataSource() throws NamingException {
        InitialContext ctx = new InitialContext();
        Context envContext = (Context) ctx.lookup("java:/comp/env");
        return (DataSource) envContext.lookup("jdbc/servicesDBDSV3");
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate() throws NamingException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return jdbcTemplate;
    }

}
