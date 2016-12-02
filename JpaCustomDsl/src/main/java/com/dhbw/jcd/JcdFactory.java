package com.dhbw.jcd;

public class JcdFactory {
	private JcdFactory() {
		//TODO
	}
	
	public static JcdFactory createFactory() {
		return new JcdFactory();
	}
	
	
	public EntityProvider select(Class clazz) {
		
	}
}
