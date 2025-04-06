package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.*;

import edu.ezip.ing1.pds.Interface.Principale;

import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.*;
//import edu.ezip.ing1.pds.services.StudentService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.*;

public class MainFrontEnd {

    private final static String LoggingLabel = "FrontEnd";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String networkConfigFile = "network.yaml";
    private static final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

    public static void main(String[] args) throws IOException, InterruptedException {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        logger.debug("Load Network config file : {}", networkConfig.toString());

        //final StudentService studentService = new StudentService(networkConfig);
        final PersonneService personneService = new PersonneService(networkConfig);
        final AbonementService abonementService = new AbonementService(networkConfig);
        final VehicleService vehicleService = new VehicleService(networkConfig);
        final PlaceDeParkingService placeDeParkingService = new PlaceDeParkingService(networkConfig);
        //studentService.insertStudents();
        //personneService.insertPersonnes();
       //abonementService.insertAbonements();
         //vehicleService.insertVehicles();
        //placeDeParkingService.insertPlaceDeParkings();
        // Students students = studentService.selectStudents();
        Personnes personnes = personneService.selectPersonnes();
        Abonnements abonnements = abonementService.selectAbonnements();
        Vehicles vehicles = vehicleService.selectVehicles();
        PlacesDeParkings placeDeParkings = placeDeParkingService.selectPlaceDeParkings();
        //final AsciiTable asciiTable = new AsciiTable();

        // ajout d'une vérification que apiRequest et getStudents ne sont pas nuls pour éviter l'exception du pointeur nul
       // if (students != null && students.getStudents() != null) {
            /*for (final Student student : students.getStudents()) {
                asciiTable.addRule();
                asciiTable.addRow(student.getFirstname(), student.getName(), student.getGroup());
            }
            asciiTable.addRule();
            logger.debug("\n{}\n", asciiTable.render());
        }*/

       /*  if (abonnements != null && abonnements.getAbonnements() != null) {
            for (final Abonnement abonnement : abonnements.getAbonnements()) {
                asciiTable.addRule();
                asciiTable.addRow(abonnement.getIdAbonnement(), abonnement.getTypeAbonnement(), abonnement.getPrix());
            }
            asciiTable.addRule();
            logger.debug("\n{}\n", asciiTable.render());
        }

        if (placeDeParkings != null && placeDeParkings.getPlaceDeParkings() != null) {
            for (final PlaceDeParking placeDeParking : placeDeParkings.getPlaceDeParkings()) {
                asciiTable.addRule();
                asciiTable.addRow(placeDeParking.getIdPlace(), placeDeParking.getEmplacement(), placeDeParking.getEmplacement());
            }
            asciiTable.addRule();
            logger.debug("\n{}\n", asciiTable.render());
        }
        if (personnes != null && personnes.getPersonnes() != null) {
            for (final Personne student : personnes.getPersonnes()) {
                asciiTable.addRule();
                asciiTable.addRow(student.getIdPersonne(), student.getNom(), student.getPrenom());
            }
            asciiTable.addRule();
            logger.debug("\n{}\n", asciiTable.render());
        }
        if (vehicles != null && vehicles.getVehicules() != null) {
            for (final Vehicle vehicle : vehicles.getVehicules()) {
                asciiTable.addRule();
                asciiTable.addRow(vehicle.getNumPlaque(), vehicle.getType(), vehicle.getMarque());
            }
            asciiTable.addRule();
            logger.debug("\n{}\n", asciiTable.render());
        }
        /*if (students != null && students.getStudents() != null) {
            for (final Student student : students.getStudents()) {
                asciiTable.addRule();
                asciiTable.addRow(student.getFirstname(), student.getName(), student.getGroup());
            }
            asciiTable.addRule();
            logger.debug("\n{}\n", asciiTable.render());
        }

        */

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        } // pour faire plus beau
        SwingUtilities.invokeLater(() -> new Principale().setVisible(true));
      

    }


}

