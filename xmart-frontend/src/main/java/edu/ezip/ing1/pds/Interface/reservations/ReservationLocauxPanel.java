package edu.ezip.ing1.pds.Interface.reservations;


import edu.ezip.ing1.pds.usecase.ReservationLocalUseCase;
import  edu.ezip.ing1.pds.CalendrierPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static edu.ezip.ing1.pds.Formulaires.chargerIcone;

public class ReservationLocauxPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ReservationLocalUseCase reservationLocalUseCase;

    public ReservationLocauxPanel(ReservationLocalUseCase reservationLocalUseCase) {
        this.reservationLocalUseCase = reservationLocalUseCase;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        String[] columns = {"numero local", "Date début", "Date fin", "type de local"};
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

        JButton reserverButton = new JButton("reserver un Local service",chargerIcone("/icons/reserver.png", 30, 30));
        reserverButton.setBackground(Color.CYAN);
        //reserverButton.addActionListener( );
        panelSud.add(reserverButton);

        JButton calendrierResa = new JButton("Calendrier", chargerIcone("/icons/calendrier.png", 30, 30));
        calendrierResa.setBackground(Color.CYAN);
        calendrierResa.addActionListener( e -> {
            CalendrierPanel calendrierPanel = new CalendrierPanel(reservationLocalUseCase);

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Calendrier des Réservations", true);
            dialog.setContentPane(calendrierPanel);
            dialog.setSize(700, 500);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        panelSud.add(calendrierResa);

        add(panelSud, BorderLayout.SOUTH);
    }
}