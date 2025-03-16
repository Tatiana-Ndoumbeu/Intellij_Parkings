package edu.ezip.ing1.pds.business.dto;



import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Abonnement {
    private String idAbonnement;
    private String typeAbonnement;
    private double prix;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statutAbonnement;

    public Abonnement(String idAbonnement, String typeAbonnement, double prix, LocalDate dateDebut, LocalDate dateFin, String statutAbonnement) {
        this.idAbonnement = idAbonnement;
        this.typeAbonnement = typeAbonnement;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statutAbonnement = statutAbonnement;
    }

    public Abonnement() {}

    public String getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(String idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public String getTypeAbonnement() {
        return typeAbonnement;
    }

    public void setTypeAbonnement(String typeAbonnement) {
        this.typeAbonnement = typeAbonnement;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatutAbonnement() {
        return statutAbonnement;
    }

    public void setStatutAbonnement(String statutAbonnement) {
        this.statutAbonnement = statutAbonnement;
    }
}

