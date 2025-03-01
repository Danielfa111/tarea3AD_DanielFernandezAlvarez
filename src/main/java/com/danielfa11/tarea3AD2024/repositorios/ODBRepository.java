package com.danielfa11.tarea3AD2024.repositorios;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.danielfa11.tarea3AD2024.modelo.EnvioACasa;
import com.danielfa11.tarea3AD2024.objectdb.ObjectDB;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class ODBRepository {
	
	private ObjectDB odb = ObjectDB.getObjectDB();
	
	private EntityManager em = odb.getEntityManager();
	
	
	public void storeEnvio(EnvioACasa envio) {
		
		em.getTransaction().begin();
		try {
			em.persist(envio);
			em.getTransaction().commit();
		} catch(EntityExistsException e) {
			em.getTransaction().rollback();
		}
		
	}
	
	public List <EnvioACasa> retrieveAllEnvios(Long idParada) {

		TypedQuery<EnvioACasa> query = em.createQuery("SELECT e FROM EnvioACasa e WHERE e.parada = :idParada", EnvioACasa.class);
		query.setParameter("idParada", idParada);
        List<EnvioACasa> results = query.getResultList();

        return results;
    }
	
}
