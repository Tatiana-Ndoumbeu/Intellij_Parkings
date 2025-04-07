package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.api.PlaceDeParkingRepository;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.business.dto.PlacesDeParkings;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.PlaceDeParkingService;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class PlaceDeParkingViewModel {

    public static void supprimerPlaceDeParking(DefaultTableModel model, JTable table, Component parentComponent, PlacesDeParkings placesDeParkings, String networkConfigFile, Logger logger) {
        String idPlace = JOptionPane.showInputDialog(parentComponent, "Entrez l'identifiant de la place de parking à supprimer :",
                "Suppression d'une place de parking", JOptionPane.QUESTION_MESSAGE);

        // verifie si l'id est valide
        if (idPlace == null || idPlace.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "L'identifiant est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(parentComponent, "Voulez-vous vraiment supprimer cette place de parking ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PlaceDeParkingRepository placeDeParkingService = new PlaceDeParkingService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
                boolean placeTrouvee = false;
                placesDeParkings.setPlaceDeParkings(placeDeParkingService.findAll()); ;
                for (PlaceDeParking place : placesDeParkings.getPlaceDeParkings()) {
                    if (place.getIdPlace().equals(idPlace)) {
                        placeTrouvee = true;
                        placeDeParkingService.delete(idPlace);

                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (model.getValueAt(i, 0).equals(idPlace)) {
                                model.removeRow(i);
                                break;
                            }
                        }

                        JOptionPane.showMessageDialog(parentComponent, "Place de parking supprimée avec succès !");
                        break;
                    }
                }
                if (!placeTrouvee) {
                    JOptionPane.showMessageDialog(parentComponent, "Place de parking non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } catch (Exception e) {
                logger.error("Erreur lors de la suppression de la place de parking", e);
                JOptionPane.showMessageDialog(parentComponent, "Erreur lors de la suppression de la place de parking.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public static  void updatePlaceDeParking(NetworkConfig networkConfig,DefaultTableModel model,Component parentComponent, String networkConfigFile, Logger logger)  {


        final PlaceDeParkingRepository placeDeParkingService = new PlaceDeParkingService(networkConfig);
        PlacesDeParkings placesDeParkings = null;

            placesDeParkings.setPlaceDeParkings(placeDeParkingService.findAll());

        logger.info("updatePlaceDeParking {}", placesDeParkings.getPlaceDeParkings().size());
        String idPlace = JOptionPane.showInputDialog(parentComponent, "ID de la place à modifier :");
        if (idPlace == null || idPlace.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "L'ID de la place ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newEmplacement = JOptionPane.showInputDialog(parentComponent, "Nouvel emplacement :");
        if (newEmplacement == null || newEmplacement.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "L'emplacement ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newType = JOptionPane.showInputDialog(parentComponent, "Nouveau type de place :");
        if (newType == null || newType.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "Le type de place ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newStatut = JOptionPane.showInputDialog(parentComponent, "Nouveau statut de place (Occupée, Libre) :");
        if (newStatut == null || newStatut.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "Le statut de la place ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean placeTrouvee = false;
        for (PlaceDeParking place : placesDeParkings.getPlaceDeParkings()) {
            if (place.getIdPlace().equals(idPlace)) {
                place.setEmplacement(newEmplacement);
                place.setTypePlace(newType);
                place.setStatutPlace(newStatut);
                placeTrouvee = true;
                break;
            }
        }

        if (!placeTrouvee) {
            JOptionPane.showMessageDialog(parentComponent, "Place de parking non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }


            //idPlace, newEmplacement, newType, newStatut
            PlaceDeParking updatedPlace = new PlaceDeParking();
            updatedPlace.setIdPlace(idPlace);
            updatedPlace.setEmplacement(newEmplacement);
            updatedPlace.setTypePlace(newType);
            updatedPlace.setStatutPlace(newStatut);
            placeDeParkingService.update(updatedPlace);

            model.setRowCount(0); // Réinitialiser les lignes de la table
            for (PlaceDeParking place : placesDeParkings.getPlaceDeParkings()) {
                model.addRow(new Object[]{place.getIdPlace(), place.getEmplacement(), place.getTypePlace(), place.getStatutPlace()});
            }

            JOptionPane.showMessageDialog(parentComponent, "Place de parking mise à jour avec succès.");

    }


    public static void insertPlaceDeParking(DefaultTableModel model,Component parentComponent, String networkConfigFile, Logger logger) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final PlaceDeParkingService placeDeParkingService = new PlaceDeParkingService(networkConfig);
        String emplacement = JOptionPane.showInputDialog(parentComponent, "Entrer Emplacement:");
        if (emplacement == null || emplacement.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "Emplacement cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String typePlace = JOptionPane.showInputDialog(parentComponent, "Entrer Type de Place:");
        if (typePlace == null || typePlace.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "Type cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String statutPlace = JOptionPane.showInputDialog(parentComponent, "Entrer Statut de Place (Occupée, Libre):");
        if (statutPlace == null || statutPlace.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "statut ne peut etre vide.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PlaceDeParking newPlace = new PlaceDeParking( );
        newPlace.setIdPlace(UUID.randomUUID().toString());
        newPlace.setEmplacement(emplacement);
        newPlace.setTypePlace(typePlace);
        newPlace.setStatutPlace(statutPlace);



            placeDeParkingService.save(newPlace);

            model.addRow(new Object[]{newPlace.getIdPlace(), newPlace.getEmplacement(), newPlace.getTypePlace(), newPlace.getStatutPlace()});
            //model.addRow(new Object[]{newPlace.getIdPlace(), newPlace.getEmplacement(), newPlace.getStatutPlace(), newPlace.getTypePlace()});
            JOptionPane.showMessageDialog(parentComponent, "Place de parking inseré !.");

    }


    public static DefaultTableModel createPlaceDeParkingTableModel(NetworkConfig networkConfig, PlacesDeParkings placesDeParkings, Logger logger) {
        final PlaceDeParkingService placeDeParkingService = new PlaceDeParkingService(networkConfig);
        String[] columns = {"ID", "Emplacement", "typePlace", "statutPlace"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);


            placesDeParkings.setPlaceDeParkings(placeDeParkingService.findAll()) ;
            if (placesDeParkings != null && placesDeParkings.getPlaceDeParkings() != null) {

                // Forcer le tri par emplacement ou ID
                List<PlaceDeParking> sortedList = new ArrayList<>(placesDeParkings.getPlaceDeParkings());
                sortedList.sort(Comparator.comparing(PlaceDeParking::getIdPlace)); // Change ici selon le critère voulu
                logger.debug("PlaceDeParkings sorted list {}", sortedList);

                for (PlaceDeParking place : sortedList) {
                    model.addRow(new Object[]{
                            place.getIdPlace(),
                            place.getEmplacement(),
                            place.getStatutPlace(),
                            place.getTypePlace()
                    });
                }
            }



        return model;
    }



}
