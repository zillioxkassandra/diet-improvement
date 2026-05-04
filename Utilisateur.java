import java.util.ArrayList;
import java.util.List;

/**
 * La classe Utilisateur représente un utilisateur connecté à l'application.
 * Elle stocke son identifiant, son état de validation,
 * et son historique de recherches/ingrédients chargé depuis son fichier.
 */
public class Utilisateur {

    private String identifiant;
    private boolean valider = false;
    private List<String> historique;

    /**
     * Constructeur — charge automatiquement l'historique depuis le fichier.
     * @param identifiant identifiant de l'utilisateur (doit exister dans utilisateurs/)
     */
    public Utilisateur(String identifiant) {
        this.identifiant = identifiant;
        this.historique = GestionUtilisateurs.chargerHistorique(identifiant);
    }

    public String getIdentifiant() {
        return this.identifiant;
    }

    public void setValider(boolean valider) {
        this.valider = valider;
    }

    public boolean isValider() {
        return this.valider;
    }

    /**
     * Retourne l'historique en mémoire (synchronisé avec le fichier).
     */
    public List<String> getHistorique() {
        return new ArrayList<>(historique);
    }

    /**
     * Ajoute un ingrédient/aliment à l'historique (en mémoire + fichier).
     */
    public void ajouterAHistorique(String entree) {
        if (!historique.contains(entree)) {
            historique.add(entree);
        }
        GestionUtilisateurs.ajouterHistorique(identifiant, entree);
    }

    /**
     * Supprime une entrée de l'historique (en mémoire + fichier).
     */
    public void supprimerDeHistorique(String entree) {
        historique.remove(entree);
        GestionUtilisateurs.supprimerHistorique(identifiant, entree);
    }

    /**
     * Recharge l'historique depuis le fichier (utile après modification externe).
     */
    public void rechargerHistorique() {
        this.historique = GestionUtilisateurs.chargerHistorique(identifiant);
    }

    @Override
    public String toString() {
        return "Utilisateur{identifiant='" + identifiant + "', valider=" + valider + "}";
    }
}
