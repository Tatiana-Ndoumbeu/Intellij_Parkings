package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.MainFrontEndSwing;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.*;

import com.toedter.calendar.JDateChooser;
import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;


import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Formulaires {

    private static final Random RANDOM = new Random();
    private Reservations reservations = new Reservations();
    private final static String LoggingLabel = "formulaires";
    private final static String networkConfigFile = "network.yaml";
    final static NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
    final static ReservationService reservationService = new ReservationService(networkConfig);
    final static PersonneService personneService = new PersonneService(networkConfig);

    final static AbonnementService abonementService = new AbonnementService(networkConfig);


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
                ReservationRequest reservation = new ReservationRequest();
                reservation.setIdReservation(generateUniqueId());
                reservation.setDateReservation(LocalDate.now().toString());
                reservation.setHeure(LocalTime.now().toString());
                reservation.setDateEntree(dateDebut.toString());
                reservation.setDateSortie(dateFin.toString());
                reservation.setHeureEntree(heureDebut.toString());
                reservation.setHeureSortie(heureFin.toString());

                Personne personne = new Personne();
                personne.setNom(Nom);
                personne.setPrenom(Prenom);
                personne.setTelephone(Telephone);
                personne.setMail(Mail);
                personne.setCodePostal(codePostal);

                // ajouter les positions aussi (update placedeparking)


                try {
                    System.out.println(reservation);
                    reservationService.insertReservation(reservation);
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

        JDialog dialog = new JDialog(parent, "Ajouter un abonnement", true);
        dialog.setSize(500, 800);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5));

        panel.add(new JLabel("Type d'abonnement (Standard ou Premium):"));
        String[] typesAbonnements = {"Standard", "Premium"};
        JComboBox<String> comboBoxTypeAbonnement = new JComboBox<>(typesAbonnements);
        comboBoxTypeAbonnement.setPreferredSize(new Dimension(100, 30));
        panel.add(comboBoxTypeAbonnement);

        panel.add(new JLabel("Prix de l'abonnement:"));
        JTextField champPrix = new JTextField();
        panel.add(champPrix);

        panel.add(new JLabel("Statut de l'abonnement (Actif, Inactif, Suspendu):"));
        String[] statutAbonnement = {"Actif", "Inactif", "Suspendu"};
        JComboBox<String> comboBoxStatut = new JComboBox<>(statutAbonnement);
        comboBoxStatut.setPreferredSize(new Dimension(100, 30));
        panel.add(comboBoxStatut);

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

        JPanel panelBouton = new JPanel();
        JButton boutonValider = new JButton("Valider l'ajout de l'abonnement", chargerIcone("/icons/ajouter.png", 30, 30));
        boutonValider.setPreferredSize(new Dimension(150, 40));
        boutonValider.setBackground(Color.GREEN);
        boutonValider.setForeground(Color.WHITE);

        boutonValider.addActionListener(e -> {
            String typeAbonnement = comboBoxTypeAbonnement.getSelectedItem().toString();
            String prix = champPrix.getText();
            String statut = comboBoxStatut.getSelectedItem().toString();

            String Nom = champNom.getText();
            String Prenom = champPrenom.getText();
            String Telephone = champTelephone.getText();
            String Mail = champMail.getText();
            String codePostal = champCodeP.getText();

            if (champPrix.getText().isEmpty() || champNom.getText().isEmpty() || champPrenom.getText().isEmpty() || champTelephone.getText().isEmpty() || champMail.getText().isEmpty() || champCodeP.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {

                Abonnement abonnement = new Abonnement();
                abonnement.setIdAbonnement(UUID.randomUUID().toString().substring(0, 8));
                abonnement.setTypeAbonnement(typeAbonnement);
                abonnement.setPrix(Double.parseDouble(prix));
                abonnement.setStatutAbonnement(statut);
                abonnement.setDateDebut( new java.sql.Date(2025,12,12));
                abonnement.setDateFin( new java.sql.Date(2025,12,23));

                Personne personne = new Personne();
                personne.setIdPersonne(generateUniqueId());
                personne.setNom(Nom);
                personne.setPrenom(Prenom);
                personne.setTelephone(Telephone);
                personne.setMail(Mail);
                personne.setCodePostal(codePostal);

                try{
                    System.out.println(abonnement);
                    abonementService.save(abonnement);
                    System.out.println(personne);
                    personneService.insertPersonnes(personne);
                }catch (IOException | InterruptedException u) {
                    JOptionPane.showMessageDialog(parent, "Erreur insertion abonnement.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(dialog, "Type Abonnement: " + typeAbonnement + "\nPrix: " + prix + "\nStatut Abonnement: " + statut + "\nNom: " + Nom + "\nPrénom: " + Prenom + "\nTéléphone: " + Telephone + "\nEmail: " + Mail + "\nCode postal: " + codePostal + "\nAbonnement: " + abonnement);
                dialog.dispose();
            }

        });

        panelBouton.add(boutonValider);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(panelBouton, BorderLayout.SOUTH);

        dialog.setVisible(true);


    }

    public static void FormulaireUpdateAbonnement(JFrame parent, Abonnement abonnement) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        JDialog dialog = new JDialog(parent, "Modifier un abonnement", true);
        dialog.setSize(500, 800);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField champType = new JTextField(abonnement.getTypeAbonnement());
        JTextField champPrix = new JTextField(String.valueOf(abonnement.getPrix()));
        JTextField champStatut = new JTextField(abonnement.getStatutAbonnement());

        panel.add(new JLabel("Type :")); panel.add(champType);
        panel.add(new JLabel("Prix :")); panel.add(champPrix);
        panel.add(new JLabel("Statut :")); panel.add(champStatut);

        JButton boutonValider = new JButton("Valider les modifications", chargerIcone("/icons/modifier.png", 30, 30));
        JButton boutonAnnuler = new JButton("Annuler", chargerIcone("/icons/quitter.png", 30, 30));
        boutonValider.setBackground(Color.GREEN);
        boutonValider.setForeground(Color.WHITE);
        boutonAnnuler.setBackground(Color.RED);
        boutonAnnuler.setForeground(Color.WHITE);

        JPanel panelBoutons = new JPanel(new BorderLayout());
        panelBoutons.add(boutonValider, BorderLayout.WEST);
        panelBoutons.add(boutonAnnuler, BorderLayout.EAST);


        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(panelBoutons, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static ImageIcon chargerIcone(String chemin, int largeur, int hauteur) {

        ImageIcon icon = new ImageIcon(Formulaires.class.getResource(chemin));
        Image image = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }


    public static String generateUniqueId() {
        return String.format("%04d", RANDOM.nextInt(10000));
    }



}