package com.dhbw.jcd;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dhbw.jcd.exceptions.AttributeNotFoundException;
import com.dhbw.jcd.exceptions.ColumnNotMappedException;
import com.dhbw.jcd.exceptions.ColumnNotNamedException;
import com.dhbw.jcd.exceptions.EntityNotMappedException;
import com.dhbw.jcd.exceptions.EntityNotNamedException;
import com.dhbw.jcd.exceptions.TypeMismatchException;

public class Utilities {
	public static String convertValueToString(Object value) {
		if(value instanceof String) {
			//Add quotes
			return String.format("'%s'", value);
		}
		else
		{
			//Should be a number - could there be anything else?
			return value.toString();
		}
	} 
	
	public static boolean isNumeric(Type type) {
		return Number.class.isAssignableFrom((Class<?>)type);
	}
	
	public static boolean isTypeAssignable(Type attributeType, Class parameterClass) throws TypeMismatchException {
		if(!parameterClass.equals(attributeType)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Find the specified annotation
	 * @param reflectionObject Object on which to search annotation
	 * @param searchedAnnotation Annotation to search
	 * @return Annotation or null if not found
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T findAnnotation(AccessibleObject reflectionObject, Class<T> searchedAnnotation) {
		Optional<Annotation> entityAnnotationOpt = Arrays.stream(reflectionObject.getDeclaredAnnotations()).filter(a -> a.annotationType().equals(searchedAnnotation)).findFirst();
		
		if(entityAnnotationOpt.isPresent()) {
			return (T)entityAnnotationOpt.get();
		} else {
			return null;
		}
	}
	
	public static String extractEntityName(Class<?> clazz) throws EntityNotMappedException, EntityNotNamedException{
		Entity entityAnnotation = clazz.getDeclaredAnnotation(Entity.class);
		if(entityAnnotation != null) {
			//TODO Name is only optional, determine default behavior. For now, we *require* a name
			String entityName = entityAnnotation.name();
			
			if(entityName.isEmpty()) {
				throw new EntityNotNamedException(clazz);
			}
				
			return entityName;
		} else {
			throw new EntityNotMappedException(clazz);
		}
	}
	
	public static String extractColumnName(Class clazz, String attributeName) throws AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException {
		Field attributeField = extractAttributeField(clazz,  attributeName);
		Column columnAnnotation = attributeField.getDeclaredAnnotation(Column.class);
		
		if(columnAnnotation != null) {
			//TODO Name is only optional, determine default behavior. For now, we *require* a name
			String columnName = columnAnnotation.name();
			
			if(columnName.isEmpty()) {
				throw new ColumnNotNamedException(clazz, attributeName);
			}
			
			return columnName;
		} else {
			throw new ColumnNotMappedException(clazz, attributeName);
		}
	}
	
	public static Field extractAttributeField(Class clazz, String attributeName) throws AttributeNotFoundException {
		try {
			Field field = clazz.getDeclaredField(attributeName);
			field.setAccessible(true);
			
			return field;
		} catch(NoSuchFieldException e) {
			throw new AttributeNotFoundException(clazz, attributeName);
		}
	}
}
