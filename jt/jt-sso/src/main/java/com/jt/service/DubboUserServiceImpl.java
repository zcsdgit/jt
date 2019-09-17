package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.utils.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

/**
 * 	生产者
 */
@Service(timeout = 3000)	//阿里的包
public class DubboUserServiceImpl implements DubboUserService {
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JedisCluster jedis;
	/**
	 * 注册
	 */
	@Override
	public void saveUser(User user) {
		//md5加密
		String md5psw = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setEmail(user.getPhone()).setPassword(md5psw).setCreated(new Date()).setUpdated(user.getCreated());//此处邮箱暂时使用电话号码充当
		userMapper.insert(user);
	}
	/**
	 * 1.用户信息校验 
	 * 		密码加密处理之后查询数据库
	 * 		
	 */
	@Override
	public String doLogin(User user) {
		String md5psw = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		
		user.setPassword(md5psw);
		QueryWrapper<User> queryWrapper=new QueryWrapper<>(user);//当用有参构造时，将对象中不为null的属性当作where条件
		User userdb = userMapper.selectOne(queryWrapper);//查询数据库获得user对象
		String token=null;
		if(userdb!=null) {//判断user对象是否为空,不为空则证明登录成功
			//将用户数据保存到redis中 生成key
			String tokenTemp="JT_TICKET"+System.currentTimeMillis()+user.getUsername();
			tokenTemp=DigestUtils.md5DigestAsHex(tokenTemp.getBytes());
			//生成value数据userjson
			//为了安全需要将数据进行脱敏处理
			userdb.setPassword("密码~");
			//将对象转化成json数据
			String json = ObjectMapperUtil.toJson(userdb);
			jedis.setex(tokenTemp, 1800, json);//过期时间
			token=tokenTemp;
		}
		return token;
	}
	
}
