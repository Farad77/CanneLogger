package com.dreams.cannelogger.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Gadpasou on 24/04/2018.
 */
@Entity
@DiscriminatorValue("Chargeur")
public class Chargeur extends Agent {

}
