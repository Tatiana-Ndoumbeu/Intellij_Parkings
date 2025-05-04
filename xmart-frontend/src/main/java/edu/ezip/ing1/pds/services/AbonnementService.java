package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.api.AbonnementRepository;
import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Abonnements;
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

public class AbonnementService implements AbonnementRepository {
    private final static String LoggingLabel = "FrontEnd - AbonnementService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_ABONNEMENT";
    final String selectRequestOrder = "SELECT_ALL_ABONNEMENTS";
    final String suppRequestOrder = "DELETE_ABONNEMENT";
    final String updateRequestOrder = "UPDATE_ABONNEMENT";
    final String SelectAboXRequestOrder = "SELECT_TYPE_ABONNEMENT";

    private final NetworkConfig networkConfig;

    public AbonnementService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    @Override
    public boolean save(Abonnement abonnement) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        int birthdate = 0;

        System.out.println("abonnement = " + abonnement);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(abonnement);
        logger.trace("Abonnement with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(insertRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                birthdate++, request, abonnement, requestBytes);
        clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final Abonnement guy = (Abonnement) clientResponse.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientResponse.getThreadName(),
                    guy.getIdAbonnement(), guy.getPrix(), guy.getStatutAbonnement(),
                    clientResponse.getResult());
            return true;
        }
        return false;
    }

    @Override
    public Abonnements findAll() throws InterruptedException, IOException {
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
        birthdate++;
        final SelectAllClientRequest clientRequest = new SelectAllClientRequest(
                networkConfig,
                birthdate, request, null, requestBytes, Abonnements.class);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Abonnements) joinedClientRequest.getResult();
        } else {
            logger.error("No Abonnements found");
            return null;
        }
    }

    public Abonnements findAbonnementX(String id_personne) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();

        Abonnement abonnement = new Abonnement();
        abonnement.setIdPersonne(id_personne);

        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(abonnement);
        logger.debug("Abonnement with its JSON face : {}", jsonifiedGuy);

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(SelectAboXRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        birthdate++;
        final SelectAllClientRequest clientRequest = new SelectAllClientRequest(
                networkConfig,
                birthdate, request, null, requestBytes, Abonnements.class);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Abonnements) joinedClientRequest.getResult();
        } else {
            logger.error("No Abonnements found");
            return null;
        }
    }

    @Override
    public boolean deleteById(String idAbonnement) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();

        Abonnement abonnement = new Abonnement();
        abonnement.setIdAbonnement(idAbonnement);

        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(abonnement);
        logger.debug("Abonnement with its JSON face : {}", jsonifiedGuy);

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(suppRequestOrder);
        request.setRequestContent(jsonifiedGuy);

        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest<>(
                networkConfig,
                requestId.hashCode(), request, null, requestBytes);
        clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            logger.debug("Thread {} complete. Deleted Abonnement with ID: {} --> {}",
                    clientResponse.getThreadName(), idAbonnement, clientResponse.getResult());
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Abonnement abonnement) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedAbonnement = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(abonnement);
        logger.trace("Abonnement en JSON : {}", jsonifiedAbonnement);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(updateRequestOrder);
        request.setRequestContent(jsonifiedAbonnement);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                requestId.hashCode(), request, abonnement, requestBytes);
        clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();

            final Abonnement updatedAbonnement = (Abonnement) clientResponse.getInfo();
            logger.debug("Thread {} complete: {} {} {} {} {} {} --> {}",
                    clientResponse.getThreadName(),
                    updatedAbonnement.getIdAbonnement(), updatedAbonnement.getTypeAbonnement(),
                    updatedAbonnement.getPrix(), updatedAbonnement.getStatutAbonnement(),
                    updatedAbonnement.getDateDebut(), updatedAbonnement.getDateFin(),
                    clientResponse.getResult());
            return true;
        }
        return false;
    }
}
