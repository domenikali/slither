package view;



import controller.GameController;
import model.Snake;

import javax.swing.*;
import java.awt.*;


public class GameWindow extends JFrame {

    private GameController gc;

    public GameWindow(Boolean b){

        setTitle("Slither.io");
        setSize(getWindowWidth(),getWindowHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GameView g = new GameView(b);
        setContentPane(g);

        addKeyListener(g.getGc());
        addMouseListener(g.getGc());
        addMouseMotionListener(g.getGc());

        setVisible(true);
    }
    public static int  getWindowWidth(){
        return  1100;
    }
    public static int  getWindowHeight(){
        return  600;
    }






}
