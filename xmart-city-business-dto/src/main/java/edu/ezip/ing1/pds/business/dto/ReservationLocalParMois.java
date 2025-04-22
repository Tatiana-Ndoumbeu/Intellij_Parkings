package edu.ezip.ing1.pds.business.dto;

public class ReservationLocalParMois {
    private int annee;
    private int mois;

    public ReservationLocalParMois() {
    }

    public ReservationLocalParMois(int annee, int mois) {
        this.annee = annee;
        this.mois = mois;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }
}
