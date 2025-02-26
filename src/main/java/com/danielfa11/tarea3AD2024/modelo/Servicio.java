package com.danielfa11.tarea3AD2024.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Servicio implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected Long id;
	protected String nombre;
	protected double precio;
	
	protected Set<Long> conjuntos = new HashSet<>();
	protected Set<Long> paradas = new HashSet<>();

	// Constructores
	
	public Servicio(Long id, String nombre, double precio) {
			this.id = id;
			this.nombre = nombre;
			this.precio = precio;
		}
	
	public Servicio() {
		
	}
	
	// Getters y Setters
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Set<Long> getConjuntos() {
		return conjuntos;
	}

	public void setConjuntos(Set<Long> conjuntos) {
		this.conjuntos = conjuntos;
	}	
	
	public Set<Long> getParadas() {
		return paradas;
	}

	public void setParadas(Set<Long> paradas) {
		this.paradas = paradas;
	}
	
	// Metodos

	@Override
	public int hashCode() {
		return Objects.hash(conjuntos, id, nombre, paradas, precio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servicio other = (Servicio) obj;
		return Objects.equals(conjuntos, other.conjuntos) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(paradas, other.paradas)
				&& Double.doubleToLongBits(precio) == Double.doubleToLongBits(other.precio);
	}

	@Override
	public String toString() {
		return "Servicio [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", conjuntos=" + conjuntos
				+ ", paradas=" + paradas + "]";
	}

	
	

	
	
	
	
}
