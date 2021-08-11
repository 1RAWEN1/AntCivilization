import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Live here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Live extends Actor
{
    /**
     * Act - do whatever the Live wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public int hp;
    private int MAX_HP;
    
    public int food;
    private int MAX_FOOD;
    
    private SimpleTimer foodTimer=new SimpleTimer();
    
    private AntHill home;
    
    private boolean underGround=true;
    
    public void setFood(int maxfood){
        MAX_FOOD=maxfood;
        food=MAX_FOOD;
    }
    public void setFood(int maxfood, int food){
        MAX_FOOD=maxfood;
        this.food=food;
    }
    
    public boolean foodNotFully(){
        return food<MAX_FOOD;
    }
    public void eat(){
        if(food<MAX_FOOD){
            food++;
        }
    }
    
    public void setHp(int maxhp){
        MAX_HP=maxhp;
        hp=MAX_HP;
    }
    public void setHp(int maxhp, int hp){
        MAX_HP=maxhp;
        this.hp=hp;
    }
    
    public void damage(int damage){
        hp-=damage;
    }
    
    public void die1(){
        if(food<=0 || hp<=0){
            getWorld().removeObject(this);
        }
    }
    
    private final int foodStep=1000;
    public void eatFood(){
        foodTimer.calculate();
        if(foodTimer.getTime()>foodStep){
            food--;
            
            foodTimer.update();
        }
    }
    
    public void setHomeHill(AntHill homeHill)
    {
        home = homeHill;
    }
    
    /**
     * Get the home hill of this creature.
     */
    public AntHill getHomeHill()
    {
        return home;
    }
    
    public boolean isUnderGround(){
        return underGround;
    }
    
    public void moveInHome(){
        underGround=!underGround;
    }

    public void act() 
    {
        // Add your action code here.
    }    
}
