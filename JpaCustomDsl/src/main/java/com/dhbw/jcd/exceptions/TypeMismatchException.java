package com.dhbw.jcd.exceptions;

import java.lang.reflect.Type;

public class TypeMismatchException extends JcdException {
	private static final long serialVersionUID = 4317773030860641767L;

	//public TypeMismatchException(Class entityClass, Class expectedType, Class realType) {
	//}
	

	
	public TypeMismatchException(Class entityClass, String attributeName, Type expectedType, Class realType) {
		super(String.format("Entity's attribute %s.%s type %s does not match passed type %s", entityClass.toGenericString(), attributeName, expectedType.toString(), realType.toGenericString()));
	}

}
