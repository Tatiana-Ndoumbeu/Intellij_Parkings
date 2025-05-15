package edu.ezip.ing1.pds.Interface.Mecanicien;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DetailMecanicienJDialog extends JDialog {

    public DetailMecanicienJDialog(Frame parent, String nom, String prenom, String tel,  String specialite, Boolean dispo, String mail) {
        super(parent, "Infos Mécanicien", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());


        JPanel infoPanel = new JPanel(new GridLayout(7, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        infoPanel.add(new JLabel("Nom : " + nom));
        infoPanel.add(new JLabel("Prénom : " + prenom));
        infoPanel.add(new JLabel("Téléphone : " +tel));
        infoPanel.add(new JLabel("Spécialité : " + specialite));
        infoPanel.add(new JLabel("Disponibilité : " + dispo));
        infoPanel.add(new JLabel("Mail : " + mail));

        JPanel boutonPanel = new JPanel(new FlowLayout());
        JButton contacterBtn = new JButton("Contacter");
        JButton mailBtn = new JButton("Laisser un mail");

        contacterBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "APPELER LE "+tel);
        });

        mailBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "ECRIRE UN MAIL À "+mail);
            try {
                String subject = "Demande d'intervention";
                String body = "Bonjour " + prenom + ",\n\n ...";
                URI mailto = new URI("mailto:" + mail + "?subject=" + ComposanteMailEncodage(subject) + "&body=" + ComposanteMailEncodage(body));
                Desktop.getDesktop().mail(mailto);
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Impossible d'ouvrir l'application de mails.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonPanel.add(contacterBtn);
        boutonPanel.add(mailBtn);

        add(infoPanel, BorderLayout.CENTER);
        add(boutonPanel, BorderLayout.SOUTH);
    }
    private String ComposanteMailEncodage(String s) {
        try {
            return java.net.URLEncoder.encode(s, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            return s;
        }
    }
}
