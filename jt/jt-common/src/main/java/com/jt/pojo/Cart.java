package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@TableName("tb_cart")
public class Cart extends BasePojo{
	@TableId(type = IdType.AUTO)
	private Long id;			//主键id
	private Long userId;		//用户id
	private Long itemId;		//商品id
	private String itemTitle;	//商品标题
	private String itemImage;	//商品主图
	private Long itemPrice;		//商品价格
	private Integer num;		//购买数量
	
}
