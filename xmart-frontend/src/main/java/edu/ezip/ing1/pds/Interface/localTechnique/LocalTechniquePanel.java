package edu.ezip.ing1.pds.Interface.localTechnique;


import edu.ezip.ing1.pds.business.dto.LocalTechnique;
import edu.ezip.ing1.pds.business.dto.LocalTechniques;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;
import edu.ezip.ing1.pds.uiUtils.LocalTechniqueViewModel;
import edu.ezip.ing1.pds.usecase.LocalTechniqueUseCase;
import edu.ezip.ing1.pds.usecase.MecanicienUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

import static edu.ezip.ing1.pds.Formulaires.FormulairePaiementTechnique;
import static edu.ezip.ing1.pds.Formulaires.chargerIcone;

public class LocalTechniquePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<LocalTechnique> localTechniques;
    private LocalTechniques localTechniques2 = new LocalTechniques();
    private final static Logger logger = LoggerFactory.getLogger("local laverie");
    private Mecaniciens mecaniciens = new Mecaniciens();

    public LocalTechniquePanel(LocalTechniqueUseCase localTechniqueUseCase, MecanicienUseCase mecanicienUseCase) throws IOException, InterruptedException {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));


        this.localTechniques =  localTechniqueUseCase.afficherLocaux().getLocalTechniques().stream().toList();
        this.localTechniques2 = localTechniqueUseCase.afficherLocaux();
        this.mecaniciens = mecanicienUseCase.afficherMecaniciens();

        String[] columns = { "numero Local", "disponibilite"};
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

        JButton insertButton = new JButton("Ajouter un Local" , chargerIcone("/icons/ajouter.png", 30, 30));
        insertButton.setBackground(Color.GREEN);
        insertButton.addActionListener(e -> {
            LocalTechniqueViewModel.insertLocalT(tableModel, this, logger);
        });
        panelSud.add(insertButton);
        JButton disponibutton = new JButton("Occuper un local", chargerIcone("/icons/modifier.png", 30, 30));
        disponibutton.setBackground(Color.YELLOW);
        disponibutton.addActionListener(e -> {
            LocalTechniqueViewModel.updateLocalT(tableModel, this, localTechniques2, logger);
        });
        panelSud.add(disponibutton);
        JButton supprimebouton = new JButton("supprimer un Local", chargerIcone("/icons/supprimer.png", 30, 30));
        supprimebouton.setBackground(Color.RED);
        supprimebouton.addActionListener(e -> {
            LocalTechniqueViewModel.deleteLocalT(tableModel, this, localTechniques2, logger);
        });
        panelSud.add(supprimebouton);
        JButton PaiementTech = new JButton("Regler une facture technique");
        PaiementTech.addActionListener(e -> { new FormulairePaiementTechnique(null, mecanicienUseCase);

        });
        panelSud.add(PaiementTech);
        add(panelSud, BorderLayout.SOUTH);

        // addBtn.addActionListener(e ->   /* new AjouterAbonnementFrame(this, abonnementUseCase)*/);



        add(panelSud, BorderLayout.SOUTH);

        refreshTable(localTechniqueUseCase.afficherLocaux().getLocalTechniques().stream().toList());

    }


    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    void refreshTable(List<LocalTechnique> updatedList) {
        tableModel.setRowCount(0);  // Clear existing rows
        for (LocalTechnique local : updatedList) {
            tableModel.addRow(new Object[]{
                    local.getNumLocalT(),
                    local.getDisponibilite()
            });
        }
    }
}
