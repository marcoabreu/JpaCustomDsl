package com.dhbw.jcd.exceptions;

import java.text.MessageFormat;

public class RelationNotMappedException extends JcdException {
	private static final long serialVersionUID = -6210623574886760554L;

	public RelationNotMappedException(Class clazz, String relationName) {
		super(MessageFormat.format("Relation at {0}.{1} is not mapped", clazz.toGenericString(), relationName));
	}

}
