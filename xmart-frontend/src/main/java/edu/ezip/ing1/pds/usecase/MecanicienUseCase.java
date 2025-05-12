package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.MecanicienRepository;
import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;

import java.io.IOException;

public class MecanicienUseCase {
    private final MecanicienRepository mecanicienRepository;

    public MecanicienUseCase(MecanicienRepository mecanicienRepository) {
        this.mecanicienRepository = mecanicienRepository;
    }

    public boolean creerMecanicien(Mecanicien mecanicien) throws InterruptedException, IOException {
        if (mecanicien.getTelephone() == null || !mecanicien.getTelephone().matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 10 chiffres et commencer par un 0.");
        }
        if (mecanicien.getMail() == null || !mecanicien.getMail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("L'adresse e-mail n'est pas valide.");
        }

        return mecanicienRepository.insert(mecanicien);
    }

    public Mecaniciens afficherMecaniciens() throws InterruptedException, IOException {
        return mecanicienRepository.select();
    }
    public boolean supprimerMecanicien(Mecanicien mecano) throws InterruptedException, IOException {


        return mecanicienRepository.delete(mecano);
    }
}
