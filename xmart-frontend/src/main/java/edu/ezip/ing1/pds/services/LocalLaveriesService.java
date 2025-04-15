package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.api.LocalLaverieRepository;
import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.apiRequest.InsertClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.SelectAllClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.DeleteClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class LocalLaveriesService implements LocalLaverieRepository {
    private final static String LoggingLabel = "FrontEnd - LocalLaverieService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);


    final String insertRequestOrder = "INSERT_LOCAL";
    final String selectRequestOrder = "SELECT_ALL_LOCAL";
    final String updateRequestOrder = "UPDATE_LOCAL";
    final String deleteRequestOrder = "DELETE_LOCAL";
    final String selectDispoRequestOrder = "SELECT_DISPO_LOCAUX";

    private final NetworkConfig networkConfig;

    public LocalLaveriesService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public Boolean insert(LocalLaverie LocalLaverie) throws InterruptedException, IOException {
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
        return true;
    }

    public LocalLaveries select() throws InterruptedException, IOException {
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
    public LocalLaveries selectDispo() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectDispoRequestOrder);
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

    public Boolean update(LocalLaverie localLaverie) throws InterruptedException, IOException {

        //j'dois mettre peut etre un try catch plus tard
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
        return true;
    }
    public Boolean delete(LocalLaverie localLaverie) throws InterruptedException, IOException {
       try {
           final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
           final ObjectMapper objectMapper = new ObjectMapper();
           final String requestId = UUID.randomUUID().toString();
           final Request request = new Request();

           request.setRequestId(requestId);
           request.setRequestOrder(deleteRequestOrder);
           final String jsonifiedLocal = objectMapper.writeValueAsString(localLaverie);
           request.setRequestContent(jsonifiedLocal);

           objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
           final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
           final DeleteClientRequest<LocalLaverie> clientRequest = new DeleteClientRequest<>(
                   networkConfig, 0, request, localLaverie, requestBytes);

           clientRequests.push(clientRequest);

           while (!clientRequests.isEmpty()) {
               final ClientRequest clientResponse = clientRequests.pop();
               clientResponse.join();
               logger.debug("Suppression terminée pour LocalLaverie {} : {}",
                       localLaverie.getNumLocalL(), clientResponse.getResult());
           }
           return true;
       } catch (Exception e) {
           logger.error("Error suppression", e);
           return false;
       }
    }

}



    

