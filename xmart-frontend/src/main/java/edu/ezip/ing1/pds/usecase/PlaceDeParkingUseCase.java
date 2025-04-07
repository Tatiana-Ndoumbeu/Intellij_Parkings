package edu.ezip.ing1.pds.usecase;

import edu.ezip.ing1.pds.api.PlaceDeParkingRepository;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;

import java.util.List;

public class PlaceDeParkingUseCase {
    private final PlaceDeParkingRepository repository;

    // Constructor injects the repository
    public PlaceDeParkingUseCase(PlaceDeParkingRepository repository) {
        this.repository = repository;
    }

    // Method to add a new PlaceDeParking
    public boolean addPlaceDeParking(PlaceDeParking placeDeParking) {
        return repository.save(placeDeParking);  // Save the new place to the repository
    }

    // Method to update an existing PlaceDeParking
    public boolean updatePlaceDeParking(PlaceDeParking placeDeParking) {
        return repository.update(placeDeParking);  // Update the place in the repository
    }

    // Method to delete a PlaceDeParking by ID
    public boolean deletePlaceDeParking(String placeId) {
        return repository.delete(placeId);  // Delete the place by ID
    }

    // Method to fetch all PlaceDeParking
    public List<PlaceDeParking> getAllPlacesDeParking() {
        return repository.findAll();  // Get all the places from the repository
    }

    // Method to find a PlaceDeParking by its ID
    public PlaceDeParking getPlaceDeParkingById(String placeId) {
        return repository.findById(placeId);  // Fetch a place by its ID from the repository
    }
}

