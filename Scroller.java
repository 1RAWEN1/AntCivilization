import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Scroller here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Scroller extends Actor
{
    double value;
    Color fillColor = Color.GREEN;
    public Scroller(int value1){
        value=(double) value1 / 100;
        updateImage();
    }
    public void updateImage(){
        GreenfootImage im = new GreenfootImage(200,6);
        im.setColor(Color.GRAY);
        im.fill();
        im.setColor(fillColor);
        im.fillRect(0,0,(int) (value * 200),5);
        setImage(im);
    }
    
    public void setValue(int val){
        value=(double) val / 100;
        if(value > 1){
            value = 1;
        }
        else if(value < 0){
            value = 0;
        }
    }
    public int getValue(){
        return (int) (100 * value);
    }
    
    public void setColor(Color fillColor){
        this.fillColor = fillColor;
    }
    
    boolean pushed;
    public void act() 
    {
        MouseInfo mi = Greenfoot.getMouseInfo();
        if(Greenfoot.mousePressed(this)){
            pushed = true;
        }
        if(Greenfoot.mouseClicked(null)){
            pushed = false;
        }
        if(pushed && mi != null){
            value=(double) ((mi.getX() - getX()) + getImage().getWidth() / 2) / getImage().getWidth();
            if(value > 1){
                value = 1;
            }
            else if(value < 0){
                value = 0;
            }
        }
        updateImage();
    }    
}
