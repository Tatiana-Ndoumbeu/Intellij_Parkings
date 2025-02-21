package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Vehicle;
import edu.ezip.ing1.pds.business.dto.Vehicles;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
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

public class VehicleService {
    private final static String LoggingLabel = "FrontEnd - VehicleService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String vehiclesToBeInserted = "vehicules-to-be-inserted.yaml";

    final String insertRequestOrder = "INSERT_VEHICULE";
    final String selectRequestOrder = "SELECT_ALL_VEHICULES";

    private final NetworkConfig networkConfig;

    public VehicleService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertVehicles(Vehicle  vehicle) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        int birthdate = 0;
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vehicle);
            logger.trace("Vehicle with its JSON face : {}", jsonifiedGuy);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(insertRequestOrder);
            request.setRequestContent(jsonifiedGuy);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertClientRequest clientRequest = new InsertClientRequest(
                    networkConfig,
                    birthdate++, request, vehicle, requestBytes);
            clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientresponse = clientRequests.pop();
            clientresponse.join();
            final Vehicle guy = (Vehicle)clientresponse.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientresponse.getThreadName(),
                    guy.getNumPlaque(), guy.getMarque(), guy.getType(),
                    clientresponse.getResult());
        }
    }

    public Vehicles selectVehicles() throws InterruptedException, IOException {
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
                birthdate++, request, null, requestBytes, Vehicles.class);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Vehicles) joinedClientRequest.getResult();
        }
        else {
            logger.error("No Vehicles found");
            return null;
        }
    }

}
