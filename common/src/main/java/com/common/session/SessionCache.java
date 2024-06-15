package com.common.session;

public interface SessionCache<T> {
	
	public void store(String key, T value, int expirationSeconds);
	
	public T get(String key);
	
	public void delete(String key);

}
