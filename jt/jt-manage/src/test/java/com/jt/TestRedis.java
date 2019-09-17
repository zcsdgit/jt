package com.jt;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestRedis {
	/**
	 * 1.Spring整合redis入门案例
	 */
	@Test
	public void testRedis() {
		String host="192.168.10.128";
		int port=6379;
		Jedis jedis=new Jedis(host,port);
		jedis.set("CGB1903", "AI~AI~AI~");
		System.out.println(jedis.get("CGB1903"));
	}
	/**
	 * 	简化超时
	 */
	@Test
	public void testRedis2() {
		Jedis jedis=new Jedis("192.168.10.128",6379);
		jedis.setex("xx", 60, "中文");
		System.out.println(jedis.get("xx"));
	}
	/**
	 * 	锁机制用法
	 * 	实际用法：保证set数据时如果这个key已经存在不允许修改
	 * 	业务场景：
	 * 	小明：set("见面","8点")
	 * 	小张：set("见面","5点半")
	 */
	@Test
	public void testRedis3() {
		Jedis jedis=new Jedis("192.168.10.128",6379);
		//jedis.set("约", "八点");
		//jedis.set("约", "五点");//会覆盖前一个值，相当于更新
		jedis.setnx("约", "八点");//相当于锁,即只有第一次设置key才可以更新，或者要解锁才行，设置成功则返回1
		jedis.setnx("约", "五点");
		System.out.println(jedis.get("约"));
	}
	/**
	 * 	死锁？？
	 * 1.如果不解锁就会造成死锁一直堵塞,底层是lock，所以异常时不会释放锁
	 * 2.解锁就是删除key
	 * 	注意：如果在解锁时发生异常也会死锁
	 * 避免死锁：添加key的超时时间
	 * 	锁机制优化
	 */
	@Test
	public void testRedis4() {
		Jedis jedis=new Jedis("192.168.10.128",6379);
		jedis.set("约", "10点", "NX", "EX", 10);//Nx表示加锁，加锁成功返回ok
	}
	/**
	 * hash在工作中使用较少
	 */
	@Test
	public void testRedis5() {
		Jedis jedis=new Jedis("192.168.10.128",6379);
		jedis.hset("user", "id", "10");//为对象属性赋值
		jedis.hset("user", "name", "zhangsan");
		jedis.hset("user", "age", "23");
		System.out.println(jedis.hgetAll("user"));//获得hash全部的键值对
	}
	/**
	 * list集合
	 */
	@Test
	public void testRedis6() {
		Jedis jedis=new Jedis("192.168.10.128",6379);
		//因为redis集合中存入数据和获取数据都有两种方向，所以要想实现栈还是队列由程序员操控,----当存入数据的方向和获取数据的方向相反时数据结构为队列，相同为栈
		//1.当做栈
		//jedis.lpush("list","1","2","3");
//		System.out.println(jedis.lpop("list"));
		//jedis.rpush("list", "1","2","3");
//		System.out.println(jedis.rpop("list"));
		//2.当作队列,先进先出
		//jedis.lpush("list","1","2","3");
		//System.out.println(jedis.rpop("list"));
		//jedis.rpush("list","1","2","3");
		//System.out.println(jedis.lpop("list"));
	}
	/**
	 * 	事务
	 */
	@Test
	public void testRedis7() {
		Jedis jedis=new Jedis("192.168.10.128",6379);
//		Transaction tr=new Transaction(jedis.getClient());
		Transaction tr1 = jedis.multi();//开启事务
		try {
			tr1.set("a", "aa");
			tr1.set("b", "bb");
			//模拟错误
			int i=1/0;
			tr1.exec();//提交
			
		} catch (Exception e) {
			tr1.discard();//回滚
		}
	}
	/**
	 * 5.Springboot整合redis实际操作代码
	 * 业务需求:
	 * 	查询itemDesc数据,之后缓存处理.
	 * 步骤:
	 * 	1.先查询缓存中是否有itemDesc数据
	 * 		null	查询数据库  将数据保存到缓存中
	 * 		!null	获取数据直接返回
	 * 问题:
	 * 	  一般使用redis时都采用String类型操作.
	 * 	 但是从数据库获取的数据都是对象 Object
	 * 	 String~~~~json~~~~Object类型转化
	 */
			
}






