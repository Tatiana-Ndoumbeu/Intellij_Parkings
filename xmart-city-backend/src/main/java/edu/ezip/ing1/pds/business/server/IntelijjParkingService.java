package edu.ezip.ing1.pds.business.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Student;
import edu.ezip.ing1.pds.business.dto.Students;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.commons.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.UUID;


import edu.ezip.ing1.pds.business.dto.*;


public class IntelijjParkingService {

    private final static String LoggingLabel = "B u s i n e s s - S e r v e r";
    private final Logger logger = LoggerFactory.getLogger(LoggingLabel);

    private enum Queries {
        SELECT_ALL_STUDENTS("SELECT t.name, t.firstname, t.groupname FROM apiRequest t"),
        INSERT_STUDENT("INSERT INTO apiRequest (name, firstname, groupname) VALUES (?, ?, ?)"),

        SELECT_ALL_VEHICULES("SELECT t.num_plaque, t.type, t.marque FROM Vehicule t"),
        INSERT_VEHICULE("INSERT INTO Vehicule (num_plaque, type, marque) VALUES (?, ?, ?)"),

        SELECT_ALL_ABONNEMENTS("SELECT t.id_abonnement, t.typeAbonnement, t.prix, t.statutAbonnement, t.dateDebut,t.dateFin FROM Abonnement t"),
        INSERT_ABONNEMENT("INSERT INTO Abonnement (id_abonnement, typeAbonnement, prix,statutAbonnement, dateDebut, dateFin ) VALUES (?, ?, ?, ?, ?, ?)"),

        SELECT_ALL_PERSONNES("SELECT t.id_personne, t.mail, t.nom, t.prenom, t.tel, t.code_postal FROM Personne t"),
        INSERT_PERSONNE("INSERT INTO Personne (id_personne, nom, prenom, tel,mail, code_postal) VALUES (?, ?, ?,?, ?, ?)"),

        SELECT_ALL_LOCAL("SELECT l.NumLocalL, l.disponibilite FROM LocalLaverie l"),
        INSERT_LOCAL("INSERT into LocalLaverie (NumLocalL, disponibilite) VALUES (? , ?)"),

        SELECT_ALL_PLACE_DE_PARKING("SELECT t.id_place, t.type_place, t.statut_place, t.emplacement FROM PlaceDeParking t"),
        INSERT_PLACE_DE_PARKING("INSERT INTO PlaceDeParking (id_place,type_place, statut_place, emplacement) VALUES (?,?, ?, ?)");

        private final String query;

        private Queries(final String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }

    public static IntelijjParkingService inst = null;

    public static final IntelijjParkingService getInstance() {
        if (inst == null) {
            inst = new IntelijjParkingService();
        }
        return inst;
    }

    private IntelijjParkingService() {
    }

    public final Response dispatch(final Request request, final Connection connection)
            throws InvocationTargetException, IllegalAccessException, SQLException, IOException {
        Response response = null;

        final Queries queryEnum = Enum.valueOf(Queries.class, request.getRequestOrder());
        switch (queryEnum) {
            case SELECT_ALL_STUDENTS:
                response = SelectAllStudents(request, connection);
                break;
            case INSERT_STUDENT:
                response = InsertStudent(request, connection);
                break;
            case SELECT_ALL_VEHICULES:
                response = SelectAllVehicules(request, connection);
                break;
            case INSERT_VEHICULE:
                response = InsertVehicule(request, connection);
                break;
            case SELECT_ALL_ABONNEMENTS:
                response = SelectAllAbonnements(request, connection);
                break;
            case INSERT_ABONNEMENT:
                response = InsertAbonnement(request, connection);
                break;
            case SELECT_ALL_PERSONNES:
                response = SelectAllPersonnes(request, connection);
                break;
            case INSERT_LOCAL:
                response = InsertLocalL(request, connection);
                break;
            case SELECT_ALL_LOCAL:
                response = SelectAllLocalL(request, connection);
                break;
            case INSERT_PERSONNE:
                response = InsertPersonne(request, connection);
                break;
            case SELECT_ALL_PLACE_DE_PARKING:
                response = SelectAllPlaceDeParking(request, connection);
                break;
            case INSERT_PLACE_DE_PARKING:
                response = InsertPlaceDeParking(request, connection);
                break;
            default:
                break;
        }

        return response;
    }

    // Insert and Select methods for Student, Vehicule, Abonnement, Personne, and PlaceDeParking
    private Response SelectAllStudents(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_STUDENTS.getQuery());

        Students students = new Students();
        while (res.next()) {
            Student student = new Student();
            student.setName(res.getString(1));
            student.setFirstname(res.getString(2));
            student.setGroup(res.getString(3));
            students.add(student);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(students));
    }

    private Response InsertStudent(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        Student student = objectMapper.readValue(request.getRequestBody(), Student.class);

        try (PreparedStatement pstmt = connection.prepareStatement(Queries.INSERT_STUDENT.getQuery())) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getFirstname());
            pstmt.setString(3, student.getGroup());
            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "Étudiant inséré avec succès" : "Échec de l'insertion");
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de l'insertion de l'étudiant", e);
            return new Response(request.getRequestId(), "Erreur SQL");
        }
    }

    private Response SelectAllVehicules(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_VEHICULES.getQuery());

        Vehicles vehicles = new Vehicles();
        while (res.next()) {
            Vehicle vehicle = new Vehicle();
            vehicle.setNumPlaque(res.getString(1));
            vehicle.setType(res.getString(2));
            vehicle.setMarque(res.getString(3));
            vehicles.add(vehicle);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(vehicles));
    }

    private Response SelectAllAbonnements(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_ABONNEMENTS.getQuery());

        Abonnements abonnements = new Abonnements();
        while (res.next()) {
            Abonnement abonnement = new Abonnement();
            abonnement.setIdAbonnement(res.getString(1));
            abonnement.setPrix(res.getDouble(2));
            abonnement.setTypeAbonnement(res.getString(3));
            abonnement.setDateDebut(res.getDate(5));
            abonnement.setDateFin(res.getDate(6));
            abonnement.setStatutAbonnement(res.getString(4));
            abonnements.add(abonnement);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(abonnements));
    }

    private Response SelectAllPersonnes(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_PERSONNES.getQuery());

        Personnes personnes = new Personnes();
        while (res.next()) {
            Personne personne = new Personne();
            personne.setIdPersonne(res.getString(1));
            personne.setNom(res.getString(2));
            personne.setNom(res.getString(3));
            personne.setMail(res.getString(4));
            personne.setTel(res.getString(5));
            personne.setCodePostal(res.getString(6));

            personnes.add(personne);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(personnes));
    }

    private Response SelectAllPlaceDeParking(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_PLACE_DE_PARKING.getQuery());

        PlacesDeParkings placesDeParkings = new PlacesDeParkings();
        while (res.next()) {
            PlaceDeParking placeDeParking = new PlaceDeParking();
            placeDeParking.setIdPlace(res.getString(1));
            placeDeParking.setStatutPlace(res.getString(2));
            placeDeParking.setTypePlace(res.getString(3));
            placesDeParkings.add(placeDeParking);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(placesDeParkings));
    }


    private Response InsertVehicule(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        Vehicle vehicle = objectMapper.readValue(request.getRequestBody(), Vehicle.class);

        try (PreparedStatement pstmt = connection.prepareStatement(Queries.INSERT_VEHICULE.getQuery())) {
            pstmt.setString(1, vehicle.getNumPlaque());
            pstmt.setString(2, vehicle.getType());
            pstmt.setString(3, vehicle.getMarque());
            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "Véhicule inséré avec succès" : "Échec de l'insertion");
        }
    }

    private Response InsertAbonnement(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        Abonnement abonnement = objectMapper.readValue(request.getRequestBody(), Abonnement.class);

        try (PreparedStatement pstmt = connection.prepareStatement(Queries.INSERT_ABONNEMENT.getQuery())) {
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setDouble(2, abonnement.getPrix());
            pstmt.setString(3, abonnement.getTypeAbonnement());
            pstmt.setString(4, abonnement.getStatutAbonnement());
            pstmt.setDate(5, abonnement.getDateDebut());
            pstmt.setDate(6, abonnement.getDateFin());
            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "abonnements inséré avec succès" : "Échec de l'insertion");
        }
    }

    private Response InsertPlaceDeParking(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        PlaceDeParking placeDeParking = objectMapper.readValue(request.getRequestBody(), PlaceDeParking.class);

        try (PreparedStatement pstmt = connection.prepareStatement(Queries.INSERT_PLACE_DE_PARKING.getQuery())) {
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, placeDeParking.getTypePlace());
            pstmt.setString(3, placeDeParking.getStatutPlace());
            pstmt.setString(4, placeDeParking.getEmplacement());
            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "place de parking inséré avec succès" : "Échec de l'insertion");
        }
    }

    private Response InsertPersonne(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        Personne personne = objectMapper.readValue(request.getRequestBody(), Personne.class);

        try (PreparedStatement pstmt = connection.prepareStatement(Queries.INSERT_PERSONNE.getQuery())) {
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, personne.getNom());
            pstmt.setString(3, personne.getPrenom());
            pstmt.setString(4, personne.getMail());
            pstmt.setString(5, personne.getTel());
            pstmt.setString(6, personne.getCodePostal());
            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "personne inséré avec succès" : "Échec de l'insertion");
        }
    }


    private Response InsertLocalL(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();


        LocalLaverie localLaverie;


        try {
            localLaverie = objectMapper.readValue(request.getRequestBody(), LocalLaverie.class);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
            return new Response(request.getRequestId(), "Données de l'étudiant invalides");
        }

        if (localLaverie.getNumLocalL() == 0 || localLaverie.getDisponibilite() == null) {
            return new Response(request.getRequestId(), "Champs manquants");
        }


        try (PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_LOCAL.query)) {
            stmt.setInt(1, localLaverie.getNumLocalL());
            stmt.setBoolean(2, localLaverie.getDisponibilite());
            stmt.executeUpdate();
            return new Response(request.getRequestId(), objectMapper.writeValueAsString(localLaverie));


        } catch (SQLException e) {
            return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());
        } catch (IOException e) {
            return new Response(request.getRequestId(), "Erreur de traitement de la requête.");
        }


    }


    private Response SelectAllLocalL(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_LOCAL.query);
        LocalLaveries localLaveries = new LocalLaveries();
        while (res.next()) {

            LocalLaverie localLaverie = new LocalLaverie();
            localLaverie.setNumLocalL(res.getInt(1));
            localLaverie.setDisponibilite(res.getBoolean(2));

            localLaveries.add(localLaverie);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(localLaveries));

    }


    // Implement other methods for InsertVehicule, SelectAllAbonnements, InsertAbonnement, etc.
    // Similar to how Student and Vehicule were handled
}
