package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.api.PersonneRepository;
import edu.ezip.ing1.pds.business.dto.*;
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
import java.util.*;

public class PersonneService implements PersonneRepository {
    private final static String LoggingLabel = "FrontEnd - PersonneService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String PersonnesToBeInserted = "people-to-be-inserted.yaml";

    final String insertRequestOrder = "INSERT_PERSONNE";
    final String selectRequestOrder = "SELECT_ALL_PERSONNES";
    final String updateRequestOrder = "UPDATE_PERSONNE";
    final String deleteRequestOrder = "DELETE_PERSONNE";
    final String SelectTypeAboRequestOrder = "SELECT_TYPE_ABONNEMENT";
    final String SelectOneAboRequestOrder = "SELECT_ONE_ABONNEMENT";


    private final NetworkConfig networkConfig;

    public PersonneService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }


    public void insertPersonnes(Personne personne) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(personne);
        logger.trace("Personne with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(insertRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                birthdate++, request, personne, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final Personne guy = (Personne) clientResponse.getInfo();
            logger.debug("Thread {} complete : {} {} {} {} {} --> {}",
                    clientResponse.getThreadName(),
                    guy.getNom(), guy.getPrenom(), guy.getTelephone(),
                    guy.getMail(), guy.getCodePostal(),
                    clientResponse.getResult());
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

    @Override
    public boolean updatePersonnes(Personne personne) throws InterruptedException, IOException {

        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedPersonne = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(personne);
        logger.trace("Personne en JSON : {}", jsonifiedPersonne);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(updateRequestOrder);
        request.setRequestContent(jsonifiedPersonne);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                requestId.hashCode(), request, personne, requestBytes);
        clientRequests.push(clientRequest);

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();

            final Personne updatedPersonne = (Personne) clientResponse.getInfo();
            logger.debug("Thread {} complete: {} {} {} {} {} --> {}",
                    clientResponse.getThreadName(),
                    updatedPersonne.getIdPersonne(),
                    updatedPersonne.getNom(), updatedPersonne.getPrenom(),
                    updatedPersonne.getTelephone(), updatedPersonne.getMail(),
                    clientResponse.getResult());
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePersonnes(Personne personne) throws InterruptedException, IOException {
        try {
            logger.debug("Deleting Personne with ID: {}", personne);
            final ObjectMapper objectMapper = new ObjectMapper();
            Personne personneToDelete = new Personne();
            personneToDelete.setIdPersonne(personne.getIdPersonne());

            final String jsonifiedPersonne = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(personneToDelete);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(deleteRequestOrder);
            request.setRequestContent(jsonifiedPersonne);

            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final DeleteClientRequest clientRequest = new DeleteClientRequest(
                    networkConfig,
                    requestId.hashCode(), request, personneToDelete, requestBytes);
            clientRequest.join();

            logger.debug("PlaceDeParking deleted with ID: {}", personne.getIdPersonne());
            return true;
        } catch (Exception e) {
            logger.error("Error deleting PlaceDeParking", e);
            return false;
        }
    }

    @Override
    public Set<Personne> findAll() {
        try {
            logger.debug("Retrieving all PlaceDeParking objects");
            final ObjectMapper objectMapper = new ObjectMapper();
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(selectRequestOrder);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);

            final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
            final SelectAllClientRequest clientRequest = new SelectAllClientRequest(
                    networkConfig,
                    requestId.hashCode(), request, null, requestBytes, PlacesDeParkings.class);
            clientRequest.join();


            Personnes personnes = (Personnes) clientRequest.getResult();
            return personnes.getPersonnes();

        } catch (Exception e) {
            logger.error("Error retrieving all Personne objects", e);
            return null;
        }
    }
}
