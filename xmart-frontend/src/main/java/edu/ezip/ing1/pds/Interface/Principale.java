package edu.ezip.ing1.pds.Interface;


import javax.swing.*;

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
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        cardLayout = new CardLayout();

        mainPannel = new JPanel(cardLayout);
        setContentPane(mainPannel);

        mainPannel.add(accueuil(), "accueuil");
        mainPannel.add(panneauServices(), "services");

        //localService = new LocalService(new NetworkConfig());

        setResizable(false);
    }

    private JPanel accueuil() {

        JPanel pannelAccueuil = new JPanel(new BorderLayout());
        pannelAccueuil.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titre = new JLabel("BONJOUR ET BIENVENUE", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        pannelAccueuil.add(titre, BorderLayout.NORTH);


        JPanel pannel = new JPanel(new GridLayout(4, 1, 10, 10));

        Bouton1 = new JButton("GESTION DES PLACES");
        Bouton1.addActionListener(this::clicAccueuil);
        pannel.add(Bouton1);
        Bouton2 = new JButton("GESTION DES ABONNEMENTS ");
        Bouton2.addActionListener(this::clicAccueuil);
        pannel.add(Bouton2);
        Bouton3 = new JButton("GESTION DES SERVICES ET CALENDRIERS");
        Bouton3.addActionListener(this::clicAccueuil);
        pannel.add(Bouton3);
        Bouton4 = new JButton("QUITTER");
        Bouton4.addActionListener(this::clicAccueuil);
        pannel.add(Bouton4);

        pannelAccueuil.add(pannel, BorderLayout.CENTER);
        return pannelAccueuil;

    }


    private void clicAccueuil(ActionEvent e){
        if (e.getSource() == Bouton1 ){

        }
        else if (e.getSource() == Bouton2 ){

        }
        else if  (e.getSource() == Bouton3 ){
            cardLayout.show(mainPannel, "services");

        }
        else if (e.getSource() == Bouton4 ){
            System.exit(0);
        }

    }

    private JPanel panneauServices(){



        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);
        

      JPanel panneauGauche = new JPanel( new GridLayout(3, 1, 10, 10));

       JButton btnLaverie = new JButton("Laverie");
       btnLaverie.addActionListener(this::clicLaverie);

      panneauGauche.add(btnLaverie);
       JButton btnReparation = new JButton("Reparation");

      panneauGauche.add(btnReparation);
       JButton btnresa = new JButton("reservation zones");

      panneauGauche.add(btnresa);



        splitPane.setLeftComponent(panneauGauche);

        panneauDroite = new JPanel();
        panneauDroite.setLayout(new BorderLayout());
        panneauDroite.add(new JLabel("Sélectionnez une action à gauche."), BorderLayout.CENTER);
        splitPane.setRightComponent(panneauDroite);


        JPanel panneauS = new JPanel(new BorderLayout());
        panneauS.add(splitPane, BorderLayout.CENTER);

        return panneauS;

    }




    //La listenner qui va verifier mes locaux laverie quand j'appui sur le bouton
    private void clicLaverie(ActionEvent e){




}
        

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Principale().setVisible(true));
    }

}
