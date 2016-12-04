package com.dhbw.jcd.exceptions;

public class EntityNotMappedException extends JcdException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7906545639387323332L;

	public EntityNotMappedException(Class clazz) {
		super(String.format("Entity %s is not mapped as an entity", clazz.toGenericString());
	}

}
