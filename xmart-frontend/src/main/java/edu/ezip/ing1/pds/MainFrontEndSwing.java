package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.*;
//import edu.ezip.ing1.pds.uiUtils.MecanicienViewModel;
import edu.ezip.ing1.pds.uiUtils.PlaceDeParkingViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;

import java.util.*;
import java.util.List;

import static edu.ezip.ing1.pds.uiUtils.PlaceDeParkingViewModel.*;

public class MainFrontEndSwing extends JFrame {

    public static final String VEHICLES = "Vehicles";
    public static final String ABONNEMENTS = "Abonnements";
    public static final String PERSONNES = "Personnes";
    public static final String PLACES_DE_PARKING = "Places de Parking";
    public static final String LOCAL_LAVERIES = "Local Laverie";
    public static final String LOCAL_TECHNIQUE = "Local Technique";
    public static final String MECANICIEN = "Mecanicien";
    public static final String RESERVATION = "Reservation";


    private Abonnements abonnements = new Abonnements();
    private Personnes personnes = new Personnes();
    private Vehicles vehicles = new Vehicles();
    private Mecaniciens mecaniciens = new Mecaniciens();
    private Reservations reservations = new Reservations();
    private PlacesDeParkings placesDeParkings = new PlacesDeParkings();
    private LocalLaveries localLaveries = new LocalLaveries();
    private LocalTechniques localTechniques = new LocalTechniques();

    private final static String LoggingLabel = "FrontEnd";
    private final static String networkConfigFile = "network.yaml";

    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);


    public MainFrontEndSwing() {
        setTitle("DASHBOARD ET SERVICES");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab(ABONNEMENTS, createTablePanel(ABONNEMENTS));
        tabbedPane.addTab(PERSONNES, createTablePanel(PERSONNES));
        tabbedPane.addTab(PLACES_DE_PARKING, createTablePanel(PLACES_DE_PARKING));
        tabbedPane.addTab(VEHICLES, createTablePanel(VEHICLES));
        tabbedPane.addTab(MECANICIEN, createTablePanel(MECANICIEN));
        tabbedPane.addTab(LOCAL_LAVERIES, createTablePanelLocaux(LOCAL_LAVERIES));
        tabbedPane.addTab(LOCAL_TECHNIQUE, createTablePanelLocaux(LOCAL_TECHNIQUE));
        tabbedPane.addTab(RESERVATION, createTablePanelresa());


        add(tabbedPane);
    }


    private ImageIcon chargerIcone(String chemin, int largeur, int hauteur) {

        ImageIcon icon = new ImageIcon(getClass().getResource(chemin));
        Image image = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }



    private JPanel createTablePanelLocaux(String LOCAL)
    {
        JPanel panelsud = new JPanel(new FlowLayout());
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();

        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        switch (LOCAL) {
            case LOCAL_LAVERIES:
                table.setModel(createLocalLaverieTableModel(networkConfig));
                break;
            case LOCAL_TECHNIQUE:
                table.setModel(createLocalTechniqueTableModel(networkConfig));
                break;
        }
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton insertButton = new JButton("Ajouter un " + LOCAL, chargerIcone("/icons/ajouter.png", 30, 30));
        insertButton.setBackground(Color.GREEN);
        insertButton.addActionListener(e -> {
            logger.debug("Ajout d'un local pour {}", LOCAL);
            switch (LOCAL) {
                case LOCAL_LAVERIES:
                    insertLocal((DefaultTableModel) table.getModel());
                    break;
                case LOCAL_TECHNIQUE:
                    insertLocalT((DefaultTableModel) table.getModel());
                    break;
                          }
        });
        panelsud.add(insertButton);

        JButton disponibutton = new JButton("modifier une disponibilité", chargerIcone("/icons/modifier.png", 30, 30));
        disponibutton.setBackground(Color.YELLOW);
   
         disponibutton.addActionListener(e -> {
            logger.debug("Modification de la disponibilité pour {}", LOCAL);
             switch (LOCAL) {
                 case LOCAL_LAVERIES:
                     updateLocal((DefaultTableModel) table.getModel());
                     break;
                 case LOCAL_TECHNIQUE:
                     updateLocalT((DefaultTableModel) table.getModel());
                     break;
                 case PLACES_DE_PARKING:
                     updatePlaceDeParking(networkConfig,(DefaultTableModel) table.getModel(),this,networkConfigFile,logger);
                     break;
    }
         });
        panelsud.add(disponibutton);
        JButton supprimebouton = new JButton("supprimer un Local", chargerIcone("/icons/supprimer.png", 30, 30));
        supprimebouton.setBackground(Color.RED);

        supprimebouton.addActionListener(e -> {
            logger.debug("Suppression  {}", LOCAL);
            switch (LOCAL) {
                case LOCAL_LAVERIES:
                   deleteLocal((DefaultTableModel) table.getModel());
                    break;
                case LOCAL_TECHNIQUE:
                    deleteLocalT((DefaultTableModel) table.getModel());
                    break;
            }
        });
        panelsud.add(supprimebouton);
        panel.add(panelsud, BorderLayout.SOUTH);
 
        return panel;


    }
    private JPanel createTablePanel(String type) {
        JPanel panelsud = new JPanel(new FlowLayout());
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        switch (type) {
            case ABONNEMENTS:
                table.setModel(createAbonnementTableModel(networkConfig));
                break;
            case PERSONNES:
                table.setModel(createPersonneTableModel());
                break;
            case PLACES_DE_PARKING:
                table.setModel(createPlaceDeParkingTableModel(networkConfig,placesDeParkings,logger));
                break;
            case VEHICLES:
                table.setModel(createVehicleTableModel());
                break;
            case MECANICIEN:
                table.setModel(createMecanicienTableModel(networkConfig));
                break;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);


        JButton insertButton = new JButton("Ajouter " + type, chargerIcone("/icons/ajouter.png", 30, 30));
        insertButton.setBackground(Color.GREEN);
        logger.debug("Load Network config file : {}", networkConfig.toString());
        insertButton.addActionListener(e -> {
            switch (type) {
                case ABONNEMENTS:
                    //insertAbonnements((DefaultTableModel) table.getModel());
                    Formulaires.FormulaireAbonnements(this);
                    break;
                case PERSONNES:
                    //insertPersonne((DefaultTableModel) table.getModel());
                    Formulaires.FormulaireAbonnements(this);
                    break;
                case PLACES_DE_PARKING:
                    insertPlaceDeParking((DefaultTableModel) table.getModel(),this,networkConfigFile,logger);
                    break;
                case VEHICLES:
                    insertVehicle((DefaultTableModel) table.getModel());
                    break;
                case MECANICIEN:
                    insertMecanicien((DefaultTableModel) table.getModel());
                    //MecanicienViewModel.insertMecanicien( (DefaultTableModel) table.getModel(), networkConfigFile,this, logger);
                    break;

            }
        });
        panelsud.add(insertButton);


        JButton deleteButton = new JButton("Supprimer "+type, chargerIcone("/icons/supprimer.png", 30, 30));
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(e -> {
            switch (type) {
                case ABONNEMENTS:
                    supprimerAbonnement((DefaultTableModel) table.getModel(), table);
                    break;
                case PLACES_DE_PARKING:
                   PlaceDeParkingViewModel.supprimerPlaceDeParking((DefaultTableModel) table.getModel(), table,this,placesDeParkings,networkConfigFile,logger);
                    break;
            
                default:
                    break;
            }
        });
        panelsud.add(deleteButton);

        JButton updateAbobutton = new JButton("modification " +type, chargerIcone("/icons/modifier.png", 30, 30));
        updateAbobutton.setBackground(Color.yellow);
        updateAbobutton.addActionListener(e ->{
            switch (type) {
                case ABONNEMENTS:
                    logger.debug("Modification pour {}", type);
                    updateAbonnement((DefaultTableModel) table.getModel());
                    break;
                case PLACES_DE_PARKING:
                    logger.debug("Modification pour {}", type);
                    updatePlaceDeParking(networkConfig,(DefaultTableModel) table.getModel(),this,networkConfigFile,logger);
                    break;

                default:
                    break;
            }
        });
        panelsud.add(updateAbobutton);
        panel.add(panelsud, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTablePanelresa()
    {
        JPanel panelsud = new JPanel(new FlowLayout());
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        table.setModel(createReservationTableModel(networkConfig));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton reserverButton = new JButton("reserver une zone spéciale",chargerIcone("/icons/reserver.png", 30, 30));
        reserverButton.setBackground(Color.CYAN);
        reserverButton.addActionListener( e -> Formulaires.FormulaireReservation(this));
        panelsud.add(reserverButton);
        panel.add(panelsud, BorderLayout.SOUTH);

        JButton reservationEnCoursButton = new JButton("Afficher toutes les reservvations", chargerIcone("/icons/liste.png", 30, 30));
        reservationEnCoursButton.setBackground(Color.CYAN);
        reservationEnCoursButton.addActionListener(e->table.setModel(reservationEnregistreesTableModel(networkConfig)));
        panelsud.add(reservationEnCoursButton);

        JButton calendrierResa = new JButton("Calendrier");
        calendrierResa.setBackground(Color.CYAN);
        calendrierResa.addActionListener( e -> ouvrirCalendrier());
            //table.setModel(createCalendrierTableModel(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));

        panelsud.add(calendrierResa);

        return panel;
    }
    private void ouvrirCalendrier() {

        Map<LocalDate, List<String>> reservations = new HashMap<>();

        JFrame calendrierFrame = new JFrame("Calendrier des réservations");
        calendrierFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        calendrierFrame.setSize(550, 450);

        //CalendrierPanel calendrierPanel = new CalendrierPanel(reservations);
        //calendrierFrame.add(calendrierPanel);
        calendrierFrame.setVisible(true);
    }
    public DefaultTableModel createCalendrierTableModel(int annee, int mois) {
        JPanel pannelNord = new JPanel(new FlowLayout());
        JButton boutonPrecedent = new JButton("Mois précédent");
        JButton boutonSuivant = new JButton("Mois suivant");

        //boutonPrecedent.addActionListener(e -> changerMois(-1));
        //boutonSuivant.addActionListener(e -> changerMois(1));

        pannelNord.add(boutonPrecedent);
        pannelNord.add(boutonSuivant);
        add(pannelNord, BorderLayout.NORTH);

        String[] columns = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        YearMonth yearMonth = YearMonth.of(annee, mois);
        LocalDate premierJour = yearMonth.atDay(1);// Premier jour du mois
        int premierJourSemaine = premierJour.getDayOfWeek().getValue();// Jour de la semaine du premier jour (1 = Lundi, 7 = Dimanche)
        int joursDansMois = yearMonth.lengthOfMonth(); // Nombre de jours dans le mois

        Object[][] donnees = new Object[6][7];
        int jour = 1;

        for (int i = (premierJourSemaine - 1) % 7; jour <= joursDansMois; i++) {
            int ligne = i / 7;
            int colonne = i % 7;
            donnees[ligne][colonne] = jour++;
        }

        for (Object[] row : donnees) {
            model.addRow(row);
        }

        return model;
    }

    private DefaultTableModel createPersonneTableModel() {
        String[] columns = {"ID", "Nom", "Prenom"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        if (personnes.getPersonnes() != null) {
            for (Personne personne : personnes.getPersonnes()) {
                model.addRow(new Object[]{personne.getIdPersonne(), personne.getNom(), personne.getPrenom()});
            }
        }
        return model;
    }

    private DefaultTableModel createVehicleTableModel() {
        String[] columns = {"Num Plaque", "Type", "Marque"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        if (vehicles.getVehicules() != null) {
            for (Vehicle vehicle : vehicles.getVehicules()) {
                model.addRow(new Object[]{vehicle.getNumPlaque(), vehicle.getType(), vehicle.getMarque()});
            }
        }
        return model;
    }

    private DefaultTableModel createMecanicienTableModel(NetworkConfig networkConfig) {
        final MecanicienService mecanicienService = new MecanicienService(networkConfig);
        String[] columns = {"Nom", "Prenom", "Spécialité"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            mecaniciens = mecanicienService.selectMecanicien();
            if (mecaniciens != null && mecaniciens.getMecaniciens() != null) {
                    for (Mecanicien mecanicien : mecaniciens.getMecaniciens()) {
                        model.addRow(new Object[]{mecanicien.getNom(), mecanicien.getPrenom(), mecanicien.getSpecialite()});

            }}
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur recuperation mecanicien", e);
        }
        return model;
    }
    private DefaultTableModel createReservationTableModel(NetworkConfig networkConfig) {
        final ReservationService reservationService = new ReservationService(networkConfig);
        String[] columns = {"id","emplacement", "diponibilite", "type "};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            placesDeParkings = reservationService.selectZoneSpeciale();
            if (placesDeParkings != null && placesDeParkings.getPlaceDeParkings() != null) {
                for (PlaceDeParking placeDeParking : placesDeParkings.getPlaceDeParkings()) {
                    model.addRow(new Object[]{
                            placeDeParking.getIdPlace(),
                            placeDeParking.getEmplacement(),
                            placeDeParking.getTypePlace()
                    });

                }}
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur recuperation reservation", e);
        }
        return model;
    }




    private DefaultTableModel reservationEnregistreesTableModel(NetworkConfig networkConfig) {
        final ReservationService reservationService = new ReservationService(networkConfig);
        String[] columns = {"id reservation","Position", "type", "date début"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try{
            reservations = reservationService.selectReservations();
            if (reservations != null && reservations.getReservations() != null){
                for (Reservation reservation : reservations.getReservations()) {
                    PlaceDeParking place = reservation.getPlaceDeParking();
                    model.addRow(new Object[]{
                            reservation.getIdReservation(),
                            (place != null) ? place.getIdPlace() : "Non attribué",
                            (place != null) ? place.getTypePlace() : "Non attribué",
                            reservation.getDateEntree()
                    });

                }
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Erreur recuperation reservation", e);
        }
        return model;
    }





    private void insertPersonne(DefaultTableModel model) {
        String nom = JOptionPane.showInputDialog(this, "Enter Nom:");
        String prenom = JOptionPane.showInputDialog(this, "Enter Prenom:");

        Personne newPersonne = new Personne(UUID.randomUUID().toString(), nom, prenom, "", "", "");
        personnes.getPersonnes().add(newPersonne);
        model.addRow(new Object[]{newPersonne.getIdPersonne(), newPersonne.getNom(), newPersonne.getPrenom()});
    }

    private void insertVehicle(DefaultTableModel model) {
        String numPlaque = JOptionPane.showInputDialog(this, "Enter Num Plaque:");
        String type = JOptionPane.showInputDialog(this, "Enter Type:");
        String marque = JOptionPane.showInputDialog(this, "Enter Marque:");

        Vehicle newVehicle = new Vehicle(numPlaque, type, marque);
        vehicles.getVehicules().add(newVehicle);
        model.addRow(new Object[]{newVehicle.getNumPlaque(), newVehicle.getType(), newVehicle.getMarque()});
    }



    private DefaultTableModel createAbonnementTableModel(NetworkConfig networkConfig) {
        final AbonementService abonementService = new AbonementService(networkConfig);
        String[] columns = { "idAbonnement", "typeAbonnement", "prix", "statutAbonnement", "dateDebut", "dateFin"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Récupère et affiche les places de parking existantes depuis BD
        try {
            abonnements = abonementService.selectAbonnements();
            if (abonnements != null && abonnements.getAbonnements() != null) {
                for (Abonnement place : abonnements.getAbonnements()) {
                    model.addRow(new Object[]{
                            place.getIdAbonnement(),
                            place.getTypeAbonnement(),
                            place.getPrix(),
                            place.getStatutAbonnement(),
                            place.getDateDebut(),
                            place.getDateFin()});
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching places de parking", e);
        }

        return model;
    }
 private DefaultTableModel createLocalLaverieTableModel(NetworkConfig networkConfig) {
        final LocalLaveriesService localService = new LocalLaveriesService(networkConfig);
        String[] columns = { "numLocal", "disponibilite"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

     // 
        try {
            localLaveries = localService.selectLocalLaveries();
            if (localLaveries != null && localLaveries.getLocalLaveries() != null) {
                for (LocalLaverie place : localLaveries.getLocalLaveries()) {
                    model.addRow(new Object[]{place.getNumLocalL(),
                            place.getDisponibilite(),
                    });
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching Local laverie", e);
        }

        return model;
    }

    private DefaultTableModel createLocalTechniqueTableModel(NetworkConfig networkConfig) {
        final LocalTechniqueService localService = new LocalTechniqueService(networkConfig);
        String[] columns = { "numLocal", "disponibilite"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        //
        try {
            localTechniques = localService.selectLocalTechnique();
            if (localTechniques != null && localTechniques.getLocalTechniques() != null) {
                for (LocalTechnique place : localTechniques.getLocalTechniques()) {
                    model.addRow(new Object[]{place.getNumLocalT(),
                            place.getDisponibilite(),
                    });
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur recuperation Local Technique", e);
        }

        return model;
    }


    private void insertAbonnements(DefaultTableModel model) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final AbonementService abonementService = new AbonementService(networkConfig);
        
        String typeAbonnement;
        while (true) {
            typeAbonnement = JOptionPane.showInputDialog(this, "Entrer le type d'abonnement (Premium ou Standard)");
            if (typeAbonnement == null) {
                JOptionPane.showMessageDialog(this, "Opération annulée.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (isValidType(typeAbonnement)) {
                break;
            }
            JOptionPane.showMessageDialog(this, "Type d'abonnement invalide. Choisissez parmi : Premium, Standard", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        String prix;
        while (true) {
            prix = JOptionPane.showInputDialog(this, "Entrer le prix de l'abonnement:");
            if (prix==null){
                JOptionPane.showMessageDialog(this, "Opération annulée.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (isValidPrix(prix)) {
                break;
            }
            JOptionPane.showMessageDialog(this, "Prix invalide : entrez un nombre positif", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        String statutAbonnement;
        while (true) {
            statutAbonnement = JOptionPane.showInputDialog(this, "Entrer le statut de l'abonnement (Actif, Inactif, Suspendu) :");
            if (statutAbonnement==null){
                JOptionPane.showMessageDialog(this, "Opération annulée.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (isValidStatut(statutAbonnement)) {
                break;
            }
            JOptionPane.showMessageDialog(this, "Statut invalide : choisissez parmi (Actif, Inactif ou Suspendu)", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        // nouvel abonnement
        Abonnement abonnement = new Abonnement();
        abonnement.setStatutAbonnement(statutAbonnement);
        abonnement.setTypeAbonnement(typeAbonnement);
        abonnement.setPrix(Double.parseDouble(prix));
        abonnement.setDateDebut( new Date(2025,12,12));
        abonnement.setDateFin( new Date(2025,12,23));

        try {
            abonementService.insertAbonements(abonnement);

            // rafraichir la table
            model.addRow(new Object[]{abonnement.getIdAbonnement(),
                    abonnement.getPrix(),
                    abonnement.getTypeAbonnement(),
                    abonnement.getStatutAbonnement(),
                    abonnement.getDateDebut(),
                    abonnement.getDateFin()});
            JOptionPane.showMessageDialog(this, "abonnement inseré avec succes.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur insertion abonnement", e);
            JOptionPane.showMessageDialog(this, "Erreur insertion abonnement.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidType(String type) {
        return type != null && (type.equalsIgnoreCase("Premium") || type.equalsIgnoreCase("Standard"));
    }
    private boolean isValidPrix(String prix) {
        try {
            return prix != null && Double.parseDouble(prix) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }private boolean isValidStatut(String statut) {
        return statut != null && (statut.equalsIgnoreCase("Actif") || statut.equalsIgnoreCase("Inactif") || statut.equalsIgnoreCase("Suspendu"));
    }

    private void supprimerAbonnement(DefaultTableModel model, JTable table){
        String idAbonnement = JOptionPane.showInputDialog(this, "Entrez l'identifiant de l'abonnement à supprimer :",
                "Suppression d'un abonnement", JOptionPane.QUESTION_MESSAGE);

        if (idAbonnement == null || idAbonnement.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "L'identifiant est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet abonnement ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
        try {
            AbonementService abonementService = new AbonementService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
            abonementService.supprimerAbonnement(idAbonnement);
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(idAbonnement)) {
                    model.removeRow(i);
                    break;
                }
            }
            JOptionPane.showMessageDialog(this, "Abonnement supprimé avec succès !");
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'abonnement", e);
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'abonnement.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        }
    }


    private void updateAbonnement(DefaultTableModel model) {

        String idAbo = JOptionPane.showInputDialog(this, "Identifiant de l'abonnement à modifier :");
        AbonementService abonementService = new AbonementService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
        if (idAbo == null || idAbo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "L'identifiant ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String typeAbo;
        while (true) {
            typeAbo = JOptionPane.showInputDialog(this, "Nouveau type de l'abonnement :");
            if (typeAbo == null || typeAbo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le type de l'abonnement ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!isValidType(typeAbo)) {
                JOptionPane.showMessageDialog(this, "Type d'abonnement invalide. Choisissez parmi : Premium, Standard", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            break;
        }
        String prix;
        while (true) {
            prix = JOptionPane.showInputDialog(this, "Nouveau prix de l'abonnement :");
            if (prix == null || prix.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le prix de l'abonnement ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!isValidPrix(prix)) {
                JOptionPane.showMessageDialog(this, "Prix invalide : entrez un nombre positif", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            break;
        }
        String statut;
        while (true) {
            statut = JOptionPane.showInputDialog(this, "Nouveau statut de l'abonnement :");
            if (statut == null || statut.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le statut de l'abonnement ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;  
            }
            if (!isValidStatut(statut)) {  
                JOptionPane.showMessageDialog(this, "Statut invalide : choisissez parmi (Actif, Inactif ou Suspendu)", "Erreur", JOptionPane.ERROR_MESSAGE);
                continue;  
            }
            break;  
        }

        Abonnement AboUpdated= new Abonnement();
        AboUpdated.setIdAbonnement(idAbo);
        AboUpdated.setTypeAbonnement(typeAbo);
        AboUpdated.setPrix(Double.parseDouble(prix));
        AboUpdated.setStatutAbonnement(statut);

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(idAbo)) {
                model.setValueAt(typeAbo, i, 1);
                model.setValueAt(prix, i, 2);
                model.setValueAt(statut, i, 3);
                break;
            }
        }


    }
    private void insertLocal(DefaultTableModel model) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final LocalLaveriesService localService = new LocalLaveriesService(networkConfig);

        String numLocal = JOptionPane.showInputDialog(this, "num local :");
        if (numLocal == null || numLocal.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "num local cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocal);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String disponibilite = JOptionPane.showInputDialog(this, "Entrer disponibilite du local true ou false:");
        if (disponibilite == null || disponibilite.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "disponibilite ne peut pas être vide.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!disponibilite.equalsIgnoreCase("true") && !disponibilite.equalsIgnoreCase("false")){
            JOptionPane.showMessageDialog(this, "La disponibilité est soit 'true' soit 'false'' ", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalLaverie localLaverie1 = new LocalLaverie();
        localLaverie1.setNumLocalL(Integer.parseInt(numLocal));
        localLaverie1.setDisponibilite(Boolean.parseBoolean(disponibilite));

        try {
            localService.insertLoclaLaveries(localLaverie1);
            model.addRow(new Object[]{localLaverie1.getNumLocalL(), localLaverie1.getDisponibilite()});
            JOptionPane.showMessageDialog(this, "Local Inséré.");

            createTablePanel("createTablePanel");
            JOptionPane.showMessageDialog(this, "Local inséré.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur insertion Local", e);
            JOptionPane.showMessageDialog(this, "Erreur insertion Local.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateLocal(DefaultTableModel model) {


        String numLocal = JOptionPane.showInputDialog(this, "Numéro du local à modifier :");
        if (numLocal == null || numLocal.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro du local ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocal);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String disponibilite = JOptionPane.showInputDialog(this, "Nouvelle disponibilité (true ou false) :");
        if (disponibilite == null || disponibilite.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La disponibilité ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!disponibilite.equalsIgnoreCase("true") && !disponibilite.equalsIgnoreCase("false")){
            JOptionPane.showMessageDialog(this, "La disponibilité est soit 'true' soit 'false'' ", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean newDisponibilite = Boolean.parseBoolean(disponibilite);

        boolean localTrouve = false;
        for (LocalLaverie local : localLaveries.getLocalLaveries()) {
            if (String.valueOf(local.getNumLocalL()).equals(numLocal)) {
                local.setDisponibilite(newDisponibilite);
                localTrouve = true;
                break;
            }
        }

        if (!localTrouve) {
            JOptionPane.showMessageDialog(this, "Local non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalLaverie localLaveriemodif = new LocalLaverie();
            localLaveriemodif.setNumLocalL(Integer.parseInt(numLocal));
            localLaveriemodif.setDisponibilite(newDisponibilite);

            final LocalLaveriesService localService = new LocalLaveriesService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
            localService.updateLocLaveries(localLaveriemodif);

            model.setRowCount(0);// on supprime dabord les anciennes lignes dans le tableau
            for (LocalLaverie place : localLaveries.getLocalLaveries()) {
                model.addRow(new Object[]{place.getNumLocalL(), place.getDisponibilite()});
            }

            JOptionPane.showMessageDialog(this, "Disponibilité du local mise à jour.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur lors de la mise à jour du local", e);
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du local.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }
    private void deleteLocal(DefaultTableModel model) {
        String numLocalL = JOptionPane.showInputDialog(this, "Numéro du local à supprimer :");
        if (numLocalL == null || numLocalL.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro du local ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocalL);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }



        boolean localTrouve = false;
        LocalLaverie localASupprimer = null;
        for (LocalLaverie local : localLaveries.getLocalLaveries()) {
            if (String.valueOf(local.getNumLocalL()).equals(numLocalL)) {
                localASupprimer = local;
                localTrouve = true;
                break;
            }
        }

        if (!localTrouve) {
            JOptionPane.showMessageDialog(this, "Local non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer ce local ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {
                final LocalLaveriesService localService = new LocalLaveriesService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
            localService.deleteLocalLaverie(localASupprimer);

            localLaveries.getLocalLaveries().remove(localASupprimer);

            model.setRowCount(0);
            for (LocalLaverie place : localLaveries.getLocalLaveries()) {
                model.addRow(new Object[]{place.getNumLocalL(), place.getDisponibilite()});
            }

            JOptionPane.showMessageDialog(this, "Local supprimé avec succès.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur lors de la suppression du local", e);
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du local.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLocalT(DefaultTableModel model) {

        String numLocalT = JOptionPane.showInputDialog(this, "Numéro du local à modifier :");
        if (numLocalT == null || numLocalT.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro du local ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocalT);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String disponibilite = JOptionPane.showInputDialog(this, "Nouvelle disponibilité (true ou false) :");
        if (disponibilite == null || disponibilite.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La disponibilité ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!disponibilite.equalsIgnoreCase("true") && !disponibilite.equalsIgnoreCase("false")){
            JOptionPane.showMessageDialog(this, "La disponibilité est soit 'true' soit 'false'' ", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean newDisponibilite = Boolean.parseBoolean(disponibilite);

        boolean localTrouve = false;
        for (LocalTechnique local : localTechniques.getLocalTechniques()) {
            if (String.valueOf(local.getNumLocalT()).equals(numLocalT)) {
                local.setDisponibilite(newDisponibilite);
                localTrouve = true;
                break;
            }
        }

        if (!localTrouve) {
            JOptionPane.showMessageDialog(this, "Local non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalTechnique localTechniquemodif = new LocalTechnique();
            localTechniquemodif.setNumLocalT(Integer.parseInt(numLocalT));
            localTechniquemodif.setDisponibilite(newDisponibilite);

            final LocalTechniqueService localService = new LocalTechniqueService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
            localService.updateLocTechnique(localTechniquemodif);

            model.setRowCount(0);// on supprime dabord les anciennes lignes dans le tableau
            for (LocalTechnique place : localTechniques.getLocalTechniques()) {
                model.addRow(new Object[]{place.getNumLocalT(), place.getDisponibilite()});
            }

            JOptionPane.showMessageDialog(this, "Disponibilité du local mise à jour.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur lors de la mise à jour du local", e);
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du local.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void insertLocalT(DefaultTableModel model) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final LocalTechniqueService localService = new LocalTechniqueService(networkConfig);

        String numLocalT = JOptionPane.showInputDialog(this, "numero local :");
        if (numLocalT == null || numLocalT.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "num local ne peut pas être vide.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocalT);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String disponibilite = JOptionPane.showInputDialog(this, "Entrer disponibilite du local true ou false:");
        if (disponibilite == null || disponibilite.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "disponibilite cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!disponibilite.equalsIgnoreCase("true") && !disponibilite.equalsIgnoreCase("false")){
            JOptionPane.showMessageDialog(this, "La disponibilité est soit 'true' soit 'false'' ", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }


        LocalTechnique localTechnique1 = new LocalTechnique();
        localTechnique1.setNumLocalT(Integer.parseInt(numLocalT));
        localTechnique1.setDisponibilite(Boolean.parseBoolean(disponibilite));

        try {
            localService.insertLocTechnique(localTechnique1);
            model.addRow(new Object[]{localTechnique1.getNumLocalT(), localTechnique1.getDisponibilite()});
            //JOptionPane.showMessageDialog(this, "Local Inséré.");
            // Refresh  after insertion
            createTablePanel("createTablePanel");
            JOptionPane.showMessageDialog(this, "Local inséré.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur insertion Local", e);
            JOptionPane.showMessageDialog(this, "Erreur insertion Local.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    private void deleteLocalT(DefaultTableModel model) {
        String numLocalT = JOptionPane.showInputDialog(this, "Numéro du local à supprimer :");
        if (numLocalT == null || numLocalT.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro du local ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer.parseInt(numLocalT);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Num local doit être un entier.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean localTrouve = false;
        LocalTechnique localASupprimer = null;
        for (LocalTechnique local : localTechniques.getLocalTechniques()) {
            if (String.valueOf(local.getNumLocalT()).equals(numLocalT)) {
                localASupprimer = local;
                localTrouve = true;
                break;
            }
        }

        if (!localTrouve) {
            JOptionPane.showMessageDialog(this, "Local non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer ce local ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            final LocalTechniqueService localService = new LocalTechniqueService(ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile));
            localService.deleteLocalTechnique(localASupprimer);

            localTechniques.getLocalTechniques().remove(localASupprimer);

            model.setRowCount(0);
            for (LocalTechnique place : localTechniques.getLocalTechniques()) {
                model.addRow(new Object[]{place.getNumLocalT(), place.getDisponibilite()});
            }

            JOptionPane.showMessageDialog(this, "Local supprimé avec succès.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur lors de la suppression du local", e);
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du local.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void insertMecanicien(DefaultTableModel model) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final MecanicienService mecanicienService = new MecanicienService(networkConfig);

        String nom = JOptionPane.showInputDialog(this, "nom du mecano :");
        if (nom == null || nom.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "nom du mecanicien ne peut etre vide", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String prenom = JOptionPane.showInputDialog(this, "Entrer son prénom :");
        String telephone = JOptionPane.showInputDialog(this, "Entrer son numero de telephone(ex:0743434343) :");
        String disponibilite= JOptionPane.showInputDialog(this, "Entrer sa disponibilité pour ce mois :");
        String specialite = JOptionPane.showInputDialog(this, "Entrer sa spécialité :");
        if(specialite == null || specialite.trim().isEmpty()) {
            do { specialite = JOptionPane.showInputDialog(this, "Entrer sa spécialité :");}
            while (specialite == null || specialite.trim().isEmpty());
        }
        String mail = JOptionPane.showInputDialog(this, "Entrer son mail :");


        Mecanicien mecanicien1 = new Mecanicien();
        mecanicien1.setNom(nom);
        mecanicien1.setPrenom(prenom);
        mecanicien1.setTelephone(telephone);
        mecanicien1.setDisponibilite(Boolean.parseBoolean(disponibilite));
        mecanicien1.setSpecialite(specialite);
        mecanicien1.setMail(mail);
        try {
            mecanicienService.insertMecanicien(mecanicien1);
            model.addRow(new Object[]{mecanicien1.getNom(), mecanicien1.getPrenom(), mecanicien1.getSpecialite()});
            JOptionPane.showMessageDialog(this, "Mecanicien Inséré.");
            createTablePanel("createTablePanel");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur insertion Mecanicien", e);
            JOptionPane.showMessageDialog(this, "Erreur insertion Mecanicien.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainFrontEndSwing frame = new MainFrontEndSwing();
            frame.setVisible(true);
        });
    }
}
