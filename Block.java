import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//renamed
public class Block extends UndergroundObs
{
    private final int MAX_ENDURANCE=100;
    private int endurance=MAX_ENDURANCE;

    public Block(){
        getImage().setTransparency(100);
    }
    
    public void dig(Actor digger){
        if(canDig(digger)){
            endurance--;
            updateImage();
            if(endurance<=0){
                getWorld().removeObject(this);
            }
        }
    }
    private int rot;
    private Actor backObs;
    private Actor leftObs;
    private Actor rightObs;
    public boolean canDig(Actor digger){
        backObs =null;
        turnTowards(digger.getX(), digger.getY());
        rot=getRotation()+180;
        setRotation(0);
        rot=((rot+45)/90)*90;
        if(getWorld()!=null){
            backObs =getOneObjectAtOffset((int)(Math.cos(Math.toRadians(rot))*20),(int)(Math.sin(Math.toRadians(rot))*20),Block.class);
            rot-=90;
            leftObs=getOneObjectAtOffset((int)(Math.cos(Math.toRadians(rot))*20),(int)(Math.sin(Math.toRadians(rot))*20),Block.class);
            rot+=180;
            rightObs=getOneObjectAtOffset((int)(Math.cos(Math.toRadians(rot))*20),(int)(Math.sin(Math.toRadians(rot))*20),Block.class);
        }
        
        return backObs !=null && leftObs!=null && rightObs!=null;
    }
    
    public void updateImage(){
        GreenfootImage block=new GreenfootImage(getImage());
        //block.scale((int)(block.getWidth()*((double)endurance/MAX_ENDURANCE))+1,(int)(block.getHeight()*((double)endurance/MAX_ENDURANCE))+1);
        block.setColorAt(Greenfoot.getRandomNumber(20), Greenfoot.getRandomNumber(20), Color.BLACK);
        block.setTransparency(100);
        setImage(block);
    }
    public void act() 
    {
        // Add your action code here.
    }    
}
