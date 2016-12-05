package com.dhbw.jcd;

import java.lang.reflect.Type;

import com.dhbw.jcd.exceptions.InvalidComparisonException;
import com.dhbw.jcd.exceptions.TypeMismatchException;

public class ComparatorProvider<T> {
	private final EntityProvider entityProvider;
	private final String attributeName;
	private final Type attributeType;
	
	private String generatedQuery;
	
	public ComparatorProvider(EntityProvider entityProvider, String attributeName, Type attributeType) {
		this.entityProvider = entityProvider;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
	}
	
	public EntityProvider eq(T value) throws TypeMismatchException {
		ensureTypeSafety(attributeType, value);
		
		//l.name = 'Heinz'
		generatedQuery = String.format("%s.%s = %s", this.entityProvider.getAlias(), this.attributeName, convertValueToString(value));
		
		return this.entityProvider;
		
	}
	
	public EntityProvider gt(T value) throws TypeMismatchException, InvalidComparisonException {
		ensureTypeSafety(attributeType, value);
		
		if(!isNumeric(attributeType)) {
			throw new InvalidComparisonException(this.entityProvider, this.attributeName, " gt can only be applied to numeric types");
		}
		
		//l.age > 123
		generatedQuery = String.format("%s.%s > %s", this.entityProvider.getAlias(), this.attributeName, convertValueToString(value));
		
		return this.entityProvider;
		
	}
	
	public EntityProvider lt(T value) throws TypeMismatchException, InvalidComparisonException {
		ensureTypeSafety(attributeType, value);
		
		if(!isNumeric(attributeType)) {
			throw new InvalidComparisonException(this.entityProvider, this.attributeName, " gt can only be applied to numeric types");
		}
		
		//l.age < 123
		generatedQuery = String.format("%s.%s < %s", this.entityProvider.getAlias(), this.attributeName, convertValueToString(value));
		
		return this.entityProvider;
		
	}
	
	public EntityProvider max() throws TypeMismatchException, InvalidComparisonException {
		if(!isNumeric(attributeType)) {
			throw new InvalidComparisonException(this.entityProvider, this.attributeName, " gt can only be applied to numeric types");
		}
		
		//max(l.age)
		generatedQuery = String.format("max(%s.%s)", this.entityProvider.getAlias(), this.attributeName);
		
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
	
	private boolean isNumeric(Type type) {
		return Number.class.isAssignableFrom((Class<?>)type);
	}
	
	private void ensureTypeSafety(Type attributeType, Object passedParameter) throws TypeMismatchException {
		if(!passedParameter.getClass().equals(attributeType)) {
			throw new TypeMismatchException(entityProvider.getEntityClass(), this.attributeName, attributeType, passedParameter.getClass());
		}
	}
}
