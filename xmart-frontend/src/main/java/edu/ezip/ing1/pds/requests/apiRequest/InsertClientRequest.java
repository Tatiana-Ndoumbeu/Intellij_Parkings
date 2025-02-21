package edu.ezip.ing1.pds.requests.apiRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;
//utilisation de la genericite pour les appels dínsertions de tous objets
public class InsertClientRequest<T, R> extends ClientRequest<T, R> {

    private final Class<Map> mapClass = Map.class;
    public InsertClientRequest(NetworkConfig networkConfig, int myBirthDate, Request request, T info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public R readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> resultMap = mapper.readValue(body, mapClass);
        @SuppressWarnings("unchecked")
        R result = (R) resultMap.toString();
        return result;
    }
}

