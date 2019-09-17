package com.jt.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**序列化、反序列化工具类*/
public class ObjectMapperUtil {
	//反序列化成对象
	public static <T>T toObject(String json,Class<T> clazz){
		T obj =null;
		try {
			obj= Inner.om.readValue(json, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	//序列化成json串
	public static String toJson(Object obj) {
		String json=null;
		try {
			json=Inner.om.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	//静态内部类实现单例
	private static class Inner{
		private static final ObjectMapper om=new ObjectMapper();
	}
}
