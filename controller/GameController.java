package controller;
import model.GamePlane;
import model.Direction;
import view.GameViewer.GameView;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
public class GameController extends AbstractGameController implements KeyListener {
    private GamePlane gp;
    private boolean modePvsP;
    private boolean AI;
    private GameView gameView;
    public GameController(Boolean b,GameView gameView,Boolean AI){
        this.gameView=gameView;
        this.modePvsP = b;
        this.AI=AI;
        this.gp=new GamePlane(this);
    }
    public GamePlane getGp(){
        return this.gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(modePvsP){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                    if (gp.getSnake().getDirection() != Direction.RIGHT) {
                        gp.getSnake().setDirection(Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_RIGHT,KeyEvent.VK_D:
                    if (gp.getSnake().getDirection() != Direction.LEFT) {
                        gp.getSnake().setDirection(Direction.RIGHT);
                    }
                    break;
                case KeyEvent.VK_UP,KeyEvent.VK_W:
                    if (gp.getSnake().getDirection() != Direction.DOWN) {
                        gp.getSnake().setDirection(Direction.UP);
                    }
                    break;
                case KeyEvent.VK_DOWN,KeyEvent.VK_S:
                    if (gp.getSnake().getDirection() != Direction.UP) {
                        gp.getSnake().setDirection(Direction.DOWN);
                    }
                    break;
            }
        }else {
            if(KeyEvent.VK_SPACE == e.getKeyCode()){
                gp.getSnake().setAccelerating(true);
            }
        }
        if (KeyEvent.VK_B == e.getKeyCode()) {
             gp.resetGame();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                gp.getSnake().setAccelerating(false);
                break;
        }
    }
    public void mouseMoved(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            gp.getSnake().setMouseX(mouseX);
            gp.getSnake().setMouseY(mouseY);
    }
    public Boolean getModePvsP(){
        return this.modePvsP;
    }
    public GameView getGv() {
        return this.gameView;
    }
    public boolean isAI() {
        return AI;
    }
}
