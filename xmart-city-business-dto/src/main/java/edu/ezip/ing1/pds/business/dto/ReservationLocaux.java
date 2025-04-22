package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class ReservationLocaux {
    @JsonProperty("ReservationLocaux")
    private Set<ReservationLocal> reservationLocaux = new LinkedHashSet<>();

 /*  @JsonProperty("reservationsParJour")
    private Map<LocalDate, List<String>> reservationsParJour;

    public Map<LocalDate, List<String>> getReservationsParJour() {
        return reservationsParJour;
    }

    public void setReservationsParJour(Map<LocalDate, List<String>> reservationsParJour) {
        this.reservationsParJour = reservationsParJour;
    }
*/
    public Set<ReservationLocal> getReservationLocaux() {
        return reservationLocaux;
    }

    public void setReservationLocaux(Set<ReservationLocal> reservationLocaux) {
        this.reservationLocaux = reservationLocaux;
    }


    public ReservationLocaux add(final ReservationLocal reservationLocal) {
        reservationLocaux.add(reservationLocal);
        return this;
    }


    @Override
    public String toString() {
        return "ReservationLocaux{" +
                "reservationLocaux=" + reservationLocaux +
                '}';
    }
}
