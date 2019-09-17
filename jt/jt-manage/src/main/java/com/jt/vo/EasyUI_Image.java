package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUI_Image {//关于不需要序列化的问题，因为JSON格式本身就是字符串，转换json用的是set方法,多系统之间对象直接传递才需要序列化,比如前台系统和后台系统之间传数据，传对象时就需要序列化，传json不需要
	private Integer error=0;//表示用户上传文件时是否有错
	private String url;//表示图片的虚拟路径
	private Integer width;//图片宽度
	private Integer height;//图片高度
	
}
