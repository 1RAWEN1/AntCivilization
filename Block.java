import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Block here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Block extends UndergroundObs
{
    /**
     * Act - do whatever the Block wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private final int MAX_ENDURANCE=100;
    private int endurance=MAX_ENDURANCE;
    public Block(){
        getImage().setTransparency(100);
    }
    
    public void dig(){
        endurance--;
    }
    public void act() 
    {
        if(endurance<=0){
            getWorld().removeObject(this);
        }// Add your action code here.
    }    
}
