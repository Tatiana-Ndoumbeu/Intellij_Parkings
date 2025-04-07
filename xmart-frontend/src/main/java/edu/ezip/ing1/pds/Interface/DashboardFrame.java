package edu.ezip.ing1.pds.Interface;

import edu.ezip.ing1.pds.Interface.placesdeparking.PlaceDeParkingPanel;
import edu.ezip.ing1.pds.Interface.reservations.ReservationPanel;
import edu.ezip.ing1.pds.api.PlaceDeParkingRepository;
import edu.ezip.ing1.pds.api.ReservationRepository;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.PlaceDeParkingService;
import edu.ezip.ing1.pds.services.ReservationService;
import edu.ezip.ing1.pds.usecase.PlaceDeParkingUseCase;
import edu.ezip.ing1.pds.usecase.ReservationUseCase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DashboardFrame extends JFrame {
    private JPanel sideMenu;
    private JPanel mainContent;
    private PlaceDeParkingUseCase placeDeParkingUseCase;
    private ReservationUseCase reservationUseCase;
    public DashboardFrame(PlaceDeParkingUseCase placeUseCase, ReservationUseCase reservUseCase) {
        setTitle("Tableau de bord - Gestion Parking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        this.placeDeParkingUseCase = placeUseCase;
        this.reservationUseCase = reservUseCase;

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

        addMenuButton("Accueil", e -> showContent("Accueil"));
        addMenuButton("Places de parking", e ->  showPlacesDeParking());
        addMenuButton("Réservations", e -> showReservations());
        addMenuButton("Affectations", e -> showContent("Affectations"));
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
    private void showContent(String section) {
        mainContent.removeAll();

        JLabel label = new JLabel("Section : " + section, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));

        mainContent.add(label, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showReservations() {
        mainContent.removeAll();
        mainContent.add(new ReservationPanel(reservationUseCase), BorderLayout.CENTER);  // Add ReservationPanel
        mainContent.revalidate();
        mainContent.repaint();
    }

    public static void main(String[] args) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, "network.yaml");
        PlaceDeParkingRepository repository = new PlaceDeParkingService(networkConfig);
        ReservationRepository reservationRepository = new ReservationService(networkConfig);
        PlaceDeParkingUseCase placeUseCase = new PlaceDeParkingUseCase(repository);
        ReservationUseCase reservUseCase = new ReservationUseCase(reservationRepository);

        SwingUtilities.invokeLater(() -> new DashboardFrame(placeUseCase, reservUseCase));
    }
}

