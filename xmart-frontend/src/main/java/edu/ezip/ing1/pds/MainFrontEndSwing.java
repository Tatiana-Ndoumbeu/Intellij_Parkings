package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.AbonementService;
import edu.ezip.ing1.pds.services.LocalLaveriesService;
import edu.ezip.ing1.pds.services.PlaceDeParkingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.util.UUID;

public class MainFrontEndSwing extends JFrame {

    public static final String VEHICLES = "Vehicles";
    public static final String ABONNEMENTS = "Abonnements";
    public static final String PERSONNES = "Personnes";
    public static final String PLACES_DE_PARKING = "Places de Parking";
    public static final String LOCAL_LAVERIES = "Local Laveries";

    private Abonnements abonnements = new Abonnements();
    private Personnes personnes = new Personnes();
    private Vehicles vehicles = new Vehicles();
    private final static String LoggingLabel = "FrontEnd";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String networkConfigFile = "network.yaml";
    private PlacesDeParkings placesDeParkings = new PlacesDeParkings();
    private LocalLaveries localLaveries = new LocalLaveries();


    public MainFrontEndSwing() {
        setTitle("Main Front End - Parking System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab(ABONNEMENTS, createTablePanel(ABONNEMENTS));
        tabbedPane.addTab(PERSONNES, createTablePanel(PERSONNES));
        tabbedPane.addTab(PLACES_DE_PARKING, createTablePanel(PLACES_DE_PARKING));
        tabbedPane.addTab(VEHICLES, createTablePanel(VEHICLES));
        tabbedPane.addTab(LOCAL_LAVERIES, createTablePanel(LOCAL_LAVERIES));
        add(tabbedPane);
    }

    private JPanel createTablePanel(String type) {
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
                table.setModel(createPlaceDeParkingTableModel(networkConfig));
                break;
            case VEHICLES:
                table.setModel(createVehicleTableModel());
                break;
            case LOCAL_LAVERIES:
                table.setModel(createLocalLaverieTableModel(networkConfig));
                break;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton insertButton = new JButton("Insert New " + type);
        logger.debug("Load Network config file : {}", networkConfig.toString());
        insertButton.addActionListener(e -> {
            switch (type) {
                case ABONNEMENTS:
                    insertAbonnements((DefaultTableModel) table.getModel());
                    break;
                case PERSONNES:
                    insertPersonne((DefaultTableModel) table.getModel());
                    break;
                case PLACES_DE_PARKING:
                    insertPlaceDeParking((DefaultTableModel) table.getModel());
                    break;
                case VEHICLES:
                    insertVehicle((DefaultTableModel) table.getModel());
                    break;
                case LOCAL_LAVERIES:
                    insertLocal((DefaultTableModel) table.getModel());
                    break;
            }
        });
        panel.add(insertButton, BorderLayout.SOUTH);

        return panel;
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

    private DefaultTableModel createPlaceDeParkingTableModel(NetworkConfig networkConfig) {
        final PlaceDeParkingService placeDeParkingService = new PlaceDeParkingService(networkConfig);
        String[] columns = {"ID", "Emplacement", "typePlace", "statutPlace"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            placesDeParkings = placeDeParkingService.selectPlaceDeParkings();
            if (placesDeParkings != null && placesDeParkings.getPlaceDeParkings() != null) {
                for (PlaceDeParking place : placesDeParkings.getPlaceDeParkings()) {
                    model.addRow(new Object[]{place.getIdPlace(), place.getEmplacement(), place.getStatutPlace(), place.getTypePlace()});
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching places de parking", e);
        }

        return model;
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
                    model.addRow(new Object[]{place.getIdAbonnement(),
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

    private void insertPlaceDeParking(DefaultTableModel model) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final PlaceDeParkingService placeDeParkingService = new PlaceDeParkingService(networkConfig);
        String emplacement = JOptionPane.showInputDialog(this, "Entrer Emplacement:");
        if (emplacement == null || emplacement.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Emplacement cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String typePlace = JOptionPane.showInputDialog(this, "Entrer Type de Place:");
        if (typePlace == null || typePlace.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Type cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String statutPlace = JOptionPane.showInputDialog(this, "Entrer Statut de Place (Occupée, Libre):");
        if (statutPlace == null || statutPlace.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "statut ne peut etre vide.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PlaceDeParking newPlace = new PlaceDeParking(UUID.randomUUID().toString(), emplacement, typePlace, statutPlace);


        try {
            placeDeParkingService.insertPlaceDeParkings(newPlace);

            model.addRow(new Object[]{newPlace.getIdPlace(), newPlace.getEmplacement(), newPlace.getTypePlace(), newPlace.getStatutPlace()});
            //model.addRow(new Object[]{newPlace.getIdPlace(), newPlace.getEmplacement(), newPlace.getStatutPlace(), newPlace.getTypePlace()});
            JOptionPane.showMessageDialog(this, "Place de parking inseré !.");
        } catch (IOException | InterruptedException e) {
            logger.error("echec insertion place de parking", e);
            JOptionPane.showMessageDialog(this, "echec insertion place de parking.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertAbonnements(DefaultTableModel model) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final AbonementService abonementService = new AbonementService(networkConfig);


        //private double prix;
        //private Date dateDebut;
        //private Date dateFin;
        //private String statutAbonnement;
        // Collecter la saisie pour nouvel abonnement
        String typeAbonnement = JOptionPane.showInputDialog(this, "Enter typeAbonnement:");
        if (typeAbonnement == null || typeAbonnement.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "typeAbonnement doit etre non vide.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String prix = JOptionPane.showInputDialog(this, "Entrer prix:");
        if (prix == null || prix.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "prix doit etre non vide", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String statutAbonnement = JOptionPane.showInputDialog(this, "Entrer statutAbonnement  (ex, actif, inactif):");
        if (statutAbonnement == null || statutAbonnement.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Statut  doit etre non vide.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // nouvelle abonnement
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

    private void insertLocal(DefaultTableModel model) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        final LocalLaveriesService localService = new LocalLaveriesService(networkConfig);


        //  private int numLocalL;
        //
        //
        //    private Boolean disponibilite;g
        String numLocal = JOptionPane.showInputDialog(this, "num local :");
        if (numLocal == null || numLocal.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "num local cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String disponibilite = JOptionPane.showInputDialog(this, "Entrer disponibilite du local true ou false:");
        if (disponibilite == null || disponibilite.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "disponibilite cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        LocalLaverie localLaverie1 = new LocalLaverie();
        localLaverie1.setNumLocalL(Integer.parseInt(numLocal));
        localLaverie1.setDisponibilite(Boolean.parseBoolean(disponibilite));

        try {
            localService.insertLoclaLaveries(localLaverie1);
            model.addRow(new Object[]{localLaverie1.getNumLocalL(), localLaverie1.getDisponibilite()});
            JOptionPane.showMessageDialog(this, "Local Inséré.");
            // Refresh the table after insertion
            createTablePanel("createTablePanel");
            JOptionPane.showMessageDialog(this, "Local inséré.");
        } catch (IOException | InterruptedException e) {
            logger.error("Erreur insertion Local", e);
            JOptionPane.showMessageDialog(this, "Erreur insertion Local.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrontEndSwing frame = new MainFrontEndSwing();
            frame.setVisible(true);
        });
    }
}
