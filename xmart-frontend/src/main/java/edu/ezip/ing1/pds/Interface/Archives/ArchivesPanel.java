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
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;

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
            int confirmation = JOptionPane.showConfirmDialog(ArchivesPanel.this,"Vous allez Exporter et Imprimer les archives au format PDF,\n\n Continuer? ", "Impression", JOptionPane.YES_NO_OPTION);

            if(confirmation == JOptionPane.YES_OPTION){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("archives.pdf"));
            int option = fileChooser.showSaveDialog(ArchivesPanel.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File fichier = fileChooser.getSelectedFile();
                exporterEnPDF(table, fichier);
            }

            }
        });
        panelSud.add(ImprimerBouton);
        add(panelSud, BorderLayout.SOUTH);
        refreshTable(archivesUseCase.afficherArchives().getArchivesPaiements().stream().toList());
    }
    public void exporterEnPDF(JTable table, File fichier) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(fichier));
            document.open();

            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            for (int i = 0; i < table.getColumnCount(); i++) {
                pdfTable.addCell(new Phrase(table.getColumnName(i)));
            }

            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    pdfTable.addCell(value != null ? value.toString() : "");
                }
            }

            document.add(pdfTable);
            document.close();
            JOptionPane.showMessageDialog(null, "PDF généré avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la génération du PDF : " + e.getMessage());
        }
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
