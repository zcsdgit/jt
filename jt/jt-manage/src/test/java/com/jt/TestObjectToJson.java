package com.jt;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class TestObjectToJson {
	//构建jackson中的对象转换器
	private ObjectMapper om=new ObjectMapper();
	//将对象转换为json串
	public void TestJson() {
		//创建商品描述对象，并赋值
		ItemDesc itemDesc=new ItemDesc();
		itemDesc.setItemId(100L).setItemDesc("商品描述").setCreated(new Date()).setUpdated(new Date());
		//将对象转换为json串----即序列化
		try {
			String json = om.writeValueAsString(itemDesc);
			System.out.println(json);
			//-------------------------------------------
			//反序列化
			//将串转换为json格式的对象(反序列化)
			ItemDesc itemDesc2=om.readValue(json, ItemDesc.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
