package edu.ezip.ing1.pds.Interface;


import javax.swing.*;

import edu.ezip.ing1.pds.MainFrontEndSwing;
import edu.ezip.ing1.pds.business.dto.LocalLaverie;
import edu.ezip.ing1.pds.business.dto.LocalLaveries;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.stream.Collectors;

public class Principale extends JFrame {

    private JPanel mainPannel;
    private CardLayout cardLayout;

    private JButton Bouton1;
    private JButton Bouton2;
    private JButton Bouton3;
    private JButton Bouton4;

    private JPanel panneauDroite;

    public Principale() {
        super("Principale");
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
/*
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("backgroundImage.png"));
                Image backgroundImage = backgroundIcon.getImage();
                Image resizedImage = backgroundImage.getScaledInstance(150, 100, Image.SCALE_SMOOTH);

                g.drawImage(backgroundImage, 0, 0, this);
            }
        };

        pannelAccueuil.setLayout(new BorderLayout());*/
        pannelAccueuil.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JLabel titre = new JLabel("BONJOUR ET BIENVENUE DANS INTELLIJ PARKING", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        pannelAccueuil.add(titre, BorderLayout.CENTER);



        //JPanel pannel = new JPanel(new GridLayout(4, 1, 10, 10));
        JPanel pannel = new JPanel();
        pannel.setLayout(new BoxLayout(pannel, BoxLayout.Y_AXIS));

        Bouton1 = new JButton("ATTRIBUTION RAPIDE", chargerIcone("/icons/attribution.png", 30, 30));
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

       // pannelAccueuil.repaint();
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

    private JPanel panneauAttributions(){



        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);
        splitPane.setOneTouchExpandable(true);
        

      JPanel panneauGauche = new JPanel( new GridLayout(4, 1, 10, 10));

      panneauGauche.setBackground(new Color(52, 152, 219));
        Font boutonFont = new Font("Arial", Font.BOLD, 18);

       JButton Bouton1 = new JButton("Attribution sans identification");
        Bouton1.setFont(boutonFont);
        Bouton1.setBackground(new Color(128, 0, 32));
        Bouton1.addActionListener(this::clicrapide);
      panneauGauche.add(Bouton1);

       JButton Bouton2 = new JButton("Attribution avec identification");
        Bouton2.setFont(boutonFont);
        Bouton2.addActionListener(this::clicrapide);
        Bouton2.setBackground(new Color(128, 0, 32));
        panneauGauche.add(Bouton2);

       JButton Bouton3 = new JButton("Attribution zones spéciales");
        Bouton3.setFont(boutonFont);
        Bouton3.addActionListener(this::clicrapide);
        Bouton3.setBackground(new Color(128, 0, 32));
        panneauGauche.add(Bouton3);

        JButton Bouton4 = new JButton("Retour");
        Bouton4.setFont(boutonFont);
        Bouton4.setBackground(new Color(128, 0, 32));
        Bouton4.addActionListener(e->{cardLayout.show(mainPannel, "accueuil");});
        panneauGauche.add(Bouton4);






        splitPane.setLeftComponent(panneauGauche);

        panneauDroite = new JPanel();
        panneauDroite.setBackground(new Color(41, 128, 185));
        panneauDroite.setLayout(new BorderLayout());
        panneauDroite.add(new JLabel("Sélectionnez une action à gauche."), BorderLayout.CENTER);
        splitPane.setRightComponent(panneauDroite);


        JPanel panneauS = new JPanel(new BorderLayout());
        panneauS.add(splitPane, BorderLayout.CENTER);

        return panneauS;

    }




    //La listenner qui va verifier mes locaux laverie quand j'appui sur le bouton
    private void clicrapide(ActionEvent e){




}
        

    

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        } // pour faire plus beau
        SwingUtilities.invokeLater(() -> new Principale().setVisible(true));
    }

}
