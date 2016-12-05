package com.dhbw.jcd;

import com.dhbw.jcd.exceptions.EntityNotMappedException;
import com.dhbw.jcd.exceptions.EntityNotNamedException;

public class JoinProvider extends EntityProvider{
	private final String relationName;
	private EntityProvider parentEntity;
	public JoinProvider(Class clazz, String relationName) throws EntityNotMappedException, EntityNotNamedException {
		super(clazz);
		
		this.relationName = relationName;
	}
	
	public String getRelationName() {
		return relationName;
	}
	
	protected void setParentEntity(EntityProvider parentEntity) {
		this.parentEntity = parentEntity;
	}
	
	public EntityProvider getParentEntity() {
		return this.parentEntity;
	}

}
