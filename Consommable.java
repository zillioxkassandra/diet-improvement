import java.util.*;
/**
 * La classe Consommable représente un ingrédient consommé
 * avec une quantité spécifique.
 * 
 * Elle permet de lier un ingrédient à une quantité donnée
 * dans le cadre d’un repas.
 */
public class Consommable  {
    private int id;
    private double quantite;
    private Ingredient ingredient;
    /**
     * Constructeur de la classe Consommable
     * 
     * @param id Identifiant du consommable
     * @param quantite Quantité consommée
     * @param ingredient Ingrédient associé
     */
    public Consommable(int id, double quantite, Ingredient ingredient) {
        this.id = id;
        this.quantite = quantite;
        this.ingredient = ingredient;
    }
/**
 *getter et setter
 */
    public int GetId(){
        return id;
    }
    public double GetQuantite(){
        return quantite;
    }
    public Ingredient GetIngredient(){
        return ingredient;
    }
    public void setQuantite(float quantite){
        this.quantite=quantite;
    }
    public void setIngredient(Ingredient ingredient ){
        this.ingredient=ingredient;
    }

    public static void main(String[] args) {
        Ingredient i1 = new Ingredient("harcot","kg",Arrays.asList(12.0,14.2));
        Consommable c = new Consommable(12,12.0,i1);
        System.out.println(c.GetIngredient().toString());
    }

}

