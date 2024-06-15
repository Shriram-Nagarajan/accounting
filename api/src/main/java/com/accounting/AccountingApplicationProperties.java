package com.accounting;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.accounting.auth.entity.PropertyEntity;
import com.accounting.auth.repository.PropertiesRepository;
import com.common.Properties;

@Service("accountingAppProperties")
public class AccountingApplicationProperties implements Properties {

	private static ConcurrentMap<String, String> propMap;
	private PropertiesRepository propertiesRepository;
	
	public AccountingApplicationProperties(PropertiesRepository propertiesRepository) {
		this.propertiesRepository = propertiesRepository;
		loadProperties(true);
	}
	
	@Override
	public String getProperty(String key) {
		return propMap.get(key);
	}
	
	private void loadProperties(boolean refresh) {
		if(refresh || (propMap == null || propMap.isEmpty())) {
			List<PropertyEntity> propertiesList = propertiesRepository.findAll();
			if(propertiesList != null && !propertiesList.isEmpty()) {
				propMap = propertiesList.stream()
					.collect(Collectors.toConcurrentMap(
							prop -> prop.getPropKey(), prop -> prop.getPropValue()));
			}
		}
	}
	
	public void refreshProperties() {
		loadProperties(true);
	}

}
