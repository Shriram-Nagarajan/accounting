package com.accounting;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "com.accounting.auth.repository" }, 
	entityManagerFactoryRef = "authDbEntityManagerFactory",
	transactionManagerRef = "authDbTransactionManager")
public class AuthJpaConfig {

}
