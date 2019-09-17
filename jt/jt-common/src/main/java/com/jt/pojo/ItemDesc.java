package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@TableName("tb_item_desc")
public class ItemDesc extends BasePojo {
	//因为商品描述表的id和商品表主键id一致所以不能设置自增否则就没关系了,但是还是要标识是主键，只不过不用写自增
	@TableId
	private Long itemId;
	private String itemDesc;
}
