package com.common.poc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class XMemcached {
	public static void main(String[] args) {

        // Builder for creating MemcachedClient
        XMemcachedClientBuilder builder = new XMemcachedClientBuilder(
                AddrUtil.getAddresses("127.0.0.1:11211")
        );

        MemcachedClient client = null;

        try {
            // Build the client
            client = builder.build();

            // Store a value (set key "foo" to value "bar" with expiration of 900 seconds)
            client.set("foo", 900, "bar");

            // Retrieve the value from Memcached
            String value = client.get("foo");
            System.out.println("Value for 'foo': " + value);

            // Delete the key-value pair
            client.delete("foo");

            // Try to retrieve the value again (should be null)
            value = client.get("foo");
            System.out.println("Value for 'foo' after deletion: " + value);

        } catch (IOException | TimeoutException | InterruptedException | MemcachedException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    // Shutdown the client
                    client.shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}
