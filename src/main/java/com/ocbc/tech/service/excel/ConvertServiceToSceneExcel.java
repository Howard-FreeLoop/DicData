package com.ocbc.tech.service.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocbc.tech.data.DataCenter;
import com.ocbc.tech.entity.ServBean;
import com.ocbc.tech.entity.ServDetailListBean;
import com.ocbc.tech.entity.ServFieldBean;
import com.ocbc.tech.entity.SysNameBean;
import com.ocbc.tech.util.ExcelUtils;
import com.ocbc.tech.util.FieldUtils;
import com.ocbc.tech.util.FileUtils;

public class ConvertServiceToSceneExcel {

	private static Logger logger = LoggerFactory.getLogger(ConvertServiceToSceneExcel.class);

	// 服务场景模板文件路径
	public static final String SERVICE_SCENE_TEMPLETE_FILE = "templates" + File.separatorChar
			+ "服务场景定义_templete_V0.2.xlsx";
	private static final String DEFAULT_EXPORT_PATH = "C:\\Users\\ZHoward\\Desktop\\gov\\export";

	/**
	 * 转换为服务场景文档
	 * 
	 * @param dataBean
	 *            接口文档数据
	 * @throws IOException
	 */
	public static void convertToSceneXlsx(ServBean dataBean) {

		OutputStream outputStream = null;
		try {
			// 1.加载模板文件
			Workbook wb = ExcelUtils.loadTempleteFile(SERVICE_SCENE_TEMPLETE_FILE);
			// 2.填写服务清单
			serviceListSheetHandler(wb, dataBean);
			// 3.填写服务场景清单
			sceneListSheetHandler(wb, dataBean);
			// 4.填写场景字段
			sceneFieldSheetHandler(wb, dataBean);
			// 5.输出文件
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
	 * 创建输出文件<br/>
	 * 命名格式：提供方系统EN_提供方系统CN_服务场景定义_服务码.xlsx<br/>
	 * 例如：SIBSINQ_核心查询系统_服务场景定义_BWC_RetrieveCIFDetail_I.xlsx
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
		String fileName = nameBean.getShortName() + "_" + nameBean.getCnName() + "_服务场景定义_"
				+ dataBean.getBriefBean().getServiceId() + ".xlsx";
		return FileUtils.createNewFile(DEFAULT_EXPORT_PATH, fileName);
	}

	/**
	 * 场景字段
	 * 
	 * @param wb
	 * @param dataBean
	 */
	private static void sceneFieldSheetHandler(Workbook wb, ServBean dataBean) {
		Sheet sceneFieldSheet = wb.getSheet("场景字段");
		/* -------------- 处理输入字段 ------------- */
		// 请求报文字段列表
		List<ServFieldBean> inputFieldList = FieldUtils.selectBodyFields(dataBean.getConsDetail().getInputFieldList());
		// 在第四行，即【服务场景字段（输出）】上方插入多行
		ExcelUtils.copyRows(sceneFieldSheet, 3, inputFieldList.size() - 1);

		// 行号。由于包含2行标题：【服务场景字段（输入）】【明细标题】，故起始值为2
		int line = 2;
		for (ServFieldBean field : inputFieldList) {
			Row row = sceneFieldSheet.getRow(line);
			processSceneFieldData(row, field, dataBean.getConsDetail());
			line++;
		}

		/* -------------- 处理输出字段 ------------- */
		// 包含2行标题：【服务场景字段（输出）】【明细标题】
		line += 2;
		List<ServFieldBean> outputFieldList = FieldUtils
				.selectBodyFields(dataBean.getConsDetail().getOutputFieldList());
		// 插入多行，起始行为输入字段数量+4行标题
		ExcelUtils.copyRows(sceneFieldSheet, line, outputFieldList.size() - 1);
		for (ServFieldBean field : outputFieldList) {
			Row row = sceneFieldSheet.getRow(line);
			processSceneFieldData(row, field, dataBean.getConsDetail());
			line++;
		}

	}

	/**
	 * 处理单元格数据
	 * 
	 * @param row
	 * @param field
	 * @param consDetail
	 */
	private static void processSceneFieldData(Row row, ServFieldBean field, ServDetailListBean consDetail) {

		String type = field.getFieldType();
		if ("struct".equalsIgnoreCase(type)) {
			fillStructField(row, field);
		} else {
			fillStandardField(row, field, consDetail);
		}

	}

	/**
	 * 循环标签-struct类型字段赋值
	 * 
	 * @param row
	 * @param field
	 */
	private static void fillStructField(Row row, ServFieldBean field) {
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

		// 字段名称（英文）
		row.getCell(0).setCellValue(name);
		// 字段名称（中文）
		row.getCell(1).setCellValue(field.getFieldName());
		// 字段类型
		row.getCell(2).setCellValue("循环标签");
		// 是否必输
		row.getCell(4).setCellValue("Y".equals(field.getFieldRequired()) ? "是" : "否");
		// 是否敏感默认值：0-否
		row.getCell(5).setCellValue("否");
		// 循环次数
		row.getCell(9).setCellValue(Optional.ofNullable(field.getFieldLength()).orElse(""));
	}

	/**
	 * 标准字段-string/number字段赋值
	 * 
	 * @param row
	 * @param field
	 * @param consDetail
	 */
	private static void fillStandardField(Row row, ServFieldBean field, ServDetailListBean consDetail) {
		// 字段名称（英文）
		row.getCell(0).setCellValue(Optional.ofNullable(field.getFieldName()).orElse(""));
		// 字段名称（中文）
		String desc = field.getFieldDescCN();
		if (desc == null || "".equals(desc.trim())) {
			desc = field.getFieldDescEN();
		}
		row.getCell(1).setCellValue(desc);
		// 字段类型
		row.getCell(2).setCellValue(FieldUtils.getFieldType(field.getFieldType()));
		// 数据格式
		row.getCell(3).setCellValue(Optional.ofNullable(field.getFieldProcess()).orElse(""));
		// 是否必输
		row.getCell(4).setCellValue("Y".equals(field.getFieldRequired()) ? "是" : "否");
		// 是否敏感默认值：0-否
		row.getCell(5).setCellValue("否");
		// 字段长度
		row.getCell(6).setCellValue(Optional.ofNullable(field.getFieldLength()).orElse(""));
		// 默认值
		row.getCell(7).setCellValue(Optional.ofNullable(field.getFieldDefaultValue()).orElse(""));
		// 枚举列表
		row.getCell(8).setCellValue(Optional.ofNullable(field.getFieldEnum()).orElse(""));
		// 循环次数
		row.getCell(9).setCellValue("");
		// 备注
		row.getCell(10).setCellValue(Optional.ofNullable(field.getFieldRemark()).orElse(""));
		// 引用名
		row.getCell(11).setCellValue(Optional.ofNullable(consDetail.getNamespaceRefName()).orElse(""));
		// 引用名空间
		row.getCell(12).setCellValue(Optional.ofNullable(consDetail.getNamespaceUrl()).orElse(""));
		// 引用文件名
		row.getCell(13).setCellValue(Optional.ofNullable(consDetail.getMetadataFileName()).orElse(""));
		// 引用字段
		row.getCell(14).setCellValue(Optional.ofNullable(field.getFieldName()).orElse(""));
	}

	/**
	 * 服务场景清单
	 * 
	 * @param wb
	 * @param dataBean
	 */
	private static void sceneListSheetHandler(Workbook wb, ServBean dataBean) {
		Sheet sceneListSheet = wb.getSheet("服务场景清单");
		Row row = sceneListSheet.getRow(2);
		row.getCell(0).setCellValue(dataBean.getBriefBean().getServiceId());
		row.getCell(1).setCellValue(dataBean.getBriefBean().getServiceName());
		row.getCell(2).setCellValue(dataBean.getBriefBean().getServiceName());
		row.getCell(3).setCellValue("A02");
		row.getCell(4).setCellValue(dataBean.getBriefBean().getServiceDesc());
		row.getCell(5).setCellValue(dataBean.getBriefBean().getChangeReason());
		row.getCell(6).setCellValue(dataBean.getBriefBean().getRemark());
		row.getCell(7).setCellValue("");
		String shortName = dataBean.getBriefBean().getPrvdSysName();
		SysNameBean nameBean = DataCenter.getNameBean(shortName);
		if (nameBean == null) {
			throw new IllegalArgumentException("无法在【系统编号】标签页中找到【" + shortName + "】对应记录！");
		}
		row.getCell(8).setCellValue(nameBean.getCnName()+"-"+nameBean.getShortName());
		row.getCell(9).setCellValue(dataBean.getBriefBean().getServiceCode());
		row.getCell(10).setCellValue("1-标准场景");
		row.getCell(11).setCellValue(dataBean.getBriefBean().getPrvdType());
		if ("SOAP".equalsIgnoreCase(dataBean.getBriefBean().getPrvdType())
				|| "JSON".equalsIgnoreCase(dataBean.getBriefBean().getPrvdType())) {
			row.getCell(12).setCellValue("HTTPS");
		} else {
			row.getCell(12).setCellValue("MQ");
		}
	}

	/**
	 * 服务清单
	 * 
	 * @param wb
	 * @param dataBean
	 */
	private static void serviceListSheetHandler(Workbook wb, ServBean dataBean) {
		Sheet serviceListSheet = wb.getSheet("服务清单");
		// 获取到服务清单Sheet页中的第3行为其中的列赋值
		Row row = serviceListSheet.getRow(2);
		// 注意 合并的单元格也要按照合并前的格数来算
		row.getCell(0).setCellValue(dataBean.getBriefBean().getServMajorClass()); // 服务大类
		row.getCell(1).setCellValue(dataBean.getBriefBean().getServSubClass()); // 服务小类
		row.getCell(2).setCellValue(dataBean.getBriefBean().getServiceName()); // 服务名称
		row.getCell(3).setCellValue(dataBean.getBriefBean().getServiceId()); // 服务ID
	}

}
