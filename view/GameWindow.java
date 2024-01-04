package view;

import Net.Client;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GameWindow extends JFrame {

    public GameWindow(Boolean b, Client client){

        setTitle("Slither.io");
        setSize(getWindowWidth(),getWindowHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        ServerGameView g = new ServerGameView(b,client);
        setContentPane(g);


        addMouseListener(g.getGc());
        addMouseMotionListener(g.getGc());

        setVisible(true);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                //client.write("CLIENT:"+client.getUserName()+" hasLeft");
                client.close();
                e.getWindow().dispose();
            }
        });
    }
    public static int  getWindowWidth(){
        return  1100;
    }
    public static int  getWindowHeight(){
        return  600;
    }
}
