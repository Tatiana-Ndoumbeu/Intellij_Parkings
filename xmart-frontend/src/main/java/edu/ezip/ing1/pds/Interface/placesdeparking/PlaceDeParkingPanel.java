package edu.ezip.ing1.pds.Interface.placesdeparking;


import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;
import edu.ezip.ing1.pds.usecase.PlaceDeParkingUseCase;
import edu.ezip.ing1.pds.usecase.ReservationUseCase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PlaceDeParkingPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<PlaceDeParking> places;

    public PlaceDeParkingPanel(PlaceDeParkingUseCase placeDeParkingUseCase, PersonneUseCase personneUseCase,  ReservationUseCase reservationUseCase,JFrame parent) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        this.places = placeDeParkingUseCase.getAllPlacesDeParking();

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
        addBtn.addActionListener(e -> new AjouterPlaceFrame(null, placeDeParkingUseCase));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(addBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable(placeDeParkingUseCase.getAllPlacesDeParking());

        // Menu contextuel
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem modifierItem = new JMenuItem("Modifier");
        JMenuItem supprimerItem = new JMenuItem("Supprimer");
        JMenuItem reserverItem = new JMenuItem("Réserver");
        JMenuItem affecterItem = new JMenuItem("Affecter à un véhicule");

        contextMenu.add(modifierItem);
        contextMenu.add(supprimerItem);
        contextMenu.add(reserverItem);
        contextMenu.add(affecterItem);

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        contextMenu.show(table, e.getX(), e.getY());

                        PlaceDeParking selected = places.get(row);

                        modifierItem.addActionListener(ev -> {
                            new ModifierPlaceFrame(selected, updatedList -> {
                                places = updatedList;
                                refreshTable(places);
                            }, placeDeParkingUseCase);
                        });

                        supprimerItem.addActionListener(ev -> {
                            int res = JOptionPane.showConfirmDialog(table, "Supprimer cette place ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                            if (res == JOptionPane.YES_OPTION) {
                                placeDeParkingUseCase.deletePlaceDeParking(selected.getIdPlace());
                                places.remove(selected);
                                refreshTable(places);
                            }
                        });

                        reserverItem.addActionListener(ev -> {
                            FormulaireReservation.showForm(
                                    parent,              // JFrame parent
                                    reservationUseCase,                        // Use case pour les réservations
                                    selected,                                  // Place sélectionnée
                                    () -> refreshTable(placeDeParkingUseCase.getAllPlacesDeParking()), // Callback de rafraîchissement
                                    personneUseCase                            // Use case pour récupérer la liste des personnes
                            );
                        });



                        affecterItem.addActionListener(ev -> {
                            JOptionPane.showMessageDialog(table, "Affectation à un véhicule pour " + selected.getIdPlace());
                        });
                    }
                }
            }
        });
    }

    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    private void refreshTable(List<PlaceDeParking> updatedList) {
        tableModel.setRowCount(0);

        if (updatedList == null) {
            JOptionPane.showMessageDialog(this,
                    "La liste des places de parking est vide ou n'a pas pu être chargée.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (PlaceDeParking p : updatedList) {
            tableModel.addRow(new Object[]{p.getIdPlace(), p.getTypePlace(), p.getStatutPlace(), p.getEmplacement()});
        }
    }

}

