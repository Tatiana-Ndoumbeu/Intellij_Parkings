package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Abonnements;
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

public class AbonementService {
    private final static String LoggingLabel = "FrontEnd - AbonementService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_ABONNEMENT";
    final String selectRequestOrder = "SELECT_ALL_ABONNEMENTS";
    final String suppRequestOrder = "DELETE_ABONNEMENT";
    final String updateRequestOrder = "UPDATE_ABONNEMENT";

    private final NetworkConfig networkConfig;

    public AbonementService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertAbonements(Abonnement abonnement) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

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
            request.toString();
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertClientRequest clientRequest = new InsertClientRequest(
                    networkConfig,
                    birthdate++, request, abonnement, requestBytes);
            clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final Abonnement guy = (Abonnement)clientResponse.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientResponse.getThreadName(),
                    guy.getIdAbonnement(), guy.getPrix(), guy.getStatutAbonnement(),
                    clientResponse.getResult());
        }
    }

    public Abonnements selectAbonnements() throws InterruptedException, IOException {
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
        birthdate++;
        final SelectAllClientRequest clientRequest = new SelectAllClientRequest(
                networkConfig,
                birthdate, request, null, requestBytes, Abonnements.class);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Abonnements) joinedClientRequest.getResult();
        }
        else {
            logger.error("No Abonnements found");
            return null;
        }
    }

    public void supprimerAbonnement(String id_Abonnement) throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(suppRequestOrder);
        request.setRequestContent(id_Abonnement);  
    
        final byte[] requestBytes = objectMapper.writeValueAsBytes(request);
    
        
        final DeleteClientRequest<String> deleteRequest = new DeleteClientRequest<>(
            networkConfig, birthdate++, request, id_Abonnement, requestBytes );
    
        clientRequests.push(deleteRequest);
    
        if (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join(); 
    
             
            if (clientResponse.getResult() != null && clientResponse.getResult().toString().contains("success")) {
                logger.debug("Abonnement supprimé avec succès.");
            } else {
                logger.error("Échec de la suppression de l'abonnement.");
            }
        }
    }

    public void updateAbonnement(Abonnement abonnement) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        //int birthdate = 0;
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

        /* InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                birthdate++, request, abonnement, requestBytes);
        clientRequests.push(clientRequest); */
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
        }
    }
}
