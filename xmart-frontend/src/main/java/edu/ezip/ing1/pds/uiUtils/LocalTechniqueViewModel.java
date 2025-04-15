package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.business.dto.LocalTechnique;
import edu.ezip.ing1.pds.business.dto.LocalTechniques;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.LocalTechniqueService;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.function.Function;

import static edu.ezip.ing1.pds.MainFrontEndSwing.networkConfigFile;

public class LocalTechniqueViewModel {

    public static  void updateLocalT(DefaultTableModel model, Component component, LocalTechniques localTechniques, Logger logger) {

        String numLocalT = JOptionPane.showInputDialog(component, "Numéro du local à modifier :");
        if (numLocalT == null || numLocalT.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "Le numéro du local ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocalT);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(component, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String disponibilite = JOptionPane.showInputDialog(component, "Nouvelle disponibilité (true ou false) :");
        if (disponibilite == null || disponibilite.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "La disponibilité ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!disponibilite.equalsIgnoreCase("true") && !disponibilite.equalsIgnoreCase("false")){
            JOptionPane.showMessageDialog(component, "La disponibilité est soit 'true' soit 'false'' ", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean newDisponibilite = Boolean.parseBoolean(disponibilite);

        boolean localTrouve = false;
        for (LocalTechnique local : localTechniques.getLocalTechniques()) {
            if (String.valueOf(local.getNumLocalT()).equals(numLocalT)) {
                local.setDisponibilite(newDisponibilite);
                localTrouve = true;
                break;
            }
        }

        if (!localTrouve) {
            JOptionPane.showMessageDialog(component, "Local non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalTechnique localTechniquemodif = new LocalTechnique();
            localTechniquemodif.setNumLocalT(Integer.parseInt(numLocalT));
            localTechniquemodif.setDisponibilite(newDisponibilite);

            final LocalTechniqueService localService = new LocalTechniqueService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
            localService.update(localTechniquemodif);

            model.setRowCount(0);// on supprime dabord les anciennes lignes dans le tableau
            for (LocalTechnique place : localTechniques.getLocalTechniques()) {
                model.addRow(new Object[]{place.getNumLocalT(), place.getDisponibilite()});
            }

            JOptionPane.showMessageDialog(component, "Disponibilité du local mise à jour.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur lors de la mise à jour du local", e);
            JOptionPane.showMessageDialog(component, "Erreur lors de la mise à jour du local.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }

    public static  void insertLocalT(DefaultTableModel model, Component component,
                              Logger logger) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final LocalTechniqueService localService = new LocalTechniqueService(networkConfig);

        String numLocalT = JOptionPane.showInputDialog(component, "numero local :");
        if (numLocalT == null || numLocalT.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "num local ne peut pas être vide.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocalT);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(component, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String disponibilite = JOptionPane.showInputDialog(component, "Entrer disponibilite du local true ou false:");
        if (disponibilite == null || disponibilite.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "disponibilite cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!disponibilite.equalsIgnoreCase("true") && !disponibilite.equalsIgnoreCase("false")){
            JOptionPane.showMessageDialog(component, "La disponibilité est soit 'true' soit 'false'' ", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }


        LocalTechnique localTechnique1 = new LocalTechnique();
        localTechnique1.setNumLocalT(Integer.parseInt(numLocalT));
        localTechnique1.setDisponibilite(Boolean.parseBoolean(disponibilite));

        try {
            localService.insert(localTechnique1);
            model.addRow(new Object[]{localTechnique1.getNumLocalT(), localTechnique1.getDisponibilite()});
            //JOptionPane.showMessageDialog(component, "Local Inséré.");

            JOptionPane.showMessageDialog(component, "Local inséré.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur insertion Local", e);
            JOptionPane.showMessageDialog(component, "Erreur insertion Local.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    public static  void deleteLocalT(DefaultTableModel model, Component component, LocalTechniques localTechniques, Logger logger) {
        String numLocalT = JOptionPane.showInputDialog(component, "Numéro du local à supprimer :");
        if (numLocalT == null || numLocalT.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "Le numéro du local ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocalT);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(component, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean localTrouve = false;
        LocalTechnique localASupprimer = null;
        for (LocalTechnique local : localTechniques.getLocalTechniques()) {
            if (String.valueOf(local.getNumLocalT()).equals(numLocalT)) {
                localASupprimer = local;
                localTrouve = true;
                break;
            }
        }

        if (!localTrouve) {
            JOptionPane.showMessageDialog(component, "Local non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(component,
                "Êtes-vous sûr de vouloir supprimer ce local ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            final LocalTechniqueService localService = new LocalTechniqueService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
            localService.delete(localASupprimer);

            localTechniques.getLocalTechniques().remove(localASupprimer);

            model.setRowCount(0);
            for (LocalTechnique place : localTechniques.getLocalTechniques()) {
                model.addRow(new Object[]{place.getNumLocalT(), place.getDisponibilite()});
            }

            JOptionPane.showMessageDialog(component, "Local supprimé avec succès.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur lors de la suppression du local", e);
            JOptionPane.showMessageDialog(component, "Erreur lors de la suppression du local.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static DefaultTableModel createLocalTechniqueTableModel(NetworkConfig networkConfig, LocalTechniques localTechniques, Logger logger) {
        final LocalTechniqueService localService = new LocalTechniqueService(networkConfig);
        String[] columns = { "numLocal", "disponibilite"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        //
        try {
            localTechniques = localService.select();
            if (localTechniques != null && localTechniques.getLocalTechniques() != null) {
                for (LocalTechnique place : localTechniques.getLocalTechniques()) {
                    model.addRow(new Object[]{place.getNumLocalT(),
                            place.getDisponibilite(),
                    });
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur recuperation Local Technique", e);
        }

        return model;
    }
}
