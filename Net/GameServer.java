package Net;

import model.Snake;

import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer {

    private Map<ClientHandler,Snake> players;

    private int newX;
    private int newY;

    public GameServer(){
        players= new HashMap<>();

    }

    public Map<ClientHandler, Snake> getPlayers() {
        return players;
    }
    public void addPlayer(ClientHandler clientHandler, Snake snake){
        players.put(clientHandler,snake);
    }
    public void update(){
        for(Map.Entry<ClientHandler,Snake> entry: players.entrySet()){
            stringToPos(entry.getKey().getNewPos());
            entry.getValue().move(newX,newY);
        }
    }

    private void stringToPos(String newPos) {

    }
}
