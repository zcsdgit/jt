package com.jt.pojo;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;
@TableName("tb_order_item")
@Data
@Accessors(chain=true)
public class OrderItem extends BasePojo{
	
	@TableId
    private String itemId;
	
	@TableId	
    private String orderId;

    private Integer num;

    private String title;

    private Long price;//单价

    private Long totalFee;//总金额

    private String picPath;//图片地址
}