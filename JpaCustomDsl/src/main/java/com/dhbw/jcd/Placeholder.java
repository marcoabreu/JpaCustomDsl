package com.dhbw.jcd;

public class Placeholder {
	public static void main(String[] args) {
		//"SELECT l from Library l";
		JcdFactory factory = JcdFactory.createFactory();
		
		factory.select(Placeholder.class).join(ComparatorProvider.class).where("name").equals("Heinz").where("age").equals(18).generateQuery();
		
		
		factory.select(Placeholder.class).joins(
				factory.select(Library.class).where("name").eq("Baumkuchen"),
				factory.select(Library.class).where("name").eq("Baumkuchen"),
				factory.select(Library.class).where("name").eq("Baumkuchen"),
				factory.select(Library.class).where("name").eq("Baumkuchen"),
				factory.select(Library.class).where("name").eq("Baumkuchen"),
				factory.select(Library.class).where("name").eq("Baumkuchen")
		)
		
		
	}
}
