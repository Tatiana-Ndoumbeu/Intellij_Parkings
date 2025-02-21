package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "localLaverie")
public class LocalLaverie {

    private int numLocalL;


    private Boolean disponibilite;

    public LocalLaverie(int numLocalL, Boolean disponibilite) {
        this.numLocalL = numLocalL;
        this.disponibilite = disponibilite;
    }
public  LocalLaverie(){}
    public int getNumLocalL() {
        return numLocalL;
    }

    public void setNumLocalL(int numLocalL) {
        this.numLocalL = numLocalL;
    }

    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    @Override
    public String toString() {
        return "LocalLaverie{" +
                "numLocalL=" + numLocalL +
                ", disponibilite=" + disponibilite +
                '}';
    }
}
