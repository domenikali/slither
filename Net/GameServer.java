package Net;

import model.Pair;
import model.Snake;

import java.util.HashMap;
import java.util.Map;

public class GameServer {

    private Map<ClientHandler,Snake> players;

    private int newX;
    private int newY;

    private Server server;

    public GameServer(Server server){
        players= new HashMap<>();
        this.server = server;

    }

    public Map<ClientHandler, Snake> getPlayers() {
        return players;
    }
    public void addPlayer(ClientHandler clientHandler, Snake snake){
        players.put(clientHandler,snake);
    }
    public void update(){
        if(!players.isEmpty()){
            for(Map.Entry<ClientHandler,Snake> entry: players.entrySet()){
                if(entry.getKey().getNewPos()==null)
                    continue;
                stringToPos(entry.getKey().getNewPos(),entry.getKey().getClientUserNamme());
                entry.getValue().move(newX,newY);
                server.sendMessage(Serialize.serializePlayerSnake(entry));
            }
        }
    }

    private void stringToPos(String newPos,String username) {
        String temp =Serialize.removeUsername(newPos,username);
        Pair p =Serialize.deserializePlayerPos(temp);
        newX=p.getX();
        newY=p.getY();
    }


}
