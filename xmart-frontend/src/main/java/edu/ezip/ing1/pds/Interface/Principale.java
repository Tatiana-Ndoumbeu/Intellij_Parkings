package edu.ezip.ing1.pds.Interface;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.ezip.ing1.pds.MainFrontEnd;
import edu.ezip.ing1.pds.MainFrontEndSwing;
import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.LocalLaveriesService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.stream.Collectors;

import static edu.ezip.ing1.pds.uiUtils.PlaceDeParkingViewModel.createPlaceDeParkingTableModel;

public class Principale extends JFrame {

    private JPanel mainPannel;
    private CardLayout cardLayout;

    private JButton Bouton1;
    private JButton Bouton2;
    private JButton Bouton3;
    private JButton Bouton4;
    ;

    private final String NIVEAU1 = "Niveau 1";
    private final String NIVEAU2 = "Niveau 2";
    private final String NIVEAU3 = "Niveau 3";

    private JPanel panneauDroite;

    public Principale() {
        super("IntelliJ Parking System");
        setSize(900, 600);
        setLocationRelativeTo(null);
       // setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        cardLayout = new CardLayout();

        mainPannel = new JPanel(cardLayout);
        setContentPane(mainPannel);

        mainPannel.add(accueuil(), "accueuil");
        mainPannel.add(panneauAttributions(), "Attributions");

        //localService = new LocalService(new NetworkConfig());

        setResizable(true);
    }

    private JPanel accueuil() {


        JPanel pannelAccueuil = new JPanel(new BorderLayout());
        pannelAccueuil.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pannelAccueuil.setBackground(Color.decode("#8d9496"));


        JLabel titre = new JLabel("BONJOUR ET BIENVENUE DANS INTELLIJ PARKING", JLabel.CENTER);
       // titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setFont(new Font("Garamond", Font.BOLD, 25));







        titre.setForeground(Color.BLUE);
        pannelAccueuil.add(titre, BorderLayout.NORTH);

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(Color.decode("#8d9496"));
        JLabel iconLabel = new JLabel(chargerIcone("/icons/logoFond.png", 280, 280)); // Charger l'image
        imagePanel.add(iconLabel); // Ajouter l'image centrée

        pannelAccueuil.add(imagePanel, BorderLayout.CENTER);



        //JPanel pannel = new JPanel(new GridLayout(4, 1, 10, 10));
        JPanel pannel = new JPanel();
        pannel.setBackground(Color.decode("#8d9496"));
        pannel.setLayout(new BoxLayout(pannel, BoxLayout.Y_AXIS));

        Bouton1 = new JButton("SE GARER", chargerIcone("/icons/attribution.png", 30, 30));
        pannel.add(Box.createVerticalStrut(10));
        Bouton1.addActionListener(this::clicAccueuil);
        Bouton1.setPreferredSize(new Dimension(250, 50));
        //Bouton1.setMaximumSize(new Dimension(200, 50));
        pannel.add(Bouton1);


        Bouton2 = new JButton("DASHBOARD ET SERVICES", chargerIcone("/icons/services.png", 30, 30));
        pannel.add(Box.createVerticalStrut(10));
        Bouton2.addActionListener(this::clicAccueuil);
        Bouton2.setPreferredSize(new Dimension(250, 50));
        pannel.add(Bouton2);

        Bouton3 = new JButton("CHIFFRES ET HISTORIQUE", chargerIcone("/icons/historique.png", 30, 30));
        pannel.add(Box.createVerticalStrut(10));
        Bouton3.addActionListener(this::clicAccueuil);
        Bouton3.setPreferredSize(new Dimension(200, 50));
        pannel.add(Bouton3);

        Bouton4 = new JButton("QUITTER", chargerIcone("/icons/quitter.png", 30, 30));
        pannel.add(Box.createVerticalStrut(10));
        Bouton4.addActionListener(this::clicAccueuil);
        Bouton4.setPreferredSize(new Dimension(250, 50));
        pannel.add(Bouton4);

        pannelAccueuil.add(pannel, BorderLayout.SOUTH);
        return pannelAccueuil;

    }
    private ImageIcon chargerIcone(String chemin, int largeur, int hauteur) {

        ImageIcon icon = new ImageIcon(getClass().getResource(chemin));
        Image image = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }


    private void clicAccueuil(ActionEvent e){
        if (e.getSource() == Bouton1 ){

            cardLayout.show(mainPannel, "Attributions");

        }
        else if (e.getSource() == Bouton2 ){

            SwingUtilities.invokeLater(() -> new MainFrontEndSwing().setVisible(true));

        }
        else if  (e.getSource() == Bouton3 ){
            cardLayout.show(mainPannel, "CHIFFRES ET HISTORIQUE");

        }
        else if (e.getSource() == Bouton4 ){
            System.exit(0);
        }

    }

    private JPanel panneauAttributions() {


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);
        splitPane.setOneTouchExpandable(true);


        JPanel panneauGauche = new JPanel(new GridLayout(4, 1, 10, 10));

        panneauGauche.setBackground(new Color(52, 152, 219));
        Font boutonFont = new Font("Arial", Font.BOLD, 18);

        JButton Bouton1 = new JButton("Attribution automatique");
        Bouton1.setFont(boutonFont);
        Bouton1.setBackground(new Color(105, 0, 31));
        Bouton1.addActionListener(this::clicrapide);
        panneauGauche.add(Bouton1);

        JButton Bouton2 = new JButton("Attribution personnalisée");
        Bouton2.setFont(boutonFont);
        Bouton2.addActionListener(this::clicrapide);
        Bouton2.setBackground(new Color(105, 0, 31));
        panneauGauche.add(Bouton2);

        JButton Bouton3 = new JButton("Attribution zones spéciales");
        Bouton3.setFont(boutonFont);
        Bouton3.addActionListener(this::clicrapide);
        Bouton3.setBackground(new Color(105, 0, 31));
        panneauGauche.add(Bouton3);

        JButton Bouton4 = new JButton("Retour");
        Bouton4.setFont(boutonFont);
        Bouton4.setBackground(new Color(105, 0, 31));
        Bouton4.addActionListener(e -> {
            cardLayout.show(mainPannel, "accueuil");
        });
        panneauGauche.add(Bouton4);


        splitPane.setLeftComponent(panneauGauche);

        panneauDroite = new JPanel(new BorderLayout());
        JPanel souspanneaudroite = new JPanel(new BorderLayout());
        // panneauDroite.setBackground(new Color(41, 128, 185));
        // panneauDroite.setLayout(new BorderLayout());
        JPanel tablePanel1 = createTablePanel(NIVEAU1);
        JPanel tablePanel2 = createTablePanel(NIVEAU2);
        JPanel tablePanel3 = createTablePanel(NIVEAU3);
        souspanneaudroite.add(tablePanel1, BorderLayout.CENTER);

        JPanel panneausud = new JPanel(new FlowLayout());
        JButton niv1Button = new JButton("Niveau 1");
        //niv1Button.addActionListener(e -> updateTablePanel(NIVEAU1));
        niv1Button.addActionListener(e ->
        {    souspanneaudroite.removeAll();
            souspanneaudroite.add(tablePanel1, BorderLayout.CENTER);
            souspanneaudroite.revalidate();
            souspanneaudroite.repaint();});

        JButton niv2Button = new JButton("Niveau 2");
        //niv2Button.addActionListener(e -> updateTablePanel(NIVEAU2));
        niv2Button.addActionListener(e ->
        {   souspanneaudroite.removeAll();
            souspanneaudroite.add(tablePanel2, BorderLayout.CENTER);
            souspanneaudroite.revalidate();
            souspanneaudroite.repaint();});

        JButton niv3Button = new JButton("Niveau 3");
        niv3Button.addActionListener(e ->
        {   souspanneaudroite.removeAll();
            souspanneaudroite.add(tablePanel3, BorderLayout.CENTER);
            souspanneaudroite.revalidate();
            souspanneaudroite.repaint();});

        panneausud.add(niv1Button);
        panneausud.add(niv2Button);
        panneausud.add(niv3Button);
        panneauDroite.add(panneausud, BorderLayout.SOUTH);
        panneauDroite.add(souspanneaudroite, BorderLayout.CENTER);

        splitPane.setRightComponent(panneauDroite);

        JPanel panneauS = new JPanel(new BorderLayout());
        panneauS.add(splitPane, BorderLayout.CENTER);

        return panneauS;

    }
    private JPanel createTablePanel(String niveau){
            JPanel panel = new JPanel(new BorderLayout());
            JTable jtable = new JTable();
            JScrollPane scrollPane = new JScrollPane(jtable);

            switch (niveau) {
                case NIVEAU1:

                    jtable.setModel(niveauxParkingModel("A"));
                    break;
                case NIVEAU2:
                    jtable.setModel(niveauxParkingModel("B"));
                    break;
                case NIVEAU3:
                    jtable.setModel(niveauxParkingModel("C"));
                    break;
                default:
                    break;

        }
            panel.add(scrollPane, BorderLayout.CENTER);
            return panel;
       }







    private DefaultTableModel niveauxParkingModel(String e) {
        String[] columns = {"Place", "Type de place", "Statut"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (int i = 1; i < 4; i++) {
            model.addRow(new Object[]{e + i, "VIP", "Libre"});
        }
        for (int i = 4; i < 9; i++) {
            model.addRow(new Object[]{e + i, "PMR", "Libre"});
        }
        for (int i = 9; i < 13; i++) {
            model.addRow(new Object[]{e + i, "Electrique", "Libre"});
        }
        for (int i = 13; i < 31; i++) {
            model.addRow(new Object[]{e + i, "Classique", "Libre"});
        }

        return model;
    }



private void clicrapide(ActionEvent event){

}
    private void updateTablePanel(String niveau) {
        panneauDroite.remove(0);
        JPanel tablePanel = createTablePanel(niveau);
        panneauDroite.add(tablePanel, BorderLayout.CENTER);
        panneauDroite.revalidate();
        panneauDroite.repaint();
    }

    

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        } // pour faire plus beau
        SwingUtilities.invokeLater(() -> new Principale().setVisible(true));
    }}

