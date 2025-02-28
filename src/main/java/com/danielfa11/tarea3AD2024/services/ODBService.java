package com.danielfa11.tarea3AD2024.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielfa11.tarea3AD2024.modelo.EnvioACasa;
import com.danielfa11.tarea3AD2024.repositorios.ODBRepository;

@Service
public class ODBService {

	@Autowired
	private ODBRepository odbRepository;
	
	public void storeEnvio(EnvioACasa envio) {
			
		odbRepository.storeEnvio(envio);
	}
	
}
