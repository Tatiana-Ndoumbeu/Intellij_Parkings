package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Personne;
import edu.ezip.ing1.pds.business.dto.Personnes;
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

public class PersonneService {
    private final static String LoggingLabel = "FrontEnd - PersonneService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String PersonnesToBeInserted = "people-to-be-inserted.yaml";

    final String insertRequestOrder = "INSERT_PERSONNE";
    final String selectRequestOrder = "SELECT_ALL_PERSONNES";

    private final NetworkConfig networkConfig;

    public PersonneService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertPersonnes() throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final Personnes guys = ConfigLoader.loadConfig(Personnes.class, PersonnesToBeInserted);

        int birthdate = 0;
        for(final Personne guy : guys.getPersonnes()) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(guy);
            logger.trace("Personne with its JSON face : {}", jsonifiedGuy);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(insertRequestOrder);
            request.setRequestContent(jsonifiedGuy);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertClientRequest clientRequest = new InsertClientRequest(
                    networkConfig,
                    birthdate++, request, guy, requestBytes);
            clientRequests.push(clientRequest);
        }

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest = clientRequests.pop();
            clientRequest.join();
            final Personne guy = (Personne)clientRequest.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientRequest.getThreadName(),
                    guy.getIdPersonne(), guy.getNom(), guy.getNom(),
                    clientRequest.getResult());
        }
    }

    public Personnes selectPersonnes() throws InterruptedException, IOException {
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
                birthdate++, request, null, requestBytes, Personnes.class);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Personnes) joinedClientRequest.getResult();
        }
        else {
            logger.error("No Personnes found");
            return null;
        }
    }

}
