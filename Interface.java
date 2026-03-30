import javax.swing.*;
import java.awt.*;

public class Interface {
    public static void main(String[] args) {

        // ===== FENETRE PRINCIPALE =====
        JFrame accueil = new JFrame("Application de gestion d'apports nutritifs");
        accueil.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        accueil.setExtendedState(JFrame.MAXIMIZED_BOTH);
        accueil.setLayout(new BorderLayout());
        accueil.getContentPane().setBackground(new Color(245, 245, 245)); // fond général

        // ===== BOUTONS SUD =====
        JButton quitter = new JButton("Quitter");
        JButton valider = new JButton("Valider");

        // Couleurs des boutons
        valider.setBackground(new Color(102, 204, 102));
        valider.setForeground(Color.WHITE);
        quitter.setBackground(new Color(255, 102, 102));
        quitter.setForeground(Color.WHITE);

        JPanel nord = new JPanel(new GridLayout(1,2));
        nord.setBackground(new Color(245, 245, 245));
        JPanel sud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sud.setBackground(new Color(245, 245, 245));

        // ===== BASE =====
        JPanel base = new JPanel(new FlowLayout(FlowLayout.LEFT));
        base.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                "Ingédients de la base de donnée"));
        base.setBackground(new Color(230, 230, 230));

        // Liste d'ingrédients possibles
        String[] ingredients = {"Pomme", "Banane", "Carotte", "Tomate", "Lait", "Oeuf", "Fromage", "Poulet", "Riz", "Haricot"};

        JComboBox<String> recherche = new JComboBox<>(ingredients);
        recherche.setEditable(true);
        recherche.setSelectedIndex(-1);
        recherche.setBackground(Color.WHITE);

        JPanel ligneBase = new JPanel(new BorderLayout());
        ligneBase.setPreferredSize(new Dimension(750, 40));
        ligneBase.setBackground(new Color(230, 230, 230));

        JButton plus = new JButton("+");
        plus.setBackground(new Color(200, 200, 200));

        ligneBase.add(plus, BorderLayout.WEST);
        ligneBase.add(recherche, BorderLayout.CENTER);

        base.add(ligneBase);

        // ===== UTILISATEUR =====
        JPanel utilisateur = new JPanel(new GridLayout(10,1,5,5));
        utilisateur.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                "Tableau des ingédients de l'utilisateur"));
        utilisateur.setBackground(new Color(230, 230, 230));

        JTextField[] champsUtilisateur = new JTextField[10]; // pour accéder facilement aux JTextField

        for(int i=0; i<10; i++){
            JPanel ligne = new JPanel(new BorderLayout());
            ligne.setBackground(new Color(230, 230, 230));

            JButton moins = new JButton("-");
            moins.setBackground(new Color(200, 200, 200));

            JSpinner quantite = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
            quantite.setPreferredSize(new Dimension(60, 30));
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(quantite, "#");
            quantite.setEditor(editor);
            editor.getTextField().setHorizontalAlignment(JTextField.CENTER);

            JTextField ingredient = new JTextField();
            ingredient.setBackground(Color.WHITE);
            champsUtilisateur[i] = ingredient;

            ligne.add(moins, BorderLayout.WEST);
            ligne.add(ingredient, BorderLayout.CENTER);

            JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            droite.setBackground(new Color(230, 230, 230));
            droite.add(quantite);
            droite.add(new JLabel("g"));

            ligne.add(droite, BorderLayout.EAST);

            utilisateur.add(ligne);
        }

        plus.addActionListener(e -> {
            String selection = (String) recherche.getSelectedItem();
            if(selection != null && !selection.isEmpty()){
                champsUtilisateur[0].setText(selection);
            }
        });

        quitter.addActionListener(e -> accueil.dispose());

        // ===== SUD =====
        sud.add(valider);
        sud.add(quitter);

        // ===== NORD =====
        nord.add(base);
        nord.add(utilisateur);

        accueil.add(nord, BorderLayout.CENTER);
        accueil.add(sud, BorderLayout.SOUTH);

        accueil.setVisible(true);
    }
}
