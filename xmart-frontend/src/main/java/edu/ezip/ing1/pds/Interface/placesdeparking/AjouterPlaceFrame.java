package edu.ezip.ing1.pds.Interface.placesdeparking;

import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.usecase.PlaceDeParkingUseCase;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AjouterPlaceFrame extends JFrame {

    private final PlaceDeParkingUseCase placeDeParkingUseCase;

    public AjouterPlaceFrame(PlaceDeParkingListFrame parent, PlaceDeParkingUseCase placeDeParkingUseCase) {
        this.placeDeParkingUseCase = placeDeParkingUseCase;
        setTitle("Ajouter une place de parking");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JTextField idField = new JTextField();
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Voiture", "Moto", "Vélo"});
        JComboBox<String> emplacementComboBox = new JComboBox<>(new String[]{"Niveau -1", "Niveau -2", "Niveau -3", "Niveau -4"});
        JComboBox<String> statutComboBox = new JComboBox<>(new String[]{"Libre", "Occupée"});
        JButton addBtn = new JButton("Ajouter");

        styleField(idField, "ID de la place");
        styleComboBox(typeComboBox, "Type de la place");
        styleComboBox(emplacementComboBox, "Emplacement");
        styleComboBox(statutComboBox, "Statut");
        styleButton(addBtn, new Color(255, 152, 0));

        panel.add(idField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(typeComboBox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(statutComboBox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(emplacementComboBox);
        panel.add(Box.createVerticalStrut(20));
        panel.add(addBtn);

        add(panel);
        setVisible(true);

        addBtn.addActionListener(e -> {
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le champ ID est obligatoire !");
                return;
            }

            String selectedType = (String) typeComboBox.getSelectedItem();
            String selectedEmplacement = (String) emplacementComboBox.getSelectedItem();
            String selectedStatut = (String) statutComboBox.getSelectedItem();

            PlaceDeParking newPlace = new PlaceDeParking(
                    idField.getText().trim(),
                    selectedType,
                    selectedStatut,
                    selectedEmplacement
            );

            boolean isAdded = placeDeParkingUseCase.addPlaceDeParking(newPlace);

            if (isAdded) {
                // Refresh the table by reloading data from the backend
                List<PlaceDeParking> updatedPlaces = placeDeParkingUseCase.getAllPlacesDeParking();
                parent.refreshTable(updatedPlaces); // Update the list in the parent frame
                JOptionPane.showMessageDialog(this, "Place ajoutée avec succès!");
                dispose();  // Close the add place window
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la place.");
            }
        });
    }

    private void styleField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
    }

    private void styleComboBox(JComboBox<String> comboBox, String placeholder) {
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        comboBox.setBorder(BorderFactory.createTitledBorder(placeholder));
    }

    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}
