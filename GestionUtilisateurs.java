import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsable de la gestion des utilisateurs :
 * création de compte, connexion, lecture/écriture de l'historique.
 * Chaque utilisateur possède son propre fichier dans le dossier "utilisateurs/".
 */
public class GestionUtilisateurs {

    private static final String DOSSIER = "utilisateurs";
    private static final String PREFIX_MDP = "MOT_DE_PASSE_HASH:";
    private static final String PREFIX_HISTORIQUE = "HISTORIQUE:";

    /**
     * Initialise le dossier utilisateurs s'il n'existe pas.
     */
    public static void initialiser() {
        File dossier = new File(DOSSIER);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }
    }

    /**
     * Hash un mot de passe en SHA-256.
     * @param motDePasse le mot de passe en clair
     * @return la chaîne hashée en hexadécimal
     */
    public static String hasher(String motDePasse) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(motDePasse.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 non disponible", e);
        }
    }

    /**
     * Retourne le chemin du fichier d'un utilisateur.
     */
    private static String cheminFichier(String identifiant) {
        return DOSSIER + File.separator + identifiant.toLowerCase() + ".txt";
    }

    /**
     * Vérifie si un utilisateur existe déjà.
     */
    public static boolean utilisateurExiste(String identifiant) {
        return new File(cheminFichier(identifiant)).exists();
    }

    /**
     * Crée un nouveau compte utilisateur.
     * @return true si la création a réussi, false si l'identifiant est déjà pris
     */
    public static boolean creerCompte(String identifiant, String motDePasse) {
        if (identifiant == null || identifiant.trim().isEmpty()) return false;
        if (motDePasse == null || motDePasse.trim().isEmpty()) return false;
        if (utilisateurExiste(identifiant)) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier(identifiant)))) {
            writer.write(PREFIX_MDP + hasher(motDePasse));
            writer.newLine();
            writer.write(PREFIX_HISTORIQUE);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Tente de connecter un utilisateur.
     * @return true si identifiant + mot de passe sont corrects
     */
    public static boolean connecter(String identifiant, String motDePasse) {
        if (!utilisateurExiste(identifiant)) return false;
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier(identifiant)))) {
            String premiereLigne = reader.readLine();
            if (premiereLigne != null && premiereLigne.startsWith(PREFIX_MDP)) {
                String hashStocke = premiereLigne.substring(PREFIX_MDP.length());
                return hashStocke.equals(hasher(motDePasse));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Charge l'historique d'un utilisateur depuis son fichier.
     * @return liste des entrées de l'historique
     */
    public static List<String> chargerHistorique(String identifiant) {
        List<String> historique = new ArrayList<>();
        if (!utilisateurExiste(identifiant)) return historique;

        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier(identifiant)))) {
            String ligne;
            boolean dansHistorique = false;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.startsWith(PREFIX_HISTORIQUE)) {
                    dansHistorique = true;
                    continue;
                }
                if (dansHistorique && !ligne.trim().isEmpty()) {
                    historique.add(ligne.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return historique;
    }

    /**
     * Ajoute une entrée dans l'historique de l'utilisateur (sans doublon).
     */
    public static void ajouterHistorique(String identifiant, String entree) {
        if (entree == null || entree.trim().isEmpty()) return;
        List<String> historique = chargerHistorique(identifiant);

        // ✅ PERMETTRE LES DOUBLONS POUR LES SÉPARATEURS
        if (entree.trim().startsWith("---SÉPARATEUR---")) {
            // Les séparateurs peuvent toujours être ajoutés
            historique.add(entree.trim());
        } else {
            // Pour les ingrédients normaux, pas de doublon
            if (!historique.contains(entree.trim())) {
                historique.add(entree.trim());
            }
        }

        sauvegarderHistorique(identifiant, historique);
    }

    /**
     * Supprime une entrée de l'historique.
     */
    public static void supprimerHistorique(String identifiant, String entree) {
        List<String> historique = chargerHistorique(identifiant);
        historique.remove(entree.trim());
        sauvegarderHistorique(identifiant, historique);
    }

    /**
     * Réécrit le fichier utilisateur avec le mot de passe existant + l'historique mis à jour.
     */
    private static void sauvegarderHistorique(String identifiant, List<String> historique) {
        // Lire le hash existant
        String hash = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier(identifiant)))) {
            String ligne = reader.readLine();
            if (ligne != null && ligne.startsWith(PREFIX_MDP)) {
                hash = ligne;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Réécrire le fichier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier(identifiant)))) {
            writer.write(hash);
            writer.newLine();
            writer.write(PREFIX_HISTORIQUE);
            writer.newLine();
            for (String entree : historique) {
                writer.write(entree);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
