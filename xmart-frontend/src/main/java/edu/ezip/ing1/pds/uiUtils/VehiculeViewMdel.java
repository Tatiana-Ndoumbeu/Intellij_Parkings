package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.business.dto.Vehicle;
import edu.ezip.ing1.pds.business.dto.Vehicles;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VehiculeViewMdel {

    public static void insertVehicle(DefaultTableModel model, Vehicles vehicles, Component component) {
        String numPlaque = JOptionPane.showInputDialog(component, "Enter Num Plaque:");
        String type = JOptionPane.showInputDialog(component, "Enter Type:");
        String marque = JOptionPane.showInputDialog(component, "Enter Marque:");

        Vehicle newVehicle = new Vehicle(numPlaque, type, marque);
        vehicles.getVehicules().add(newVehicle);
        model.addRow(new Object[]{newVehicle.getNumPlaque(), newVehicle.getType(), newVehicle.getMarque()});
    }
}
