package com.accounting;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class EntityManagerConfig {

    private Map<String, Object> getJpaProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        return properties;
    }

	@Primary
	@Bean("entityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("accountsDataSource") DataSource accountsDataSource) {
    	LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    	bean.setPackagesToScan("com.accounting.entity");
    	bean.setDataSource(accountsDataSource);
    	bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    	bean.setJpaPropertyMap(getJpaProperties());
    	return bean;
    }
    
	@Primary
    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    

    @Bean("authDbEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean authDbEntityManagerFactory(@Qualifier("authDataSource") DataSource authDataSource) {
    	LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    	bean.setPackagesToScan("com.accounting.auth.entity");
    	bean.setDataSource(authDataSource);
    	bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    	bean.setJpaPropertyMap(getJpaProperties());
    	return bean;
    }
    
    @Bean("authDbTransactionManager")
    public PlatformTransactionManager authDbTransactionManager(@Qualifier("authDbEntityManagerFactory") EntityManagerFactory authDbEntityManagerFactory) {
        return new JpaTransactionManager(authDbEntityManagerFactory);
    }

}
