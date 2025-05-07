package edu.ezip.ing1.pds.Interface.locallaverie;

import edu.ezip.ing1.pds.usecase.AbonnementUseCase;
import edu.ezip.ing1.pds.usecase.LocalLaverieUseCase;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;

import javax.swing.*;
        import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelListeAttenteLaverie extends JPanel {
    private AbonnementUseCase abonnementUseCase;
    private PersonneUseCase personneUseCase;
    private LocalLaverieUseCase localLaverieUseCase;

    public PanelListeAttenteLaverie(List<String> clientsEnAttente, PersonneUseCase personneUseCase, AbonnementUseCase abonnementUseCase, LocalLaverieUseCase localLaverieUseCase) {


        this.abonnementUseCase = abonnementUseCase;
        this.personneUseCase = personneUseCase;
        this.localLaverieUseCase = localLaverieUseCase;

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
                    Object[] options = {"Laver maintenant et supprimer", "Supprimer"};
                    int choix = JOptionPane.showOptionDialog(
                            this,
                            "Que voulez-vous faire avec : " + contenu + " ?",
                            "Action sur le client",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    if (choix == 0) {

                        JOptionPane.showMessageDialog(this, "Procédure de lavage pour " + contenu + " lancée.");
                        new FormulaireLaverMaintenant(null, personneUseCase, abonnementUseCase, localLaverieUseCase);
                        clientsEnAttente.set(index, "");
                        bouton.setText("+");
                        bouton.setBackground(Color.WHITE);
                        bouton.setFont(new Font("SansSerif", Font.BOLD, 24));

                    } else if (choix == 1) {
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
