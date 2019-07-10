package com.ocbc.tech.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileController {

	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	@ResponseBody
	@RequestMapping("/upload")
	public Object upload(MultipartFile file) {
		String fileName = "";
		try {
			fileName = file.getOriginalFilename();

			log.info("开始上传文件【{}】...", fileName);
			// 放到服务器上运行时这里需要修改保存路径
			// TODO Auto-generated method stub
			String path = "D:/test";
			File dest = new File(path + "/" + fileName);
			file.transferTo(dest);
			// FileUtils.writeByteArrayToFile(new File("D:/" + fileName),
			// bytes);

		} catch (IOException e) {
			log.error("文件上传失败", e);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "文件上传失败");
			return new ResponseEntity<Object>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("文件【{}】上传成功...", fileName);
		// 同时将文件的下载路径返回给前端
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "文件上传成功");
		map.put("path", "");
		return map;
	}

	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		// 从该目录去获取待下载的文件
		String path = request.getParameter("path");
		String fileName = "student.xml";
		if ("1".equals(type)) {
			fileName = "接口定义.xlsx";
		}
		if ("2".equals(type)) {
			fileName = "服务场景.xlsx";
		}
		if ("3".equals(type)) {
			fileName = "RCD.zip";
		}
		log.info("下载文件类型为{}", type);

		String userAgent = request.getHeader("User-Agent");
		String downloadFileName = "";
		try {
			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				downloadFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			} else {
				// 非IE浏览器的处理：
				downloadFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (Exception e) {
			log.error("文件下载失败", e);
		}
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 放到服务器上运行时这里需要修改保存路径
			// TODO Auto-generated method stub
			bis = new BufferedInputStream(new FileInputStream(new File("D:/test/" + fileName)));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			log.error("文件下载失败", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					log.error("IO流关闭失败", e);
				}
			}
		}
	}

}
