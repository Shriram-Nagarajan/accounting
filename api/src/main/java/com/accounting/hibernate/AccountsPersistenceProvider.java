package com.accounting.hibernate;

import java.util.Map;

import org.hibernate.cfg.Configuration;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceProvider;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.ProviderUtil;

public class AccountsPersistenceProvider implements PersistenceProvider{

	@Override
	public EntityManagerFactory createEntityManagerFactory(String emName, Map map) {
		return new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
	}

	@Override
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map map) {
		return null;
	}

	@Override
	public void generateSchema(PersistenceUnitInfo info, Map map) {
	}

	@Override
	public boolean generateSchema(String persistenceUnitName, Map map) {
		return false;
	}

	@Override
	public ProviderUtil getProviderUtil() {
		return null;
	}
	
}
