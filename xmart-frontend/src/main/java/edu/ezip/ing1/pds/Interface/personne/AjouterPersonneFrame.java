package edu.ezip.ing1.pds.Interface.personne;

import edu.ezip.ing1.pds.business.dto.Personne;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AjouterPersonneFrame extends JFrame{

    private final PersonneUseCase personneUseCase;

    public AjouterPersonneFrame(PersonnePanel parent, PersonneUseCase personneUseCase) {
        this.personneUseCase = personneUseCase;

        setTitle("Ajouter une personne");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField telField = new JTextField();
        JTextField mailField = new JTextField();
        JTextField postalField = new JTextField();
        JButton addBtn = new JButton("Ajouter");

        styleField(nomField, "Nom");
        styleField(prenomField, "Prenom");
        styleField(telField, "Numéro de Téléphone");
        styleField(mailField, "Mail");
        styleField(postalField, "Code Postal");
        styleButton(addBtn, new Color(255, 152, 0));

        panel.add(nomField);
        panel.add(prenomField);
        panel.add(telField);
        panel.add(mailField);
        panel.add(postalField);
        panel.add(addBtn);

        add(panel);
        setVisible(true);

        addBtn.addActionListener(e -> {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String tel = telField.getText();
            String mail = mailField.getText();
            String postal = postalField.getText();

            if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || mail.isEmpty() || postal.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }
            try{
                Set<String> existingIds = personneUseCase.getALLPersonnes().stream().map(Personne::getIdPersonne).collect(Collectors.toSet());

                String id = generateUniqueId(5, existingIds);
                Personne personne = new Personne(

                id,
                nom,
                prenom,
                tel,
                mail,
                postal);

                personneUseCase.ajouterPersonnes(personne);

                dispose();
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(this, iae.getMessage(), "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            }catch (IOException | InterruptedException ex){
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la personne : " + ex.getMessage());
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
    private String generateUniqueId(int length, Set<String> existingIds) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String id;
        do {
            StringBuilder idBuilder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int index = (int) (Math.random() * characters.length());
                idBuilder.append(characters.charAt(index));
            }
            id = idBuilder.toString();
        } while (existingIds.contains(id));
        return id;
    }

}
