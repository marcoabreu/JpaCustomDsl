package com.dhbw.jcd;

public class Placeholder {
	public static void main(String[] args) {
		//"SELECT l from Library l";
		JcdFactory factory = JcdFactory.createFactory();
		
		factory.select(Placeholder.class).join(ComparatorProvider.class).where("name").equals("Heinz").where("age").equals(18).generateQuery();
		//select(Blabla.class).where("name").equals("Heinz").join(Ente.class).join()
	}
}
