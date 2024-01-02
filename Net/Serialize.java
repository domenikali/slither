package Net;

import model.Pair;
import model.Snake;

import java.util.*;

public class Serialize {


    public static String serializePlayerPos(int mousx,int mousy){
        return "x:"+mousx+",y:"+mousy;
    }

    public static Pair deserializePlayerPos(String str){
        int x;
        int y;
        String[] tokens =str.split(",");
        String[] t = tokens[0].split(":");
        x=Integer.parseInt(t[1]);
        t=tokens[1].split(":");
        y=Integer.parseInt(t[1]);
        return new Pair(x,y);
    }

    public static String  removeUsername(String str,String username){
        return str.replace(username+"-","");
    }

    public static String serializePlayerSnake(Map.Entry<ClientHandler, Snake> player){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(player.getKey().getClientUserNamme());
        stringBuilder.append(",");
        for(int i=0;i<player.getValue().getBody().size()-1;i++){
            stringBuilder.append(player.getValue().getBody().get(i).toString());
            stringBuilder.append(",");
        }
        stringBuilder.append(player.getValue().getBody().get(player.getValue().getBody().size()-1));
        return stringBuilder.toString();
    }

    public static  Map<String,List<Pair>> deserializeSnakes(String snakes){
        String [] tokens=snakes.split(";");
        Map<String,List<Pair>> snakeMap = new HashMap<>();
        for(int i=0;i<tokens.length;i++){
            snakeMap.put(tokens[i].split(",")[0],deserializeSnake(tokens[i]));

        }
        return snakeMap;
    }

    public static List<Pair> deserializeSnake(String str){
        List<Pair> snakeCoordinate=new ArrayList<>();

        String []tokens = str.split(",");
        for(int i =1;i<tokens.length;i++){
            String []coordinates=tokens[i].split(":");
            snakeCoordinate.add(new Pair(Integer.parseInt(coordinates[0]),Integer.parseInt(coordinates[1])));
        }
        return snakeCoordinate;
    }

}
