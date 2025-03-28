package edu.ezip.ing1.pds.business.dto;


import java.util.UUID;

public class Personne {
    private String idPersonne;
    private String nom;
    private String prenom;
    private String telephone;
    private String mail;
    private String codePostal;

    public Personne() {}

    public Personne(String idPersonne, String nom, String prenom, String telephone, String mail, String codePostal) {
        this.idPersonne = idPersonne;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.mail = mail;
        this.codePostal = codePostal;
    }
    public String getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
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

    public void setTelephone(String tel) {
        this.telephone = tel;
    }
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
}

