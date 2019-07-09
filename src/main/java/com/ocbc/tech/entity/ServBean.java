package com.ocbc.tech.entity;

public class ServBean {
	// 概览信息
	private ServBriefBean briefBean;
	// C端接口文档明细
	private ServDetailListBean consDetail;
	// P端接口文档明细
	private ServDetailListBean prvdDetail;
	
	public ServBriefBean getBriefBean() {
		return briefBean;
	}
	public void setBriefBean(ServBriefBean briefBean) {
		this.briefBean = briefBean;
	}
	public ServDetailListBean getConsDetail() {
		return consDetail;
	}
	public void setConsDetail(ServDetailListBean consDetail) {
		this.consDetail = consDetail;
	}
	public ServDetailListBean getPrvdDetail() {
		return prvdDetail;
	}
	public void setPrvdDetail(ServDetailListBean prvdDetail) {
		this.prvdDetail = prvdDetail;
	}
	
	
}
