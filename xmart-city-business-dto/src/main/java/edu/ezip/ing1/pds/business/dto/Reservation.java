package edu.ezip.ing1.pds.business.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class Reservation {

    private String idReservation;
    private LocalDate dateReservation;
    private LocalTime heure;
    private Date dateEntree;
    private Date dateSortie;
    private Date heureEntree;
    private Date heureSortie;
    private String idPersonne;
    private String position;
    private Personne personne;
    private PlaceDeParking placeDeParking;
   // private String idPaiement; plus tard

    public Reservation() {
    }

    public Reservation(String idReservation, LocalDate dateReservation, LocalTime heure, Date dateEntree, Date dateSortie, Date heureEntree, Date heureSortie, String idPersonne, String position, Personne personne, PlaceDeParking placeDeParking) {
        this.idReservation = idReservation;
        this.dateReservation = dateReservation;
        this.heure = heure;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
        this.heureEntree = heureEntree;
        this.heureSortie = heureSortie;
        this.idPersonne = idPersonne;
        this.position = position;
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

    public Date getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(Date dateEntree) {
        this.dateEntree = dateEntree;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(Date dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public Date getHeureSortie() {
        return heureSortie;
    }

    public void setHeureSortie(Date heureSortie) {
        this.heureSortie = heureSortie;
    }

    public Date getHeureEntre() {
        return heureEntree;
    }

    public void setHeureeEntre(Date heureEntree) {
        this.heureEntree = heureEntree;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation='" + idReservation + '\'' +
                ", dateReservation=" + dateReservation +
                ", heure=" + heure +
                ", dateEntree=" + dateEntree +
                ", dateSortie=" + dateSortie +
                ", heureEntree=" + heureEntree +
                ", heureSortie=" + heureSortie +
                ", idPersonne='" + idPersonne + '\'' +
                ", position='" + position + '\'' +
                ", personne=" + personne +
                ", placeDeParking=" + placeDeParking +
                '}';
    }
}
