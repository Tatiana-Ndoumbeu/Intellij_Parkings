package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationLocal {
    private String reservationLocalId;
    private int numLocal;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateDebut;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateFin;

    @JsonFormat(pattern = "HH:mm")
    private String heureEntree;

    @JsonFormat(pattern = "HH:mm")
    private String heureSortie;

    private String typeLocal;
    private String idPersonne;

    public ReservationLocal() {
    }

    public ReservationLocal(String reservationLocalId, int numLocal, String idPersonne, String typeLocal, String heureSortie, String dateFin, String dateDebut, String heureEntree) {
        this.reservationLocalId = reservationLocalId;
        this.numLocal = numLocal;
        this.idPersonne = idPersonne;
        this.typeLocal = typeLocal;
        this.heureSortie = heureSortie;
        this.dateFin = dateFin;
        this.dateDebut = dateDebut;
        this.heureEntree = heureEntree;
    }

    public String getReservationLocalId() {
        return reservationLocalId;
    }

    public void setReservationLocalId(String reservationLocalId) {
        this.reservationLocalId = reservationLocalId;
    }

    public int getNumLocal() {
        return numLocal;
    }

    public void setNumLocal(int numLocal) {
        this.numLocal = numLocal;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getHeureEntree() {
        return heureEntree;
    }

    public void setHeureEntree(String heureEntree) {
        this.heureEntree = heureEntree;
    }

    public String getHeureSortie() {
        return heureSortie;
    }

    public void setHeureSortie(String heureSortie) {
        this.heureSortie = heureSortie;
    }

    public String getTypeLocal() {
        return typeLocal;
    }

    public void setTypeLocal(String typeLocal) {
        this.typeLocal = typeLocal;
    }

    public String getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    @Override
    public String toString() {
        return "ReservationLocal{" +
                "reservationLocalId='" + reservationLocalId + '\'' +
                ", numLocal=" + numLocal +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", heureEntree='" + heureEntree + '\'' +
                ", heureSortie='" + heureSortie + '\'' +
                ", typeLocal='" + typeLocal + '\'' +
                ", idPersonne='" + idPersonne + '\'' +
                '}';
    }
}
