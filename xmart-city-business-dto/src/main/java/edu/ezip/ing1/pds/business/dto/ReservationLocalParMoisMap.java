package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReservationLocalParMoisMap {

    @JsonProperty("map")
    private Map<LocalDate, List<String>> map;

    public Map<LocalDate, List<String>> getMap() {
        return map;
    }

    public void setMap(Map<LocalDate, List<String>> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "ReservationLocalParMoisMap{" +
                "map=" + map +
                '}';
    }
}
