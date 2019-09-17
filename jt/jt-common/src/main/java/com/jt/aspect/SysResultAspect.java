package com.jt.aspect;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

//@ControllerAdvice	//针对controller层生效
@RestControllerAdvice	//相当于@ControllerAdvice+@ResponseBody
@Slf4j
public class SysResultAspect {
	/**
	 * 如果程序报错，则统一返回系统异常信息
	 * SysResult.fail()
	 */
	@ExceptionHandler(RuntimeException.class)//如果遇到指定的异常类型就执行下列方法
	//@ResponseBody
	public SysResult sysResultFail(Exception e) {
		e.printStackTrace();
		log.error(e.getMessage());
		return SysResult.fail(e);
	}
}
