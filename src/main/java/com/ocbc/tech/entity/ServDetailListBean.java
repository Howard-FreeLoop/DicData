package com.ocbc.tech.entity;

import java.util.List;

/**
 * 服务接口明细列表对象
 * 
 * @author ZHoward
 *
 */
public class ServDetailListBean {

	// 服务ID
	private String serviceId;
	// 服务名称
	private String serviceName;
	// 接口码
	private String serviceCode;
	// 报文类型
	private String msgType;
	// 报文编码
	private String msgEncoding;
	// 命名空间URL
	private String namespaceUrl;
	// 命名空间引用名
	private String namespaceRefName;
	// 数据字典文件名称
	private String metadataFileName;
	// 输入（请求）报文字段列表
	private List<ServFieldBean> inputFieldList;
	// 输出（响应）报文字段列表
	private List<ServFieldBean> outputFieldList;
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
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMsgEncoding() {
		return msgEncoding;
	}
	public void setMsgEncoding(String msgEncoding) {
		this.msgEncoding = msgEncoding;
	}
	public String getNamespaceUrl() {
		return namespaceUrl;
	}
	public void setNamespaceUrl(String namespaceUrl) {
		this.namespaceUrl = namespaceUrl;
	}
	public String getNamespaceRefName() {
		return namespaceRefName;
	}
	public void setNamespaceRefName(String namespaceRefName) {
		this.namespaceRefName = namespaceRefName;
	}
	public String getMetadataFileName() {
		return metadataFileName;
	}
	public void setMetadataFileName(String metadataFileName) {
		this.metadataFileName = metadataFileName;
	}
	public List<ServFieldBean> getInputFieldList() {
		return inputFieldList;
	}
	public void setInputFieldList(List<ServFieldBean> inputFieldList) {
		this.inputFieldList = inputFieldList;
	}
	public List<ServFieldBean> getOutputFieldList() {
		return outputFieldList;
	}
	public void setOutputFieldList(List<ServFieldBean> outputFieldList) {
		this.outputFieldList = outputFieldList;
	}
	@Override
	public String toString() {
		return "ServDetailListBean [serviceId=" + serviceId + ", serviceName=" + serviceName + ", serviceCode="
				+ serviceCode + ", msgType=" + msgType + ", msgEncoding=" + msgEncoding + ", namespaceUrl="
				+ namespaceUrl + ", namespaceRefName=" + namespaceRefName + ", metadataFileName=" + metadataFileName
				+ ", inputFieldList=" + inputFieldList + ", outputFieldList=" + outputFieldList + "]";
	}
	
}
