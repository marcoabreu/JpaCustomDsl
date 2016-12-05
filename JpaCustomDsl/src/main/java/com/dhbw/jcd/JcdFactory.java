package com.dhbw.jcd;

import com.dhbw.jcd.exceptions.EntityNotMappedException;
import com.dhbw.jcd.exceptions.EntityNotNamedException;

public class JcdFactory {
	private AliasGenerator aliasGenerator;
	private JcdFactory() {
		aliasGenerator = new AliasGenerator();
	}
	
	public static JcdFactory createFactory() {
		return new JcdFactory();
	}
	
	
	public EntityProvider startFrom(Class clazz) throws EntityNotMappedException, EntityNotNamedException {
		return new EntityProvider(clazz, aliasGenerator);
	}
	
	public JoinProvider startJoin(Class clazz, String relationName) throws EntityNotMappedException, EntityNotNamedException {
		
		return new JoinProvider(clazz, relationName, aliasGenerator);
	}
}
