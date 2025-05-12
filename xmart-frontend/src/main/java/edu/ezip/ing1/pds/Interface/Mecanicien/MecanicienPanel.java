package edu.ezip.ing1.pds.Interface.Mecanicien;




import edu.ezip.ing1.pds.Interface.abonnement.AjouterAbonnementFrame;
import edu.ezip.ing1.pds.Interface.Mecanicien.DetailMecanicienJDialog;
import edu.ezip.ing1.pds.business.dto.Mecanicien;
import edu.ezip.ing1.pds.business.dto.Mecaniciens;
import edu.ezip.ing1.pds.uiUtils.LocalTechniqueViewModel;
import edu.ezip.ing1.pds.uiUtils.MecanicienViewModel;
import edu.ezip.ing1.pds.usecase.MecanicienUseCase;
import edu.ezip.ing1.pds.usecase.ReservationLocalUseCase;
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
    private MecanicienUseCase mecanicienUseCase;

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
        });
        panelSud.add(insertButton);

        JButton supprimebouton = new JButton("supprimer un Mecanicien", chargerIcone("/icons/supprimer.png", 30, 30));
        supprimebouton.setBackground(Color.RED);
        supprimebouton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        MecanicienPanel.this,
                        "Veuillez sélectionner un mécanicien à supprimer.",
                        "Aucun mécanicien sélectionné",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String nom = (String) tableModel.getValueAt(selectedRow, 0);
            String prenom = (String) tableModel.getValueAt(selectedRow, 1);

            Mecanicien mecanicienASupprimer = mecaniciens.stream().filter(m -> m.getNom().equals(nom) && m.getPrenom().equals(prenom)).findFirst().orElse(null);

            if (mecanicienASupprimer != null) {
                int confirmation = JOptionPane.showConfirmDialog(
                        MecanicienPanel.this,
                        "Voulez-vous vraiment supprimer : " + nom + " " + prenom + " ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        mecanicienUseCase.supprimerMecanicien(mecanicienASupprimer);
                        JOptionPane.showMessageDialog(MecanicienPanel.this, "Mécanicien supprimé avec succès.");

                        mecaniciens = mecanicienUseCase.afficherMecaniciens().getMecaniciens().stream().toList();
                        refreshTable(mecaniciens);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                MecanicienPanel.this,
                                "Erreur lors de la suppression : " + ex.getMessage(),
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
        panelSud.add(supprimebouton);
        add(panelSud, BorderLayout.SOUTH);


        refreshTable(mecanicienUseCase.afficherMecaniciens().getMecaniciens().stream().toList());

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem PlusItem = new JMenuItem("Plus");

        contextMenu.add(PlusItem);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        PlusItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String nom = (String) tableModel.getValueAt(selectedRow, 0);
                String prenom = (String) tableModel.getValueAt(selectedRow, 1);
                String specialite = (String) tableModel.getValueAt(selectedRow, 2);
                Mecanicien mec = mecaniciens.stream().filter(m -> m.getNom().equals(nom) && m.getPrenom().equals(prenom)).findFirst().orElse(null); //je filtre pour recuperer le mecanicien courant

                if (mec != null) {
                    String tel = mec.getTelephone();
                    String mail = mec.getMail();
                    Boolean dispo = mec.getDisponibilite();

                DetailMecanicienJDialog dialog = new DetailMecanicienJDialog((Frame) SwingUtilities.getWindowAncestor(this), nom, prenom, tel, specialite, dispo, mail);
                dialog.setVisible(true);
                }
            }
        });

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
