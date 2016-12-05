package com.dhbw.jcd.exceptions;

import java.text.MessageFormat;

public class ColumnNotNamedException extends JcdException {
	private static final long serialVersionUID = -69273992548311744L;

	public ColumnNotNamedException(Class clazz, String attributeName) {
		super(MessageFormat.format("Column {0}.{1} has no name", clazz.toGenericString(), attributeName));
	}

}
