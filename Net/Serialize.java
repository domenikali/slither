package Net;

import model.Pair;
import model.Snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        for(int i=0;i<player.getValue().getBody().size();i++){
            stringBuilder.append(player.getValue().getBody().get(i).toString());
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }
    public static List<Pair> deserializeSnake(String str){
        List<Pair> snakeCoordinate=new ArrayList<>();

        String []tokens = str.split(",");
        for(int i =1;i<tokens.length;i++){
            String []coordinates=tokens[i].replaceAll("[()]","").split(":");
            snakeCoordinate.add(new Pair(Integer.parseInt(coordinates[0]),Integer.parseInt(coordinates[1])));
        }
        System.out.println(tokens[0]);
        for(int i=0;i<snakeCoordinate.size();i++)
            System.out.print(snakeCoordinate.get(i).toString());
        return snakeCoordinate;
    }

}
