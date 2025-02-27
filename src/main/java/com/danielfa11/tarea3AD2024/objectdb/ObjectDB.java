package com.danielfa11.tarea3AD2024.objectdb;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


@Component
public class ObjectDB {

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("/objectdb/odb.odb");
	private EntityManager em;
	
	
	public EntityManager getEntityManager() {
		if(em==null) {
			em=emf.createEntityManager();
			return em;
		}
		return em;
	}
	
	public void closeODB() {
		em.close();
	}
	
}
