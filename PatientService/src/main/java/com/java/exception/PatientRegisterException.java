package com.java.exception;

public class PatientRegisterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PatientRegisterException(String message) {
		super(message);
	}

}
