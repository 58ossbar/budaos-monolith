package com.budaos.common.exception;

/**
 * Title:自定义异常 Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */

public class BudaosException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public BudaosException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public BudaosException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public BudaosException(int code,String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public BudaosException(int code,String msg,  Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
