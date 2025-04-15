package edu.ezip.ing1.pds.Interface.locallaverie;

import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;
import edu.ezip.ing1.pds.uiUtils.LocalViewModel;
import edu.ezip.ing1.pds.usecase.LocalLaverieUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

import static edu.ezip.ing1.pds.Formulaires.chargerIcone;

public class LocalLaveriePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<LocalLaverie> localLaveries;
    private LocalLaveries localLaveries2 = new LocalLaveries();
    private final static Logger logger = LoggerFactory.getLogger("local laverie");

    public LocalLaveriePanel(LocalLaverieUseCase localLaverieUseCase) throws IOException, InterruptedException {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));


        this.localLaveries =  localLaverieUseCase.afficherLocaux().getLocalLaveries().stream().toList();
        this.localLaveries2 = localLaverieUseCase.afficherLocaux();


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
        insertButton.addActionListener(e->{
            LocalViewModel.insertLocal(tableModel, this, logger );
        });
        panelSud.add(insertButton);
        JButton disponibutton = new JButton("Occuper un local", chargerIcone("/icons/modifier.png", 30, 30));
        disponibutton.setBackground(Color.YELLOW);
        disponibutton.addActionListener(e->{
            LocalViewModel.updateLocal(tableModel, this, localLaveries2, logger);
        });
        panelSud.add(disponibutton);
        JButton supprimebouton = new JButton("supprimer un Local", chargerIcone("/icons/supprimer.png", 30, 30));
        supprimebouton.setBackground(Color.RED);
        supprimebouton.addActionListener(e->{
            LocalViewModel.deleteLocal(tableModel, this, localLaveries2, logger);
        });
        panelSud.add(supprimebouton);
        add(panelSud, BorderLayout.SOUTH);



       // styleButton(addBtn, new Color(255, 152, 0));
       // addBtn.addActionListener(e ->   /* new AjouterAbonnementFrame(this, abonnementUseCase)*/);



        refreshTable(localLaverieUseCase.afficherLocaux().getLocalLaveries().stream().toList());

    }


    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    void refreshTable(List<LocalLaverie> updatedList) {
        tableModel.setRowCount(0);  // Clear existing rows
        for (LocalLaverie local : updatedList) {
            tableModel.addRow(new Object[]{
                    local.getNumLocalL(),
                    local.getDisponibilite()
            });
        }
    }
}
