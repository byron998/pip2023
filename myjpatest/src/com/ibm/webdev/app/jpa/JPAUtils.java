package com.ibm.webdev.app.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JPAUtils {
	
	private static EntityManagerFactory em;
	
	static {
		em = Persistence.createEntityManagerFactory("myPersistUnit");
	}

	/**
	 * @return EntityManager
	 */
	public static EntityManager getEntityManager() {
		return em.createEntityManager();
	}
	
}