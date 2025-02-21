package edu.ezip.ing1.pds.business.dto;


public class Vehicle {
    private String numPlaque;
    private String type;
    private String marque;


    public Vehicle(String numPlaque, String type, String marque) {
        this.numPlaque = numPlaque;
        this.type = type;
        this.marque = marque;
    }

    public Vehicle() {
    }

    public String getNumPlaque() {
        return numPlaque;
    }

    public void setNumPlaque(String numPlaque) {
        this.numPlaque = numPlaque;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }
}


