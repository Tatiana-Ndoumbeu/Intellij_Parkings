package edu.ezip.ing1.pds.Interface.locallaverie;

import com.sun.jdi.IntegerValue;
import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.usecase.*;
import org.apache.commons.lang3.ObjectUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class FormulaireLaverMaintenant {
    private final PersonneUseCase personneUseCase;
    private final AbonnementUseCase abonnementUseCase;
    private final LocalLaverieUseCase localLaverieUseCase;
    private final ArchivesUseCase archivesUseCase;
    private static Personnes personnes = new Personnes();
    private static LocalLaveries localLaveries = new LocalLaveries();
    private static Abonnements abonnements = new Abonnements();


    public  FormulaireLaverMaintenant(JFrame parent, PersonneUseCase personneUseCase, AbonnementUseCase abonnementUseCase, LocalLaverieUseCase localLaverieUseCase, ArchivesUseCase archivesUseCase) {
        JDialog dialog = new JDialog(parent, "Formulaire Lavage", true);
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));
        this.personneUseCase = personneUseCase;
        this.abonnementUseCase = abonnementUseCase;
        this.localLaverieUseCase = localLaverieUseCase;
        this.archivesUseCase = archivesUseCase;


        ArrayList<Integer> numLocaux = new ArrayList<>();
        try {
            localLaveries = localLaverieUseCase.afficherLocauxDispo();
            if (localLaveries != null && localLaveries.getLocalLaveries() != null) {
                for (LocalLaverie loc : localLaveries.getLocalLaveries()) {
                    numLocaux.add(loc.getNumLocalL());
                }

            }
        } catch (IOException | InterruptedException ie) {
            ie.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des locaux.");
        }
        int[] array = new int[numLocaux.size()];
        for(int i = 0; i < array.length; i++) {
            array[i] = numLocaux.get(i);
        }
        JComboBox<Integer> comboLocaux = new JComboBox<>();
        for (int numerolocal : array) {
            comboLocaux.addItem(numerolocal);
        }



        JPanel panelChoix = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelChoix.add(new JLabel("Type d'utilisateur :"));
        String[] choixAbonnement = {"Abonné", "Visiteur"};
        JComboBox<String> comboType = new JComboBox<>(choixAbonnement);
        panelChoix.add(comboType);
        panelChoix.add(new JLabel("Local à utiliser :"));
        panelChoix.add(comboLocaux);
        dialog.add(panelChoix, BorderLayout.NORTH);

        JPanel panelCardLayout = new JPanel(new CardLayout());
        JPanel panelPremium = creerPanelPremium("Premium", (int) comboLocaux.getSelectedItem(), "Abonne", "Premium");
        JPanel panelStandard = creerPanelPremium("Standard", (int) comboLocaux.getSelectedItem(),"Abonne", "Standard");


        JPanel panelAbonne = new JPanel(new GridLayout(2, 2, 5, 5));
        panelAbonne.setBorder(BorderFactory.createTitledBorder("Informations Abonné"));
        JTextField champMailAbonne = new JTextField();
        panelAbonne.add(new JLabel("Email :"));
        panelAbonne.add(champMailAbonne);

 /// //PANNEL VISITEUR/////
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
        panelCardLayout.add(panelPremium, "Premium");
        panelCardLayout.add(panelStandard, "Standard");
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
            String email = (String) champMailAbonne.getText().toLowerCase().trim();
            //String Local = (String) comboLocaux.getSelectedItem();
            int LocalInt = (int) comboLocaux.getSelectedItem();

            int supplementOption = 0;
            if (OptInterieur.isSelected()) supplementOption += 5;
            if (OptPneus.isSelected()) supplementOption += 5;



            if (type.equals("Abonné")) {
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Veuillez entrer votre email.");
                    return;
                }
                try {
                    personnes = personneUseCase.afficherPersonnes();
                    if (personnes != null && personnes.getPersonnes() != null) {
                        for (Personne perso : personnes.getPersonnes()) {
                            if (perso.getMail().toLowerCase().trim().equals(email)) {
                                Abonnement abonnement = abonnementUseCase.findAbonnementX(perso);

                                if (abonnement != null) {
                                    String typeAbonnement = abonnement.getTypeAbonnement();
                                    CardLayout cl = (CardLayout) (panelCardLayout.getLayout());

                                    if (typeAbonnement.trim().equalsIgnoreCase("Premium")) {
                                        cl.show(panelCardLayout, "Premium");

                                        JOptionPane.showMessageDialog(dialog, "Vous avez un abonnement " + typeAbonnement +"\nle service laverie vous est offert en intégralité");
                                    } else if (typeAbonnement.trim().equalsIgnoreCase("Standard")) {
                                        cl.show(panelCardLayout, "Standard");

                                        JOptionPane.showMessageDialog(dialog, "Vous avez un abonnement " + typeAbonnement +"\n sélectionnez vos choix");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(dialog, "Aucun abonnement trouvé pour cette personne.");
                                }
                                return;
                            }
                        }
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Erreur lors de la récupération des données.");
                }
                JOptionPane.showMessageDialog(dialog, "Email non reconnu.");


            } else {
                if (nom.isEmpty() || prenom.isEmpty()|| telephone.isEmpty()|| !telephone.matches("^0\\d{9}$")) {
                    JOptionPane.showMessageDialog(dialog, "Champs manquants ou incorrects.");
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
                                + "Option : " + comboOption.getSelectedItem() + "\n\n"
                                + "Local Laverie : " + LocalInt + "\n\n"
                                + "Prix : " + prix + " €\n\n"
                                + "Souhaitez-vous continuer ?",
                        "Confirmation de paiement",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Payer "+prix+" €", "Annuler"},
                        "Payer");


                if (choix == JOptionPane.YES_OPTION) {

                    archiverPaiement(nom, prenom, prix, "Laverie", LocalInt);

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
    private JPanel creerPanelPremium(String TypeClient, int local, String nom, String prenom ) {
        JPanel panelPremium = new JPanel(new GridLayout(4, 2, 10, 10));
        panelPremium.setBorder(BorderFactory.createTitledBorder("Formulaire Client " +TypeClient));

        JComboBox<String> comboTypeLavage = new JComboBox<>(new String[]{"Lavage au rouleau", "Lavage haute pression", "Lavage à la main"});
        JComboBox<String> comboSenteur = new JComboBox<>(new String[]{"Aucune", "Orange", "Fleurs d'été", "Fleurs de cerisier", "Vanille"});

        JCheckBox checkInterieur = new JCheckBox("Nettoyage intérieur");
        JCheckBox checkPneus = new JCheckBox("Pression des pneus");

        panelPremium.add(new JLabel("Type de lavage :"));
        panelPremium.add(comboTypeLavage);

        panelPremium.add(new JLabel("Senteur (si intérieur coché) :"));
        panelPremium.add(comboSenteur);

        panelPremium.add(new JLabel("Options supplémentaires :"));
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.add(checkInterieur);
        optionsPanel.add(checkPneus);
        panelPremium.add(optionsPanel);

        JButton boutonLaver = new JButton("Laver maintenant");
        if (TypeClient.trim().equalsIgnoreCase("Premium")) {
            boutonLaver.addActionListener(e -> {
                String typeLavage = (String) comboTypeLavage.getSelectedItem();
                String senteur = (String) comboSenteur.getSelectedItem();
                boolean interieur = checkInterieur.isSelected();
                boolean pneus = checkPneus.isSelected();

                int prix = 0;
                String recap = "Résumé  lavage Premium :\n"
                        + "Type de lavage : " + typeLavage + "\n"
                        + "Nettoyage intérieur : " + (interieur ? "Oui" : "Non") + "\n"
                        + "Pression pneus : " + (pneus ? "Oui" : "Non") + "\n"
                        + "Senteur : " + senteur + "\n"
                        + "Prix : " + prix + " €";

                archiverPaiement(nom, prenom, prix, "Laverie", local);
                JOptionPane.showMessageDialog(panelPremium, recap);


            });
        }
        else {
            boutonLaver.addActionListener(e -> {
                String typeLavage = (String) comboTypeLavage.getSelectedItem();
                String senteur = (String) comboSenteur.getSelectedItem();
                boolean interieur = checkInterieur.isSelected();
                boolean pneus = checkPneus.isSelected();

                int prixBase = switch (typeLavage) {
                    case "Lavage haute pression" -> 10;
                    case "Lavage à la main" -> 20;
                    default -> 0;
                };

                int prixOptions = 0;
                if (interieur) prixOptions += 5;
                if (pneus) prixOptions += 5;
                if (!senteur.equalsIgnoreCase("Aucune")) prixOptions += 3;

                int prixTotal = prixBase + prixOptions;

                String recap = "Résumé du lavage Standard :\n"
                        + "Type de lavage : " + typeLavage + "\n"
                        + "Nettoyage intérieur : " + (interieur ? "Oui" : "Non") + "\n"
                        + "Pression pneus : " + (pneus ? "Oui" : "Non") + "\n"
                        + "Senteur : " + senteur + "\n\n"
                        + "Local Laverie : " + local + "\n\n"
                        + "Prix total : " + prixTotal + " €";

                int choix = JOptionPane.showOptionDialog(null,
                        recap + "\nSouhaitez-vous confirmer le lavage ?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Payer " + prixTotal + " € et Laver", "Annuler"},
                        "Payer");

                if (choix == JOptionPane.YES_OPTION) {

                   archiverPaiement(nom, prenom, prixTotal, "Laverie", local);

                    JOptionPane.showMessageDialog(panelPremium, "[ARCHIVE PREMIUM] " + recap);
                }
            });}

        panelPremium.add(boutonLaver, BorderLayout.SOUTH);


        return panelPremium;
    }
    private void archiverPaiement(String nom, String prenom, int montant, String service, int local) {
        ArchivesPaiement paiement = new ArchivesPaiement();
        paiement.setNom(nom);
        paiement.setPrenom(prenom);
        paiement.setMontant(montant);
        paiement.setService(service);
        paiement.setDate(LocalDate.now().toString());

        try {
            archivesUseCase.createArchive(paiement);

            LocalLaverie localSolicite = new LocalLaverie();
            localSolicite.setDisponibilite(false);
            localSolicite.setNumLocalL(local);
            localLaverieUseCase.modifierDisponibilite(localSolicite);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement ou la mise à jour.");
        }
    }

}
