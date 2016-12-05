package com.dhbw.jcd.exceptions;

import java.text.MessageFormat;

public class AttributeNotFoundException extends JcdException {
	private static final long serialVersionUID = 4998060454455913660L;

	public AttributeNotFoundException(Class clazz, String attributeName) {
		super(MessageFormat.format("Unable to find {0}.{1}", clazz.toGenericString(), attributeName));
	}


}
