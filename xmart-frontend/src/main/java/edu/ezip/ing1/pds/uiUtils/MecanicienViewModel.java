package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.business.dto.PlacesDeParkings;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.MecanicienService;
import edu.ezip.ing1.pds.services.ReservationService;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.function.Function;

public class MecanicienViewModel {



    public static DefaultTableModel createMecanicienTableModel(NetworkConfig networkConfig, Mecaniciens mecaniciens,  Logger logger) {
        final MecanicienService mecanicienService = new MecanicienService(networkConfig);
        String[] columns = {"Nom", "Prenom", "Spécialité"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            mecaniciens = mecanicienService.selectMecanicien();
            if (mecaniciens != null && mecaniciens.getMecaniciens() != null) {
                for (Mecanicien mecanicien : mecaniciens.getMecaniciens()) {
                    model.addRow(new Object[]{mecanicien.getNom(), mecanicien.getPrenom(), mecanicien.getSpecialite()});
                }}
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur recuperation mecanicien", e);
        }
        return model;
    }
    public static DefaultTableModel createReservationTableModel(NetworkConfig networkConfig, PlacesDeParkings placesDeParkings,Logger logger) {
        final ReservationService reservationService = new ReservationService(networkConfig);
        String[] columns = {"id","emplacement", "diponibilite", "type "};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            placesDeParkings = reservationService.selectZoneSpeciale();
            if (placesDeParkings != null && placesDeParkings.getPlaceDeParkings() != null) {
                for (PlaceDeParking placeDeParking : placesDeParkings.getPlaceDeParkings()) {
                    model.addRow(new Object[]{
                            placeDeParking.getIdPlace(),
                            placeDeParking.getEmplacement(),
                            placeDeParking.getTypePlace()
                    });

                }}
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur recuperation reservation", e);
        }
        return model;
    }

    public static void insertMecanicien(DefaultTableModel model,
                                  NetworkConfig networkConfig,
                                  Component component,
                                  Logger logger,
                                  Function<String, JPanel> createTablePanelFunction,
                                  String type) {

        final MecanicienService mecanicienService = new MecanicienService(networkConfig);

        String nom = JOptionPane.showInputDialog(component, "nom du mecano :");
        if (nom == null || nom.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "nom du mecanicien ne peut etre vide", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String prenom = JOptionPane.showInputDialog(component, "Entrer son prénom :");
        String telephone = JOptionPane.showInputDialog(component, "Entrer son numero de telephone(ex:0743434343) :");
        String disponibilite= JOptionPane.showInputDialog(component, "Entrer sa disponibilité pour ce mois :");
        String specialite = JOptionPane.showInputDialog(component, "Entrer sa spécialité :");
        if(specialite == null || specialite.trim().isEmpty()) {
            do { specialite = JOptionPane.showInputDialog(component, "Entrer sa spécialité :");}
            while (specialite == null || specialite.trim().isEmpty());
        }
        String mail = JOptionPane.showInputDialog(component, "Entrer son mail :");


        Mecanicien mecanicien1 = new Mecanicien();
        mecanicien1.setNom(nom);
        mecanicien1.setPrenom(prenom);
        mecanicien1.setTelephone(telephone);
        mecanicien1.setDisponibilite(Boolean.parseBoolean(disponibilite));
        mecanicien1.setSpecialite(specialite);
        mecanicien1.setMail(mail);
        try {
            mecanicienService.insertMecanicien(mecanicien1);
            model.addRow(new Object[]{mecanicien1.getNom(), mecanicien1.getPrenom(), mecanicien1.getSpecialite()});
            JOptionPane.showMessageDialog(component, "Mecanicien Inséré.");
            JPanel refreshedPanel = createTablePanelFunction.apply(type);
            logger.debug("Panel refreshed for type {}: {}", type, refreshedPanel.getName());
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur insertion Mecanicien", e);
            JOptionPane.showMessageDialog(component, "Erreur insertion Mecanicien.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
