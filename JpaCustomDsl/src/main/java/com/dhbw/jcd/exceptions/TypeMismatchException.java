package com.dhbw.jcd.exceptions;

import java.lang.reflect.Type;

public class TypeMismatchException extends JcdException {

	public TypeMismatchException(Class entityClass, Class expectedType, Class realType) {
		// TODO Auto-generated constructor stub
	}
	
	public TypeMismatchException(Class entityClass, String attributeName, Type expectedType, Class realType) {
		super(String.format("Entity's attribute %s.%s type %s does not match passed type %s", entityClass.toGenericString(), attributeName, expectedType.toString(), realType.toGenericString()));
	}

}
