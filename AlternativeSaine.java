import java.util.ArrayList;
import java.util.Arrays;

public class AlternativeSaine {

    public Consommable alternative_saine;
    public ArrayList<Repas> repas;

    public void alt(Ingredient ingredient, float qte) {
        // Compute nutri values and find the worst
        //from bdd in same food category : 
            // find better than the worst
        //set alternative_saine to better
    };
    public void AfficherInfoAlt() {
        // Ask bdd for nutri values
        // print infos
    };

    public static void main(String[] args) {
        Ingredient i1 = new Ingredient("harcot","g", Arrays.asList(12.0,14.2));
    }
}
