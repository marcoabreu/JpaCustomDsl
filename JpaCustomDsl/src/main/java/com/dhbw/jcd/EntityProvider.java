package com.dhbw.jcd;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.ClassUtils;

import com.dhbw.jcd.exceptions.AttributeNotFoundException;
import com.dhbw.jcd.exceptions.ColumnNotMappedException;
import com.dhbw.jcd.exceptions.ColumnNotNamedException;
import com.dhbw.jcd.exceptions.EntityNotMappedException;
import com.dhbw.jcd.exceptions.EntityNotNamedException;
import com.dhbw.jcd.exceptions.JcdException;
import com.dhbw.jcd.exceptions.RelationNotMappedException;

public class EntityProvider {
	private final Class entityClass;
	private final String entityName;
	private final String alias;
	
	private List<ComparatorProvider> comparators = new ArrayList<>();
	private Map<String, JoinProvider> joinEntities = new LinkedHashMap<>();
	
	
	public EntityProvider(Class clazz, AliasGenerator aliasGenerator) throws EntityNotMappedException, EntityNotNamedException {
		this.entityClass = clazz;

		this.entityName = extractEntityName(clazz);		
		this.alias = aliasGenerator.generateAlias(this);
	}
	
	public Class getEntityClass() {
		return this.entityClass;
	}
	
	public String getEntityName() {
		return this.entityName;
	}

	public String getAlias() {
		return this.alias;
	}
	
	protected Set<JoinProvider> getJoinEntities() {
		Set<JoinProvider> joins = new LinkedHashSet<>();
		joins.addAll(joinEntities.values());
		
		//Add nested joins
		joinEntities.values().stream().map(j -> j.getJoinEntities()).forEach(j -> joins.addAll(j));
		
		return joins;
	}
	
	public EntityProvider joins(EntityProvider... entityProviders) throws JcdException {
		//The type may be EntityProvider, but we expect all passed providers to be JoinProviders because of the hirarchy tree
		
		List<JoinProvider> joinProviders = Arrays.stream(entityProviders).filter(e -> (e instanceof JoinProvider)).map(e -> (JoinProvider)e).collect(Collectors.toList());
		
		//Check if there are any passed providers which are not a joinProvider
		if(joinProviders.size() != entityProviders.length) {
			 List<EntityProvider> unmappedEntityProviders = Arrays.stream(entityProviders).filter(e -> !(e instanceof JoinProvider)).collect(Collectors.toList());
			 throw new JcdException(MessageFormat.format("{0} is not a join provider. Please make sure to use the factory method to start a join", unmappedEntityProviders.get(0).entityName));
		}
		
		for(JoinProvider joinProvider : joinProviders) {
			Field relationField = extractAttributeField(this.entityClass, joinProvider.getRelationName());
			
			//Check if attribute is mapped as annotation
			OneToMany oneToManyAnnotation = relationField.getDeclaredAnnotation(OneToMany.class);
			ManyToOne manyToOneAnnotation = relationField.getDeclaredAnnotation(ManyToOne.class);
			ManyToMany manyToManyAnnotation = relationField.getDeclaredAnnotation(ManyToMany.class);
			
			if(oneToManyAnnotation != null) {
				
			} else if(manyToOneAnnotation != null) {
				
			} else if(manyToManyAnnotation != null) {
				
			} else {
				throw new RelationNotMappedException(this.entityClass, joinProvider.getRelationName());
			}
			
			joinEntities.put(joinProvider.getRelationName(), joinProvider);
			joinProvider.setParentEntity(this);
		}
		
		return this;
	}
	
	protected List<ComparatorProvider> getComparators() {
		List<ComparatorProvider> compares = new ArrayList<>();
		compares.addAll(comparators);
		
		//Add nested joins
		joinEntities.values().stream().map(j -> j.getComparators()).forEach(j -> compares.addAll(j));
		
		return compares;
	}
	
	public ComparatorProvider where(String attributeName) throws AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException {
		//Comfort wrapper
		
		return where(attributeName, Object.class);
	}
		
	
	public <T> ComparatorProvider<T> where(String attributeName, Class<T> type) throws AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException {
		Field attributeField = extractAttributeField(this.entityClass, attributeName);
		
		//Check if attribute is annotated as Column and has a name
		String columnName = extractColumnName(this.entityClass, attributeName);
		
		Type attributeType = ClassUtils.primitiveToWrapper((Class<?>) attributeField.getGenericType());
		
		ComparatorProvider<T> comparator = new ComparatorProvider<T>(this, attributeName, attributeType);
		
		comparators.add(comparator);
		
		return comparator;
	}
	
	private String generateWhereQuery() {
		List<String> whereQueries = new ArrayList<>();
		for(ComparatorProvider comp : getComparators()) {
			whereQueries.add(comp.getQuery());
		}
		
		String combinedWhereQuery = String.join(" AND ", whereQueries);
		
		return combinedWhereQuery;
	}
	
	private String generateJoinQuery() {
		List<String> joinQueries = new ArrayList<>();
		for(JoinProvider join : getJoinEntities()) {
			joinQueries.add(MessageFormat.format("JOIN {0}.{1} {2}", join.getParentEntity().getAlias(), join.getRelationName(), join.getAlias()));
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
		
		if(!joinClause.isEmpty()) {
			sb.append(" ");
			sb.append(joinClause);
		}
		
		if(!whereClause.isEmpty()) {
			sb.append(" WHERE ");
			sb.append(whereClause);
		}
		
		return sb.toString();
	}
	
	/**
	 * Find the specified annotation
	 * @param reflectionObject Object on which to search annotation
	 * @param searchedAnnotation Annotation to search
	 * @return Annotation or null if not found
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Annotation> T findAnnotation(AccessibleObject reflectionObject, Class<T> searchedAnnotation) {
		Optional<Annotation> entityAnnotationOpt = Arrays.stream(reflectionObject.getDeclaredAnnotations()).filter(a -> a.annotationType().equals(searchedAnnotation)).findFirst();
		
		if(entityAnnotationOpt.isPresent()) {
			return (T)entityAnnotationOpt.get();
		} else {
			return null;
		}
	}
	
	private static String extractEntityName(Class<?> clazz) throws EntityNotMappedException, EntityNotNamedException{
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
	
	private static String extractColumnName(Class clazz, String attributeName) throws AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException {
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
	
	private static Field extractAttributeField(Class clazz, String attributeName) throws AttributeNotFoundException {
		try {
			Field field = clazz.getDeclaredField(attributeName);
			field.setAccessible(true);
			
			return field;
		} catch(NoSuchFieldException e) {
			throw new AttributeNotFoundException(clazz, attributeName);
		}
	}
}
