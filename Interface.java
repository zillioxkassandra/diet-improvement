import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Interface {

    public static void main(String[] args) {

        String username = args[0]; // utilisateur connecté

        JFrame accueil = new JFrame("Application - " + username);
        accueil.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        accueil.setExtendedState(JFrame.MAXIMIZED_BOTH);
        accueil.setLayout(new BorderLayout());

        JButton quitter = new JButton("Quitter");
        JButton valider = new JButton("Valider");

        JPanel nord = new JPanel(new GridLayout(1,2));
        JPanel sud = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // ===== BASE =====
        JPanel base = new JPanel(new FlowLayout(FlowLayout.LEFT));
        base.setBorder(BorderFactory.createTitledBorder("Base de données"));

        String[] ingredients = {"Pomme","Banane","Carotte","Tomate","Lait","Oeuf","Fromage","Poulet","Riz","Haricot"};

        JComboBox<String> recherche = new JComboBox<>(ingredients);
        recherche.setEditable(true);
        recherche.setSelectedIndex(-1);

        JPanel ligneBase = new JPanel(new BorderLayout());
        ligneBase.setPreferredSize(new Dimension(400,40));

        JButton plus = new JButton("+");

        ligneBase.add(plus, BorderLayout.WEST);
        ligneBase.add(recherche, BorderLayout.CENTER);

        base.add(ligneBase);

        // ===== UTILISATEUR =====
        JPanel utilisateur = new JPanel(new GridLayout(10,1,5,5));
        utilisateur.setBorder(BorderFactory.createTitledBorder("Utilisateur"));

        JTextField[] champs = new JTextField[10];
        JSpinner[] quantites = new JSpinner[10];

        for(int i=0; i<10; i++){

            JPanel ligne = new JPanel(new BorderLayout());

            JButton moins = new JButton("-");

            JTextField ingredient = new JTextField();
            champs[i] = ingredient;

            JSpinner quantite = new JSpinner(new SpinnerNumberModel(0,0,1000,1));
            quantite.setPreferredSize(new Dimension(60,30));
            quantites[i] = quantite;

            JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
            droite.add(quantite);
            droite.add(new JLabel("g"));

            ligne.add(moins, BorderLayout.WEST);
            ligne.add(ingredient, BorderLayout.CENTER);
            ligne.add(droite, BorderLayout.EAST);

            int index = i;

            // bouton -
            moins.addActionListener(e -> {
                champs[index].setText("");
                quantites[index].setValue(0);
            });

            utilisateur.add(ligne);
        }

        // ===== BOUTON + =====
        plus.addActionListener(e -> {
            String selection = (String) recherche.getSelectedItem();

            if(selection != null && !selection.isEmpty()){
                for(int i=0;i<10;i++){
                    if(champs[i].getText().isEmpty()){
                        champs[i].setText(selection);
                        break;
                    }
                }
            }
        });

        // ===== SAUVEGARDE =====
        valider.addActionListener(e -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(username + ".txt"));

                for(int i=0;i<10;i++){
                    String ing = champs[i].getText();
                    int q = (int) quantites[i].getValue();

                    writer.write(ing + ";" + q);
                    writer.newLine();
                }

                writer.close();
                JOptionPane.showMessageDialog(accueil,"Données sauvegardées !");
            } catch(IOException ex){
                ex.printStackTrace();
            }
        });

        // ===== CHARGEMENT =====
        try {
            BufferedReader reader = new BufferedReader(new FileReader(username + ".txt"));

            String ligne;
            int i=0;

            while((ligne = reader.readLine()) != null && i<10){
                String[] parts = ligne.split(";");

                champs[i].setText(parts[0]);
                quantites[i].setValue(Integer.parseInt(parts[1]));

                i++;
            }

            reader.close();

        } catch(IOException ex){
        }

        quitter.addActionListener(e -> accueil.dispose());

        sud.add(valider);
        sud.add(quitter);

        nord.add(base);
        nord.add(utilisateur);

        accueil.add(nord, BorderLayout.CENTER);
        accueil.add(sud, BorderLayout.SOUTH);

        accueil.setVisible(true);
    }
}
