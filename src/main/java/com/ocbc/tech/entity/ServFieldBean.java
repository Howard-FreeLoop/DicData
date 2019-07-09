package com.ocbc.tech.entity;
/**
 * 接口字段对象
 * 
 * @author ZHoward
 *
 */
public class ServFieldBean {
	// 字段名称
	private String fieldName;
	// 英文描述
	private String fieldDescEN;
	// 中文描述
	private String fieldDescCN;
	// 字段类型
	private String fieldType;
	// 是否必输
	private String fieldRequired;
	// 字段长度
	private String fieldLength;
	// 默认值
	private String fieldDefaultValue;
	// 枚举值
	private String fieldEnum;
	// 上下文变量
	private String fieldContextValue;
	// 处理逻辑
	private String fieldProcess;
	// 备注
	private String fieldRemark;
	// 字段位置
	private String location;
	// 位置类型（如：header，body，error等）
	private String locationType;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldDescEN() {
		return fieldDescEN;
	}
	public void setFieldDescEN(String fieldDescEN) {
		this.fieldDescEN = fieldDescEN;
	}
	public String getFieldDescCN() {
		return fieldDescCN;
	}
	public void setFieldDescCN(String fieldDescCN) {
		this.fieldDescCN = fieldDescCN;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldRequired() {
		return fieldRequired;
	}
	public void setFieldRequired(String fieldRequired) {
		this.fieldRequired = fieldRequired;
	}
	public String getFieldLength() {
		return fieldLength;
	}
	public void setFieldLength(String fieldLength) {
		this.fieldLength = fieldLength;
	}
	public String getFieldDefaultValue() {
		return fieldDefaultValue;
	}
	public void setFieldDefaultValue(String fieldDefaultValue) {
		this.fieldDefaultValue = fieldDefaultValue;
	}
	public String getFieldEnum() {
		return fieldEnum;
	}
	public void setFieldEnum(String fieldEnum) {
		this.fieldEnum = fieldEnum;
	}
	public String getFieldContextValue() {
		return fieldContextValue;
	}
	public void setFieldContextValue(String fieldContextValue) {
		this.fieldContextValue = fieldContextValue;
	}
	public String getFieldProcess() {
		return fieldProcess;
	}
	public void setFieldProcess(String fieldProcess) {
		this.fieldProcess = fieldProcess;
	}
	public String getFieldRemark() {
		return fieldRemark;
	}
	public void setFieldRemark(String fieldRemark) {
		this.fieldRemark = fieldRemark;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	@Override
	public String toString() {
		return "ServFieldBean [fieldName=" + fieldName + ", fieldDescEN=" + fieldDescEN + ", fieldDescCN=" + fieldDescCN
				+ ", fieldType=" + fieldType + ", fieldRequired=" + fieldRequired + ", fieldLength=" + fieldLength
				+ ", fieldDefaultValue=" + fieldDefaultValue + ", fieldEnum=" + fieldEnum + ", fieldContextValue="
				+ fieldContextValue + ", fieldProcess=" + fieldProcess + ", fieldRemark=" + fieldRemark + ", location="
				+ location + ", locationType=" + locationType + "]";
	}
	
}
