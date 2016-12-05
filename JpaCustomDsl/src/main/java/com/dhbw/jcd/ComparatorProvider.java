package com.dhbw.jcd;

import java.lang.reflect.Type;

import org.apache.commons.lang3.ClassUtils;

import com.dhbw.jcd.exceptions.TypeMismatchException;

public class ComparatorProvider {
	private final EntityProvider entityProvider;
	private final String attributeName;
	private final Type attributeType;
	
	private String generatedQuery;
	
	public ComparatorProvider(EntityProvider entityProvider, String attributeName, Type attributeType) {
		this.entityProvider = entityProvider;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
	}
	
	public EntityProvider eq(Object value) throws TypeMismatchException {
		ensureTypeSafety(attributeType, value);
		
		//l.name = 'Heinz'
		generatedQuery = String.format("%s.%s = %s", this.entityProvider.getAlias(), this.attributeName, convertValueToString(value));
		
		return this.entityProvider;
		
	}
	

	public EntityProvider gt(Object value) throws TypeMismatchException {
		ensureTypeSafety(attributeType, value);
		
		//l.name > 'Heinz'
		generatedQuery = String.format("%s.%s > %s", this.entityProvider.getAlias(), this.attributeName, convertValueToString(value));
		
		return this.entityProvider;
		
	}
	
	public EntityProvider lt(Object value) throws TypeMismatchException {
		ensureTypeSafety(attributeType, value);
		
		//l.name < 'Heinz'
		generatedQuery = String.format("%s.%s < %s", this.entityProvider.getAlias(), this.attributeName, convertValueToString(value));
		
		return this.entityProvider;
		
	}
	
	public String getQuery() {
		return this.generatedQuery;
	}
	
	private String convertValueToString(Object value) {
		if(value instanceof String) {
			//Add quotes
			return String.format("\"%s\"", value);
		}
		else
		{
			//Should be a number - could there be anything else?
			return value.toString();
		}
	} 
	
	private void ensureTypeSafety(Type attributeType, Object passedParameter) throws TypeMismatchException {
		if(!passedParameter.getClass().equals(attributeType)) {
			throw new TypeMismatchException(entityProvider.getEntityClass(), this.attributeName, attributeType, passedParameter.getClass());
		}
	}
}
