//package com.ibm.webdev.app.jpa.service.test;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
//import com.ibm.webdev.app.jpa.dao.Movie;
//
//
//public class MovieServiceTest {
//	@BeforeClass
//	public static void setUpBeforeClass()throws Exception{
//	}
//	@Test
//	public void save(){
//	EntityManagerFactory factory=Persistence.createEntityManagerFactory("myjpatest");
//	EntityManager em=factory.createEntityManager();
//	em.getTransaction().begin();//开始事物
//	em.persist(new Movie("创梦网络")); //持久化到数据库.persist:持久化
//	em.getTransaction().commit();
//	em.close();
//	factory.close();
//	}
//}
