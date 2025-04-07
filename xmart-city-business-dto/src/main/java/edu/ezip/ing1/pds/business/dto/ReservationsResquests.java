package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class ReservationsResquests {

    @JsonProperty("reservationRequests")
    private Set<ReservationRequest> reservationRequests = new LinkedHashSet<>();


    public Set<ReservationRequest> getReservationRequests() {
        return reservationRequests;
    }

    public void setReservationsRequests(Set<ReservationRequest> reservations) {
        this.reservationRequests = reservations;
    }

    public ReservationsResquests add(final ReservationRequest reservation) {
        reservationRequests.add(reservation);
        return this;
    }

    @Override
    public String toString() {
        return "Reservations{" +
                "reservation Request"+reservationRequests+
                '}';
    }
}
