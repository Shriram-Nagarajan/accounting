package com.accounting.hibernate;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceProvider;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.ProviderUtil;

public class AccountsPersistenceProvider implements PersistenceProvider{

	@Override
	public EntityManagerFactory createEntityManagerFactory(String emName, Map map) {
		SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    	return sessionFactory;
	}

	@Override
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateSchema(PersistenceUnitInfo info, Map map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean generateSchema(String persistenceUnitName, Map map) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ProviderUtil getProviderUtil() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
