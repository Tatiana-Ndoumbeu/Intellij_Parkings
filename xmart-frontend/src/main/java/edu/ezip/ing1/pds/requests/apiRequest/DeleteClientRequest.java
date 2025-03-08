package edu.ezip.ing1.pds.requests.apiRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;

public class DeleteClientRequest<T> extends ClientRequest<T, String> {
    
    public DeleteClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, T info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    public String readResult(String body) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, String.class);
    }

}
