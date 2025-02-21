package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@JsonRootName(value = "service")
public class Service {
    private String idService;
    private LocalDateTime date;
    private String type_service;
    private String libelle;
   


    public Service() {
    }
    public final Service build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet, "nom", "prenom", "mail", "codePostal", "telephone", "pays", "numPlaque");
        return this;
    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, "nom", "prenom", "mail", "codePostal", "telephone", "pays", "numPlaque");
    }
    



   

    public Service(String idService, LocalDateTime date, String type_service, String libelle) {
        this.idService = idService;
        this.date = date;
        this.type_service = type_service;
        this.libelle = libelle;
    }


    public String getIdService() {
        return idService;
    }
    @JsonProperty("serviceId")
    public void setIdService(String idService) {
        this.idService = idService;
    }
    public LocalDateTime getDate() {
        return date;
    }
    @JsonProperty("service_date")
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getType_service() {
        return type_service;
    }
    @JsonProperty("service_type")
    public void setType_service(String type_service) {
        this.type_service = type_service;
    }
    public String getLibelle() {
        return libelle;
    }
    @JsonProperty("service_libelle")
    public void setLibelle(String libelle) {
        this.libelle = libelle;
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
        return "Service [idService=" + idService + ", date=" + date + ", type_service=" + type_service + ", libelle="
                + libelle + "]";
    }
    


}
