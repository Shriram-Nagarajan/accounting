package com.um;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "com.um.accounting.repository" }, 
	entityManagerFactoryRef = "accountingEntityManagerFactory",
	transactionManagerRef = "accountingTransactionManager")
public class AccountingJpaConfig {

}
