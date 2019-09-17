package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//vo:是服务器数据与页面交互的对象 一般需要转化为JSON对象
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUI_Tree {
	private Long id;//分类id号
	private String text;//分类名称
	private String state;//open 节点打开 closed节点关闭
}
