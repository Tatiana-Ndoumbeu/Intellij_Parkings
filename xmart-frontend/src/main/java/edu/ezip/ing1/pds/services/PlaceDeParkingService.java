package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.business.dto.PlacesDeParkings;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.apiRequest.InsertClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.SelectAllClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class PlaceDeParkingService {
    private final static String LoggingLabel = "FrontEnd - PlaceDeParkingService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String PlaceDeParkingsToBeInserted = "places-to-be-inserted.yaml";

    final String insertRequestOrder = "INSERT_PLACE_DE_PARKING";
    final String selectRequestOrder = "SELECT_ALL_PLACE_DE_PARKING";


    private final NetworkConfig networkConfig;

    public PlaceDeParkingService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertPlaceDeParkings(PlaceDeParking placeDeParking) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final PlacesDeParkings guys = ConfigLoader.loadConfig(PlacesDeParkings.class, PlaceDeParkingsToBeInserted);

        int birthdate = 0;
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(placeDeParking);
            logger.trace("PlaceDeParking with its JSON face : {}", jsonifiedGuy);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(insertRequestOrder);
            request.setRequestContent(jsonifiedGuy);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertClientRequest clientRequest = new InsertClientRequest(
                    networkConfig,
                    birthdate++, request, placeDeParking, requestBytes);
            clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final PlaceDeParking guy = (PlaceDeParking)clientRequest.getInfo();
            logger.debug("Thread {} complete : {} {} {} {} --> {}",
                    clientResponse.getThreadName(),
                    guy.getIdPlace(), guy.getEmplacement(), guy.getTypePlace(),guy.getStatutPlace(),
                    clientResponse.getResult());
        }
    }

    public PlacesDeParkings selectPlaceDeParkings() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectAllClientRequest clientRequest = new SelectAllClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes, PlacesDeParkings.class);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            PlacesDeParkings result=  (PlacesDeParkings) joinedClientRequest.getResult();
            logger.debug("palce de parking got  {} complete.", result);
            return result;
        }
        else {
            logger.error("No apiRequest found");
            return null;
        }
    }

}
