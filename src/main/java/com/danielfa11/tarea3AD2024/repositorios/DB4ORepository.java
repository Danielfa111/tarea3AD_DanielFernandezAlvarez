package com.danielfa11.tarea3AD2024.repositorios;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.danielfa11.tarea3AD2024.db4o.Db4o;
import com.danielfa11.tarea3AD2024.modelo.ConjuntoContratado;
import com.danielfa11.tarea3AD2024.modelo.Servicio;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.query.Predicate;
import com.db4o.query.Query;


@Repository
public class DB4ORepository {
	
	
	private Db4o db4o = Db4o.getDb4o();
	
	private static ObjectContainer db;
	
	public void storeServicio(Servicio servicio) {
		db = db4o.getDb();
		try {
			db.store(servicio);
			db.commit();
		} catch(DatabaseClosedException e) {
			db.rollback();
		} catch(DatabaseReadOnlyException e) {
			db.rollback();
		}
		db4o.closeDb();
		
	}
	
	public void storeConjuntoContratado(ConjuntoContratado cc) {
		
		db = db4o.getDb();
		
		try {
			db.store(cc);
			db.commit();
		} catch(DatabaseClosedException e) {
			db.rollback();
		} catch(DatabaseReadOnlyException e) {
			db.rollback();
		}
		
		db4o.closeDb();
	}
	
	public Servicio retrieveServicio(Long id) {
		
		db = db4o.getDb();
		
		Query query=db.query();
        query.constrain(Servicio.class);
        query.descend("id").constrain(id);
        ObjectSet<Servicio> result=query.execute();
        if(result.size()>0) {
        	Servicio servicio = result.getFirst();
        	db4o.closeDb();
        	return servicio;
        }
        else {
        	db4o.closeDb();
        	return null;
        }
        
        
	}
	
	public ObjectSet<Servicio> retrieveAllServicio(){
		
		db = db4o.getDb();
		
		Query query=db.query();
        query.constrain(Servicio.class);
        ObjectSet<Servicio> result=query.execute();

        
        return result;
	}
	
	public ConjuntoContratado retrieveConjuntoContratado(Long id) {
		
		db = db4o.getDb();
		
		Query query=db.query();
        query.constrain(ConjuntoContratado.class);
        query.descend("id").constrain(id);
        ObjectSet<ConjuntoContratado> result=query.execute();
        if(result.size()>0) {
        	ConjuntoContratado cc = result.getFirst();
        	db4o.closeDb();
        	return cc;
        }
        else {
        	db4o.closeDb();
        	return null;
        }
	}
	
	public ObjectSet<ConjuntoContratado> retrieveAllConjuntoContratado(){
		
		db = db4o.getDb();
		
		Query query=db.query();
        query.constrain(ConjuntoContratado.class);
        ObjectSet<ConjuntoContratado> result=query.execute();
        
		db4o.closeDb();
		return result;
		
	}
	
	public void updateServicio(Long id, Servicio servicio) {
		

	 	db = db4o.getDb();

        Query query = db.query();
        query.constrain(Servicio.class);
        query.descend("id").constrain(id);

        ObjectSet<Servicio> resultado = query.execute();

        Servicio s = resultado.next();

        s.setNombre(servicio.getNombre());
        s.setPrecio(servicio.getPrecio());
        s.getParadas().clear();
		
		for(Long l : servicio.getParadas()) {
			s.getParadas().add(l);
		}

        db.store(s);

	}
	
	public void updateConjuntoContratado(Long id, ConjuntoContratado cc) {
		
	 	db = db4o.getDb();
	 	
        Query query = db.query();
        query.constrain(Servicio.class);
        query.descend("id").constrain(id);

        ObjectSet<ConjuntoContratado> resultado = query.execute();

    	ConjuntoContratado c = resultado.next();

    	c.setIdEstancia(cc.getIdEstancia());
		c.setExtra(cc.getExtra());
		c.setModoPago(cc.getModoPago());
		c.setPrecioTotal(cc.getPrecioTotal());
		c.setServicios(cc.getServicios());
		

        db.store(c);

	}
	
	public Long findServicioLastId() {
        
		db = db4o.getDb();
		
		try {
            Query query = db.query();
            query.constrain(Servicio.class);
            query.descend("id").orderDescending();
            List<Servicio> resultado = query.execute();
            return resultado.get(0).getId() + 1;
        } catch (Exception e) {
        	db4o.closeDb();
            return 1L;
        }
    }
	
	public Long findConjuntoContratadoLastId() {
        
		db = db4o.getDb();
		
		try {
            Query query = db.query();
            query.constrain(ConjuntoContratado.class);
            query.descend("id").orderDescending();
            List<Servicio> resultado = query.execute();
            return resultado.get(0).getId() + 1;
        } catch (Exception e) {
        	db4o.closeDb();
            return 1L;
        }
    }
	
}
