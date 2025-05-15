package edu.ezip.ing1.pds.usecase;



import edu.ezip.ing1.pds.api.PlaceDeParkingRepository;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlaceDeParkingUseCaseTest {

    private PlaceDeParkingUseCase useCase;

    // Données simulées en mémoire
    private final Map<String, PlaceDeParking> fakeDatabase = new HashMap<>();

    @BeforeEach
    void setUp() {
        PlaceDeParkingRepository stubRepo = new PlaceDeParkingRepository() {
            @Override
            public boolean save(PlaceDeParking place) {
                if (place == null || place.getIdPlace() == null) return false;
                fakeDatabase.put(place.getIdPlace(), place);
                return true;
            }

            @Override
            public PlaceDeParking findById(String id) {
                return fakeDatabase.get(id);
            }

            @Override
            public List<PlaceDeParking> findAll() {
                return new ArrayList<>(fakeDatabase.values());
            }

            @Override
            public boolean update(PlaceDeParking place) {
                if (place == null || !fakeDatabase.containsKey(place.getIdPlace())) return false;
                fakeDatabase.put(place.getIdPlace(), place);
                return true;
            }

            @Override
            public boolean delete(String id) {
                return fakeDatabase.remove(id) != null;
            }

            @Override
            public boolean reserver(String id) {
                return fakeDatabase.containsKey(id);
            }

            @Override
            public boolean affecter(String id, String vehicleId) {
                return fakeDatabase.containsKey(id) && vehicleId != null;
            }
        };

        useCase = new PlaceDeParkingUseCase(stubRepo);
    }

    @Test
    void testAddPlaceDeParking() {
        PlaceDeParking place = new PlaceDeParking();
        place.setIdPlace("P1");
        place.setEmplacement("A1");

        boolean result = useCase.addPlaceDeParking(place);
        assertTrue(result);
    }

    @Test
    void testGetPlaceDeParkingById_found() {
        PlaceDeParking place = new PlaceDeParking();
        place.setIdPlace("P2");
        useCase.addPlaceDeParking(place);

        PlaceDeParking found = useCase.getPlaceDeParkingById("P2");
        assertNotNull(found);
    }

    @Test
    void testGetPlaceDeParkingById_notFound() {
        assertNull(useCase.getPlaceDeParkingById("UNKNOWN"));
    }

    @Test
    void testGetAllPlacesDeParking() {
        useCase.addPlaceDeParking(new PlaceDeParking() {{
            setIdPlace("P3");
        }});
        useCase.addPlaceDeParking(new PlaceDeParking() {{
            setIdPlace("P4");
        }});

        List<PlaceDeParking> list = useCase.getAllPlacesDeParking();
        assertEquals(2, list.size());
    }

    @Test
    void testUpdatePlaceDeParking_success() {
        PlaceDeParking place = new PlaceDeParking();
        place.setIdPlace("P5");
        place.setEmplacement("A5");
        useCase.addPlaceDeParking(place);

        place.setEmplacement("Updated A5");
        boolean result = useCase.updatePlaceDeParking(place);
        assertTrue(result);
        assertEquals("Updated A5", useCase.getPlaceDeParkingById("P5").getEmplacement());
    }

    @Test
    void testDeletePlaceDeParking_success() {
        PlaceDeParking place = new PlaceDeParking();
        place.setIdPlace("P6");
        useCase.addPlaceDeParking(place);

        boolean result = useCase.deletePlaceDeParking("P6");
        assertTrue(result);
        assertNull(useCase.getPlaceDeParkingById("P6"));
    }

    @Test
    void testDeletePlaceDeParking_notFound() {
        boolean result = useCase.deletePlaceDeParking("NOT_EXIST");
        assertFalse(result);
    }
}
