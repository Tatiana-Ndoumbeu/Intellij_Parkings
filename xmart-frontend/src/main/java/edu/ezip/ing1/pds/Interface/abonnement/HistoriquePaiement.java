package edu.ezip.ing1.pds.Interface.abonnement;

import edu.ezip.ing1.pds.business.dto.Abonnement;

import javax.swing.*;
import java.awt.*;

public class HistoriquePaiement extends JFrame {

    public HistoriquePaiement(JFrame parent, Abonnement abonnement) {
        super("Détails du paiement de l'abonnement N° " +abonnement.getIdAbonnement());
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel moyenLabel = new JLabel("Moyen de paiement : " );
        JLabel dateLabel = new JLabel("Date de paiement : " );
        JLabel montantLabel = new JLabel("Montant Payé: " +abonnement.getPrix());
        JLabel periodeLabel = new JLabel("Période couverte : du "+abonnement.getDateDebut() + " au " + abonnement.getDateFin());

        moyenLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        montantLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        periodeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(moyenLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(montantLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(periodeLabel);

        add(infoPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton printButton = new JButton("Imprimer justificatif de paiement");
        styleButton(printButton, new Color(0, 123, 255));
        printButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Justificatif de paiement imprimé.",
                    "Impression",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton closeButton = new JButton("Fermer");
        styleButton(closeButton, new Color(255, 152, 0));
        closeButton.addActionListener(e -> dispose());

        buttonsPanel.add(printButton);
        buttonsPanel.add(closeButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}
