package edu.ezip.ing1.pds.Interface;

import edu.ezip.ing1.pds.Interface.Mecanicien.MecanicienPanel;
import edu.ezip.ing1.pds.Interface.abonnement.AbonnementPanel;
import edu.ezip.ing1.pds.Interface.localTechnique.LocalTechniquePanel;
import edu.ezip.ing1.pds.Interface.locallaverie.LocalLaveriePanel;
import edu.ezip.ing1.pds.Interface.placesdeparking.PlaceDeParkingPanel;
import edu.ezip.ing1.pds.Interface.reservations.ReservationPanel;
import edu.ezip.ing1.pds.api.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.*;
import edu.ezip.ing1.pds.usecase.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DashboardFrame extends JFrame {
    private JPanel sideMenu;
    private JPanel mainContent;
    private PlaceDeParkingUseCase placeDeParkingUseCase;
    private ReservationUseCase reservationUseCase;
    private AbonnementUseCase abonnementUseCase;
    private LocalLaverieUseCase localLaverieUseCase;
    private LocalTechniqueUseCase localTechniqueUseCase;
    private MecanicienUseCase mecanicienUseCase;

    public DashboardFrame(PlaceDeParkingUseCase placeUseCase, ReservationUseCase reservUseCase, AbonnementUseCase abonUseCase, LocalLaverieUseCase LLUsecase, LocalTechniqueUseCase LLUseCase, MecanicienUseCase MecaUseCase) {
        setTitle("Tableau de bord - Gestion Parking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        this.placeDeParkingUseCase = placeUseCase;
        this.reservationUseCase = reservUseCase;
        this.abonnementUseCase = abonUseCase;
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
        addMenuButton("Accueil", e -> showContent("Accueil"));
        addMenuButton("Places de parking", e -> showPlacesDeParking());
        addMenuButton("Réservations", e -> showReservations());
        addMenuButton("Abonnements", e -> {
            try {
                showAbonnements();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });  // New button for abonnements


        JButton servicesButton = new JButton("Services");
        servicesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        servicesButton.setMaximumSize(new Dimension(180, 40));
        servicesButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        servicesButton.setFocusPainted(false);
        servicesButton.setBackground(new Color(70, 83, 96));
        servicesButton.setForeground(Color.WHITE);
        servicesButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

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

// Affichage du menu au clic sur le bouton
        servicesButton.addActionListener(e -> servicesMenu.show(servicesButton, 0, servicesButton.getHeight()));

// Ajout au panneau
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
        showContent("Accueil");
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

    private void showAbonnements() throws IOException, InterruptedException {
        mainContent.removeAll();
        mainContent.add(new AbonnementPanel(abonnementUseCase), BorderLayout.CENTER);  // Add AbonnementPanel
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showLocalLaverie() throws IOException, InterruptedException {
        mainContent.removeAll();
        mainContent.add(new LocalLaveriePanel(localLaverieUseCase), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }
    private void showLocalTechnique() throws IOException, InterruptedException {
        mainContent.removeAll();
        mainContent.add(new LocalTechniquePanel(localTechniqueUseCase), BorderLayout.CENTER);
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

    private void showEntityManagement() {
        // Add your entity management logic here
        mainContent.removeAll();
        JLabel label = new JLabel("Gestion des entités", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainContent.add(label, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    public static void main(String[] args) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, "network.yaml");
        PlaceDeParkingRepository repository = new PlaceDeParkingService(networkConfig);
        ReservationRepository reservationRepository = new ReservationService(networkConfig);
        AbonnementRepository abonnementRepository = new AbonnementService(networkConfig);
        LocalLaverieRepository localLaverieRepository = new LocalLaveriesService(networkConfig);
        LocalTechniqueRepository localTechniqueRepository = new LocalTechniqueService(networkConfig);
        MecanicienRepository mecanicienRepository = new MecanicienService(networkConfig);

        PlaceDeParkingUseCase placeUseCase = new PlaceDeParkingUseCase(repository);
        ReservationUseCase reservUseCase = new ReservationUseCase(reservationRepository);
        AbonnementUseCase abonUseCase = new AbonnementUseCase(abonnementRepository);
        LocalLaverieUseCase LLUsecase = new LocalLaverieUseCase(localLaverieRepository);
        LocalTechniqueUseCase LTUsecase = new LocalTechniqueUseCase(localTechniqueRepository);
        MecanicienUseCase MecaUseCase = new MecanicienUseCase(mecanicienRepository);

        SwingUtilities.invokeLater(() -> new DashboardFrame(placeUseCase, reservUseCase, abonUseCase, LLUsecase, LTUsecase, MecaUseCase));
    }
}
