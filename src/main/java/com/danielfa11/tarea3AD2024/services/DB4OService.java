package com.danielfa11.tarea3AD2024.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielfa11.tarea3AD2024.modelo.ConjuntoContratado;
import com.danielfa11.tarea3AD2024.modelo.Servicio;
import com.danielfa11.tarea3AD2024.repositorios.DB4ORepository;

@Service
public class DB4OService {

	@Autowired
	private DB4ORepository db4oRepository;
	
	public void storeServicio(Servicio servicio) {
		db4oRepository.storeServicio(servicio);;
	}
	
	public void storeConjuntoContratado(ConjuntoContratado cc) {
		db4oRepository.storeConjuntoContratado(cc);;
	}
	
	public Servicio retrieveServicio(Long id) {
		return db4oRepository.retrieveServicio(id);
	}
	
	public List<Servicio> retrieveAllServicio(){
		return db4oRepository.retrieveAllServicio();
	}
	
	public ConjuntoContratado retrieveConjuntoContratado(Long id) {
		return db4oRepository.retrieveConjuntoContratado(id);
	}
	
	public List<ConjuntoContratado> retrieveAllConjuntoContratado(){
		return db4oRepository.retrieveAllConjuntoContratado();
	}
	
	public void updateServicio(Long id, Servicio servicio) {
		db4oRepository.updateServicio(id, servicio);
	}
	
	public void updateConjuntoContratado(Long id, ConjuntoContratado cc) {
		db4oRepository.updateConjuntoContratado(id, cc);
	}
	
	public Long findServicioLastId() {
		return db4oRepository.findServicioLastId();
	}
	
	public Long findConjuntoContratadoLastId() {
		return db4oRepository.findConjuntoContratadoLastId();
	}
	
	
	
}
