package edu.ezip.ing1.pds.Interface;

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
    private JPanel sideMenu;
    private JPanel mainContent;
    private PlaceDeParkingUseCase placeDeParkingUseCase;
    private ReservationUseCase reservationUseCase;
    private AbonnementUseCase abonnementUseCase;
    private PersonneUseCase personneUseCase;
    private LocalLaverieUseCase localLaverieUseCase;
    private LocalTechniqueUseCase localTechniqueUseCase;
    private MecanicienUseCase mecanicienUseCase;
    private ReservationLocalUseCase reservationLocalUseCase;


    public DashboardFrame(PlaceDeParkingUseCase placeUseCase, ReservationUseCase reservUseCase, AbonnementUseCase abonUseCase, PersonneUseCase persoUseCase, LocalLaverieUseCase LLUsecase, LocalTechniqueUseCase LLUseCase, MecanicienUseCase MecaUseCase, ReservationLocalUseCase reservationLocalUseCase) {
        setTitle("Tableau de bord - Gestion Parking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        this.placeDeParkingUseCase = placeUseCase;
        this.reservationUseCase = reservUseCase;
        this.reservationLocalUseCase = reservationLocalUseCase;
        this.abonnementUseCase = abonUseCase;
        this.personneUseCase = persoUseCase;
        this.localLaverieUseCase = LLUsecase;
        this.localTechniqueUseCase = LLUseCase;
        this.mecanicienUseCase = MecaUseCase;
        initSideMenu();
        initMainContent();

        add(sideMenu, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);

        setVisible(true);
    }

    private void initSideMenu() {
        sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBackground(new Color(50, 63, 75));
        sideMenu.setPreferredSize(new Dimension(200, getHeight()));

        JLabel title = new JLabel("Tableau de bord");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        sideMenu.add(title);

        // Menu items
        addMenuButton("Accueil", e ->showAccueuil());
       // addMenuButton("Accueil", e -> showContent("Accueil"));
        addMenuButton("Places de parking", e -> showPlacesDeParking());

        JButton ReservationButton = boutonderoulant("Réservations");
        JPopupMenu reservationMenu = new JPopupMenu();
        reservationMenu.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        reservationMenu.setBackground(new Color(60, 73, 85));

        JMenuItem parkingReservationItem = new JMenuItem("Places de parking");
        parkingReservationItem.addActionListener(e -> showReservations());

        JMenuItem localReservationItem = new JMenuItem("Locaux services");
        localReservationItem.addActionListener(e ->{
        try {
            showReservationsLocaux();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);}
        catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
        });

        ReservationButton.addActionListener(e -> reservationMenu.show(ReservationButton, 0, ReservationButton.getHeight()));
        reservationMenu.add(parkingReservationItem);
        reservationMenu.add(localReservationItem);
        sideMenu.add(Box.createVerticalStrut(10));
        sideMenu.add(ReservationButton);

        addMenuButton("Abonnements", e -> {
            try {
                showAbonnements();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });  // New button for abonnements

        addMenuButton("Personnes", e -> {
            try{
                showPersonnes();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });


        JButton servicesButton = boutonderoulant("Services");

        JPopupMenu servicesMenu = new JPopupMenu();
        servicesMenu.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        servicesMenu.setBackground(new Color(60, 73, 85));

        JMenuItem laverieItem = new JMenuItem("Laverie");
        laverieItem.addActionListener(e -> {  try {
            showLocalLaverie();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        });

        JMenuItem techniqueItem = new JMenuItem("Technique");
        techniqueItem.addActionListener(e -> { try {
            showLocalTechnique();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        });

        JMenuItem mecanicienItem = new JMenuItem("Mécaniciens");
        mecanicienItem.addActionListener(e -> { try {
            showMecaniciens();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }


        });

        servicesMenu.add(laverieItem);
        servicesMenu.add(techniqueItem);
        servicesMenu.add(mecanicienItem);

        servicesButton.addActionListener(e -> servicesMenu.show(servicesButton, 0, servicesButton.getHeight()));

        sideMenu.add(Box.createVerticalStrut(10));
        sideMenu.add(servicesButton);
        addMenuButton("Gestion des entités", e -> showEntityManagement());
        addMenuButton("Déconnexion", e -> System.exit(0));
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

    private void initMainContent() {
        mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        showAccueuil();
    }

    private void showPlacesDeParking() {
        mainContent.removeAll();
        mainContent.add(new PlaceDeParkingPanel(placeDeParkingUseCase, reservationUseCase), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showReservations() {
        mainContent.removeAll();
        mainContent.add(new ReservationPanel(reservationUseCase), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }
    private void showReservationsLocaux() throws IOException, InterruptedException  {
        mainContent.removeAll();
        mainContent.add(new ReservationLocauxPanel(reservationLocalUseCase), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showAbonnements() throws IOException, InterruptedException {
        mainContent.removeAll();
        mainContent.add(new AbonnementPanel(abonnementUseCase), BorderLayout.CENTER);  // Add AbonnementPanel
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showPersonnes() throws IOException, InterruptedException {
        mainContent.removeAll();
        mainContent.add(new PersonnePanel(personneUseCase), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showLocalLaverie() throws IOException, InterruptedException {
        mainContent.removeAll();
        mainContent.add(new LocalLaveriePanel(localLaverieUseCase, personneUseCase, abonnementUseCase), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }
    private void showLocalTechnique() throws IOException, InterruptedException {
        mainContent.removeAll();
        mainContent.add(new LocalTechniquePanel(localTechniqueUseCase, mecanicienUseCase), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }
    private void showMecaniciens() throws IOException, InterruptedException {
        mainContent.removeAll();
        mainContent.add(new MecanicienPanel(mecanicienUseCase), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showContent(String section) {
        mainContent.removeAll();

        JLabel label = new JLabel("Section : " + section, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));

        mainContent.add(label, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showAccueuil() {
        mainContent.removeAll();

       mainContent.add(new PanelAccueuil(), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showEntityManagement() {
        mainContent.removeAll();
        JLabel label = new JLabel("Gestion des entités", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainContent.add(label, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }
    private JButton boutonderoulant(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 83, 96));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }


    public static void main(String[] args) {
        showDasbord();
    }

    private static void showDasbord() {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, "network.yaml");
        PlaceDeParkingRepository repository = new PlaceDeParkingService(networkConfig);
        ReservationRepository reservationRepository = new ReservationService(networkConfig);
        AbonnementRepository abonnementRepository = new AbonnementService(networkConfig);
        PersonneRepository personneRepository = new PersonneService(networkConfig);
        LocalLaverieRepository localLaverieRepository = new LocalLaveriesService(networkConfig);
        LocalTechniqueRepository localTechniqueRepository = new LocalTechniqueService(networkConfig);
        MecanicienRepository mecanicienRepository = new MecanicienService(networkConfig);
        ReservationLocalRepository reservationLocalRepository = new ReservationLocalService(networkConfig);

        PlaceDeParkingUseCase placeUseCase = new PlaceDeParkingUseCase(repository);
        ReservationUseCase reservUseCase = new ReservationUseCase(reservationRepository);
        AbonnementUseCase abonUseCase = new AbonnementUseCase(abonnementRepository);
        PersonneUseCase persoUseCase = new PersonneUseCase(personneRepository);
        LocalLaverieUseCase LLUsecase = new LocalLaverieUseCase(localLaverieRepository);
        LocalTechniqueUseCase LTUsecase = new LocalTechniqueUseCase(localTechniqueRepository);
        MecanicienUseCase MecaUseCase = new MecanicienUseCase(mecanicienRepository);
        ReservationLocalUseCase ResaLocalUseCase = new ReservationLocalUseCase(reservationLocalRepository);

        SwingUtilities.invokeLater(() -> new DashboardFrame(placeUseCase, reservUseCase, abonUseCase, persoUseCase, LLUsecase, LTUsecase, MecaUseCase, ResaLocalUseCase));
    }
}
