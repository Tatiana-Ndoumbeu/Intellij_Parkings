package edu.ezip.ing1.pds.api;

import edu.ezip.ing1.pds.business.dto.Personnes;
import edu.ezip.ing1.pds.business.dto.Personne;

import java.io.IOException;

public interface PersonneRepository {
    void insertPersonnes(Personne personne) throws InterruptedException, IOException;
    Personnes selectPersonnes() throws InterruptedException, IOException;

}
