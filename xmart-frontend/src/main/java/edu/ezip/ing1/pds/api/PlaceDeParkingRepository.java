package edu.ezip.ing1.pds.api;


import edu.ezip.ing1.pds.business.dto.PlaceDeParking;

import java.util.List;

public interface PlaceDeParkingRepository {
    // Save a new PlaceDeParking
    boolean save(PlaceDeParking place);

    // Find a PlaceDeParking by its ID
    PlaceDeParking findById(String id);

    // Find a list of all places
    List<PlaceDeParking> findAll();

    // Update an existing PlaceDeParking
    boolean update(PlaceDeParking place);

    // Delete a PlaceDeParking by its ID
    boolean delete(String id);

    // Reserve a PlaceDeParking
    boolean reserver(String id);

    // Assign a vehicle to a PlaceDeParking
    boolean affecter(String id, String vehicleId);
}

