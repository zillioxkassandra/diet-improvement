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

    // ========== CALCUL DES NUTRIMENTS ==========
    /**
     * Calcule les valeurs nutritionnelles de ce consommable
     * @return Une ArrayList contenant tous les nutriments ajustés à la quantité
     */
    public ArrayList<Double> calculerNutriments() {
        ArrayList<Double> nutrimentsFinal = new ArrayList<>();
        List<Double> nutriments = ingredient.getInformationNutritionnelles();

        for (Double valeur : nutriments) {
            // Calcul : (valeur pour 100g / 100) * quantité
            double valeurFinal = (valeur / 100.0) * quantite;
            nutrimentsFinal.add(valeurFinal);
        }

        return nutrimentsFinal;
    }

    /**
     * Calcule les nutriments avec détail des valeurs manquantes
     * @return Une ArrayList avec les nutriments valides et manquants
     */
    public ArrayList<String> calculerNutrimentsAvecManquants() {
        ArrayList<String> resultat = new ArrayList<>();
        List<Double> nutriments = ingredient.getInformationNutritionnelles();

        String[] labels = {"Énergie (kcal)", "Protéines (g)", "Lipides (g)",
                "Glucides (g)", "Fibres (g)", "Sodium (mg)", "Eau (g)"};

        for (int i = 0; i < nutriments.size(); i++) {
            double valeur = nutriments.get(i);
            String label = (i < labels.length) ? labels[i] : "Nutriment " + i;

            if (valeur > 0) {
                double valeurFinal = (valeur / 100.0) * quantite;
                resultat.add(label + ": " + String.format("%.2f", valeurFinal));
            } else {
                resultat.add(label + ": N/A");
            }
        }

        return resultat;
    }

    // ========== AFFICHAGE ==========
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════╗\n");
        sb.append("║ CONSOMMABLE ID: ").append(id).append("\n");
        sb.append("║ Ingrédient: ").append(ingredient.getNom()).append("\n");
        sb.append("║ Quantité: ").append(quantite).append("g\n");
        sb.append("╠════════════════════════════════════╣\n");
        sb.append("║ NUTRIMENTS:\n");

        ArrayList<Double> nutriments = calculerNutriments();
        String[] labels = {"Énergie (kcal)", "Protéines (g)", "Lipides (g)",
                "Glucides (g)", "Fibres (g)", "Sodium (mg)", "Eau (g)"};

        if (nutriments.isEmpty()) {
            sb.append("║ ⚠️  Aucune donnée disponible\n");
        } else {
            for (int i = 0; i < nutriments.size() && i < labels.length; i++) {
                sb.append("║ • ").append(String.format("%-20s", labels[i]))
                        .append(": ").append(String.format("%8.2f", nutriments.get(i))).append("\n");
            }
        }

        sb.append("╚════════════════════════════════════╝\n");
        return sb.toString();
    }

    /**
     * Affiche les nutriments avec détails sur les valeurs manquantes
     */
    public void afficherAvecDetails() {
        ArrayList<String> nutriments = calculerNutrimentsAvecManquants();

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║ CONSOMMABLE ID: " + id);
        System.out.println("║ Ingrédient: " + ingredient.getNom());
        System.out.println("║ Quantité: " + quantite + "g");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ NUTRIMENTS:");

        for (String nutriment : nutriments) {
            System.out.println("║ • " + nutriment);
        }

        System.out.println("╚════════════════════════════════════╝\n");
    }

    public static void main(String[] args) {
        try {
            // Créer un ingrédient depuis le JSON
            Ingredient pomme = new Ingredient("Apple");

            // Créer un consommable
            Consommable c = new Consommable(1, 150, pomme);

            System.out.println(c.toString());
            c.afficherAvecDetails();

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}