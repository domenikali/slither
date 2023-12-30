package Net;

import model.Food;
import model.Pair;
import model.Snake;
import model.SnakeBodyPart;

import java.lang.reflect.Parameter;
import java.util.*;

public class GameServer {
    private Map<ClientHandler,Snake> players;

    private List<Pair> foods;
    private int newX;
    private int newY;
    private Server server;



    public GameServer(Server server){

        players= new HashMap<>();
        foods=new ArrayList<>();
        this.server = server;
    }

    public Map<ClientHandler, Snake> getPlayers() {
        return players;
    }
    public void addPlayer(ClientHandler clientHandler, Snake snake){
        addFood(snake.getBody().getFirst());
        players.put(clientHandler,snake);
    }
    public void update(){
        if(!players.isEmpty()){
            for(Map.Entry<ClientHandler,Snake> entry: players.entrySet()){
                if(entry.getKey().getNewPos()==null)
                    continue;
                stringToPos(entry.getKey().getNewPos(),entry.getKey().getClientUserNamme());
                entry.getValue().move(newX,newY);
                checkFoodCollision(entry.getValue());

                //server.sendMessage(Serialize.serializePlayerSnake(entry));
            }
            sendPackeg();
        }
    }

    public void addFood(SnakeBodyPart snakeHead){
        int x=snakeHead.getX();
        int y=snakeHead.getY();


        for (int i=0;i<100;i++){
            generateFood(x,y);
        }
    }

    public void generateFood(int x, int y){
        Random random = new Random();
        int foodX=random.nextInt(1100)+x;
        int foodY=random.nextInt(700)+y;
        foods.add(new Pair(foodX,foodY));
    }

    public void checkFoodCollision(Snake snake){
        for(int i=0;i<foods.size();i++){
            if(snake.collisionsWithFood(foods.get(i))){
                snake.grow();
                foods.remove(i);
                for(int j=0;j<3;j++){
                    generateFood(snake.getBody().getFirst().getX(),snake.getBody().getFirst().getY());
                }
            }
        }
    }

    public void sendPackeg(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<ClientHandler,Snake> entry: players.entrySet()){
            stringBuilder.append(Serialize.serializePlayerSnake(entry));
            //stringBuilder.append(";");
        }
        stringBuilder.append("&");
        for(Pair pair : foods){
            stringBuilder.append(pair.toString());
            stringBuilder.append(",");
        }

        server.sendMessage(stringBuilder.toString());
    }

    private void stringToPos(String newPos,String username) {
        String temp =Serialize.removeUsername(newPos,username);
        Pair p =Serialize.deserializePlayerPos(temp);
        newX=p.getX();
        newY=p.getY();
    }
}
