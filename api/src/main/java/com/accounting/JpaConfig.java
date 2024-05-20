package com.accounting;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EntityScan(basePackages = {"com.accounting.entity"})
@EnableJpaRepositories(basePackages = {"com.accounting.repository"})
@EnableTransactionManagement
public class JpaConfig {

    @Bean("entityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource accountsDataSource) {
    	LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    	bean.setPackagesToScan("com.accounting.entity");
    	bean.setDataSource(accountsDataSource);
    	bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//    	bean.setPersistenceProviderClass(AccountsPersistenceProvider.class);
    	return bean;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
