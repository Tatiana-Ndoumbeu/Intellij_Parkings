package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.ReservationLocalRepository;
import edu.ezip.ing1.pds.business.dto.ReservationLocal;
import edu.ezip.ing1.pds.business.dto.ReservationLocaux;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationLocalUseCaseTest {

    private ReservationLocalUseCase useCase;

    @BeforeEach
    public void setUp() {
        ReservationLocalRepository stubRepo = new ReservationLocalRepository() {
            @Override
            public Boolean insert(ReservationLocal reservationLocal) {
                return true;
            }

            @Override
            public ReservationLocaux selectAll() {
                return new ReservationLocaux();
            }

            @Override
            public ReservationLocaux selectParMois(int annee, int mois) {
                return new ReservationLocaux();
            }

            @Override
            public Boolean delete(ReservationLocal reservationLocal) {
                return true;
            }
        };

        useCase = new ReservationLocalUseCase(stubRepo);
    }

    @Test
    public void testCreateReservationLocal() throws IOException, InterruptedException {
        ReservationLocal res = new ReservationLocal();
        boolean result = useCase.createReservationLocal(res);
        assertTrue(result);
    }

    @Test
    public void testGetAllReservationsLocal() throws IOException, InterruptedException {
        ReservationLocaux all = useCase.getAllReservationsLocal();
        assertNotNull(all);
    }

    @Test
    public void testGetReservationsParMois() throws IOException, InterruptedException {
        ReservationLocaux filtered = useCase.getReservationsParMois(2025, 5);
        assertNotNull(filtered);
    }

    @Test
    public void testDeleteReservationLocal() throws IOException, InterruptedException {
        ReservationLocal res = new ReservationLocal();
        boolean result = useCase.deleteReservationLocal(res);
        assertTrue(result);
    }
}
