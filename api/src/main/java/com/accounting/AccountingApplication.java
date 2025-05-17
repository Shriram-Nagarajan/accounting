package com.accounting;

import java.io.IOException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.common.Properties;
import com.common.model.User;
import com.common.session.MemcacheImpl;
import com.common.session.SessionCache;

@SpringBootApplication(scanBasePackages = {"com.accounting","com.accounting.*"})
@PropertySource("classpath:*.properties")
public class AccountingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountingApplication.class, args);
	}

    @Bean
    CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			System.out.println("Application Name: "+ ctx.getApplicationName());
			
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}
  
    @Primary
    @Bean("accountsDataSource")
    DataSource accountsDataSource(Environment env) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
	}
    
    @Bean("authDataSource")
    DataSource authDataSource(Environment env) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("authDatasource.url"));
        dataSource.setUsername(env.getProperty("authDatasource.username"));
        dataSource.setPassword(env.getProperty("authDatasource.password"));
        return dataSource;
	}
    
    
    @Bean("memCache")
    SessionCache<?> sessionCache(Properties accountingAppProperties) throws IOException {
    	return new MemcacheImpl<User>(accountingAppProperties.getProperty("ip_address"),
    			accountingAppProperties.getProperty("port"));
    }
    
	@Bean
	public WebMvcConfigurer corsConfigurer(Environment env) {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				
				String allowedOriginCsv = env.getProperty("cors.allowed.origins");
				String allowedOrigins[] = allowedOriginCsv.split(",");
				
				registry.addMapping("/**")
					.allowedOrigins(allowedOrigins)
					.allowedMethods("GET", "POST", "PUT", "DELETE")
					.allowedHeaders("*")
					.allowCredentials(true)
					.maxAge(3600);
			}
		};
	}
	
}
