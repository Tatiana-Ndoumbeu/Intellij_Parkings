package edu.ezip.ing1.pds.Interface;

import edu.ezip.ing1.pds.api.*;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.*;
import edu.ezip.ing1.pds.uiUtils.LoginAdminUseCase;
import edu.ezip.ing1.pds.uiUtils.RegisterAdminUseCase;
import edu.ezip.ing1.pds.usecase.*;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginFrame extends JFrame {
    public LoginFrame(NetworkConfig config) {
        setTitle("Connexion Administrateur");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        AdminService service = new AdminService(config);
        LoginAdminUseCase loginUseCase = new LoginAdminUseCase(service);
        RegisterAdminUseCase registerUseCase = new RegisterAdminUseCase(service);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(230, 240, 255)); // Bleu pâle doux
        panel.setBorder(BorderFactory.createEmptyBorder(100, 450, 100, 450));

        JLabel title = new JLabel("Connexion à l'espace administrateur", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(30, 30, 30));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Se connecter");
        JButton registerBtn = new JButton("Créer un compte");

        styleField(emailField, "Adresse e-mail");
        styleField(passwordField, "Mot de passe");
        styleButton(loginBtn, new Color(76, 175, 80)); // Vert
        styleButton(registerBtn, new Color(66, 133, 244)); // Bleu Google
        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Champs manquants", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JDialog loader = showLoader("Connexion en cours...");
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() {
                    return loginUseCase.login(email, password);
                }

                @Override
                protected void done() {
                    loader.dispose();
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(LoginFrame.this, "Connexion réussie !");
                            dispose();
                         //   SwingUtilities.invokeLater(() -> new edu.ezip.ing1.pds.MainFrontEndSwing().setVisible(true));
                            showDasbord();
                        } else {
                            JOptionPane.showMessageDialog(LoginFrame.this, "Échec de la connexion.");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(LoginFrame.this, "Erreur interne.");
                    }
                }
            };
            worker.execute();
            loader.setVisible(true);
        });



        registerBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new RegisterFrame(registerUseCase));
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(40));
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(30));
        panel.add(loginBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(registerBtn);

        add(panel);
        setVisible(true);
    }

    private void styleField(JTextField field, String placeholder) {
        field.setEditable(true);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        /*field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(placeholder),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));*/
    }

    private void styleButton(JButton button, Color bg) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private javax.swing.JDialog showLoader(String message) {
        javax.swing.JDialog loader = new javax.swing.JDialog(this, true);
        loader.setUndecorated(true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel(message, JLabel.CENTER), BorderLayout.CENTER);
        loader.getContentPane().add(panel);
        loader.setSize(200, 100);
        loader.setLocationRelativeTo(this);
        return loader;
    }


    private static void showDasbord() {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, "network.yaml");
        PlaceDeParkingRepository repository = new PlaceDeParkingService(networkConfig);
        ReservationRepository reservationRepository = new ReservationService(networkConfig);
        AbonnementRepository abonnementRepository = new AbonnementService(networkConfig);
        PersonneRepository personneRepository = new PersonneService(networkConfig);
        LocalLaverieRepository localLaverieRepository = new LocalLaveriesService(networkConfig);
        LocalTechniqueRepository localTechniqueRepository = new LocalTechniqueService(networkConfig);
        MecanicienRepository mecanicienRepository = new MecanicienService(networkConfig);
        ReservationLocalRepository reservationLocalRepository = new ReservationLocalService(networkConfig);
        ArchivesRepository archivesRepository = new ArchivesPaiementService(networkConfig);

        PlaceDeParkingUseCase placeUseCase = new PlaceDeParkingUseCase(repository);
        ReservationUseCase reservUseCase = new ReservationUseCase(reservationRepository);
        AbonnementUseCase abonUseCase = new AbonnementUseCase(abonnementRepository);
        PersonneUseCase persoUseCase = new PersonneUseCase(personneRepository);
        LocalLaverieUseCase LLUsecase = new LocalLaverieUseCase(localLaverieRepository);
        LocalTechniqueUseCase LTUsecase = new LocalTechniqueUseCase(localTechniqueRepository);
        MecanicienUseCase MecaUseCase = new MecanicienUseCase(mecanicienRepository);
        ReservationLocalUseCase ResaLocalUseCase = new ReservationLocalUseCase(reservationLocalRepository);
        ArchivesUseCase ArchivesPaiementUseCase = new ArchivesUseCase(archivesRepository);

        List<String> clientsEnAttente = new ArrayList<>(Arrays.asList("", "", "", ""));

        SwingUtilities.invokeLater(() -> new DashboardFrame(placeUseCase, reservUseCase, abonUseCase, persoUseCase, LLUsecase, LTUsecase, MecaUseCase, ResaLocalUseCase, ArchivesPaiementUseCase, clientsEnAttente));
    }
}
