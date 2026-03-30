import java.util.List;

/**
 * La classe Ingredient représente un ingrédient avec ses informations
 * principales
 * telles que son nom, son identifiant, sa famille, son unité de mesure et ses
 * informations nutritionnelles.
 */
public class Ingredient {

    /** Nom de l'ingrédient */
    private String nom;

    /** Identifiant unique de l'ingrédient */
    private int id;

    /** Identifiant de la famille de l'ingrédient */
    private int nomFamille;

    /** Unité de mesure de l'ingrédient (ex : g, kg, ml) */
    private String unite;

    /** Liste des informations nutritionnelles (ex : calories, protéines, etc.) */
    private List<Double> informationNutritionnelles;

    public Ingredient(String nom, int id, int nomFamille, String unite, List<Double> informationNutritionnelles) {
        this.nom = nom;
        this.id = id;
        this.nomFamille = nomFamille;
        this.unite = unite;
        this.informationNutritionnelles = informationNutritionnelles;
    }

    public String getNom() {
        return nom;
    }

    public int getId() {
        return id;
    }

    public int getNomFamille() {
        return nomFamille;
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

    @Override
    public String toString() {
        return "Ingredient : " + "nom = " + nom + ", id =" + id + ", nom de famille = " + nomFamille + ", unite = "
                + unite + ", info Nutri. = " + informationNutritionnelles;
    }
}
