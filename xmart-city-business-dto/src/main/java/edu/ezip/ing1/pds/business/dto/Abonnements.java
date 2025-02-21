package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Abonnements {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Abonnements")
    private  Set<Abonnement> abonnements = new LinkedHashSet<Abonnement>();

    public Set<Abonnement> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(Set<Abonnement> Abonnements) {
        this.abonnements = Abonnements;
    }

    public final Abonnements add (final Abonnement Abonnement) {
        abonnements.add(Abonnement);
        return this;
    }

    @Override
    public String toString() {
        return "Abonnements{" +
                "Abonnements=" + abonnements +
                '}';
    }
}
