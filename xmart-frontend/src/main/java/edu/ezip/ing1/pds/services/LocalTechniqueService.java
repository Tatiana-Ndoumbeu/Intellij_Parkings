package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;
import edu.ezip.ing1.pds.business.dto.LocalTechnique;
import edu.ezip.ing1.pds.business.dto.LocalTechniques;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.apiRequest.DeleteClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.InsertClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.SelectAllClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class LocalTechniqueService {
    private final static String LoggingLabel = "FrontEnd - LocalTechniqueService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);


    final String insertRequestOrder = "INSERT_LOCAL_T";
    final String selectRequestOrder = "SELECT_ALL_LOCAL_T";
    final String updateRequestOrder = "UPDATE_LOCAL_T";
    final String deleteRequestOrder = "DELETE_LOCAL_T";

    private final NetworkConfig networkConfig;

    public LocalTechniqueService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertLocTechnique(LocalTechnique LocalTechnique) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        int birthdate = 0;
        logger.trace("LocalTechnique with its JSON face : {}", LocalTechnique);
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LocalTechnique);
        logger.trace("LocalTechnique with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(insertRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                birthdate++, request, LocalTechnique, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final LocalTechnique guy = (LocalTechnique) clientResponse.getInfo();
            logger.debug("Thread {} complete : {} {} --> {}",
                    clientResponse.getThreadName(),
                    guy.getNumLocalT(), guy.getDisponibilite(),
                    clientResponse.getResult());
        }
    }

    public LocalTechniques selectLocalTechnique() throws InterruptedException, IOException {
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
                birthdate++, request, null, requestBytes, LocalTechniques.class);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (LocalTechniques) joinedClientRequest.getResult();
        }
        else {
            logger.error("No LocalTechnique found");
            return null;
        }
    }

    public void updateLocTechnique(LocalTechnique localTechnique) throws InterruptedException, IOException {

        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        int birthdate = 0;
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedLocal = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(localTechnique);
        logger.trace("LocalTechnique en JSON : {}", jsonifiedLocal);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(updateRequestOrder);
        request.setRequestContent(jsonifiedLocal);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                birthdate++, request, localTechnique, requestBytes);
        clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();

            final LocalTechnique updatedLocal = (LocalTechnique) clientResponse.getInfo();
            logger.debug("Mise à jour terminée : {} disponibilité = {}",
                    updatedLocal.getNumLocalT(), updatedLocal.getDisponibilite());
        }
    }
    public void deleteLocalTechnique(LocalTechnique localTechnique) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();

        request.setRequestId(requestId);
        request.setRequestOrder(deleteRequestOrder);
        final String jsonifiedLocal = objectMapper.writeValueAsString(localTechnique);
        request.setRequestContent(jsonifiedLocal);

        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        final DeleteClientRequest<LocalTechnique> clientRequest = new DeleteClientRequest<>(
                networkConfig, 0, request, localTechnique, requestBytes);
        clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            logger.debug("Suppression terminée pour LocalTechnique {} : {}",
                    localTechnique.getNumLocalT(), clientResponse.getResult());
        }}
}


