package com.dhbw.jcd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Entity;

import com.dhbw.jcd.exceptions.AttributeNotFoundException;
import com.dhbw.jcd.exceptions.EntityNotMappedException;
import com.dhbw.jcd.exceptions.EntityNotNamedException;

public class EntityProvider {
	private final Class entityClass;
	private final String entityName;
	private final String alias;
	
	private List<ComparatorProvider> comparators = new ArrayList<>();
	private Map<String, JoinProvider> joinEntities = new LinkedHashMap<>();
	
	
	public EntityProvider(Class clazz) throws EntityNotMappedException, EntityNotNamedException {
		this.entityClass = clazz;

		this.entityName = extractEntityName(clazz);		
		this.alias = generateAlias(this.entityName);
	}
	
	public Class getEntityClass() {
		return this.entityClass;
	}

	public String getAlias() {
		return this.alias;
	}
	
	public EntityProvider joins(JoinProvider... joinProviders) throws AttributeNotFoundException {
		
		
		for(JoinProvider joinProvider : joinProviders) {
			//TODO Check if relation-annotation exists
			
			//TODO differentiate between ToOne and ToMany - is this needed?
			
			Field relationField = extractAttributeField(this.entityClass, joinProvider.getRelationName());
			
			//TODO check if relationField contains required annotation
			
			joinEntities.put(joinProvider.getRelationName(), joinProvider);
		}
		
		return this;
	}
	
	public ComparatorProvider where(String attributeName) throws AttributeNotFoundException {
		//TODO Check if attribute is annotated 
		Field attributeField = extractAttributeField(this.entityClass, attributeName);
		Type attributeType = attributeField.getGenericType();
		
		ComparatorProvider comparator = new ComparatorProvider(this, attributeName, attributeType);
		
		comparators.add(comparator);
		
		return comparator;
	}
	
	public String generateWhereQuery() {
		//TODO Don't forget the comparators in the joins
		List<String> whereQueries = new ArrayList<>();
		for(ComparatorProvider comp : comparators) {
			whereQueries.add(comp.getQuery());
		}
		
		String combinedWhereQuery = String.join(" AND ", whereQueries);
		
		return combinedWhereQuery;
	}
	
	public String generateJoinQuery() {
		//TODO Don't forget nested joins
		List<String> joinQueries = new ArrayList<>();
		for(Entry<String, JoinProvider> join : joinEntities.entrySet()) {
			joinQueries.add(MessageFormat.format("JOIN {0}.{1} {2}", this.getAlias(), join.getValue().getRelationName(), join.getValue().getAlias()));
		}
		
		String combinedJoinQuery = String.join(" ", joinQueries);
		
		return combinedJoinQuery;
	}
	
	public String generateQuery() {
		String fromClause = MessageFormat.format("FROM {0} AS {1}", this.entityName, this.alias);
		String whereClause = generateWhereQuery();
		String joinClause = generateJoinQuery();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(fromClause);
		
		if(!whereClause.isEmpty()) {
			sb.append(" WHERE ");
			sb.append(whereClause);
		}
		
		if(!joinClause.isEmpty()) {
			sb.append(" ");
			sb.append(joinClause);
		}
		
		return sb.toString();
	}
	
	private static String extractEntityName(Class clazz) throws EntityNotMappedException, EntityNotNamedException{
		//TODO Check via reflection if clazz contains entity-annotation
		//TODO Extract entity-name of that annotation
		Optional<Annotation> entityAnnotationOpt = Arrays.stream(clazz.getDeclaredAnnotations()).filter(a -> a.annotationType().equals(javax.persistence.Entity.class)).findFirst();
		
		if(entityAnnotationOpt.isPresent()) {
			Entity entityAnnotation = (Entity)entityAnnotationOpt.get();
			
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
	
	private static Field extractAttributeField(Class clazz, String attributeName) throws AttributeNotFoundException {
		try {
			Field field = clazz.getDeclaredField(attributeName);
			field.setAccessible(true);
			
			return field;
		} catch(NoSuchFieldException e) {
			//TODO
			throw new AttributeNotFoundException(clazz, attributeName);
		}
	}
	

	
	/**
	 * Generate unique alias for passed class
	 * @param entityClass
	 * @return
	 */
	private static String generateAlias(String entityName) {
		return String.format("%s_%s", entityName, UUID.randomUUID().toString());
	}
	
}
