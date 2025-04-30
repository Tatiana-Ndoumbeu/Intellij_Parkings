package edu.ezip.ing1.pds.Interface;

import javax.swing.*;
import java.awt.*;

import static edu.ezip.ing1.pds.Formulaires.chargerIcone;

public class PanelAccueuil extends JPanel {

    public PanelAccueuil(){
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.decode("#8d9496"));
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("BONJOUR ET BIENVENUE DANS INTELLIJ PARKING", JLabel.CENTER);
        titre.setFont(new Font("Garamond", Font.BOLD, 25));
        titre.setForeground(Color.BLUE);
        add(titre, BorderLayout.NORTH);

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(Color.decode("#8d9496"));
        JLabel iconLabel = new JLabel(chargerIcone("/icons/logoFond.png", 280, 280)); // Charger l'image
        imagePanel.add(iconLabel);

        add(imagePanel, BorderLayout.CENTER);

        JLabel bas = new JLabel("", JLabel.CENTER);
        titre.setFont(new Font("Garamond", Font.BOLD, 25));
        titre.setForeground(Color.BLUE);
        add(bas, BorderLayout.SOUTH);
    }
}
