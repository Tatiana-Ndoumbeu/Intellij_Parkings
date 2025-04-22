package edu.ezip.ing1.pds.business.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

public class ReservationMapper {

    private static final SimpleDateFormat dateFormat =  new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

    public static Reservation toReservation(ReservationRequest request) {
        Reservation reservation = new Reservation();

        try {
            reservation.setIdReservation(request.getIdReservation());
            reservation.setDateReservation(LocalDate.parse(request.getDateReservation()));
            reservation.setHeure(LocalTime.parse(request.getHeure()));
            reservation.setDateEntree(dateFormat.parse(request.getDateEntree()));
            reservation.setDateSortie(dateFormat.parse(request.getDateSortie()));
            reservation.setHeureEntree(timeFormat.parse(request.getHeureEntree()));
            reservation.setHeureSortie(timeFormat.parse(request.getHeureSortie()));
            reservation.setIdPersonne(request.getIdPersonne());
            reservation.setPosition(request.getIdPlace());
            reservation.setIdPlace(request.getPersonne());
            reservation.setPlaceDeParking(request.getPlaceDeParking());
        } catch (ParseException e) {
            e.printStackTrace(); // Tu peux aussi lever une RuntimeException ici si tu préfères
        }

        return reservation;
    }

    // Convert ReservationsResquests (a set of ReservationRequest) to Reservations (a set of Reservation)
    public static Reservations mapToReservations(ReservationsResquests reservationRequests) {
        Reservations reservations = new Reservations();

        for (ReservationRequest request : reservationRequests.getReservationRequests()) {
            Reservation reservation = toReservation(request); // Use the existing method to convert
            reservations.add(reservation);
        }

        return reservations;
    }


}

