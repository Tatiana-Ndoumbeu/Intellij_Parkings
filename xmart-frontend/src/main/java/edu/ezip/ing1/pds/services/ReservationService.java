package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.api.ReservationRepository;
import edu.ezip.ing1.pds.business.dto.*;
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

public class ReservationService implements ReservationRepository {

    private final static String LoggingLabel = "FrontEnd - ReservationService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    private final String selectZoneSpecRequestOrder = "SELECT_ZONE_SPE_PLACE_DE_PARKING";
    private final String selectRequestOrder = "SELECT_ALL_RESERVATIONS";
    private final String insertRequestOrder = "INSERT_RESERVATION";

    private final NetworkConfig networkConfig;

    public ReservationService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    @Override
    public PlacesDeParkings selectZoneSpeciale() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();

        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectZoneSpecRequestOrder);

        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);

        final SelectAllClientRequest clientRequest = new SelectAllClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes, PlacesDeParkings.class
        );
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            PlacesDeParkings result = (PlacesDeParkings) joinedClientRequest.getResult();
            logger.debug("place spéciales de parking got  {} complete.", result);
            return result;
        } else {
            logger.error("No apiRequest found");
            return null;
        }
    }

    @Override
    public ReservationsResquests selectReservations() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();

        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);

        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);

        final SelectAllClientRequest clientRequest = new SelectAllClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes, ReservationsResquests.class
        );
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            ReservationsResquests result = (ReservationsResquests) joinedClientRequest.getResult();

            return result;
        } else {
            logger.error("No apiRequest found");
            return null;
        }
    }

    @Override
    public boolean insertReservation(ReservationRequest reservation) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        int birthdate = 0;

        logger.trace("Reservation with its JSON face : {}", reservation);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservation);
        logger.trace("Reservation JSON : {}", jsonifiedGuy);

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(insertRequestOrder);
        request.setRequestContent(jsonifiedGuy);

        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                birthdate++, request, reservation, requestBytes
        );
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final ReservationRequest res = (ReservationRequest) clientResponse.getInfo();
            logger.debug("Thread {} complete : {} {} --> {}",
                    clientResponse.getThreadName(),
                    res.getIdReservation(), res.getHeureEntree(),
                    clientResponse.getResult());
            return true;
        }else {
            return false;
        }
    }
}
