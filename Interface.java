import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Classe principale de l'interface graphique.
 * Gère l'écran de connexion/création de compte,
 * puis l'interface principale avec panneau historique groupé par date.
 */
public class Interface {

    private static Utilisateur utilisateurConnecte = null;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    // =========================================================
    // UTILITAIRE DATE
    // =========================================================
    private static String maintenant() {
        return LocalDateTime.now().format(FORMATTER);
    }

    /**
     * Convertit une date stockée (yyyy-MM-dd HH:mm) en label de groupe :
     * "Aujourd'hui", "Hier", ou la date formatée.
     */
    private static String labelGroupe(String dateStr) {
        try {
            // ✅ Ignorer les séparateurs
            if (dateStr.startsWith("0000-00-00")) {
                return null; // Pas de groupe pour les séparateurs
            }

            LocalDate date = LocalDate.parse(dateStr.substring(0, 10));
            LocalDate today = LocalDate.now();
            if (date.equals(today)) return "Aujourd'hui";
            if (date.equals(today.minusDays(1))) return "Hier";
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return "Date inconnue";
        }
    }

    /**
     * Reconstruit le modèle de la liste avec les séparateurs de date.
     * Format d'une entrée : "Nom|quantité|yyyy-MM-dd HH:mm"
     * Dans le modèle on insère des lignes spéciales préfixées par "##GROUP##".
     */
    private static void reconstruireModele(DefaultListModel<String> modele, List<String> historique) {
        modele.clear();

        // Regrouper par date (jour)
        LinkedHashMap<String, List<String>> groupes = new LinkedHashMap<>();
        for (String entree : historique) {
            String[] parts = entree.split("\\|");
            String dateKey = (parts.length >= 3) ? parts[2].substring(0, 10) : "0000-00-00";
            groupes.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(entree);
        }

        // Trier par date décroissante (plus récent en haut)
        List<Map.Entry<String, List<String>>> entries = new ArrayList<>(groupes.entrySet());
        entries.sort((a, b) -> b.getKey().compareTo(a.getKey()));

        for (Map.Entry<String, List<String>> entry : entries) {
            String label = labelGroupe(entry.getKey());

            // ✅ Ne pas ajouter de groupe si c'est null (séparateur)
            if (label != null) {
                modele.addElement("##GROUP##" + label);
            }

            // Entrées du groupe
            for (String e : entry.getValue()) {
                modele.addElement(e);
            }
        }
    }

    // =========================================================
    // ÉCRAN DE LOGIN
    // =========================================================
    public static void afficherLogin() {
        JFrame loginFrame = new JFrame("Connexion – Gestion nutritionnelle");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(420, 320);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        loginFrame.getContentPane().setBackground(new Color(245, 245, 245));
        loginFrame.setLayout(new BorderLayout());

        JLabel titre = new JLabel("Gestion d'apports nutritifs", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 18));
        titre.setForeground(new Color(50, 50, 50));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        loginFrame.add(titre, BorderLayout.NORTH);

        JPanel formulaire = new JPanel(new GridBagLayout());
        formulaire.setBackground(new Color(245, 245, 245));
        formulaire.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formulaire.add(new JLabel("Identifiant :"), gbc);
        JTextField champIdentifiant = new JTextField(18);
        gbc.gridx = 1; gbc.weightx = 1;
        formulaire.add(champIdentifiant, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formulaire.add(new JLabel("Mot de passe :"), gbc);
        JPasswordField champMotDePasse = new JPasswordField(18);
        gbc.gridx = 1; gbc.weightx = 1;
        formulaire.add(champMotDePasse, gbc);

        JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(200, 50, 50));
        messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formulaire.add(messageLabel, gbc);

        loginFrame.add(formulaire, BorderLayout.CENTER);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        boutons.setBackground(new Color(245, 245, 245));

        JButton btnConnexion = new JButton("Se connecter");
        styliserBouton(btnConnexion, new Color(102, 204, 102));
        JButton btnCreer = new JButton("Créer un compte");
        styliserBouton(btnCreer, new Color(100, 160, 220));

        boutons.add(btnConnexion);
        boutons.add(btnCreer);
        loginFrame.add(boutons, BorderLayout.SOUTH);

        ActionListener actionConnexion = e -> {
            String id = champIdentifiant.getText().trim();
            String mdp = new String(champMotDePasse.getPassword()).trim();
            if (id.isEmpty() || mdp.isEmpty()) {
                messageLabel.setText("Veuillez remplir tous les champs.");
                return;
            }
            if (GestionUtilisateurs.connecter(id, mdp)) {
                utilisateurConnecte = new Utilisateur(id);
                utilisateurConnecte.setValider(true);
                loginFrame.dispose();
                afficherInterfacePrincipale();
            } else {
                messageLabel.setText("Identifiant ou mot de passe incorrect.");
                champMotDePasse.setText("");
            }
        };

        btnConnexion.addActionListener(actionConnexion);
        champMotDePasse.addActionListener(actionConnexion);

        btnCreer.addActionListener(e -> {
            String id = champIdentifiant.getText().trim();
            String mdp = new String(champMotDePasse.getPassword()).trim();
            if (id.isEmpty() || mdp.isEmpty()) {
                messageLabel.setText("Veuillez remplir tous les champs.");
                return;
            }
            if (mdp.length() < 4) {
                messageLabel.setText("Mot de passe trop court (min. 4 caractères).");
                return;
            }
            if (GestionUtilisateurs.utilisateurExiste(id)) {
                messageLabel.setForeground(new Color(200, 50, 50));
                messageLabel.setText("Cet identifiant est déjà pris.");
                return;
            }
            if (GestionUtilisateurs.creerCompte(id, mdp)) {
                messageLabel.setForeground(new Color(50, 150, 50));
                messageLabel.setText("Compte créé ! Vous pouvez vous connecter.");
                champMotDePasse.setText("");
            } else {
                messageLabel.setForeground(new Color(200, 50, 50));
                messageLabel.setText("Erreur lors de la création du compte.");
            }
        });

        loginFrame.setVisible(true);
        champIdentifiant.requestFocusInWindow();
    }

    // =========================================================
    // INTERFACE PRINCIPALE
    // =========================================================
    private static void afficherInterfacePrincipale() {
        JFrame accueil = new JFrame(
                "Application de gestion d'apports nutritifs – " + utilisateurConnecte.getIdentifiant());
        accueil.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        accueil.setExtendedState(JFrame.MAXIMIZED_BOTH);
        accueil.setLayout(new BorderLayout());
        accueil.getContentPane().setBackground(new Color(245, 245, 245));

        // ===== PANNEAU GAUCHE : HISTORIQUE =====
        JPanel panneauHistorique = new JPanel(new BorderLayout());
        panneauHistorique.setPreferredSize(new Dimension(220, 0));
        panneauHistorique.setBackground(new Color(220, 225, 235));
        panneauHistorique.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                "Historique de " + utilisateurConnecte.getIdentifiant()));

        DefaultListModel<String> modeleHistorique = new DefaultListModel<>();
        reconstruireModele(modeleHistorique, utilisateurConnecte.getHistorique());

        JList<String> listeHistorique = new JList<>(modeleHistorique);
        listeHistorique.setBackground(new Color(235, 238, 245));
        listeHistorique.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeHistorique.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // ===== RENDERER : séparateurs de date en gris, ingrédients normaux =====
        listeHistorique.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {

                String s = value.toString();

                if (s.startsWith("##GROUP##")) {
                    // Ligne de groupe : label de date en gris
                    String label = s.substring("##GROUP##".length());
                    JLabel lbl = new JLabel(label);
                    lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
                    lbl.setForeground(new Color(130, 130, 130));
                    lbl.setBackground(new Color(210, 215, 228));
                    lbl.setOpaque(true);
                    lbl.setBorder(BorderFactory.createEmptyBorder(6, 8, 2, 4));
                    return lbl;
                }

                // Ligne d'ingrédient normale
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String[] parts = s.split("\\|");
                if (parts.length >= 3) {
                    // Affichage : "  Pomme – 150g"  (heure en petite police grise via HTML)
                    String heure = parts[2].length() >= 16 ? parts[2].substring(11, 16) : "";
                    setText("<html>&nbsp;&nbsp;" + parts[0] + " – " + parts[1] + "g"
                            + " <span style='color:#aaaaaa; font-size:10px;'>(" + heure + ")</span></html>");
                } else if (parts.length == 2) {
                    setText("  " + parts[0] + " – " + parts[1] + "g");
                } else {
                    setText("  " + s);
                }

                // Empêcher la sélection des lignes de groupe
                if (!isSelected) {
                    setForeground(new Color(40, 40, 40));
                    setBackground(new Color(235, 238, 245));
                }
                setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
                return this;
            }
        });

        // Empêcher la sélection des lignes "##GROUP##"
        listeHistorique.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int idx = listeHistorique.getSelectedIndex();
                if (idx >= 0 && modeleHistorique.getElementAt(idx).startsWith("##GROUP##")) {
                    listeHistorique.clearSelection();
                }
            }
        });

        JScrollPane scrollHistorique = new JScrollPane(listeHistorique);
        scrollHistorique.setBorder(null);
        panneauHistorique.add(scrollHistorique, BorderLayout.CENTER);

        // Bouton supprimer
        JButton btnSupprimerHistorique = new JButton("Supprimer");
        btnSupprimerHistorique.setBackground(new Color(220, 100, 100));
        btnSupprimerHistorique.setForeground(Color.WHITE);
        btnSupprimerHistorique.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnSupprimerHistorique.addActionListener(e -> {
            String selectionne = listeHistorique.getSelectedValue();
            if (selectionne != null && !selectionne.startsWith("##GROUP##")) {
                utilisateurConnecte.supprimerDeHistorique(selectionne);
                reconstruireModele(modeleHistorique, utilisateurConnecte.getHistorique());
            }
        });

        JPanel sudHistorique = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sudHistorique.setBackground(new Color(220, 225, 235));
        sudHistorique.add(btnSupprimerHistorique);
        panneauHistorique.add(sudHistorique, BorderLayout.SOUTH);

        // ===== ZONE CENTRALE =====
        JPanel zoneCentrale = new JPanel(new BorderLayout());
        zoneCentrale.setBackground(new Color(245, 245, 245));

        // ===== BASE DE DONNÉES =====
        JPanel base = new JPanel(new FlowLayout(FlowLayout.LEFT));
        base.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                "Ingrédients de la base de données"));
        base.setBackground(new Color(230, 230, 230));

        // ===== COMBOBOX DYNAMIQUE =====
        ArrayList<String> lesNoms = Ingredient.obtenirTousLesNomsIngreidents();
        JComboBox<String> recherche = new JComboBox<>(lesNoms.toArray(new String[0]));
        recherche.setEditable(true);
        recherche.setSelectedIndex(-1);

        JPanel ligneBase = new JPanel(new BorderLayout());
        ligneBase.setPreferredSize(new Dimension(600, 40));
        ligneBase.setBackground(new Color(230, 230, 230));

        JButton plus = new JButton("+");
        plus.setBackground(new Color(200, 200, 200));
        ligneBase.add(plus, BorderLayout.WEST);
        ligneBase.add(recherche, BorderLayout.CENTER);
        base.add(ligneBase);

        // ===== TABLEAU UTILISATEUR =====
        JPanel utilisateur = new JPanel(new GridLayout(10, 1, 5, 5));
        utilisateur.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                "Tableau des ingrédients de l'utilisateur"));
        utilisateur.setBackground(new Color(230, 230, 230));

        JTextField[] champsUtilisateur = new JTextField[10];
        JSpinner[] spinners = new JSpinner[10];

        for (int i = 0; i < 10; i++) {
            JPanel ligne = new JPanel(new BorderLayout());

            JButton moins = new JButton("-");
            moins.setBackground(new Color(200, 200, 200));

            final JTextField ingredient = new JTextField();
            ingredient.setBackground(Color.WHITE);
            ingredient.setEditable(false); // Empêcher la modification manuelle
            champsUtilisateur[i] = ingredient;

            JSpinner quantite = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
            quantite.setPreferredSize(new Dimension(60, 30));
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(quantite, "#");
            quantite.setEditor(editor);
            editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
            spinners[i] = quantite;

            final int index = i;
            moins.addActionListener(e -> {
                ingredient.setText("");
                spinners[index].setValue(0);
            });

            JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
            droite.add(quantite);
            droite.add(new JLabel("g"));

            ligne.add(moins, BorderLayout.WEST);
            ligne.add(ingredient, BorderLayout.CENTER);
            ligne.add(droite, BorderLayout.EAST);

            int index = i;

            // bouton -
            moins.addActionListener(e -> {
                champs[index].setText("");
                quantites[index].setValue(0);
            });

            utilisateur.add(ligne);
        }

        // ===== BOUTON + =====
        plus.addActionListener(e -> {
            String selection = (String) recherche.getSelectedItem();
            if (selection != null && !selection.isEmpty()) {
                for (int i = 0; i < champsUtilisateur.length; i++) {
                    if (champsUtilisateur[i].getText().isEmpty()) {
                        champsUtilisateur[i].setText(selection);
                        break;
                    }
                }
            }
        });

        // ===== PANNEAU NORD =====
        JPanel nord = new JPanel(new GridLayout(1, 2));
        nord.setBackground(new Color(245, 245, 245));
        nord.add(base);
        nord.add(utilisateur);
        zoneCentrale.add(nord, BorderLayout.CENTER);

        // ===== BOUTONS SUD =====
        JButton quitter = new JButton("Quitter");
        JButton valider = new JButton("Valider");
        JButton deconnexion = new JButton("Déconnexion");

        styliserBouton(valider, new Color(102, 204, 102));
        styliserBouton(quitter, new Color(255, 102, 102));
        styliserBouton(deconnexion, new Color(150, 150, 200));

        valider.addActionListener(e -> {
            // ===== CRÉER LE REPAS =====
            Repas repas = new Repas("Repas validé");

            for (int i = 0; i < champsUtilisateur.length; i++) {
                String texte = champsUtilisateur[i].getText().trim();
                int qte = (int) spinners[i].getValue();

                if (!texte.isEmpty() && qte > 0) {
                    Ingredient ingredient = new Ingredient(texte);
                    Consommable consommable = new Consommable(i, qte, ingredient);
                    repas.ajouter(consommable);

                    // Ajouter à l'historique
                    String entree = texte + "|" + qte + "|" + maintenant();
                    utilisateurConnecte.ajouterAHistorique(entree);
                }
            }

            // ✅ AJOUTER UN SÉPARATEUR VIDE POUR DISTINGUER LES REPAS
            utilisateurConnecte.ajouterAHistorique("-------------------------------");

            // Reconstruire le modèle
            reconstruireModele(modeleHistorique, utilisateurConnecte.getHistorique());

            // ===== BASCULER VERS LA FENÊTRE DE RÉSULTATS =====
            accueil.dispose();
            afficherFenetreResultats(repas, accueil);

            // Réinitialiser les champs
            for (int i = 0; i < champsUtilisateur.length; i++) {
                champsUtilisateur[i].setText("");
                spinners[i].setValue(0);
            }
        });

        quitter.addActionListener(e -> System.exit(0));

        deconnexion.addActionListener(e -> {
            utilisateurConnecte = null;
            accueil.dispose();
            afficherLogin();
        });

        JPanel sud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sud.setBackground(new Color(245, 245, 245));
        sud.add(deconnexion);
        sud.add(valider);
        sud.add(quitter);
        zoneCentrale.add(sud, BorderLayout.SOUTH);

        // ===== ASSEMBLAGE FINAL =====
        accueil.add(panneauHistorique, BorderLayout.WEST);
        accueil.add(zoneCentrale, BorderLayout.CENTER);
        accueil.setVisible(true);
    }

    // =========================================================
    // FENÊTRE DE RÉSULTATS NUTRITIONNELS
    // =========================================================
    private static void afficherFenetreResultats(Repas repas, JFrame frameParente) {
        JFrame resultatsFrame = new JFrame("Résultats nutritionnels" + repas.getNom());
        resultatsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        resultatsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultatsFrame.getContentPane().setBackground(new Color(245, 245, 245));
        resultatsFrame.setLayout(new BorderLayout());

        // ===== TITRE =====
        JLabel titre = new JLabel("Analyse nutritionnelle du repas", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 18));
        titre.setForeground(new Color(50, 50, 50));
        titre.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        resultatsFrame.add(titre, BorderLayout.NORTH);

        // ===== PANNEAU PRINCIPAL =====
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 245, 245));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== AFFICHER LES NUTRIMENTS =====
        ArrayList<Double> nutriments = repas.calculer();
        String[] labels = {"Énergie (kcal)", "Protéines (g)", "Lipides (g)",
                "Glucides (g)", "Fibres (g)", "Sodium (mg)", "Eau (g)"};

        // Seuils recommandés
        double[] seuilsMin = {400, 10, 5, 50, 4, 0, 0};
        double[] seuilsMax = {800, 50, 25, 130, Double.MAX_VALUE, 600, Double.MAX_VALUE};

        JPanel panelNutriments = new JPanel(new GridLayout(labels.length, 1, 5, 8));
        panelNutriments.setBackground(new Color(245, 245, 245));

        for (int i = 0; i < nutriments.size() && i < labels.length; i++) {
            double valeur = nutriments.get(i);
            double min = seuilsMin[i];
            double max = seuilsMax[i];

            // Déterminer la couleur
            Color couleur = new Color(100, 200, 100); // Vert par défaut
            String status = "✅";

            if (valeur < min || valeur > max) {
                couleur = new Color(220, 100, 100); // Rouge si hors limites
                status = "⚠️";
            }

            JPanel panelLigne = new JPanel(new BorderLayout());
            panelLigne.setBackground(couleur);
            panelLigne.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            JLabel labelNom = new JLabel(status + " " + labels[i]);
            labelNom.setFont(new Font("SansSerif", Font.BOLD, 14));
            labelNom.setForeground(Color.WHITE);

            JLabel labelValeur = new JLabel(String.format("%.2f", valeur));
            labelValeur.setFont(new Font("SansSerif", Font.BOLD, 14));
            labelValeur.setForeground(Color.WHITE);

            panelLigne.add(labelNom, BorderLayout.WEST);
            panelLigne.add(labelValeur, BorderLayout.EAST);

            panelNutriments.add(panelLigne);
        }

        JScrollPane scrollNutriments = new JScrollPane(panelNutriments);
        scrollNutriments.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                "Détail des nutriments"));
        scrollNutriments.setBackground(new Color(245, 245, 245));

        panelPrincipal.add(scrollNutriments, BorderLayout.CENTER);

        resultatsFrame.add(panelPrincipal, BorderLayout.CENTER);

        // ===== BOUTONS SUD =====
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        panelBoutons.setBackground(new Color(245, 245, 245));

        JButton btnRetour = new JButton("Retour");
        JButton btnQuitter = new JButton("Quitter");

        styliserBouton(btnRetour, new Color(100, 160, 220));
        styliserBouton(btnQuitter, new Color(255, 102, 102));

        btnRetour.addActionListener(e -> {
            resultatsFrame.dispose();
            afficherInterfacePrincipale();
        });

        btnQuitter.addActionListener(e -> System.exit(0));

        panelBoutons.add(btnRetour);
        panelBoutons.add(btnQuitter);

        resultatsFrame.add(panelBoutons, BorderLayout.SOUTH);

        resultatsFrame.setVisible(true);
    }

    // =========================================================
    // UTILITAIRE : style bouton
    // =========================================================
    private static void styliserBouton(JButton bouton, Color couleur) {
        bouton.setBackground(couleur);
        bouton.setForeground(Color.WHITE);
        bouton.setFont(new Font("SansSerif", Font.BOLD, 13));
        bouton.setFocusPainted(false);
        bouton.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
    }
}