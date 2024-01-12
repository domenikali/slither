package view;

import Net.Client;
import view.GameViewer.GameView;
import view.GameViewer.PvPGameView;
import view.GameViewer.ServerGameView;
import view.GameViewer.SoloGameView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GameWindow extends JFrame {

    public GameWindow(Boolean PvP, Client client,Boolean AI){

        GameView g;

        setTitle("Slither.io");
        setSize(getWindowWidth(),getWindowHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //change istance of gameView depending on game mod
        if(client!=null)
            g = new ServerGameView(client);
        else{
            if(PvP) {
                g = new PvPGameView();
            }else if(AI) {
                g = new SoloGameView(true);
            }else{
                g = new SoloGameView(false);
            }

        }
        setContentPane(g);

        addKeyListener(g.getGc());
        addMouseListener(g.getGc());
        addMouseMotionListener(g.getGc());

        setVisible(true);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {

                SwingUtilities.invokeLater(MenuFrame::new);
                if(client!=null)
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
