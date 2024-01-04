package view;

import Net.Client;
import model.Pair;
import Net.Serialize;
import controller.GameController;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServerGameView extends JPanel implements GameView {

    private final GameController gc;
    private final Image background;



    private final Image[] foodImage;
    private final Image snakeImage;

    private final Client client;

    public ServerGameView(Boolean b, Client client) {
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

            if(client.isClosed()){
                new ConnectionMenu().setVisible(true);
                closeCurrentGameWindow();

            }
            super.paintComponent(g);
            g.drawImage(background, 0, 0, 1100, 600, this);
            if(client.getMessageFromServer()!=null) {


                Map<String, List<Pair>> snakes = Serialize.deserializeSnakes(client.getSnakes());
                List<Pair> foods = Serialize.deserializeSnake(client.getFoods());
                int offsetX = 0;
                int offsetY = 0;
                offsetX = getWidth() / 2 - snakes.get(client.getUserName()).get(0).getX();
                offsetY = getHeight() / 2 - snakes.get(client.getUserName()).get(0).getY();
                for (Map.Entry<String, List<Pair>> entry : snakes.entrySet()) {
                    List<Pair> snakePos = entry.getValue();
                    g.drawString(entry.getKey(), snakePos.getFirst().getX() + offsetX - (entry.getKey().length() * 3), snakePos.getFirst().getY() + offsetY);

                    for (int i = 0; i < snakePos.size(); i++) {
                        int x = snakePos.get(i).getX() + offsetX;
                        int y = snakePos.get(i).getY() + offsetY;
                        g.drawImage(snakeImage, x, y, 15, 15, this);
                    }
                }

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Score: " + (snakes.get(client.getUserName()).size() - 5), 10, 20);

                for (int i = 0; i < foods.size(); i++) {
                    g.drawImage(foodImage[foods.get(i).getX() % 4], foods.get(i).getX() + offsetX, foods.get(i).getY() + offsetY, 10, 10, this);
                }
            }
        repaint();
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
}
