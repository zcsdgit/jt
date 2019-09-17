package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.utils.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Component	//将对象交给spring管理
@Aspect		//标识此类为切面类
public class RedisAspect {
	@Autowired(required = false)
	private JedisCluster jedis;
	@SuppressWarnings("unchecked")
	@Around("@annotation(cache_find)")
	public Object around(ProceedingJoinPoint pj,Cache_Find cache_find) {
		//1.获取key的值
		String key=getKey(pj,cache_find);
		//2.根据key查询缓存
		String result = jedis.get(key);
		Object data=null;
		try {
		if(StringUtils.isEmpty(result)) {
			//如果结果为null表示缓存中没有数据，需要查询数据库
			data=pj.proceed();
			//将数据转换为json串
			String json = ObjectMapperUtil.toJson(data);
			//判断用户是否设置超时时间
			if(cache_find.secondes()==0) 
				jedis.set(key, json);//表示永不失效
			else
				jedis.setex(key, cache_find.secondes(), json);
			System.out.println("第一次查询数据库");
		}else {
			//不为空则查询缓存
			Class targetClass=getClass(pj);
			//如果缓存中有数据就将json转为对象返回
			data=ObjectMapperUtil.toObject(result, targetClass);
			System.out.println("aop查询缓存");
			
		}
		
		}catch(Throwable e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return data;
	}
	//获取返回值类型
	private Class getClass(ProceedingJoinPoint pj) {
		MethodSignature signature=(MethodSignature) pj.getSignature();
		return signature.getReturnType();
	}
	//获取key
	private String getKey(ProceedingJoinPoint pj, Cache_Find cache_find) {
		//1.获取key的类型
		KEY_ENUM keyType = cache_find.keyType();
		//2.判断key的类型
		if(KEY_ENUM.EMPTY.equals(keyType)) {
			//表示使用自己的key
			return cache_find.key();
		}
		//不为EMPTY表示key需要拼接
		
		//表示用户的key需要拼接  key+"_"+第一个参数
//		String strArgs = 
//				String.valueOf(joinPoint.getArgs()[0]);//用此拼接必须写key否着会报错
//		String key =  keyType+"_"+strArgs;
		String methodName = pj.getSignature().getName();
		String strArgs=String.valueOf(pj.getArgs()[0]);
		String key=methodName+"::"+strArgs;
		
		return key;
	}
	
}
