package view;

import Net.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;

public class ConnectionMenu extends JFrame {
    private Client client;
    public ConnectionMenu(){
        setTitle("Slither.io Menu");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null); // Use null layout manager for absolute positioning

        JButton join = new JButton("Join");

        join.setBackground(new Color(0, 255, 0)); // Couleur verte
        join.setForeground(Color.WHITE); // Texte blanc
        join.setFont(new Font("Arial", Font.BOLD, 20));
        join.setBounds(200, 150, 200, 50);

        add(join);

        JTextField usernameTextBox=new JTextField("your username");
        usernameTextBox.setBounds(200, 230, 200, 50);
        usernameTextBox.setBackground(null);
        usernameTextBox.setBorder(null);
        //usernameTextBox.setText("insert username");
        usernameTextBox.setToolTipText("your username");
        usernameTextBox.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                usernameTextBox.setText("");
            }
        });
        add(usernameTextBox);



        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Fermer le menu et lancer le jeu
                dispose(); // Ferme la fenêtre du menu
                String username=usernameTextBox.getText();
                try {
                    Socket socket = new Socket("localhost",1234);
                    Client client = new Client(socket,username);
                    new Thread(client::listenForMessage).start();
                    new Thread(client::sendMessage).start();
                }catch (IOException ignore){

                }
                new GameWindow(false); // Lance la fenêtre du jeu
            }
        });

        JLabel titleLabel = new JLabel("Slither.io Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setBounds(210, 50, 300, 50);
        add(titleLabel);

        ImageIcon backgroundImage = new ImageIcon("C:\\yassine\\Java\\Projectsss\\NewSlither\\ressources\\MenuBackgound.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        add(backgroundLabel);

        getContentPane().setBackground(Color.BLACK); // Fond noir
        setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
        setVisible(true);
    }
}
