package com.ocbc.tech.test;

import java.util.List;

import com.ocbc.tech.entity.ServBean;
import com.ocbc.tech.service.excel.ConvertServiceToInterfaceExcel;
import com.ocbc.tech.service.excel.ConvertServiceToSceneExcel;
import com.ocbc.tech.service.excel.ServiceExcelAnalyzer;

public class ExcelAnalyzeTest {

	public static String targetFolderPath = "C:\\Users\\ZHoward\\Desktop\\gov\\export";
	
	public static void main(String[] args) throws Exception {

		String filePath = "C:\\Users\\ZHoward\\Desktop\\gov\\Demo.xlsx";
		// 1.解析新版接口文档
		List<ServBean> serviceList = ServiceExcelAnalyzer.analyzeExcelFile(filePath);
		for(ServBean bean:serviceList) {
			// 2.导出服务场景定义文档
			ConvertServiceToSceneExcel.convertToSceneXlsx(bean);
			// 3.导出接口定义文档
			ConvertServiceToInterfaceExcel.convertToInterfaceXlsx(bean);
		}
	}

	

}
