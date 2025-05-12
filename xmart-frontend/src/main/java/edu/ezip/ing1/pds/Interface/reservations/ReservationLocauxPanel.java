package edu.ezip.ing1.pds.Interface.reservations;


import edu.ezip.ing1.pds.Interface.DashboardFrame;
import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.ReservationLocal;
import edu.ezip.ing1.pds.usecase.ReservationLocalUseCase;
import  edu.ezip.ing1.pds.CalendrierPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static edu.ezip.ing1.pds.Formulaires.chargerIcone;

public class ReservationLocauxPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ReservationLocalUseCase reservationLocalUseCase;
    private ReservationLocal selectedReservationLocal;
    private List<ReservationLocal> reservationLocalList;

    public ReservationLocauxPanel(ReservationLocalUseCase reservationLocalUseCase) throws IOException, InterruptedException {
        this.reservationLocalUseCase = reservationLocalUseCase;
        this.reservationLocalList = reservationLocalUseCase.getAllReservationsLocal().getReservationLocaux().stream().toList();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        String[] columns = {"id resa","numero local", "Date début", "Date fin","durée", "type de local"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelSud = new JPanel(new FlowLayout());

        JButton calendrierResa = new JButton("Calendrier", chargerIcone("/icons/calendrier.png", 30, 30));
        calendrierResa.setBackground(Color.CYAN);
        calendrierResa.addActionListener( e -> {
            try {

                CalendrierPanel calendrierPanel = new CalendrierPanel(reservationLocalUseCase);


                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Calendrier des Réservations", true);
                dialog.setContentPane(calendrierPanel);
                dialog.setSize(700, 500);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        panelSud.add(calendrierResa);
        JButton rafraichirButton = new JButton("",chargerIcone("/icons/refresh.png", 30, 30));
        rafraichirButton.setBackground(Color.CYAN);
        rafraichirButton.addActionListener( e -> {
            refreshTable(reservationLocalList);
        });
        panelSud.add(rafraichirButton);

        add(panelSud, BorderLayout.SOUTH);

        refreshTable(reservationLocalList);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem supprimerItem = new JMenuItem("Supprimer");
        supprimerItem.addActionListener(ev -> {
            if (selectedReservationLocal != null) {
                int res = JOptionPane.showConfirmDialog(table, "Supprimer cette reservation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    try {
                        reservationLocalUseCase.deleteReservationLocal(selectedReservationLocal);
                        refreshTable(reservationLocalList);
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(table, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        contextMenu.add(supprimerItem);

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        selectedReservationLocal = reservationLocalList.get(row);
                        contextMenu.show(table, e.getX(), e.getY());

                    }
                }
            }
        });
    }
    void refreshTable(List<ReservationLocal> updatedList) {
        tableModel.setRowCount(0);

        for (ReservationLocal resalocal : updatedList) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime entree = LocalTime.parse(resalocal.getHeureEntree(), formatter);
            LocalTime sortie = LocalTime.parse(resalocal.getHeureSortie(), formatter);

            Duration duree = Duration.between(entree, sortie);

            long heures = duree.toHours();
            long minutes = duree.toMinutes() % 60;

            String dureeFormat = heures + "h " + minutes + "min";
            tableModel.addRow(new Object[]{
                    resalocal.getReservationLocalId(),
                    resalocal.getNumLocal(),
                    resalocal.getDateDebut(),
                    resalocal.getDateFin(),
                    dureeFormat,
                    resalocal.getTypeLocal()

            });
        }
    }
}