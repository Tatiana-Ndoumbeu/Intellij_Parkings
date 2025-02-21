package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonRootName(value = "usager")
public class Usager {
    private String idUsager;
    private String nom;
    private String prenom;
    private String mail;
    private String codePostal;
    private String telephone;
    private String pays;
    private String numPlaque;


    public Usager() {
    }
    public final Usager build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "nom", "prenom", "mail", "codePostal", "telephone", "pays", "numPlaque");
        return this;
    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, "nom", "prenom", "mail", "codePostal", "telephone", "pays", "numPlaque");
    }
    

    public Usager(String idUsager, String nom, String prenom, String mail, String codePostal, String telephone,
            String pays, String numPlaque) {
        this.idUsager = idUsager;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.codePostal = codePostal;
        this.telephone = telephone;
        this.pays = pays;
        this.numPlaque = numPlaque;
    }

   

    public String getIdUsager() {
        return idUsager;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getMail() {
        return mail;
    }
    public String getCodePostal() {
        return codePostal;
    }
    public String getTelephone() {
        return telephone;
    }
    public String getPays() {
        return pays;
    }
    public String getNumPlaque() {
        return numPlaque;
    }
    
    public void setIdUsager(String idUsager) {
        this.idUsager = idUsager;
    }
    @JsonProperty("usager_nom")
    public void setNom(String nom) {
        this.nom = nom;
    }
    @JsonProperty("usager_prenom")
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    @JsonProperty("usager_mail")
    public void setMail(String mail) {
        this.mail = mail;
    }
    @JsonProperty("usager_codeP")
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
    @JsonProperty("usager_tel")
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    @JsonProperty("usager_pays")
    public void setPays(String pays) {
        this.pays = pays;
    }
    @JsonProperty("usager_plaque")
    public void setNumPlaque(String numPlaque) {
        this.numPlaque = numPlaque;
    }



    private void setFieldsFromResulset(final ResultSet resultSet, final String ... fieldNames )
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        for(final String fieldName : fieldNames ) {
            final Field field = this.getClass().getDeclaredField(fieldName);
            field.set(this, resultSet.getObject(fieldName));
        }
    }
    private final PreparedStatement buildPreparedStatement(PreparedStatement preparedStatement, final String ... fieldNames )
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        int ix = 0;
        for(final String fieldName : fieldNames ) {
            preparedStatement.setString(++ix, fieldName);
        }
        return preparedStatement;
    }
    @Override
    public String toString() {
        return "Usager [idUsager=" + idUsager + ", nom=" + nom + ", prenom=" + prenom + ", mail=" + mail
                + ", codePostal=" + codePostal + ", telephone=" + telephone + ", pays=" + pays + ", numPlaque="
                + numPlaque + "]";
    }


}
