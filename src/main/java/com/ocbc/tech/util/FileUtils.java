package com.ocbc.tech.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 创建文件，如果文件夹不存在，则创建父路径
	 * 
	 * @param parentPath
	 * @param fileName
	 * @return
	 */
	public static File createNewFile(String parentPath, String fileName) {
		// 判断导出路径是否存在
		File dir = new File(parentPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 写入到新的excel
		return new File(parentPath, fileName);
	}
	
	/**
	 * 读取excel模板，并复制到新文件中供写入和下载
	 * 
	 * @param templeteFilePath
	 *            模板文件路径
	 * @param targetFolderPath
	 *            目标文件保存目录
	 * @param targetFileName
	 *            目标文件文件名
	 * @return
	 */
	public static File createFileByTemplete(String templeteFilePath, String targetFolderPath, String targetFileName) {
		return createFileByTemplete(new File(templeteFilePath), targetFolderPath, targetFileName);

	}

	/**
	 * 读取excel模板，并复制到新文件中供写入和下载
	 * 
	 * @param tempFile
	 *            模板文件
	 * @param targetFolderPath
	 *            目标文件保存目录
	 * @param targetFileName
	 *            目标文件文件名
	 * @return
	 */
	public static File createFileByTemplete(File tempFile, String targetFolderPath, String targetFileName) {
		if (!tempFile.exists()) {
			logger.error("模板文件[" + tempFile.getAbsolutePath() + "]不存在！");
			throw new IllegalArgumentException("模板文件[" + tempFile.getName() + "]不存在！");
		}
		// 判断导出路径是否存在
		File dir = new File(targetFolderPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 写入到新的excel
		File newFile = new File(targetFolderPath, targetFileName);
		try {
			newFile.createNewFile();
			// 复制模板到新文件
			copyFile(tempFile, newFile);
			
			logger.info("创建并复制新文件["+newFile.getName()+"]成功！");
		} catch (Exception e) {
			logger.error("复制文件失败！", e);
			throw new IllegalArgumentException("复制文件失败！ " + e.getMessage());
		}
		return newFile;
	}

	/**
	 * 复制文件
	 * 
	 * @param s
	 *            源文件
	 * @param t
	 *            复制到的新文件
	 * @throws IOException
	 */
	public static void copyFile(File s, File t) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(s), 1024);
			out = new BufferedOutputStream(new FileOutputStream(t), 1024);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}
	}
}
