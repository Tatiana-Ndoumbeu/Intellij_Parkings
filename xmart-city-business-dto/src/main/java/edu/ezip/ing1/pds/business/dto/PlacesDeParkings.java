package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

import java.util.LinkedHashSet;
import java.util.Set;


public class PlacesDeParkings {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("PlaceDeParkings")
    private List<PlaceDeParking> placeDeParkings = new ArrayList<>();

    public List<PlaceDeParking> getPlaceDeParkings() {
        return placeDeParkings;
    }

    public void setPlaceDeParkings(List<PlaceDeParking> placeDeParkings) {
        this.placeDeParkings = placeDeParkings;
    }

    public final PlacesDeParkings add(final PlaceDeParking placeDeParking) {
        placeDeParkings.add(placeDeParking);
        return this;
    }

    @Override
    public String toString() {
        return "PlaceDeParkings{" +
                "PlaceDeParkings=" + placeDeParkings +
                '}';
    }
}


