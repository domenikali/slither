package Net;

import model.Pair;
import model.Snake;
import model.SnakeBodyPart;
import java.util.*;


public class GameServer {
    private volatile Map<ClientHandler,Snake> players;
    private final List<Pair> foods;
    private final Server server;



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

                if(!entry.getKey().isAlive())
                    players.remove(entry.getKey(),entry.getValue());
                if(entry.getKey().getNewPos()==null)
                    continue;
                Pair newPos =stringToPos(entry.getKey().getNewPos(),entry.getKey().getClientUserNamme());
                entry.getValue().move(newPos.getX(),newPos.getY());
                checkFoodCollision(entry.getValue());
                checkPlayerCollision(entry);
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
                foods.remove(i--);
                for(int j=0;j<3;j++){
                    generateFood(snake.getBody().getFirst().getX(),snake.getBody().getFirst().getY());
                }
            }
        }
    }

    public void die(Map.Entry<ClientHandler,Snake> player){
        player.getKey().write("SERVER: you died!");
        player.getKey().close();
        explode(player.getValue());
        players.remove(player.getKey(),player.getValue());

    }

    public void explode(Snake snake){
        Random random = new Random();
        for(SnakeBodyPart snakeBodyPart : snake.getBody()){
            foods.add(new Pair(snakeBodyPart.getX()+ random.nextInt(20),snakeBodyPart.getY()+random.nextInt(20)));
        }
    }

    public void checkPlayerCollision(Map.Entry<ClientHandler,Snake> playerEntry){
        List<SnakeBodyPart> heads = new ArrayList<>();
        Snake snake = playerEntry.getValue();

        for(Map.Entry<ClientHandler,Snake> entry : players.entrySet()){
            if(!entry.getKey().equals(playerEntry.getKey()))
                heads.add(entry.getValue().getBody().get(0));
        }
        if(snake.selfCollision(snake.getBody().get(0)))
            die(playerEntry);

        for(SnakeBodyPart head : heads){
            if(snake.collisionsWithBody(head))
                die(playerEntry);
        }
    }

    public void sendPackeg(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<ClientHandler,Snake> entry: players.entrySet()){
            stringBuilder.append(Serialize.serializePlayerSnake(entry));
            stringBuilder.append(";");
        }
        stringBuilder.append("&");
        for(Pair pair : foods){
            stringBuilder.append(pair.toString());
            stringBuilder.append(",");
        }

        server.sendMessage(stringBuilder.toString());
    }

    private Pair stringToPos(String newPos,String username) {
        String temp =Serialize.removeUsername(newPos,username);
        return Serialize.deserializePlayerPos(temp);
    }
}
