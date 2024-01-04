package view;

import Net.Client;
import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public interface GameView {


    void paintComponent(Graphics g);

    public GameController getGc();

    public void closeCurrentGameWindow();
}
