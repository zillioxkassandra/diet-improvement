import java.util.*;

/**
 * La Classe Repas
 * Elle possède plusieurs attributs publics : la liste de consommables qui ont été sélectionnés et un nom.
 * Elle possède plusieurs méthodes qui vont interagir avec la liste,
 * comme calculer les valeurs nutritionnelles totales,
 * ajouter des consommables ou rechercher lequel est le plus mauvais en termes d'impact nutritionnel dans le repas
 * et celle qui affichera les informations nutritionnelles du repas.
 */
public class Repas {
    private ArrayList<Consommable> listConsommable;
    private String nom;

    public Repas(ArrayList<Consommable> listConsommable, String nom) {
        this.listConsommable = listConsommable;
        this.nom = nom;
    }

    public Repas(String nom) {
        this.listConsommable = new ArrayList<>();
        this.nom = nom;
    }

    public Repas() {
        this.listConsommable = new ArrayList<>();
    }

    // ========== GETTERS & SETTERS ==========
    public ArrayList<Consommable> getListConsommable() {
        return listConsommable;
    }

    public void setListConsommable(ArrayList<Consommable> listConsommable) {
        this.listConsommable = listConsommable;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // ========== CALCUL DES NUTRIMENTS ==========
    /**
     * Calcule les valeurs nutritionnelles totales du repas
     * @return Une ArrayList contenant tous les nutriments avec leurs valeurs totales
     */
    public ArrayList<Double> calculer() {
        ArrayList<Double> infosNutriFinale = new ArrayList<>();

        if (listConsommable.isEmpty()) {
            return infosNutriFinale;
        }

        // Initialiser la liste avec des 0 pour chaque nutriment
        Ingredient firstIngredient = listConsommable.get(0).getIngredient();
        int nbNutriments = firstIngredient.getInformationNutritionnelles().size();
        for (int i = 0; i < nbNutriments; i++) {
            infosNutriFinale.add(0.0);
        }

        // Calculer les totaux pour chaque consommable
        for (Consommable consommable : listConsommable) {
            Ingredient ingredient = consommable.getIngredient();
            double quantite = consommable.getQuantite();
            List<Double> nutriments = ingredient.getInformationNutritionnelles();

            // Pour chaque nutriment de l'ingrédient
            for (int i = 0; i < nutriments.size(); i++) {
                double valeurNutriment = nutriments.get(i);

                // Calculer : (valeur nutriment / 100) * quantité
                double contribution = (valeurNutriment / 100.0) * quantite;

                // Ajouter à la valeur actuelle
                double valeurActuelle = infosNutriFinale.get(i);
                infosNutriFinale.set(i, valeurActuelle + contribution);
            }
        }

        return infosNutriFinale;
    }

    // ========== AJOUTER UN CONSOMMABLE ==========
    /**
     * Ajoute un consommable au repas
     * @param c Le consommable à ajouter
     */
    public void ajouter(Consommable c) {
        listConsommable.add(c);
    }

    // ========== SUPPRIMER UN CONSOMMABLE ==========
    /**
     * Supprime un consommable du repas
     * @param index Index du consommable à supprimer
     */
    public void supprimer(int index) {
        if (index >= 0 && index < listConsommable.size()) {
            listConsommable.remove(index);
        }
    }

    // ========== RECHERCHER L'INGRÉDIENT PROBLÉMATIQUE ==========
    /**
     * Trouve l'ingrédient le plus mauvais en termes d'impact nutritionnel
     * @return L'ingrédient avec les valeurs nutritionnelles les plus élevées
     */
    public Ingredient findFlaw() {
        // Seuils recommandés par REPAS
        double[] seuilsMin = {400, 10, 5, 50, 4, 0, 0};
        double[] seuilsMax = {800, 50, 25, 130, Double.MAX_VALUE, 600, Double.MAX_VALUE};

        String[] labels = {"Énergie (kcal)", "Protéines (g)", "Lipides (g)",
                "Glucides (g)", "Fibres (g)", "Sodium (mg)", "Eau (g)"};

        // Calculer les totaux du repas
        ArrayList<Double> totalNutriments = calculer();

        if (totalNutriments.isEmpty()) {
            System.out.println("Repas vide");
            return null;
        }

        // Trouver les nutriments hors limites
        ArrayList<Integer> nutrimentProblematiques = new ArrayList<>();

        for (int i = 0; i < totalNutriments.size() && i < seuilsMax.length; i++) {
            double valeur = totalNutriments.get(i);
            double min = seuilsMin[i];
            double max = seuilsMax[i];

            if (valeur < min || valeur > max) {
                nutrimentProblematiques.add(i);
            }
        }

        // Si pas de problème global, retourner null
        if (nutrimentProblematiques.isEmpty()) {
            System.out.println("\nLe repas '" + nom + "' respecte tous les seuils nutritionnels !\n");
            return null;
        }

        // Trouver l'ingrédient qui CONTRIBUE LE PLUS au problème
        Ingredient ingredientProblematique = null;
        double maxContribution = 0;
        String nutrimentMaxProbleme = "";

        for (Consommable consommable : listConsommable) {
            Ingredient ingredient = consommable.getIngredient();
            ArrayList<Double> nutriments = new ArrayList<>(ingredient.getInformationNutritionnelles());
            double quantite = consommable.getQuantite();

            // Pour chaque nutriment problématique
            for (int indexProbleme : nutrimentProblematiques) {
                if (indexProbleme < nutriments.size()) {
                    double valeurNutriment = nutriments.get(indexProbleme);
                    double contribution = (valeurNutriment / 100.0) * quantite;
                    double valeurTotaleDuRepas = totalNutriments.get(indexProbleme);
                    double min = seuilsMin[indexProbleme];
                    double max = seuilsMax[indexProbleme];

                    // Calculer la contribution au dépassement
                    double contributionProbleme = 0;

                    if (valeurTotaleDuRepas > max) {
                        // Dépasse le maximum : contribution = % de la valeur totale
                        contributionProbleme = contribution;
                    } else if (valeurTotaleDuRepas < min) {
                        // En dessous du minimum : contribution négative
                        contributionProbleme = -contribution;
                    }

                    // Garder l'ingrédient avec la plus grande contribution au problème
                    if (Math.abs(contributionProbleme) > maxContribution) {
                        maxContribution = Math.abs(contributionProbleme);
                        ingredientProblematique = ingredient;
                        nutrimentMaxProbleme = labels[indexProbleme];
                    }
                }
            }
        }

        // Affichage du rapport
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ REPAS NON ÉQUILIBRÉ");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Repas: " + nom);
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ NUTRIMENTS HORS LIMITES:");

        for (int index : nutrimentProblematiques) {
            double valeur = totalNutriments.get(index);
            double min = seuilsMin[index];
            double max = seuilsMax[index];

            String status;
            if (valeur < min) {
                status = String.format("INSUFFISANT (%.1f < %.1f)", valeur, min);
            } else {
                status = String.format("EXCESSIF (%.1f > %.1f)", valeur, max);
            }

            System.out.println("║ • " + labels[index] + ": " + status);
        }

        if (ingredientProblematique != null) {
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ INGRÉDIENT LE PLUS PROBLÉMATIQUE:");
            System.out.println("║ " + ingredientProblematique.getNom());
        }

        System.out.println("╚════════════════════════════════════════╝\n");

        return ingredientProblematique;
    }


    // ========== AFFICHAGE ==========
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════╗\n");
        sb.append("║ REPAS: ").append(nom).append("\n");
        sb.append("╠════════════════════════════════════╣\n");

        if (listConsommable.isEmpty()) {
            sb.append("║ Aucun consommable\n");
        } else {
            for (int i = 0; i < listConsommable.size(); i++) {
                Consommable consommable = listConsommable.get(i);
                Ingredient ingredient = consommable.getIngredient();
                sb.append("║ ").append(i + 1).append(". ").append(ingredient.getNom())
                        .append(" (").append(consommable.getQuantite()).append("g)\n");
            }
        }

        sb.append("╚════════════════════════════════════╝\n");
        return sb.toString();
    }

    /**
     * Affiche les informations nutritionnelles détaillées du repas
     */
    public void afficherNutriments() {
        ArrayList<Double> nutriments = calculer();

        String[] labels = {"Énergie (kcal)", "Protéines (g)", "Lipides (g)",
                "Glucides (g)", "Fibres (g)", "Sodium (mg)", "Eau (g)"};

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║ VALEURS NUTRITIONNELLES DU REPAS: " + nom);
        System.out.println("╠════════════════════════════════════╣");

        for (int i = 0; i < nutriments.size() && i < labels.length; i++) {
            System.out.printf("║ %-25s: %8.2f\n", labels[i], nutriments.get(i));
        }

        System.out.println("╚════════════════════════════════════╝\n");
    }

    public static void main(String[] args) {
        try {
            // Créer des ingrédients depuis le JSON
            System.out.println("=== Création du repas ===\n");

            Ingredient pomme = new Ingredient("Apple");
            Ingredient poulet = new Ingredient("Chicken Breast");

            // Créer des consommables
            Consommable c1 = new Consommable(1, 150, pomme);       // 150g de pomme
            Consommable c2 = new Consommable(2, 200, poulet);      // 200g de poulet

            // Créer et configurer le repas
            Repas repas = new Repas("Déjeuner sain");
            repas.ajouter(c1);
            repas.ajouter(c2);

            System.out.println(repas.toString());
            repas.afficherNutriments();
            repas.findFlaw();

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}