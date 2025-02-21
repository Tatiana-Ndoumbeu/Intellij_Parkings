package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class Vehicles {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Vehicules")
    private  Set<Vehicle> vehicles = new LinkedHashSet<Vehicle>();

    public Set<Vehicle> getVehicules() {
        return vehicles;
    }

    public void setVehicules(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public final Vehicles add (final Vehicle Vehicle) {
        vehicles.add(Vehicle);
        return this;
    }

    @Override
    public String toString() {
        return "Vehicules{" +
                "Vehicules=" + vehicles +
                '}';
    }
}
