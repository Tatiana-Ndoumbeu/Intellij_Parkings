package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.business.dto.Personne;
import edu.ezip.ing1.pds.business.dto.Personnes;
import edu.ezip.ing1.pds.business.dto.Vehicle;
import edu.ezip.ing1.pds.business.dto.Vehicles;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.UUID;

public class PersonneViewModel {

    public static DefaultTableModel createPersonneTableModel(Personnes personnes) {
        String[] columns = {"ID", "Nom", "Prenom"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        if (personnes.getPersonnes() != null) {
            for (Personne personne : personnes.getPersonnes()) {
                model.addRow(new Object[]{personne.getIdPersonne(), personne.getNom(), personne.getPrenom()});
            }
        }
        return model;
    }

    public static DefaultTableModel createVehicleTableModel(Vehicles vehicles) {
        String[] columns = {"Num Plaque", "Type", "Marque"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        if (vehicles.getVehicules() != null) {
            for (Vehicle vehicle : vehicles.getVehicules()) {
                model.addRow(new Object[]{vehicle.getNumPlaque(), vehicle.getType(), vehicle.getMarque()});
            }
        }
        return model;
    }


    public static void insertPersonne(DefaultTableModel model, Personnes personnes, Component parent) {
        String nom = JOptionPane.showInputDialog(parent, "Enter Nom:");
        String prenom = JOptionPane.showInputDialog(parent, "Enter Prenom:");

        Personne newPersonne = new Personne(UUID.randomUUID().toString(), nom, prenom, "", "", "");
        personnes.getPersonnes().add(newPersonne);
        model.addRow(new Object[]{newPersonne.getIdPersonne(), newPersonne.getNom(), newPersonne.getPrenom()});
    }
}
