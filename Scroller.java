import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Scroller here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Scroller extends Actor
{
    /**
     * Act - do whatever the Scroller wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    double value;
    Color c=Color.GREEN;
    public Scroller(int value1){
        value=(double)value1/100;
        updateImage();
    }
    public void updateImage(){
        GreenfootImage im=new GreenfootImage(200,6);
        im.setColor(Color.GRAY);
        im.fill();
        im.setColor(c);
        im.fillRect(0,0,(int)(value*200),5);
        setImage(im);
    }
    
    public void setValue(int val){
        value=(double)val/100;
        if(value>1){
            value=1;
        }
        else if(value<0){
            value=0;
        }
    }
    public int getValue(){
        return (int)(100*value);
    }
    
    public void setColor(Color c1){
        c=c1;
    }
    
    boolean mpush;
    public void act() 
    {
        MouseInfo mi=Greenfoot.getMouseInfo();
        if(Greenfoot.mousePressed(this)){
            mpush=true;
        }
        if(Greenfoot.mouseClicked(null)){
            mpush=false;
        }
        if(mpush && mi!=null){
            value=(double)((mi.getX()-getX())+getImage().getWidth()/2)/getImage().getWidth();
            if(value>1){
                value=1;
            }
            else if(value<0){
                value=0;
            }
        }
        updateImage();
        // Add your action code here.
    }    
}
