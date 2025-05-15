package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.ReservationRepository;
import edu.ezip.ing1.pds.business.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ReservationUseCaseTest {

    private ReservationUseCase useCase;

    @BeforeEach
    void setUp() {
        ReservationRepository stubRepository = new ReservationRepository() {
            @Override
            public PlacesDeParkings selectZoneSpeciale() {
                PlacesDeParkings places = new PlacesDeParkings();
                PlaceDeParking place = new PlaceDeParking();
                place.setIdPlace("P1");
                place.setStatutPlace("libre");
                place.setTypePlace("handicapé");
                place.setEmplacement("RDC");
                places.add(place);
                return places;
            }

            @Override
            public ReservationsResquests selectReservations() {
                ReservationsResquests reservations = new ReservationsResquests();
                ReservationRequest r = new ReservationRequest();
                r.setIdReservation("RES-001");
                reservations.add(r);
                return reservations;
            }

            @Override
            public boolean insertReservation(ReservationRequest reservation) {
                return reservation != null && reservation.getIdReservation() != null;
            }
        };

        useCase = new ReservationUseCase(stubRepository);
    }

    @Test
    void testGetZoneSpeciale() throws IOException, InterruptedException {
        PlacesDeParkings result = useCase.getZoneSpeciale();
        assertNotNull(result);
        assertEquals(1, result.getPlaceDeParkings().size());
        assertEquals("P1", result.getPlaceDeParkings().get(0).getIdPlace());
    }

    @Test
    void testGetAllReservations() throws IOException, InterruptedException {
        ReservationsResquests result = useCase.getAllReservations();
        assertNotNull(result);
        assertEquals(1, result.getReservationRequests().size());
        assertEquals("RES-001", result.getReservationRequests().stream().findFirst().get().getIdReservation());
    }

    @Test
    void testCreateReservation_success() throws IOException, InterruptedException {
        ReservationRequest reservation = new ReservationRequest();
        reservation.setIdReservation("RES-123");

        boolean result = useCase.createReservation(reservation);

        assertTrue(result);
    }

    @Test
    void testCreateReservation_failure_null() throws IOException, InterruptedException {
        boolean result = useCase.createReservation(null);
        assertFalse(result); // Le stub retourne false si l'objet est null
    }
}
