package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class ArchivesPaiements {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("localLaveries")
    private  Set<ArchivesPaiement> archivesPaiements = new LinkedHashSet<ArchivesPaiement>();

    public Set<ArchivesPaiement> getArchivesPaiements() {
        return archivesPaiements;
    }

    public void setArchivesPaiements(Set<ArchivesPaiement> ArchivesPaiements) {
        this.archivesPaiements = ArchivesPaiements;
    }

    public final ArchivesPaiements add (final ArchivesPaiement archivesPaiement) {
        archivesPaiements.add(archivesPaiement);
        return this;
    }

    @Override
    public String toString() {
        return "ArchivesPaiements{" +
                "archivesPaiements=" + archivesPaiements +
                '}';
    }
}