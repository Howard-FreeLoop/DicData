package com.ocbc.tech.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

public class ExcelUtils {
	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";

	
	/**
	 * 加载模板文件
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Workbook loadTempleteFile(String templeteFile) throws IOException {
		ClassPathResource resource = new ClassPathResource(templeteFile);
		File tempFile = resource.getFile();
		FileInputStream in = new FileInputStream(tempFile); // 文件流
		try {
			checkExcelVaild(tempFile);
			return getWorkbok(in, tempFile);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	/**
	 * 判断Excel的版本,获取Workbook
	 * 
	 * @param in
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbok(InputStream in, File file) throws IOException {
		Workbook wb = null;
		if (file.getName().endsWith(EXCEL_XLS)) { // Excel 2003
			wb = new HSSFWorkbook(in);
		} else if (file.getName().endsWith(EXCEL_XLSX)) { // Excel 2007/2010
			wb = new XSSFWorkbook(in);
		}
		return wb;
	}

	/**
	 * 判断文件是否是excel
	 * 
	 * @throws Exception
	 */
	public static void checkExcelVaild(File file) throws IOException {
		if (!file.exists()) {
			throw new IOException("文件[" + file.getName() + "]不存在");
		}
		if (!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))) {
			throw new IllegalArgumentException("文件[" + file.getName() + "]不是Excel");
		}
	}

	/**
	 * 获取单元格内容，全部转为String类型返回
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		String str = "";
		switch (cell.getCellTypeEnum()) {
		case BOOLEAN:
			str = String.valueOf(cell.getBooleanCellValue());
			break;
		case ERROR:
			str = String.valueOf(cell.getErrorCellValue());
			break;
		case NUMERIC:
			str = String.valueOf(cell.getNumericCellValue());
			break;
		case STRING:
			str = cell.getStringCellValue();
			break;
		default:
			break;
		}
		return str.trim();
	}

	/**
	 * 判断单元格是否为空
	 * 
	 * @param cell
	 * @return
	 */
	public static boolean isBlankCell(Cell cell) {
		return cell.getCellTypeEnum() == CellType.BLANK;
	}

	/**
	 * 在指定标签页中指定位置插入多行
	 * 
	 * @param sheet
	 *            标签页
	 * @param startLine
	 *            插入位置（即在第N+1行上方插入）
	 * @param num
	 *            需要插入多少行的数量
	 */
	public static void copyRows(Sheet sheet, int startLine, int num) {
		// 如果复制的是最后一行，则以最后一行的样式作为基础样式
		int sourceLine = startLine - 1;
		if(startLine == sheet.getLastRowNum()) {
			sourceLine = startLine;
		}
		Row sourceRow = sheet.getRow(sourceLine);
		// 插入多行
		sheet.shiftRows(startLine, sheet.getLastRowNum(), num, true, false);
		// 依次复制单元格样式
		for (int i = 0; i < num; i++) {
			Row newRow = sheet.createRow(startLine + i);
			newRow.setHeight(sourceRow.getHeight());
			for (int j = 0; j < sourceRow.getLastCellNum(); j++) {
				Cell sourceCell = sourceRow.getCell(j);
				if (sourceCell != null) {
					copyCell(sourceCell, newRow.createCell(j));
				}
			}
		}
	}

	/**
	 * 复制单元格，包括：样式、备注、公式、数值
	 * 
	 * @param sourceCell
	 *            模板单元格
	 * @param newCell
	 *            复制后单元格
	 */
	public static void copyCell(Cell sourceCell, Cell newCell) {
		// 样式
		newCell.setCellStyle(sourceCell.getCellStyle());
		// 评论(备注)
		if (sourceCell.getCellComment() != null) {
			newCell.setCellComment(sourceCell.getCellComment());
		}

		switch (sourceCell.getCellTypeEnum()) {
		case NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(sourceCell)) {
				newCell.setCellValue(sourceCell.getDateCellValue());
			} else {
				newCell.setCellValue(sourceCell.getNumericCellValue());
			}
			break;
		case STRING:
			newCell.setCellValue(sourceCell.getStringCellValue());
			break;
		case BLANK:
			newCell.setCellValue("");
			break;
		case FORMULA:
			if (sourceCell.getCachedFormulaResultTypeEnum() == CellType.NUMERIC) {
				newCell.setCellValue(sourceCell.getNumericCellValue());
			} else {
				newCell.setCellValue(sourceCell.getStringCellValue());
			}
			break;
		default:
			throw new IllegalArgumentException("无法处理[" + sourceCell.getCellTypeEnum() + "]类型单元格！");
		}

	}

}
