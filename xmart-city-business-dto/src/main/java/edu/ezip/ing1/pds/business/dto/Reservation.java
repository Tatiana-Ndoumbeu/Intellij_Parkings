package edu.ezip.ing1.pds.business.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reservation {

    private String idReservation;
    private LocalDate dateReservation;
    private LocalTime heure;
    private LocalDateTime dateEntree;
    private LocalDateTime dateSortie;
    private String idPersonne;
    private String idPlace;
    private Personne personne;
    private PlaceDeParking placeDeParking;
   // private String idPaiement; plus tard

    public Reservation() {
    }

    public Reservation(String idReservation, LocalTime heure, LocalDate dateReservation, LocalDateTime dateEntree, LocalDateTime dateSortie, String idPersonne, String idPlace, Personne personne, PlaceDeParking placeDeParking) {
        this.idReservation = idReservation;
        this.heure = heure;
        this.dateReservation = dateReservation;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
        this.idPersonne = idPersonne;
        this.idPlace = idPlace;
        this.personne = personne;
        this.placeDeParking = placeDeParking;

    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public LocalDateTime getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDateTime dateEntree) {
        this.dateEntree = dateEntree;
    }

    public LocalDateTime getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDateTime dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
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
        return "Reservation{" +
                "idReservation='" + idReservation + '\'' +
                ", dateReservation=" + dateReservation +
                ", heure=" + heure +
                ", dateEntree=" + dateEntree +
                ", dateSortie=" + dateSortie +
                ", idPersonne='" + idPersonne + '\'' +
                ", idPlace='" + idPlace + '\'' +
                ", personne=" + personne +
                ", placeDeParking=" + placeDeParking +
                '}';
    }
}
