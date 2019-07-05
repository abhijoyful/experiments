package com.bank.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Utility to get entity manager (persistence context) for each db operation.
 * @author Abhishek Malik
 *
 */
public class EntityManagerUtil {
	private static final EntityManagerFactory entityManagerFactory;
	static {
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("accountdb");

		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager();

	}
}
