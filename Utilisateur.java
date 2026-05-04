import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Login {
    public static void main(String[] args) {

        JFrame loginFrame = new JFrame("Authentification");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(350,200);
        loginFrame.setLayout(new GridLayout(4,2,5,5));

        JLabel userLabel = new JLabel("Utilisateur :");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Mot de passe :");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Se connecter");
        JButton registerButton = new JButton("Créer un compte");

        loginFrame.add(userLabel);
        loginFrame.add(userField);
        loginFrame.add(passLabel);
        loginFrame.add(passField);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        // ===== CONNEXION =====
        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if(checkUser(user, pass)){
                loginFrame.dispose();
                Interface.main(new String[]{user});
            } else {
                JOptionPane.showMessageDialog(loginFrame,
                        "Identifiants incorrects",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // ===== CREATION COMPTE =====
        registerButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if(user.isEmpty() || pass.isEmpty()){
                JOptionPane.showMessageDialog(loginFrame,
                        "Remplis tous les champs",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(userExists(user)){
                JOptionPane.showMessageDialog(loginFrame,
                        "Utilisateur déjà existant",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true));
                writer.write(user + ";" + pass);
                writer.newLine();
                writer.close();

                JOptionPane.showMessageDialog(loginFrame,
                        "Compte créé !");
            } catch(IOException ex){
                ex.printStackTrace();
            }
        });

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    // ===== VERIFIER LOGIN =====
    private static boolean checkUser(String user, String pass){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;

            while((line = reader.readLine()) != null){
                String[] parts = line.split(";");

                if(parts[0].equals(user) && parts[1].equals(pass)){
                    reader.close();
                    return true;
                }
            }

            reader.close();
        } catch(IOException e){
        }
        return false;
    }

    // ===== VERIFIER SI USER EXISTE =====
    private static boolean userExists(String user){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;

            while((line = reader.readLine()) != null){
                String[] parts = line.split(";");

                if(parts[0].equals(user)){
                    reader.close();
                    return true;
                }
            }

            reader.close();
        } catch(IOException e){
        }
        return false;
    }
}
