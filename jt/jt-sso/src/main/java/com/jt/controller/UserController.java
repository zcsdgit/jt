package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	/**
	 * 经过跨域请求 返回系统数据 data:true(已存在)/false(不存在)
	 * http://sso.jt.com/user/check/asfasfasfasfasdafs/1?r=0.18628019862017364&callback=jsonp1563327478623&_=1563327481880
	 */
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedis;
	
	/**
	 * 
	 *	校验用户名、邮箱、电话
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject check(String callback,@PathVariable String param,@PathVariable Integer type) {
		JSONPObject jSONPObject =null;
		try {
		boolean flag=userService.findUserCheck(param,type);
		
		jSONPObject=new JSONPObject(callback,SysResult.success(flag));
		}catch(Exception e) {
			e.printStackTrace();
			jSONPObject=new JSONPObject(callback,SysResult.fail());
		}
		
		
		return jSONPObject;
	}
	//校验cookie,主要是处理网页头部退出、注册等显示问题
	@RequestMapping("/query/{token}")
	public JSONPObject findUserByToken(String callback,@PathVariable String token) {
		//根据密钥查询用户信息
		JSONPObject jSONPObject=null;
		String json = jedis.get(token);
		if(StringUtils.isEmpty(json)) {
			jSONPObject=new JSONPObject(callback, SysResult.fail());
		}else {
			//表示用户数据获取成功
			jSONPObject=new JSONPObject(callback, SysResult.success(json));
		}
		return jSONPObject;
	}
	
	
}
