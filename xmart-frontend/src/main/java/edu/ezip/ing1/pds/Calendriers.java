package edu.ezip.ing1.pds;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calendriers extends JPanel {
    private JTable table;
    private DefaultTableModel caltableModel;
    private int annee;
    private int mois;
    private Map<LocalDate, List<String>> reservations;


    public Calendriers(Map<LocalDate, List<String>> reservations) {
        this.reservations = reservations;
        setLayout(new BorderLayout());

        annee = LocalDate.now().getYear();
        mois = LocalDate.now().getMonthValue();


        JPanel panelNord = new JPanel(new FlowLayout());
        JButton boutonPrecedent = new JButton("Mois précédent");
        JButton boutonSuivant = new JButton("Mois suivant");

        boutonPrecedent.addActionListener(e -> changerMois(-1));
        boutonSuivant.addActionListener(e -> changerMois(1));

        panelNord.add(boutonPrecedent);
        panelNord.add(boutonSuivant);
        add(panelNord, BorderLayout.NORTH);

        String[] joursSemaine = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        caltableModel = new DefaultTableModel(null, joursSemaine);
        table = new JTable(caltableModel);
        table.setDefaultRenderer(Object.class, new CalendrierRenderer(reservations));

        remplirCalendrier(annee, mois);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void remplirCalendrier(int annee, int mois) {
        caltableModel.setRowCount(0);
        YearMonth moisActuel = YearMonth.of(annee, mois);
        LocalDate premierJour = moisActuel.atDay(1);
        int premierJourSemaine = premierJour.getDayOfWeek().getValue();
        int joursDansMois = moisActuel.lengthOfMonth();

        Object[][] donnees = new Object[6][7];
        int jour = 1;

        for (int i = (premierJourSemaine - 1) % 7; jour <= joursDansMois; i++) {
            int ligne = i / 7;
            int colonne = i % 7;
            donnees[ligne][colonne] = jour++;
        }

        for (Object[] row : donnees) {
            caltableModel.addRow(row);
        }
    }

    private void changerMois(int delta) {
        mois += delta;
        if (mois < 1) {
            mois = 12;
            annee--;
        } else if (mois > 12) {
            mois = 1;
            annee++;
        }
        remplirCalendrier(annee, mois);
    }

    // Classe interne pour colorer les jours réservés
    private static class CalendrierRenderer extends DefaultTableCellRenderer {
        private final Map<LocalDate, List<String>> reservations;

        public CalendrierRenderer(Map<LocalDate, List<String>> reservations) {
            this.reservations = reservations;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            cell.setBackground(Color.WHITE);

            if (value != null) {
                int jour = (int) value;
                LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), jour);

                if (reservations.containsKey(date)) {
                    cell.setBackground(Color.CYAN); // Jours réservés en bleu
                }
            }

            return cell;
        }
    }
}
