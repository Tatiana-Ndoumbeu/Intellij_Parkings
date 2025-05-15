package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.AbonnementRepository;
import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Abonnements;
import edu.ezip.ing1.pds.business.dto.Personne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AbonnementUseCaseTest {

    private AbonnementUseCase useCase;

    @BeforeEach
    void setUp() {
        AbonnementRepository stubRepo = new AbonnementRepository() {
            @Override
            public boolean save(Abonnement abonnement) {
                return true;
            }

            @Override
            public Abonnements findAll() {
                Abonnements abonnements = new Abonnements();
                abonnements.add(new Abonnement());
                return abonnements;
            }

            @Override
            public boolean deleteById(String idAbonnement) {
                return "ABO-123".equals(idAbonnement);
            }

            @Override
            public boolean update(Abonnement abonnement) {
                return abonnement != null;
            }

            @Override
            public Abonnement findAbonnementX(Personne personne) {
                Abonnement abo = new Abonnement();
                abo.setIdPersonne(personne.getIdPersonne());
                return abo;
            }

            @Override
            public Abonnement findOneAbonnement(String id_personne) {
                Abonnement abo = new Abonnement();
                abo.setIdPersonne(id_personne);
                return abo;
            }
        };

        useCase = new AbonnementUseCase(stubRepo);
    }

    @Test
    void testCreateAbonnement_success() throws IOException, InterruptedException {
        Abonnement abonnement = new Abonnement();
        assertTrue(useCase.createAbonnement(abonnement));
    }

    @Test
    void testGetAllAbonnements() throws IOException, InterruptedException {
        Abonnements result = useCase.getAllAbonnements();
        assertNotNull(result);
        assertFalse(result.getAbonnements().isEmpty());
    }

    @Test
    void testDeleteAbonnementById_success() throws IOException, InterruptedException {
        assertTrue(useCase.deleteAbonnementById("ABO-123"));
        assertFalse(useCase.deleteAbonnementById("UNKNOWN"));
    }

    @Test
    void testUpdateAbonnement_success() throws IOException, InterruptedException {
        Abonnement abonnement = new Abonnement();
        assertTrue(useCase.updateAbonnement(abonnement));
    }

    @Test
    void testFindAbonnementX_returnsExpected() throws IOException, InterruptedException {
        Personne p = new Personne();
        p.setIdPersonne("P1");
        Abonnement result = useCase.findAbonnementX(p);
        assertNotNull(result);
        assertEquals("P1", result.getIdPersonne());
    }

    @Test
    void testFindOneAbonnement_returnsExpected() throws IOException, InterruptedException {
        Abonnement result = useCase.findOneAbonnement("P2");
        assertNotNull(result);
        assertEquals("P2", result.getIdPersonne());
    }
}
