package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Personnes {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Personnes")
    private  Set<Personne> Personnes = new LinkedHashSet<Personne>();

    public Set<Personne> getPersonnes() {
        return Personnes;
    }

    public void setPersonnes(Set<Personne> Personnes) {
        this.Personnes = Personnes;
    }

    public final Personnes add (final Personne Personne) {
        Personnes.add(Personne);
        return this;
    }

    @Override
    public String toString() {
        return "Personnes{" +
                "Personnes=" + Personnes +
                '}';
    }
}
