package com.um;

import java.io.IOException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
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
    DataSource userDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/userdb");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
	}
    
    @Bean("authDataSource")
    DataSource authDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/authdb");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
	}
    
    @Bean("memCache")
    SessionCache sessionCache(Properties userManagementProperties) throws IOException {
    	return new MemcacheImpl(userManagementProperties.getProperty("ip_address"),
    			userManagementProperties.getProperty("port"));
    }
    
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins("http://localhost:3000", "http://local.finfree.com:3000")
					.allowedMethods("GET", "POST", "PUT", "DELETE")
					.allowedHeaders("*")
					.allowCredentials(true)
					.maxAge(3600);
			}
		};
	}
	
	/*
	 * @Bean("accountsSessionFactory") SessionFactory accountsSessionFactory() {
	 * SessionFactory sessionFactory = new Configuration()
	 * .configure("hibernate.cfg.xml") .buildSessionFactory(); return
	 * sessionFactory; }
	 */
    
}
