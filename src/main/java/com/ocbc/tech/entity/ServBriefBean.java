package com.ocbc.tech.entity;
/**
 * 服务概览对象
 * 
 * @author ZHoward
 *
 */
public class ServBriefBean {
	// 服务ID
	private String serviceId;
	// 服务名称
	private String serviceName;
	// 接口码
	private String serviceCode;
	// 服务描述
	private String serviceDesc;
	// C端报文类型
	private String consType;
	// P端报文类型
	private String prvdType;
	// 提供方系统名称
	private String prvdSysName;
	// 服务大类
	private String servMajorClass;
	// 服务小类
	private String servSubClass;
	// 变更原因
	private String changeReason;
	// 备注
	private String remark;
	// C端接口文档超链接
	private String consHyLink;
	// P端接口文档超链接
	private String prvdHyLink;
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getConsType() {
		return consType;
	}
	public void setConsType(String consType) {
		this.consType = consType;
	}
	public String getPrvdType() {
		return prvdType;
	}
	public void setPrvdType(String prvdType) {
		this.prvdType = prvdType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getConsHyLink() {
		return consHyLink;
	}
	public void setConsHyLink(String consHyLink) {
		this.consHyLink = consHyLink;
	}
	public String getPrvdHyLink() {
		return prvdHyLink;
	}
	public void setPrvdHyLink(String prvdHyLink) {
		this.prvdHyLink = prvdHyLink;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public String getPrvdSysName() {
		return prvdSysName;
	}
	public void setPrvdSysName(String prvdSysName) {
		this.prvdSysName = prvdSysName;
	}
	public String getServMajorClass() {
		return servMajorClass;
	}
	public void setServMajorClass(String servMajorClass) {
		this.servMajorClass = servMajorClass;
	}
	public String getServSubClass() {
		return servSubClass;
	}
	public void setServSubClass(String servSubClass) {
		this.servSubClass = servSubClass;
	}
	public String getChangeReason() {
		return changeReason;
	}
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	@Override
	public String toString() {
		return "ServBriefBean [serviceId=" + serviceId + ", serviceName=" + serviceName + ", serviceCode=" + serviceCode
				+ ", serviceDesc=" + serviceDesc + ", consType=" + consType + ", prvdType=" + prvdType
				+ ", prvdSysName=" + prvdSysName + ", servMajorClass=" + servMajorClass + ", servSubClass="
				+ servSubClass + ", changeReason=" + changeReason + ", remark=" + remark + ", consHyLink=" + consHyLink
				+ ", prvdHyLink=" + prvdHyLink + "]";
	}
	
	
}
