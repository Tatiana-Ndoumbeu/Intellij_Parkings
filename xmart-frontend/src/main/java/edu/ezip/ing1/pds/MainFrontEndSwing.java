package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.*;
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

import static edu.ezip.ing1.pds.uiUtils.AbonnementViewModel.*;
import static edu.ezip.ing1.pds.uiUtils.LocalTechniqueViewModel.*;
import static edu.ezip.ing1.pds.uiUtils.LocalViewModel.*;
import static edu.ezip.ing1.pds.uiUtils.MecanicienViewModel.createMecanicienTableModel;
import static edu.ezip.ing1.pds.uiUtils.MecanicienViewModel.createReservationTableModel;
import static edu.ezip.ing1.pds.uiUtils.PersonneViewModel.*;
import static edu.ezip.ing1.pds.uiUtils.PlaceDeParkingViewModel.*;
import static edu.ezip.ing1.pds.uiUtils.VehiculeViewMdel.insertVehicle;

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
    public final static String networkConfigFile = "network.yaml";

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





    private JPanel createTablePanelLocaux(String LOCAL)
    {
        JPanel panelsud = new JPanel(new FlowLayout());
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();

        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        switch (LOCAL) {
            case LOCAL_LAVERIES:
                table.setModel(createLocalLaverieTableModel(networkConfig,localLaveries,logger));
                break;
            case LOCAL_TECHNIQUE:
                table.setModel(createLocalTechniqueTableModel(networkConfig,localTechniques,logger));
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
                    insertLocal((DefaultTableModel) table.getModel(),this,this::createTablePanel,LOCAL_LAVERIES,logger);
                    break;
                case LOCAL_TECHNIQUE:
                    insertLocalT((DefaultTableModel) table.getModel(),this,this::createTablePanel,LOCAL_TECHNIQUE,logger);
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
                     updateLocal((DefaultTableModel) table.getModel(),this
                     ,localLaveries,logger);
                     break;
                 case LOCAL_TECHNIQUE:
                     updateLocalT((DefaultTableModel) table.getModel(),this,localTechniques,logger);
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
                   deleteLocal((DefaultTableModel) table.getModel(),this,localLaveries,logger);
                    break;
                case LOCAL_TECHNIQUE:
                    deleteLocalT((DefaultTableModel) table.getModel(),this,localTechniques,logger);
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
                table.setModel(createAbonnementTableModel(networkConfig,abonnements,logger));
                break;
            case PERSONNES:
                table.setModel(createPersonneTableModel(new Personnes()));
                break;
            case PLACES_DE_PARKING:
                table.setModel(createPlaceDeParkingTableModel(networkConfig,placesDeParkings,logger));
                break;
            case VEHICLES:
                table.setModel(createVehicleTableModel(new Vehicles()));
                break;
            case MECANICIEN:
                table.setModel(createMecanicienTableModel(networkConfig,mecaniciens,logger));
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
                    insertAbonnements((DefaultTableModel) table.getModel(),this,logger);
                    break;
                case PERSONNES:
                    insertPersonne((DefaultTableModel) table.getModel(),personnes,this);
                    break;
                case PLACES_DE_PARKING:
                    insertPlaceDeParking((DefaultTableModel) table.getModel(),this,networkConfigFile,logger);
                    break;
                case VEHICLES:
                    insertVehicle((DefaultTableModel) table.getModel(),vehicles,this);
                    break;
                case MECANICIEN:
                    //insertMecanicien((DefaultTableModel) table.getModel());
                 //   MecanicienViewModel.insertMecanicien( (DefaultTableModel) table.getModel(), networkConfigFile,this, logger);
                    break;

            }
        });
        panelsud.add(insertButton);


        JButton deleteButton = new JButton("Supprimer "+type, chargerIcone("/icons/supprimer.png", 30, 30));
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(e -> {
            switch (type) {
                case ABONNEMENTS:
                    supprimerAbonnement((DefaultTableModel) table.getModel(), table,this,logger);
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
                    updateAbonnement((DefaultTableModel) table.getModel(),this,logger);
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
