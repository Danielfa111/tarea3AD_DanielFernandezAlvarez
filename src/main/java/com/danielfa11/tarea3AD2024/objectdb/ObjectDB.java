package com.danielfa11.tarea3AD2024.objectdb;

import org.springframework.stereotype.Component;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


@Component
public class ObjectDB {

	private static ObjectDB instance;
	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("./src/main/resources/objectdb/odb.odb");
	private static EntityManager em;
	
	private ObjectDB() {
		
	}
	
	public static ObjectDB getObjectDB() {
		if(instance==null) {
			instance=new ObjectDB();
			return instance;
		}
		return instance;
	}
	
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
