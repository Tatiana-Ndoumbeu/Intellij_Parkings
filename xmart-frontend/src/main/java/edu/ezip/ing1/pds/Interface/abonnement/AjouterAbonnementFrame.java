package edu.ezip.ing1.pds.Interface.abonnement;

import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.usecase.AbonnementUseCase;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AjouterAbonnementFrame extends JFrame {

    private final AbonnementUseCase abonnementUseCase;

    public AjouterAbonnementFrame(AbonnementPanel parent, AbonnementUseCase abonnementUseCase) {
        this.abonnementUseCase = abonnementUseCase;

        setTitle("Ajouter un abonnement");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        //JTextField idField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField prixField = new JTextField();
        JTextField statutField = new JTextField();
        JTextField dateDebutField = new JTextField();
        JTextField dateFinField = new JTextField();
        JButton addBtn = new JButton("Ajouter");

        //styleField(idField, "ID Abonnement");
        styleField(typeField, "Type Abonnement");
        styleField(prixField, "Prix");
        styleField(statutField, "Statut (Actif, Inactif, Suspendu");
        styleField(dateDebutField, "Date Début (dd/MM/yyyy)");
        styleField(dateFinField, "Date Fin (dd/MM/yyyy)");
        styleButton(addBtn, new Color(255, 152, 0));

        //panel.add(idField);
        panel.add(Box.createVerticalStrut(10));
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
        panel.add(addBtn);

        add(panel);
        setVisible(true);

        addBtn.addActionListener(e -> {
            //String id = idField.getText().trim();
            String type = typeField.getText().trim();
            String prixStr = prixField.getText().trim();
            String statut = statutField.getText().trim();
            String dateDebutStr = dateDebutField.getText().trim();
            String dateFinStr = dateFinField.getText().trim();

            if (type.isEmpty() || prixStr.isEmpty() || statut.isEmpty()
                    || dateDebutStr.isEmpty() || dateFinStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }

            try {
                double prix = Double.parseDouble(prixStr);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dateDebut = sdf.parse(dateDebutStr);
                Date dateFin = sdf.parse(dateFinStr);

                String id = UUID.randomUUID().toString();

                Abonnement newAbonnement = new Abonnement(
                        id,
                        type,
                        prix,
                        new java.sql.Date(dateDebut.getTime()),
                        new java.sql.Date(dateFin.getTime()),
                        statut
                );

                boolean isAdded = abonnementUseCase.createAbonnement(newAbonnement);

                if (isAdded) {
                    List<Abonnement> updatedList = abonnementUseCase.getAllAbonnements().getAbonnements().stream().toList();
                    parent.refreshTable(updatedList);
                    JOptionPane.showMessageDialog(this, "Abonnement ajouté avec succès !");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'abonnement.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur dans le format des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
