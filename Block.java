import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.ArrayList;
import java.util.List;

//renamed
public class Block extends UndergroundObs
{
    private final int MAX_ENDURANCE=50;
    private int endurance=MAX_ENDURANCE;

    private boolean isAtEdgeOfHome;

    private List<Integer> position = new ArrayList<>();

    public Block(){
        getImage().setTransparency(100);
    }

    private int x;
    private int y;

    public Block(int x, int y){
        this.x = x;
        this.y = y;
        getImage().setTransparency(100);
    }
    
    public void dig(Actor digger){
        if(canDig(digger)){
            endurance--;
            updateImage();
            if(endurance<=0){
                getWorld().removeObject(this);
            }
        }
    }
    private int rot;
    private Actor backObs;
    private Actor leftObs;
    private Actor rightObs;
    public boolean canDig(Actor digger){
        backObs = null;
        leftObs = null;
        rightObs = null;
        turnTowards(digger.getX(), digger.getY());
        rot=getRotation()+180;
        setRotation(0);
        rot=((rot+45)/90)*90;
        if(getWorld()!=null){
            //backObs =getOneObjectAtOffset((int)(Math.cos(Math.toRadians(rot))*20),(int)(Math.sin(Math.toRadians(rot))*20),Block.class);
            rot-=90;
            leftObs=getOneObjectAtOffset((int)(Math.cos(Math.toRadians(rot))*20),(int)(Math.sin(Math.toRadians(rot))*20),Block.class);
            rot+=180;
            rightObs=getOneObjectAtOffset((int)(Math.cos(Math.toRadians(rot))*20),(int)(Math.sin(Math.toRadians(rot))*20),Block.class);
        }
        
        return leftObs!=null && rightObs!=null && !isAtEdgeOfHome;
    }
    
    public void updateImage(){
        GreenfootImage block=new GreenfootImage(getImage());
        //block.scale((int)(block.getWidth()*((double)endurance/MAX_ENDURANCE))+1,(int)(block.getHeight()*((double)endurance/MAX_ENDURANCE))+1);
        int x = Greenfoot.getRandomNumber(20);
        int y = Greenfoot.getRandomNumber(20);
        block.setColorAt(x, y, Color.BLACK);
        position.add(x);
        position.add(y);
        block.setTransparency(100);
        setImage(block);
    }

    private int timer = 0;
    private int step = 200;

    private boolean start = true;
    private int step1 = 0;
    private int blocks;
    public void act() 
    {
        if(start){
            if(step1 == 1) {
                for (int i = 0; i < 4; i++) {
                    rot = i * 90;
                    if (getOneObjectAtOffset((int) (Math.cos(Math.toRadians(rot)) * 20), (int) (Math.sin(Math.toRadians(rot)) * 20), Block.class) != null) {
                        blocks++;
                    }
                }
                if (x == 3 && y == 4 && blocks == 3
                        || x == 5 && y == 4 && blocks == 3
                        || x == 4 && y == 3 && blocks == 3
                        || x == 4 && y == 5 && blocks == 3) {

                } else if (blocks < 4) {
                    isAtEdgeOfHome = true;
                }
                start = false;
            }
            step1++;
        }
        timer++;
        if(timer >= step){
            timer = 0;
            recovery();
        }
        if(isAtEdge()){
            getWorld().removeObject(this);
        }
        // Add your action code here.
    }

    public void recovery(){
        if(endurance < MAX_ENDURANCE){
            endurance++;

            GreenfootImage image = new GreenfootImage("sandBlock.jpg");

            int num = Greenfoot.getRandomNumber(position.size() / 2) * 2;

            int x = position.get(num);
            int y = position.get(num + 1);
            getImage().setColorAt(x, y,
                    new Color(image.getColorAt(x, y).getRed(), image.getColorAt(x, y).getGreen(), image.getColorAt(x, y).getBlue()));
        }
    }
}
