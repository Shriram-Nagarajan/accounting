package com.accounting;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "com.accounting.repository" }, 
	entityManagerFactoryRef = "entityManagerFactory",
	transactionManagerRef = "transactionManager")
public class AccountingJpaConfig {

}
