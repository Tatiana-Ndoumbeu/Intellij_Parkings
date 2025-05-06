package edu.ezip.ing1.pds.api;

import edu.ezip.ing1.pds.business.dto.Personnes;
import edu.ezip.ing1.pds.business.dto.Personne;

import java.io.IOException;
import java.util.Set;

public interface PersonneRepository {
    void insertPersonnes(Personne personne) throws InterruptedException, IOException;
    Personnes selectPersonnes() throws InterruptedException, IOException;
    boolean updatePersonnes(Personne personne) throws InterruptedException, IOException;
    boolean deletePersonnes(Personne personne) throws InterruptedException, IOException;
    Set<Personne> findAll();

}
