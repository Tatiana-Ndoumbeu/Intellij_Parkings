package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReservationRequest {

    private String idReservation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateReservation;

    @JsonFormat(pattern = "HH:mm")
    private String heure;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateEntree;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateSortie;

    @JsonFormat(pattern = "HH:mm")
    private String heureEntree;

    @JsonFormat(pattern = "HH:mm")
    private String heureSortie;
private String idPlace;
    private String idPersonne;
    private Personne personne;
    private PlaceDeParking placeDeParking;

    public ReservationRequest() {
    }

    public ReservationRequest(String idReservation, String dateReservation, String heure,
                              String dateEntree, String dateSortie,
                              String heureEntree, String heureSortie,
                              String idPlace, String idPersonne,
                              Personne personne, PlaceDeParking placeDeParking) {
        this.idReservation = idReservation;
        this.dateReservation = dateReservation;
        this.heure = heure;
        this.idPlace = idPlace;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
        this.heureEntree = heureEntree;
        this.heureSortie = heureSortie;
        this.idPersonne = idPersonne;
        this.personne = personne;
        this.placeDeParking = placeDeParking;
    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(String dateEntree) {
        this.dateEntree = dateEntree;
    }

    public String getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(String dateSortie) {
        this.dateSortie = dateSortie;
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

    public String getIdPersonne() {
        return idPersonne;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public PlaceDeParking getPlaceDeParking() {
        return placeDeParking;
    }

    public void setPlaceDeParking(PlaceDeParking placeDeParking) {
        this.placeDeParking = placeDeParking;
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "idReservation='" + idReservation + '\'' +
                ", dateReservation='" + dateReservation + '\'' +
                ", heure='" + heure + '\'' +
                ", dateEntree='" + dateEntree + '\'' +
                ", dateSortie='" + dateSortie + '\'' +
                ", heureEntree='" + heureEntree + '\'' +
                ", heureSortie='" + heureSortie + '\'' +
                ", idPersonne='" + idPersonne + '\'' +
                ", personne=" + personne +
                ", placeDeParking=" + placeDeParking +
                '}';
    }
}
