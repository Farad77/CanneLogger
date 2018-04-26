package com.dreams.cannelogger.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Gadpasou on 26/04/2018.
 */
@Entity
@DiscriminatorValue("Coupeur")
public class Coupeur extends Agent {
}
