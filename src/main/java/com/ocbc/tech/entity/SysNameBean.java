package com.ocbc.tech.entity;
/**
 * 应用系统名实体类<br>
 * 存储数据如：< CCLM=额度管理系统 >
 * 
 * @author ZHoward
 *
 */
public class SysNameBean {
	// 系统名称
	private String cnName;
	// 系统简称
	private String shortName;
	// 系统描述
	private String desc;
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "AppSysNameBean [cnName=" + cnName + ", shortName=" + shortName + ", desc=" + desc + "]";
	}
	
}
