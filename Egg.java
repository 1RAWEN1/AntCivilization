import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Egg here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Egg extends Live
{
    /**
     * Act - do whatever the Egg wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private SimpleTimer t=new SimpleTimer();
    private int profession=0;
    private int food = 0;
    public Egg(int prof, AntHill home){
        setHomeHill(home);
        profession=prof;
        if(prof==2){
            size=18;
            SPAWN_TIME*=2;
        }
        
        setHp(1);
        
        setFood(3,1);
        
        updateImage();
    }
    
    public int getFood(){
        return food;
    }
    
    public void countFood(){
        food++;
    }
    private int size = 10;
    private void updateImage(){
        GreenfootImage myImage=new GreenfootImage((int)(((double)t.getTime()/SPAWN_TIME)*size)+1,(int)(((double)t.getTime()/SPAWN_TIME)*size)+1);
        if(profession==2){
            myImage.setColor(Color.BLUE);
        }
        else{
            myImage.setColor(Color.WHITE);
        }
        myImage.fillOval(0,0,myImage.getWidth()-1,myImage.getHeight()-1);
        myImage.setColor(Color.BLACK);
        myImage.drawOval(0,0,myImage.getWidth()-1,myImage.getHeight()-1);
        myImage.setTransparency(100);
        setImage(myImage);
    }
    
    private boolean start=true;
    
    private int SPAWN_TIME=500;
    public void act() 
    {
        t.calculate();
        updateImage();
        if(t.getTime()>SPAWN_TIME && !isTouching(Block.class)){
            if(Greenfoot.getRandomNumber(100) < 10) 
            {
                if(food==3){
                    Ant ant=new Ant(getHomeHill(), profession);
                    getWorld().addObject(ant, getX(), getY());
                    getHomeHill().newAnt(ant);
                    getHomeHill().countAnts();
                }
                
                getWorld().removeObject(this);
            }
        }
        else if(isTouching(Block.class)){
            getWorld().removeObject(this);
        }
        else{
            die1();
        }
        // Add your action code here.
    }    
}
