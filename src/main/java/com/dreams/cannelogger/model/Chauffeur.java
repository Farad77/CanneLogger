package com.dreams.cannelogger.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Gadpasou on 24/04/2018.
 */
@Entity
@DiscriminatorValue("Chauffeur")
public class Chauffeur extends Agent {

}
