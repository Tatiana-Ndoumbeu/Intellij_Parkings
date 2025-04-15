package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.MecanicienRepository;
import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;

import java.io.IOException;

public class MecanicienUseCase {
    private final MecanicienRepository mecanicienRepository;

    public MecanicienUseCase(MecanicienRepository mecanicienRepository) {
        this.mecanicienRepository = mecanicienRepository;
    }

    public boolean creerMecanicien(Mecanicien mecanicien) throws InterruptedException, IOException {

        //REGLES METIERS ICI

        return mecanicienRepository.insert(mecanicien);
    }

    public Mecaniciens afficherMecaniciens() throws InterruptedException, IOException {
        return mecanicienRepository.select();
    }
}
