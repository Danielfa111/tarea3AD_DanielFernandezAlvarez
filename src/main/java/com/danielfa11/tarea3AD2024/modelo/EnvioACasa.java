package com.danielfa11.tarea3AD2024.modelo;

import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EnvioACasa extends Servicio {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEnvio;
	private double peso;
	private int[] volumen = new int[3];
	private boolean urgente = false;
	
	@Embedded
	private Direccion direccion;
	
	
	private Long parada;
	
	// Constructores
	
	public EnvioACasa(Long id, String nombre, double precio, Long idEnvio, double peso, int[] volumen, boolean urgente,
			Direccion direccion, Long parada) {
		super(id, nombre, precio);
		this.idEnvio = idEnvio;
		this.peso = peso;
		this.volumen = volumen;
		this.urgente = urgente;
		this.direccion = direccion;
		this.parada = parada;
	}
	
	public EnvioACasa(Long id, String nombre, double precio) {
		super(id, nombre, precio);
	}

	public EnvioACasa() {
		
	}
	
	// Getters y Setters

	public Long getIdEnvio() {
		return idEnvio;
	}

	public void setIdEnvio(Long idEnvio) {
		this.idEnvio = idEnvio;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public int[] getVolumen() {
		return volumen;
	}

	public void setVolumen(int[] volumen) {
		this.volumen = volumen;
	}

	public boolean isUrgente() {
		return urgente;
	}

	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public Long getParada() {
		return parada;
	}

	public void setParada(Long parada) {
		this.parada = parada;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	//Metodos
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(volumen);
		result = prime * result + Objects.hash(direccion, idEnvio, parada, peso, urgente);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnvioACasa other = (EnvioACasa) obj;
		return Objects.equals(direccion, other.direccion) && Objects.equals(idEnvio, other.idEnvio)
				&& Objects.equals(parada, other.parada)
				&& Double.doubleToLongBits(peso) == Double.doubleToLongBits(other.peso) && urgente == other.urgente
				&& Arrays.equals(volumen, other.volumen);
	}

	@Override
	public String toString() {
		return "EnvioACasa [idEnvio=" + idEnvio + ", peso=" + peso + ", volumen=" + Arrays.toString(volumen)
				+ ", urgente=" + urgente + ", direccion=" + direccion + ", parada=" + parada + "]";
	}
	
	
	
	

}
