import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SimpleTimer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SimpleTimer
{
    /**
     * Act - do whatever the SimpleTimer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    int steps = 0;
    
    public void calculate() 
    {
        steps++;// Add your action code here.
    }    
    
    public int getTime(){
        return steps;
    }
    
    public void update(){
        steps = 0;
    }
}
