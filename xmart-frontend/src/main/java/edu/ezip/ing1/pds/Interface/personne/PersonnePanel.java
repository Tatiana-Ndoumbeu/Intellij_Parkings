package edu.ezip.ing1.pds.Interface.personne;

import edu.ezip.ing1.pds.Interface.abonnement.AjouterAbonnementFrame;
import edu.ezip.ing1.pds.api.AbonnementRepository;
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

public class PersonnePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Personne> personnes;
    private Personne selectedPersonne;
    private AbonnementUseCase abonnementUseCase;

    public PersonnePanel(PersonneUseCase personneUseCase, AbonnementUseCase abonnementUseCase) throws IOException, InterruptedException  {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        this.personnes = personneUseCase.afficherPersonnes().getPersonnes().stream().toList();
        this.abonnementUseCase = abonnementUseCase;

        String[] columns = {"ID", "Nom", "Prenom", "Tél", "Mail", "Code Postal"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        JButton addBtn = new JButton("Ajouter une personne");
        styleButton(addBtn, new Color(255, 152, 0));
        addBtn.addActionListener(e ->    new AjouterPersonneFrame(this, personneUseCase));


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(addBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable(personneUseCase.afficherPersonnes().getPersonnes().stream().toList());

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem creerAboItem = new JMenuItem("Creer un abonnement pour cette personne");
        JMenuItem voirAboItem = new JMenuItem("Voir son abonnement");
        JMenuItem modifierItem = new JMenuItem("Modifier");
        JMenuItem supprimerItem = new JMenuItem("Supprimer");

        creerAboItem.addActionListener(e ->{
            if(selectedPersonne != null) {
                try{
                    new AjouterAbonnementFrame(null, abonnementUseCase, selectedPersonne);
                } catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        modifierItem.addActionListener(e ->{
            if (selectedPersonne != null) {
                try{
                    new ModifierPersonneFrame(selectedPersonne, personneUseCase);
                } catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        contextMenu.add(creerAboItem);
        contextMenu.add(voirAboItem);
        contextMenu.add(modifierItem);
        contextMenu.add(supprimerItem);

        // Mouse listener for showing the context menu
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        selectedPersonne = personnes.get(row);
                        contextMenu.show(table, e.getX(), e.getY());




                    }
                }
            }
        });



    }

    private void styleButton(JButton button, Color bg) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    void refreshTable(List<Personne> updatedList) {
        this.personnes = updatedList;
        tableModel.setRowCount(0);
        for (Personne personne : updatedList) {
            tableModel.addRow(new Object[]{
                    personne.getIdPersonne(),
                    personne.getNom(),
                    personne.getPrenom(),
                    personne.getTelephone(),
                    personne.getMail(),
                    personne.getCodePostal()
            });
        }
    }
}
