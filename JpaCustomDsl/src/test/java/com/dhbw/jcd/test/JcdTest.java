package com.dhbw.jcd.test;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dhbw.jcd.JcdFactory;
import com.dhbw.jcd.exceptions.AttributeNotFoundException;
import com.dhbw.jcd.exceptions.EntityNotMappedException;
import com.dhbw.jcd.exceptions.EntityNotNamedException;
import com.dhbw.jcd.exceptions.TypeMismatchException;
import com.dhbw.jcd.test.entity.ChildEntity;
import com.dhbw.jcd.test.entity.ParentEntity;

public class JcdTest {
	private static JcdFactory factory;
	
	@BeforeClass
	public static void init() {
		factory = JcdFactory.createFactory();
	}

	@Test
	public void fromTest() throws EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ParentEntity.class).generateQuery();
		System.out.println(query);
	}
	
	@Test
	public void fromWhereTest() throws EntityNotMappedException, EntityNotNamedException, TypeMismatchException, AttributeNotFoundException {
		String query = factory.startFrom(ParentEntity.class)
				.where("intColumn").eq(5).generateQuery();
		System.out.println(query);
	}
	
	@Test
	public void fromJoinTest() throws AttributeNotFoundException, EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ParentEntity.class)
				.joins(
					factory.startJoin(ChildEntity.class, "childEntityRelation")
				)
				.generateQuery();
		System.out.println(query);
	}
}
