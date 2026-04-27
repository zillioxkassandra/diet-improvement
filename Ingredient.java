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

    /** Unité de mesure de l'ingrédient (ex : g, kg, ml) */
    private String unite;

    /** Liste des informations nutritionnelles (ex : calories, protéines, etc.) */
    private List<Double> informationNutritionnelles;

    public Ingredient(String nom,String unite, List<Double> informationNutritionnelles) {
        this.nom = nom;
        this.unite = unite;
        this.informationNutritionnelles = informationNutritionnelles;
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

    @Override
    public String toString() {
        return "Ingredient : " + "nom = " + nom + ", unite = "
                + unite + ", info Nutri. = " + informationNutritionnelles;
    }

    public static void main(String[] args) {

    }
}
