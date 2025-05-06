package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.AbonnementRepository;
import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Abonnements;


import java.io.IOException;

public class AbonnementUseCase {

    private final AbonnementRepository abonnementRepository;

    public AbonnementUseCase(AbonnementRepository abonnementRepository) {
        this.abonnementRepository = abonnementRepository;
    }

    public boolean createAbonnement(Abonnement abonnement) throws InterruptedException, IOException {
       return abonnementRepository.save(abonnement);
    }

    public Abonnements getAllAbonnements() throws InterruptedException, IOException {
        return abonnementRepository.findAll();
    }

    public boolean deleteAbonnementById(String idAbonnement) throws InterruptedException, IOException {
       return abonnementRepository.deleteById(idAbonnement);
    }

    public boolean updateAbonnement(Abonnement abonnement) throws InterruptedException, IOException {
      return   abonnementRepository.update(abonnement);
    }

    public Abonnements findAbonnementX(String id_personne) throws InterruptedException, IOException {
        return abonnementRepository.findAbonnementX(id_personne);
    }

    public Abonnement findOneAbonnement(String id_personne) throws InterruptedException, IOException{
        return abonnementRepository.findOneAbonnement(id_personne);
    }
}
