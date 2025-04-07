package edu.ezip.ing1.pds.Interface.placesdeparking;

import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.usecase.PlaceDeParkingUseCase;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ModifierPlaceFrame extends JFrame {

    public ModifierPlaceFrame(PlaceDeParking place,
                              Consumer<List<PlaceDeParking>> onUpdate,
                              PlaceDeParkingUseCase placeDeParkingUseCase) {

        setTitle("Modifier une place");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JTextField typeField = new JTextField(place.getTypePlace());
        JTextField statutField = new JTextField(place.getStatutPlace());
        JTextField emplacementField = new JTextField(place.getEmplacement());
        JButton updateBtn = new JButton("Mettre à jour");

        styleField(typeField, "Type de la place");
        styleField(statutField, "Statut de la place");
        styleField(emplacementField, "Emplacement");
        styleButton(updateBtn, new Color(255, 152, 0));

        panel.add(typeField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(statutField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(emplacementField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(updateBtn);

        add(panel);
        setVisible(true);

        updateBtn.addActionListener(e -> {
            place.setTypePlace(typeField.getText().trim());
            place.setStatutPlace(statutField.getText().trim());
            place.setEmplacement(emplacementField.getText().trim());

            boolean success = placeDeParkingUseCase.updatePlaceDeParking(place);
            if (success) {
                List<PlaceDeParking> updatedList = placeDeParkingUseCase.getAllPlacesDeParking();
                onUpdate.accept(updatedList);
                JOptionPane.showMessageDialog(this, "Mise à jour réussie !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
            }
        });
    }

    private void styleField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
    }

    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}
