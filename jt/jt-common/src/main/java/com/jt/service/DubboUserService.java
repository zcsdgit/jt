package com.jt.service;

import com.jt.pojo.User;

/**
 * 定义中立的第三方接口
 */
public interface DubboUserService {

	void saveUser(User user);

	String doLogin(User user);
	
}
