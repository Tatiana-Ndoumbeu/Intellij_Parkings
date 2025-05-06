package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.PersonneRepository;
import edu.ezip.ing1.pds.business.dto.Personnes;
import edu.ezip.ing1.pds.business.dto.Personne;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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

    public Set<Personne> getALLPersonnes() throws InterruptedException, IOException {
        return personneRepository.findAll();

    }

    public boolean updatePersonne(Personne personne) throws InterruptedException, IOException {
        return personneRepository.updatePersonnes(personne);
    }

    public boolean deletePersonne(Personne personne) throws InterruptedException, IOException {
        return personneRepository.deletePersonnes(personne);
    }






}
