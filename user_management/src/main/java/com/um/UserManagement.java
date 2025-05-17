package com.um;

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
import com.common.session.MemcacheImpl;
import com.common.session.SessionCache;

@SpringBootApplication(scanBasePackages = {"com.um","com.um.*"})
@PropertySource("classpath:*.properties")
public class UserManagement {

	public static void main(String[] args) {
		SpringApplication.run(UserManagement.class, args);
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
  
    @Bean("userDataSource")
    DataSource userDataSource(Environment env) {
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
    
    
    @Primary
    @Bean("accountsDataSource")
    DataSource accountsDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/accounts");
        dataSource.setUsername("root");
        dataSource.setPassword("root1234");
        return dataSource;
	}
    
    @Bean("memCache")
    SessionCache sessionCache(Properties userManagementProperties) throws IOException {
    	return new MemcacheImpl(userManagementProperties.getProperty("ip_address"),
    			userManagementProperties.getProperty("port"));
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

	@Bean
	public CommandLineRunner checkEnvironment(ApplicationContext ctx) {
		return args -> {
			Environment env = ctx.getEnvironment();

			// Check which properties files are loaded
			System.out.println("Active profiles: " + String.join(", ", env.getActiveProfiles()));

			// Check database properties
			System.out.println("Database URL: " + env.getProperty("spring.datasource.url"));
			System.out.println("Database username: " + env.getProperty("spring.datasource.username"));
			System.out.println("Hibernate dialect: " + env.getProperty("spring.jpa.properties.hibernate.dialect"));

			// If these print as null, it means the properties aren't being loaded correctly
		};
	}

}
