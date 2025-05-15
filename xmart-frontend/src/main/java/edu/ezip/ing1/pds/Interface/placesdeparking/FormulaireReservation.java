package edu.ezip.ing1.pds.Interface.placesdeparking;

import com.toedter.calendar.JDateChooser;
import edu.ezip.ing1.pds.business.dto.Personne;
import edu.ezip.ing1.pds.business.dto.PlaceDeParking;
import edu.ezip.ing1.pds.business.dto.ReservationRequest;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;
import edu.ezip.ing1.pds.usecase.ReservationUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class FormulaireReservation {
    private final static String LoggingLabel = "FrontEnd - ReservationService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    public static void showForm(JFrame parent,
                                ReservationUseCase reservationUseCase,
                                PlaceDeParking selectedPlace,
                                Runnable refreshTable,
                                PersonneUseCase personneUseCase) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        JDialog dialog = new JDialog(parent, "Réserver une place", true);
        dialog.setSize(500, 800);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel title = new JLabel("Créer une réservation", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(50, 50, 50));

        JTextField champPosition = new JTextField(selectedPlace.getIdPlace());
        JTextField champEmplacement = new JTextField(selectedPlace.getEmplacement());
        champPosition.setEditable(false);
        champEmplacement.setEditable(false);
        styleField(champPosition, "Position");
        styleField(champEmplacement, "Emplacement");

        JComboBox<Personne> personneComboBox = new JComboBox<>();
        try {
           var personnes = personneUseCase.afficherPersonnes().getPersonnes();
            for (Personne p : personnes) {
                personneComboBox.addItem(p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Erreur lors du chargement des personnes.");
        }
        personneComboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        personneComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        personneComboBox.setBorder(BorderFactory.createTitledBorder("Sélectionner une personne"));
        personneComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Personne p) {
                    setText(p.getPrenom() + " " + p.getNom());
                }
                return this;
            }
        });

        JDateChooser dateDebutChooser = new JDateChooser();
        dateDebutChooser.setDateFormatString("dd/MM/yyyy");
        JSpinner hourSpinner = new JSpinner(new SpinnerDateModel());
        hourSpinner.setEditor(new JSpinner.DateEditor(hourSpinner, "HH:mm"));

        JDateChooser dateFinChooser = new JDateChooser();
        dateFinChooser.setDateFormatString("dd/MM/yyyy");
        JSpinner hourSpinnerFin = new JSpinner(new SpinnerDateModel());
        hourSpinnerFin.setEditor(new JSpinner.DateEditor(hourSpinnerFin, "HH:mm"));

        styleDateField(dateDebutChooser);
        styleDateField(dateFinChooser);

        JButton boutonValider = new JButton("Valider");
        styleButton(boutonValider, new Color(255, 152, 0));

        boutonValider.addActionListener(e -> {
            Date dateDebut = dateDebutChooser.getDate();
            Date heureDebut = (Date) hourSpinner.getValue();
            Date dateFin = dateFinChooser.getDate();
            Date heureFin = (Date) hourSpinnerFin.getValue();
            Personne selectedPersonne = (Personne) personneComboBox.getSelectedItem();

            if (selectedPersonne == null || dateDebut == null || dateFin == null) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ReservationRequest reservation = new ReservationRequest();
            reservation.setIdReservation(generateUniqueId());
            reservation.setDateReservation(LocalDate.now().toString());
            reservation.setHeure(LocalTime.now().toString());
            reservation.setDateEntree(dateDebut.toString());
            reservation.setDateSortie(dateFin.toString());
            reservation.setHeureEntree(heureDebut.toString());
            reservation.setHeureSortie(heureFin.toString());
            reservation.setIdPersonne(selectedPersonne.getIdPersonne());
            reservation.setPlaceDeParking(selectedPlace);

            try {
                boolean success = reservationUseCase.createReservation(reservation);
                if (success) {
                    dialog.dispose();
                    refreshTable.run();
                    JOptionPane.showMessageDialog(dialog, "Réservation effectuée avec succès.");
                } else {
                    JOptionPane.showMessageDialog(dialog, "La réservation a échoué. Veuillez vérifier les dates ou la disponibilité de la place.", "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                logger.error("Erreur lors de la réservation", ex);
                String message = ex.getMessage();
                if (message != null && message.contains("réservée")) {
                    JOptionPane.showMessageDialog(dialog, message, "Conflit de réservation", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erreur : " + message, "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(champPosition);
        panel.add(Box.createVerticalStrut(15));
        panel.add(champEmplacement);
        panel.add(Box.createVerticalStrut(15));
        panel.add(personneComboBox);
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

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private static void styleField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
    }

    private static void styleDateField(JDateChooser dateChooser) {
        dateChooser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        dateChooser.setFont(new Font("SansSerif", Font.PLAIN, 16));
        dateChooser.setBorder(BorderFactory.createTitledBorder("Date"));
    }

    private static void styleButton(JButton button, Color bg) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private static String generateUniqueId() {
        return "RES-" + System.currentTimeMillis();
    }
}