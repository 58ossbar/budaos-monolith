package com.budaos.common.exception;

import com.budaos.common.constants.ExecStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据校验异常类
 * 
 */

public class ValidateException extends BudaosException {

	public Map<String, String> getErrors() {
		return errors;
	}

	private int code;
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private static final long serialVersionUID = 2239780575292471688L;
	private final Map<String, String> errors = new HashMap<>();

	public ValidateException() {
		this(ExecStatus.VALIDATION_FAIL);
	}

	public ValidateException(ExecStatus execStatus) {
		this(execStatus.getCode(), execStatus.getMsg());
	}

	public ValidateException(String msg) {
		super(ExecStatus.VALIDATION_FAIL.getCode(),msg);
		this.code = ExecStatus.VALIDATION_FAIL.getCode();
		this.msg = msg;
	}

	public ValidateException(int code, String msg) {
		super(code,msg);
		this.code = code;
		this.msg = msg;
	}

	public ValidateException addError(String name, String message) {
		this.errors.put(name, message);
		return this;
	}

}
