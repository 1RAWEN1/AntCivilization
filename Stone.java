import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Stone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stone extends Actor
{
    /**
     * Act - do whatever the Stone wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    boolean start=true;
    public void addedToWorld(){
        if(start){
            if(getOneIntersectingObject(AntHill.class)!=null){
                getWorld().removeObject(this);
            }
            else if(getOneIntersectingObject(Food.class)!=null){
                getWorld().removeObject(this);
            }
            start=false;
        }
    }
    public void act() 
    {
        addedToWorld();// Add your action code here.
    }    
}
