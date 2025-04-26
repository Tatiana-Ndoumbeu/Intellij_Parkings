package edu.ezip.ing1.pds.business.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.commons.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.Date;

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
        DELETE_ABONNEMENT("DELETE FROM Abonnement t WHERE t.id_abonnement = ?"),
        UPDATE_ABONNEMENT("UPDATE Abonnement SET typeAbonnement= ?, prix= ?, statutAbonnement= ?, dateDebut=?, dateFin=?, WHERE id_abonnement=?"),


        SELECT_ALL_PERSONNES("SELECT t.id_personne, t.nom, t.prenom, t.tel, t.mail, t.code_postal FROM Personne t"),
        INSERT_PERSONNE("INSERT INTO Personne (id_personne, nom, prenom, tel, mail, code_postal) VALUES (?, ?, ?,?, ?, ?)"),

        SELECT_ALL_MECANICIEN("SELECT m.nom, m.prenom, m.telephone, m.disponibilite, m.specialite,m.mail FROM Mecanicien m"),
        INSERT_MECANICIEN("INSERT INTO Mecanicien (nom, prenom, telephone, disponibilite, specialite, mail) VALUES (?, ?, ?,?, ?, ?)"),

        SELECT_ALL_LOCAL("SELECT l.NumLocalL, l.disponibilite FROM LocalLaverie l"),
        INSERT_LOCAL("INSERT into LocalLaverie (NumLocalL, disponibilite) VALUES (? , ?)"),
        UPDATE_LOCAL("UPDATE LocalLaverie SET disponibilite = ? WHERE NumLocalL = ?"),
        DELETE_LOCAL("DELETE FROM LocalLaverie WHERE NumLocalL = ?"),
        SELECT_DISPO_LOCAUX("SELECT l.NumLocalL FROM LocalLaverie l WHERE disponibilite = true"),

        SELECT_ALL_RESERVATION_LOCAL("SELECT * FROM Reservation_local"),
        SELECT_ALL_RESERVATION_LOCAL_MOIS("SELECT * FROM Reservation_local WHERE (YEAR(date_debut) = ? AND MONTH(date_debut) = ?) OR (YEAR(date_fin) = ? AND MONTH(date_fin) = ?)"),
        INSERT_RESERVATION_LOCAL("INSERT INTO Reservation_local (numero_local, date_debut, date_fin, heure_entree, heure_sortie, type_local, id_personne) VALUES (?, ?, ?, ?, ?, ?, ?)"),

        SELECT_ALL_LOCAL_T("SELECT l.numLocalT, l.disponibilite FROM LocalTechnique l"),
        INSERT_LOCAL_T("INSERT into LocalTechnique (numLocalT, disponibilite) VALUES (? , ?)"),
        UPDATE_LOCAL_T("UPDATE LocalTechnique SET disponibilite = ? WHERE numLocalT = ?"),
        DELETE_LOCAL_T("DELETE FROM LocalTechnique WHERE numLocalT = ?"),
        SELECT_DISPO_LOCAUX_T("SELECT l.numLocalT FROM LocalTechnique l WHERE disponibilite = true"),

        INSERT_UTILISATEUR("INSERT INTO Admin (id_admin, nom, email, mot_de_passe) VALUES (?, ?, ?, ?)"),
        LOGIN_UTILISATEUR("SELECT COUNT(*) FROM Admin WHERE email = ? AND mot_de_passe = ?"),
        SELECT_ALL_UTILISATEUR("SELECT id_admin, nom, email FROM Admin"),

        SELECT_ZONE_SPE_PLACE_DE_PARKING("SELECT t.id_place, t.emplacement, t.type_place, t.statut_place FROM PlaceDeParking t WHERE t.type_place <> 'simple'"),
        SELECT_ALL_RESERVATIONS("SELECT r.idReservation, r.dateReservation, r.heure, r.dateEntree, r.dateSortie, t.id_place, t.statut_place, t.emplacement, t.type_place \n" +
                "FROM Reservation r \n" +
                "INNER JOIN PlaceDeParking t ON t.id_place = r.id_place;\n"),
        INSERT_RESERVATION("INSERT into Reservation (idReservation, dateReservation, heure, dateEntree, dateSortie, heureEntree, heureSortie, id_Personne, id_place) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "),

        SELECT_ALL_PLACE_DE_PARKING("SELECT t.id_place, t.emplacement, t.type_place, t.statut_place FROM PlaceDeParking t"),
        INSERT_PLACE_DE_PARKING("INSERT INTO PlaceDeParking (id_place, emplacement, type_place, statut_place) VALUES (?,?, ?, ?)"),
        UPDATE_PLACE_DE_PARKING("UPDATE PlaceDeParking t  SET t.emplacement = ?, t.type_place = ?, t.statut_place = ? WHERE t.id_place = ? "),
        //UPDATE_PLACE_DE_PARKING("UPDATE PlaceDeParking SET statut_place = ? WHERE position = ?"),
        DELETE_PLACE_DE_PARKING("DELETE FROM PlaceDeParking t WHERE t.id_place = ?");

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
            case DELETE_ABONNEMENT:
                response = SupprimerAbonnement(request, connection);
                break;
            case INSERT_UTILISATEUR:
                response = insertAdmin(request, connection);
                break;
            case LOGIN_UTILISATEUR:
                response = loginAdmin(request, connection);
                break;
            case SELECT_ALL_UTILISATEUR:
                response = selectAllAdmins(request, connection);
                break;

            case UPDATE_ABONNEMENT:
                response = UpdateAbonnement(request, connection);
                break;
            case SELECT_ALL_PERSONNES:
                response = SelectAllPersonnes(request, connection);
                break;
            case INSERT_LOCAL:
                response = InsertLocalL(request, connection);
                break;
            case INSERT_LOCAL_T:
                response = InsertLocalT(request, connection);
                break;
            case SELECT_ALL_LOCAL:
                response = SelectAllLocalL(request, connection);
                break;
            case SELECT_DISPO_LOCAUX:
                response = SelectLocalLDispo(request, connection);
                break;
            case SELECT_DISPO_LOCAUX_T:
                response = SelectLocalTDispo(request, connection);
                break;
            case SELECT_ALL_LOCAL_T:
                response = SelectAllLocalT(request, connection);
                break;
            case UPDATE_LOCAL:
                response = UpdateLocal(request, connection);
                break;
            case UPDATE_LOCAL_T:
                response = UpdateLocalT(request, connection);
                break;
            case DELETE_LOCAL:
                response = supprimerLocal(request, connection);
                break;
            case DELETE_LOCAL_T:
                response = supprimerLocalT(request, connection);
                break;
            case SELECT_ALL_RESERVATION_LOCAL:
                response = selectAllReservationLocal(request, connection);
                break;
            case SELECT_ALL_RESERVATION_LOCAL_MOIS:
                response = selectReservationLocalParMois(request, connection);
                break;
            case INSERT_RESERVATION_LOCAL:
                response = insertReservationLocal(request, connection);
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
                case UPDATE_PLACE_DE_PARKING:
                    response = UpdatePlaceDeParking(request, connection);
                    break;
            case DELETE_PLACE_DE_PARKING:
                    response = DeletePlaceDeParking(request, connection);
                    break;
            case SELECT_ZONE_SPE_PLACE_DE_PARKING:
                response = selectZoneSpeciale(request, connection);
                break;
            case INSERT_RESERVATION:
                response=InsertReservation(request, connection);
                break;
            case SELECT_ALL_MECANICIEN:
                response = SelectAllMecaniciens(request, connection);
                break;
            case INSERT_MECANICIEN:
                response = InsertMecanicien(request, connection);
                break;
            case SELECT_ALL_RESERVATIONS:
                response = SelectAllReservations(request, connection);
                break;
            default:
                break;
        }

        return response;
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
            abonnement.setPrix(res.getDouble(3));
            abonnement.setTypeAbonnement(res.getString(2));
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
            personne.setTelephone(res.getString(4));
            personne.setMail(res.getString(5));
            personne.setCodePostal(res.getString(6));

            personnes.add(personne);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(personnes));
    }

    private Response SelectAllPlaceDeParking(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_PLACE_DE_PARKING.getQuery());
//UPDATE PlaceDeParking t SET t.emplacement = ?, t.type_place = ?, t.statut_place = ? WHERE t.id_place = ?
        PlacesDeParkings placesDeParkings = new PlacesDeParkings();
        while (res.next()) {
            PlaceDeParking placeDeParking = new PlaceDeParking();
            placeDeParking.setIdPlace(res.getString(1));
            placeDeParking.setEmplacement(res.getString(2));
            placeDeParking.setTypePlace(res.getString(3));
            placeDeParking.setStatutPlace(res.getString(4));
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
            pstmt.setString(1, UUID.randomUUID().toString().substring(0, 8)); //Prend les 8 premiers caractères Sde l'UUID
            pstmt.setDouble(3, abonnement.getPrix());
            pstmt.setString(2, abonnement.getTypeAbonnement());
            pstmt.setString(4, abonnement.getStatutAbonnement());
            pstmt.setDate(5, abonnement.getDateDebut());
            pstmt.setDate(6, abonnement.getDateFin());

            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "abonnement inséré avec succès" : "Échec de l'insertion");
        }
    }

    private Response SupprimerAbonnement(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        Abonnement abonnement = objectMapper.readValue(request.getRequestBody(), Abonnement.class);
    
        try (PreparedStatement pstmt = connection.prepareStatement(Queries.DELETE_ABONNEMENT.getQuery())) {
            pstmt.setString(1, abonnement.getIdAbonnement());

            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "Abonnement supprimée avec succès" : "Échec de la suppression");

        }
    }
    private Response UpdateAbonnement(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        Abonnement abonnement;
        try {
            abonnement= objectMapper.readValue(request.getRequestBody(), Abonnement.class);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
            return new Response(request.getRequestId(), "Données invalides");
        }


        if (abonnement.getTypeAbonnement() == null || abonnement.getPrix() == 0 || abonnement.getStatutAbonnement() == null) {
            return new Response(request.getRequestId(), "Champs manquants");
        }


        try (PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_ABONNEMENT.getQuery())) {
            stmt.setString(1, abonnement.getIdAbonnement());
            stmt.setString(2, abonnement.getTypeAbonnement());
            stmt.setDouble(3, abonnement.getPrix());
            stmt.setString(4, abonnement.getStatutAbonnement());
            stmt.setDate(5, abonnement.getDateDebut() );
            stmt.setDate(6, abonnement.getDateFin() );
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return new Response(request.getRequestId(), "Abonnement mis à jour avec succès");
            } else {
                return new Response(request.getRequestId(), "Aucun abonnement trouvé avec cet ID");
            }

        } catch (SQLException e) {
            logger.error("Erreur SQL lors de la mise à jour de l'abonnement", e);
            return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());
        }
    }

    

    private Response InsertPlaceDeParking(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        PlaceDeParking placeDeParking = objectMapper.readValue(request.getRequestBody(), PlaceDeParking.class);

        try (PreparedStatement pstmt = connection.prepareStatement(Queries.INSERT_PLACE_DE_PARKING.getQuery())) {
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, placeDeParking.getEmplacement());
            pstmt.setString(3, placeDeParking.getTypePlace());
            pstmt.setString(4, placeDeParking.getStatutPlace());
            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "place de parking inséré avec succès" : "Échec de l'insertion");
        }
    }

    private Response DeletePlaceDeParking(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        PlaceDeParking placeDeParking = objectMapper.readValue(request.getRequestBody(), PlaceDeParking.class);

        try (PreparedStatement pstmt = connection.prepareStatement(Queries.DELETE_PLACE_DE_PARKING.getQuery())) {
            pstmt.setString(1, placeDeParking.getIdPlace()); // Use the ID to delete

            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "Place de parking supprimée avec succès" : "Échec de la suppression");
        }
    }



    private Response UpdatePlaceDeParking(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        PlaceDeParking placeDeParking = objectMapper.readValue(request.getRequestBody(), PlaceDeParking.class);

        try (PreparedStatement pstmt = connection.prepareStatement(Queries.UPDATE_PLACE_DE_PARKING.getQuery())) {
            pstmt.setString(1, placeDeParking.getEmplacement());
            pstmt.setString(2, placeDeParking.getTypePlace());
            pstmt.setString(3, placeDeParking.getStatutPlace());
            pstmt.setString(4, placeDeParking.getIdPlace()); // Use the existing ID for WHERE condition

            int affectedRows = pstmt.executeUpdate();
            return new Response(request.getRequestId(), affectedRows > 0 ? "Place de parking mise à jour avec succès" : "Échec de la mise à jour");
        }
    }


    private Response InsertPersonne(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();

        Personne personne;

        try {
            personne = objectMapper.readValue(request.getRequestBody(), Personne.class);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
            return new Response(request.getRequestId(), "Données  invalides");

        }

        try (PreparedStatement pstmt = connection.prepareStatement(Queries.INSERT_PERSONNE.getQuery())) {
            //pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(1, personne.getIdPersonne());
            pstmt.setString(2, personne.getNom());
            pstmt.setString(3, personne.getPrenom());
            pstmt.setString(4, personne.getTelephone());
            pstmt.setString(5, personne.getMail());
            pstmt.setString(6, personne.getCodePostal());

            pstmt.executeUpdate();
            return new Response(request.getRequestId(), objectMapper.writeValueAsString(personne));
        }
         catch (SQLException e) {
        return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());

        } catch (IOException e) {
            return new Response(request.getRequestId(), "Erreur de traitement de la requête.");
        }
    }


    private Response InsertLocalL(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();


        LocalLaverie localLaverie;


        try {
            localLaverie = objectMapper.readValue(request.getRequestBody(), LocalLaverie.class);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
            return new Response(request.getRequestId(), "Données du local invalides");
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
    private Response SelectLocalLDispo(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_DISPO_LOCAUX.query);
        LocalLaveries localLaveries = new LocalLaveries();
        while (res.next()) {

            LocalLaverie localLaverie = new LocalLaverie();
            localLaverie.setNumLocalL(res.getInt(1));
            localLaverie.setDisponibilite(res.getBoolean(2));

            localLaveries.add(localLaverie);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(localLaveries));

    }

    private Response UpdateLocal(final Request request, final Connection connection) throws SQLException, IOException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();

        LocalLaverie localLaverie;
        try {
            localLaverie = objectMapper.readValue(request.getRequestBody(), LocalLaverie.class);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
            return new Response(request.getRequestId(), "Données invalides");
        }
    

        if (localLaverie.getNumLocalL() == 0 || localLaverie.getDisponibilite() == null) {
            return new Response(request.getRequestId(), "Champs manquants");
        }
    

        try (PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_LOCAL.getQuery())) {
            stmt.setBoolean(1, localLaverie.getDisponibilite()); 
            stmt.setInt(2, localLaverie.getNumLocalL()); 
            
            int affectedRows = stmt.executeUpdate(); 
            
            if (affectedRows > 0) {
                return new Response(request.getRequestId(), "Local mis à jour avec succès");
            } else {
                return new Response(request.getRequestId(), "Aucun local trouvé avec cet ID");
            }
    
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de la mise à jour du local", e);
            return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());
        }
    }
    private Response supprimerLocal(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            String requestBody = request.getRequestBody();
            LocalLaverie localLaverie = objectMapper.readValue(requestBody, LocalLaverie.class);

            if (localLaverie.getNumLocalL() == 0) {
                return new Response(request.getRequestId(), "Numéro de local invalide");
            }

            try (PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_LOCAL.getQuery())) {
                stmt.setInt(1, localLaverie.getNumLocalL());
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    return new Response(request.getRequestId(), "Local supprimé avec succès");
                } else {
                    return new Response(request.getRequestId(), "Aucun local trouvé avec cet ID");
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
            return new Response(request.getRequestId(), "Données invalides");
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de la suppression du local", e);
            return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());
        }
    }

private Response InsertLocalT(final Request request, final Connection connection) throws SQLException, IOException {

    final ObjectMapper objectMapper = new ObjectMapper();


    LocalTechnique localTechnique;


    try {
        localTechnique = objectMapper.readValue(request.getRequestBody(), LocalTechnique.class);
    } catch (JsonProcessingException e) {
        logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
        return new Response(request.getRequestId(), "Données du local invalides");
    }

    if (localTechnique.getNumLocalT() == 0 || localTechnique.getDisponibilite() == null) {
        return new Response(request.getRequestId(), "Champs manquants");
    }


    try (PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_LOCAL_T.query)) {
        stmt.setInt(1, localTechnique.getNumLocalT());
        stmt.setBoolean(2, localTechnique.getDisponibilite());
        stmt.executeUpdate();
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(localTechnique));


    } catch (SQLException e) {
        return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());
    } catch (IOException e) {
        return new Response(request.getRequestId(), "Erreur de traitement de la requête.");
    }


}


private Response SelectAllLocalT(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
    final ObjectMapper objectMapper = new ObjectMapper();
    final Statement stmt = connection.createStatement();
    final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_LOCAL_T.query);
    LocalTechniques localTechniques = new LocalTechniques();
    while (res.next()) {

        LocalTechnique localTechnique = new LocalTechnique();
        localTechnique.setNumLocalT(res.getInt(1));
        localTechnique.setDisponibilite(res.getBoolean(2));

        localTechniques.add(localTechnique);
    }
    return new Response(request.getRequestId(), objectMapper.writeValueAsString(localTechniques));

}

private Response UpdateLocalT(final Request request, final Connection connection) throws SQLException, IOException, JsonProcessingException {
    final ObjectMapper objectMapper = new ObjectMapper();

    LocalTechnique localTechnique;
    try {
        localTechnique = objectMapper.readValue(request.getRequestBody(), LocalTechnique.class);
    } catch (JsonProcessingException e) {
        logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
        return new Response(request.getRequestId(), "Données invalides");
    }


    if (localTechnique.getNumLocalT() == 0 || localTechnique.getDisponibilite() == null) {
        return new Response(request.getRequestId(), "Champs manquants");
    }


    try (PreparedStatement stmt = connection.prepareStatement(Queries.UPDATE_LOCAL_T.getQuery())) {
        stmt.setBoolean(1, localTechnique.getDisponibilite());
        stmt.setInt(2, localTechnique.getNumLocalT());

        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
            return new Response(request.getRequestId(), "Local mis à jour avec succès");
        } else {
            return new Response(request.getRequestId(), "Aucun local trouvé avec cet ID");
        }

    } catch (SQLException e) {
        logger.error("Erreur SQL lors de la mise à jour du local", e);
        return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());
    }
}
    private Response supprimerLocalT(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            String requestBody = request.getRequestBody();
            LocalTechnique localTechnique = objectMapper.readValue(requestBody, LocalTechnique.class);

            if (localTechnique.getNumLocalT() == 0) {
                return new Response(request.getRequestId(), "Numéro de local invalide");
            }

            try (PreparedStatement stmt = connection.prepareStatement(Queries.DELETE_LOCAL_T.getQuery())) {
                stmt.setInt(1, localTechnique.getNumLocalT());
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    return new Response(request.getRequestId(), "Local supprimé avec succès");
                } else {
                    return new Response(request.getRequestId(), "Aucun local trouvé avec ce numero");
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
            return new Response(request.getRequestId(), "Données invalides");
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de la suppression du local", e);
            return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());
        }
    }
    private Response SelectLocalTDispo(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_DISPO_LOCAUX_T.query);
        LocalTechniques localTechniques = new LocalTechniques();
        while (res.next()) {

            LocalTechnique localTechnique = new LocalTechnique();
            localTechnique.setNumLocalT(res.getInt(1));
            localTechnique.setDisponibilite(res.getBoolean(2));

            localTechniques.add(localTechnique);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(localTechniques));

    }
    private Response SelectAllMecaniciens(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_MECANICIEN.query);
        Mecaniciens mecaniciens = new Mecaniciens();

        while (res.next()) {
            Mecanicien mecanicien = new Mecanicien();
            mecanicien.setNom(res.getString(1));
            mecanicien.setPrenom(res.getString(2));
            mecanicien.setTelephone(res.getString(3));
            mecanicien.setDisponibilite(res.getBoolean(4));
            mecanicien.setSpecialite(res.getString(5));
            mecanicien.setMail(res.getString(6));
            mecaniciens.add(mecanicien);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(mecaniciens));

    }
    private Response InsertMecanicien(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        Mecanicien mecanicien;
        try {
            mecanicien = objectMapper.readValue(request.getRequestBody(), Mecanicien.class);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
            return new Response(request.getRequestId(), "Données du mecanicien invalides");
        }

        if (mecanicien.getNom() == null || mecanicien.getTelephone() == null) {
            return new Response(request.getRequestId(), "Champs nom ou/et telephone manquants");
        }


        try (PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_MECANICIEN.query)) {
            stmt.setString(1, mecanicien.getNom());
            stmt.setString(2, mecanicien.getPrenom());
            stmt.setString(3, mecanicien.getTelephone());
            stmt.setBoolean(4, mecanicien.getDisponibilite());
            stmt.setString(5, mecanicien.getSpecialite());
            stmt.setString(6, mecanicien.getMail());
            stmt.executeUpdate();
            return new Response(request.getRequestId(), objectMapper.writeValueAsString(mecanicien));

        } catch (SQLException e) {
            return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());
        } catch (IOException e) {
            return new Response(request.getRequestId(), "Erreur de traitement de la requête.");
        }


    }
    private Response selectZoneSpeciale(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ZONE_SPE_PLACE_DE_PARKING.query);
        PlacesDeParkings placesDeParkings = new PlacesDeParkings();

        while (res.next()) {
            PlaceDeParking placeDeParking = new PlaceDeParking();
            placeDeParking.setIdPlace(res.getString(1));
            placeDeParking.setEmplacement(res.getString(2));
            placeDeParking.setTypePlace(res.getString(3));
            placeDeParking.setStatutPlace(res.getString(4));
            placesDeParkings.add(placeDeParking);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(placesDeParkings));

    }

    private Response InsertReservation(final Request request, final Connection connection) throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        Reservation reservation;
        try {
          ReservationRequest  reservationRequest = objectMapper.readValue(request.getRequestBody(), ReservationRequest.class);
          reservation = ReservationMapper.toReservation(reservationRequest);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing du JSON: {}", request.getRequestBody(), e);
            return new Response(request.getRequestId(), "Données de reservation invalides");
        }

        if (reservation.getDateReservation() == null || reservation.getDateEntree() == null) {
            return new Response(request.getRequestId(), "Champs  manquants");
        }


        try (PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_RESERVATION.query)) {
            stmt.setString(1, reservation.getIdReservation());
            stmt.setDate(2, java.sql.Date.valueOf(reservation.getDateReservation()));
            stmt.setTime(3, java.sql.Time.valueOf(reservation.getHeure()));
            stmt.setDate(4, new java.sql.Date(reservation.getDateEntree().getTime()));
            stmt.setDate(5, new java.sql.Date(reservation.getDateSortie().getTime()));
            stmt.setTime(6, new java.sql.Time(reservation.getDateEntree().getTime()));
            stmt.setTime(7,  new java.sql.Time(reservation.getHeureSortie().getTime()));
            stmt.setString(8, reservation.getIdPersonne());
            stmt.setString(9, reservation.getPlaceDeParking().getIdPlace());
            stmt.executeUpdate();
            return new Response(request.getRequestId(), objectMapper.writeValueAsString(reservation));

        } catch (SQLException e) {
            return new Response(request.getRequestId(), "Erreur SQL : " + e.getMessage());
        } catch (IOException e) {
            return new Response(request.getRequestId(), "Erreur de traitement de la requête.");
        }


    }
    private Response SelectAllReservations(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_RESERVATIONS.query);
        ReservationsResquests reservations = new ReservationsResquests();

        while (res.next()) {
            ReservationRequest reservation = new ReservationRequest();
            reservation.setIdReservation(res.getString("idReservation"));
            reservation.setDateReservation(res.getDate("dateReservation").toLocalDate().toString());
            reservation.setHeure(res.getTime("heure").toLocalTime().toString());
            reservation.setDateEntree(res.getDate("dateEntree").toString());
            reservation.setDateSortie(res.getDate("dateSortie").toString());


            PlaceDeParking placeDeParking = new PlaceDeParking();
            placeDeParking.setIdPlace(res.getString("id_place"));
            placeDeParking.setTypePlace(res.getString("type_place"));
            placeDeParking.setStatutPlace(res.getString("statut_place"));
            placeDeParking.setEmplacement(res.getString("emplacement"));

            reservation.setPlaceDeParking(placeDeParking); //pour les associer

            reservations.add(reservation);

        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(reservations));

    }
    private Response selectAllReservationLocal(final Request request, final Connection connection)
            throws SQLException, JsonProcessingException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_RESERVATION_LOCAL.query);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ReservationLocaux reservationLocaux = new ReservationLocaux();

        while (res.next()) {
            ReservationLocal reservationLocal = new ReservationLocal();
            reservationLocal.setNumLocal(res.getInt("numero_local"));

            Date dateDebut = res.getDate("date_debut");
            Date dateFin = res.getDate("date_fin");

            reservationLocal.setDateDebut(dateDebut != null ? dateDebut.toLocalDate().format(dateFormatter) : null);
            reservationLocal.setDateFin(dateFin != null ? dateFin.toLocalDate().format(dateFormatter) : null);
            Time heureEntree = res.getTime("heure_entree");
            Time heureSortie = res.getTime("heure_sortie");

            reservationLocal.setHeureEntree(heureEntree != null ? heureEntree.toLocalTime().format(timeFormatter) : null);
            reservationLocal.setHeureSortie(heureSortie != null ? heureSortie.toLocalTime().format(timeFormatter) : null);

            reservationLocal.setTypeLocal(res.getString("type_local"));

            reservationLocaux.add(reservationLocal);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(reservationLocaux));
    }

    private Response selectReservationLocalParMois(final Request request, final Connection connection)
            throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final ReservationLocalParMois params = objectMapper.readValue(
                request.getRequestBody(), ReservationLocalParMois.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.SELECT_ALL_RESERVATION_LOCAL_MOIS.query);

        stmt.setInt(1, params.getAnnee());
        stmt.setInt(2, params.getMois());
        stmt.setInt(3, params.getAnnee());
        stmt.setInt(4, params.getMois());

        final ResultSet res = stmt.executeQuery();
        ReservationLocaux reservationLocaux = new ReservationLocaux();

        while (res.next()) {
            ReservationLocal reservationLocal = new ReservationLocal();
            reservationLocal.setNumLocal(res.getInt("numero_local"));
            reservationLocal.setDateDebut(res.getString("date_debut"));
            reservationLocal.setDateFin(res.getString("date_fin"));
            reservationLocal.setHeureEntree(res.getString("heure_entree"));
            reservationLocal.setHeureSortie(res.getString("heure_sortie"));
            reservationLocal.setTypeLocal(res.getString("type_local"));

            reservationLocaux.add(reservationLocal);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(reservationLocaux));
    }

    private Response insertReservationLocal(final Request request, final Connection connection)
            throws SQLException, IOException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final ReservationLocal reservationLocal = objectMapper.readValue(
                request.getRequestBody(), ReservationLocal.class);
        System.out.println("DEBUG - ReservationLocal reçue : " + reservationLocal);


        try (final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_RESERVATION_LOCAL.query)) {

            stmt.setInt(1, reservationLocal.getNumLocal());
            stmt.setDate(2, java.sql.Date.valueOf(reservationLocal.getDateDebut()));
            stmt.setDate(3, java.sql.Date.valueOf(reservationLocal.getDateFin()));
            stmt.setTime(4, java.sql.Time.valueOf(reservationLocal.getHeureEntree() + ":00"));
            stmt.setTime(5, java.sql.Time.valueOf(reservationLocal.getHeureSortie() + ":00"));
            stmt.setString(6, reservationLocal.getTypeLocal());
            stmt.setString(7, reservationLocal.getIdPersonne());


            stmt.executeUpdate();

            return new Response(request.getRequestId(), objectMapper.writeValueAsString(reservationLocal));
        }catch (SQLException e) {
            System.err.println("Erreur d'insertion dans la base de données : " + e.getMessage());
            e.printStackTrace();
            return new Response(request.getRequestId(), "Erreur d'insertion dans la base de données.");
        }
    }

    private Response insertAdmin(final Request request, final Connection connection) throws SQLException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Admin admin = objectMapper.readValue(request.getRequestBody(), Admin.class);

        if (admin.getName() == null || admin.getEmail() == null || admin.getPassword() == null) {
            return new Response(request.getRequestId(), "Champs manquants");
        }

        try (PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_UTILISATEUR.query)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, admin.getName());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPassword());
            int affectedRows = stmt.executeUpdate();

            return new Response(request.getRequestId(), affectedRows > 0 ? "Inscription réussie" : "Erreur à l'inscription");
        }
    }

    private Response loginAdmin(final Request request, final Connection connection) throws SQLException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Admin admin = objectMapper.readValue(request.getRequestBody(), Admin.class);

        try (PreparedStatement stmt = connection.prepareStatement(Queries.LOGIN_UTILISATEUR.query)) {
            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getPassword());

            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new Response(request.getRequestId(), "OK");
            } else {
                return new Response(request.getRequestId(), "INVALID");
            }
        }
    }
    private Response selectAllAdmins(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_UTILISATEUR.query);

        List<Admin> admins = new ArrayList<>();
        while (res.next()) {
            Admin admin = new Admin();
            admin.setEmail(res.getString("id_admin"));
            admin.setName(res.getString("nom"));
            admin.setEmail(res.getString("email"));
            admins.add(admin);
        }

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(admins));
    }


}