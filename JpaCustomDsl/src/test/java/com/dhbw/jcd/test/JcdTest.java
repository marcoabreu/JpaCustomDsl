package com.dhbw.jcd.test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dhbw.jcd.JcdFactory;
import com.dhbw.jcd.exceptions.AttributeNotFoundException;
import com.dhbw.jcd.exceptions.EntityNotMappedException;
import com.dhbw.jcd.exceptions.EntityNotNamedException;
import com.dhbw.jcd.exceptions.JcdException;
import com.dhbw.jcd.exceptions.TypeMismatchException;
import com.dhbw.jcd.test.entity.ChildChildEntity;
import com.dhbw.jcd.test.entity.ChildEntity;
import com.dhbw.jcd.test.entity.ParentEntity;

public class JcdTest {
	private static final String PERSISTENCE_UNIT_NAME = "h2-mem";
	private static EntityManagerFactory entityFactory;
	private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JcdTest.class);
	private static JcdFactory factory;
	
	@BeforeClass
	public static void init() {
		factory = JcdFactory.createFactory();
		entityFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		fillDatabase();
	}
	
	private static void fillDatabase() {
		//TODO Create example entries
		
		
	}
	
	//TODO Create and execute some queries in the tests and check if the returned entities are those, which we expected

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
	public void fromJoinTest() throws JcdException {
		String query = factory.startFrom(ParentEntity.class)
				.joins(
					factory.startJoin(ChildEntity.class, "childEntityRelation")
				)
				.generateQuery();
		System.out.println(query);
	}
	
	@Test
	public void nestedJoinTest() throws EntityNotMappedException, EntityNotNamedException, JcdException {
		String query = factory.startFrom(ParentEntity.class)
				.joins(
					factory.startJoin(ChildEntity.class, "childEntityRelation")
						.joins(
								factory.startJoin(ChildChildEntity.class, "childChildEntityRelation")
						)
				)
				.generateQuery();
		System.out.println(query);
	}
	
	@Test
	public void nestedJoinWhereTest() throws EntityNotMappedException, EntityNotNamedException, JcdException {
		String query = factory.startFrom(ParentEntity.class)
				.where("intColumn").eq(5).where("stringColumn").eq("elephant")
				.joins(
					factory.startJoin(ChildEntity.class, "childEntityRelation")
						.where("shortColumn").eq((short)42).where("longColumn").gt((long)5)
						.joins(
								factory.startJoin(ChildChildEntity.class, "childChildEntityRelation")
						)
				)
				.generateQuery();
		System.out.println(query);
	}
}
