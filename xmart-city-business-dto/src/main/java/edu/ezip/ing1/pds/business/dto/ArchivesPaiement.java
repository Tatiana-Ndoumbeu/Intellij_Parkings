package edu.ezip.ing1.pds.business.dto;

public class ArchivesPaiement {
    private String nom;
    private String prenom;
    private String service;
    private String date;
    private double montant;

    public ArchivesPaiement() {
    }

    public ArchivesPaiement(String nom, String prenom, String service, String date, double montant) {
        this.nom = nom;
        this.prenom = prenom;
        this.service = service;
        this.date = date;
        this.montant = montant;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "ArchivePaiement{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", service='" + service + '\'' +
                ", date='" + date + '\'' +
                ", montant=" + montant +
                '}';
    }
}
