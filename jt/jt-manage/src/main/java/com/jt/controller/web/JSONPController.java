package com.jt.controller.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemCat;
import com.jt.utils.ObjectMapperUtil;

@RestController	//要求返回json数据

public class JSONPController {
	/**
	 * jsonp返回值的结果必须经过特殊格式的封装
	 * 调用者：：回调函数获取
	 * 数据返回：封装数据
	 */
	/*
	 * @RequestMapping("/web/testJSONP") public String testJSONP(String callback) {
	 * ItemCat item=new ItemCat(); item.setId(1000L).setName("跨域"); String json =
	 * ObjectMapperUtil.toJson(item); return callback+"("+json+")"; }
	 */
	@RequestMapping("/web/testJSONP")
	public JSONPObject testJSONP2(String callback) {
		ItemCat itemCat = new ItemCat();
		itemCat.setId(2000L).setName("JSONP简化写法!!!");
		JSONPObject object = new JSONPObject(callback, itemCat);
		return object;

	}
}
