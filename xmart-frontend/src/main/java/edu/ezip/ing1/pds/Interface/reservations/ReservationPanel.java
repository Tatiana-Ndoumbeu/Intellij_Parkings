package edu.ezip.ing1.pds.Interface.reservations;

import edu.ezip.ing1.pds.Interface.placesdeparking.FormulaireReservation;
import edu.ezip.ing1.pds.business.dto.ReservationRequest;
import edu.ezip.ing1.pds.usecase.ReservationUseCase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReservationPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ReservationUseCase reservationUseCase;
    private List<ReservationRequest> reservations;

    public ReservationPanel(ReservationUseCase reservationUseCase) {
        this.reservationUseCase = reservationUseCase;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Set up the table
        String[] columns = {"ID", "Nom", "Date début", "Date fin", "ID Place"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add Button
        JButton addBtn = new JButton("Ajouter une réservation");
        styleButton(addBtn, new Color(33, 150, 243));
  /*      addBtn.addActionListener(e -> {
            FormulaireReservation.showForm(
                    this,
                    reservationUseCase,
                    null,
                    this::refreshTable
            );
        });*/

        // Bottom Panel for the button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(addBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Context Menu
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem supprimerItem = new JMenuItem("Supprimer");
        contextMenu.add(supprimerItem);

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        contextMenu.show(table, e.getX(), e.getY());
                        ReservationRequest selected = reservations.get(row);

                        supprimerItem.addActionListener(ev -> {
                            int res = JOptionPane.showConfirmDialog(
                                    table, "Supprimer cette réservation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                            if (res == JOptionPane.YES_OPTION) {
                                // Implement deletion logic here
                                JOptionPane.showMessageDialog(table, "Suppression non encore implémentée.");
                            }
                        });
                    }
                }
            }
        });

        refreshTable(); // Initial table population
    }

    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(250, 40));
    }

    public void refreshTable() {
        try {
            var all = reservationUseCase.getAllReservations();
            reservations = new ArrayList<>();
            all.getReservationRequests().forEach(r -> { reservations.add(r); });
            tableModel.setRowCount(0);
            for (ReservationRequest r : reservations) {
                tableModel.addRow(new Object[]{
                        r.getIdReservation(), r.getDateReservation(), r.getDateEntree(), r.getDateSortie(), r.getPlaceDeParking().getEmplacement()
                });
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des réservations.");
            e.printStackTrace();
        }
    }
}
