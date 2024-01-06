package model;

import view.GameWindow;

import java.io.FilterOutputStream;
import java.util.ArrayList;

public class AISnake extends Snake{
    private int lastSize;

    private Pair lookingTo;
    public AISnake(int position, Direction d,ArrayList<Food> foods) {

        super(position, d);
        lastSize =5;
        lookingTo=closestFood(foods);
    }

    public void moveAI(ArrayList<Food> foods){
        SnakeBodyPart head = getBody().get(0);
        if(lastSize!=getBody().size())
            lookingTo = closestFood(foods);

        for (int i = getBody().size() - 1; i > 0; i--) {
            getBody().get(i).setX(getBody().get(i - 1).getX());
            getBody().get(i).setY(getBody().get(i - 1).getY());
        }

        double angle = Math.atan2( lookingTo.getY()- head.getY(), lookingTo.getX()-head.getX() );

        int newX = (int) (getBody().get(0).getX() + 5 * Math.cos(angle));
        int newY = (int) (getBody().get(0).getY() + 5 * Math.sin(angle));

        getBody().get(0).setX(newX);
        getBody().get(0).setY(newY);
    }

    private Pair closestFood(ArrayList<Food> foods) {
        Food closestFood = foods.get(0);
        SnakeBodyPart head = getBody().get(0);
        int closestFoodDistance =distance(closestFood.getX(),head.getX(),closestFood.getY(), head.getY());

        for(Food food :foods){
            int foodDistance =distance(food.getX(),head.getX(),food.getY(), head.getY());
            if(closestFoodDistance>foodDistance){
                closestFood=food;
                closestFoodDistance = foodDistance;
            }
        }
        return new Pair(closestFood.getX(),closestFood.getY());
    }

    public Pair getLookingTo(){
        return lookingTo;
    }
}
