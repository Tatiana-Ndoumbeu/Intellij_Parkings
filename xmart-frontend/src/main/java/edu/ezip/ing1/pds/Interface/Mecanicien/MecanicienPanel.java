package edu.ezip.ing1.pds.Interface.Mecanicien;




import edu.ezip.ing1.pds.Interface.abonnement.AjouterAbonnementFrame;
import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;
import edu.ezip.ing1.pds.uiUtils.LocalTechniqueViewModel;
import edu.ezip.ing1.pds.uiUtils.MecanicienViewModel;
import edu.ezip.ing1.pds.usecase.MecanicienUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

import static edu.ezip.ing1.pds.Formulaires.chargerIcone;

public class MecanicienPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Mecanicien> mecaniciens;
    private final static Logger logger = LoggerFactory.getLogger("mecanicien");

    public MecanicienPanel(MecanicienUseCase mecanicienUseCase) throws IOException, InterruptedException {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));


        this.mecaniciens =  mecanicienUseCase.afficherMecaniciens().getMecaniciens().stream().toList();


        String[] columns = {"Nom", "Prenom", "Spécialité"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton insertButton = new JButton("Ajouter un Mecanicien" , chargerIcone("/icons/ajouter.png", 30, 30));
        insertButton.setBackground(Color.GREEN);
        insertButton.addActionListener(e -> {
            new AjouterMecanicienForm(this, mecanicienUseCase);
            //MecanicienViewModel.insertMecanicien(tableModel, this, logger);
        });
        panelSud.add(insertButton);

        JButton supprimebouton = new JButton("supprimer un Mecanicien", chargerIcone("/icons/supprimer.png", 30, 30));
        supprimebouton.setBackground(Color.RED);
        supprimebouton.addActionListener(e -> {

            //ajouter l'action plus tard

        });
        panelSud.add(supprimebouton);
        add(panelSud, BorderLayout.SOUTH);


        refreshTable(mecanicienUseCase.afficherMecaniciens().getMecaniciens().stream().toList());

    }


    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    void refreshTable(List<Mecanicien> updatedList) {
        tableModel.setRowCount(0);
        for (Mecanicien mecanicien : updatedList) {
            tableModel.addRow(new Object[]{
                    mecanicien.getNom(),
                    mecanicien.getPrenom(),
                    mecanicien.getSpecialite()});


        }
    }
}
