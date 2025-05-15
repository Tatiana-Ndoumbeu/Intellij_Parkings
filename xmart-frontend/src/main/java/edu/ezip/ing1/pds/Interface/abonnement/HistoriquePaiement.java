package edu.ezip.ing1.pds.Interface.abonnement;

import edu.ezip.ing1.pds.business.dto.Abonnement;

import javax.swing.*;
import java.awt.*;

public class HistoriquePaiement extends JFrame {

    public HistoriquePaiement(JFrame parent, Abonnement abonnement) {
        super();
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel moyenLabel = new JLabel("Moyen de paiement : " );
        JLabel dateLabel = new JLabel("Date du dernier paiement : " );
        JLabel montantLabel = new JLabel("Montant : ");

        JButton closeButton = new JButton("Fermer");
        styleButton(closeButton, new Color(255, 152, 0));
        closeButton.addActionListener(e -> dispose());

        add(moyenLabel);
        add(dateLabel);
        add(montantLabel);
        add(closeButton);

        setVisible(true);
    }

    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}
