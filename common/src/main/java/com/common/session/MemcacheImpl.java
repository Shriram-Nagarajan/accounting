package com.common.session;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.common.Properties;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class MemcacheImpl implements SessionCache {

	private final MemcachedClient client;
	
	public MemcacheImpl(String ipAddress, String port) throws IOException {
        XMemcachedClientBuilder builder = new XMemcachedClientBuilder(
                AddrUtil.getAddresses(getAddress(ipAddress, port))
        );
		client = builder.build();
	}
	
	@Override
	public void store(String key, Object value, int expirationSeconds) {
		try {
			client.set(key, expirationSeconds, value);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			log.error("Exception occured while storing into memcache: ", e);
		}
	}

	@Override
	public Object get(String key) {
		try {
			return client.get(key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			log.error("Exception occured while fetching from memcache: ", e);
		} 
		return null;
	}

	@Override
	public void delete(String key) {
		try {
			client.delete(key);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			log.error("Exception occured while deleting from memcache: ", e);
		}
	}
	
	private String getAddress(String ipAddress, String port) {
		StringBuilder sb = new StringBuilder();
		return sb.append(ipAddress).append(":").append(port).toString();
	}

	private static final Logger log = LogManager.getLogger(MemcacheImpl.class);
}
