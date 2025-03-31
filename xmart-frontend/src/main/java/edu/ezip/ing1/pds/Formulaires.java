package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.MainFrontEndSwing;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.*;

import com.toedter.calendar.JDateChooser;
import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;


import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Date;

public class Formulaires {

    private static final Random RANDOM = new Random();
    private Reservations reservations = new Reservations();
    private final static String LoggingLabel = "formulaires";
    private final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final static ReservationService reservationService = new ReservationService(networkConfig);
    final static PersonneService personneService = new PersonneService(networkConfig);

    public static void FormulaireReservation(JFrame parent) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }


        JDialog dialog = new JDialog(parent, "Reserver une zone spéciale", true);
        dialog.setSize(500, 800);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5));

        panel.add(new JLabel("Type de place:"));
        String[] typesDePlace = {"PMR", "VIP", "Electrique", "Livraison"};
        JComboBox<String> comboBoxTypePlace = new JComboBox<>(typesDePlace);
        comboBoxTypePlace.setPreferredSize(new Dimension(100, 30));
        panel.add(comboBoxTypePlace);

        panel.add(new JLabel("Emplacement:"));
        String[] emplacement = {"Niveau 1", "Niveau 2", "Niveau 3"};
        JComboBox<String> comboBoxEmplacement = new JComboBox<>(emplacement);
        comboBoxEmplacement.setPreferredSize(new Dimension(100, 30));
        panel.add(comboBoxEmplacement);

        panel.add(new JLabel("Position:"));
        JTextField champPosition = new JTextField();
        panel.add(champPosition);

        panel.add(new JLabel("Date de début:"));
        JDateChooser dateDebutChooser = new JDateChooser();
        dateDebutChooser.setPreferredSize(new Dimension(150, 30));
        panel.add(dateDebutChooser);

        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner hourSpinner = new JSpinner(model);
        hourSpinner.setEditor(new JSpinner.DateEditor(hourSpinner, "HH:mm"));
        panel.add(new JLabel("Heure de début:"));
        panel.add(hourSpinner);

        panel.add(new JLabel("Date de fin:"));
        JDateChooser dateFinChooser = new JDateChooser();
        dateFinChooser.setPreferredSize(new Dimension(150, 30));
        panel.add(dateFinChooser);

        SpinnerDateModel modelFin = new SpinnerDateModel();
        JSpinner hourSpinnerFin = new JSpinner(modelFin);
        hourSpinnerFin.setEditor(new JSpinner.DateEditor(hourSpinnerFin, "HH:mm"));
        panel.add(new JLabel("Heure de fin:"));
        panel.add(hourSpinnerFin);


        panel.add(new JLabel("Nom:"));
        JTextField champNom = new JTextField();
        panel.add(champNom);

        panel.add(new JLabel("Prénom:"));
        JTextField champPrenom = new JTextField();
        panel.add(champPrenom);

        panel.add(new JLabel("Téléphone:"));
        JTextField champTelephone = new JTextField();
        panel.add(champTelephone);

        panel.add(new JLabel("e-Mail:"));
        JTextField champMail = new JTextField();
        panel.add(champMail);

        panel.add(new JLabel("code Postal:"));
        JTextField champCodeP = new JTextField();
        panel.add(champCodeP);

        // Panel pour le bouton
        JPanel panelBouton = new JPanel();
        JButton boutonValider = new JButton("Valider", chargerIcone("/icons/ajouter.png", 30, 30));
        boutonValider.setPreferredSize(new Dimension(150, 40));
        boutonValider.setBackground(Color.GREEN);
        boutonValider.setForeground(Color.WHITE);

        boutonValider.addActionListener(e -> {

            String typePlace = comboBoxTypePlace.getSelectedItem().toString();
            String Emplacement = comboBoxEmplacement.getSelectedItem().toString();
            String Position = champPosition.getText();
            Date dateDebut = dateDebutChooser.getDate();
            Date heureDebut = (Date) hourSpinner.getValue();
            Date dateFin = dateFinChooser.getDate();
            Date heureFin = (Date) hourSpinnerFin.getValue();
            //heures
            String Nom = champNom.getText();
            String Prenom = champPrenom.getText();
            String Telephone = champTelephone.getText();
            String Mail = champMail.getText();
            String codePostal = champCodeP.getText();


            //RAJOUTER DES REGLES METIERS PLUS TARD

            if (champPosition.getText().isEmpty() || champNom.getText().isEmpty() || champTelephone.getText().isEmpty() || champCodeP.getText().isEmpty() || dateDebut == null || dateFin == null) {
                JOptionPane.showMessageDialog(dialog, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                Reservation reservation = new Reservation();
                reservation.setIdReservation(generateUniqueId());
                reservation.setDateReservation(LocalDate.now());
                reservation.setHeure(LocalTime.now());
                reservation.setDateEntree(dateDebut);
                reservation.setDateSortie(dateFin);
                reservation.setHeureeEntre(heureDebut);
                reservation.setHeureSortie(heureFin);

                Personne personne = new Personne();
                personne.setNom(Nom);
                personne.setPrenom(Prenom);
                personne.setTelephone(Telephone);
                personne.setMail(Mail);
                personne.setCodePostal(codePostal);

                // ajouter les positions aussi (update placedeparking)


                try {
                    System.out.println(reservation);
                    reservationService.insertReservations(reservation);
                    System.out.println(personne);
                    personneService.insertPersonnes(personne);
                }
                 catch (IOException | InterruptedException u) {

                    JOptionPane.showMessageDialog(parent, "Erreur insertion reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // LocalDate reservationDate = LocalDate.ofInstant(dateDebut.toInstant(), ZoneId.systemDefault());
                //   reservations.computeIfAbsent(reservationDate, k -> new ArrayList<>()).add("Réservation ajoutée le " + reservationDate.toString());

                JOptionPane.showMessageDialog(dialog, "Prenom: " + Prenom + "\nNom: " + Nom + "\nTelephone: " + Telephone + "\nE-Mail: " + Mail + "\nCode Postal: " + codePostal + "\nPosition: " + Position + "\nEmplacement: " + Emplacement + "\nType de place: " + typePlace + "\ndate de debut: " + dateDebut + "\ndate de fin: " + dateFin);
                dialog.dispose();
            }
        });

        panelBouton.add(boutonValider);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(panelBouton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void FormulaireAbonnements(JFrame parent) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }


        //à completer


    }

    private static ImageIcon chargerIcone(String chemin, int largeur, int hauteur) {

        ImageIcon icon = new ImageIcon(Formulaires.class.getResource(chemin));
        Image image = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }


    public static String generateUniqueId() {
        return String.format("%04d", RANDOM.nextInt(10000));
    }



}