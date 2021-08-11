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
        
        die();// Add your action code here.
    }  
    
    private Egg newEgg;
    private void spawnEgg(){
        if(Greenfoot.getRandomNumber(100)<10 && newEgg==null && !getHomeHill().fully()){
            newEgg = new Egg(Greenfoot.getRandomNumber(2)+1, getHomeHill());
            getWorld().addObject(newEgg,getX()+(int)(Math.cos(Math.toRadians(getRotation()+180))*getImage().getWidth())
            , getY()+(int)(Math.sin(Math.toRadians(getRotation()+180))*getImage().getWidth()));
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
        if(foodNotFully()){
            for(int i=0;i<getObjectsInRange(radius,Warehouse.class).size();i++){
                if(getObjectsInRange(radius,Warehouse.class).get(i).notEmpty()){
                    target=getObjectsInRange(radius,Warehouse.class).get(i);
                }
            }
        }
            
        if(target!=null && target.getWorld()!=null){
            headTowards(target);
            walk();
        }
        
        eat1();
    }
    
    public void eat1(){
        Warehouse wh=(Warehouse)getOneIntersectingObject(Warehouse.class);
        if(wh!=null && wh.notEmpty() && foodNotFully()){
            wh.takeSome();
            eat();
        }
    }
    
    private void die(){
        if(hp<0 || food<0){
            getHomeHill().queenDead();
            getWorld().removeObject(this);
        }
    }
}
