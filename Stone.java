import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//renamed
public class Stone extends Obs
{
    boolean start = true;

    private void remove(){
        if(start){
            if(getOneIntersectingObject(AntHill.class) != null){
                getWorld().removeObject(this);
            }
            else if(getOneIntersectingObject(Food.class) != null){
                getWorld().removeObject(this);
            }
            start = false;
        }

        //getImage().setColor(Color.RED);
        //getImage().drawRect(0,0,getImage().getWidth() - 1, getImage().getHeight() - 1);
    }
    public void act() 
    {
        remove();
    }    
}
