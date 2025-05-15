package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.PersonneRepository;
import edu.ezip.ing1.pds.business.dto.Personne;
import edu.ezip.ing1.pds.business.dto.Personnes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonneUseCaseTest {

    private PersonneUseCase useCase;

    @BeforeEach
    void setUp() {
        PersonneRepository stubRepo = new PersonneRepository() {
            @Override
            public void insertPersonnes(Personne personne) {
                // do nothing
            }

            @Override
            public Personnes selectPersonnes() {
                return new Personnes();
            }

            @Override
            public boolean updatePersonnes(Personne personne) {
                return true;
            }

            @Override
            public boolean deletePersonnes(Personne personne) {
                return true;
            }

            @Override
            public Set<Personne> findAll() {
                return new HashSet<>();
            }
        };
        useCase = new PersonneUseCase(stubRepo);
    }

    @Test
    void ajouterPersonne_valide_succes() throws IOException, InterruptedException {
        Personne p = new Personne();
        p.setNom("Doe");
        p.setPrenom("John");
        p.setTelephone("0123456789");
        p.setMail("john.doe@email.com");
        p.setCodePostal("75001");

        assertDoesNotThrow(() -> useCase.ajouterPersonnes(p));
    }

    @Test
    void ajouterPersonne_numeroInvalide_exception() {
        Personne p = new Personne();
        p.setTelephone("123456789");
        p.setMail("test@test.com");
        p.setCodePostal("75001");

        assertThrows(IllegalArgumentException.class, () -> useCase.ajouterPersonnes(p));
    }

    @Test
    void updatePersonne_emailInvalide_exception() {
        Personne p = new Personne();
        p.setTelephone("0123456789");
        p.setMail("invalidemail");
        p.setCodePostal("75001");

        assertThrows(IllegalArgumentException.class, () -> useCase.updatePersonne(p));
    }

    @Test
    void updatePersonne_valide_succes() throws IOException, InterruptedException {
        Personne p = new Personne();
        p.setTelephone("0123456789");
        p.setMail("ok@test.fr");
        p.setCodePostal("75000");

        assertTrue(useCase.updatePersonne(p));
    }

    @Test
    void afficherPersonnes_notNull() throws IOException, InterruptedException {
        assertNotNull(useCase.afficherPersonnes());
    }

    @Test
    void getAllPersonnes_notNull() throws IOException, InterruptedException {
        assertNotNull(useCase.getALLPersonnes());
    }

    @Test
    void deletePersonne_success() throws IOException, InterruptedException {
        Personne p = new Personne();
        assertTrue(useCase.deletePersonne(p));
    }
}
