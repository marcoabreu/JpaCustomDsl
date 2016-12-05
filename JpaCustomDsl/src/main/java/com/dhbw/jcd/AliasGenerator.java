package com.dhbw.jcd;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class AliasGenerator {
	private Map<EntityProvider, Integer> aliasCounter = new HashMap<>();
	
	public AliasGenerator() {
	}

	public synchronized String generateAlias(EntityProvider entityProvider) {
		Integer currentCounter = aliasCounter.get(entityProvider);
		
		if(currentCounter == null) {
			currentCounter = 1;
		} else {
			currentCounter += 1;
		}
		
		aliasCounter.put(entityProvider, currentCounter);
		
		return MessageFormat.format("{0}_{1}", entityProvider.getEntityName(), currentCounter);
	}
}
