package edu.ezip.ing1.pds.Interface.personne;

import edu.ezip.ing1.pds.business.dto.Personne;
import edu.ezip.ing1.pds.business.dto.Personnes;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.function.Consumer;

public class ModifierPersonneFrame extends JFrame {

    public ModifierPersonneFrame(Personne personne, Runnable onUpdate, PersonneUseCase personneUseCase) {
        setTitle("Modifier Personne");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JTextField nomField = new JTextField(personne.getNom());
        JTextField prenomField = new JTextField(personne.getPrenom());
        JTextField emailField = new JTextField(personne.getMail());
        JTextField telephoneField = new JTextField(personne.getTelephone());
        JTextField codepostalField = new JTextField(personne.getCodePostal());

        JButton updateBtn = new JButton("Mettre à jour");

        styleField(nomField, "Nom");
        styleField(prenomField, "Prenom");
        styleField(emailField, "Mail");
        styleField(telephoneField, "Telephone");
        styleField(codepostalField, "CodePostal");
        styleButton(updateBtn, new Color(255, 152, 0));

        panel.add(nomField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(prenomField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(telephoneField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(codepostalField);
        panel.add(Box.createVerticalStrut(10));

        panel.add(updateBtn);
        add(panel);
        setVisible(true);

        updateBtn.addActionListener(e -> {
            try{

                personne.setNom(nomField.getText().trim());
                personne.setPrenom(prenomField.getText().trim());
                personne.setMail(emailField.getText().trim());
                personne.setTelephone(telephoneField.getText().trim());
                personne.setCodePostal(codepostalField.getText().trim());

                boolean success = personneUseCase.updatePersonne(personne);
                if (success) {
                    Set<Personne> updatedList = personneUseCase.getALLPersonnes();
                    //JOptionPane.showMessageDialog(this, "Personne mise à jour avec succès !");
                    dispose();
                    onUpdate.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
                }

            }catch (Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
