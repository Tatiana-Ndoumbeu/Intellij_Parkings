package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;
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

public class LocalLaveriesService {
    private final static String LoggingLabel = "FrontEnd - AbonementService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);


    final String insertRequestOrder = "INSERT_LOCAL";
    final String selectRequestOrder = "SELECT_ALL_LOCAL";
    final String updateRequestOrder = "UPDATE_LOCAL";

    private final NetworkConfig networkConfig;

    public LocalLaveriesService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertLoclaLaveries(LocalLaverie LocalLaverie) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        int birthdate = 0;
        logger.trace("LocalLaverie with its JSON face : {}", LocalLaverie);
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(LocalLaverie);
            logger.trace("LocalLaverie with its JSON face : {}", jsonifiedGuy);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(insertRequestOrder);
            request.setRequestContent(jsonifiedGuy);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertClientRequest clientRequest = new InsertClientRequest(
                    networkConfig,
                    birthdate++, request, LocalLaverie, requestBytes);
            clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final LocalLaverie guy = (LocalLaverie)clientResponse.getInfo();
            logger.debug("Thread {} complete : {} {} --> {}",
                    clientResponse.getThreadName(),
                    guy.getNumLocalL(), guy.getDisponibilite(),
                    clientResponse.getResult());
        }
    }

    public LocalLaveries selectLocalLaveries() throws InterruptedException, IOException {
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
                birthdate++, request, null, requestBytes, LocalLaveries.class);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (LocalLaveries) joinedClientRequest.getResult();
        }
        else {
            logger.error("No LocalLaveries found");
            return null;
        }
    }

    public void updateLocLaveries(LocalLaverie localLaverie) throws InterruptedException, IOException {

        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        int birthdate = 0;
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedLocal = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(localLaverie);
        logger.trace("LocalLaverie en JSON : {}", jsonifiedLocal);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(updateRequestOrder); 
        request.setRequestContent(jsonifiedLocal);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                birthdate++, request, localLaverie, requestBytes);
        clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
    
            final LocalLaverie updatedLocal = (LocalLaverie) clientResponse.getInfo();
            logger.debug("Mise à jour terminée : {} disponibilité = {}",
                    updatedLocal.getNumLocalL(), updatedLocal.getDisponibilite());
        }
    }

    

}