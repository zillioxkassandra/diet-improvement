import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        GestionUtilisateurs.initialiser();
        SwingUtilities.invokeLater(Interface::afficherLogin);
    }
}
