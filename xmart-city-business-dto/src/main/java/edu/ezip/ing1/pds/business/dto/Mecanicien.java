package edu.ezip.ing1.pds.business.dto;



import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Mecanicien {
    private String nom;
    private String prenom;
    private String telephone;
    private Boolean disponibilite;
    private String specialite;
    private String mail;

    public Mecanicien(String prenom, String nom, String telephone, Boolean disponibilite, String specialite, String mail) {
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
        this.disponibilite = disponibilite;
        this.specialite = specialite;
        this.mail = mail;
    }

    public Mecanicien() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Mecanicien{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", disponibilite=" + disponibilite +
                ", specialite='" + specialite + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}