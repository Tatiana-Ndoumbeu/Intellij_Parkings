package edu.ezip.ing1.pds.Interface.abonnement;

import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.usecase.AbonnementUseCase;
import edu.ezip.ing1.pds.business.dto.Personne;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;


import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AjouterAbonnementFrame extends JFrame {

    private final AbonnementUseCase abonnementUseCase;
    private final String idPersonne;
    private final String nomPersonne;
    private final String prenomPersonne;

    public AjouterAbonnementFrame(AbonnementPanel parent, AbonnementUseCase abonnementUseCase, Personne personne) {
        this.abonnementUseCase = abonnementUseCase;
        this.idPersonne = personne.getIdPersonne();
        this.nomPersonne = personne.getNom();
        this.prenomPersonne = personne.getPrenom();

        setTitle("Ajouter un abonnement");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel infoLabel = new JLabel("Création d’un abonnement pour : " + personne.getPrenom() + " " + personne.getNom() + " (" + personne.getMail() + ")");
        infoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //JTextField idField = new JTextField();
        //JTextField typeField = new JTextField();
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Standard", "Premium"});
        JTextField prixField = new JTextField();
        JComboBox<String> statutField = new JComboBox<>(new String[]{"Actif", "Inactif", "Suspendu"});
        JTextField dateDebutField = new JTextField();
        JTextField dateFinField = new JTextField();
        JButton addBtn = new JButton("Ajouter");

        //styleField(idField, "ID Abonnement");
        //styleField(typeField, "Type Abonnement");
        styleComboBox(typeComboBox, "Type d'abonnement");
        styleField(prixField, "Prix (€)");
        styleComboBox(statutField, "Statut (Actif, Inactif, Suspendu)");
        styleField(dateDebutField, "Date Début (JJ/MM/AAAA)");
        styleField(dateFinField, "Date Fin (JJ/MM/AAAA)");
        styleButton(addBtn, new Color(255, 152, 0));

        //panel.add(idField);
        panel.add(infoLabel);
        panel.add(Box.createVerticalStrut(10));
        //panel.add(typeField);
        panel.add(typeComboBox);
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
            String selectedType = (String) typeComboBox.getSelectedItem();
            String prixStr = prixField.getText().trim();
            String selectedstatut = (String) statutField.getSelectedItem();
            String dateDebutStr = dateDebutField.getText().trim();
            String dateFinStr = dateFinField.getText().trim();

            if ( prixStr.isEmpty() || selectedstatut.isEmpty()
                    || dateDebutStr.isEmpty() || dateFinStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }

            try {
                double prix = Double.parseDouble(prixStr);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dateDebut = sdf.parse(dateDebutStr);
                Date dateFin = sdf.parse(dateFinStr);

                if (dateFin.before(dateDebut)) {
                    JOptionPane.showMessageDialog(this, "La date de fin doit être postérieure à la date de début.");
                    return;
                }

                String id = UUID.randomUUID().toString();

                Abonnement newAbonnement = new Abonnement(
                        id,
                        selectedType,
                        prix,
                        new java.sql.Date(dateDebut.getTime()),
                        new java.sql.Date(dateFin.getTime()),
                        selectedstatut,
                        idPersonne,
                        nomPersonne,
                        prenomPersonne
                );

                boolean isAdded = abonnementUseCase.createAbonnement(newAbonnement);

                if (isAdded) {
                    try{
                        List<Abonnement> updatedList = abonnementUseCase.getAllAbonnements().getAbonnements().stream().toList();
                        parent.refreshTable(updatedList);
                    } catch (Exception ex){
                        System.err.println("Erreur lors du rafraîchissement : " + ex.getMessage());
                    }
                    JOptionPane.showMessageDialog(this, "Abonnement ajouté avec succès !");
                    dispose();

                }else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'abonnement.");
                }
            } catch (NumberFormatException | ParseException ex) {
                JOptionPane.showMessageDialog(this, "Erreur dans le format des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex){
                ex.printStackTrace(); // Pour comprendre ce qui plante réellement
                JOptionPane.showMessageDialog(this, "Une erreur inattendue est survenue.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
