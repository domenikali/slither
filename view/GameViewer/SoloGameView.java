package view.GameViewer;

import controller.GameController;
import model.Food;
import view.GameViewer.GameView;

import java.awt.*;
import javax.swing.*;


public class SoloGameView extends GameView {

    private GameController gc;
    private Image background;

    private Image[] foodImage;
    private Image snakeImage;

    private JLabel timerLabel;


    public SoloGameView() {
        this.gc=new GameController(false,this);
        background = new ImageIcon(this.getClass().getResource("/ressources/background.PNG")).getImage();
        snakeImage=new ImageIcon(this.getClass().getResource("/ressources/serpent.png")).getImage();
        foodImage=new Image[4];
        foodImage[0] = new ImageIcon(this.getClass().getResource("/ressources/food.png")).getImage();
        foodImage[1] = new ImageIcon(this.getClass().getResource("/ressources/food1.png")).getImage();
        foodImage[2] = new ImageIcon(this.getClass().getResource("/ressources/food2.png")).getImage();
        foodImage[3] = new ImageIcon(this.getClass().getResource("/ressources/food3.png")).getImage();

        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE);
        add(timerLabel);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 1100, 600, this);


        // Calcul des décalages pour centrer le serpent
        int decalageX = getWidth() / 2 - gc.getGp().getSnake().getBody().get(0).getX();
        int decalageY = getHeight() / 2 - gc.getGp().getSnake().getBody().get(0).getY();

        // Dessin du fond en fonction des décalages
        for (int i = 0; i < gc.getGp().getSnake().getBody().size(); i++) {
            int x = gc.getGp().getSnake().getBody().get(i).getX() + decalageX;
            int y = gc.getGp().getSnake().getBody().get(i).getY() + decalageY;

            g.drawImage(snakeImage, x, y, 15, 15, this);


        }
        timerLabel.setText("Time: " + gc.getGp().getRemainingTime() + "s");

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Score: " + gc.getGp().getScore(), 10, 20);

        //using a standard for loop fix the ConcurrentModificationException
        for (int i = 0; i < gc.getGp().getFoods().size(); i++) {
            Food food = gc.getGp().getFoods().get(i);
            int x = food.getX() + decalageX;
            int y = food.getY() + decalageY;

            g.drawImage(foodImage[food.getColor()], x, y, 10, 10, this);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3)); // Épaisseur de la ligne
        g2d.drawLine( decalageX, -100 + decalageY, 1550 + decalageX, -100 + decalageY);
        g2d.drawLine(1550 + decalageX, -100 + decalageY, 1550 + decalageX, 1550 + decalageY);
        g2d.drawLine(decalageX, -100 + decalageY,  decalageX, 1550 + decalageY);
        g2d.drawLine( decalageX, 1550 + decalageY, 1550 + decalageX, 1550 + decalageY);

    }

    public void updateTimerLabel() {
        if(!(timerLabel==null))
            timerLabel.setText("Time: " + gc.getGp().getRemainingTime() + "s");
        repaint();
    }

    public GameController getGc() {
        return gc;
    }

}
