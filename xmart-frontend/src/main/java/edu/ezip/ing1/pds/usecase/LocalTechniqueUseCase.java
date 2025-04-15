package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.LocalTechniqueRepository;
import edu.ezip.ing1.pds.business.dto.LocalTechnique;
import edu.ezip.ing1.pds.business.dto.LocalTechniques;

import java.io.IOException;

public class LocalTechniqueUseCase {
    private final LocalTechniqueRepository localTechniqueRepository;

    public LocalTechniqueUseCase(LocalTechniqueRepository localTechniqueRepository) {
        this.localTechniqueRepository = localTechniqueRepository;
    }

    public boolean creerLocalTechnique(LocalTechnique local) throws InterruptedException, IOException {

        //REGLES METIERS ICI

        return localTechniqueRepository.insert(local);
    }

    public LocalTechniques afficherLocaux() throws InterruptedException, IOException {
        return localTechniqueRepository.select();
    }
    public LocalTechniques afficherLocauxDispo() throws InterruptedException, IOException {
        return localTechniqueRepository.selectDispo();
    }


    public boolean supprimerLocal(LocalTechnique local) throws InterruptedException, IOException {

        //REGLES METIERS ICI

        return localTechniqueRepository.delete(local);
    }

    public boolean modifierDisponibilite(LocalTechnique local) throws InterruptedException, IOException {

        //REGLES METIERS ICI

        return   localTechniqueRepository.update(local);
    }
}
