package edu.ezip.ing1.pds.requests.apiRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;

public class SelectAllClientRequest<T> extends ClientRequest<Object, T> {

    private final Class<T> resultClass;

    public SelectAllClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, Object info, byte[] bytes, Class<T> resultClass)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
        this.resultClass = resultClass;
    }

    @Override
    public T readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, resultClass);
    }
}
