package com.jt.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Configuration//标识一个配置类
@PropertySource("classpath:/properties/redis.properties")
@Lazy		//懒加载
public class RedisConfig {
	/**
	 * 	回顾：
	 * 1.利用xml配置文件添加bean标签(远古时期)
	 * 2.配置类的形式
	 * 	配置：
	 * 	将jedis对象交给spring管理
	 * 	利用properties配置文件为属性动态赋值
	 * 	
	 */
	/*
	 * @Value("${redis.port}") private int port;
	 * 
	 * @Value("${redis.host}") private String ip;
	 * 
	 * @Bean public Jedis jedis() { return new Jedis(ip,port); }
	 */
	/*
	 *	// 分片机制
	 * @Value("${redis.nodes}") private String nodes;
	 * 
	 * @Bean public ShardedJedis shardedJedis() { List<JedisShardInfo> shards=new
	 * ArrayList<>(); String[] strNodes = nodes.split(",");//拆分成多个 ip和端口 for (String
	 * strNode : strNodes) { String[] node = strNode.split(":");//拆分ip、端口 String
	 * host=node[0]; int port=Integer.parseInt(node[1]); shards.add(new
	 * JedisShardInfo(host, port)); } return new ShardedJedis(shards); }
	 */
	/**
	 * 实现哨兵机制
	 */
	/*
	 * @Value("${redis.sentinel.masterName}") private String masterName;
	 * 
	 * @Value("${redis.sentinel.sentinels}") private String nodes;
	 * 
	 * @Bean(name="jedisSentinelPool")//该对象是单例
	 */
	/*
	 * public JedisSentinelPool getPool() { Set<String> sentinels = new HashSet<>();
	 * sentinels.add(nodes); JedisSentinelPool pool=new
	 * JedisSentinelPool(masterName,sentinels); return pool; }
	 */
	
	//@Qualifier该注解表示指定bean赋值  用在方法中
	/*
	 * @Bean
	 * 
	 * @Scope("prototype")//多例对象 public Jedis
	 * jedis(@Qualifier("jedisSentinelPool")JedisSentinelPool pool) {
	 * 
	 * Jedis jedis = pool.getResource(); return jedis; }
	 */
	@Value("${redis.nodes}")
	private String nodes;
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodeSets=getNodes();
		JedisCluster jedisCluster=new JedisCluster(nodeSets);
		return jedisCluster;
	}
	private Set<HostAndPort> getNodes() {
		Set<HostAndPort> nodeSets=new HashSet<>();
		String[] node = nodes.split(",");
		for (String n : node) {
			String[] nd = n.split(":");
			nodeSets.add(new HostAndPort(nd[0], Integer.parseInt(nd[1])));
		}
		return nodeSets;
	}
	
}
