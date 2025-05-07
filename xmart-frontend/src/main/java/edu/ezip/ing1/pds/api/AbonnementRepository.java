package edu.ezip.ing1.pds.api;

import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Abonnements;
import edu.ezip.ing1.pds.business.dto.Personne;


import java.io.IOException;

public interface AbonnementRepository {
    boolean save(Abonnement abonnement) throws InterruptedException, IOException;
    Abonnements findAll() throws InterruptedException, IOException;
    boolean deleteById(String idAbonnement) throws InterruptedException, IOException;
    boolean update(Abonnement abonnement) throws InterruptedException, IOException;
    Abonnement findAbonnementX(Personne personne) throws InterruptedException, IOException;
    Abonnement findOneAbonnement(String id_personne) throws InterruptedException, IOException;
}
