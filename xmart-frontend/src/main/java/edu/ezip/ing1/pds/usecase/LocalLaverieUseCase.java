package edu.ezip.ing1.pds.usecase;


import edu.ezip.ing1.pds.api.LocalLaverieRepository;

import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;

import java.io.IOException;

public class LocalLaverieUseCase {
    private final LocalLaverieRepository localLaverieRepository;

    public LocalLaverieUseCase(LocalLaverieRepository localLaverieRepository) {
        this.localLaverieRepository = localLaverieRepository;
    }

    public boolean createLocalLaverie(LocalLaverie localLaverie) throws InterruptedException, IOException {

        //REGLES METIERS ICI

        return localLaverieRepository.insert(localLaverie);
    }

    public LocalLaveries afficherLocaux() throws InterruptedException, IOException {
        return localLaverieRepository.select();
    }
    public LocalLaveries afficherLocauxDispo() throws InterruptedException, IOException {
        return localLaverieRepository.selectDispo();
    }


    public boolean supprimerLocal(LocalLaverie local) throws InterruptedException, IOException {

        //REGLES METIERS ICI

        return localLaverieRepository.delete(local);
    }

    public boolean modifierDisponibilite(LocalLaverie local) throws InterruptedException, IOException {

        //REGLES METIERS ICI

        return   localLaverieRepository.update(local);
    }
}
