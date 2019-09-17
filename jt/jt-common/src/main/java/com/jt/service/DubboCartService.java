package com.jt.service;

import java.util.List;

import com.jt.pojo.Cart;

public interface DubboCartService {

	List<Cart> findCartListByUserId(Long userId);

	void deleteItemByUserIdAndItemId(Cart cart);

	void addCart(Cart cart);

	void updateCart(Cart cart);

}
