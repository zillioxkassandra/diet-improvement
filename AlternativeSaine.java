import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlternativeSaine {

    public Consommable alternative_saine;
    public ArrayList<Repas> repas;

    /**
     * Trouve un ingrédient alternatif plus sain pour remplacer l'ingrédient problématique
     * @param ingredient L'ingrédient problématique
     * @param qte La quantité de cet ingrédient
     * @return L'ingrédient alternatif plus sain, ou null si aucun n'est trouvé
     */
    public static Ingredient alt(Ingredient ingredient, float qte) {
        // Seuils recommandés par REPAS
        double[] seuilsMin = {400, 10, 5, 50, 4, 0, 0};
        double[] seuilsMax = {800, 50, 25, 130, Double.MAX_VALUE, 600, Double.MAX_VALUE};

        String[] labels = {"Énergie (kcal)", "Protéines (g)", "Lipides (g)",
                "Glucides (g)", "Fibres (g)", "Sodium (mg)", "Eau (g)"};

        // Récupérer tous les ingrédients
        ArrayList<Ingredient> tousLesIngreidents = Ingredient.obtenirTousLesIngreidents();

        if (tousLesIngreidents == null || tousLesIngreidents.isEmpty()) {
            System.out.println("Aucun ingrédient disponible pour trouver une alternative");
            return null;
        }

        // Calculer les nutriments actuels de l'ingrédient problématique
        ArrayList<Double> nutrimentActuel = new ArrayList<>();
        List<Double> nutrientsIng = ingredient.getInformationNutritionnelles();
        for (Double val : nutrientsIng) {
            nutrimentActuel.add((val / 100.0) * qte);
        }

        // Calculer les nutriments totaux du repas
        ArrayList<Double> totalRepas = Repas.calculer();

        // Trouver les nutriments hors limites
        ArrayList<Integer> nutrimentProblematiques = new ArrayList<>();
        for (int i = 0; i < totalRepas.size() && i < seuilsMax.length; i++) {
            double valeur = totalRepas.get(i);
            double min = seuilsMin[i];
            double max = seuilsMax[i];

            if (valeur < min || valeur > max) {
                nutrimentProblematiques.add(i);
            }
        }

        if (nutrimentProblematiques.isEmpty()) {
            System.out.println("✅ L'ingrédient " + ingredient.getNom() + " ne pose pas de problème");
            return null;
        }

        // Chercher l'ingrédient alternatif le plus sain
        Ingredient meilleurAlternatif = null;
        double meilleureScore = Double.MAX_VALUE;

        for (Ingredient alt : tousLesIngreidents) {
            // Ignorer l'ingrédient lui-même
            if (alt.getNom().equalsIgnoreCase(ingredient.getNom())) {
                continue;
            }

            ArrayList<Double> nutrimentAlt = new ArrayList<>();
            List<Double> nutrientsAlt = alt.getInformationNutritionnelles();
            for (Double val : nutrientsAlt) {
                nutrimentAlt.add((val / 100.0) * qte);
            }

            // Calculer le score d'amélioration
            double scoreAmeliration = 0;

            for (int indexProbleme : nutrimentProblematiques) {
                if (indexProbleme < nutrimentAlt.size()) {
                    double valeurActuelle = nutrimentActuel.get(indexProbleme);
                    double valeurAlternative = nutrimentAlt.get(indexProbleme);
                    double valeurTotaleDuRepas = totalRepas.get(indexProbleme);
                    double min = seuilsMin[indexProbleme];
                    double max = seuilsMax[indexProbleme];

                    // Calculer la différence d'écart par rapport aux seuils
                    if (valeurTotaleDuRepas > max) {
                        // Si le repas dépasse le max, on préfère un ingrédient moins "gras"
                        double ecartActuel = valeurTotaleDuRepas - max;
                        double ecartAvecAlt = (totalRepas.get(indexProbleme) - valeurActuelle + valeurAlternative) - max;
                        scoreAmeliration += Math.abs(ecartAvecAlt - ecartActuel);
                    } else if (valeurTotaleDuRepas < min) {
                        // Si le repas est en dessous du min, on préfère un ingrédient plus "riche"
                        double manqueActuel = min - valeurTotaleDuRepas;
                        double manqueAvecAlt = min - (totalRepas.get(indexProbleme) - valeurActuelle + valeurAlternative);
                        scoreAmeliration += Math.abs(manqueAvecAlt - manqueActuel);
                    }
                }
            }

            // Garder l'ingrédient avec le meilleur score d'amélioration
            if (scoreAmeliration < meilleureScore) {
                meilleureScore = scoreAmeliration;
                meilleurAlternatif = alt;
            }
        }

        // Afficher le résultat
        if (meilleurAlternatif != null) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║ 💡 ALTERNATIVE SAINE PROPOSÉE");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ À remplacer: " + ingredient.getNom());
            System.out.println("║ Par: " + meilleurAlternatif.getNom());
            System.out.printf("║ Score d'amélioration: %.2f\n", meilleureScore);
            System.out.println("╚════════════════════════════════════════╝\n");
        } else {
            System.out.println("❌ Aucune alternative trouvée");
        }

        return meilleurAlternatif;
    }

    public void AfficherInfoAlt() {
        // Ask bdd for nutri values
        // print infos
    };

    public static void main(String[] args) {
        Repas repas = new Repas("Déjeuner");
        repas.ajouter(new Consommable(1, 150, new Ingredient("Bacon")));
        repas.ajouter(new Consommable(2, 200, new Ingredient("Riz")));

// Trouver l'ingrédient problématique
        Ingredient probleme = repas.findFlaw();

        if (probleme != null) {
            // Trouver une alternative
            Ingredient alternative = alt(probleme, 200);

            if (alternative != null) {
                System.out.println("✅ Alternative trouvée: " + alternative.getNom());
            }
        }

    }
}
