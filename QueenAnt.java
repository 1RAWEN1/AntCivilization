import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class QueenAnt here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class QueenAnt extends Creature
{
    /**
     * Act - do whatever the QueenAnt wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private final int radius = 150;
    public QueenAnt(AntHill home){
        setRotation(180);
        setHomeHill(home);
        
        setHp(3);
        
        setFood(10);
    }
    public void act() 
    {
        spawnEgg();
        if(isUnderGround()){
            inHome();
        }
        
        eatFood();
        
        updateAnimation();
        
        die();// Add your action code here.
    }  
    
    private Egg newEgg;
    private void spawnEgg(){
        if(Greenfoot.getRandomNumber(100)<10 && newEgg==null && !getHomeHill().fully()){
            newEgg = new Egg(Greenfoot.getRandomNumber(2)+1, getHomeHill());
            getWorld().addObject(newEgg,getX()+(int)(Math.cos(Math.toRadians(getRotation()+180))*getImage().getWidth()/2)
            , getY()+(int)(Math.sin(Math.toRadians(getRotation()+180))*getImage().getWidth()/2));
        }
        if(newEgg!=null){
            if(intersects(newEgg) && newEgg.getFood()<3 && food>1){
                newEgg.countFood();
                food--;
            }
            if(newEgg.getWorld()==null){
                newEgg=null;
            }
        }
    }
    
    private Actor target;
    private void inHome(){
        target=newEgg;
        if(foodNotFully() && getObjectsInRange(radius,TakenFood.class).size()>0){
            target=getObjectsInRange(radius,TakenFood.class).get(0);
        }
            
        if(target!=null && target.getWorld()!=null){
            headTowards(target);
            walk();
        }
        
        eat1();
    }
    
    public void eat1(){
        TakenFood tf=(TakenFood)getOneIntersectingObject(TakenFood.class);
        if(tf!=null && foodNotFully()){
            tf.eat();
            eat();
        }
    }
    
    private int animation;
    
    private final int step=2;
    
    private int shot=1;
    
    private final int MAX_SHOT=3;
    private void updateAnimation(){
        if(moved()){
            animation++;
        }
        if(animation>=step){
            animation=0;
            shot++;
        }
        if(shot>MAX_SHOT){
            shot=1;
        }
        setImage("queen"+shot+".png");
        if(getHomeHill().getTeam()==1){
            getImage().setColor(Color.BLUE);
        }
        else if(getHomeHill().getTeam()==2){
            getImage().setColor(Color.CYAN);
        }
        else if(getHomeHill().getTeam()==3){
            getImage().setColor(Color.YELLOW);
        }
        else if(getHomeHill().getTeam()==4){
            getImage().setColor(Color.GREEN);
        }
        getImage().fillRect(3,8,1,2);
        
        if(isUnderGround()){
            getImage().setTransparency(100);
        }
    }
    
    private void die(){
        if(hp<=0 || food<=0){
            getHomeHill().queenDead();
            getWorld().removeObject(this);
        }
    }
}
