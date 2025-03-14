package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class LocalTechniques {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("LocalTechniques")
    private  Set<LocalTechnique> localTechniques = new LinkedHashSet<LocalTechnique>();

    public Set<LocalTechnique> getLocalTechniques() {
        return localTechniques;
    }

    public void setLocalTechniques(Set<LocalTechnique> LocalTechniques) {
        this.localTechniques = LocalTechniques;
    }

    public final LocalTechniques add (final LocalTechnique localTechnique) {
        localTechniques.add(localTechnique);
        return this;
    }

    @Override
    public String toString() {
        return "LocalTechniques{" +
                "LocalTechniques=" + localTechniques +
                '}';
    }
}
