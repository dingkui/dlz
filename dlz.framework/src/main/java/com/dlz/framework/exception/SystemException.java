package com.dlz.framework.exception;

/**
 * BaseException for SDK
 */
public class SystemException extends BaseException {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -5345825923487658213L;
	
	private static int DEFUALT_ERROR_CODE = 6001;

	public SystemException(String message, Throwable cause) {
		super(DEFUALT_ERROR_CODE, message, cause);
	}

	public SystemException(String message) {
		super(DEFUALT_ERROR_CODE, message, null);
	}
}
