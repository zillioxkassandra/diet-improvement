import java.util.*;

/**
 * La classe Consommable représente un ingrédient consommé
 * avec une quantité spécifique.
 *
 * Elle permet de lier un ingrédient à une quantité donnée
 * dans le cadre d'un repas.
 */
public class Consommable {
    private int id;
    private double quantite;
    private Ingredient ingredient;

    /**
     * Constructeur de la classe Consommable
     *
     * @param id Identifiant du consommable
     * @param quantite Quantité consommée (en grammes par défaut)
     * @param ingredient Ingrédient associé
     */
    public Consommable(int id, double quantite, Ingredient ingredient) {
        this.id = id;
        this.quantite = quantite;
        this.ingredient = ingredient;
    }

    // ========== GETTERS & SETTERS ==========
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }


    // ========== AFFICHAGE ==========
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════╗\n");
        sb.append("║ CONSOMMABLE ID: ").append(id).append("\n");
        sb.append("║ Ingrédient: ").append(ingredient.getNom()).append("\n");
        sb.append("║ Quantité: ").append(quantite).append("g\n");
        sb.append("╚════════════════════════════════════╝\n");
        return sb.toString();
    }


    public static void main(String[] args) {
        try {
            // Créer un ingrédient depuis le JSON
            Ingredient pomme = new Ingredient("Apple");

            // Créer un consommable
            Consommable c = new Consommable(1, 150, pomme);

            System.out.println(c.toString());

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}