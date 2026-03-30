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
    ArrayList<Consomable> ListConsumable;
    String nom;

    public Repas(ArrayList<Consomable> ListConsomable, String nom) {
        this.ListConsumable = ListConsomable;
        this.nom = nom;
    }

    /// Getteur & Accesseur
    public ArrayList<Consomable> getListConsomable() {
        return ListConsumable;
    }

    public void setListConsomable(ArrayList<Consomable> listConsomable) {
        ListConsumable = listConsomable;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {}

    /*public ArrayList<int> calculer(){
        return list
    }
    */

    public void ajouter(Consomable c){
        ListConsumable.add(c);
    }



}
