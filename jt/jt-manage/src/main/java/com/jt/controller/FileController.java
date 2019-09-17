package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.EasyUI_Image;

@Controller
public class FileController {
	@Autowired
	private FileService fileService;
	
	/**
	 * 2.获取用户提交参数
	 * 3.响应合适的页面
	 * 业务要求：
	 * 将文件上传到D:/jt下---D:\ws111903\fwqwj   文件名：goodImage
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//获取图片名称
		String imageName = fileImage.getOriginalFilename();
		File file=new File("D:/ws111903/fwqwj/"+imageName);
		
		//实现文件上传API
				fileImage.transferTo(file);
				
		//重定向到file.jsp
				
		return "redirect:/file.jsp";
	}
	/**
	 * 实现文件上传
	 */
	@RequestMapping("/pic/upload")
	@ResponseBody
	public EasyUI_Image fileUpload(MultipartFile uploadFile) {
		
		return fileService.fileUpload(uploadFile);
	}
}
