package com.dhbw.jcd;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dhbw.jcd.exceptions.InvalidComparisonException;
import com.dhbw.jcd.exceptions.TypeMismatchException;

public class ComparatorProvider<T> {
	private final EntityProvider entityProvider;
	private final String attributeName;
	private final Type attributeType;
	
	private Set<String> modifiers = new LinkedHashSet<>();
	private String generatedQuery;
	
	public ComparatorProvider(EntityProvider entityProvider, String attributeName, Type attributeType) {
		this.entityProvider = entityProvider;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
	}
	
	public EntityProvider eq(T value) throws TypeMismatchException {
		ensureTypeSafety(value);
		
		//l.name = 'Heinz'
		generatedQuery = String.format("%s = %s", generateEntityVariableQuery(), Utilities.convertValueToString(value));
		
		return this.entityProvider;
		
	}
	
	public EntityProvider gt(T value) throws TypeMismatchException, InvalidComparisonException {
		ensureTypeSafety(value);
		
		if(!Utilities.isNumeric(attributeType)) {
			throw new InvalidComparisonException(this.entityProvider, this.attributeName, " gt can only be applied to numeric types");
		}
		
		//l.age > 123
		generatedQuery = String.format("%s > %s", generateEntityVariableQuery(), Utilities.convertValueToString(value));
		
		return this.entityProvider;
		
	}
	
	public EntityProvider lt(T value) throws TypeMismatchException, InvalidComparisonException {
		ensureTypeSafety(value);
		
		if(!Utilities.isNumeric(attributeType)) {
			throw new InvalidComparisonException(this.entityProvider, this.attributeName, " gt can only be applied to numeric types");
		}
		
		//l.age < 123
		generatedQuery = String.format("%s < %s", generateEntityVariableQuery(), Utilities.convertValueToString(value));
		
		return this.entityProvider;
		
	}
	
	public EntityProvider like(String pattern) throws TypeMismatchException, InvalidComparisonException {
		if(Utilities.isNumeric(attributeType)) {
			throw new InvalidComparisonException(this.entityProvider, this.attributeName, " like can only be applied to non-numeric types");
		}
		
		//l.name LIKE 'eleph_nt'
		generatedQuery = String.format("%s LIKE '%s'", generateEntityVariableQuery(), pattern);
		
		return this.entityProvider;
		
	}
	
	
	/**
	 * Add a modifier to the compared variable in the style of 'TRIM(entity.var)'
	 * @param functionName Modifier-function-name
	 * @return Comparator with applied modifier
	 */
	public ComparatorProvider<T> addModifier(String functionName) {
		this.modifiers.add(functionName);
		
		return this;
	}
	
	

	public String getQuery() {
		return this.generatedQuery;
	}
	
	/**
	 * Generate the part of the query which represents the queried variable and all assigned modifiers
	 * @return
	 */
	private String generateEntityVariableQuery() {
		int nbClosingBrackets = this.modifiers.size();
		
		StringBuilder sb = new StringBuilder();
		
		//Add modifiers
		for(String modifier : this.modifiers) {
			sb.append(modifier + "(");
		}
		
		sb.append(MessageFormat.format("{0}.{1}", this.entityProvider.getAlias(), this.attributeName));
		
		//Add closing brackets matching to modifiers
		for(int i = 0; i < nbClosingBrackets; i++) {
			sb.append(")");
		}
		
		return sb.toString();
	}
	
	private void ensureTypeSafety(Object passedParameter) throws TypeMismatchException {
		if(!Utilities.isTypeAssignable(this.attributeType, passedParameter.getClass())) {
			throw new TypeMismatchException(entityProvider.getEntityClass(), this.attributeName, attributeType, passedParameter.getClass());
		}
	}
}
