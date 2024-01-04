package controller;

import Net.Client;
import Net.Serialize;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController extends MouseAdapter{
    private Client client;
    public GameController( Client client){
        this.client=client;
    }
    public void mouseMoved(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            //System.out.println(client.toString());
            if (client != null) {
                client.write(Serialize.serializePlayerPos(mouseX, mouseY));
            }
    }
}
