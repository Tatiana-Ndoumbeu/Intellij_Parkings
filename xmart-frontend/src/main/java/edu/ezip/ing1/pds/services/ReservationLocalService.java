package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.api.ReservationLocalRepository;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.apiRequest.DeleteClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.InsertClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.SelectAllClientRequest;
//import edu.ezip.ing1.pds.requests.apiRequest.DeleteClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ReservationLocalService implements ReservationLocalRepository {

    private final static String LoggingLabel = "FrontEnd - ReservationLocalService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_RESERVATION_LOCAL";
    final String selectRequestOrder = "SELECT_ALL_RESERVATION_LOCAL";
    final String deleteRequestOrder = "DELETE_RESERVATION_LOCAL";
    final String SelectMoisRequestOrder = "SELECT_ALL_RESERVATION_LOCAL_MOIS";

    private final NetworkConfig networkConfig;

    public ReservationLocalService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }


    public Boolean insert(ReservationLocal reservation) throws InterruptedException, IOException {

        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedReservation = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservation);
        logger.trace("ReservationLocal JSON : {}", jsonifiedReservation);

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(insertRequestOrder);
        request.setRequestContent(jsonifiedReservation);

        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig, birthdate++, request, reservation, requestBytes);
        clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final ReservationLocal result = (ReservationLocal) clientResponse.getInfo();

            logger.debug("Thread {} terminé : Reservation du local {} du {} au {} --> {}",
                    clientResponse.getThreadName(),
                    result.getNumLocal(), result.getDateDebut(), result.getDateFin(),
                    clientResponse.getResult());
        }

        return true;
    }


    public ReservationLocaux selectAll() throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);

        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final SelectAllClientRequest<ReservationLocaux> clientRequest = new SelectAllClientRequest<>(
                networkConfig,
                birthdate++, request, null, requestBytes, ReservationLocaux.class
        );
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();

            logger.debug("Thread {} terminé.", joinedClientRequest.getThreadName());
            return (ReservationLocaux) joinedClientRequest.getResult();
        } else {
            logger.error("Aucune réservation trouvée.");
            return null;
        }
    }


    public ReservationLocaux selectParMois(int annee, int mois) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();


        final ReservationLocalParMois params = new ReservationLocalParMois();
        params.setAnnee(annee);
        params.setMois(mois);


        final JsonNode requestBody = objectMapper.valueToTree(params);

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(SelectMoisRequestOrder);
        request.setRequestBody(requestBody);


        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);


        final SelectAllClientRequest<ReservationLocaux> clientRequest =
                new SelectAllClientRequest<>(
                        networkConfig,
                        birthdate++,
                        request,
                        null,
                        requestBytes,
                        ReservationLocaux.class
                );

        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} terminé.", joinedClientRequest.getThreadName());
            return (ReservationLocaux) joinedClientRequest.getResult();
        } else {
            logger.error("Aucune réservation récupérée.");
            return null;
        }
    }
    public Boolean delete(ReservationLocal reservationLocal) throws InterruptedException, IOException {
        try {
            final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
            final ObjectMapper objectMapper = new ObjectMapper();
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();

            request.setRequestId(requestId);
            request.setRequestOrder(deleteRequestOrder);
            final String jsonifiedLocal = objectMapper.writeValueAsString(reservationLocal);
            request.setRequestContent(jsonifiedLocal);

            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
            final DeleteClientRequest<ReservationLocal> clientRequest = new DeleteClientRequest<>(
                    networkConfig, 0, request, reservationLocal, requestBytes);

            clientRequests.push(clientRequest);

            while (!clientRequests.isEmpty()) {
                final ClientRequest clientResponse = clientRequests.pop();
                clientResponse.join();
                logger.debug("Suppression terminée pour reservationlocal {} du {}: {}",
                        reservationLocal.getTypeLocal(), reservationLocal.getDateDebut(), clientResponse.getResult());
            }
            return true;
        } catch (Exception e) {
            logger.error("Error suppression", e);
            return false;
        }
    }


}
