package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.ReservationLocalRepository;
import edu.ezip.ing1.pds.business.dto.ReservationLocal;
import edu.ezip.ing1.pds.business.dto.ReservationLocalParMoisMap;
import edu.ezip.ing1.pds.business.dto.ReservationLocaux;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReservationLocalUseCase {
    private final ReservationLocalRepository reservationLocalRepository;

    public ReservationLocalUseCase(ReservationLocalRepository reservationLocalRepository) {
        this.reservationLocalRepository = reservationLocalRepository;
    }

    public boolean createReservationLocal(ReservationLocal reservationLocal) throws InterruptedException, IOException {
        return reservationLocalRepository.insert(reservationLocal);
    }

    public ReservationLocaux getAllReservationsLocal() throws InterruptedException, IOException {
        return reservationLocalRepository.selectAll();
    }
    public ReservationLocalParMoisMap getReservationsParMois(int annee, int mois) throws InterruptedException, IOException {
        return reservationLocalRepository.selectParMois(annee, mois);
    }
}