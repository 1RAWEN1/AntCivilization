import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class takenFood here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TakenFood extends Actor
{
    boolean taken = true;
    AntHill ah;
    public TakenFood(AntHill ah){
        ah.countFood();
        this.ah=ah;
    }
    public void act() 
    {
        // Add your action code here.
    }
    
    public void drop(){
        taken=false;
    }
    
    public void eat(){
        ah.eatFood();
        getWorld().removeObject(this);
    }
}
