package edu.ezip.ing1.pds.Interface.placesdeparking;

import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.usecase.PlaceDeParkingUseCase;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AjouterPlaceFrame extends JFrame {

    private final PlaceDeParkingUseCase placeDeParkingUseCase;

    public AjouterPlaceFrame(PlaceDeParkingPanel parent, PlaceDeParkingUseCase placeDeParkingUseCase) {
        this.placeDeParkingUseCase = placeDeParkingUseCase;
        setTitle("Ajouter une place de parking");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Voiture", "Moto", "Vélo"});
        JComboBox<String> emplacementComboBox = new JComboBox<>(new String[]{"Niveau -1", "Niveau -2", "Niveau -3", "Niveau -4"});
        JComboBox<String> statutComboBox = new JComboBox<>(new String[]{"Libre", "Occupée"});
        JButton addBtn = new JButton("Ajouter");

        styleComboBox(typeComboBox, "Type de la place");
        styleComboBox(emplacementComboBox, "Emplacement");
        styleComboBox(statutComboBox, "Statut");
        styleButton(addBtn, new Color(255, 152, 0));

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
            String selectedType = (String) typeComboBox.getSelectedItem();
            String selectedEmplacement = (String) emplacementComboBox.getSelectedItem();
            String selectedStatut = (String) statutComboBox.getSelectedItem();

            // On génère l'ID automatiquement avec un pattern lisible
            String id = "PARK-" + System.currentTimeMillis();

            PlaceDeParking newPlace = new PlaceDeParking(
                    id,
                    selectedType,
                    selectedStatut,
                    selectedEmplacement
            );

            boolean isAdded = placeDeParkingUseCase.addPlaceDeParking(newPlace);

            if (isAdded) {
                List<PlaceDeParking> updatedPlaces = placeDeParkingUseCase.getAllPlacesDeParking();
                parent.refreshTable(updatedPlaces);
                JOptionPane.showMessageDialog(this, "Place ajoutée avec succès!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la place.");
            }
        });
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
