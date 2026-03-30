/**
 * La classe Consommable représente un ingrédient consommé
 * avec une quantité spécifique.
 * 
 * Elle permet de lier un ingrédient à une quantité donnée
 * dans le cadre d’un repas.
 */
public class Consommable extends Ingredient {
    private String id;
    private float quantite;
    private Ingredient ingredient;
    /**
     * Constructeur de la classe Consommable
     * 
     * @param id Identifiant du consommable
     * @param quantite Quantité consommée
     * @param ingredient Ingrédient associé
     */
    public Consommable(String id, float quantite, Ingredient ingredient) {
        this.id = id;
        this.quantite = quantite;
        this.ingredient = ingredient;
    }
/**
 *getter et setter
 */
    public String GetId(){
        return id;
    }
    public float GetQuantite(){
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



}

