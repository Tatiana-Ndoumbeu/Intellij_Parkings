package edu.ezip.ing1.pds.business.dto;


import java.util.UUID;

public class Personne {
    private String idPersonne;
    private String mail;
    private String nom;
    private String prenom;
    private String tel;
    private String pays;
    private String codePostal;

    public Personne() {}
    public Personne(String mail, String nom, String prenom, String tel, String pays, String codePostal) {
        this.mail = mail;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.pays = pays;
        this.codePostal = codePostal;
    }
    public String getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getMail() {
        return mail;
    }


    public void setMail(String mail) {
        this.mail = mail;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
}

