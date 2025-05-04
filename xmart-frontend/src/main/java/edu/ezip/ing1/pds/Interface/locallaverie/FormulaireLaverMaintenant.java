package edu.ezip.ing1.pds.Interface.locallaverie;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.usecase.AbonnementUseCase;
import edu.ezip.ing1.pds.usecase.MecanicienUseCase;
import edu.ezip.ing1.pds.usecase.PersonneUseCase;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class FormulaireLaverMaintenant {
    private final PersonneUseCase personneUseCase;
    private final AbonnementUseCase abonnementUseCase;
    private static Personnes personnes = new Personnes();
    private static Abonnements abonnements = new Abonnements();

    public  FormulaireLaverMaintenant(JFrame parent, PersonneUseCase personneUseCase, AbonnementUseCase abonnementUseCase) {
        JDialog dialog = new JDialog(parent, "Formulaire Lavage", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));
        this.personneUseCase = personneUseCase;
        this.abonnementUseCase = abonnementUseCase;

        JPanel panelChoix = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelChoix.add(new JLabel("Type d'utilisateur :"));
        String[] choixAbonnement = {"Abonné", "Visiteur"};
        JComboBox<String> comboType = new JComboBox<>(choixAbonnement);
        panelChoix.add(comboType);
        dialog.add(panelChoix, BorderLayout.NORTH);

        JPanel panelCardLayout = new JPanel(new CardLayout());

        JPanel panelAbonne = new JPanel(new GridLayout(2, 2, 5, 5));
        panelAbonne.setBorder(BorderFactory.createTitledBorder("Informations Abonné"));
        JTextField champMailAbonne = new JTextField();
        panelAbonne.add(new JLabel("Email :"));
        panelAbonne.add(champMailAbonne);


        JPanel panelVisiteur = new JPanel(new GridLayout(8, 2, 5, 5));
        panelVisiteur.setBorder(BorderFactory.createTitledBorder("Informations Visiteur"));
        JTextField champNom = new JTextField();
        JTextField champPrenom = new JTextField();
        JTextField champMailVisiteur = new JTextField();
        JTextField champTelephoneVisiteur = new JTextField();
        JComboBox<String> comboVehicule = new JComboBox<>(new String[]{"Cycle", "Monospace", "4X4", "Van", "Truck et autres"});
        JComboBox<String> comboTypeLavage = new JComboBox<>(new String[]{"Lavage au rouleau", "Lavage haute pression", "Lavage à la main"});
        JComboBox<String> comboOption = new JComboBox<>(new String[]{"Aucune", "Nettoyage intérieur", "Pression pneus"});

        panelVisiteur.add(new JLabel("Nom :"));
        panelVisiteur.add(champNom);
        panelVisiteur.add(new JLabel("Prénom :"));
        panelVisiteur.add(champPrenom);
        panelVisiteur.add(new JLabel("Email :"));
        panelVisiteur.add(champMailVisiteur);
        panelVisiteur.add(new JLabel("Telephone :"));
        panelVisiteur.add(champTelephoneVisiteur);
        panelVisiteur.add(new JLabel("Type de véhicule :"));
        panelVisiteur.add(comboVehicule);
        panelVisiteur.add(new JLabel("Type de lavage :"));
        panelVisiteur.add(comboTypeLavage);

        panelVisiteur.add(new JLabel("Options supplémentaires :"));
        JPanel panelOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox OptInterieur = new JCheckBox("Nettoyage Intérieur");
        JCheckBox OptPneus = new JCheckBox("Pression des pneus");
        panelOptions.add(OptInterieur);
        panelOptions.add(OptPneus);
        panelVisiteur.add(panelOptions);

        panelCardLayout.add(panelAbonne, "Abonné");
        panelCardLayout.add(panelVisiteur, "Visiteur");
        dialog.add(panelCardLayout, BorderLayout.CENTER);

        comboType.addActionListener(e -> {
            CardLayout cl = (CardLayout) (panelCardLayout.getLayout());
            cl.show(panelCardLayout, (String) comboType.getSelectedItem());
        });

        JButton boutonValider = new JButton("Valider");
        boutonValider.addActionListener(e -> {
            String typeVehicule = (String) comboVehicule.getSelectedItem();
            String typeLavage = (String) comboTypeLavage.getSelectedItem();
            String option = (String) comboOption.getSelectedItem();
            String type = (String) comboType.getSelectedItem();
            String nom = (String) champNom.getText().trim();
            String prenom = (String) champPrenom.getText().trim();
            String telephone = (String) champTelephoneVisiteur.getText().trim();
            String email = (String) champMailAbonne.getText().trim().toLowerCase();

            int supplementOption = 0;
            if (OptInterieur.isSelected()) supplementOption += 5;
            if (OptPneus.isSelected()) supplementOption += 5;



            if (type.equals("Abonné")) {
                String TYPE = "";
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Veuillez entrer votre email.");
                    return;
                }
                ArrayList<String> emails = new ArrayList<>();
                ArrayList<String> ids = new ArrayList<>();
                try {
                    personnes = personneUseCase.afficherPersonnes();
                    if (personnes != null && personnes.getPersonnes() != null) {
                        for (Personne perso : personnes.getPersonnes()) {
                            emails.add(perso.getMail());
                            ids.add(perso.getIdPersonne());
                        }
                    }
                }
                catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }


                for (int i = 0; i < emails.size(); i++) {
                    if (emails.get(i).equalsIgnoreCase(email.trim())) {
                        String idPersonne = ids.get(i);
                        try {
                            abonnements = abonnementUseCase.findAbonnementX(idPersonne);
                            if (abonnements != null && abonnements.getAbonnements() != null) {
                                for (Abonnement abo : abonnements.getAbonnements()) {
                                    TYPE = abo.getTypeAbonnement().trim();
                                     break;

                                }
                            }
                        }
                        catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                         }

                        JOptionPane.showMessageDialog(dialog, "vous avez un abonnement ." +TYPE);

                        return;
                    }

                }
                JOptionPane.showMessageDialog(dialog, "email non reconnu.");

                // TODO
                //  Vérifie l'email dans la personne liée à abonnement
                //  verifie quel type de place de son abonnement
                //  verifie si c'est VOITURE Ou moto
                //  et définis le prix
                //en attendant maryline
                int prix = 20; //exemple

            } else {
                if (nom.isEmpty() || prenom.isEmpty()|| telephone.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Champs manquants.");
                    return;
                }
                int prixBase = switch (typeLavage) {
                    case "Lavage à la main" -> 50;
                    case "Lavage haute pression" -> 40;
                    default -> 30;
                };
                int TypeVehicule = switch (typeVehicule) {
                    case "Cycle" -> 0;
                    case "Monospace" -> 10;
                    case "4X4" -> 20;
                    case "Van" -> 30;
                    case "Truck et autres" -> 40;
                    default -> 0;
                };

                int prix = prixBase + TypeVehicule + supplementOption;

                int choix = JOptionPane.showOptionDialog(dialog,
                        "Type d'utilisateur : " + type + "\n"
                                + "Nom : " + nom + "\n"
                                +"Prénom : " + prenom + "\n"
                                + "Telephone : " + telephone + "\n"
                                + "Email : " + email + "\n"
                                + "Type de véhicule : " + comboVehicule.getSelectedItem() + "\n"
                                + "Type de lavage : " + comboTypeLavage.getSelectedItem() + "\n"
                                + "Option : " + comboOption.getSelectedItem() + "\n"
                                + "Prix : " + prix + " €\n\n"
                                + "Souhaitez-vous continuer ?",
                        "Confirmation de paiement",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Payer "+prix+" €", "Annuler"},
                        "Payer");
                //je dois aussi inscrire le paiement dans la bd

                if (choix == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(dialog, "Paiement effectué. Merci !");
                    dialog.dispose();
                }

            }


        });

        JPanel panelSud = new JPanel();
        panelSud.add(boutonValider);
        dialog.add(panelSud, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

}
