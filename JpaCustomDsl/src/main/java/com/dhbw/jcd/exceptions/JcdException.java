package com.dhbw.jcd.exceptions;

public class JcdException extends Exception {

	public JcdException() {
	}

	public JcdException(String arg0) {
		super(arg0);
	}

	public JcdException(Throwable arg0) {
		super(arg0);
	}

	public JcdException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public JcdException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
