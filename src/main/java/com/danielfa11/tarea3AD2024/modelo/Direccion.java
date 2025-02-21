package com.danielfa11.tarea3AD2024.modelo;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class Direccion implements Serializable {

	private static final long serialVersionUID = 1L;

	private String direccion;
	private String localidad;
	
	// Constructores
	
	public Direccion(String direccion, String localidad) {
		this.direccion = direccion;
		this.localidad = localidad;
	}
	
	public Direccion() {

	}
	
	// Getters y Setters

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	// Metodos
	
	@Override
	public int hashCode() {
		return Objects.hash(direccion, localidad);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Direccion other = (Direccion) obj;
		return Objects.equals(direccion, other.direccion) && Objects.equals(localidad, other.localidad);
	}

	@Override
	public String toString() {
		return "Direccion [direccion=" + direccion + ", localidad=" + localidad + "]";
	}
	
	
	
	
	
	
	
}
