package com.um;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class EntityManagerConfig {

    private final Environment env;

    public EntityManagerConfig(Environment env) {
        this.env = env;
    }

    private java.util.Properties getHibernateProperties() {
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        return properties;
    }

    @Bean("entityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("userDataSource") DataSource userDataSource) {
    	LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    	bean.setPackagesToScan("com.um.entity");
    	bean.setDataSource(userDataSource);
    	bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    	bean.setJpaProperties(getHibernateProperties());
    	return bean;
    }
    
    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    

    @Bean("authDbEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean authDbEntityManagerFactory(@Qualifier("authDataSource") DataSource authDataSource) {
    	LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    	bean.setPackagesToScan("com.um.auth.entity");
    	bean.setDataSource(authDataSource);
    	bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    	bean.setJpaProperties(getHibernateProperties());
    	return bean;
    }
    
    @Bean("authDbTransactionManager")
    public PlatformTransactionManager authDbTransactionManager(@Qualifier("authDbEntityManagerFactory") EntityManagerFactory authDbEntityManagerFactory) {
        return new JpaTransactionManager(authDbEntityManagerFactory);
    }


	@Primary
	@Bean("accountingEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean accountingEntityManagerFactory(@Qualifier("accountsDataSource") DataSource accountsDataSource) {
    	LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    	bean.setPackagesToScan("com.um.accounting.entity");
    	bean.setDataSource(accountsDataSource);
    	bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    	bean.setJpaProperties(getHibernateProperties());
    	return bean;
    }
    
	@Primary
    @Bean("accountingTransactionManager")
    public PlatformTransactionManager accountingTransactionManager(@Qualifier("accountingEntityManagerFactory") EntityManagerFactory accountingEntityManagerFactory) {
        return new JpaTransactionManager(accountingEntityManagerFactory);
    }
}
