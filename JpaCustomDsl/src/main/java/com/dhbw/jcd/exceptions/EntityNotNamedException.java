package com.dhbw.jcd.exceptions;

public class EntityNotNamedException extends JcdException {

	public EntityNotNamedException(Class clazz) {
		super(String.format("Entity %s is mapped without specifing an entity name", clazz.toGenericString()));
	}

}
