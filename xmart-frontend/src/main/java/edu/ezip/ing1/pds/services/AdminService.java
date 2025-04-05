package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.ing1.pds.api.AdminRepository;
import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Admin;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.apiRequest.InsertClientRequest;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class AdminService implements AdminRepository {
    private final static String LoggingLabel = "AdminService - AbonementService";
    private final NetworkConfig networkConfig;
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    public AdminService(NetworkConfig config) {
        this.networkConfig = config;
    }

    @Override
    public boolean save(Admin admin) {
        try {
            final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(admin);
            final String requestId = UUID.randomUUID().toString();
            logger.trace("Abonnement with its JSON face : {}", jsonifiedGuy);
            Request request = new Request();
            request.setRequestId(UUID.randomUUID().toString());
            request.setRequestOrder("INSERT_UTILISATEUR");
            request.setRequestContent(jsonifiedGuy);
            request.toString();
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
            final InsertClientRequest clientRequest = new InsertClientRequest(
                    networkConfig,
                    requestId.hashCode(), request, admin, requestBytes);
            clientRequests.push(clientRequest);


            while (!clientRequests.isEmpty()) {
                final ClientRequest clientResponse = clientRequests.pop();
                clientResponse.join();
                var guy = clientResponse.getInfo();
                logger.debug("Thread {} complete : {} {} {} --> {}",
                        clientResponse.getThreadName(),
                        guy,
                        clientResponse.getResult());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    @Override
    public Admin findByEmail(String email) {
        // Optionnel : requête spécifique selon ton backend
        return null;
    }

    @Override
    public boolean login(String email, String password) {
        try {
            final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
            final ObjectMapper objectMapper = new ObjectMapper();
            var admin = new Admin();
            admin.setEmail(email);
            admin.setPassword(password);
            final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(admin);
            final String requestId = UUID.randomUUID().toString();
            logger.trace("Abonnement with its JSON face : {}", jsonifiedGuy);
            Request request = new Request();
            request.setRequestId(UUID.randomUUID().toString());
            request.setRequestOrder("LOGIN_UTILISATEUR");
            request.setRequestContent(jsonifiedGuy);
            request.toString();
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
            final InsertClientRequest clientRequest = new InsertClientRequest(
                    networkConfig,
                    requestId.hashCode(), request, admin, requestBytes);
            clientRequests.push(clientRequest);


            while (!clientRequests.isEmpty()) {
                final ClientRequest clientResponse = clientRequests.pop();
                clientResponse.join();
                var guy = clientResponse.getInfo();
                logger.debug("Thread {} complete : {} {} {} --> {}",
                        clientResponse.getThreadName(),
                        guy,
                        clientResponse.getResult());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
