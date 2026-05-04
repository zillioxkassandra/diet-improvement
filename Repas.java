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
        Ingredient ingredientFautif = null;
        Double nutri_max = 0.0;
        int indexNutrimentProblematique = -1;

        String[] labels = {"Énergie (kcal)", "Protéines (g)", "Lipides (g)",
                "Glucides (g)", "Fibres (g)", "Sodium (mg)", "Eau (g)"};

        for (Consommable consommable : listConsommable) {
            Ingredient ingredient = consommable.getIngredient();
            List<Double> nutriments = ingredient.getInformationNutritionnelles();

            // Parcourir tous les nutriments
            for (int i = 0; i < nutriments.size(); i++) {
                Double valeurNutriment = nutriments.get(i);

                // Chercher la valeur maximale (surtout pour Energy, Fat, Sugar)
                if (valeurNutriment > nutri_max) {
                    nutri_max = valeurNutriment;
                    ingredientFautif = ingredient;
                    indexNutrimentProblematique = i;
                }
            }
        }

        if (ingredientFautif != null) {
            String nomNutriment = (indexNutrimentProblematique >= 0 && indexNutrimentProblematique < labels.length)
                    ? labels[indexNutrimentProblematique]
                    : "Nutriment " + indexNutrimentProblematique;

            System.out.println("⚠️  Ingrédient problématique : " + ingredientFautif.getNom());
            System.out.println("   Nutriment dépassé : " + nomNutriment);
            System.out.println("   Valeur : " + nutri_max);
        } else {
            System.out.println("✓ Tous les ingrédients sont dans les normes");
        }

        return ingredientFautif;
    }

    // ========== CALCUL DES MOYENNES NUTRITIONNELLES ==========
    /**
     * Calcule les moyennes nutritionnelles par consommable
     * @return ArrayList des moyennes
     */
    public ArrayList<Double> calculerMoyennes() {
        ArrayList<Double> totaux = calculer();
        ArrayList<Double> moyennes = new ArrayList<>();

        if (listConsommable.isEmpty()) {
            return moyennes;
        }

        for (Double total : totaux) {
            moyennes.add(total / listConsommable.size());
        }

        return moyennes;
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

    /**
     * Affiche le résumé du repas
     */
    public void afficherResume() {
        ArrayList<Double> nutriments = calculer();

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║ RÉSUMÉ - " + nom);
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Nombre de consommables: " + listConsommable.size());

        if (!nutriments.isEmpty()) {
            System.out.printf("║ Énergie totale: %.2f kcal\n", nutriments.get(0));
            System.out.printf("║ Protéines totales: %.2f g\n", nutriments.get(1));
            System.out.printf("║ Lipides totaux: %.2f g\n", nutriments.get(2));
            System.out.printf("║ Glucides totaux: %.2f g\n", nutriments.get(3));
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
            repas.afficherResume();
            repas.findFlaw();

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}