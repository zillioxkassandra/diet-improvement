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

    public Repas() {

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


    public ArrayList<Double> calculer() {
        ArrayList<Double> infosNutriFinale = new ArrayList<>() ;

        if (!ListConsommable.isEmpty()) {
            Ingredient firstIngredient = ListConsommable.get(0).GetIngredient();
            int nbCategories = firstIngredient.getInformationNutritionnelles().size();
            for (int j = 0; j < nbCategories; j++) {
                infosNutriFinale.add(0.0);
            }
        }

        for (Consommable consommable : ListConsommable) {
            Ingredient ingredient  = consommable.GetIngredient();
            Double qte = consommable.GetQuantite();
            List<Double> infoNutri = ingredient.getInformationNutritionnelles();

            for (int i = 0; i < infoNutri.size(); i++) {
                Double nutri = qte * infoNutri.get(i);
                Double valeurActuelle = infosNutriFinale.get(i);
                infosNutriFinale.set(i, valeurActuelle + nutri);
            }

        }
        System.out.println(infosNutriFinale);
        return  infosNutriFinale ;
    }


    public void ajouter(Consommable c){
        ListConsommable.add(c);
    }

    @Override
    public String toString() {

        String message = "";
        for (Consommable consommable : ListConsommable){
            String nom = consommable.GetIngredient().getNom();
            List<Double> infosNutri = consommable.GetIngredient().getInformationNutritionnelles();
            message +=   ", ingredient = " + nom + ", " + " infosNutritionnelles = " + infosNutri;
        }

        return "Repas : " + nom + " " + message;

    }

    public Ingredient findFlaw() {
        Ingredient ingredientFautif = null;
        Double nutri_max = 0.0;
        int indexFlaw = -1;

        for (Consommable consommable : ListConsommable) {
            Ingredient ingredient = consommable.GetIngredient();
            List<Double> infosNutri = ingredient.getInformationNutritionnelles();

            for (int i = 0; i < infosNutri.size(); i++) {
                Double nutri = infosNutri.get(i);
                if (nutri > 5.0 && nutri > nutri_max) {  // Garder la valeur max
                    nutri_max = nutri;
                    ingredientFautif = ingredient;
                    indexFlaw = i;
                }
            }
        }

        if (ingredientFautif != null) {
            System.out.println("Ingrédient problématique : " + ingredientFautif.getNom());
            System.out.println("Valeur dépassée : " + nutri_max + " à l'index " + indexFlaw);
        } else {
            System.out.println("✓ Tous les ingrédients sont dans les normes");
        }

        return ingredientFautif;
    }


    public static void main(String[] args) {

        Ingredient i1 = new Ingredient("harcot","g",Arrays.asList(0.05,4.2));
        Ingredient i2 = new Ingredient("pâtes","g",Arrays.asList(5.3,3.6));
        Consommable c1 = new Consommable(12,100,i1);
        Consommable c2 = new Consommable(12,500,i2);


        Repas repas = new Repas(new ArrayList<Consommable>(),"pâte haricot");
        repas.ajouter(c1);
        repas.ajouter(c2);

        System.out.println(repas.toString());

        repas.calculer();

        repas.findFlaw();















    }



}
