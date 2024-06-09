package com.common.session;

public interface SessionCache {
	
	public void store(String key, Object value, int expirationSeconds);
	
	public Object get(String key);
	
	public void delete(String key);

}
