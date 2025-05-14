package edu.ezip.ing1.pds.Interface.abonnement;

import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.usecase.AbonnementUseCase;
import edu.ezip.ing1.pds.business.dto.Personne;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AbonnementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Abonnement> abonnements;
    private Abonnement selectedAbonnement;
    private Personne personne;

    public AbonnementPanel(AbonnementUseCase abonnementUseCase, PersonneUseCase personneUseCase) throws IOException, InterruptedException {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Fetch abonnements from the use case
        this.abonnements =  abonnementUseCase.getAllAbonnements().getAbonnements().stream().toList();


        // Define the table columns
        String[] columns = {"ID", "Type Abonnement", "Prix", "Statut", "Date Début", "Date Fin", "Titulaire de l'abonnement"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;  // Disable editing in the table
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add button for adding new abonnement
        //JButton addBtn = new JButton("Ajouter un abonnement");
        //styleButton(addBtn, new Color(255, 152, 0));
        //addBtn.addActionListener(e ->    new AjouterAbonnementFrame(this, abonnementUseCase, selectedPersonne.getIdPersonne()));

        // Bottom panel with the add button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        //bottomPanel.add(addBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Refresh table to show abonnements
        refreshTable(abonnementUseCase.getAllAbonnements().getAbonnements().stream().toList());

        // Context menu for right-click actions (edit and delete)
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem modifierItem = new JMenuItem("Modifier");
        JMenuItem supprimerItem = new JMenuItem("Supprimer");

        modifierItem.addActionListener(ev -> {

            if (selectedAbonnement != null) {
                new ModifierAbonnementFrame(selectedAbonnement, updatedList -> {
                    abonnements = updatedList;

                    try {
                        refreshTable(abonnements);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, abonnementUseCase);
            }

        });

        // Action to delete the selected abonnement
        supprimerItem.addActionListener(ev -> {
            if (selectedAbonnement != null) {
                int res = JOptionPane.showConfirmDialog(table, "Supprimer cet abonnement ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    try {
                        abonnementUseCase.deleteAbonnementById(selectedAbonnement.getIdAbonnement());

                        List<Abonnement> abonnementsModifiables = new ArrayList<>(abonnements);
                        abonnementsModifiables.remove(selectedAbonnement);
                        abonnements = abonnementsModifiables;
                        refreshTable(abonnements);
                        //abonnements.remove(selectedAbonnement);
                        //refreshTable(abonnements);
                        JOptionPane.showMessageDialog(table, "Abonnement supprimé avec succès.", "Suppression", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(table, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        contextMenu.add(modifierItem);
        contextMenu.add(supprimerItem);

        // Mouse listener for showing the context menu
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        selectedAbonnement = abonnements.get(row);
                        contextMenu.show(table, e.getX(), e.getY());


                        // Action to modify the selected abonnement

                    }
                }
            }
        });
    }

    // Style buttons to make them look good
    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    // Method to refresh the table when the abonnements list changes
    void refreshTable(List<Abonnement> updatedList) throws IOException, InterruptedException {
        this.abonnements= updatedList;
        tableModel.setRowCount(0);  // Clear existing rows
        for (Abonnement abonnement : updatedList) {
            tableModel.addRow(new Object[]{
                    abonnement.getIdAbonnement(),
                    abonnement.getTypeAbonnement(),
                    abonnement.getPrix(),
                    abonnement.getStatutAbonnement(),
                    abonnement.getDateDebut(),
                    abonnement.getDateFin(),
                    abonnement.getNomPersonne() + " " + abonnement.getPrenomPersonne()
            });
        }
    }
}
