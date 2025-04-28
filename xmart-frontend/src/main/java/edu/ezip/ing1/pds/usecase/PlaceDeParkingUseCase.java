package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.PlaceDeParkingRepository;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;

import java.util.List;

public class PlaceDeParkingUseCase {
    private final PlaceDeParkingRepository repository;

    public PlaceDeParkingUseCase(PlaceDeParkingRepository repository) {
        this.repository = repository;
    }


    public boolean addPlaceDeParking(PlaceDeParking placeDeParking) {
        return repository.save(placeDeParking);
    }


    public boolean updatePlaceDeParking(PlaceDeParking placeDeParking) {
        return repository.update(placeDeParking);
    }


    public boolean deletePlaceDeParking(String placeId) {
        return repository.delete(placeId);
    }


    public List<PlaceDeParking> getAllPlacesDeParking() {
        return repository.findAll();  // prendre toutes les places du repository
    }


    public PlaceDeParking getPlaceDeParkingById(String placeId) {
        return repository.findById(placeId);  // rechercher des places par ID
    }
}

