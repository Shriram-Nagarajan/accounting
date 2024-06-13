package com.common.auth;

@SuppressWarnings("unchecked")
public class UserThreadLocal<T> {
    
	private static class Holder<U> {
		private ThreadLocal<U> userThreadLocal = new ThreadLocal<U>(); 
	}

    private static final Holder<Object> holder = new Holder<>();

	public void put(T user) {
        holder.userThreadLocal.set(user);
	}
	
	public T get() {
        return (T) holder.userThreadLocal.get();
	}
	
	public void remove() {
        holder.userThreadLocal.remove();
    }	
}
