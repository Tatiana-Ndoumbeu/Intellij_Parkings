package edu.ezip.ing1.pds.Interface;

import edu.ezip.ing1.pds.Interface.Archives.ArchivesPanel;
import edu.ezip.ing1.pds.Interface.Mecanicien.MecanicienPanel;
import edu.ezip.ing1.pds.Interface.abonnement.AbonnementPanel;
import edu.ezip.ing1.pds.Interface.localTechnique.LocalTechniquePanel;
import edu.ezip.ing1.pds.Interface.locallaverie.LocalLaveriePanel;
import edu.ezip.ing1.pds.Interface.placesdeparking.PlaceDeParkingPanel;
import edu.ezip.ing1.pds.Interface.reservations.ReservationLocauxPanel;
import edu.ezip.ing1.pds.Interface.reservations.ReservationPanel;
import edu.ezip.ing1.pds.Interface.personne.PersonnePanel;
import edu.ezip.ing1.pds.api.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.*;
import edu.ezip.ing1.pds.usecase.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardFrame extends JFrame {

    // Composants graphiques
    private JPanel sideMenu;
    private JPanel mainContent;
    private final JDialog loadingDialog = new JDialog(this, "Chargement...", true); // Loader modal

    // Use cases métier
    private PlaceDeParkingUseCase placeDeParkingUseCase;
    private ReservationUseCase reservationUseCase;
    private AbonnementUseCase abonnementUseCase;
    private PersonneUseCase personneUseCase;
    private LocalLaverieUseCase localLaverieUseCase;
    private LocalTechniqueUseCase localTechniqueUseCase;
    private MecanicienUseCase mecanicienUseCase;
    private ReservationLocalUseCase reservationLocalUseCase;
    private ArchivesUseCase archivesUseCase;

    // Données partagées
    private List<String> ClientsEnAttente;

    public DashboardFrame(
            PlaceDeParkingUseCase placeUseCase,
            ReservationUseCase reservUseCase,
            AbonnementUseCase abonUseCase,
            PersonneUseCase persoUseCase,
            LocalLaverieUseCase LLUsecase,
            LocalTechniqueUseCase LLUseCase,
            MecanicienUseCase MecaUseCase,
            ReservationLocalUseCase reservationLocalUseCase,
            ArchivesUseCase archivesUseCase,
            List<String> clients
    ) {
        setTitle("Tableau de bord - Gestion Parking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialisation des services métier
        this.placeDeParkingUseCase = placeUseCase;
        this.reservationUseCase = reservUseCase;
        this.reservationLocalUseCase = reservationLocalUseCase;
        this.abonnementUseCase = abonUseCase;
        this.personneUseCase = persoUseCase;
        this.localLaverieUseCase = LLUsecase;
        this.localTechniqueUseCase = LLUseCase;
        this.mecanicienUseCase = MecaUseCase;
        this.archivesUseCase = archivesUseCase;
        this.ClientsEnAttente = clients;

        // Interface
        initSideMenu();      // Menu latéral
        initMainContent();   // Zone centrale
        initLoadingDialog(); // Prépare la fenêtre de chargement

        add(sideMenu, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
        setVisible(true);
    }

    // Prépare la boîte de dialogue de chargement
    private void initLoadingDialog() {
        JLabel label = new JLabel("Chargement en cours...", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        loadingDialog.getContentPane().add(label);
        loadingDialog.setSize(250, 100);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    // Affiche un panneau avec un loader pendant le chargement
    private void loadPanelAsync(Runnable panelLoader) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
                panelLoader.run();
                return null;
            }
            @Override
            protected void done() {
                loadingDialog.setVisible(false);
            }
        };
        worker.execute();
    }

    // Méthodes de navigation
    private void showPlacesDeParking() {
        loadPanelAsync(() -> {
            mainContent.removeAll();
            mainContent.add(new PlaceDeParkingPanel(placeDeParkingUseCase, personneUseCase, reservationUseCase, this), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });
    }

    private void showReservations() {
        loadPanelAsync(() -> {
            mainContent.removeAll();
            mainContent.add(new ReservationPanel(reservationUseCase), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });
    }

    private void showReservationsLocaux() {
        loadPanelAsync(() -> {
            try {
                mainContent.removeAll();
                mainContent.add(new ReservationLocauxPanel(reservationLocalUseCase), BorderLayout.CENTER);
                mainContent.revalidate();
                mainContent.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showAbonnements() {
        loadPanelAsync(() -> {
            try {
                mainContent.removeAll();
                mainContent.add(new AbonnementPanel(abonnementUseCase, personneUseCase), BorderLayout.CENTER);
                mainContent.revalidate();
                mainContent.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showPersonnes() {
        loadPanelAsync(() -> {
            try {
                mainContent.removeAll();
                mainContent.add(new PersonnePanel(personneUseCase, abonnementUseCase), BorderLayout.CENTER);
                mainContent.revalidate();
                mainContent.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showLocalLaverie() {
        loadPanelAsync(() -> {
            try {
                mainContent.removeAll();
                mainContent.add(new LocalLaveriePanel(localLaverieUseCase, personneUseCase, abonnementUseCase, archivesUseCase, ClientsEnAttente), BorderLayout.CENTER);
                mainContent.revalidate();
                mainContent.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showLocalTechnique() {
        loadPanelAsync(() -> {
            try {
                mainContent.removeAll();
                mainContent.add(new LocalTechniquePanel(localTechniqueUseCase, mecanicienUseCase), BorderLayout.CENTER);
                mainContent.revalidate();
                mainContent.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showMecaniciens() {
        loadPanelAsync(() -> {
            try {
                mainContent.removeAll();
                mainContent.add(new MecanicienPanel(mecanicienUseCase), BorderLayout.CENTER);
                mainContent.revalidate();
                mainContent.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showArchives() {
        loadPanelAsync(() -> {
            try {
                mainContent.removeAll();
                mainContent.add(new ArchivesPanel(archivesUseCase), BorderLayout.CENTER);
                mainContent.revalidate();
                mainContent.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showAccueuil() {
        mainContent.removeAll();
        mainContent.add(new PanelAccueuil(), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    // Initialise la zone principale
    private void initMainContent() {
        mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        showAccueuil();
    }

    // Initialise le menu latéral
    private void initSideMenu() {
        sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBackground(new Color(50, 63, 75));
        sideMenu.setPreferredSize(new Dimension(200, getHeight()));

        // Titre
        JLabel title = new JLabel("Tableau de bord");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sideMenu.add(title);

        // Boutons du menu
        addMenuButton("Accueil", e -> showAccueuil());
        addMenuButton("Places de parking", e -> showPlacesDeParking());
        addMenuButton("Réservations", e -> showReservations());
        addMenuButton("Réservations locaux", e -> showReservationsLocaux());
        addMenuButton("Abonnements", e -> showAbonnements());
        addMenuButton("Personnes", e -> showPersonnes());
        addMenuButton("Laverie", e -> showLocalLaverie());
        addMenuButton("Technique", e -> showLocalTechnique());
        addMenuButton("Mécaniciens", e -> showMecaniciens());
        addMenuButton("Archives", e -> showArchives());
        addMenuButton("Quitter", e -> System.exit(0));
    }

    private void addMenuButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 83, 96));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addActionListener(listener);
        sideMenu.add(Box.createVerticalStrut(10));
        sideMenu.add(button);
    }



    public static void main(String[] args) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, "network.yaml");
        PlaceDeParkingRepository repository = new PlaceDeParkingService(networkConfig);
        ReservationRepository reservationRepository = new ReservationService(networkConfig);
        AbonnementRepository abonnementRepository = new AbonnementService(networkConfig);
        PersonneRepository personneRepository = new PersonneService(networkConfig);
        LocalLaverieRepository localLaverieRepository = new LocalLaveriesService(networkConfig);
        LocalTechniqueRepository localTechniqueRepository = new LocalTechniqueService(networkConfig);
        MecanicienRepository mecanicienRepository = new MecanicienService(networkConfig);
        ReservationLocalRepository reservationLocalRepository = new ReservationLocalService(networkConfig);
        ArchivesRepository archivesRepository = new ArchivesPaiementService(networkConfig);

        PlaceDeParkingUseCase placeUseCase = new PlaceDeParkingUseCase(repository);
        ReservationUseCase reservUseCase = new ReservationUseCase(reservationRepository);
        AbonnementUseCase abonUseCase = new AbonnementUseCase(abonnementRepository);
        PersonneUseCase persoUseCase = new PersonneUseCase(personneRepository);
        LocalLaverieUseCase LLUsecase = new LocalLaverieUseCase(localLaverieRepository);
        LocalTechniqueUseCase LTUsecase = new LocalTechniqueUseCase(localTechniqueRepository);
        MecanicienUseCase MecaUseCase = new MecanicienUseCase(mecanicienRepository);
        ReservationLocalUseCase ResaLocalUseCase = new ReservationLocalUseCase(reservationLocalRepository);
        ArchivesUseCase ArchivesPaiementUseCase = new ArchivesUseCase(archivesRepository);

        List<String> clientsEnAttente = new ArrayList<>(Arrays.asList("", "", "", ""));

        SwingUtilities.invokeLater(() -> new DashboardFrame(
                placeUseCase,
                reservUseCase,
                abonUseCase,
                persoUseCase,
                LLUsecase,
                LTUsecase,
                MecaUseCase,
                ResaLocalUseCase,
                ArchivesPaiementUseCase,
                clientsEnAttente
        ));
    }





}
