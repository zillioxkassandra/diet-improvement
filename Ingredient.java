import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

/**
 * La classe Ingredient représente un ingrédient avec ses informations
 * principales récupérées depuis un fichier JSON.
 * Elle permet de créer un ingrédient à partir d'un nom et charge automatiquement
 * ses informations nutritionnelles.
 */
public class Ingredient {

    private static final String JSON_FILE_PATH = "ingredients.json";
    private static ArrayList<Ingredient> ingredientsCache = null;

    /** Identifiant unique de l'ingrédient */
    private int id;

    /** Nom de l'ingrédient */
    private String nom;

    /** Unité de mesure de l'ingrédient (g, ml) */
    private String unite;

    /** Liste des informations nutritionnelles (ex : calories, protéines, etc.) */
    private List<Double> informationNutritionnelles;

    // ========== CONSTRUCTEUR PAR NOM ==========
    /**
     * Crée un ingrédient à partir de son nom et récupère automatiquement ses données du JSON
     * @param nom Nom de l'ingrédient à rechercher
     */
    public Ingredient(String nom) {
        chargerIngredientsJSON();
        rechercherEtChargerIngredient(nom);
    }

    // ========== CONSTRUCTEUR MANUEL ==========
    /**
     * Crée un ingrédient manuellement (utilisé lors du chargement du JSON)
     * @param id Identifiant
     * @param nom Nom de l'ingrédient
     * @param unite Unité de mesure
     * @param informationNutritionnelles Liste des nutriments
     */
    public Ingredient(int id, String nom, String unite, List<Double> informationNutritionnelles) {
        this.id = id;
        this.nom = nom;
        this.unite = unite;
        this.informationNutritionnelles = informationNutritionnelles;
    }

    // ========== CONSTRUCTEUR ANCIEN (compatibilité) ==========
    /**
     * Constructeur ancien pour compatibilité avec le code existant
     * @param nom Nom de l'ingrédient
     * @param unite Unité de mesure
     * @param informationNutritionnelles Liste des nutriments
     */
    public Ingredient(String nom, String unite, List<Double> informationNutritionnelles) {
        this.nom = nom;
        this.unite = unite;
        this.informationNutritionnelles = informationNutritionnelles;
    }

    // ========== CHARGEMENT DU JSON (SANS GSON) ==========
    /**
     * Charge le fichier JSON contenant tous les ingrédients (sans librairie JSON)
     */
    private static void chargerIngredientsJSON() {
        if (ingredientsCache != null) {
            return; // Déjà chargé
        }

        try {
            // Lire le fichier JSON
            String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));

            // Parser manuel du JSON
            ingredientsCache = new ArrayList<>();

            // Trouver le tableau "ingredients"
            Pattern ingredientPattern = Pattern.compile("\\{\\s*\"id\":\\s*(\\d+),\\s*\"nom\":\\s*\"([^\"]+)\",\\s*\"unite\":\\s*\"([^\"]+)\",\\s*\"informationNutritionnelles\":\\s*\\[([^\\]]+)\\]\\s*\\}");
            Matcher matcher = ingredientPattern.matcher(jsonContent);

            while (matcher.find()) {
                int id = Integer.parseInt(matcher.group(1));
                String nom = matcher.group(2);
                String unite = matcher.group(3);
                String nutrientsStr = matcher.group(4);

                // Parser les nutriments
                List<Double> nutrients = new ArrayList<>();
                String[] nutrientValues = nutrientsStr.split(",");
                for (String value : nutrientValues) {
                    try {
                        nutrients.add(Double.parseDouble(value.trim()));
                    } catch (NumberFormatException e) {
                        // Ignorer les valeurs invalides
                    }
                }

                // Créer l'ingrédient et l'ajouter au cache
                Ingredient ingredient = new Ingredient(id, nom, unite, nutrients);
                ingredientsCache.add(ingredient);
            }

            System.out.println("✓ " + ingredientsCache.size() + " ingrédients chargés depuis " + JSON_FILE_PATH);

        } catch (Exception e) {
            System.out.println("❌ Erreur lors du chargement du JSON : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== RECHERCHE D'INGRÉDIENT ==========
    /**
     * Recherche et charge un ingrédient à partir de son nom
     * @param nomRecherche Nom de l'ingrédient à rechercher
     */
    private void rechercherEtChargerIngredient(String nomRecherche) {
        if (ingredientsCache == null || ingredientsCache.isEmpty()) {
            System.out.println("❌ Aucune donnée d'ingrédients disponible");
            return;
        }

        // Recherche insensible à la casse
        String nomRechercheLower = nomRecherche.toLowerCase().trim();

        for (Ingredient ingredient : ingredientsCache) {
            String nomItem = ingredient.getNom().toLowerCase();

            // Recherche exacte ou partielle
            if (nomItem.equals(nomRechercheLower) || nomItem.contains(nomRechercheLower)) {
                this.id = ingredient.getId();
                this.nom = ingredient.getNom();
                this.unite = ingredient.getUnite();
                this.informationNutritionnelles = new ArrayList<>(ingredient.getInformationNutritionnelles());

                System.out.println("✓ Ingrédient trouvé : " + this.nom);
                return;
            }
        }

        System.out.println("❌ Ingrédient '" + nomRecherche + "' non trouvé");
        this.nom = nomRecherche;
        this.informationNutritionnelles = new ArrayList<>();
    }

    // ========== GETTERS ==========
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
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

    // ========== AFFICHAGE ==========
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════╗\n");
        sb.append("║ ID: ").append(id).append("\n");
        sb.append("║ NOM: ").append(nom).append("\n");
        sb.append("║ UNITÉ: ").append(unite).append("\n");
        sb.append("╠════════════════════════════════════╣\n");
        sb.append("║ INFORMATIONS NUTRITIONNELLES:\n");

        String[] labels = {"Énergie (kcal)", "Protéines (g)", "Lipides (g)",
                "Glucides (g)", "Fibres (g)", "Sodium (mg)", "Eau (g)"};

        if (informationNutritionnelles != null && !informationNutritionnelles.isEmpty()) {
            for (int i = 0; i < informationNutritionnelles.size() && i < labels.length; i++) {
                sb.append("║ • ").append(String.format("%-20s", labels[i]))
                        .append(": ").append(String.format("%.2f", informationNutritionnelles.get(i))).append("\n");
            }
        } else {
            sb.append("║ ⚠️  Aucune donnée disponible\n");
        }

        sb.append("╚════════════════════════════════════╝\n");
        return sb.toString();
    }

    // ========== MÉTHODES UTILITAIRES ==========
    /**
     * Affiche tous les ingrédients disponibles dans le JSON
     */
    public static void afficherTousLesIngreidents() {
        chargerIngredientsJSON();

        if (ingredientsCache == null || ingredientsCache.isEmpty()) {
            System.out.println("❌ Aucun ingrédient disponible");
            return;
        }

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║ TOUS LES INGRÉDIENTS DISPONIBLES");
        System.out.println("╠════════════════════════════════════╣");

        for (int i = 0; i < ingredientsCache.size(); i++) {
            Ingredient ingredient = ingredientsCache.get(i);
            System.out.printf("║ %3d. %-30s (%s)\n",
                    ingredient.getId(),
                    ingredient.getNom(),
                    ingredient.getUnite());
        }

        System.out.println("╚════════════════════════════════════╝\n");
    }

    /**
     * Retourne la liste de tous les ingrédients
     * @return ArrayList contenant tous les ingrédients
     */
    public static ArrayList<Ingredient> obtenirTousLesIngreidents() {
        chargerIngredientsJSON();
        return new ArrayList<>(ingredientsCache);
    }

    /**
     * Retourne la liste de tous les noms d'ingrédients
     * @return Liste des noms d'ingrédients
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
     * Crée un ingrédient à partir de son ID
     * @param id Identifiant de l'ingrédient
     * @return L'ingrédient correspondant, ou null si non trouvé
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
                        ingredient.getUnite(),
                        new ArrayList<>(ingredient.getInformationNutritionnelles())
                );
            }
        }

        return null;
    }

    /**
     * Recherche les ingrédients contenant un mot-clé
     * @param motCle Mot-clé à rechercher
     * @return ArrayList des ingrédients correspondants
     */
    public static ArrayList<Ingredient> rechercherParMotCle(String motCle) {
        chargerIngredientsJSON();
        ArrayList<Ingredient> resultats = new ArrayList<>();
        String motCleLower = motCle.toLowerCase();

        if (ingredientsCache != null) {
            for (Ingredient ingredient : ingredientsCache) {
                if (ingredient.getNom().toLowerCase().contains(motCleLower)) {
                    resultats.add(ingredient);
                }
            }
        }

        return resultats;
    }

    /**
     * Affiche les ingrédients d'une catégorie (par numéro ou critère)
     * @param debut Index de début
     * @param fin Index de fin
     */
    public static void afficherIngreidientsPage(int debut, int fin) {
        chargerIngredientsJSON();

        if (ingredientsCache == null || ingredientsCache.isEmpty()) {
            System.out.println("❌ Aucun ingrédient disponible");
            return;
        }

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║ INGRÉDIENTS " + (debut + 1) + " À " + Math.min(fin, ingredientsCache.size()));
        System.out.println("╠════════════════════════════════════╣");

        for (int i = debut; i < Math.min(fin, ingredientsCache.size()); i++) {
            Ingredient ingredient = ingredientsCache.get(i);
            System.out.printf("║ %3d. %-30s (%s)\n",
                    ingredient.getId(),
                    ingredient.getNom(),
                    ingredient.getUnite());
        }

        System.out.println("╚════════════════════════════════════╝\n");
    }

    // ========== EXEMPLE D'UTILISATION ==========
    public static void main(String[] args) {
        try {
            System.out.println("=== CHARGEMENT DES INGRÉDIENTS ===\n");

            // Afficher tous les ingrédients
            Ingredient.afficherTousLesIngreidents();

            // Créer un ingrédient par nom
            System.out.println("=== RECHERCHE PAR NOM ===\n");
            Ingredient pomme = new Ingredient("Apple");
            System.out.println(pomme);

            Ingredient poulet = new Ingredient("chicken");
            System.out.println(poulet);

            // Créer un ingrédient par ID
            System.out.println("=== RECHERCHE PAR ID ===\n");
            Ingredient riz = Ingredient.creerParId(29);
            if (riz != null) {
                System.out.println(riz);
            }

            // Recherche par mot-clé
            System.out.println("=== RECHERCHE PAR MOT-CLÉ (cheese) ===\n");
            ArrayList<Ingredient> cheeses = Ingredient.rechercherParMotCle("cheese");
            System.out.println("Trouvé " + cheeses.size() + " résultats:");
            for (Ingredient cheese : cheeses) {
                System.out.println("  • " + cheese.getNom());
            }

            // Afficher une page
            System.out.println("\n=== AFFICHAGE PAR PAGE ===\n");
            Ingredient.afficherIngreidientsPage(0, 10);

            // Créer manuellement (ancien code)
            System.out.println("=== CRÉATION MANUELLE ===\n");
            Ingredient manuel = new Ingredient("Carotte", "g", Arrays.asList(41.0, 0.93, 0.24, 9.58, 2.8, 69.0, 88.29));
            System.out.println(manuel);

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}