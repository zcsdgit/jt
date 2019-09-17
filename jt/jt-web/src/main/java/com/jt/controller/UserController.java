package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;
@Controller
@RequestMapping("/user")
public class UserController {
	@Reference(timeout = 3000,check = true,loadbalance = "roundRobin")//check=true必须先启动生产者才能启动消费者
	private DubboUserService dubboUserService;
	@Autowired
	private JedisCluster jedis;
	
	/**通用页面跳转*/
	@RequestMapping("/{moduleNmae}")
	public String xx(@PathVariable String moduleNmae) {
		return moduleNmae;
	}
	
	/**注册*/
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult doSave(User user) {
		//利用dubbo rpc协议而安城远程过程调用
		dubboUserService.saveUser(user);
		return SysResult.success();
	}
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletResponse response) {
		String token=dubboUserService.doLogin(user);
		//校验远程服务器返回数据是否为null
		if(StringUtils.isEmpty(token))
			return SysResult.fail();
		//将token数据写入cookie
		Cookie cookie=new Cookie("JT_TICKET",token);
		//存活时间 -1表示会话关闭删除cookie 0表示删除cookie >0表示存活时间
		cookie.setMaxAge(1800);
		//cookie作用范围
		cookie.setPath("/");
		//cookie共享
		cookie.setDomain("jt.com");
		response.addCookie(cookie);
			return SysResult.success();
	}
	@RequestMapping("/logout")
	public String logout(HttpServletRequest req,HttpServletResponse rep) {
		//获取cookie
		Cookie[] cookies = req.getCookies();
		
		String token=null;
		if(cookies.length>0) {
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					token=cookie.getValue();
					break;
				}
			}
		}
		if(!StringUtils.isEmpty(token)) {
			//删除redis中的key
			jedis.del(token);
			//删除客户端的cookie，注意想要删除cookie需要创建一个一摸一样的cookie并设置存活时间为0才可以
			Cookie cookie=new Cookie("JT_TICKET", "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			cookie.setDomain("jt.com");
			rep.addCookie(cookie);
		}
		
		return "redirect:/";
	}
}
