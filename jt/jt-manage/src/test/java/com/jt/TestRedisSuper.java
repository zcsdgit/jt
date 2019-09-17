package com.jt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

public class TestRedisSuper {
	/**
	 * 测试redis分片机制
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
		String host="192.168.10.128";
		shards.add(new JedisShardInfo(host, 6379));
		shards.add(new JedisShardInfo(host, 6380));
		shards.add(new JedisShardInfo(host, 6381));
		ShardedJedis jedis=new ShardedJedis(shards);
		jedis.set("1903", "测试redis分片即扩容,三台当一台");
		System.out.println("获取数据"+jedis.get("1903"));
	}
	
	/**
	 * 测试redis哨兵机制
	 * 说明：链接哨兵，HOST和端口写的是哨兵的地址
	 */
	@Test
	public void testSentinel() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.10.128:26379");
		JedisSentinelPool pool=new JedisSentinelPool("mymaster",sentinels);
		Jedis jedis = pool.getResource();
		jedis.set("1903", "哨兵测试");
		System.out.println(jedis.get("1903"));
	}
	/**
	 * 	测试redis集群
	 */
	@Test
	public void testCluster() {
		Set<HostAndPort> nodes=new HashSet<>();
		nodes.add(new HostAndPort("192.168.10.128", 7000));
		nodes.add(new HostAndPort("192.168.10.128", 7001));
		nodes.add(new HostAndPort("192.168.10.128", 7002));
		nodes.add(new HostAndPort("192.168.10.128", 7003));
		nodes.add(new HostAndPort("192.168.10.128", 7004));
		nodes.add(new HostAndPort("192.168.10.128", 7005));
		JedisCluster jedisCluster=new JedisCluster(nodes);
		jedisCluster.set("CGB1903", "测试redis集群");
		System.out.println(jedisCluster.get("CGB1903"));
	}
}
