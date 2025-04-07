package edu.ezip.ing1.pds.uiUtils;

import edu.ezip.ing1.pds.Formulaires;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.business.dto.PlacesDeParkings;
import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ReservationService;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.ezip.ing1.pds.MainFrontEndSwing.networkConfigFile;
import static edu.ezip.ing1.pds.uiUtils.MecanicienViewModel.createReservationTableModel;

public class ReservationViewModel {

    public static JPanel createTablePanelresa(PlacesDeParkings placesDeParkings, Logger logger,JFrame component,Reservations reservations)
    {
        JPanel panelsud = new JPanel(new FlowLayout());
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        table.setModel(createReservationTableModel(networkConfig,placesDeParkings,logger));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton reserverButton = new JButton("reserver une zone spéciale",chargerIcone("/icons/reserver.png", 30, 30));
        reserverButton.setBackground(Color.CYAN);
        reserverButton.addActionListener( e -> Formulaires.FormulaireReservation(component));
        panelsud.add(reserverButton);
        panel.add(panelsud, BorderLayout.SOUTH);

        JButton reservationEnCoursButton = new JButton("Afficher toutes les reservvations", chargerIcone("/icons/liste.png", 30, 30));
        reservationEnCoursButton.setBackground(Color.CYAN);
        reservationEnCoursButton.addActionListener(e->table.setModel(reservationEnregistreesTableModel(networkConfig,reservations,logger)));
        panelsud.add(reservationEnCoursButton);

        JButton calendrierResa = new JButton("Calendrier");
        calendrierResa.setBackground(Color.CYAN);
        calendrierResa.addActionListener( e -> ouvrirCalendrier());
        //table.setModel(createCalendrierTableModel(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));

        panelsud.add(calendrierResa);

        return panel;
    }
    private static void ouvrirCalendrier() {

        Map<LocalDate, List<String>> reservations = new HashMap<>();

        JFrame calendrierFrame = new JFrame("Calendrier des réservations");
        calendrierFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        calendrierFrame.setSize(550, 450);

        //CalendrierPanel calendrierPanel = new CalendrierPanel(reservations);
        // calendrierFrame.add(calendrierPanel);
        calendrierFrame.setVisible(true);
    }
    public DefaultTableModel createCalendrierTableModel(int annee, int mois) {
        JPanel pannelNord = new JPanel(new FlowLayout());
        JButton boutonPrecedent = new JButton("Mois précédent");
        JButton boutonSuivant = new JButton("Mois suivant");

        //boutonPrecedent.addActionListener(e -> changerMois(-1));
        //boutonSuivant.addActionListener(e -> changerMois(1));

        pannelNord.add(boutonPrecedent);
        pannelNord.add(boutonSuivant);
       // add(pannelNord, BorderLayout.NORTH);

        String[] columns = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        YearMonth yearMonth = YearMonth.of(annee, mois);
        LocalDate premierJour = yearMonth.atDay(1);// Premier jour du mois
        int premierJourSemaine = premierJour.getDayOfWeek().getValue();// Jour de la semaine du premier jour (1 = Lundi, 7 = Dimanche)
        int joursDansMois = yearMonth.lengthOfMonth(); // Nombre de jours dans le mois

        Object[][] donnees = new Object[6][7];
        int jour = 1;

        for (int i = (premierJourSemaine - 1) % 7; jour <= joursDansMois; i++) {
            int ligne = i / 7;
            int colonne = i % 7;
            donnees[ligne][colonne] = jour++;
        }

        for (Object[] row : donnees) {
            model.addRow(row);
        }

        return model;
    }





    private static DefaultTableModel reservationEnregistreesTableModel(NetworkConfig networkConfig, Reservations reservations, Logger logger) {
        final ReservationService reservationService = new ReservationService(networkConfig);
        String[] columns = {"id reservation","Position", "type", "date début"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try{
            reservations = reservationService.selectReservations();
            if (reservations != null && reservations.getReservations() != null){
                for (Reservation reservation : reservations.getReservations()) {
                    PlaceDeParking place = reservation.getPlaceDeParking();
                    model.addRow(new Object[]{
                            reservation.getIdReservation(),
                            (place != null) ? place.getIdPlace() : "Non attribué",
                            (place != null) ? place.getTypePlace() : "Non attribué",
                            reservation.getDateEntree()
                    });

                }
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Erreur recuperation reservation", e);
        }
        return model;
    }

    private static ImageIcon chargerIcone(String chemin, int largeur, int hauteur) {

        ImageIcon icon = new ImageIcon(ReservationViewModel.class.getResource(chemin));
        Image image = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }


}
