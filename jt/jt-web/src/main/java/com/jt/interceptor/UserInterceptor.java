package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.utils.ObjectMapperUtil;
import com.jt.utils.UserThreadLocal;

import redis.clients.jedis.JedisCluster;
/**
 * 
 *	因为HandlerInterceptor接口里的方法由default修饰，所以不需要立即实例化，用哪个方法就重写哪个
 *	spring 4需要重写三个方法，srping5每个方法都有默认值
 */
@Component
public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedis;
	/**
	 * boolean: 
	 * 		true: 表示放行
	 * 		false:表示拦截  一般配合重定向使用
	 * 
	 * 业务实现步骤
	 * 	1.获取用户Cookie中的token信息.
	 * 	2.校验数据是否有效
	 * 	3.校验redis中是否有数据
	 * 	如果上述操作正确无误.返回true
	 * 	否则return false 重定向到登录页面
	 * 
	 * preHandle:在执行业务方法之前拦截，即controller前
	 * postHandle:在执行完业务方法之后拦截，即mapper返回数据到controller后controller将数据返回之前
	 * afterCompletion:在视图渲染完之后即将返回页面给客户端前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.获取cookie数据
		Cookie[] cookies = request.getCookies();
		String token=null;
		if(cookies.length>0) {
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					//获取指定数据的值
					token = cookie.getValue();
					break;
				}
			}
		}
		//2.校验token的有效性
		if(!StringUtils.isEmpty(token)) {
			//2.1校验redis中是否有数据
			String json = jedis.get(token);
			if(!StringUtils.isEmpty(json)) {
				User user = ObjectMapperUtil.toObject(json, User.class);
				//传递参数给controller，可以利用request域传递,此处利用ThreadLocal传递
				//request.setAttribute("JT_USER",user);
				//利用threadLocal实现数据共享
				UserThreadLocal.set(user);
				return true;
			}
		}
		
		//必须重定向
				response.sendRedirect("/user/login.html");
				return false;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserThreadLocal.remove();
	}
}
