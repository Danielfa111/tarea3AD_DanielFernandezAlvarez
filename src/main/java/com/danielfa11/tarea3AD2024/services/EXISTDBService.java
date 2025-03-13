package com.danielfa11.tarea3AD2024.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import com.danielfa11.tarea3AD2024.repositorios.EXISTDBRepository;

@Service
public class EXISTDBService {

	@Autowired
	private EXISTDBRepository existdbRepository;
	
	public void store(String fichero, String coleccion, String peregrino) {
		existdbRepository.store(fichero, coleccion, peregrino);
	}
	
	public Collection getOrCreateCollection(String collectionUri){
		try {
			return existdbRepository.getOrCreateCollection(collectionUri);
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getAllResourcesFromCollection(String coleccion){
		return existdbRepository.getAllResourcesFromCollection(coleccion);
	}
}
