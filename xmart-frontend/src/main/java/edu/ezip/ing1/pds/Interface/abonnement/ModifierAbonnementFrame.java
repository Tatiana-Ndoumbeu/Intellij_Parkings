package edu.ezip.ing1.pds.Interface.abonnement;

import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.usecase.AbonnementUseCase;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class ModifierAbonnementFrame extends JFrame {

    public ModifierAbonnementFrame(Abonnement abonnement,
                                   Consumer<List<Abonnement>> onUpdate,
                                   AbonnementUseCase abonnementUseCase) {
        setTitle("Modifier un abonnement");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JTextField typeField = new JTextField(abonnement.getTypeAbonnement());
        JTextField prixField = new JTextField(String.valueOf(abonnement.getPrix()));
        JTextField statutField = new JTextField(abonnement.getStatutAbonnement());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JTextField dateDebutField = new JTextField(sdf.format(abonnement.getDateDebut()));
        JTextField dateFinField = new JTextField(sdf.format(abonnement.getDateFin()));

        JButton updateBtn = new JButton("Mettre à jour");

        styleField(typeField, "Type d'abonnement");
        styleField(prixField, "Prix");
        styleField(statutField, "Statut");
        styleField(dateDebutField, "Date de début (dd/MM/yyyy)");
        styleField(dateFinField, "Date de fin (dd/MM/yyyy)");
        styleButton(updateBtn, new Color(255, 152, 0));

        panel.add(typeField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(prixField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(statutField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(dateDebutField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(dateFinField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(updateBtn);

        add(panel);
        setVisible(true);

        updateBtn.addActionListener(e -> {
            try {
                abonnement.setTypeAbonnement(typeField.getText().trim());
                abonnement.setPrix(Double.parseDouble(prixField.getText().trim()));
                abonnement.setStatutAbonnement(statutField.getText().trim());
                abonnement.setDateDebut(new java.sql.Date(sdf.parse(dateDebutField.getText().trim()).getTime()));
                abonnement.setDateFin(new java.sql.Date(sdf.parse(dateFinField.getText().trim()).getTime()));

                boolean success = abonnementUseCase.updateAbonnement(abonnement);
                if (success) {
                    List<Abonnement> updatedList = abonnementUseCase.getAllAbonnements().getAbonnements().stream().toList();
                    onUpdate.accept(updatedList);
                    JOptionPane.showMessageDialog(this, "Abonnement mis à jour avec succès !");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la saisie des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
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

