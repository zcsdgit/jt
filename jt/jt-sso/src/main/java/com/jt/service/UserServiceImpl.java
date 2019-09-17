package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * true  当前用户输入内容 已存在
	 * false 表示数据可以使用
	 * param:用户输入的数据
	 * type: 参数类型  1 username 2 phone 3 email
	 */
	@Override
	public boolean findUserCheck(String param, Integer type) {
		//1.定义查询字段
		String column=type==1?"username":type==2?"phone":"email";
		//校验是否有数据
		QueryWrapper<User> wrapper=new QueryWrapper<>();
		wrapper.eq(column, param);
		 
		return userMapper.selectCount(wrapper)==0?false:true;
	}
	
	
	
}
