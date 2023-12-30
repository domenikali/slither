package model;
import view.GameWindow;


import java.awt.*;
import java.util.LinkedList;

public class Snake {
    private LinkedList<SnakeBodyPart> body;
    private int position;
    private int startSize=5;
    private Direction direction;
    private boolean isAccelerating;
    private int speed=5;  // Nouvelle variable pour stocker la vitesse actuelle
    private   int initialSpeed = 5;
    private int mouseX, mouseY;

    public Snake(int position,Direction d){
        this.direction=d;
        this.position=position;
        this.mouseX=0;
        this.mouseY=0;
        this.isAccelerating=false;
        this.body=new LinkedList<SnakeBodyPart>();
        for(int i=startSize;i>0;i--){
            body.add(new SnakeBodyPart(i*position, Toolkit.getDefaultToolkit().getScreenSize().height/2));
        }
    }

    public void reset() {
        body.clear();
        for (int i = startSize; i > 0; i--) {
            body.add(new SnakeBodyPart(i * this.position, 10));
        }

        direction = Direction.RIGHT;
        isAccelerating = false;
        speed = initialSpeed;
        mouseX = 0;
        mouseY = 0;
    }

    public void move2(Direction direction){

        int s=speed-5;
        switch (direction) {
            case UP:
                for (int i = body.size() - 1; i > 0; i--) {
                    body.get(i).setX(body.get(i - 1).getX());
                    body.get(i).setY(body.get(i - 1).getY()-s);
                }
                body.get(0).setY(body.get(0).getY() - speed);
                break;
            case DOWN:
                for (int i = body.size() - 1; i > 0; i--) {
                    body.get(i).setX(body.get(i - 1).getX());
                    body.get(i).setY(body.get(i - 1).getY()+s);
                }
                body.get(0).setY(body.get(0).getY() + speed);
                break;
            case LEFT:
                for (int i = body.size() - 1; i > 0; i--) {
                    body.get(i).setX(body.get(i - 1).getX()-s);
                    body.get(i).setY(body.get(i - 1).getY());
                }
                body.get(0).setX(body.get(0).getX() - speed);
                break;
            case RIGHT:
                for (int i = body.size() - 1; i > 0; i--) {
                    body.get(i).setX(body.get(i - 1).getX()+s);
                    body.get(i).setY(body.get(i - 1).getY());
                }
                body.get(0).setX(body.get(0).getX() + speed);
                break;
        }

        // Augmentation linéaire de la vitesse lorsque l'accélération est activée
        if (isAccelerating && speed < 20) {
            speed += 1;
        } else if (!isAccelerating && speed > initialSpeed) {
            // Diminution linéaire de la vitesse lorsque l'accélération est désactivée
            speed -= 1;
        }
    }

    public void move(int mouseX, int mouseY) {

        for (int i = body.size() - 1; i > 0; i--) {
            body.get(i).setX(body.get(i - 1).getX());
            body.get(i).setY(body.get(i - 1).getY());
        }

        double angle = Math.atan2(mouseY -GameWindow.getWindowHeight()/2, mouseX - GameWindow.getWindowWidth()/2);

        int newX = (int) (body.get(0).getX() + speed * Math.cos(angle));
        int newY = (int) (body.get(0).getY() + speed * Math.sin(angle));

        body.get(0).setX(newX);
        body.get(0).setY(newY);

        // Augmentation linéaire de la vitesse lorsque l'accélération est activée
        if (isAccelerating && speed < 20) {
            speed += 1;
        } else if (!isAccelerating && speed > initialSpeed) {
            // Diminution linéaire de la vitesse lorsque l'accélération est désactivée
            speed -= 1;
        }
    }

    public void grow() {
        SnakeBodyPart bodyLast = body.getLast();
        SnakeBodyPart newBodyLast = new SnakeBodyPart(bodyLast.getX(), bodyLast.getY());
        body.addLast(newBodyLast);
    }

    public boolean collisionsWithFood(Pair food) {
        return body.getFirst().getX() < food.getX() + 10 &&
                body.getFirst().getX() + 10 > food.getX() &&
                body.getFirst().getY() < food.getY() + 10 &&
                body.getFirst().getY() + 10 > food.getY();
    }

    public int collisionsWithBody() {
        int x=0;
        for (int i = 1; i < body.size(); i++) {
            if (body.get(0).getX() == body.get(i).getX() && body.get(0).getY() == body.get(i).getY()) {
                x=i;
                break;
            }
        }
        return x;
    }


    public boolean isAccelerating() {
        return isAccelerating;
    }

    public void setAccelerating(boolean accelerating) {
        isAccelerating = accelerating;
    }

    public int getSpeed(){
        return this.speed;
    }

    public LinkedList<SnakeBodyPart> getBody(){
        return this.body;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


    public int getStartSize(){
        return this.startSize;
    }

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }
}
