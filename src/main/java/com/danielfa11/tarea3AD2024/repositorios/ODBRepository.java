package com.danielfa11.tarea3AD2024.repositorios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.danielfa11.tarea3AD2024.modelo.EnvioACasa;
import com.danielfa11.tarea3AD2024.objectdb.ObjectDB;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

public class ODBRepository {

	@Autowired
	private ObjectDB odb;
	
	private EntityManager em = odb.getEntityManager();
	private CriteriaBuilder cb = em.getCriteriaBuilder();
	
	
	public void storeEnvio(EnvioACasa envio) {
		
		em.getTransaction().begin();
		try {
			em.persist(envio);
			em.getTransaction().commit();
		} catch(EntityExistsException e) {
			em.getTransaction().rollback();
		}
		
	}
	
	public EnvioACasa retrieveEnvio(Long id) {
		em.getTransaction().begin();
		
		 CriteriaQuery<EnvioACasa> q = cb.createQuery(EnvioACasa.class);
		 Root<EnvioACasa> c = q.from(EnvioACasa.class);
		 q.select(c);
		 q.where(cb.equal(c.get("id"), id));

		 Selection<EnvioACasa> s = q.getSelection();
		 
//		 s.getCompoundSelectionItems()
		
		
		
			TypedQuery<EnvioACasa> query = em.createQuery("SELECT e FROM EnvioACasa e where id=", EnvioACasa.class);
			List<EnvioACasa> results = query.getResultList();
			return results.getFirst();
		
	}
	
	public void updateEnvio() {
		
	}
}
