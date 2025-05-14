package edu.ezip.ing1.pds.Interface.placesdeparking;

import com.toedter.calendar.JDateChooser;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;

import edu.ezip.ing1.pds.business.dto.ReservationRequest;
import edu.ezip.ing1.pds.usecase.ReservationUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;




public class FormulaireReservation {
    private final static String LoggingLabel = "FrontEnd - ReservationService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    public static void showForm(JFrame parent, ReservationUseCase reservationUseCase, PlaceDeParking selectedPlace, Runnable refreshTable) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        // Create the dialog instance and set its title
        JDialog dialog = new JDialog(parent, "Réserver une place", true);
        dialog.setSize(500, 800);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245)); // Light gray background
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100)); // Padding

        JLabel title = new JLabel("Créer une réservation", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(50, 50, 50));

        // Define fields for reservation
        JTextField champPosition = new JTextField(selectedPlace.getIdPlace());
        JTextField champEmplacement = new JTextField(selectedPlace.getEmplacement());
        champPosition.setEditable(false);
        champEmplacement.setEditable(false);

        JTextField champNom = new JTextField();
        JTextField champPrenom = new JTextField();
        JTextField champTelephone = new JTextField();
        JTextField champMail = new JTextField();
        JTextField champCodeP = new JTextField();

        // Set field styles
        styleField(champPosition, "Position");
        styleField(champEmplacement, "Emplacement");
        styleField(champNom, "Nom complet");
        styleField(champPrenom, "Prénom");
        styleField(champTelephone, "Téléphone");
        styleField(champMail, "E-mail");
        styleField(champCodeP, "Code Postal");

        JDateChooser dateDebutChooser = new JDateChooser();
        JSpinner hourSpinner = new JSpinner(new SpinnerDateModel());
        hourSpinner.setEditor(new JSpinner.DateEditor(hourSpinner, "HH:mm"));

        JDateChooser dateFinChooser = new JDateChooser();
        JSpinner hourSpinnerFin = new JSpinner(new SpinnerDateModel());
        hourSpinnerFin.setEditor(new JSpinner.DateEditor(hourSpinnerFin, "HH:mm"));

        styleDateField(dateDebutChooser);
        styleDateField(dateFinChooser);

        // Button for submission
        JButton boutonValider = new JButton("Valider");
        styleButton(boutonValider, new Color(255, 152, 0)); // Orange

        boutonValider.addActionListener(e -> {
            // Collect data from form
            String nom = champNom.getText().trim();
            String prenom = champPrenom.getText().trim();
            String telephone = champTelephone.getText().trim();
            String mail = champMail.getText().trim();
            String codePostal = champCodeP.getText().trim();
            Date dateDebut = dateDebutChooser.getDate();
            Date heureDebut = (Date) hourSpinner.getValue();
            Date dateFin = dateFinChooser.getDate();
            Date heureFin = (Date) hourSpinnerFin.getValue();

            // Validation
            if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || codePostal.isEmpty() || dateDebut == null || dateFin == null) {
                JOptionPane.showMessageDialog(dialog, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                ReservationRequest reservation = new ReservationRequest();
                reservation.setIdReservation(generateUniqueId());
                reservation.setDateReservation(LocalDate.now().toString());
                reservation.setHeure(LocalTime.now().toString());
                reservation.setDateEntree(dateDebut.toString());
                reservation.setDateSortie(dateFin.toString());
                reservation.setHeureEntree(heureDebut.toString());
                reservation.setHeureSortie(heureFin.toString());
                reservation.setIdPersonne(mail);
                reservation.setPlaceDeParking(selectedPlace); 

                // Insert reservation using the use case
                try {
                  var reservationResult=   reservationUseCase.createReservation(reservation);

                    if(reservationResult){
                        dialog.dispose();
                        refreshTable.run();
                        JOptionPane.showMessageDialog(dialog, "Réservation effectuée avec succès.");

                    }else{
                        JOptionPane.showMessageDialog(dialog, "Erreur lors de l'enregistrement de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);

                    }
                   // Refresh the table after reservation
                } catch (Exception ex) {
                    logger.error("could not create reservation", ex);
                    JOptionPane.showMessageDialog(dialog, "Erreur lors de l'enregistrement de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(champPosition);
        panel.add(Box.createVerticalStrut(15));
        panel.add(champEmplacement);
        panel.add(Box.createVerticalStrut(15));
        panel.add(champNom);
        panel.add(Box.createVerticalStrut(15));
        panel.add(champPrenom);
        panel.add(Box.createVerticalStrut(15));
        panel.add(champTelephone);
        panel.add(Box.createVerticalStrut(15));
        panel.add(champMail);
        panel.add(Box.createVerticalStrut(15));
        panel.add(champCodeP);
        panel.add(Box.createVerticalStrut(15));
        panel.add(dateDebutChooser);
        panel.add(Box.createVerticalStrut(15));
        panel.add(hourSpinner);
        panel.add(Box.createVerticalStrut(15));
        panel.add(dateFinChooser);
        panel.add(Box.createVerticalStrut(15));
        panel.add(hourSpinnerFin);
        panel.add(Box.createVerticalStrut(30));
        panel.add(boutonValider);

        // Add a JScrollPane to make the form scrollable
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scroll bar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Only show horizontal scroll bar if needed

        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER); // Add the scrollable panel to the dialog

        dialog.setVisible(true);
    }

    private static void styleField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
    }

    private static void styleDateField(JDateChooser dateChooser) {
        dateChooser.setPreferredSize(new Dimension(150, 30));
    }

    private static void styleButton(JButton button, Color bg) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    // Helper function to generate a unique reservation ID (you can modify this to suit your needs)
    private static String generateUniqueId() {
        return "RES-" + System.currentTimeMillis();
    }
}
