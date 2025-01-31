package Net;

import model.Pair;
import model.Snake;
import model.SnakeBodyPart;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 * this class handle the game serverside, responsible for logic and update every client connected
 */
public class GameServer {
    private volatile Map<ClientHandler,Snake> players;
    private final List<Pair> foods;
    private final Server server;

    private final int borderX = 10000;
    public final int bordery = 10000;


    /**
     * GameServer constructor initialized player map and food list
     * @param server Server connected to the players
     * */
    public GameServer(Server server){
        players= new ConcurrentHashMap<>();
        foods=new ArrayList<>();
        this.server = server;
    }

    public Map<ClientHandler, Snake> getPlayers() {
        return players;
    }

    /**
     * addPlayer add a player to the players map and generate food around the snake
     * @param clientHandler ClientHandler connected to the client
     * @param snake Snake the snake of the player
     */
    public void addPlayer(ClientHandler clientHandler, Snake snake){
        addFood(snake.getBody().getFirst());
        players.put(clientHandler,snake);
    }

    /**
     * this method update positions of the player, check for collision and grow the snake at every tick (about 60 times a second)
     */
    public void update(){
        if(!players.isEmpty()){

            Iterator<Map.Entry<ClientHandler,Snake>> iterator =players.entrySet().iterator();
            Map.Entry<ClientHandler,Snake> entry=null;

            while (iterator.hasNext()){
                entry = iterator.next();

                if(!entry.getKey().isAlive()){
                    players.remove(entry.getKey(),entry.getValue());
                    continue;
                }

                if(entry.getKey().getNewPos()==null)
                    continue;

                Pair newPos =stringToPos(entry.getKey().getNewPos(),entry.getKey().getClientUserNamme());
                entry.getValue().move(newPos.getX(),newPos.getY());

                checkFoodCollision(entry.getValue());
                playerCollided(entry);
                borderCollision(entry);
            }
            sendPackeg();
        }
    }
    /**
     * addFood generate food around the snake head
     * @param snakeHead SnakeBodyPart of the head of the snake
     */
    public void addFood(SnakeBodyPart snakeHead){
        int x=snakeHead.getX();
        int y=snakeHead.getY();
        for (int i=0;i<100;i++){
            generateFood(x,y);
        }
    }

    /**
     * this method generate Food around the coordinate inserted as parameter
     * @param x int
     * @param y int
     */
    public void generateFood(int x, int y){
        Random random = new Random();
        int foodX=random.nextInt(1100)+x;
        int foodY=random.nextInt(700)+y;
        foods.add(new Pair(foodX,foodY));
    }

    /**
     * checkFoodCollision check for food collision, generate food and grow the snake
     * @param snake Snake
     */
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

    /**
     * this method kill a player, close the socket, and distribute the food generated by his death
     * @param player Map entry form the players map
     */
    public void die(Map.Entry<ClientHandler,Snake> player){
        player.getKey().write("SERVER: you died!");
        player.getKey().close();
        explode(player.getValue());
        players.remove(player.getKey(),player.getValue());
    }

    /**
     * generate food from snake death
     * @param snake snake object to retrieve snake body-part position
     */
    public void explode(Snake snake){
        Random random = new Random();
        for(SnakeBodyPart snakeBodyPart : snake.getBody()){
            foods.add(new Pair(snakeBodyPart.getX()+ random.nextInt(20),snakeBodyPart.getY()+random.nextInt(20)));
        }
    }

    /**
     * this method check the collision between a player snake and other players' snake, kill the player if a collision happened
     * @param playerEntry map entry form players map
     * @return true if a collision between player snake and another snake is detected
     */
    public boolean playerCollided(Map.Entry<ClientHandler,Snake> playerEntry){
        LinkedList<SnakeBodyPart> snakes = new LinkedList<>();
        Snake snake = playerEntry.getValue();

        for(Map.Entry<ClientHandler,Snake> entry : players.entrySet()){
            if(!entry.getKey().equals(playerEntry.getKey()))
                snakes.addAll(entry.getValue().getBody());
        }
        /*
        //self collision rule
        if(snake.selfCollision())
            die(playerEntry);
         */
        if(snake.collisionsWithBody(snakes)){
            die(playerEntry);
            return true;
        }
        return false;
    }

    /**
     * check whether the play has exceeded the border
     * @param playerEntry map entry form players map
     * @return true if the player exude the borders
     */
    public boolean borderCollision(Map.Entry<ClientHandler,Snake> playerEntry){
        SnakeBodyPart head = playerEntry.getValue().getBody().get(0);
        if (Math.abs(head.getX())>borderX||Math.abs(head.getY())>bordery){
            die(playerEntry);
            return true;
        }
        return false;
    }

    /**
     * this method send a package with all the info to render the view to every client connected
     */
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

    /**
     * return a pair of form the message received by the server
     * @param newPos String the entire message received
     * @param username the username of the client who sent the message
     */
    private Pair stringToPos(String newPos,String username) {
        String temp =Serialize.removeUsername(newPos,username);
        return Serialize.deserializePlayerPos(temp);
    }

    public Pair getBorders(){
        return  new Pair(borderX,bordery);
    }
}
