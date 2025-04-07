package edu.ezip.ing1.pds.api;



import edu.ezip.ing1.pds.business.dto.*;

import java.io.IOException;

public interface ReservationRepository {
    PlacesDeParkings selectZoneSpeciale() throws InterruptedException, IOException;
    ReservationsResquests selectReservations() throws InterruptedException, IOException;
    boolean insertReservation(ReservationRequest reservation) throws InterruptedException, IOException;
}
