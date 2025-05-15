package edu.ezip.ing1.pds.Interface.Mecanicien;

import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;
import edu.ezip.ing1.pds.usecase.AbonnementUseCase;
import edu.ezip.ing1.pds.usecase.MecanicienUseCase;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class AjouterMecanicienForm extends JFrame {

    private final MecanicienUseCase mecanicienUseCase;

    public AjouterMecanicienForm(MecanicienPanel parent, MecanicienUseCase mecanicienUseCase) {
        this.mecanicienUseCase = mecanicienUseCase;

        setTitle("Ajouter un Mécanicien");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField telephoneField = new JTextField();
        JTextField dispoField = new JTextField();
        JTextField specialiteField = new JTextField();
        JTextField mailField = new JTextField();
        JButton ajoutbouton = new JButton("Ajouter le mécanicien");

        styleField(nomField, "Nom");
        styleField(prenomField, "Prénom");
        styleField(telephoneField, "Téléphone (ex:0743434343)");
        styleField(dispoField, "Disponibilité:true/false(mois courant)");
        styleField(specialiteField, "Spécialité");
        styleField(mailField, "E-mail");

        panel.add(nomField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(prenomField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(telephoneField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(dispoField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(specialiteField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(mailField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(ajoutbouton);

        add(panel);
        setVisible(true);

        ajoutbouton.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String telephone = telephoneField.getText().trim();
            String dispoEnString =dispoField.getText().trim();
            Boolean disponibilite = Boolean.parseBoolean(dispoField.getText().trim());
            String specialite = specialiteField.getText().trim();
            String mail = mailField.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty()  || specialite.isEmpty()
                    || mail.isEmpty() || !dispoEnString.equalsIgnoreCase("true") && !dispoEnString.equalsIgnoreCase("false")) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir correctement tous les champs .");
                return;
            }

            try {
                Mecanicien Mecanicien1 = new Mecanicien(
                        nom,
                        prenom,
                        telephone,
                        disponibilite,
                        specialite,
                        mail
                );


                boolean isAdded = mecanicienUseCase.creerMecanicien(Mecanicien1);

                if (isAdded) {
                    List<Mecanicien> updatedList = mecanicienUseCase.afficherMecaniciens().getMecaniciens().stream().toList();
                    parent.refreshTable(updatedList);
                    JOptionPane.showMessageDialog(this, "Mecanicien ajouté avec succès !");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du Mécanicien.");
                }
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(this, iae.getMessage(), "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur dans le format des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void styleField(JTextField field, String label) {
        field.setEditable(true);
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(label));
        field.setPreferredSize(new Dimension(400, 60));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setForeground(Color.BLACK);
        field.setBackground(Color.WHITE);
    }


}
