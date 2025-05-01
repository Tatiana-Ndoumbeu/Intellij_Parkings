package edu.ezip.ing1.pds.Interface.personne;

import edu.ezip.ing1.pds.business.dto.Personne;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AjouterPersonneFrame extends JFrame{

    private final PersonneUseCase personneUseCase;

    public AjouterPersonneFrame(PersonnePanel parent, PersonneUseCase personneUseCase) {
        this.personneUseCase = personneUseCase;

        setTitle("Ajouter une personne");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
    }

    private void styleField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(placeholder));
    }

    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

}
