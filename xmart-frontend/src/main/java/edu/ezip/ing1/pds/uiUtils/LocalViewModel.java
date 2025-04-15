package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.LocalLaveriesService;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.function.Function;

import static edu.ezip.ing1.pds.MainFrontEndSwing.networkConfigFile;

public class LocalViewModel {

    public static void insertLocal(DefaultTableModel model, Component component,
                             Logger logger) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final LocalLaveriesService localService = new LocalLaveriesService(networkConfig);

        String numLocal = JOptionPane.showInputDialog(component, "num local :");
        if (numLocal == null || numLocal.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "num local cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocal);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(component, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String disponibilite = JOptionPane.showInputDialog(component, "Entrer disponibilite du local true ou false:");
        if (disponibilite == null || disponibilite.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "disponibilite ne peut pas être vide.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!disponibilite.equalsIgnoreCase("true") && !disponibilite.equalsIgnoreCase("false")){
            JOptionPane.showMessageDialog(component, "La disponibilité est soit 'true' soit 'false'' ", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalLaverie localLaverie1 = new LocalLaverie();
        localLaverie1.setNumLocalL(Integer.parseInt(numLocal));
        localLaverie1.setDisponibilite(Boolean.parseBoolean(disponibilite));

        try {
            localService.insert(localLaverie1);
            model.addRow(new Object[]{localLaverie1.getNumLocalL(), localLaverie1.getDisponibilite()});
            JOptionPane.showMessageDialog(component, "Local Inséré.");

            JOptionPane.showMessageDialog(component, "Local inséré.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur insertion Local", e);
            JOptionPane.showMessageDialog(component, "Erreur insertion Local.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void updateLocal(DefaultTableModel model, Component component, LocalLaveries localLaveries, Logger logger) {


        String numLocal = JOptionPane.showInputDialog(component, "Numéro du local à modifier :");
        if (numLocal == null || numLocal.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "Le numéro du local ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocal);
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
        for (LocalLaverie local : localLaveries.getLocalLaveries()) {
            if (String.valueOf(local.getNumLocalL()).equals(numLocal)) {
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
            LocalLaverie localLaveriemodif = new LocalLaverie();
            localLaveriemodif.setNumLocalL(Integer.parseInt(numLocal));
            localLaveriemodif.setDisponibilite(newDisponibilite);

            final LocalLaveriesService localService = new LocalLaveriesService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
            localService.update(localLaveriemodif);

            model.setRowCount(0);// on supprime dabord les anciennes lignes dans le tableau
            for (LocalLaverie place : localLaveries.getLocalLaveries()) {
                model.addRow(new Object[]{place.getNumLocalL(), place.getDisponibilite()});
            }

            JOptionPane.showMessageDialog(component, "Disponibilité du local mise à jour.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur lors de la mise à jour du local", e);
            JOptionPane.showMessageDialog(component, "Erreur lors de la mise à jour du local.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }
    public static void deleteLocal(DefaultTableModel model, Component component,LocalLaveries localLaveries, Logger logger) {
        String numLocalL = JOptionPane.showInputDialog(component, "Numéro du local à supprimer :");
        if (numLocalL == null || numLocalL.trim().isEmpty()) {
            JOptionPane.showMessageDialog(component, "Le numéro du local ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocalL);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(component, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }



        boolean localTrouve = false;
        LocalLaverie localASupprimer = null;
        for (LocalLaverie local : localLaveries.getLocalLaveries()) {
            if (String.valueOf(local.getNumLocalL()).equals(numLocalL)) {
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
            final LocalLaveriesService localService = new LocalLaveriesService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
            localService.delete(localASupprimer);

            localLaveries.getLocalLaveries().remove(localASupprimer);

            model.setRowCount(0);
            for (LocalLaverie place : localLaveries.getLocalLaveries()) {
                model.addRow(new Object[]{place.getNumLocalL(), place.getDisponibilite()});
            }

            JOptionPane.showMessageDialog(component, "Local supprimé avec succès.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur lors de la suppression du local", e);
            JOptionPane.showMessageDialog(component, "Erreur lors de la suppression du local.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    public static DefaultTableModel createLocalLaverieTableModel(NetworkConfig networkConfig, LocalLaveries localLaveries,Logger logger) {
        final LocalLaveriesService localService = new LocalLaveriesService(networkConfig);
        String[] columns = { "numLocal", "disponibilite"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        //
        try {
            localLaveries = localService.select();
            if (localLaveries != null && localLaveries.getLocalLaveries() != null) {
                for (LocalLaverie place : localLaveries.getLocalLaveries()) {
                    model.addRow(new Object[]{place.getNumLocalL(),
                            place.getDisponibilite(),
                    });
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching Local laverie", e);
        }

        return model;
    }
}
