package com.dreams.cannelogger.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Gadpasou on 24/04/2018.
 */
@Entity
@DiscriminatorValue("Planteur")
public class Planteur extends Agent {

	private String numeroPlanteur;
	public String getNumeroPlanteur() {
		return numeroPlanteur;
	}

	public void setNumeroPlanteur(String numeroPlanteur) {
		this.numeroPlanteur = numeroPlanteur;
	}

	private String nomResponsable;

	private String nomSocial;

	public Planteur() {

	}

	public String getNomResponsable() {
		return nomResponsable;
	}

	public void setNomResponsable(String nomResponsable) {
		this.nomResponsable = nomResponsable;
	}

	public String getNomSocial() {
		return nomSocial;
	}

	public void setNomSocial(String nomSocial) {
		this.nomSocial = nomSocial;
	}
}
