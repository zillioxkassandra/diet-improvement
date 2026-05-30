import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlternativeSaine {

    public Consommable alternative_saine;
    public ArrayList<Repas> repas;

    /**
     * Trouve un ingrédient alternatif plus sain pour remplacer l'ingrédient problématique
     *
     * @param ingredient L'ingrédient problématique
     * @param qte        La quantité de cet ingrédient
     * @return L'ingrédient alternatif plus sain, ou null si aucun n'est trouvé
     */
    public static Ingredient alt(Ingredient ingredient, float qte, Repas repas) {

        if (ingredient == null || repas == null) return null;

        String famille = ingredient.getFamille();

        ArrayList<Ingredient> tous = Ingredient.obtenirTousLesIngreidents();

        Ingredient meilleur = null;
        double meilleurScore = Double.MAX_VALUE;

        for (Ingredient i : tous) {

            if (i.getFamille() == null) continue;

            // même famille uniquement
            if (!i.getFamille().equals(famille)) continue;

            // éviter de se remplacer soi-même
            if (i.getNom().equals(ingredient.getNom())) continue;

            // ❌ IMPORTANT : éviter doublon dans le repas
            boolean dejaPresent = false;

            for (Consommable c : repas.getListConsommable()) {
                if (c.getIngredient().getNom().equals(i.getNom())) {
                    dejaPresent = true;
                    break;
                }
            }

            if (dejaPresent) continue;

            List<Double> n = i.getInformationNutritionnelles();

            // score nutritionnel (plus petit = meilleur)
            double score =
                    n.get(0) +      // calories
                            n.get(2) * 2 +  // lipides
                            n.get(5);       // sodium

            if (score < meilleurScore) {
                meilleurScore = score;
                meilleur = i;
            }
        }

        return meilleur;
    }
}

