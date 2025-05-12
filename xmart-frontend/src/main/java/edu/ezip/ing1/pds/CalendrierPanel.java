package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.ReservationLocal;
import edu.ezip.ing1.pds.business.dto.ReservationLocalParMoisMap;
import edu.ezip.ing1.pds.business.dto.ReservationLocaux;
import edu.ezip.ing1.pds.usecase.ReservationLocalUseCase;
import edu.ezip.ing1.pds.usecase.ReservationUseCase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendrierPanel extends JPanel{

    private ReservationLocalUseCase reservationLocalUseCase;
    private int annee;
    private int mois;

    //private Map<LocalDate, List<String>> reservations = Map.of();

    private ReservationLocalParMoisMap reservationLocalParMoisMap = new ReservationLocalParMoisMap();


    public CalendrierPanel(ReservationLocalUseCase reservationLocalUseCase) throws IOException, InterruptedException {
        this.reservationLocalUseCase = reservationLocalUseCase;
        this.annee = LocalDate.now().getYear();
        this.mois = LocalDate.now().getMonthValue();
        setLayout(new BorderLayout());
        chargerReservations();
        afficherCalendrier();




    }

    public  void afficherCalendrier()  {
        try {
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

        ReservationLocaux reservations = reservationLocalUseCase.getAllReservationsLocal();
        Map<LocalDate, List<String>> reservationsMap = transformerEnMap(reservations);



        int jour = 1;
        for (int i = 0; i < 42; i++) { // 42 cases c'est 6 lignes x 7 colonnes
            if (i >= (premierJourSemaine - 1) % 7 && jour <= joursDansMois) {
                JButton boutonJour = new JButton(String.valueOf(jour));
                LocalDate date = LocalDate.of(annee, mois, jour);


                if (reservationsMap.containsKey(date)) {
                    boutonJour.setBackground(Color.RED);
                    List<String> infos = reservationsMap.get(date);
                    String infoText = infos.size() == 1
                            ? infos.get(0)
                            : infos.size() + " réservations ce jour";

                    boutonJour.setToolTipText(infoText);
                } else {
                    boutonJour.setToolTipText("Vide");
                    //boutonJour.setBackground(Color.WHITE);
                }


                // pour l'aff des resas dans le calendriers et le pop up
                boutonJour.addActionListener(e ->{
                    if (reservationLocalParMoisMap.getMap().containsKey(date)) {
                        List<String> resasDuJour = reservationLocalParMoisMap.getMap().get(date);
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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage du calendrier : " + e.getMessage());
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

        removeAll();
        chargerReservations();
        afficherCalendrier();
        revalidate();
        repaint();
    }
    private void reserverJour(LocalDate date) {
        Formulaires.FormulaireReservationLocal(null);



        reservationLocalParMoisMap.getMap().computeIfAbsent(date, k -> new ArrayList<>()).add("Réservation ajoutée le " + date.toString());


        removeAll();
        afficherCalendrier();
        revalidate();
        repaint();
    }

    private void chargerReservations() {
        try {
            ReservationLocaux reservations = reservationLocalUseCase.getReservationsParMois(annee, mois);
            if (reservations != null && !reservations.getReservationLocaux().isEmpty()) {
                Map<LocalDate, List<String>> map = transformerEnMap(reservations);
                reservationLocalParMoisMap.setMap(map);
            } else {
                reservationLocalParMoisMap.setMap(new HashMap<>());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des réservations : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);

            reservationLocalParMoisMap.setMap(new HashMap<>());
        }
    }
    private Map<LocalDate, List<String>> transformerEnMap(ReservationLocaux reservationLocaux) {
        Map<LocalDate, List<String>> map = new HashMap<>();

        for (ReservationLocal r : reservationLocaux.getReservationLocaux()) {
            LocalDate debut = LocalDate.parse(r.getDateDebut());
            LocalDate fin = LocalDate.parse(r.getDateFin());

            for (LocalDate date = debut; !date.isAfter(fin); date = date.plusDays(1)) {
                String info = "Local " + r.getNumLocal() + " (" + r.getHeureEntree() + "-" + r.getHeureSortie() + ")";
                map.computeIfAbsent(date, d -> new ArrayList<>()).add(info);
            }
        }

        return map;
    }


}
