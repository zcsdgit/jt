package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.utils.HttpClientService;
import com.jt.utils.ObjectMapperUtil;
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private HttpClientService httpClient;
	
	@Override
	public Item findItemById(Long itemId) {
		String url="http://manage.jt.com/web/item/findItemById/"+itemId;
		String json = httpClient.doGet(url);
		return ObjectMapperUtil.toObject(json, Item.class);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url="http://manage.jt.com/web/item/findItemDescById/"+itemId;
		String json = httpClient.doGet(url);
		return ObjectMapperUtil.toObject(json, ItemDesc.class);
	}

}
