package com.github.dobrosi.test.xa.configuration;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "AppDataSourceConfiguration",
        basePackages = {"com.github.dobrosi.test.xa.repository"}
)
public class DataSourceConfiguration {

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    public Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("javax.persistence.transactionType", "JTA");
        return jpaProperties;
    }

    @Bean("AppDataSourceConfiguration")
    public LocalContainerEntityManagerFactoryBean getPostgresEntityManager(DataSource dataSource) {
        var vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.github.dobrosi.test.xa.model");
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(jpaProperties());
        return em;
    }

    @Bean
    public DataSource dataSource() {
        var pgXaDataSource = new PGXADataSource();
        pgXaDataSource.setUrl(url);
        pgXaDataSource.setUser(username);
        pgXaDataSource.setPassword(password);
        var xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(pgXaDataSource);
        xaDataSource.setUniqueResourceName("xa_app");
        xaDataSource.setMaxPoolSize(10);
        return xaDataSource;
    }
}
