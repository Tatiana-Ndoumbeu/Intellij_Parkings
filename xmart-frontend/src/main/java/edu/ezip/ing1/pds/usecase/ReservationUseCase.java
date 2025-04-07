package edu.ezip.ing1.pds.usecase;


import edu.ezip.ing1.pds.api.ReservationRepository;
import edu.ezip.ing1.pds.business.dto.*;

import java.io.IOException;

public class ReservationUseCase {

    private final ReservationRepository reservationRepository;

    public ReservationUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public PlacesDeParkings getZoneSpeciale() throws InterruptedException, IOException {
        return reservationRepository.selectZoneSpeciale();
    }

    public ReservationsResquests getAllReservations() throws InterruptedException, IOException {
        return reservationRepository.selectReservations();
    }

    public boolean createReservation(ReservationRequest reservation) throws InterruptedException, IOException {
       return reservationRepository.insertReservation(reservation);
    }
}

