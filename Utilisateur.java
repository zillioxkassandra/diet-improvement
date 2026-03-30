public class Utilisateur {

    private String identifiant;
    private boolean valider = false;

    public Utilisateur(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getIdentifiant() {
        return this.identifiant;
    }

    public void setValider(boolean valider) {
        this.valider = valider;
    }

    public boolean isValider() {
        return this.valider;
    }

    public static void main(String[] args) {
        Utilisateur utilisateur = new Utilisateur("Noé");
        System.out.println(utilisateur.getIdentifiant());
        System.out.println(utilisateur.isValider());
        utilisateur.setValider(true);
        System.out.println(utilisateur.isValider());


    }


}
