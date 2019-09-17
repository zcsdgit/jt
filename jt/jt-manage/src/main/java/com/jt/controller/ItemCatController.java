package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUI_Tree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	/**
	 * 业务
	 * 参数：itemCatId：val
	 * 返回值类型：返回具体分类名称
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		return itemCatService.findItemCatNameById(itemCatId);
	}
	/**
	 * 实现商品分类的树形结构查询
	 */
	@RequestMapping("/list")
	@Cache_Find(key="ITEM_CAT",keyType=KEY_ENUM.AUTO)
	public List<EasyUI_Tree> findItemCatByParentId(@RequestParam(name="id",defaultValue = "0") Long parentId){//@RequestParam注解将请求参数绑定到你控制器的方法参数上
		//1.定义parentId		
		//return itemCatService.findItemCatByCache(parentId);//使用注解缓存后就不需要重复使用这个缓存了
		return itemCatService.findItemCatByParentId(parentId);
	}
}
