package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.PersonneRepository;
import edu.ezip.ing1.pds.business.dto.Personnes;
import edu.ezip.ing1.pds.business.dto.Personne;

import java.io.IOException;

public class PersonneUseCase {

    private PersonneRepository personneRepository;

    public PersonneUseCase(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    public void ajouterPersonnes(Personne personne) throws InterruptedException, IOException {
        personneRepository.insertPersonnes(personne);
    }

    public Personnes afficherPersonnes() throws InterruptedException, IOException {
        return personneRepository.selectPersonnes();
    }




}
