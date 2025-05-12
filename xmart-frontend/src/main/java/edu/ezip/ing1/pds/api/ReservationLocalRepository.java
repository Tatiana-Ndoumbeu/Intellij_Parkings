package edu.ezip.ing1.pds.api;
import edu.ezip.ing1.pds.business.dto.ReservationLocal;
import edu.ezip.ing1.pds.business.dto.ReservationLocalParMoisMap;
import edu.ezip.ing1.pds.business.dto.ReservationLocaux;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReservationLocalRepository {

    Boolean insert(ReservationLocal reservationLocal) throws InterruptedException, IOException;

    ReservationLocaux selectAll() throws InterruptedException, IOException;

    ReservationLocaux selectParMois(int annee, int mois) throws InterruptedException, IOException;

    Boolean delete(ReservationLocal reservationLocal) throws InterruptedException, IOException;
}

