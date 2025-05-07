package edu.ezip.ing1.pds.Interface.locallaverie;

import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;
import edu.ezip.ing1.pds.uiUtils.LocalViewModel;
import edu.ezip.ing1.pds.usecase.AbonnementUseCase;
import edu.ezip.ing1.pds.usecase.LocalLaverieUseCase;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.ezip.ing1.pds.Formulaires.FormulaireLaver;
import static edu.ezip.ing1.pds.Formulaires.chargerIcone;

public class LocalLaveriePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<LocalLaverie> localLaveries;
    private LocalLaveries localLaveries2 = new LocalLaveries();
    private final static Logger logger = LoggerFactory.getLogger("local laverie");


    public LocalLaveriePanel(LocalLaverieUseCase localLaverieUseCase, PersonneUseCase personneUseCase, AbonnementUseCase abonnementUseCase, List<String> clientenattente) throws IOException, InterruptedException {
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
        Boolean dispo = true;
        refreshTable(localLaverieUseCase.afficherLocaux().getLocalLaveries().stream().toList());


        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton LaverButton = new JButton("Laver tout de suite" /*, chargerIcone("/icons/ajouter.png", 30, 30)*/);
        LaverButton.setBackground(Color.GREEN);
        LaverButton.addActionListener(e->{
            boolean dispoLocal = false;

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Object dispoValue = tableModel.getValueAt(i, 1);
                if (dispoValue instanceof Boolean && (Boolean) dispoValue ) {
                    dispoLocal = true;
                    break;
                }
            }

            if (!dispoLocal) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Plus de disponibilité en terme de local laverie, passer à la liste d'attente ?",
                        "Liste d'attente",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Liste d'attente Laverie");
                    dialog.setSize(400, 400);
                    dialog.setLocationRelativeTo(null);
                    dialog.setModal(true);
                    dialog.add(new PanelListeAttenteLaverie(clientenattente, personneUseCase, abonnementUseCase, localLaverieUseCase));
                    dialog.setVisible(true);
                }
            } else {
                //FormulaireLaver(null);
                new FormulaireLaverMaintenant(null, personneUseCase, abonnementUseCase, localLaverieUseCase);
            }
        });
        panelSud.add(LaverButton);

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
        JButton Attentebouton = new JButton("Liste D'attente"/*, chargerIcone("/icons/supprimer.png", 30, 30)*/);
        Attentebouton.setBackground(Color.GRAY);
        Attentebouton.addActionListener(e->{
            JDialog dialog = new JDialog();
            dialog.setTitle("Liste d'attente Laverie");
            dialog.setSize(400, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.add(new PanelListeAttenteLaverie(clientenattente, personneUseCase, abonnementUseCase, localLaverieUseCase));
            dialog.setVisible(true);
        });
        panelSud.add(Attentebouton);
        add(panelSud, BorderLayout.SOUTH);

    }


    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    void refreshTable(List<LocalLaverie> updatedList) {
        tableModel.setRowCount(0);
        for (LocalLaverie local : updatedList) {
            tableModel.addRow(new Object[]{
                    local.getNumLocalL(),
                    local.getDisponibilite()
            });
        }
    }
}
