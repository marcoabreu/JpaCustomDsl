package com.dhbw.jcd;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.ClassUtils;

import com.dhbw.jcd.exceptions.AttributeNotFoundException;
import com.dhbw.jcd.exceptions.ColumnNotMappedException;
import com.dhbw.jcd.exceptions.ColumnNotNamedException;
import com.dhbw.jcd.exceptions.EntityNotMappedException;
import com.dhbw.jcd.exceptions.EntityNotNamedException;
import com.dhbw.jcd.exceptions.JcdException;
import com.dhbw.jcd.exceptions.RelationNotMappedException;
import com.dhbw.jcd.exceptions.TypeMismatchException;

public class EntityProvider {
	private final Class entityClass;
	private final String entityName;
	private final String alias;
	
	private List<ComparatorProvider> comparators = new ArrayList<>();
	private Map<String, JoinProvider> joinEntities = new LinkedHashMap<>();
	
	
	public EntityProvider(Class clazz, AliasGenerator aliasGenerator) throws EntityNotMappedException, EntityNotNamedException {
		this.entityClass = clazz;

		this.entityName = Utilities.extractEntityName(clazz);		
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
			Field relationField = Utilities.extractAttributeField(this.entityClass, joinProvider.getRelationName());
			
			//Extract mapped class
			Class<?> mappedClass;
			if(relationField.getDeclaredAnnotation(OneToOne.class) != null ||
					relationField.getDeclaredAnnotation(ManyToOne.class) != null) {
				mappedClass = relationField.getType();
			} else if(relationField.getDeclaredAnnotation(OneToMany.class) != null || 
					relationField.getDeclaredAnnotation(ManyToMany.class) != null) {
		        ParameterizedType listType = (ParameterizedType) relationField.getGenericType();
		        mappedClass = (Class<?>) listType.getActualTypeArguments()[0];
			} else {
				//No annotation found, it does not seem to be mapped
				throw new RelationNotMappedException(this.entityClass, joinProvider.getRelationName());
			}
			
			//Check if relation attribute type matches the passed type of the passed entity-provider
			if(Utilities.isTypeAssignable(this.entityClass, mappedClass)) {
				throw new TypeMismatchException(this.entityClass, joinProvider.getRelationName(), mappedClass, joinProvider.getEntityClass());
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
		Field attributeField = Utilities.extractAttributeField(this.entityClass, attributeName);
		
		//Check if attribute is annotated as Column and has a name
		String columnName = Utilities.extractColumnName(this.entityClass, attributeName);
		
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
	
	
}
