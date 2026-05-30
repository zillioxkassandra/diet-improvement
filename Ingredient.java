import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

/**
 * La classe Ingredient représente un ingrédient avec ses informations
 * principales récupérées depuis un fichier JSON.
 */
public class Ingredient {

    private static final String JSON_FILE_PATH = "ingredients.json";
    private static ArrayList<Ingredient> ingredientsCache = null;

    /** Identifiant unique */
    private int id;

    /** Nom */
    private String nom;

    /** Famille alimentaire */
    private String famille;

    /** Unité */
    private String unite;

    /** Informations nutritionnelles */
    private List<Double> informationNutritionnelles;

    // =========================================================
    // CONSTRUCTEUR PAR NOM
    // =========================================================

    public Ingredient(String nom) {
        chargerIngredientsJSON();
        rechercherEtChargerIngredient(nom);
    }

    // =========================================================
    // CONSTRUCTEUR MANUEL
    // =========================================================

    public Ingredient(int id,
                      String nom,
                      String famille,
                      String unite,
                      List<Double> informationNutritionnelles) {

        this.id = id;
        this.nom = nom;
        this.famille = famille;
        this.unite = unite;
        this.informationNutritionnelles = informationNutritionnelles;
    }

    // =========================================================
    // CHARGEMENT JSON
    // =========================================================

    private static void chargerIngredientsJSON() {

        if (ingredientsCache != null) {
            return;
        }

        try {

            String jsonContent =
                    new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));

            ingredientsCache = new ArrayList<>();

            Pattern ingredientPattern = Pattern.compile(
                    "\\{\\s*\"id\":\\s*(\\d+)," +
                            "\\s*\"nom\":\\s*\"([^\"]+)\"," +
                            "\\s*\"famille\":\\s*\"([^\"]+)\"," +
                            "\\s*\"unite\":\\s*\"([^\"]+)\"," +
                            "\\s*\"informationNutritionnelles\":\\s*\\[([^\\]]+)\\]\\s*\\}"
            );

            Matcher matcher = ingredientPattern.matcher(jsonContent);

            while (matcher.find()) {

                int id = Integer.parseInt(matcher.group(1));
                String nom = matcher.group(2);
                String famille = matcher.group(3);
                String unite = matcher.group(4);
                String nutrientsStr = matcher.group(5);

                List<Double> nutrients = new ArrayList<>();

                String[] nutrientValues = nutrientsStr.split(",");

                for (String value : nutrientValues) {

                    try {
                        nutrients.add(Double.parseDouble(value.trim()));
                    }
                    catch (NumberFormatException e) {

                    }
                }

                Ingredient ingredient = new Ingredient(
                        id,
                        nom,
                        famille,
                        unite,
                        nutrients
                );

                ingredientsCache.add(ingredient);
            }

            System.out.println(
                    "✓ " + ingredientsCache.size()
                            + " ingrédients chargés depuis "
                            + JSON_FILE_PATH
            );

        }
        catch (Exception e) {

            System.out.println(
                    "Erreur lors du chargement du JSON : "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // RECHERCHE D'UN INGREDIENT
    // =========================================================

    private void rechercherEtChargerIngredient(String nomRecherche) {

        if (ingredientsCache == null || ingredientsCache.isEmpty()) {

            System.out.println("Aucune donnée d'ingrédients disponible");
            return;
        }

        String nomRechercheLower =
                nomRecherche.toLowerCase().trim();

        for (Ingredient ingredient : ingredientsCache) {

            String nomItem =
                    ingredient.getNom().toLowerCase();

            if (nomItem.equals(nomRechercheLower)
                    || nomItem.contains(nomRechercheLower)) {

                this.id = ingredient.getId();
                this.nom = ingredient.getNom();
                this.famille = ingredient.getFamille();
                this.unite = ingredient.getUnite();

                this.informationNutritionnelles =
                        new ArrayList<>(
                                ingredient.getInformationNutritionnelles()
                        );

                System.out.println(
                        "✓ Ingrédient trouvé : " + this.nom
                );

                return;
            }
        }

        System.out.println(
                "Ingrédient '" + nomRecherche + "' non trouvé"
        );

        this.nom = nomRecherche;
        this.famille = "Inconnue";
        this.unite = "g";
        this.informationNutritionnelles = new ArrayList<>();
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getFamille() {
        return famille;
    }

    public String getUnite() {
        return unite;
    }

    public List<Double> getInformationNutritionnelles() {
        return informationNutritionnelles;
    }

    public Ingredient getIngredient() {
        return this;
    }

    // =========================================================
    // AFFICHAGE
    // =========================================================

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("\n╔════════════════════════════════════╗\n");
        sb.append("║ ID: ").append(id).append("\n");
        sb.append("║ NOM: ").append(nom).append("\n");
        sb.append("║ FAMILLE: ").append(famille).append("\n");
        sb.append("║ UNITÉ: ").append(unite).append("\n");
        sb.append("╠════════════════════════════════════╣\n");
        sb.append("║ INFORMATIONS NUTRITIONNELLES:\n");

        String[] labels = {
                "Énergie (kcal)",
                "Protéines (g)",
                "Lipides (g)",
                "Glucides (g)",
                "Fibres (g)",
                "Sodium (mg)",
                "Eau (g)"
        };

        if (informationNutritionnelles != null
                && !informationNutritionnelles.isEmpty()) {

            for (int i = 0;
                 i < informationNutritionnelles.size()
                         && i < labels.length;
                 i++) {

                sb.append("║ • ")
                        .append(String.format("%-20s", labels[i]))
                        .append(": ")
                        .append(String.format(
                                "%.2f",
                                informationNutritionnelles.get(i)
                        ))
                        .append("\n");
            }

        } else {

            sb.append("║ Aucune donnée disponible\n");
        }

        sb.append("╚════════════════════════════════════╝\n");

        return sb.toString();
    }

    // =========================================================
    // OUTILS
    // =========================================================

    /**
     * Affiche tous les ingrédients
     */
    public static void afficherTousLesIngreidents() {

        chargerIngredientsJSON();

        if (ingredientsCache == null
                || ingredientsCache.isEmpty()) {

            System.out.println("Aucun ingrédient disponible");
            return;
        }

        System.out.println(
                "\n╔════════════════════════════════════╗"
        );

        System.out.println(
                "║ TOUS LES INGRÉDIENTS DISPONIBLES"
        );

        System.out.println(
                "╠════════════════════════════════════╣"
        );

        for (Ingredient ingredient : ingredientsCache) {

            System.out.printf(
                    "║ %3d. %-25s (%s)\n",
                    ingredient.getId(),
                    ingredient.getNom(),
                    ingredient.getFamille()
            );
        }

        System.out.println(
                "╚════════════════════════════════════╝\n"
        );
    }

    /**
     * Retourne tous les ingrédients
     */
    public static ArrayList<Ingredient> obtenirTousLesIngreidents() {

        chargerIngredientsJSON();

        return new ArrayList<>(ingredientsCache);
    }

    /**
     * Retourne tous les noms
     */
    public static ArrayList<String> obtenirTousLesNomsIngreidents() {

        chargerIngredientsJSON();

        ArrayList<String> noms = new ArrayList<>();

        if (ingredientsCache != null) {

            for (Ingredient ingredient : ingredientsCache) {

                noms.add(ingredient.getNom());
            }
        }

        return noms;
    }

    /**
     * Création par ID
     */
    public static Ingredient creerParId(int id) {

        chargerIngredientsJSON();

        if (ingredientsCache == null) {
            return null;
        }

        for (Ingredient ingredient : ingredientsCache) {

            if (ingredient.getId() == id) {

                return new Ingredient(
                        ingredient.getId(),
                        ingredient.getNom(),
                        ingredient.getFamille(),
                        ingredient.getUnite(),
                        new ArrayList<>(
                                ingredient.getInformationNutritionnelles()
                        )
                );
            }
        }

        return null;
    }



}