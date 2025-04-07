package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.ing1.pds.api.PlaceDeParkingRepository;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.business.dto.PlacesDeParkings;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.apiRequest.DeleteClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.InsertClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.SelectAllClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;

public class PlaceDeParkingService implements PlaceDeParkingRepository {
    private final static String LoggingLabel = "FrontEnd - PlaceDeParkingService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_PLACE_DE_PARKING";
    final String selectRequestOrder = "SELECT_ALL_PLACE_DE_PARKING";
    final String updatePlaceDeParkingOrder = "UPDATE_PLACE_DE_PARKING";
    final String deletePlaceDeParkingOrder = "DELETE_PLACE_DE_PARKING";

    private final NetworkConfig networkConfig;

    public PlaceDeParkingService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    @Override
    public boolean save(PlaceDeParking placeDeParking) {
        try {
            logger.debug("Saving PlaceDeParking: {}", placeDeParking);
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedPlace = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(placeDeParking);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(insertRequestOrder);
            request.setRequestContent(jsonifiedPlace);

            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertClientRequest clientRequest = new InsertClientRequest(
                    networkConfig,
                    requestId.hashCode(), request, placeDeParking, requestBytes);
            clientRequest.join();

            logger.debug("PlaceDeParking saved: {}", placeDeParking);
            return true;
        } catch (Exception e) {
            logger.error("Error saving PlaceDeParking", e);
            return false;
        }
    }

    @Override
    public PlaceDeParking findById(String idPlace) {
        // This method would require an actual request to the backend to find a PlaceDeParking by ID.
        try {
            logger.debug("Finding PlaceDeParking by ID: {}", idPlace);
            final ObjectMapper objectMapper = new ObjectMapper();
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(selectRequestOrder);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);

            final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
            final SelectAllClientRequest clientRequest = new SelectAllClientRequest(
                    networkConfig,
                    requestId.hashCode(), request, null, requestBytes, PlacesDeParkings.class);
            clientRequest.join();

            // In a real case, you would filter by idPlace and return the matching object
            PlacesDeParkings placesDeParkings = (PlacesDeParkings) clientRequest.getResult();
            for (PlaceDeParking place : placesDeParkings.getPlaceDeParkings()) {
                if (place.getIdPlace().equals(idPlace)) {
                    return place;
                }
            }

            return null;
        } catch (Exception e) {
            logger.error("Error finding PlaceDeParking by ID", e);
            return null;
        }
    }

    @Override
    public boolean delete(String idPlace) {
        try {
            logger.debug("Deleting PlaceDeParking with ID: {}", idPlace);
            final ObjectMapper objectMapper = new ObjectMapper();
            PlaceDeParking placeDeParking = new PlaceDeParking();
            placeDeParking.setIdPlace(idPlace);

            final String jsonifiedPlace = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(placeDeParking);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(deletePlaceDeParkingOrder);
            request.setRequestContent(jsonifiedPlace);

            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final DeleteClientRequest clientRequest = new DeleteClientRequest(
                    networkConfig,
                    requestId.hashCode(), request, placeDeParking, requestBytes);
            clientRequest.join();

            logger.debug("PlaceDeParking deleted with ID: {}", idPlace);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting PlaceDeParking", e);
            return false;
        }
    }

    @Override
    public boolean reserver(String id) {
        return false;
    }

    @Override
    public boolean affecter(String id, String vehicleId) {
        return false;
    }

    @Override
    public boolean update(PlaceDeParking placeDeParking) {
        try {
            logger.debug("Updating PlaceDeParking: {}", placeDeParking);
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedPlace = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(placeDeParking);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(updatePlaceDeParkingOrder);
            request.setRequestContent(jsonifiedPlace);

            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertClientRequest clientRequest = new InsertClientRequest(
                    networkConfig,
                    requestId.hashCode(), request, placeDeParking, requestBytes);
            clientRequest.join();

            logger.debug("PlaceDeParking updated: {}", placeDeParking);
            return true;
        } catch (Exception e) {
            logger.error("Error updating PlaceDeParking", e);
            return false;
        }
    }

    @Override
    public List<PlaceDeParking> findAll() {
        try {
            logger.debug("Retrieving all PlaceDeParking objects");
            final ObjectMapper objectMapper = new ObjectMapper();
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(selectRequestOrder);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);

            final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
            final SelectAllClientRequest clientRequest = new SelectAllClientRequest(
                    networkConfig,
                    requestId.hashCode(), request, null, requestBytes, PlacesDeParkings.class);
            clientRequest.join();

            PlacesDeParkings placesDeParkings = (PlacesDeParkings) clientRequest.getResult();
            return placesDeParkings.getPlaceDeParkings(); // Assuming getPlaces() returns a List<PlaceDeParking>
        } catch (Exception e) {
            logger.error("Error retrieving all PlaceDeParking objects", e);
            return null;
        }
    }
}
