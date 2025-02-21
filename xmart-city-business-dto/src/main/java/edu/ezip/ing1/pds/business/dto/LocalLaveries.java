package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class LocalLaveries {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("localLaveries")
    private  Set<LocalLaverie> localLaveries = new LinkedHashSet<LocalLaverie>();

    public Set<LocalLaverie> getLocalLaveries() {
        return localLaveries;
    }

    public void setLocalLaveries(Set<LocalLaverie> LocalLaveries) {
        this.localLaveries = LocalLaveries;
    }

    public final LocalLaveries add (final LocalLaverie localLaverie) {
        localLaveries.add(localLaverie);
        return this;
    }

    @Override
    public String toString() {
        return "LocalLaveries{" +
                "LocalLaveries=" + localLaveries +
                '}';
    }
}
