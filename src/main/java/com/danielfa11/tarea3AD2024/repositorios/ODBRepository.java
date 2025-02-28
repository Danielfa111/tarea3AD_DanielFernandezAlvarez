package com.danielfa11.tarea3AD2024.repositorios;


import org.springframework.stereotype.Repository;

import com.danielfa11.tarea3AD2024.modelo.EnvioACasa;
import com.danielfa11.tarea3AD2024.objectdb.ObjectDB;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;

@Repository
public class ODBRepository {
	
	private ObjectDB odb = ObjectDB.getObjectDB();
	
	
	private EntityManager em = odb.getEntityManager();
//	private CriteriaBuilder cb = em.getCriteriaBuilder();
	
	
	public void storeEnvio(EnvioACasa envio) {
		
		em.getTransaction().begin();
		try {
			em.persist(envio);
			em.getTransaction().commit();
		} catch(EntityExistsException e) {
			em.getTransaction().rollback();
		}
		
	}
	
//	public EnvioACasa retrieveEnvio(Long id) {
//		em.getTransaction().begin();
//		
//		 CriteriaQuery<EnvioACasa> q = cb.createQuery(EnvioACasa.class);
//		 Root<EnvioACasa> c = q.from(EnvioACasa.class);
//		 q.select(c);
//		 q.where(cb.equal(c.get("id"), id));
//
//		 Selection<EnvioACasa> s = q.getSelection();
//		 
////		 s.getCompoundSelectionItems()
//		
//		
//		
//			TypedQuery<EnvioACasa> query = em.createQuery("SELECT e FROM EnvioACasa e where id=", EnvioACasa.class);
//			List<EnvioACasa> results = query.getResultList();
//			return results.getFirst();
//		
//	}
	
}
