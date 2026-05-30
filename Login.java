import javax.swing.*;
import java.awt.*;

public class Login {
    public static void main(String[] args) {
        JFrame loginFrame = new JFrame("Authentification");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300,150);
        loginFrame.setLayout(new GridLayout(3,2,5,5));

        JLabel userLabel = new JLabel("Utilisateur :");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Mot de passe :");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Se connecter");

        loginFrame.add(userLabel);
        loginFrame.add(userField);
        loginFrame.add(passLabel);
        loginFrame.add(passField);
        loginFrame.add(new JLabel()); 
        loginFrame.add(loginButton);

        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            JOptionPane.showMessageDialog(loginFrame,
                        "Utilisateur ou mot de passe incorrect !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);

        });

        loginFrame.getRootPane().setDefaultButton(loginButton);

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
}
