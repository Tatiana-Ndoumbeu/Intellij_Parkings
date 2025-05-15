package edu.ezip.ing1.pds.Interface.abonnement;

import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Personne;

import javax.swing.*;
import java.awt.*;

public class VoirAbonnement extends JFrame {

    public VoirAbonnement(JFrame parent, Abonnement abonnement, Personne personne) {
        super("Abonnement de " +personne.getNom() + " " + personne.getPrenom());
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel typeLabel = new JLabel("Type d'abonnement : " +abonnement.getTypeAbonnement());
        JLabel statutLabel = new JLabel("Statut de l'abonnement : " +abonnement.getStatutAbonnement());
        JLabel dateDebutLabel = new JLabel("Date de début : " +abonnement.getDateDebut());
        JLabel dateFinLabel = new JLabel("Date d'expiration : " +abonnement.getPrix() );
        JLabel montantLabel = new JLabel("Prix : " +abonnement.getPrix());

        infoPanel.add(typeLabel);
        infoPanel.add(statutLabel);
        infoPanel.add(dateDebutLabel);
        infoPanel.add(dateFinLabel);
        infoPanel.add(montantLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Fermer");
        styleButton(closeButton, new Color(255, 152, 0));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        setVisible(true);
    }

    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}

