package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.utils.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Reference(timeout = 3000,check = true)
	private DubboCartService cartService;
	
	@RequestMapping("/show")
	public String show(Model model) {
		 Long userId=UserThreadLocal.get().getId();
		 List<Cart> cartList=cartService.findCartListByUserId(userId);
		 model.addAttribute("cartList", cartList);
		return "cart";
	}
	/**
	 * 	restful风格接收参数时，如果参数和对象属性名一致可以用对象接收如:Cart cart
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId) {
		Long userId=UserThreadLocal.get().getId();
		Cart cart=new Cart();	//如果参数直接是cart就不用new了，所以当参数和对象属性一致时建议参数用对象接收
		cart.setItemId(itemId).setUserId(userId);
		cartService.deleteItemByUserIdAndItemId(cart);
		return "redirect:/cart/show.html";
	}
	/**
	 * 该方法利用页面的表单提交获取参数.
	 * 之后应该跳转购物车展现页面
	 * @param cart
	 * @return
	 */
	@RequestMapping("/add/{itemId}")
	public String addCart(Cart cart) {
		Long userId=UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.addCart(cart);
		return "redirect:/cart/show.html";
	}
	//http://www.jt.com/cart/update/num/562379/2
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCart(Cart cart) {
		Long userId=UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.updateCart(cart);
		return SysResult.success();
	}
}
