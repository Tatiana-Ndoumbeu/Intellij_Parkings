package edu.ezip.ing1.pds.business.dto;



import java.sql.Date;
import java.time.LocalDate;

public class Abonnement {
    private String idAbonnement;
    private String typeAbonnement;
    private double prix;
    private Date dateDebut;
    private Date dateFin;
    private String statutAbonnement;
    private String idPersonne;

    public Abonnement(String idAbonnement, String typeAbonnement, double prix, Date dateDebut, Date dateFin, String statutAbonnement, String idPersonne) {
        this.idAbonnement = idAbonnement;
        this.typeAbonnement = typeAbonnement;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statutAbonnement = statutAbonnement;
        this.idPersonne = idPersonne;
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

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatutAbonnement() {
        return statutAbonnement;
    }

    public void setStatutAbonnement(String statutAbonnement) {
        this.statutAbonnement = statutAbonnement;
    }

    public String getIdPersonne() {
        return idPersonne;
    }
    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "idAbonnement='" + idAbonnement + '\'' +
                ", typeAbonnement='" + typeAbonnement + '\'' +
                ", prix=" + prix +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statutAbonnement='" + statutAbonnement + '\'' +
                ", idPersonne='" + idPersonne + '\'' +


                '}';
    }
}

