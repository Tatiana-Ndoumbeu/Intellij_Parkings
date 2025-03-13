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

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class MainFrontEndSwing extends JFrame {

    public static final String VEHICLES = "Vehicles";
    public static final String ABONNEMENTS = "Abonnements";
    public static final String PERSONNES = "Personnes";
    public static final String PLACES_DE_PARKING = "Places de Parking";
    public static final String LOCAL_LAVERIES = "Local Laverie";
    public static final String LOCAL_TECHNIQUE = "Local Technique";
    public static final String TECHNICIENS = "Techniciens";


    private Abonnements abonnements = new Abonnements();
    private Personnes personnes = new Personnes();
    private Vehicles vehicles = new Vehicles();
    //private Technicien technicien = new Technicien(); à continuer
    private final static String LoggingLabel = "FrontEnd";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String networkConfigFile = "network.yaml";
    private PlacesDeParkings placesDeParkings = new PlacesDeParkings();
    private LocalLaveries localLaveries = new LocalLaveries();
    //private LocalTechnique LocalTechnique = new LocalTechnique(); à continuer


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
        tabbedPane.addTab(LOCAL_LAVERIES, createTablePanelLocaux(LOCAL_LAVERIES));
        tabbedPane.addTab(LOCAL_TECHNIQUE, createTablePanelLocaux(LOCAL_TECHNIQUE));
        tabbedPane.addTab(TECHNICIENS, createTablePanel(TECHNICIENS));
        add(tabbedPane);
    }






    private JPanel createTablePanelLocaux(String LOCAL_LAVERIES)
    {
        JPanel panelsud = new JPanel(new FlowLayout());
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();

        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        table.setModel(createLocalLaverieTableModel(networkConfig));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton insertButton = new JButton("Ajouter un " + LOCAL_LAVERIES);
        
        insertButton.addActionListener(e -> {
            logger.debug("Ajout d'un local pour {}", LOCAL_LAVERIES);
            insertLocal((DefaultTableModel) table.getModel());});

        panelsud.add(insertButton);


        JButton disponibutton = new JButton("modifier une disponibilité");
   
         disponibutton.addActionListener(e -> {
            logger.debug("Modification de la disponibilité d'un local");
            updateLocal((DefaultTableModel) table.getModel());});

        panelsud.add(disponibutton);
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
                table.setModel(createPlaceDeParkingTableModel(networkConfig));
                break;
            case VEHICLES:
                table.setModel(createVehicleTableModel());
                break;
            case TECHNICIENS:
                table.setModel(createTechnicienTableModel());
                break;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);


        JButton insertButton = new JButton("Ajouter " + type);
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
                //case TECHNICIENS:
                 //   insertLocal((DefaultTableModel) table.getModel());
                 //   break;

            }
        });
        panelsud.add(insertButton);


        JButton deleteButton = new JButton("Supprimer "+type);
        deleteButton.addActionListener(e -> {
            switch (type) {
                case ABONNEMENTS:
                    supprimerAbonnement((DefaultTableModel) table.getModel(), table);
                    break;
            
                default:
                    break;
            }
        });
        panelsud.add(deleteButton);
        panel.add(panelsud, BorderLayout.SOUTH);



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

    private DefaultTableModel createTechnicienTableModel() {
        String[] columns = {"Nom", "Prenom", "Spécialité"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        if (personnes.getPersonnes() != null) {
            for (Personne personne : personnes.getPersonnes()) {
                model.addRow(new Object[]{personne.getIdPersonne(), personne.getNom(), personne.getPrenom()});
            } //à modifier aussi
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

                // Forcer le tri par emplacement ou ID
                List<PlaceDeParking> sortedList = new ArrayList<>(placesDeParkings.getPlaceDeParkings());
                sortedList.sort(Comparator.comparing(PlaceDeParking::getIdPlace)); // Change ici selon le critère voulu

                for (PlaceDeParking place : sortedList) {
                    model.addRow(new Object[]{
                            place.getIdPlace(),
                            place.getEmplacement(),
                            place.getStatutPlace(),
                            place.getTypePlace()
                    });
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

    private void updateLocal(DefaultTableModel model) {
        

        String numLocal = JOptionPane.showInputDialog(this, "Numéro du local à modifier :");
        if (numLocal == null || numLocal.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro du local ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String disponibilite = JOptionPane.showInputDialog(this, "Nouvelle disponibilité (true ou false) :");
        if (disponibilite == null || disponibilite.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La disponibilité ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
