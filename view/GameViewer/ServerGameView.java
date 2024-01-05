package view.GameViewer;

import Net.Client;
import model.Pair;
import Net.Serialize;
import controller.GameController;
import view.MenuFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServerGameView extends GameView {

    private final GameController gc;
    private final Image background;

    private final Image[] foodImage;
    private final Image snakeImage;

    private final Client client;

    public ServerGameView( Client client) {
        this.client=client;
        this.gc=new GameController(client);
        background = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/background.PNG"))).getImage();
        snakeImage=new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/serpent.png"))).getImage();
        foodImage=new Image[4];
        foodImage[0] = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/food.png"))).getImage();
        foodImage[1] = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/food1.png"))).getImage();
        foodImage[2] = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/food2.png"))).getImage();
        foodImage[3] = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/ressources/food3.png"))).getImage();
    }

    public void paintComponent(Graphics g) {

            //disconnect if the client is closed
            if(client.isClosed()){
                JOptionPane.showMessageDialog(this,"You lost,Game over!");
                new MenuFrame().setVisible(true);
                closeCurrentGameWindow();
            }
            //super.paintComponent(g);
            g.drawImage(background, 0, 0, 1100, 600, this);

            if(client.getMessageFromServer()!=null) {
                /*Initialization of list and variable depending on message received form the server*/
                Map<String, List<Pair>> snakes = Serialize.deserializeSnakes(client.getSnakes());
                List<Pair> foods = Serialize.deserializeSnake(client.getFoods());
                int offsetX = 0;
                int offsetY = 0;

                //change the offset for every player to let them view they snake at the center of the screen
                offsetX = getWidth() / 2 - snakes.get(client.getUserName()).get(0).getX();
                offsetY = getHeight() / 2 - snakes.get(client.getUserName()).get(0).getY();

                //paint every snake
                for (Map.Entry<String, List<Pair>> entry : snakes.entrySet()) {
                    List<Pair> snakePos = entry.getValue();
                    g.drawString(entry.getKey(), snakePos.get(0).getX() + offsetX - (entry.getKey().length() * 3), snakePos.get(0).getY() + offsetY);

                    for (int i = 0; i < snakePos.size(); i++) {
                        int x = snakePos.get(i).getX() + offsetX;
                        int y = snakePos.get(i).getY() + offsetY;
                        g.drawImage(snakeImage, x, y, 15, 15, this);
                    }
                }

                //paint teh score in the right high corner
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Score: " + (snakes.get(client.getUserName()).size() - 5), 10, 20);

                //paint the food
                for (int i = 0; i < foods.size(); i++) {
                    //change color depending on x value, gives the impression of randomness in color
                    g.drawImage(foodImage[foods.get(i).getX() % 4], foods.get(i).getX() + offsetX, foods.get(i).getY() + offsetY, 10, 10, this);
                }
            }
        repaint();
    }
    public GameController getGc() {
        return gc;
    }

    @Override
    public void updateTimerLabel() {

    }

}
