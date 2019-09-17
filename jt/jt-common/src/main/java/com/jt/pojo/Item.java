package com.jt.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;
@Accessors(chain=true)
@TableName("tb_item")
@JsonIgnoreProperties(ignoreUnknown = true)//如果get/set方法不全，添加该注解忽略转化
@Data
public class Item extends BasePojo{
	@TableId(type=IdType.AUTO)
	private Long id;			//主键id
	private String title;		//标题
	private String sellPoint;	//卖点信息
	private Long price;			//商品价格	1.dubbo有精度问题 0.88888..+0.1111112=0.9999.... //2.速度  int > long > dubbo 将商品扩大100倍保存
	private Integer num;		//商品数量
	private String barcode;		//二维码
	private String image;		//图片
	private Long cid;			//商品分类信息
	private Integer status;		//商品状态
	
	public String[] getImages() {
		return image.split(",");
	}
		 
}
