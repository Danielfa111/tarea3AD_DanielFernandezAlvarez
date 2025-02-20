package com.danielfa11.tarea3AD2024.repositorios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.danielfa11.tarea3AD2024.db4o.Db4o;
import com.danielfa11.tarea3AD2024.modelo.ConjuntoContratado;
import com.danielfa11.tarea3AD2024.modelo.Servicio;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;


@Repository
public class DB4ORepository {

	@Autowired
	private Db4o db4o;
	
	private ObjectContainer db = db4o.getDb4o();
	
	public void storeServicio(Servicio servicio) {
		db.store(servicio);
	}
	
	public void storeConjuntoContratado(ConjuntoContratado cc) {
		db.store(cc);
	}
	
	public Servicio retrieveServicio(Long id) {
		Query query=db.query();
        query.constrain(Servicio.class);
        query.descend("id").constrain(id);
        ObjectSet<Servicio> result=query.execute();
        if(result.size()>0) {
        	Servicio servicio = result.getFirst();
        	return servicio;
        }
        else {
        	return null;
        }
	}
	
	public ConjuntoContratado retrieveConjuntoContratado(Long id) {
		Query query=db.query();
        query.constrain(ConjuntoContratado.class);
        query.descend("id").constrain(id);
        ObjectSet<ConjuntoContratado> result=query.execute();
        if(result.size()>0) {
        	ConjuntoContratado cc = result.getFirst();
        	return cc;
        }
        else {
        	return null;
        }
	}
	
	public void updateServicio(Long id, Servicio servicio) {
		List<Servicio> result = db.query(new Predicate<Servicio>() {
			public boolean match(Servicio servicio) {
				return servicio.getId().equals(id);
			}
		});
		
		Servicio found = (Servicio) result.get(0);
		
		found.setConjuntos(servicio.getConjuntos());
		found.setNombre(servicio.getNombre());
		found.setPrecio(servicio.getPrecio());
		found.setParadas(servicio.getParadas());
		
		db.store(found);
		
	}
	
	public void updateConjuntoContratado(Long id, ConjuntoContratado cc) {
		List<ConjuntoContratado> result = db.query(new Predicate<ConjuntoContratado>() {
			public boolean match(ConjuntoContratado cc) {
				return cc.getId().equals(id);
			}
		});
		
		ConjuntoContratado found = (ConjuntoContratado) result.get(0);	
		
		found.setIdEstancia(id);
		found.setExtra(cc.getExtra());
		found.setModoPago(cc.getModoPago());
		found.setPrecioTotal(cc.getPrecioTotal());
		found.setServicios(cc.getServicios());
		
		db.store(found);
		
	}
	
}
