package edu.ezip.ing1.pds.business.dto;

public class LocalTechnique {

    private int numLocalT;


    private Boolean disponibilite;

    public LocalTechnique(int numLocalT, Boolean disponibilite) {
        this.numLocalT = numLocalT;
        this.disponibilite = disponibilite;
    }
    public  LocalTechnique(){}

    public int getNumLocalT() {
        return numLocalT;
    }
    public void setNumLocalT(int numLocalT) {
        this.numLocalT = numLocalT;
    }
    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Boolean disponibilite) {
        this.disponibilite = disponibilite;
    }


    @Override
    public String toString() {
        return "LocalTechnique{" +
                "numLocalT=" + numLocalT +
                ", disponibilite=" + disponibilite +
                '}';
    }
}

