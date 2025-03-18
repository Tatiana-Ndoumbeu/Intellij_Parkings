package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Abonnements;
import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.apiRequest.InsertClientRequest;
import edu.ezip.ing1.pds.requests.apiRequest.SelectAllClientRequest;
//import edu.ezip.ing1.pds.requests.apiRequest.DeleteClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class MecanicienService {
    private final static String LoggingLabel = "FrontEnd - AbonementService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_MECANICIEN";
    final String selectRequestOrder = "SELECT_ALL_MECANICIEN";
    //final String suppRequestOrder = "DELETE_MECANICIEN";
    //final String updateRequestOrder = "UPDATE_MECANICIEN";

    private final NetworkConfig networkConfig;

    public MecanicienService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertMecanicien(Mecanicien mecanicien) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mecanicien);
        logger.trace("Mecanicien with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(insertRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                birthdate++, request, mecanicien, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final Mecanicien guy = (Mecanicien) clientResponse.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientResponse.getThreadName(),
                    guy.getNom(), guy.getPrenom(), guy.getSpecialite(),
                    clientResponse.getResult());
        }
    }

    public Mecaniciens selectMecanicien() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
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
                birthdate++, request, null, requestBytes, Mecaniciens.class);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Mecaniciens) joinedClientRequest.getResult();
        } else {
            logger.error("Pas de Mécaniciens trouvés");
            return null;
        }
    }

}