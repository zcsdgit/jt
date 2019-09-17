package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@Controller
@RequestMapping("/items")
public class ItemController {
	@Autowired ItemService itemService;
	/**
	 * 编辑jt-web前台服务器.
	 * 从后台manage中获取数据,之后页面展现.
	 */
	@RequestMapping("/{itemId}")
	public String findItemById(@PathVariable Long itemId,Model model) {
		Item item=itemService.findItemById(itemId);
		ItemDesc itemDesc=itemService.findItemDescById(itemId);
		model.addAttribute("itemDesc", itemDesc);
		model.addAttribute("item", item);
		return "item";//要跳转的页面
	}
	
}
