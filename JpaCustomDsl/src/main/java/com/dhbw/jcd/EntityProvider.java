package com.dhbw.jcd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.dhbw.jcd.exceptions.AttributeNotFoundException;
import com.dhbw.jcd.exceptions.DuplicateAttributeException;
import com.dhbw.jcd.exceptions.EntityNotMappedException;

public class EntityProvider {
	private final Class clazz;
	private final String alias;
	
	private List<ComparatorProvider> comparators = new ArrayList<>();
	private Map<String, EntityProvider> joinEntities = new LinkedHashMap<>();
	
	
	public EntityProvider(Class clazz) throws EntityNotMappedException {
		this.clazz = clazz;
		
		if(!ensureContainsEntityAnnotation(clazz)) {
			throw new EntityNotMappedException(clazz);
		}
		
		this.alias = generateAlias(clazz);
	}
	

	public String getAlias() {
		return this.alias;
	}
	
	public EntityProvider joins(JoinProvider... joinProviders) throws AttributeNotFoundException {
		
		
		for(JoinProvider joinProvider : joinProviders) {
			//Check if relation exists
			
			//TODO differentiate between ToOne and ToMany - is this needed?
			
			extractAttributeType(this.clazz, joinProvider.getRelationName());
			
			
		}
		
		return this;
	}
	
	public EntityProvider join(String relationName) throws EntityNotMappedException, AttributeNotFoundException {
		//Check whether selected relation exists
		Class relationType = extractAttributeType(this.clazz, relationName);
		
		//If no exception has been thrown, this relation exists
		EntityProvider entityProvider = new EntityProvider(relationType);
		
		joinEntities.put(relationName, entityProvider);
		
		return new EntityProvider(relationType);
	}
	
	public ComparatorProvider where(String attributeName) throws AttributeNotFoundException, DuplicateAttributeException {
		Class attributeType = extractAttributeType(this.clazz, attributeName);
		
		ComparatorProvider comparator = new ComparatorProvider(this, attributeName, attributeType);
		
		comparators.add(comparator);
		
		return comparator;
	}
	
	public String generateWhereQuery() {
		List<String> whereQueries = new ArrayList<>();
		for(Entry<String, ComparatorProvider> comp : comparators.entrySet()) {
			whereQueries.add(comp.getValue().getQuery());
		}
		
		String combinedWhereQuery = String.join(" and ", whereQueries);
		
		return combinedWhereQuery;
	}
	
	public String generateJoinQuery() {
		
	}
	
	private static boolean ensureContainsEntityAnnotation(Class clazz) {
		//TODO Check via reflection if clazz contains entity-annotation
	}
	
	private static Class extractAttributeType(Class clazz, String attributeName) throws AttributeNotFoundException {
		try {
			Field field = clazz.getDeclaredField(attributeName);
			field.setAccessible(true);
			
			return field.getClass();
		} catch(NoSuchFieldException e) {
			//TODO
			throw new AttributeNotFoundException(clazz, attributeName);
		}
	}
	

	
	/**
	 * Generate unique alias for passed class
	 * @param clazz
	 * @return
	 */
	private static String generateAlias(Class clazz) {
		return String.format("%s_%s", clazz.getName(), UUID.randomUUID().toString());
	}
	
}
