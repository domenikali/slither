package controller;

import Net.Client;
import Net.Serialize;
import model.Direction;
import model.GamePlane;

import view.GameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController extends MouseAdapter implements KeyListener{
    private GamePlane gp;
    private GameView gv;
    private boolean modePvsP;

    private Client client;

    public GameController(Boolean b, GameView gv, Client client){
        this.client=client;
        this.modePvsP=b;
        this.gv=gv;
        this.gp=new GamePlane(this);
    }
    public GamePlane getGp(){
        return this.gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        if(modePvsP) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (gp.getSnake().getDirection() != Direction.RIGHT) {
                        gp.getSnake().setDirection(Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (gp.getSnake().getDirection() != Direction.LEFT) {
                        gp.getSnake().setDirection(Direction.RIGHT);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (gp.getSnake().getDirection() != Direction.DOWN) {
                        gp.getSnake().setDirection(Direction.UP);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (gp.getSnake().getDirection() != Direction.UP) {
                        gp.getSnake().setDirection(Direction.DOWN);
                    }
                    break;
                case KeyEvent.VK_A:
                    if (gp.getSnake2().getDirection() != Direction.RIGHT) {
                        gp.getSnake2().setDirection(Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_W:
                    if (gp.getSnake2().getDirection() != Direction.DOWN) {
                        gp.getSnake2().setDirection(Direction.UP);
                    }
                    break;
                case KeyEvent.VK_S:
                    if (gp.getSnake2().getDirection() != Direction.UP) {
                        gp.getSnake2().setDirection(Direction.DOWN);
                    }
                    break;
                case KeyEvent.VK_D:
                    if (gp.getSnake2().getDirection() != Direction.LEFT) {
                        gp.getSnake2().setDirection(Direction.RIGHT);
                    }
                    break;
            }
        }
        if (KeyEvent.VK_B == e.getKeyCode()) {
            gp.resetGame();
            gv.closeCurrentGameWindow();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                gp.getSnake().setAccelerating(false);
                break;
        }
    }



    public void mouseMoved(MouseEvent e) {
        if (!modePvsP) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            //System.out.println(client.toString());
            if(client!=null){

                client.write(Serialize.serializePlayerPos(mouseX,mouseY));
            }else {
                gp.getSnake().setMouseX(mouseX);
                gp.getSnake().setMouseY(mouseY);
            }

        }
    }


    public Boolean getModePvsP(){
        return this.modePvsP;
    }

    public GameView getGv() {
        return gv;
    }
}
