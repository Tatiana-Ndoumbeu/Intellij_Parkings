package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.api.ArchivesRepository;
import edu.ezip.ing1.pds.business.dto.ArchivesPaiement;
import edu.ezip.ing1.pds.business.dto.ArchivesPaiements;
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

public class ArchivesPaiementService implements ArchivesRepository {
    private final static String LoggingLabel = "FrontEnd - ArchivesPaiementService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_ARCHIVES";
    final String selectRequestOrder = "SELECT_ALL_ARCHIVES";
    //final String suppRequestOrder = "DELETE_ARCHIVES";
    //final String updateRequestOrder = "UPDATE_ARCHIVES";

    private final NetworkConfig networkConfig;

    public ArchivesPaiementService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public Boolean insert(ArchivesPaiement archivesPaiement) throws InterruptedException, IOException {

        //j'dois mettre peut etre un try catch plus tard

        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(archivesPaiement);
        logger.trace("Archive with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(insertRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertClientRequest clientRequest = new InsertClientRequest(
                networkConfig,
                birthdate++, request, archivesPaiement, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientResponse = clientRequests.pop();
            clientResponse.join();
            final ArchivesPaiement guy = (ArchivesPaiement) clientResponse.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientResponse.getThreadName(),
                    guy.getNom(), guy.getPrenom(), guy.getService(),
                    clientResponse.getResult());
        }
        return true;
    }

    public ArchivesPaiements select() throws InterruptedException, IOException {
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
                birthdate++, request, null, requestBytes, ArchivesPaiements.class);
        clientRequests.push(clientRequest);

        if (!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (ArchivesPaiements) joinedClientRequest.getResult();
        } else {
            logger.error("Pas d'Archives trouvés");
            return null;
        }
    }
}
