package view.GameViewer;

import controller.GameController;
import view.MenuFrame;

import javax.swing.*;
import java.awt.*;

/**abstract class to diversify every gameView depending on the game mod*/
public abstract class GameView extends JPanel{

    /**
     * main method to render images on the game window
     */
    @Override
    public void paintComponent(Graphics g) {
    }

    public abstract GameController getGc();

    /**update the timer liable*/
    public abstract void updateTimerLabel();
    public void showTimeUpDialog() {
        JOptionPane.showMessageDialog(this, "Time's up! Your score is "+getGc().getGp().getScore(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showLoseDialog() {
        JOptionPane.showMessageDialog(this, "You lost! Your score is "+getGc().getGp().getScore(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * show the main menu closing the gameWindow
     */
    public void showMenu() {
        SwingUtilities.invokeLater(() -> {
            new MenuFrame();
            closeCurrentGameWindow();
        });
    }

    /**
     * close the current game window*/
    public void closeCurrentGameWindow() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
    public void showWinnerDialog(int s) {
        JOptionPane.showMessageDialog(this, "Snake "+s+ " won the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWinnerDialogVSAI(int s) {
        if(s==1){
            JOptionPane.showMessageDialog(this, "You won the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this, "The AI won the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

        }
    }


}
