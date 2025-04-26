package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.MainFrontEndSwing;
import edu.ezip.ing1.pds.api.ReservationLocalRepository;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.*;

import com.toedter.calendar.JDateChooser;
import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;
import edu.ezip.ing1.pds.usecase.AbonnementUseCase;
import edu.ezip.ing1.pds.usecase.MecanicienUseCase;
import edu.ezip.ing1.pds.usecase.ReservationLocalUseCase;


import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    final static LocalLaveriesService localLaveriesService = new LocalLaveriesService(networkConfig);
    final static LocalTechniqueService localTechniqueService = new LocalTechniqueService(networkConfig);

    final static PersonneService personneService = new PersonneService(networkConfig);
    private static LocalLaveries localLaveries = new LocalLaveries();
    private static LocalTechniques localTechniques = new LocalTechniques();
    private static Mecaniciens mecaniciens = new Mecaniciens();
    final static AbonnementService abonementService = new AbonnementService(networkConfig);


    public static void FormulaireReservationLocal(JFrame parent) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        JDialog dialog = new JDialog(parent, "Reserver un Local Service", true);
        dialog.setSize(500, 800);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5));

        panel.add(new JLabel("Service souhaité:"));
        String[] typesDeService = {"Laverie", "Dépannage"};
        JComboBox<String> comboBoxTypeService = new JComboBox<>(typesDeService);
        comboBoxTypeService.setPreferredSize(new Dimension(100, 30));
        panel.add(comboBoxTypeService);




        ArrayList<Integer> locauxDisponibles = new ArrayList<>();

        try {
            localLaveries = localLaveriesService.select();
            if (localLaveries != null && localLaveries.getLocalLaveries() != null) {
                for (LocalLaverie place : localLaveries.getLocalLaveries()) {
                    locauxDisponibles.add(place.getNumLocalL());
                }
            }
        }
        catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        Integer[] array = new Integer[locauxDisponibles.size()];
        for(int i = 0; i < array.length; i++) {
            array[i] = locauxDisponibles.get(i);
        }


        panel.add(new JLabel("Local Service:"));

        JComboBox<Integer> comboBoxLocal = new JComboBox<>(array);

        comboBoxLocal.setPreferredSize(new Dimension(100, 30));
        panel.add(comboBoxLocal);



        comboBoxTypeService.addActionListener(e -> {
            comboBoxLocal.removeAllItems();
            String selectedService = (String) comboBoxTypeService.getSelectedItem();
            locauxDisponibles.clear();

            try {
                if ("Laverie".equals(selectedService)) {
                    localLaveries = localLaveriesService.selectDispo();
                    if (localLaveries != null && localLaveries.getLocalLaveries() != null) {
                        for (LocalLaverie place : localLaveries.getLocalLaveries()) {
                            locauxDisponibles.add(place.getNumLocalL());
                        }
                    }
                } else if ("Dépannage".equals(selectedService)) {
                    localTechniques = localTechniqueService.selectDispo();
                    if (localTechniques != null && localTechniques.getLocalTechniques() != null) {
                        for (LocalTechnique local : localTechniques.getLocalTechniques()) {
                            locauxDisponibles.add(local.getNumLocalT());
                        }
                    }
                }

                for (Integer numLocal : locauxDisponibles) {
                    comboBoxLocal.addItem((numLocal));
                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        int numeroLocalChoisi = (Integer) comboBoxLocal.getSelectedItem();

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
            String newId = generateUniqueId();
            String selectedService = (String) comboBoxTypeService.getSelectedItem();

            Date dateDebut = dateDebutChooser.getDate();
            Date heureDebut = (Date) hourSpinner.getValue();
            Date dateFin = dateFinChooser.getDate();
            Date heureFin = (Date) hourSpinnerFin.getValue();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String DateDebut = dateDebut != null ? dateFormat.format(dateDebut) : null;
            String DateFin = dateFin != null ? dateFormat.format(dateFin) : null;

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String HeureDebut = heureDebut != null ? timeFormat.format(heureDebut) : null;
            String HeureFin = heureFin != null ? timeFormat.format(heureFin) : null;

            String Nom = champNom.getText();
            String Prenom = champPrenom.getText();
            String Telephone = champTelephone.getText();
            String Mail = champMail.getText();
            String codePostal = champCodeP.getText();


            ReservationLocal reservationLocal = new ReservationLocal();
            /*
            if (dateDebut.after(dateFin)) {
                JOptionPane.showMessageDialog(dialog, "La date de début ne peut pas être après la date de fin.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ( champNom.getText().isEmpty() || champTelephone.getText().isEmpty() || champCodeP.getText().isEmpty() || dateDebut == null || dateFin == null) {
                JOptionPane.showMessageDialog(dialog, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {*/
            reservationLocal.setNumLocal(numeroLocalChoisi);
            reservationLocal.setDateDebut(DateDebut);
            reservationLocal.setDateFin(DateFin);
            reservationLocal.setHeureEntree(HeureDebut);
            reservationLocal.setHeureSortie(HeureFin);
            reservationLocal.setTypeLocal(selectedService);
            reservationLocal.setIdPersonne(newId);

                Personne personne = new Personne();
                personne.setIdPersonne(newId);
                personne.setNom(Nom);
                personne.setPrenom(Prenom);
                personne.setTelephone(Telephone);
                personne.setMail(Mail);
                personne.setCodePostal(codePostal);



                try {
                    personneService.insertPersonnes(personne);

                    ReservationLocalRepository reservationLocalRepository = new ReservationLocalService(networkConfig);
                    ReservationLocalUseCase reservationLocalUseCase = new ReservationLocalUseCase(reservationLocalRepository);
                    boolean isReservationCreated = reservationLocalUseCase.createReservationLocal(reservationLocal);


                } catch (IOException | InterruptedException u) {


                    // LocalDate reservationDate = LocalDate.ofInstant(dateDebut.toInstant(), ZoneId.systemDefault());
                    //   reservations.computeIfAbsent(reservationDate, k -> new ArrayList<>()).add("Réservation ajoutée le " + reservationDate.toString());

                    JOptionPane.showMessageDialog(dialog, "Prenom: " + Prenom + "\nNom: " + Nom + "\nTelephone: " + Telephone + "\nE-Mail: " + Mail + "\nCode Postal: " + codePostal + " \ndate de debut: " + dateDebut + "\ndate de fin: " + dateFin);
                    dialog.dispose();

                }
                ;

            });
        panelBouton.add(boutonValider);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(panelBouton, BorderLayout.SOUTH);

        dialog.setVisible(true);}



                               /// / FORMULAIRE LAVERIE///////



    public static void FormulaireLaver(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Formulaire Lavage", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel panelChoix = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelChoix.add(new JLabel("Type d'utilisateur :"));
        String[] choixAbonnement = {"Abonné", "Visiteur"};
        JComboBox<String> comboType = new JComboBox<>(choixAbonnement);
        panelChoix.add(comboType);
        dialog.add(panelChoix, BorderLayout.NORTH);

        JPanel panelCardLayout = new JPanel(new CardLayout());

        JPanel panelAbonne = new JPanel(new GridLayout(2, 2, 5, 5));
        panelAbonne.setBorder(BorderFactory.createTitledBorder("Informations Abonné"));
        JTextField champMailAbonne = new JTextField();
        panelAbonne.add(new JLabel("Email :"));
        panelAbonne.add(champMailAbonne);


        JPanel panelVisiteur = new JPanel(new GridLayout(8, 2, 5, 5));
        panelVisiteur.setBorder(BorderFactory.createTitledBorder("Informations Visiteur"));
        JTextField champNom = new JTextField();
        JTextField champPrenom = new JTextField();
        JTextField champMailVisiteur = new JTextField();
        JTextField champTelephoneVisiteur = new JTextField();
        JComboBox<String> comboVehicule = new JComboBox<>(new String[]{"Cycle", "Monospace", "4X4", "Van", "Truck et autres"});
        JComboBox<String> comboTypeLavage = new JComboBox<>(new String[]{"Lavage au rouleau", "Lavage haute pression", "Lavage à la main"});
        JComboBox<String> comboOption = new JComboBox<>(new String[]{"Aucune", "Nettoyage intérieur", "Pression pneus"});

        panelVisiteur.add(new JLabel("Nom :"));
        panelVisiteur.add(champNom);
        panelVisiteur.add(new JLabel("Prénom :"));
        panelVisiteur.add(champPrenom);
        panelVisiteur.add(new JLabel("Email :"));
        panelVisiteur.add(champMailVisiteur);
        panelVisiteur.add(new JLabel("Telephone :"));
        panelVisiteur.add(champTelephoneVisiteur);
        panelVisiteur.add(new JLabel("Type de véhicule :"));
        panelVisiteur.add(comboVehicule);
        panelVisiteur.add(new JLabel("Type de lavage :"));
        panelVisiteur.add(comboTypeLavage);

        panelVisiteur.add(new JLabel("Options supplémentaires :"));
        JPanel panelOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox OptInterieur = new JCheckBox("Nettoyage Intérieur");
        JCheckBox OptPneus = new JCheckBox("Pression des pneus");
        panelOptions.add(OptInterieur);
        panelOptions.add(OptPneus);
        panelVisiteur.add(panelOptions);

        panelCardLayout.add(panelAbonne, "Abonné");
        panelCardLayout.add(panelVisiteur, "Visiteur");
        dialog.add(panelCardLayout, BorderLayout.CENTER);

        comboType.addActionListener(e -> {
            CardLayout cl = (CardLayout) (panelCardLayout.getLayout());
            cl.show(panelCardLayout, (String) comboType.getSelectedItem());
        });

        JButton boutonValider = new JButton("Valider");
        boutonValider.addActionListener(e -> {
            String typeVehicule = (String) comboVehicule.getSelectedItem();
            String typeLavage = (String) comboTypeLavage.getSelectedItem();
            String option = (String) comboOption.getSelectedItem();
            String type = (String) comboType.getSelectedItem();
            String nom = (String) champNom.getText().trim();
            String prenom = (String) champPrenom.getText().trim();
            String telephone = (String) champTelephoneVisiteur.getText().trim();
            String email = (String) champMailAbonne.getText().trim();

            int supplementOption = 0;
            if (OptInterieur.isSelected()) supplementOption += 5;
            if (OptPneus.isSelected()) supplementOption += 5;



            if (type.equals("Abonné")) {
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Veuillez entrer votre email.");
                    return;
                }

                // TODO
                //  Vérifie l'email dans la personne liée à abonnement
                //  verifie quel type de place de son abonnement
                //  verifie si c'est VOITURE Ou moto
                //  et définis le prix
                //en attendant maryline
                int prix = 20; //exemple

            } else {
                if (nom.isEmpty() || prenom.isEmpty()|| telephone.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Champs manquants.");
                    return;
                }
                int prixBase = switch (typeLavage) {
                    case "Lavage à la main" -> 50;
                    case "Lavage haute pression" -> 40;
                    default -> 30;
                };
                int TypeVehicule = switch (typeVehicule) {
                    case "Cycle" -> 0;
                    case "Monospace" -> 10;
                    case "4X4" -> 20;
                    case "Van" -> 30;
                    case "Truck et autres" -> 40;
                    default -> 0;
                };

                int prix = prixBase + TypeVehicule + supplementOption;

                int choix = JOptionPane.showOptionDialog(dialog,
                        "Type d'utilisateur : " + type + "\n"
                                + "Nom : " + nom + "\n"
                                +"Prénom : " + prenom + "\n"
                                + "Telephone : " + telephone + "\n"
                                + "Email : " + email + "\n"
                                + "Type de véhicule : " + comboVehicule.getSelectedItem() + "\n"
                                + "Type de lavage : " + comboTypeLavage.getSelectedItem() + "\n"
                                + "Option : " + comboOption.getSelectedItem() + "\n"
                                + "Prix : " + prix + " €\n\n"
                                + "Souhaitez-vous continuer ?",
                        "Confirmation de paiement",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Payer "+prix+" €", "Annuler"},
                        "Payer");
                //je dois aussi inscrire le paiement dans la bd

                if (choix == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(dialog, "Paiement effectué. Merci !");
                    dialog.dispose();
                }

            }


        });

        JPanel panelSud = new JPanel();
        panelSud.add(boutonValider);
        dialog.add(panelSud, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }


    private static int getPrix(JComboBox<String> comboAbonne, JTextField champMail) {
        if (comboAbonne.getSelectedItem().equals("Oui")) {
            String email = champMail.getText().trim();
            String type = getTypeAbonnement(email);
            return switch (type) {
                case "Premium" -> 0;
                case "Standard" -> 20;
                default -> 50;
            };
        } else {
            return 50;
        }
    }

    private static void mettreAJourPrix(JComboBox<String> comboAbonne, JTextField champMail, JLabel labelPrix) {
        int prix = getPrix(comboAbonne, champMail);
        labelPrix.setText("Prix : " + prix + " €");
    }

    private static String getTypeAbonnement(String email) {
        return switch (email.toLowerCase()) {
            case "alice@exemple.com" -> "Premium";
            case "bob@exemple.com" -> "Standard";
            default -> null;
        };
    }

    public static void FormulairePaiementTechnique(JFrame parent, MecanicienUseCase mecanicienUseCase) {
        JDialog dialog = new JDialog(parent, "Paiement Service Technique", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));


        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField telField = new JTextField();
        JTextField mailField = new JTextField();
        JTextField cpField = new JTextField();
        JTextField dateServiceField = new JTextField();

        JComboBox<String> comboMecanicien = new JComboBox<>();
        comboMecanicien.addItem("Sélectionner un mécanicien");
        ArrayList<String> techniciens = new ArrayList<>();

        // je vais ajouter les mecanos recupérés dans la bd

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

            if (nomField.getText().trim().isEmpty() || prenomField.getText().trim().isEmpty() || prixField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Merci de remplir tous les champs obligatoires (nom, prénom, prix).");
                return;
            }

            String resume = String.format(
                    "Client : %s %s\nTéléphone : %s\nMail : %s\nCode Postal : %s\nDate service : %s\nMécanicien : %s\nPrix : %s €",
                    nomField.getText(), prenomField.getText(), telField.getText(), mailField.getText(), cpField.getText(),
                    dateServiceField.getText(), comboMecanicien.getSelectedItem(), prixField.getText());

            int choix = JOptionPane.showOptionDialog(dialog, resume + "\n\nConfirmer le paiement ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Payer", "Annuler"}, "Payer");

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

    /// /////FORMULAIRE RESERVATION ///////////

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