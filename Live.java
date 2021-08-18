import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Live here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Live extends Actor
{
    public int hp;
    private int MAX_HP;
    
    public int food;
    private int MAX_FOOD;
    
    private SimpleTimer foodTimer=new SimpleTimer();
    
    private AntHill home;
    
    private boolean underGround=true;

    public int impulseX = 0;
    public int impulseY = 0;

    public void setImpulse(int damage, int rotation){
        impulseX = (int) (damage * 2 * Math.cos(Math.toRadians(rotation)));
        impulseY = (int) (damage * 2 * Math.sin(Math.toRadians(rotation)));
    }

    public void setFood(int maxFood){
        MAX_FOOD=maxFood;
        food=MAX_FOOD;
    }
    public void setFood(int maxFood, int food){
        MAX_FOOD=maxFood;
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
    
    public void setHp(int maxHp){
        MAX_HP=maxHp;
        hp=MAX_HP;
    }
    public void setHp(int maxHp, int hp){
        MAX_HP=maxHp;
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
    
    public AntHill getHomeHill()
    {
        return home;
    }
    
    public boolean isUnderGround(){
        return underGround;
    }
    public void moveInHome1(){
        underGround=!underGround;
    }
    
    public void act() 
    {
        // Add your action code here.
    }    
}
