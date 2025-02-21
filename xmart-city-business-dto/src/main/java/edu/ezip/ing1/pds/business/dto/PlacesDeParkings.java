package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class PlacesDeParkings {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("PlaceDeParkings")
    private  Set<PlaceDeParking> placeDeParkings = new LinkedHashSet<PlaceDeParking>();

    public Set<PlaceDeParking> getPlaceDeParkings() {
        return placeDeParkings;
    }

    public void setPlaceDeParkings(Set<PlaceDeParking> PlaceDeParkings) {
        this.placeDeParkings = PlaceDeParkings;
    }

    public final PlacesDeParkings add (final PlaceDeParking PlaceDeParking) {
        placeDeParkings.add(PlaceDeParking);
        return this;
    }

    @Override
    public String toString() {
        return "PlaceDeParkings{" +
                "PlaceDeParkings=" + placeDeParkings +
                '}';
    }
}
