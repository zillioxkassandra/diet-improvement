import java.util.*;

/**
 * La Classe Repas
 * Elle possède plusieurs attributs publics : la liste de consommables qui ont été sélectionnés et un nom.
 * Elle possède plusieurs méthodes qui vont interagir avec la liste,
 * comme calculer les valeurs nutritionnelles totales,
 * ajouter des consommables ou rechercher lequel est le plus mauvais en termes d’impact nutritionnel dans le repas
 * et celle qui affichera les informations nutritionnelles du repas.
 */
public class Repas {
    private ArrayList<Consommable> ListConsommable;
    private String nom;

    public Repas(ArrayList<Consommable> ListConsommable, String nom) {
        this.ListConsommable = ListConsommable;
        this.nom = nom;
    }

    /// Getter & Setter
    public ArrayList<Consommable> getListConsommable() {
        return ListConsommable;
    }

    public void setListConsommable(ArrayList<Consommable> listConsommable) {
        ListConsommable = listConsommable;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {}

/* BUG A TERMINEE
    public ArrayList<Double> calculer(){
        List<Double> infonutrifinale ;
        for (int i = 0; i < ListConsommable.size(); i++){
            Consommable c = ListConsommable.get(i);
            Ingredient ingredient  = c.GetIngredient();
            List<Double> infonutri = ingredient.getInformationNutritionnelles();
            for (int j = 0; j < infonutri.size(); j++){
                double score = infonutri.get(j)* c.GetQuantite();
                infonutrifinale.add(j, score);
            }

        }
        return  infonutrifinale ;
    }
*/

    public void ajouter(Consommable c){
        ListConsommable.add(c);
    }



}
