package view;


import Net.Client;
import model.Pair;
import Net.Serialize;
import controller.GameController;
import model.Food;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class GameView extends JPanel {

    private final GameController gc;
    private final Image background;

    private boolean modePvsP;

    private final Image[] foodImage;
    private final Image snakeImage;
    private final Image snake2Image;

    private final JLabel timerLabel;

    private final Client client;




    public GameView(Boolean b, Client client) {
        //System.out.println(client.toString());
        this.client=client;
        this.gc=new GameController(b,this,client);

        this.modePvsP=b;
        background = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/background.PNG"))).getImage();
        snakeImage=new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/serpent.png"))).getImage();
        snake2Image=new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/serpent2.png"))).getImage();
        foodImage=new Image[4];
        foodImage[0] = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/food.png"))).getImage();
        foodImage[1] = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/food1.png"))).getImage();
        foodImage[2] = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/food2.png"))).getImage();
        foodImage[3] = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/food3.png"))).getImage();

        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE);
        add(timerLabel);

    }

    public void showMenu() {
        SwingUtilities.invokeLater(() -> {
            new MenuFrame();
            closeCurrentGameWindow();
        });
    }

    protected void paintComponent(Graphics g) {
        if(!modePvsP) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, 1100, 600, this);
            if(client.getMessageFromServer()!=null) {


                Map<String,List<Pair>> snakes = Serialize.deserializeSnakes(client.getSnakes());
                List<Pair> foods = Serialize.deserializeSnake(client.getFoods());
                int offsetX = 0;
                int offsetY =0;
                offsetX = getWidth() / 2 - snakes.get(client.getUserName()).get(0).getX();
                offsetY = getHeight() / 2 - snakes.get(client.getUserName()).get(0).getY();
                for(Map.Entry<String,List<Pair>> entry : snakes.entrySet()) {
                    List<Pair> snakePos = entry.getValue();
                    g.drawString(entry.getKey(),snakePos.getFirst().getX()+offsetX-(entry.getKey().length()*3),snakePos.getFirst().getY()+offsetY);

                    for (int i = 0; i < snakePos.size(); i++) {
                        int x = snakePos.get(i).getX() + offsetX;
                        int y = snakePos.get(i).getY() + offsetY;
                        g.drawImage(snakeImage, x, y, 15, 15, this);
                    }
                }

            /*
            // Calcul des décalages pour centrer le serpent
            int offsetX = getWidth() / 2 - gc.getGp().getSnake().getBody().get(0).getX();
            int offsetY = getHeight() / 2 - gc.getGp().getSnake().getBody().get(0).getY();

            // Dessin du fond en fonction des décalages
            for (int i = 0; i < gc.getGp().getSnake().getBody().size(); i++) {
                int x = gc.getGp().getSnake().getBody().get(i).getX() + offsetX;
                int y = gc.getGp().getSnake().getBody().get(i).getY() + offsetY;

                g.drawImage(snakeImage, x, y, 15, 15, this);


            }

             */
                //timerLabel.setText("Time: " + gc.getGp().getRemainingTime() + "s");

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Score: " + (snakes.get(client.getUserName()).size()-5), 10, 20);

                for(int i=0;i<foods.size();i++){
                    g.drawImage(foodImage[foods.get(i).getX()%4],foods.get(i).getX()+offsetX,foods.get(i).getY()+offsetY,10,10,this);
                }
                //using a standard for loop fix the ConcurrentModificationException
                /*
                for (int i = 0; i < gc.getGp().getFoods().size(); i++) {
                    Food food = gc.getGp().getFoods().get(i);
                    int x = food.getX() ;
                    int y = food.getY() ;

                    g.drawImage(foodImage[food.getColor()], x, y, 10, 10, this);
                }

                 */
                /*
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3)); // Épaisseur de la ligne
                g2d.drawLine(offsetX, -100 + offsetY, 1550 + offsetX, -100 + offsetY);
                g2d.drawLine(1550 + offsetX, -100 + offsetY, 1550 + offsetX, 1550 + offsetY);
                g2d.drawLine(offsetX, -100 + offsetY, offsetX, 1550 + offsetY);
                g2d.drawLine(offsetX, 1550 + offsetY, 1550 + offsetX, 1550 + offsetY);

                 */

            }
        }else{
            super.paintComponent(g);
            g.drawImage(background, 0, 0, 1100, 600, this);

            for (int i = 0; i < gc.getGp().getSnake().getBody().size(); i++) {
                g.drawImage(snakeImage, gc.getGp().getSnake().getBody().get(i).getX(), gc.getGp().getSnake().getBody().get(i).getY(), 15, 15, this);
            }

            for (int i = 0; i < gc.getGp().getSnake2().getBody().size(); i++) {
                g.drawImage(snake2Image, gc.getGp().getSnake2().getBody().get(i).getX(), gc.getGp().getSnake2().getBody().get(i).getY(), 15, 15, this);
            }

            for (int i = 0; i < gc.getGp().getFoods().size(); i++) {
                Food food = gc.getGp().getFoods().get(i);
                g.drawImage(foodImage[food.getColor()], food.getX(), food.getY(), 10, 10, this);

            }
        }
        repaint();
    }

    public void updateTimerLabel() {
        timerLabel.setText("Time: " + gc.getGp().getRemainingTime() + "s");
        repaint();
    }

    public void showTimeUpDialog() {
        JOptionPane.showMessageDialog(this, "Time's up! Your score is "+gc.getGp().getScore(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showLoseDialog() {
        JOptionPane.showMessageDialog(this, "You lost! Your score is "+gc.getGp().getScore(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWinnerDialog() {
        int x=gc.getGp().checkSnakesBodysCollision()+gc.getGp().checkBody2Collision();
        JOptionPane.showMessageDialog(this, "Snake "+x+" won the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void closeCurrentGameWindow() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }



    public GameController getGc() {
        return gc;
    }


    public boolean isModePvsPView() {
        return modePvsP;
    }

    public void setModePvsPView(boolean modePvsP) {
        this.modePvsP = modePvsP;
    }
}
