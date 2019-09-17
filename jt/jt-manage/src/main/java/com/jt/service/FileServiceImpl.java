package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.EasyUI_Image;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@PropertySource("classpath:/properties/dir.properties")
public class FileServiceImpl implements FileService {
	@Value("${localDirPath}")
	private String localDirPath;//定义本地磁盘路径
	@Value("${urlDirPath}")
	private String urlDirPath;//虚拟网络路径		
	/**
	 * 文件上传思路
	 * 问题：
	 * 	1.校验文件类型是否为图片  如何校验？校验后缀
	 * 	2.如何防止恶意文件上传？校验文件是否有宽和高
	 * 	3.众多图片如何保存  ？按照yyyy/MM/dd存储
	 * 	4.用户上传的图片名重复如何处理？UUID
	 */
	@Override
	public EasyUI_Image fileUpload(MultipartFile uploadFile) {
		EasyUI_Image easyUI_Image=new EasyUI_Image();
		
		//1.获取文件名称abc.jpg
		String fileName = uploadFile.getOriginalFilename();
		//将名称转为小写
		String newFileName = fileName.toLowerCase();
		//校验文件名称
		if(!newFileName.matches("^.+\\.(jpg|png|gif)$")) {
			easyUI_Image.setError(1);
			return easyUI_Image;
		}	
		//3.利用API读取用户提交数据
		try {
			BufferedImage bufferedImage=ImageIO.read(uploadFile.getInputStream());
			int height=bufferedImage.getHeight();
			int width = bufferedImage.getWidth();
			if(height==0||width==0) {//如果有一项为0表示不是图片
				easyUI_Image.setError(1);
				return easyUI_Image;
			}
			//封装图片数据
			easyUI_Image.setHeight(height).setWidth(width);
			
			//4.以时间格式创建文件夹D:/ws111903/fwqwj/yyyy/MM/dd
			String  dataPathDir=new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			//拼接真实路径
			String realDirPath=localDirPath+dataPathDir;
			
			File dirFile=new File(realDirPath);
			if(!dirFile.exists()) {
				dirFile.mkdirs();
			}
			
			log.info("接近成功");
			//6.采用UUID命名文件名称,替换中间的-
			String uuid=UUID.randomUUID().toString().replace("-", "");
			String fileType=fileName.substring(fileName.lastIndexOf("."));
			//拼接文件名
			String realName=uuid+fileType;
			
			//7.实现文件上传,存入指定的路径
			String realFilePath=realDirPath+"/"+realName;
			uploadFile.transferTo(new File(realFilePath));
			
			//8。编辑虚拟路径数据返回
			String realUrlPath=urlDirPath+dataPathDir+"/"+realName;
			easyUI_Image.setUrl(realUrlPath);
		} catch (IOException e) {
			e.printStackTrace();
			easyUI_Image.setError(1);//对象转化时异常
			return easyUI_Image;
		}
		return easyUI_Image;
	}

}
