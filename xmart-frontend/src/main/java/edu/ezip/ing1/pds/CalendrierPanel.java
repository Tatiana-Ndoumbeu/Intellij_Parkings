package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.ReservationLocalParMoisMap;
import edu.ezip.ing1.pds.usecase.ReservationLocalUseCase;
import edu.ezip.ing1.pds.usecase.ReservationUseCase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalendrierPanel extends JPanel {

    private ReservationLocalUseCase reservationLocalUseCase;
    private int annee;
    private int mois;

    private Map<LocalDate, List<String>> reservations = Map.of();


    public CalendrierPanel(ReservationLocalUseCase reservationLocalUseCase) {
        this.reservationLocalUseCase = reservationLocalUseCase;
        this.annee = LocalDate.now().getYear();
        this.mois = LocalDate.now().getMonthValue();
        setLayout(new BorderLayout());
        chargerReservations();
        afficherCalendrier();

    }

    public  void afficherCalendrier() {
        JPanel joursPanel = new JPanel(new GridLayout(1, 7));
        String[] joursSemaine = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (String jour : joursSemaine) {
            JLabel label = new JLabel(jour, SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            joursPanel.add(label);
        }

        JPanel calendrierPanel = new JPanel(new GridLayout(6, 7));
        YearMonth yearMonth = YearMonth.of(annee, mois);
        LocalDate premierJour = yearMonth.atDay(1);
        int premierJourSemaine = premierJour.getDayOfWeek().getValue();
        int joursDansMois = yearMonth.lengthOfMonth();


        int jour = 1;
        for (int i = 0; i < 42; i++) { // 42 cases c'est 6 lignes x 7 colonnes
            if (i >= (premierJourSemaine - 1) % 7 && jour <= joursDansMois) {
                JButton boutonJour = new JButton(String.valueOf(jour));
                LocalDate date = LocalDate.of(annee, mois, jour);


                if (reservations.containsKey(date)) {
                    boutonJour.setBackground(Color.RED);
                    boutonJour.setToolTipText("Réservations: " + String.join(", ", reservations.get(date)));
                } else {
                    //boutonJour.setBackground(Color.WHITE);
                    boutonJour.setToolTipText(null);
                }


                // pour l'aff des resas dans le calendriers et le pop up
                boutonJour.addActionListener(e ->{
                    if (reservations.containsKey(date)) {
                        List<String> resasDuJour = reservations.get(date);
                        String message = String.join("\n", resasDuJour);
                        JOptionPane.showMessageDialog(this, message, "Réservations pour le " + date, JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        int choix = JOptionPane.showConfirmDialog(this,
                                "Aucune réservation. Voulez-vous en créer une ?",
                                "Pas de réservation",
                                JOptionPane.YES_NO_OPTION);

                        if (choix == JOptionPane.YES_OPTION) {
                            reserverJour(date);
                        }
                    }
                });

                calendrierPanel.add(boutonJour);
                jour++;
            } else {
                calendrierPanel.add(new JLabel("")); // Ajouter une cellule vide si pas de jour
            }
        }

        add(calendrierPanel, BorderLayout.CENTER);

        JPanel panelNord = new JPanel();
        JButton boutonPrecedent = new JButton("Mois précédent");
        JButton boutonSuivant = new JButton("Mois suivant");
        String nomMois = YearMonth.of(annee, mois).getMonth().name();
        JButton moisactuel = new JButton(nomMois + " " + annee);
        moisactuel.setBackground(Color.RED);

        boutonPrecedent.addActionListener(e -> changerMois(-1));
        boutonSuivant.addActionListener(e -> changerMois(1));

        panelNord.add(boutonPrecedent);
        panelNord.add(boutonSuivant);
        panelNord.add(moisactuel);
        add(panelNord, BorderLayout.SOUTH);
        add(joursPanel, BorderLayout.NORTH);
        add(calendrierPanel, BorderLayout.CENTER);
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

        removeAll();
        chargerReservations();
        afficherCalendrier();
        revalidate();
        repaint();
    }
    private void reserverJour(LocalDate date) {
        Formulaires.FormulaireReservationLocal(null);



        reservations.computeIfAbsent(date, k -> new ArrayList<>()).add("Réservation ajoutée le " + date.toString());


        removeAll();
        afficherCalendrier();
        revalidate();
        repaint();
    }
    private void chargerReservations() {

            try {
                ReservationLocalParMoisMap dto = reservationLocalUseCase.getReservationsParMois(annee, mois);
                this.reservations = dto.getMap();
            } catch (Exception e) {
                e.printStackTrace();
                this.reservations = Map.of();
            }

    }


}
