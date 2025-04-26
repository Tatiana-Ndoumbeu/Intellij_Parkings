package edu.ezip.ing1.pds.Interface.locallaverie;

import javax.swing.*;
        import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelListeAttenteLaverie extends JPanel {

    public PanelListeAttenteLaverie(List<String> clientsEnAttente) {
        setLayout(new GridLayout(2, 2, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        for (int i = 0; i < 4; i++) {
            JButton bouton = new JButton();
            int index = i;
            String nomClient = clientsEnAttente.get(i);
            if (nomClient != null && !nomClient.isEmpty()) {
                bouton.setText(nomClient);
                bouton.setBackground(new Color(220, 20, 60));
                bouton.setForeground(Color.RED);
                bouton.setFont(new Font("SansSerif", Font.BOLD, 16));
            } else {
                bouton.setText("+");
                bouton.setBackground(Color.WHITE);
                bouton.setFont(new Font("SansSerif", Font.BOLD, 24));
            }

            bouton.setFocusPainted(false);
            bouton.setOpaque(true);

            bouton.addActionListener(e -> {
                String contenu = bouton.getText();
                if ("+".equals(contenu)) {
                    String nom = JOptionPane.showInputDialog(this, "Entrez le nom du client :");
                    if (nom != null && !nom.trim().isEmpty()) {
                        clientsEnAttente.set(index, nom);
                        bouton.setText(nom);
                        bouton.setBackground(new Color(220, 20, 60));
                        bouton.setForeground(Color.RED);
                        bouton.setFont(new Font("SansSerif", Font.BOLD, 16));
                    }
                } else {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Supprimer " + contenu + " de la liste d'attente ?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        clientsEnAttente.set(index, "");
                        bouton.setText("+");
                        bouton.setBackground(Color.WHITE);
                        bouton.setFont(new Font("SansSerif", Font.BOLD, 24));
                    }
                }
            });
            add(bouton);
        }
    }
}
