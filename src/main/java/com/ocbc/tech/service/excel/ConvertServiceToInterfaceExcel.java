package com.ocbc.tech.service.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocbc.tech.data.DataCenter;
import com.ocbc.tech.entity.ServBean;
import com.ocbc.tech.entity.ServFieldBean;
import com.ocbc.tech.entity.SysNameBean;
import com.ocbc.tech.util.ExcelUtils;
import com.ocbc.tech.util.FieldUtils;
import com.ocbc.tech.util.FileUtils;

public class ConvertServiceToInterfaceExcel {

	private static Logger logger = LoggerFactory.getLogger(ConvertServiceToInterfaceExcel.class);

	// 服务场景模板文件路径
	public static final String SERVICE_INTERFACE_TEMPLETE_FILE = "templates" + File.separatorChar
			+ "接口定义_templete_V0.1.xlsx";
	private static final String DEFAULT_EXPORT_PATH = "C:\\Users\\ZHoward\\Desktop\\gov\\export";

	/**
	 * 转为接口定义文档
	 * 
	 * @param dataBean
	 *            接口文档数据
	 */
	public static void convertToInterfaceXlsx(ServBean dataBean) {

		OutputStream outputStream = null;
		try {
			// 1.加载模板文件
			Workbook wb = ExcelUtils.loadTempleteFile(SERVICE_INTERFACE_TEMPLETE_FILE);
			// 2.填写接口清单
			interfaceListSheetHandler(wb, dataBean);
			// 3.填写接口字段
			interfaceFieldSheetHandler(wb, dataBean);
			// 4.输出文件
			File exportFile = createExportFile(dataBean);
			outputStream = new FileOutputStream(exportFile);
			wb.write(outputStream);

			logger.info("导出文件[" + exportFile.getName() + "]成功！");

		} catch (IOException e) {
			logger.error("", e);
		} finally {
			// 关闭流
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				logger.error("关闭输出流失败！", e);
			}
		}

	}

	/**
	 * 填写接口字段标签页
	 * 
	 * @param wb
	 * @param dataBean
	 */
	private static void interfaceFieldSheetHandler(Workbook wb, ServBean dataBean) {
		/*
		 * 由于赞同提供的模板文件存在数个隐藏标签页，故【接口<code>字段】sheet位于最后一个sheet页;
		 * 由于赞同治理平台要求【接口<code>字段】中的code（接口码）必须与实际接口码一致，故需要重命名sheetName
		 */
		wb.setSheetName(wb.getNumberOfSheets() - 1, "接口<" + dataBean.getBriefBean().getServiceCode() + ">字段");
		Sheet sheet = wb.getSheetAt(wb.getNumberOfSheets() - 1);

		boolean isSoapMsg = "SOAP".equalsIgnoreCase(dataBean.getPrvdDetail().getMsgType());

		/* -------------- 处理输入字段 ------------- */
		// 请求报文字段列表
		List<ServFieldBean> inputFieldList = FieldUtils.selectBodyFields(dataBean.getPrvdDetail().getInputFieldList());
		// 在第四行，即【服务场景字段（输出）】上方插入多行
		ExcelUtils.copyRows(sheet, 3, inputFieldList.size() - 1);

		// 行号。由于包含2行标题：【服务场景字段（输入）】【明细标题】，故起始值为2
		int line = 2;
		for (ServFieldBean field : inputFieldList) {
			Row row = sheet.getRow(line);
			processInterfaceFieldData(row, field, isSoapMsg);
			line++;
		}

		/* -------------- 处理输出字段 ------------- */
		// 包含2行标题：【服务场景字段（输出）】【明细标题】
		line += 2;
		List<ServFieldBean> outputFieldList = FieldUtils
				.selectBodyFields(dataBean.getPrvdDetail().getOutputFieldList());
		// 插入多行，起始行为输入字段数量+4行标题
		ExcelUtils.copyRows(sheet, line, outputFieldList.size() - 1);
		for (ServFieldBean field : outputFieldList) {
			Row row = sheet.getRow(line);
			processInterfaceFieldData(row, field, isSoapMsg);
			line++;
		}

	}

	/**
	 * 处理接口字段数据
	 * 
	 * @param row
	 * @param field
	 * @param isSoapMsg
	 *            是否为SOAP类型报文
	 */
	private static void processInterfaceFieldData(Row row, ServFieldBean field, boolean isSoapMsg) {

		if ("struct".equalsIgnoreCase(field.getFieldType())) {
			fillStructField(row, field, isSoapMsg);
		} else {
			fillStandardField(row, field, isSoapMsg);
		}

	}

	/**
	 * 填充标准字段，类型为string number
	 * 
	 * @param row
	 * @param field
	 * @param isSoapMsg
	 */
	private static void fillStandardField(Row row, ServFieldBean field, boolean isSoapMsg) {
		// 字段名称（英文）
		row.getCell(0).setCellValue(field.getFieldName());
		// 字段名称（中文）
		String desc = field.getFieldDescCN();
		if (desc == null || "".equals(desc.trim())) {
			desc = field.getFieldDescEN();
		}
		row.getCell(1).setCellValue(desc);
		// 字段位置
		if (isSoapMsg) {
			row.getCell(2).setCellValue(field.getLocation());
		} else {
			row.getCell(2).setCellValue(field.getFieldName());
		}
		// 数据类型
		row.getCell(3).setCellValue(FieldUtils.getFieldType(field.getFieldType()));
		// 字段长度
		row.getCell(4).setCellValue(Optional.ofNullable(field.getFieldLength()).orElse(""));
		// 数据格式
		row.getCell(5).setCellValue(Optional.ofNullable(field.getFieldProcess()).orElse(""));
		// 是否必输
		row.getCell(6).setCellValue("Y".equals(field.getFieldRequired()) ? "是" : "否");

		if ("number".equalsIgnoreCase(field.getFieldType())) {
			row.getCell(7).setCellValue("2-左填充");// 填充方式
			row.getCell(8).setCellValue("0");// 填充字符
		} else {
			row.getCell(7).setCellValue("1-右填充");// 填充方式
			row.getCell(8).setCellValue("0x20");// 填充字符
		}
		// 是否敏感默认值：0-否
		row.getCell(9).setCellValue("否");
		// 枚举列表
		row.getCell(10).setCellValue(Optional.ofNullable(field.getFieldEnum()).orElse(""));
		// 字段常量
		row.getCell(11).setCellValue(Optional.ofNullable(field.getFieldDefaultValue()).orElse(""));
		// 循环次数
		row.getCell(12).setCellValue("");
		// 备注
		row.getCell(13).setCellValue(Optional.ofNullable(field.getFieldRemark()).orElse(""));
	}

	/**
	 * 填充结构体循环标签，类型 struct
	 * 
	 * @param row
	 * @param field
	 * @param isSoapMsg
	 */
	private static void fillStructField(Row row, ServFieldBean field, boolean isSoapMsg) {
		String name = field.getFieldName();
		name = name.substring(name.lastIndexOf("/") + 1, name.length());

		String remark = field.getFieldRemark();
		if ("start".equalsIgnoreCase(remark)) {
			name = "<" + name + ">";
		} else if ("end".equalsIgnoreCase(remark)) {
			name = "</" + name + ">";
		} else {
			throw new IllegalArgumentException("[循环标签(struct)]字段[" + field.getFieldName() + "]，缺少备注，如: start/end！");
		}

		row.getCell(0).setCellValue(name);// 字段名称（英文）
		row.getCell(1).setCellValue(field.getFieldName());// 字段名称（中文）
		// 字段位置
		if (isSoapMsg) {
			row.getCell(2).setCellValue(field.getLocation());
		} else {
			row.getCell(2).setCellValue(field.getFieldName());
		}

		row.getCell(3).setCellValue("循环标签");// 字段类型
		row.getCell(6).setCellValue("Y".equals(field.getFieldRequired()) ? "是" : "否");// 是否必输
		row.getCell(7).setCellValue("1-右填充");// 填充方式
		row.getCell(8).setCellValue("0x20");// 填充字符
		row.getCell(9).setCellValue("否");// 是否敏感默认值：0-否
		row.getCell(12).setCellValue(field.getFieldLength());// 循环次数
	}

	/**
	 * 填写接口清单
	 * 
	 * @param wb
	 * @param dataBean
	 */
	private static void interfaceListSheetHandler(Workbook wb, ServBean dataBean) {
		Sheet sheet = wb.getSheet("接口清单");
		// 获取到服务清单Sheet页中的第3行为其中的列赋值
		Row row = sheet.getRow(2);

		// 修改超链接地址
		CreationHelper createHelper = wb.getCreationHelper();
		XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link.setAddress("'接口<" + dataBean.getBriefBean().getServiceCode() + ">字段'!A1");
		row.getCell(0).setHyperlink(link);

		row.getCell(0).setCellValue(dataBean.getBriefBean().getServiceCode()); // 接口交易码
		row.getCell(1).setCellValue(dataBean.getBriefBean().getServiceName()); // 接口名称
		row.getCell(2).setCellValue(dataBean.getBriefBean().getServiceDesc()); // 交易接口功能概述

		row.getCell(4).setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))); // 最后更新日期
		row.getCell(8).setCellValue(dataBean.getPrvdDetail().getMsgType()); // 报文格式

	}

	/**
	 * 创建输出文件<br/>
	 * 命名格式：提供方系统EN_提供方系统CN_接口定义_接口码.xlsx<br/>
	 * 例如：SIBSINQ_核心查询系统_接口定义_BWC_1612.xlsx
	 * 
	 * @param dataBean
	 * @return
	 */
	private static File createExportFile(ServBean dataBean) {
		String shortName = dataBean.getBriefBean().getPrvdSysName();
		SysNameBean nameBean = DataCenter.getNameBean(shortName);
		if (nameBean == null) {
			throw new IllegalArgumentException("无法在【系统编号】标签页中找到【" + shortName + "】对应记录！");
		}
		String fileName = nameBean.getShortName() + "_" + nameBean.getCnName() + "_接口定义_"
				+ dataBean.getBriefBean().getServiceCode() + ".xlsx";
		return FileUtils.createNewFile(DEFAULT_EXPORT_PATH, fileName);
	}

}
