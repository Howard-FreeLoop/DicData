package com.ocbc.tech.util;

import java.util.ArrayList;
import java.util.List;

import com.ocbc.tech.entity.ServFieldBean;

public class FieldUtils {
	
	/**
	 * 获取字段类型（中文）
	 * 
	 * @param fieldType
	 * @return
	 */
	public static String getFieldType(String fieldType) {
		String sfDataType = fieldType;
		if (fieldType != null) {

			switch (fieldType) {
			case "string":
				sfDataType = "字符型";
				break;
			case "number":
				sfDataType = "数字型";
				break;
			case "struct":
				sfDataType = "循环标签";
				break;
			default:
				break;
			}
		}
		return sfDataType;
	}
	
	/**
	 * 由于赞同场景文档仅需要body部分，故使用此方法筛选BODY类型字段
	 * 
	 * @param sourceList
	 * @return
	 */
	public static List<ServFieldBean> selectBodyFields(List<ServFieldBean> sourceList) {
		List<ServFieldBean> targetList = new ArrayList<>();
		for (ServFieldBean field : sourceList) {
			if (isBodyField(field)) {
				targetList.add(field);
			}
		}
		return targetList;
	}

	/**
	 * 字段位置类型为Body，字段类型为string/number/struct认为是符合要求的字段
	 * 
	 * @param field
	 * @return
	 */
	private static boolean isBodyField(ServFieldBean field) {
		if (!"body".equalsIgnoreCase(field.getLocationType())) {
			return false;
		}
		if ("string".equalsIgnoreCase(field.getFieldType())) {
			return true;
		}
		if ("number".equalsIgnoreCase(field.getFieldType())) {
			return true;
		}
		if ("struct".equalsIgnoreCase(field.getFieldType())) {
			return true;
		}
		return false;
	}
	
}
