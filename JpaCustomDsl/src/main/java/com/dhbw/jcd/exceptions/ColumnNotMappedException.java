package com.dhbw.jcd.exceptions;

import java.text.MessageFormat;

public class ColumnNotMappedException extends JcdException {
	private static final long serialVersionUID = 6848926132273354117L;

	public ColumnNotMappedException(Class clazz, String attributeName) {
		super(MessageFormat.format("Column {0}.{1} is not mapped as an entity", clazz.toGenericString(), attributeName));
	}

}
