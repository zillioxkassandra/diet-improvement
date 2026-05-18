import java.util.ArrayList;
import java.util.List;

/**
 * La classe Utilisateur représente un utilisateur connecté à  l'application.
 * Elle stocke son identifiant, son état de validation,
 * et son historique de recherches/ingrédients chargé depuis son fichier.
 */
public class Utilisateur {

    private String identifiant;
    private boolean valider = false;
    private List<String> historique;

    /**
     * Constructeur charge automatiquement l'historique depuis le fichier.
     * @param identifiant identifiant de l'utilisateur (doit exister dans utilisateurs/)
     */
    public Utilisateur(String identifiant) {
        this.identifiant = identifiant;
        this.historique = GestionUtilisateurs.chargerHistorique(identifiant);
    }

        JLabel userLabel = new JLabel("Utilisateur :");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Mot de passe :");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Se connecter");
        JButton registerButton = new JButton("Créer un compte");

        loginFrame.add(userLabel);
        loginFrame.add(userField);
        loginFrame.add(passLabel);
        loginFrame.add(passField);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        // ===== CONNEXION =====
        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if(checkUser(user, pass)){
                loginFrame.dispose();
                Interface.main(new String[]{user});
            } else {
                JOptionPane.showMessageDialog(loginFrame,
                        "Identifiants incorrects",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // ===== CREATION COMPTE =====
        registerButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if(user.isEmpty() || pass.isEmpty()){
                JOptionPane.showMessageDialog(loginFrame,
                        "Remplis tous les champs",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(userExists(user)){
                JOptionPane.showMessageDialog(loginFrame,
                        "Utilisateur déjà existant",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true));
                writer.write(user + ";" + pass);
                writer.newLine();
                writer.close();

                JOptionPane.showMessageDialog(loginFrame,
                        "Compte créé !");
            } catch(IOException ex){
                ex.printStackTrace();
            }
        });

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    /**
     * Retourne l'historique en mémoire (synchronisé avec le fichier).
     */
    public List<String> getHistorique() {
        return new ArrayList<>(historique);
    }

    /**
     * Ajoute un ingrédient/aliment à  l'historique (en mémoire + fichier).
     */
    public void ajouterAHistorique(String entree) {
        if (entree == null || entree.trim().isEmpty()) return;

        // ✅ PERMETTRE LES DOUBLONS POUR LES SÉPARATEURS
        if (entree.startsWith("---SÉPARATEUR---")) {
            // Les séparateurs peuvent toujours être ajoutés (pas de vérification de doublon)
            historique.add(entree.trim());
            GestionUtilisateurs.ajouterHistorique(identifiant, entree.trim());
        } else {
            // Pour les ingrédients normaux, pas de doublon
            if (!historique.contains(entree.trim())) {
                historique.add(entree.trim());
                GestionUtilisateurs.ajouterHistorique(identifiant, entree.trim());
            }
        }
    }

    /**
     * Supprime une entrÃ©e de l'historique (en mÃ©moire + fichier).
     */
    public void supprimerDeHistorique(String entree) {
        historique.remove(entree);
        GestionUtilisateurs.supprimerHistorique(identifiant, entree);
    }

    /**
     * Recharge l'historique depuis le fichier (utile aprÃ¨s modification externe).
     */
    public void rechargerHistorique() {
        this.historique = GestionUtilisateurs.chargerHistorique(identifiant);
    }

    @Override
    public String toString() {
        return "Utilisateur{identifiant='" + identifiant + "', valider=" + valider + "}";
    }
}
