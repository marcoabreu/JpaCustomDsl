package com.dhbw.jcd.test;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.engine.spi.EntityKey;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dhbw.jcd.JcdFactory;
import com.dhbw.jcd.exceptions.AttributeNotFoundException;
import com.dhbw.jcd.exceptions.ColumnNotMappedException;
import com.dhbw.jcd.exceptions.ColumnNotNamedException;
import com.dhbw.jcd.exceptions.EntityNotMappedException;
import com.dhbw.jcd.exceptions.EntityNotNamedException;
import com.dhbw.jcd.exceptions.InvalidComparisonException;
import com.dhbw.jcd.exceptions.JcdException;
import com.dhbw.jcd.exceptions.TypeMismatchException;
import com.dhbw.jcd.test.entity.ChildChildEntity;
import com.dhbw.jcd.test.entity.ChildEntity;
import com.dhbw.jcd.test.entity.ParentEntity;

public class JcdTest {
	private static final String PERSISTENCE_UNIT_NAME = "h2-mem";
	private static EntityManagerFactory entityFactory;
	private static EntityManager entityManager;
	private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JcdTest.class);
	private static JcdFactory factory;
	
	private static ArrayList<ParentEntity> parentEntityList = new ArrayList<>();
	private static ArrayList<ChildEntity> childEntityList = new ArrayList<>();
	private static ArrayList<ChildChildEntity> childChildEntityList = new ArrayList<>();

	@BeforeClass
	public static void init() {
		factory = JcdFactory.createFactory();
		entityFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		entityManager = entityFactory.createEntityManager();
		fillDatabase();
	}

	private static void fillDatabase() {
		try {
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			try {
				// Create Artists
				ParentEntity projectOne = new ParentEntity("Project One", "Scantraxx Reloaded", 1);
				ParentEntity psykoPunkz = new ParentEntity("Psyko Punkz", "Dirty Workz", 26);
				ParentEntity adaro = new ParentEntity("Adaro", "Scantraxx", 28);
				
			
				// Create Albums
				ChildEntity projectOneAlbum = new ChildEntity("Project One", 13);
				ChildEntity spaceshipEp = new ChildEntity("SpaceshipEP", 1);
				ChildEntity weStayUpEp = new ChildEntity("We stay up EP", 1);
				ChildEntity streamOfBloodEp = new ChildEntity("Stream of Blood EP", 2);
				ChildEntity murderEp = new ChildEntity("Murder EP", 2);
				ChildEntity rockNowEp = new ChildEntity("Rock Now EP", 2);
				ChildEntity mySoulToTakeEp = new ChildEntity("My Soul To Take / Raggamuffin", 4);

				// Create Tracks
				ChildChildEntity prelude = new ChildChildEntity("Prelude", 68);
				ChildChildEntity lifeBeyondEarth = new ChildChildEntity("Life Beyond Earth", 249);
				ChildChildEntity theWorldIsYours = new ChildChildEntity("The World Is Yours", 223);

				ChildChildEntity spaceship = new ChildChildEntity("Spaceship", 204);
				ChildChildEntity westayup = new ChildChildEntity("We Stay Up", 331);
				ChildChildEntity streamOfBlood = new ChildChildEntity("Stream Of Blood", 301);
				ChildChildEntity phonePrank = new ChildChildEntity("Phone Prank", 365);

				ChildChildEntity murderOriginial = new ChildChildEntity("Murder Original Mix", 315);
				ChildChildEntity murderRadio = new ChildChildEntity("Murder Radio Edit", 223);
				ChildChildEntity rockNowOriginal = new ChildChildEntity("Rock Now Original Mix", 400);
				ChildChildEntity rockNowEdit = new ChildChildEntity("Rock Now Radio Edit", 298);
				ChildChildEntity mySoulToTakeOriginal = new ChildChildEntity("My Soul To Take Original Mix", 317);
				ChildChildEntity mySoulToTakeRadio = new ChildChildEntity("My Soul To Take Radio Edit", 208);
				ChildChildEntity raggamuffinOriginal = new ChildChildEntity("Raggamuffin Original Mix", 346);
				ChildChildEntity raggamuffinRadio = new ChildChildEntity("Raggamuffin RadioEdit", 218);

				projectOneAlbum.addTrack(prelude);
				projectOneAlbum.addTrack(lifeBeyondEarth);
				projectOneAlbum.addTrack(theWorldIsYours);
				projectOne.addAlbum(projectOneAlbum);
				
				entityManager.persist(prelude);
				entityManager.persist(lifeBeyondEarth);
				entityManager.persist(theWorldIsYours);
				entityManager.persist(projectOneAlbum);
				entityManager.persist(projectOne);
				
				spaceshipEp.addTrack(spaceship);
				weStayUpEp.addTrack(westayup);
				streamOfBloodEp.addTrack(streamOfBlood);
				streamOfBloodEp.addTrack(phonePrank);
				psykoPunkz.addAlbum(spaceshipEp);
				psykoPunkz.addAlbum(weStayUpEp);
				psykoPunkz.addAlbum(streamOfBloodEp);
				
				entityManager.persist(spaceship);
				entityManager.persist(westayup);
				entityManager.persist(streamOfBlood);
				entityManager.persist(phonePrank);
				entityManager.persist(spaceshipEp);
				entityManager.persist(weStayUpEp);
				entityManager.persist(streamOfBloodEp);
				entityManager.persist(psykoPunkz);
				
				murderEp.addTrack(murderOriginial);
				murderEp.addTrack(murderRadio);
				rockNowEp.addTrack(rockNowOriginal);
				rockNowEp.addTrack(rockNowEdit);
				mySoulToTakeEp.addTrack(mySoulToTakeOriginal);
				mySoulToTakeEp.addTrack(mySoulToTakeRadio);
				mySoulToTakeEp.addTrack(raggamuffinOriginal);
				mySoulToTakeEp.addTrack(raggamuffinRadio);
				adaro.addAlbum(murderEp);
				adaro.addAlbum(rockNowEp);
				adaro.addAlbum(mySoulToTakeEp);
				
				entityManager.persist(murderOriginial);
				entityManager.persist(murderRadio);
				entityManager.persist(rockNowOriginal);
				entityManager.persist(rockNowEdit);
				entityManager.persist(mySoulToTakeOriginal);
				entityManager.persist(mySoulToTakeRadio);
				entityManager.persist(raggamuffinOriginal);
				entityManager.persist(raggamuffinRadio);
				entityManager.persist(murderEp);
				entityManager.persist(rockNowEp);
				entityManager.persist(mySoulToTakeEp);
				entityManager.persist(adaro);
				
				parentEntityList.add(projectOne);
				parentEntityList.add(psykoPunkz);
				parentEntityList.add(adaro);
				
				childEntityList.add(mySoulToTakeEp);
				childEntityList.add(rockNowEp);
				childEntityList.add(murderEp);
				childEntityList.add(streamOfBloodEp);
				childEntityList.add(weStayUpEp);
				childEntityList.add(spaceshipEp);
				childEntityList.add(projectOneAlbum);
				
				childChildEntityList.add(mySoulToTakeRadio);
				childChildEntityList.add(mySoulToTakeOriginal);
				childChildEntityList.add(rockNowEdit);
				childChildEntityList.add(murderRadio);
				childChildEntityList.add(murderOriginial);
				childChildEntityList.add(phonePrank);
				childChildEntityList.add(streamOfBlood);
				childChildEntityList.add(westayup);
				childChildEntityList.add(spaceship);
				childChildEntityList.add(theWorldIsYours);
				childChildEntityList.add(lifeBeyondEarth);
				childChildEntityList.add(prelude);
				childChildEntityList.add(raggamuffinRadio);
				childChildEntityList.add(raggamuffinOriginal);
				childChildEntityList.add(rockNowOriginal);
			
				transaction.commit();
				
				
				
			} finally {
				if (transaction.isActive())
					transaction.rollback();
			}

		} finally {
			
		}

	}
	
	@Test
	public void getAllArtists() throws EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ParentEntity.class).generateQuery();
		ArrayList<ParentEntity> resultList = (ArrayList<ParentEntity>) entityManager.createQuery(query).getResultList();
		Assert.assertTrue(resultList.containsAll(parentEntityList) && parentEntityList.containsAll(resultList));
	}
	
	@Test
	public void getAllAlbums() throws EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ChildEntity.class).generateQuery();
		ArrayList<ChildEntity> resultList = (ArrayList<ChildEntity>) entityManager.createQuery(query).getResultList();
		Assert.assertTrue(resultList.containsAll(childEntityList) && childEntityList.containsAll(resultList));
	}
	
	@Test
	public void getAllTracks() throws EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ChildChildEntity.class).generateQuery();
		ArrayList<ChildChildEntity> resultList = (ArrayList<ChildChildEntity>) entityManager.createQuery(query).getResultList();
		Assert.assertTrue(resultList.containsAll(childChildEntityList) && childChildEntityList.containsAll(resultList));
	}
	
	@Test
	public void joinChildEntityFromParent () throws EntityNotMappedException, EntityNotNamedException, JcdException {
		String query =factory.startFrom(ParentEntity.class)
				.joins(factory.startJoin(ChildEntity.class, "childEntityRelation")).generateQuery();
		ArrayList<Object[]> list = (ArrayList<Object[]>) entityManager.createQuery(query).getResultList();
		/*for (Object object[] : list) {
			if (object[0] instanceof ParentEntity || object[1] instanceof ParentEntity) {
				System.out.println("Parent");
			} if (object[0] instanceof ChildEntity || object[1] instanceof ChildEntity) {
				System.out.println("Child");
			} else {
				System.out.println("WHAT");
			}
		}
		*/
		
		Assert.assertEquals(childEntityList.size(), list.size());
	}
	
	@Test
	public void joinChildChildEntityFromChildFromParent () throws EntityNotMappedException, EntityNotNamedException, JcdException {
		String query =factory.startFrom(ParentEntity.class)
				.joins(factory.startJoin(ChildEntity.class, "childEntityRelation")
						.joins(factory.startJoin(ChildChildEntity.class, "childChildEntityRelation"))).generateQuery();
		ArrayList<Object[]> list = (ArrayList<Object[]>) entityManager.createQuery(query).getResultList();
		
		
		Assert.assertEquals(childChildEntityList.size(), list.size());
	}
	
	@Test
	public void whereLikeTestArtist() throws TypeMismatchException, InvalidComparisonException, AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException, EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ParentEntity.class)
				.where("artist", String.class).like("Ada_o").generateQuery();
		ParentEntity entity = (ParentEntity) entityManager.createQuery(query).getSingleResult();
		
		ParentEntity original = null;
		for (ParentEntity parentEntity : parentEntityList) {
			if (parentEntity.getArtist().equals("Adaro")) {
				original = parentEntity;
			}
		}
		
		Assert.assertEquals(original, entity);
	}
	
	@Test
	public void testLt() throws TypeMismatchException, InvalidComparisonException, AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException, EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ParentEntity.class)
				.where("amountOfAlbums", Integer.class).lt(15).generateQuery();
		ParentEntity result = (ParentEntity) entityManager.createQuery(query).getSingleResult();
		
		ParentEntity original = null;
		for (ParentEntity parentEntity : parentEntityList) {
			if (parentEntity.getArtist().equals("Project One")) {
				original = parentEntity;
			}
		}
		
		Assert.assertEquals(original, result);;
	}
	
	@Test
	public void testGt() throws TypeMismatchException, InvalidComparisonException, AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException, EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ParentEntity.class)
				.where("amountOfAlbums", Integer.class).gt(27).generateQuery();
		ParentEntity result = (ParentEntity) entityManager.createQuery(query).getSingleResult();
		
		ParentEntity original = null;
		for (ParentEntity parentEntity : parentEntityList) {
			if (parentEntity.getArtist().equals("Adaro")) {
				original = parentEntity;
			}
		}
		
		Assert.assertEquals(original, result);
	}

	@Test
	public void testEqString() throws EntityNotMappedException, EntityNotNamedException, TypeMismatchException, AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException {
		String query = factory.startFrom(ParentEntity.class).where("artist", String.class).eq("Adaro").generateQuery();
		ParentEntity entity = (ParentEntity) entityManager.createQuery(query).getSingleResult();
		ParentEntity original = null;
		for (ParentEntity parentEntity : parentEntityList) {
			if (parentEntity.getArtist().equals("Adaro")) {
				original = parentEntity;
			}
		}
		//Assert.assertTrue(list1.containsAll(list2.containsAll) && list2.containsAll(list1.containsAll));
		Assert.assertEquals(original, entity);
		
		System.out.println(query);
	}
	
	@Test
	public void testEqInteger() throws EntityNotMappedException, EntityNotNamedException, TypeMismatchException, AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException {
		String query = factory.startFrom(ParentEntity.class)
				.where("amountOfAlbums", Integer.class).eq(1).generateQuery();
		ParentEntity entity = (ParentEntity) entityManager.createQuery(query).getSingleResult();
		
		ParentEntity original = null;
		for (ParentEntity parentEntity : parentEntityList) {
			if (parentEntity.getArtist().equals("Project One")) {
				original = parentEntity;
			}
		}
		//Assert.assertTrue(list1.containsAll(list2.containsAll) && list2.containsAll(list1.containsAll));
		Assert.assertEquals(original, entity);
		
		System.out.println(query);
	}
	
	
	@Test
	public void nestedJoinWhereTest() throws EntityNotMappedException, EntityNotNamedException, JcdException {
		String query = factory.startFrom(ParentEntity.class).where("artist", String.class).eq("Adaro")
				.where("amountOfAlbums", Integer.class).gt(20)
				.joins(factory.startJoin(ChildEntity.class, "childEntityRelation").where("albumName", String.class)
						.eq("Murder EP")
						.joins(factory.startJoin(ChildChildEntity.class, "childChildEntityRelation")))
				.generateQuery();
		System.out.println(query);
		ArrayList<Object[]> resultList = (ArrayList<Object[]>) entityManager.createQuery(query).getResultList();
		
		Assert.assertEquals(2, resultList.size());
		
	}
	
	// Marcos example query generators

	@Test
	public void fromTest() throws EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ParentEntity.class).generateQuery();
		System.out.println(query);
	}

	@Test
	public void fromWhereTest() throws EntityNotMappedException, EntityNotNamedException, TypeMismatchException, AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException {
		String query = factory.startFrom(ParentEntity.class)
				.where("intColumn", Integer.class).eq(5).generateQuery();
		System.out.println(query);
	}

	@Test
	public void fromJoinTest() throws JcdException {
		String query = factory.startFrom(ParentEntity.class)
				.joins(factory.startJoin(ChildEntity.class, "childEntityRelation")).generateQuery();
		System.out.println(query);
	}

	@Test
	public void nestedJoinTest() throws EntityNotMappedException, EntityNotNamedException, JcdException {
		String query = factory.startFrom(ParentEntity.class)
				.joins(factory.startJoin(ChildEntity.class, "childEntityRelation")
						.joins(factory.startJoin(ChildChildEntity.class, "childChildEntityRelation")))
				.generateQuery();
		System.out.println(query);
	}

	@Test
	public void whereModifierTest() throws EntityNotMappedException, EntityNotNamedException, JcdException {
		String query = factory.startFrom(ParentEntity.class)
				.where("intColumn", Integer.class).eq(5).where("stringColumn", String.class).addModifier("TRIM").addModifier("LOWER").eq("ente").generateQuery();
		System.out.println(query);
	}

	/*@Test
	public void nestedJoinWhereTest() throws EntityNotMappedException, EntityNotNamedException, JcdException {
		String query = factory.startFrom(ParentEntity.class).where("intColumn", Integer.class).eq(5)
				.where("stringColumn", String.class).eq("elephant")
				.joins(factory.startJoin(ChildEntity.class, "childEntityRelation").where("shortColumn", Short.class)
						.eq((short) 42).where("longColumn", long.class).gt((long) 5)
						.joins(factory.startJoin(ChildChildEntity.class, "childChildEntityRelation")))
				.generateQuery();
		System.out.println(query);
	}*/
	
	@Test
	public void whereLikeTest() throws TypeMismatchException, InvalidComparisonException, AttributeNotFoundException, ColumnNotMappedException, ColumnNotNamedException, EntityNotMappedException, EntityNotNamedException {
		String query = factory.startFrom(ParentEntity.class)
				.where("stringColumn", String.class).like("elep_nt").generateQuery();
		System.out.println(query);
	}
}
