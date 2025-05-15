package edu.ezip.ing1.pds.Interface.personne;

import edu.ezip.ing1.pds.Interface.abonnement.AjouterAbonnementFrame;
import edu.ezip.ing1.pds.Interface.abonnement.HistoriquePaiement;
import edu.ezip.ing1.pds.Interface.abonnement.ModifierAbonnementFrame;
import edu.ezip.ing1.pds.Interface.abonnement.VoirAbonnement;
import edu.ezip.ing1.pds.api.AbonnementRepository;
import edu.ezip.ing1.pds.business.dto.Abonnement;
import edu.ezip.ing1.pds.business.dto.Abonnements;
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

import static org.yaml.snakeyaml.nodes.Tag.STR;

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
        voirAboItem.addActionListener(e -> {
            if (selectedPersonne != null) {
                try {
                    Abonnement abonnement = abonnementUseCase.findAbonnementX(selectedPersonne);
                    //List<Abonnement> abonnements = abonnementUseCase.getAllAbonnementsByPersonneId(selectedPersonne.getIdPersonne());
                    if (abonnement != null) {
                        new VoirAbonnement((JFrame) SwingUtilities.getWindowAncestor(this), abonnement, selectedPersonne);
                    } else {
                        JOptionPane.showMessageDialog(this, "Cette personne n'a pas d'abonnement.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de la récupération de l'abonnement.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }

        });



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
                new ModifierPersonneFrame(selectedPersonne, () -> {
                    //personnes = (List<Personne>) updatedList;
                    //refreshTable(personnes);
                    List<Personne> personnesActuelles = null;
                    try {
                        personnesActuelles = personneUseCase.afficherPersonnes().getPersonnes().stream().toList();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    refreshTable(personnesActuelles);

                    JOptionPane.showMessageDialog(this, "Les informations ont bien été modifiée.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }, personneUseCase);
            }
        });

        supprimerItem.addActionListener(e ->{
            if (selectedPersonne != null) {
                int res = JOptionPane.showConfirmDialog(table, "Supprimer cette personne ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    try {
                        personneUseCase.deletePersonne(selectedPersonne);
                        //personnes.remove(selectedPersonne);
                        //refreshTable(personnes);
                        List<Personne> personnesModifiables = new ArrayList<>(personnes);
                        personnesModifiables.remove(selectedPersonne);
                        personnes = personnesModifiables;
                        refreshTable(personnes);

                        JOptionPane.showMessageDialog(this, "Personne supprimée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(table, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
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
