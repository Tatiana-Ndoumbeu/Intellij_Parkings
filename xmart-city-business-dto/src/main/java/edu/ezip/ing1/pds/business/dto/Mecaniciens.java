package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Mecaniciens {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Mecaniciens")
    private Set<Mecanicien> mecaniciens = new LinkedHashSet<Mecanicien>();

    public Set<Mecanicien> getMecaniciens() {
        return mecaniciens;
    }

    public void setMecaniciens(Set<Mecanicien> Mecaniciens) {
        this.mecaniciens = Mecaniciens;
    }

    public final Mecaniciens add (final Mecanicien Mecanicien) {
        mecaniciens.add(Mecanicien);
        return this;
    }

    @Override
    public String toString() {
        return "Mecaniciens{" +
                "Mecaniciens=" + mecaniciens +
                '}';
    }
}
