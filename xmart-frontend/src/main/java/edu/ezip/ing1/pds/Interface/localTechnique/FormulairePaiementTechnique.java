package edu.ezip.ing1.pds.Interface.localTechnique;

import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;
import edu.ezip.ing1.pds.services.MecanicienService;
import edu.ezip.ing1.pds.usecase.MecanicienUseCase;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class FormulairePaiementTechnique  {

    private final MecanicienUseCase mecanicienUseCase;
    private static Mecaniciens mecaniciens = new Mecaniciens();

    public  FormulairePaiementTechnique(JFrame parent, MecanicienUseCase mecanicienUseCase) {
        JDialog dialog = new JDialog(parent, "Paiement Service Technique", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));
        this.mecanicienUseCase = mecanicienUseCase;

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField telField = new JTextField();
        JTextField mailField = new JTextField();
        JTextField cpField = new JTextField();
        JTextField dateServiceField = new JTextField();


        ArrayList<String> techniciens = new ArrayList<>();
        try {
            mecaniciens = mecanicienUseCase.afficherMecaniciens();
            if (mecaniciens != null && mecaniciens.getMecaniciens() != null) {
                for (Mecanicien mecano : mecaniciens.getMecaniciens()) {
                    techniciens.add(mecano.getNom());
                }
            }
        }
        catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        String[] array = new String[techniciens.size()];
        for(int i = 0; i < array.length; i++) {
            array[i] = techniciens.get(i);
        }
        JComboBox<String> comboMecanicien = new JComboBox<>(array);



        JTextField prixField = new JTextField();
        JLabel dateJourLabel = new JLabel("Date du paiement : " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        panel.add(new JLabel("Nom client :"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom client :"));
        panel.add(prenomField);
        panel.add(new JLabel("Téléphone :"));
        panel.add(telField);
        panel.add(new JLabel("Email :"));
        panel.add(mailField);
        panel.add(new JLabel("Code Postal :"));
        panel.add(cpField);
        panel.add(new JLabel("Date du service :"));
        panel.add(dateServiceField);
        panel.add(new JLabel("Mécanicien :"));
        panel.add(comboMecanicien);
        panel.add(new JLabel("Prix du service (€) :"));
        panel.add(prixField);
        panel.add(dateJourLabel);
        panel.add(new JLabel(""));

        dialog.add(panel, BorderLayout.CENTER);

        JButton btnPayer = new JButton("Payer");
        btnPayer.setBackground(Color.GREEN);
        btnPayer.setForeground(Color.WHITE);
        btnPayer.setFont(new Font("SansSerif", Font.BOLD, 16));

        btnPayer.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String tel = telField.getText().trim();
            String mail = mailField.getText().trim();
            String cp = cpField.getText().trim();
            String dateservice = dateServiceField.getText();
            String mecanicien = comboMecanicien.getSelectedItem().toString().trim();
            String prix = prixField.getText();

            if (nom.isEmpty() || prenom.isEmpty() || prix.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Merci de remplir tous les champs obligatoires (nom, prénom, prix).");
                return;
            }

            int choix = JOptionPane.showOptionDialog(dialog, "Client "  + "\n"
                            + "Nom : " + nom + "\n"
                            +"Prénom : " + prenom + "\n"
                            + "Telephone : " + tel + "\n"
                            + "Email : " + mail + "\n"
                            + "code postal : " + cp + "\n"
                            + "Date du service : " + dateservice + "\n\n"
                            + "mecanicien : " + mecanicien + "\n\n\n"
                            + "Prix du service: " + prix + " €\n" +"\nConfirmer le paiement ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Confirmer", "Annuler"}, "Payer");

            if (choix == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(dialog, "Paiement effectué avec succès !");
                dialog.dispose();
            }
        });

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnPayer);
        dialog.add(panelBtn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
