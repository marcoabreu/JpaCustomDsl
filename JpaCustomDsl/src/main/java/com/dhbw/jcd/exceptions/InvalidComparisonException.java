package com.dhbw.jcd.exceptions;

import java.text.MessageFormat;

import com.dhbw.jcd.EntityProvider;

public class InvalidComparisonException extends JcdException{

	public InvalidComparisonException(EntityProvider entityProvider, String attributeName, String message) {
		super(MessageFormat.format("Invalid comparison for {0}.{1}: {2}", entityProvider.getEntityClass().toGenericString(), attributeName, message));
	}

}
