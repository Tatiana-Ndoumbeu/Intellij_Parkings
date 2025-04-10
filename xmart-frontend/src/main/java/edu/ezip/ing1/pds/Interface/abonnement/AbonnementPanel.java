package edu.ezip.ing1.pds.Interface.abonnement;

import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.usecase.AbonnementUseCase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class AbonnementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Abonnement> abonnements;

    public AbonnementPanel(AbonnementUseCase abonnementUseCase) throws IOException, InterruptedException {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Fetch abonnements from the use case
        this.abonnements =  abonnementUseCase.getAllAbonnements().getAbonnements().stream().toList();

        // Define the table columns
        String[] columns = {"ID", "Type Abonnement", "Prix", "Statut", "Date Début", "Date Fin"};
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
        JButton addBtn = new JButton("Ajouter un abonnement");
        styleButton(addBtn, new Color(255, 152, 0));
        addBtn.addActionListener(e ->    new AjouterAbonnementFrame(this, abonnementUseCase));

        // Bottom panel with the add button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(addBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Refresh table to show abonnements
        refreshTable(abonnementUseCase.getAllAbonnements().getAbonnements().stream().toList());

        // Context menu for right-click actions (edit and delete)
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem modifierItem = new JMenuItem("Modifier");
        JMenuItem supprimerItem = new JMenuItem("Supprimer");

        contextMenu.add(modifierItem);
        contextMenu.add(supprimerItem);

        // Mouse listener for showing the context menu
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        contextMenu.show(table, e.getX(), e.getY());

                        Abonnement selected = abonnements.get(row);

                        // Action to modify the selected abonnement
                        modifierItem.addActionListener(ev -> {

                                new ModifierAbonnementFrame(selected, updatedList -> {
                                    abonnements = updatedList;  // Update the abonnements list
                                    refreshTable(abonnements);  // Refresh the table with the updated list
                                },abonnementUseCase);

                        });

                        // Action to delete the selected abonnement
                        supprimerItem.addActionListener(ev -> {
                            int res = JOptionPane.showConfirmDialog(table, "Supprimer cet abonnement ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                            if (res == JOptionPane.YES_OPTION) {
                                try {
                                    abonnementUseCase.deleteAbonnementById(selected.getIdAbonnement());
                                    abonnements.remove(selected);  // Remove from the list
                                    refreshTable(abonnements);  // Refresh the table after deletion
                                } catch (IOException | InterruptedException ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(table, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
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
    void refreshTable(List<Abonnement> updatedList) {
        tableModel.setRowCount(0);  // Clear existing rows
        for (Abonnement abonnement : updatedList) {
            tableModel.addRow(new Object[]{
                    abonnement.getIdAbonnement(),
                    abonnement.getTypeAbonnement(),
                    abonnement.getPrix(),
                    abonnement.getStatutAbonnement(),
                    abonnement.getDateDebut(),
                    abonnement.getDateFin()
            });
        }
    }
}
