package ${package}.redis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import ${package}.AppTest;

public class JedisTest extends AppTest{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private JedisPool jedisPool;
	
	@Test
	public void test() {
		char[] chars = "abcdefghigklmnopqrstuvwxyz".toCharArray();
		Map<String, String> keys = new HashMap<String, String>(1000);
		
		Jedis jedis = null;
		
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			logger.error("jedis error!", e);
		} finally {
			jedis.close();
		}
		
		for (int i = 1000; i > 0; i--) {
			String value = RandomStringUtils.random(100, chars);
			String key = "f.test."+RandomStringUtils.random(10, chars);
			
			jedis.set(key, value);
			keys.put(key, value);
		}
		
		String[] strings = keys.keySet().toArray(new String[0]);
		
		for (int i = 0; i < strings.length; i++) {
			Assert.assertEquals(keys.get(strings[i]), jedis.get(strings[i]));
			
			jedis.del(strings[i]);
			
		}
	}

}
