package view;

import controller.GameController;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame {


    public MenuFrame( ) {
        setTitle("Slither.io Menu");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null); // Use null layout manager for absolute positioning



        // Créer les boutons du menu
        JButton soloButton = new JButton("Online");
        JButton pvspButton = new JButton("PvsP");
        JButton quitButton = new JButton("Quit");

        // Style des boutons
        soloButton.setBackground(new Color(0, 255, 0)); // Couleur verte
        soloButton.setForeground(Color.WHITE); // Texte blanc
        soloButton.setFont(new Font("Arial", Font.BOLD, 20));

        pvspButton.setBackground(new Color(0, 255, 0)); // Couleur verte
        pvspButton.setForeground(Color.WHITE); // Texte blanc
        pvspButton.setFont(new Font("Arial", Font.BOLD, 20));

        quitButton.setBackground(new Color(255, 0, 0)); // Couleur rouge
        quitButton.setForeground(Color.WHITE); // Texte blanc
        quitButton.setFont(new Font("Arial", Font.BOLD, 20));

        soloButton.setBounds(200, 150, 200, 50);
        pvspButton.setBounds(200, 230, 200, 50);
        quitButton.setBounds(200, 310, 200, 50);

        // Ajouter des actions aux boutons
        soloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fermer le menu et lancer le jeu
                dispose(); // Ferme la fenêtre du menu
                new GameWindow(false,null); // Lance la fenêtre du jeu
            }
        });

        pvspButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fermer le menu et lancer le jeu
                dispose(); // Ferme la fenêtre du menu
                new GameWindow(true,null); // Lance la fenêtre du jeu
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Quitte l'application
            }
        });

        add(soloButton);
        add(pvspButton);
        add(quitButton);

        // Mettre en forme le menu
        JLabel titleLabel = new JLabel("Slither.io Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setBounds(210, 50, 300, 50);
        add(titleLabel);


        // Load the background image with an absolute path
        ImageIcon backgroundImage = new ImageIcon("C:\\yassine\\Java\\Projectsss\\NewSlither\\ressources\\MenuBackgound.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        add(backgroundLabel);

        getContentPane().setBackground(Color.BLACK); // Fond noir
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
        setVisible(true);
    }


    public static void main(String[] args) {
        new MenuFrame().setVisible(true);
    }
}
