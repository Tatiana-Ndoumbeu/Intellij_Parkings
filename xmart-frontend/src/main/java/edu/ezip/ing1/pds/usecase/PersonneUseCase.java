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

        if (personne.getTelephone() == null || !personne.getTelephone().matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 10 chiffres et commencer par un 0.");
        }
        if (personne.getMail() == null || !personne.getMail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("L'adresse e-mail n'est pas valide.");
        }
        if (personne.getCodePostal() != null && !personne.getCodePostal().matches("\\d{5}")) {
            throw new IllegalArgumentException("Le code postal doit contenir exactement 5 chiffres.");
        }
        personneRepository.insertPersonnes(personne);
    }

    public Personnes afficherPersonnes() throws InterruptedException, IOException {
        return personneRepository.selectPersonnes();
    }

    public Set<Personne> getALLPersonnes() throws InterruptedException, IOException {
        return personneRepository.findAll();

    }

    public boolean updatePersonne(Personne personne) throws InterruptedException, IOException {
        if (personne.getTelephone() == null || !personne.getTelephone().matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 10 chiffres et commencer par un 0.");
        }
        if (personne.getMail() == null || !personne.getMail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("L'adresse e-mail n'est pas valide.");
        }
        if (personne.getCodePostal() != null && !personne.getCodePostal().matches("\\d{5}")) {
            throw new IllegalArgumentException("Le code postal doit contenir exactement 5 chiffres.");
        }
        return personneRepository.updatePersonnes(personne);
    }

    public boolean deletePersonne(Personne personne) throws InterruptedException, IOException {
        return personneRepository.deletePersonnes(personne);
    }






}
