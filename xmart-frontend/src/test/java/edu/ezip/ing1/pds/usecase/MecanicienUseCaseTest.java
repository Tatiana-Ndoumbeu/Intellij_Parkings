package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.MecanicienRepository;
import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MecanicienUseCaseTest {

    private MecanicienUseCase useCase;

    @BeforeEach
    void setUp() {

        MecanicienRepository stubRepo = new MecanicienRepository() {
            @Override
            public Boolean insert(Mecanicien mecanicien) {
                return true;
            }

            @Override
            public Mecaniciens select() {
                return new Mecaniciens(); // retourne une liste vide
            }

            @Override
            public Boolean delete(Mecanicien mecano) {
                return true;
            }
        };

        useCase = new MecanicienUseCase(stubRepo);
    }

    @Test
    void testCreerMecanicien_valid() throws IOException, InterruptedException {
        Mecanicien m = new Mecanicien( "Jean", "Durand", "0612345678", true, "moteur", "jean.durand@email.com");
        boolean result = useCase.creerMecanicien(m);
        assertTrue(result);
    }

    @Test
    void testCreerMecanicien_invalidTelephone() {
        Mecanicien m = new Mecanicien( "Paul", "Martin", "12345678", true, "freins", "paul@email.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.creerMecanicien(m);
        });

        assertEquals("Le numéro de téléphone doit contenir exactement 10 chiffres et commencer par un 0.", exception.getMessage());
    }

    @Test
    void testCreerMecanicien_invalidMail() {
        Mecanicien m = new Mecanicien( "Julie", "Robert", "0612345678", true, "carrosserie","MEC-003");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.creerMecanicien(m);
        });

        assertEquals("L'adresse e-mail n'est pas valide.", exception.getMessage());
    }

    @Test
    void testAfficherMecaniciens() throws IOException, InterruptedException {
        Mecaniciens result = useCase.afficherMecaniciens();
        assertNotNull(result); // même s’il est vide
    }

    @Test
    void testSupprimerMecanicien() throws IOException, InterruptedException {
        Mecanicien m = new Mecanicien();
        m.setNom("MEC-004");

        boolean result = useCase.supprimerMecanicien(m);
        assertTrue(result);
    }
}
