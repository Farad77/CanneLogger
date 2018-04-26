package com.dreams.cannelogger.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Gadpasou on 24/04/2018.
 */
@Entity
@XmlRootElement
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	public int getCtics() {
		return ctics;
	}

	public void setCtics(int ctics) {
		this.ctics = ctics;
	}

	private int ctics;
	private String details;
	@ManyToOne
	private Chauffeur chauffeur;
	@ManyToOne
	private Chargeur chargeur;
	@ManyToOne
	private Coupeur coupeur;
	public Coupeur getCoupeur() {
		return coupeur;
	}

	public void setCoupeur(Coupeur coupeur) {
		this.coupeur = coupeur;
	}

	private Integer tonnage;
	private Float richesse;

	public Ticket() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Chauffeur getChauffeur() {
		return chauffeur;
	}

	public void setChauffeur(Chauffeur chauffeur) {
		this.chauffeur = chauffeur;
	}

	public Chargeur getChargeur() {
		return chargeur;
	}

	public void setChargeur(Chargeur chargeur) {
		this.chargeur = chargeur;
	}

	public int getTonnage() {
		return tonnage;
	}

	public void setTonnage(int tonnage) {
		this.tonnage = tonnage;
	}

	public Float getRichesse() {
		return richesse;
	}

	public void setRichesse(Float richesse) {
		this.richesse = richesse;
	}

	@ManyToOne
	private Planteur planteur;

	public Planteur getPlanteur() {
		return planteur;
	}

	public void setPlanteur(Planteur planteur) {
		this.planteur = planteur;
	}

}
