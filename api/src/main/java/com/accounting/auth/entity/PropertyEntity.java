package com.accounting.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "properties")
public class PropertyEntity {
	
	@Id
	@Column(name = "prop_key")
	private String propKey;
	
	@Column(name = "prop_value")
	private String propValue;
	
	public String getPropKey() {
		return propKey;
	}
	public void setPropKey(String propKey) {
		this.propKey = propKey;
	}
	public String getPropValue() {
		return propValue;
	}
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	
}
