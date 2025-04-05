package edu.ezip.ing1.pds.Interface;

import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.uiUtils.RegisterAdminUseCase;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final static String networkConfigFile = "network.yaml";
    public RegisterFrame(RegisterAdminUseCase useCase) {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        setTitle("Création d'un compte administrateur");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245)); // Gris clair doux
        panel.setBorder(BorderFactory.createEmptyBorder(80, 450, 80, 450));

        JLabel title = new JLabel("Créer un compte administrateur", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(50, 50, 50));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();
        JButton createBtn = new JButton("Créer le compte");

        styleField(nameField, "Nom complet");
        styleField(emailField, "Adresse e-mail");
        styleField(passField, "Mot de passe");
        styleField(confirmField, "Confirmer le mot de passe");
        styleButton(createBtn, new Color(255, 152, 0)); // Orange

        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
            String confirmPassword = new String(confirmField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Champs manquants", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = useCase.register(name, email, password, confirmPassword);
            JOptionPane.showMessageDialog(this, success ? "Compte créé avec succès." : "Erreur lors de la création du compte.");

            if (success) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame(networkConfig));
            }
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(30));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(passField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(confirmField);
        panel.add(Box.createVerticalStrut(25));
        panel.add(createBtn);

        add(panel);
        setVisible(true);
    }

    private void styleField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
    }

    private void styleButton(JButton button, Color bg) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}
