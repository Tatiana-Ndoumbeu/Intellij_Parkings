package edu.ezip.ing1.pds.Interface.Archives;

import edu.ezip.ing1.pds.business.dto.ArchivesPaiement;
import edu.ezip.ing1.pds.usecase.ArchivesUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import static edu.ezip.ing1.pds.Formulaires.chargerIcone;

public class ArchivesPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<ArchivesPaiement> archives;
    private final static Logger logger = LoggerFactory.getLogger("Archives ");

    public ArchivesPanel(ArchivesUseCase archivesUseCase)  throws IOException, InterruptedException{
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        this.archives =  archivesUseCase.afficherArchives().getArchivesPaiements().stream().toList();

        String[] columns = { "Nom", "Prenom", "Service Solicité", "Date", "Montant versé (€)" };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        JPanel panelSud = new JPanel();
        JButton ImprimerBouton = new JButton("", chargerIcone("/icons/imprimante.png", 30, 30));
        ImprimerBouton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(ArchivesPanel.this,"Vous allez Exporter et Imprimer les archives?\n\n Continuer? ", "Impression", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(
                        ArchivesPanel.this,
                        "Les archives ont été exportées au format PDF.",
                        "Exportation réussie",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        });
        panelSud.add(ImprimerBouton);
        add(panelSud, BorderLayout.SOUTH);
        refreshTable(archivesUseCase.afficherArchives().getArchivesPaiements().stream().toList());
    }

    public void refreshTable(List<ArchivesPaiement> paiements) {
        tableModel.setRowCount(0);

        for (ArchivesPaiement paiement : paiements) {
            tableModel.addRow(new Object[] {
                    paiement.getNom(),
                    paiement.getPrenom(),
                    paiement.getService(),
                    paiement.getDate(),
                    paiement.getMontant()
            });
        }
    }
}
