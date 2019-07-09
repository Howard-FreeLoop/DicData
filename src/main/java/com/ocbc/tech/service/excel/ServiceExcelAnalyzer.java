package com.ocbc.tech.service.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocbc.tech.data.DataCenter;
import com.ocbc.tech.entity.ServBean;
import com.ocbc.tech.entity.ServBriefBean;
import com.ocbc.tech.entity.ServDetailListBean;
import com.ocbc.tech.entity.ServFieldBean;
import com.ocbc.tech.entity.SysNameBean;
import com.ocbc.tech.util.ExcelUtils;

public class ServiceExcelAnalyzer {
	
	private static Logger logger = LoggerFactory.getLogger(ServiceExcelAnalyzer.class);

	private static final String OUTPUT_SIGN = "输出";
	
	/**
	 * 解析EXCEL文件
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public static List<ServBean> analyzeExcelFile(String filePath) throws IOException {
		
		List<ServBean> serviceList = new ArrayList<>();
		
		File excelFile = new File(filePath); // 创建文件对象
		FileInputStream in = new FileInputStream(excelFile); // 文件流
		ExcelUtils.checkExcelVaild(excelFile);
		Workbook workbook = ExcelUtils.getWorkbok(in, excelFile);

		analyzeSysNameSheet(workbook.getSheet("系统编号"));
		
		List<ServBriefBean> list = analyzeIndexSheet(workbook.getSheet("Index"));

		for (ServBriefBean brief : list) {
			ServBean bean = new ServBean();
			bean.setBriefBean(brief);
			// 查找并解析C端接口字段明细
			Sheet consSheet = workbook.getSheet(brief.getConsHyLink().substring(1, brief.getConsHyLink().indexOf("\'!")));
			if (consSheet == null) {
				logger.error("无法找到服务[" + brief.getServiceId() + "]对应C端接口明细Sheet页！");
				break;
			}
			bean.setConsDetail(analyzeDetailSheet(consSheet));
			
			// 查找并解析P端接口字段明细
			Sheet prvdSheet = workbook.getSheet(brief.getPrvdHyLink().substring(1, brief.getPrvdHyLink().indexOf("\'!")));
			if (prvdSheet == null) {
				logger.error("无法找到服务[" + brief.getServiceId() + "]对应P端接口明细Sheet页！");
				break;
			}
			bean.setPrvdDetail(analyzeDetailSheet(prvdSheet));
			
			serviceList.add(bean);
		}
		
		logger.info("解析完毕，共计["+serviceList.size()+"]个服务！");
		
		return serviceList;
	}
	
	/**
	 * 解析系统编号对照sheet
	 * 
	 * @param sheet
	 */
	private static void analyzeSysNameSheet(Sheet sheet) {
		// 从1开始是为了跳过标题行
		for(int i=1;i<sheet.getLastRowNum();i++) {
			Row row = sheet.getRow(i);
			if(ExcelUtils.isBlankCell(row.getCell(2))) {
				continue;
			}
			String shortName = ExcelUtils.getCellValue(row.getCell(2));
			SysNameBean bean = new SysNameBean();
			bean.setCnName(ExcelUtils.getCellValue(row.getCell(1)));
			bean.setShortName(shortName);
			bean.setDesc(ExcelUtils.getCellValue(row.getCell(3)));
			DataCenter.addSysName(shortName, bean);
		}
	}

	/**
	 * 解析接口明细标签页
	 * 
	 * @param sheet
	 * @return
	 */
	private static ServDetailListBean analyzeDetailSheet(Sheet sheet) {
		ServDetailListBean detailBean = new ServDetailListBean();
		/* ------ 获取头部信息 ------ */
		detailBean.setServiceId(ExcelUtils.getCellValue(sheet.getRow(0).getCell(1)));
		detailBean.setServiceName(ExcelUtils.getCellValue(sheet.getRow(1).getCell(1)));
		detailBean.setServiceCode(ExcelUtils.getCellValue(sheet.getRow(2).getCell(1)));
		detailBean.setMsgType(ExcelUtils.getCellValue(sheet.getRow(0).getCell(5)));
		detailBean.setMsgEncoding(ExcelUtils.getCellValue(sheet.getRow(1).getCell(5)));
		detailBean.setNamespaceUrl(ExcelUtils.getCellValue(sheet.getRow(2).getCell(5)));
		detailBean.setNamespaceRefName(ExcelUtils.getCellValue(sheet.getRow(0).getCell(8)));
		detailBean.setMetadataFileName(ExcelUtils.getCellValue(sheet.getRow(1).getCell(8)));

		// 前6行为表格头及标题等固定部分，正式接口字段从第7行开始
		int inputStart = 6;
		int outputEnd = sheet.getLastRowNum();

		int inputEnd = -1;
		int outputStart = -1;

		for (int i = 6; i < sheet.getLastRowNum(); i++) {
			if (OUTPUT_SIGN.equals(ExcelUtils.getCellValue(sheet.getRow(i).getCell(0)))) {
				outputStart = i + 1;// 跳过[输出]字符所在行
				inputEnd = i - 1;
				break;
			}
		}

		if (inputEnd == -1 || outputStart == -1) {
			logger.error("标签页[" + sheet.getSheetName() + "]中无法找到服务[" + detailBean.getServiceId() + "]的[输出]接口字段！");
			throw new IllegalArgumentException("标签页[" + sheet.getSheetName() + "]中无法找到服务[" + detailBean.getServiceId() + "]的[输出]接口字段！");
		}

		/* ------ 获取输入报文字段清单 ------ */
		detailBean.setInputFieldList(analyzeFields(sheet, inputStart, inputEnd));

		/* ------ 获取输出报文字段清单 ------ */
		detailBean.setOutputFieldList(analyzeFields(sheet, outputStart, outputEnd));
		
		return detailBean;
	}
	
	private static List<ServFieldBean> analyzeFields(Sheet sheet,int s,int e) {
		List<ServFieldBean> list = new ArrayList<>();
		
		String location = "";
		String locationType = "";
		
		for (int i = s; i <= e; i++) {
			Row row = sheet.getRow(i);
			// 过滤空行
			if (ExcelUtils.isBlankCell(row.getCell(0))) {
				continue;
			}
			String fieldName = ExcelUtils.getCellValue(row.getCell(0));
			String fieldType = ExcelUtils.getCellValue(row.getCell(3));
			if("header".equalsIgnoreCase(fieldType) || "body".equalsIgnoreCase(fieldType) || "error".equalsIgnoreCase(fieldType)) {
				locationType = fieldType;
				location = fieldName;
				continue;
			}
			String remark = ExcelUtils.getCellValue(row.getCell(10));
			if("struct".equalsIgnoreCase(fieldType) && "start".equalsIgnoreCase(remark)) {
				location = fieldName;
			}
			ServFieldBean bean = new ServFieldBean();
			bean.setLocation(location);
			bean.setLocationType(locationType);
			analyzeFieldRow(row, bean);
			list.add(bean);
		}
		return list;
	}
	
	/**
	 * 解析单行字段信息
	 * 
	 * @param row
	 * @param bean
	 */
	private static void analyzeFieldRow(Row row,ServFieldBean bean) {

		bean.setFieldName(ExcelUtils.getCellValue(row.getCell(0)));
		bean.setFieldDescEN(ExcelUtils.getCellValue(row.getCell(1)));
		bean.setFieldDescCN(ExcelUtils.getCellValue(row.getCell(2)));
		bean.setFieldType(ExcelUtils.getCellValue(row.getCell(3)));
		bean.setFieldRequired(ExcelUtils.getCellValue(row.getCell(4)));
		
		String length = ExcelUtils.getCellValue(row.getCell(5));
		if(length.endsWith(".0")) {
			length = length.substring(0, length.lastIndexOf(".0"));
		}
		bean.setFieldLength(length);
		bean.setFieldDefaultValue(ExcelUtils.getCellValue(row.getCell(6)));
		bean.setFieldEnum(ExcelUtils.getCellValue(row.getCell(7)));
		bean.setFieldContextValue(ExcelUtils.getCellValue(row.getCell(8)));
		bean.setFieldProcess(ExcelUtils.getCellValue(row.getCell(9)));
		bean.setFieldRemark(ExcelUtils.getCellValue(row.getCell(10)));

	}


	/**
	 * 解析首页，内容应为接口信息概览
	 * 
	 * @param sheet
	 *            第一个标签页
	 * @return
	 */
	public static List<ServBriefBean> analyzeIndexSheet(Sheet sheet) {
		List<ServBriefBean> list = new ArrayList<>();

		// 遍历Index标签页，跳过第一行标题，从第二行数据开始
		for (int i = 1; i < sheet.getLastRowNum(); i++) {

			Row row = sheet.getRow(i);
			// 默认[服务ID]列没有数据则为空行
			if (ExcelUtils.isBlankCell(row.getCell(1))) {
				continue;
			}
			// 转为对象存储
			ServBriefBean entity = new ServBriefBean();
			entity.setServiceId(ExcelUtils.getCellValue(row.getCell(1)));
			entity.setServiceName(ExcelUtils.getCellValue(row.getCell(2)));
			entity.setServiceDesc(ExcelUtils.getCellValue(row.getCell(3)));
			entity.setServiceCode(ExcelUtils.getCellValue(row.getCell(4)));
			entity.setConsType(ExcelUtils.getCellValue(row.getCell(5)));
			entity.setPrvdType(ExcelUtils.getCellValue(row.getCell(6)));
			
			entity.setPrvdSysName(ExcelUtils.getCellValue(row.getCell(7)));
			entity.setServMajorClass(ExcelUtils.getCellValue(row.getCell(8)));
			entity.setServSubClass(ExcelUtils.getCellValue(row.getCell(9)));
			entity.setChangeReason(ExcelUtils.getCellValue(row.getCell(10)));
			entity.setRemark(ExcelUtils.getCellValue(row.getCell(11)));
			
			entity.setConsHyLink(row.getCell(5).getHyperlink().getAddress());
			entity.setPrvdHyLink(row.getCell(6).getHyperlink().getAddress());
			
			list.add(entity);
		}
		return list;
	}
	
}
