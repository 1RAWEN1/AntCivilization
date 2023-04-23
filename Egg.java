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
    private int profession;
    
    private int x;
    private int y;

    private boolean royalEgg;
    public Egg(int prof, AntHill home, boolean royalEgg){
        this.royalEgg = royalEgg;
        setHomeHill(home);
        profession=prof;
        if(prof==2 || royalEgg){
            size=18;
            SPAWN_TIME*=2;
        }

        if(!royalEgg) {
            if(profession == 1) {
                setFood(3, 1);
            }
            else{
                setFood(6, 1);
            }
        }
        else{
            setFood(10, 1);
        }
        
        setHp(1);
        
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
        if(royalEgg){
            myImage.setColor(Color.GREEN);
        }
        else if(profession==2){
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
    
    private int SPAWN_TIME=1000;

    int changeXLoc;
    int changeYLoc;
    int changeXLoc1;
    int changeYLoc1;
    public void act() 
    {
        setRotation(0);
        if(t.getTime() <= SPAWN_TIME) {
            t.calculate();
        }
        updateImage();
        if(t.getTime()>SPAWN_TIME && !isTouching(UndergroundObs.class)){
            if(Greenfoot.getRandomNumber(100) < 10) 
            {
                if(!foodNotFully()){
                    if(!royalEgg) {
                        Ant ant = new Ant(getHomeHill(), profession);
                        getWorld().addObject(ant, getX(), getY());
                        getHomeHill().newAnt(ant);
                        getHomeHill().countAnts();
                    }
                    else{
                        if(Greenfoot.getRandomNumber(2) == 1) {
                            QueenAnt queen = new QueenAnt(getHomeHill());
                            queen.notQueen();
                            getWorld().addObject(queen, getX(), getY());

                            getHomeHill().newPrincess();
                        }
                        else{
                            Prince prince = new Prince(getHomeHill());
                            getWorld().addObject(prince, getX(), getY());

                            getHomeHill().newPrince();
                        }
                    }
                    getHomeHill().reduseEgg(getMAX_FOOD());
                }
                
                getWorld().removeObject(this);
            }
        }
        else if(isTouching(UndergroundObs.class)){
            x=getX();
            y=getY();
            changeXLoc = 0;
            changeYLoc = 0;
            for (UndergroundObs uo : getIntersectingObjects(UndergroundObs.class)) {
                changeXLoc1 = (uo.getImage().getWidth() / 2) + (getImage().getWidth() % 2) + (getImage().getWidth() / 2) - Math.abs(getX() - uo.getX());

                changeYLoc1 = (uo.getImage().getHeight() / 2) + (getImage().getHeight() % 2) + (getImage().getHeight() / 2) - Math.abs(getY() - uo.getY());

                if (changeXLoc1 <= changeYLoc1 && changeXLoc1 > 0) {
                    changeYLoc1 = 0;
                } else if (changeXLoc1 > changeYLoc1 && changeYLoc1 > 0) {
                    changeXLoc1 = 0;
                }

                changeXLoc = Math.abs(changeXLoc) > changeXLoc1 ? changeXLoc : changeXLoc1;
                changeYLoc = Math.abs(changeYLoc) > changeYLoc1 ? changeYLoc : changeYLoc1;

                if (uo.getX() > getX() && changeXLoc == changeXLoc1) {
                    changeXLoc = -changeXLoc1;
                }
                if (uo.getY() > getY() && changeYLoc == changeYLoc1) {
                    changeYLoc = -changeYLoc1;
                }
            }

            setLocation(x + changeXLoc, y + changeYLoc);
            /*for(int i=0;i<4;i++){
                setLocation(x+(int)Math.cos(Math.toRadians(i*90)),y+(int)Math.sin(Math.toRadians(i*90)));
                if(!isTouching(UndergroundObs.class)){
                    break;
                }
            }*/
            /*setLocation(getX()+1,getY());
            if(isTouching(Block.class)){
                setLocation(getX()-2,getY());
                if(isTouching(Block.class)){
                    setLocation(getX()+1, getY()+1);
                    if(isTouching(Block.class)){
                        setLocation(getX(), getY()-2);
                        if(isTouching(Block.class)){
                            getWorld().removeObject(this);
                        }
                    }
                }
            }*/
        }
        else{
            die1();
        }
        // Add your action code here.
    }    
}
