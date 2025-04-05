package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Abonnements;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.AbonementService;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.io.IOException;
import java.sql.Date;

import static edu.ezip.ing1.pds.MainFrontEndSwing.networkConfigFile;

public class AbonnementViewModel {


    public static   void supprimerAbonnement(DefaultTableModel model, JTable table, Component component, Logger logger){
        String idAbonnement = JOptionPane.showInputDialog(component , "Entrez l'identifiant de l'abonnement à supprimer :",
                "Suppression d'un abonnement", JOptionPane.QUESTION_MESSAGE);

        if (idAbonnement == null || idAbonnement.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component , "L'identifiant est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(component , "Voulez-vous vraiment supprimer cet abonnement ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                AbonementService abonementService = new AbonementService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
                abonementService.supprimerAbonnement(idAbonnement);
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(idAbonnement)) {
                        model.removeRow(i);
                        break;
                    }
                }
                JOptionPane.showMessageDialog(component , "Abonnement supprimé avec succès !");
            } catch (Exception e) {
                logger.error("Erreur lors de la suppression de l'abonnement", e);
                JOptionPane.showMessageDialog(component , "Erreur lors de la suppression de l'abonnement.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public static   void updateAbonnement(DefaultTableModel model,Component component, Logger logger) {

        String idAbo = JOptionPane.showInputDialog(component , "Identifiant de l'abonnement à modifier :");
        AbonementService abonementService = new AbonementService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
        if (idAbo == null || idAbo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component , "L'identifiant ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String typeAbo;
        while (true) {
            typeAbo = JOptionPane.showInputDialog(component , "Nouveau type de l'abonnement :");
            if (typeAbo == null || typeAbo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(component , "Le type de l'abonnement ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!isValidType(typeAbo)) {
                JOptionPane.showMessageDialog(component , "Type d'abonnement invalide. Choisissez parmi : Premium, Standard", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            break;
        }
        String prix;
        while (true) {
            prix = JOptionPane.showInputDialog(component , "Nouveau prix de l'abonnement :");
            if (prix == null || prix.trim().isEmpty()) {
                JOptionPane.showMessageDialog(component , "Le prix de l'abonnement ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!isValidPrix(prix)) {
                JOptionPane.showMessageDialog(component , "Prix invalide : entrez un nombre positif", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            break;
        }
        String statut;
        while (true) {
            statut = JOptionPane.showInputDialog(component , "Nouveau statut de l'abonnement :");
            if (statut == null || statut.trim().isEmpty()) {
                JOptionPane.showMessageDialog(component , "Le statut de l'abonnement ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!isValidStatut(statut)) {
                JOptionPane.showMessageDialog(component , "Statut invalide : choisissez parmi (Actif, Inactif ou Suspendu)", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            break;
        }

        Abonnement AboUpdated= new Abonnement();
        AboUpdated.setIdAbonnement(idAbo);
        AboUpdated.setTypeAbonnement(typeAbo);
        AboUpdated.setPrix(Double.parseDouble(prix));
        AboUpdated.setStatutAbonnement(statut);

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(idAbo)) {
                model.setValueAt(typeAbo, i, 1);
                model.setValueAt(prix, i, 2);
                model.setValueAt(statut, i, 3);
                break;
            }
        }


    }

    public static   boolean isValidType(String type) {
        return type != null && (type.equalsIgnoreCase("Premium") || type.equalsIgnoreCase("Standard"));
    }
    public static   boolean isValidPrix(String prix) {
        try {
            return prix != null && Double.parseDouble(prix) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }public static   boolean isValidStatut(String statut) {
        return statut != null && (statut.equalsIgnoreCase("Actif") || statut.equalsIgnoreCase("Inactif") || statut.equalsIgnoreCase("Suspendu"));
    }



    public static DefaultTableModel createAbonnementTableModel(NetworkConfig networkConfig, Abonnements abonnements,Logger logger) {
        final AbonementService abonementService = new AbonementService(networkConfig);
        String[] columns = { "idAbonnement", "typeAbonnement", "prix", "statutAbonnement", "dateDebut", "dateFin"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Récupère et affiche les places de parking existantes depuis BD
        try {
            abonnements = abonementService.selectAbonnements();
            if (abonnements != null && abonnements.getAbonnements() != null) {
                for (Abonnement place : abonnements.getAbonnements()) {
                    model.addRow(new Object[]{
                            place.getIdAbonnement(),
                            place.getTypeAbonnement(),
                            place.getPrix(),
                            place.getStatutAbonnement(),
                            place.getDateDebut(),
                            place.getDateFin()});
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching places de parking", e);
        }

        return model;
    }


    public static   void insertAbonnements(DefaultTableModel model, Component component, Logger logger) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final AbonementService abonementService = new AbonementService(networkConfig);

        String typeAbonnement;
        while (true) {
            typeAbonnement = JOptionPane.showInputDialog(component  , "Entrer le type d'abonnement (Premium ou Standard)");
            if (typeAbonnement == null) {
                JOptionPane.showMessageDialog(component  , "Opération annulée.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (isValidType(typeAbonnement)) {
                break;
            }
            JOptionPane.showMessageDialog(component  , "Type d'abonnement invalide. Choisissez parmi : Premium, Standard", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        String prix;
        while (true) {
            prix = JOptionPane.showInputDialog(component  , "Entrer le prix de l'abonnement:");
            if (prix==null){
                JOptionPane.showMessageDialog(component  , "Opération annulée.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (isValidPrix(prix)) {
                break;
            }
            JOptionPane.showMessageDialog(component  , "Prix invalide : entrez un nombre positif", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        String statutAbonnement;
        while (true) {
            statutAbonnement = JOptionPane.showInputDialog(component  , "Entrer le statut de l'abonnement (Actif, Inactif, Suspendu) :");
            if (statutAbonnement==null){
                JOptionPane.showMessageDialog(component  , "Opération annulée.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (isValidStatut(statutAbonnement)) {
                break;
            }
            JOptionPane.showMessageDialog(component  , "Statut invalide : choisissez parmi (Actif, Inactif ou Suspendu)", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        // nouvel abonnement
        Abonnement abonnement = new Abonnement();
        abonnement.setStatutAbonnement(statutAbonnement);
        abonnement.setTypeAbonnement(typeAbonnement);
        abonnement.setPrix(Double.parseDouble(prix));
        abonnement.setDateDebut( new Date(2025,12,12));
        abonnement.setDateFin( new Date(2025,12,23));

        try {
            abonementService.insertAbonements(abonnement);

            // rafraichir la table
            model.addRow(new Object[]{abonnement.getIdAbonnement(),
                    abonnement.getPrix(),
                    abonnement.getTypeAbonnement(),
                    abonnement.getStatutAbonnement(),
                    abonnement.getDateDebut(),
                    abonnement.getDateFin()});
            JOptionPane.showMessageDialog(component  , "abonnement inseré avec succes.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur insertion abonnement", e);
            JOptionPane.showMessageDialog(component  , "Erreur insertion abonnement.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
