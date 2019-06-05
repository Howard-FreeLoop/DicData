package com.ocbc.tech.exception;

@SuppressWarnings("serial")
public class BizException extends RuntimeException {
	private String errorCode;
	private String errorMsg;

	public BizException() {
		super();
	}
	
	public BizException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

	public BizException(String errorCode, String errorMsg) {
		super(errorCode);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
