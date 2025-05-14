package edu.ezip.ing1.pds.Interface.placesdeparking;

import edu.ezip.ing1.pds.api.PersonneRepository;
import edu.ezip.ing1.pds.api.PlaceDeParkingRepository;
import edu.ezip.ing1.pds.api.ReservationRepository;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.PersonneService;
import edu.ezip.ing1.pds.services.PlaceDeParkingService;
import edu.ezip.ing1.pds.services.ReservationService;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;
import edu.ezip.ing1.pds.usecase.PlaceDeParkingUseCase;
import edu.ezip.ing1.pds.usecase.ReservationUseCase;


public class PlaceDeParkingListFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private PlaceDeParkingUseCase placeDeParkingUseCase; // The use case
    private List<PlaceDeParking> places;
    private final static String networkConfigFile = "network.yaml";

    public PlaceDeParkingListFrame(PlaceDeParkingUseCase placeDeParkingUseCase,PersonneUseCase personneUseCase, ReservationUseCase  reservationUseCase) {
        this.placeDeParkingUseCase = placeDeParkingUseCase;
        this.places = placeDeParkingUseCase.getAllPlacesDeParking(); // Load data from use case

        setTitle("Gestion des places de parking");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));

        String[] columns = {"ID", "Type", "Statut", "Emplacement"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton addBtn = new JButton("Ajouter une place");
        styleButton(addBtn, new Color(255, 152, 0));
        addBtn.addActionListener(e -> new AjouterPlaceFrame(this,placeDeParkingUseCase));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(addBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable(places);

        // Create Context Menu
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem modifierItem = new JMenuItem("Modifier");
        JMenuItem supprimerItem = new JMenuItem("Supprimer");
        JMenuItem reserverItem = new JMenuItem("Réserver");
        JMenuItem affecterItem = new JMenuItem("Affecter à un véhicule");

        contextMenu.add(modifierItem);
        contextMenu.add(supprimerItem);
        contextMenu.add(reserverItem);
        contextMenu.add(affecterItem);

        // Add MouseListener for right-click context menu
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        contextMenu.show(table, e.getX(), e.getY());

                        PlaceDeParking selected = places.get(row);

                        // Modifier
                        modifierItem.addActionListener(ev -> {
                            new ModifierPlaceFrame(selected, updatedList -> {
                                places = updatedList;
                                refreshTable(places);
                            }, placeDeParkingUseCase);
                        });

                        // Supprimer
                        supprimerItem.addActionListener(ev -> {
                            int res = JOptionPane.showConfirmDialog(table, "Supprimer cette place ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                            if (res == JOptionPane.YES_OPTION) {
                                placeDeParkingUseCase.deletePlaceDeParking(selected.getIdPlace()); // Call delete from use case
                                places.remove(selected);  // Remove from local list
                                refreshTable(places);  // Refresh the table
                            }
                        });

                        // Réserver
                        reserverItem.addActionListener(ev -> {
                            FormulaireReservation.showForm(
                                    PlaceDeParkingListFrame.this,              // JFrame parent
                                    reservationUseCase,                        // Use case pour les réservations
                                    selected,                                  // Place sélectionnée
                                    () -> refreshTable(placeDeParkingUseCase.getAllPlacesDeParking()),
                                    personneUseCase// Callback de rafraîchissement
                                                                // Use case pour récupérer la liste des personnes
                            );
                        });

                        // Affecter
                        affecterItem.addActionListener(ev -> {
                            JOptionPane.showMessageDialog(table, "Affectation à un véhicule pour " + selected.getIdPlace());
                        });
                    }
                }
            }
        });

        setVisible(true);
    }


    // Style for buttons
    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    public void refreshTable(List<PlaceDeParking> updatedList) {
        tableModel.setRowCount(0);  // Clear existing rows
        for (PlaceDeParking p : updatedList) {
            tableModel.addRow(new Object[]{p.getIdPlace(), p.getTypePlace(), p.getStatutPlace(), p.getEmplacement()});
        }
    }


    // Main method to test with a use case
    public static void main(String[] args) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        // Assuming PlaceDeParkingUseCase is your business layer
        PlaceDeParkingRepository repository = new PlaceDeParkingService(networkConfig);  // Replace with your actual repository implementation
        PersonneRepository personneRepository = new PersonneService(networkConfig); // Replace with your actual repository implementation
        ReservationRepository reservationRepository = new ReservationService(networkConfig);  // Replace with your actual repository implementation
        PlaceDeParkingUseCase placeDeParkingUseCase = new PlaceDeParkingUseCase(repository);
        ReservationUseCase reservationUseCase = new ReservationUseCase(reservationRepository);
        PersonneUseCase personneUseCase = new PersonneUseCase(personneRepository);

        SwingUtilities.invokeLater(() -> new PlaceDeParkingListFrame(placeDeParkingUseCase,personneUseCase,reservationUseCase));
    }
}
