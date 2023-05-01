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

    private boolean fly = false;
    private int height;

    public void fly(){
        if(height < 100){
            height ++;
        }
        if(height > 0){
            fly = true;
        }
    }

    public void stopFly(){
        if(height > 0){
            height --;
        }
        if(height == 0){
            fly = false;
        }
    }

    public void flyAnimation(){
        getImage().scale((int)(getImage().getWidth() * (1 + ((double)height / 100))), (int)(getImage().getHeight() * (1 + ((double)height / 100))));
        getImage().setTransparency(255 - height);
    }

    public boolean isFly(){
        return fly;
    }

    public int impulseX = 0;
    public int impulseY = 0;

    public boolean canInteract(Live l){
        return l.underGround == underGround && !l.fly && !fly || fly && l.fly && height == l.height;
    }

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

    public void eatFood(int food){
        this.food -= food;
    }
    public int getFood(){
        return food;
    }
    public double getFoodPersent(){
        return (double)food / MAX_FOOD;
    }

    public int needFood(){
        return MAX_FOOD - food;
    }

    public int getMAX_FOOD(){
        return MAX_FOOD;
    }
    
    public boolean foodNotFully(){
        return food<MAX_FOOD;
    }

    public void eat(){
        if(food<MAX_FOOD){
            food++;
        }
    }

    public void eat(int energy){
        food += energy;
    }

    public int calculateDistToHome(){
        return (int)Math.sqrt(Math.pow(getX() - home.getX(), 2) + Math.pow(getY() - home.getY(), 2));
    }

    public void setHp(int maxHp){
        MAX_HP=maxHp;
        hp=MAX_HP;
    }
    public void setHp1(int maxHp){
        MAX_HP=maxHp;
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

    private int MAX_AGE = 10000;
    private SimpleTimer ageTimer = new SimpleTimer();

    public int getMAX_AGE() {
        return MAX_AGE;
    }

    public void setMAX_AGE(int MAX_AGE) {
        this.MAX_AGE = MAX_AGE;
    }

    public boolean grow(){
        ageTimer.calculate();
        return ageTimer.getTime() > MAX_AGE;
    }

    private int foodStep=1000;
    public void eatFood(){
        foodTimer.calculate();
        if(foodTimer.getTime()>foodStep){
            food--;
            
            foodTimer.update();
        }
    }

    public void setFoodStep(int i){
        foodStep = i;
    }

    SimpleTimer hpTimer = new SimpleTimer();
    private int hpStep = 500;
    public void regenerateHp(){
        if(hp < MAX_HP){
            hpTimer.calculate();
            if(hpTimer.getTime() > hpStep){
                hp++;
                hpTimer.update();
            }
        }
    }

    public void setHpStep(int hpStep) {
        this.hpStep = hpStep;
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
