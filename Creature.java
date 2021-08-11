import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Creature  extends Live
{
    /** The maximum movement speed of the ant. */
    public final int SPEED = 3;

    /** Current movement. Defined as the offset in x and y direction moved in each step. */
    private int deltaX;
    private int deltaY;

    private Actor takenObject;
    
    private Actor target;
    /** The home ant hill. */

    /**
     * Crtae a new creature with neutral movement (movement speed is zero).
     */
    public Creature()
    {
        getImage().setTransparency(100);
        deltaX = 0;
        deltaY = 0;
    }
    
    public void take(Actor obj){
        takenObject=obj;
    }
    
    public void carry(){
        if(takenObject!=null){
            takenObject.setRotation(getRotation());
            takenObject.setLocation(getX()+(int)((getImage().getWidth()/2)*Math.cos(Math.toRadians(getRotation())))
            , getY()+(int)((getImage().getWidth()/2)*Math.sin(Math.toRadians(getRotation()))));
        }
    }
    
    public void put(){
        takenObject=null;
    }
    
    public void remove(){
        getWorld().removeObject(takenObject);
        takenObject=null;
    }
    /**
     * Set the home hill of this creature.
     */
    /**
     * Walk around randomly (random direction and speed).
     */
    public void randomWalk()
    {
        if (randomChance(50)) {
            deltaX = adjustSpeed(deltaX);
            deltaY = adjustSpeed(deltaY);
        }
        walk();
    }

    /**
     * Try to walk home. Sometimes creatures get distracted or encounter small obstacles, so
     * they occasionally head in a different direction for a moment.
     */
    public void walkTowardsHome()
    {
        if(getHomeHill() == null) {
            //if we do not have a home, we can not go there.
            return;
        }
        if (randomChance(30) && !intersects(getHomeHill())) {
            randomWalk();  // cannot always walk straight. 30% chance to turn off course.
        }
        else {
            headRoughlyTowards(getHomeHill());
            walk();
        }
    }
    
    /**
     * Try to walk away from home. (Goes occasionally off course a little.)
     */
    public void walkAwayFromHome()
    {
        if(getHomeHill() == null) {
            //if we do not have a home, we can not head away from it.
            return;
        }
        if (randomChance(30)) {
            randomWalk();  // cannot always walk straight. 30% chance to turn off course.
        }
        else {
            headRoughlyTowards(getHomeHill());   // first head towards home...
            deltaX = -deltaX;           // ...then turn 180 degrees
            deltaY = -deltaY;
            walk();
        }
    }

    /**
     * Adjust the walking direction to head towards the given co-ordinates.
     */
    public void headTowards(int x, int y){
        deltaX = capSpeed(x - getX());
        deltaY = capSpeed(y - getY());
    }
    public void headTowards(Actor target)
    {
        if(target!=null && target.getWorld()!=null){
            this.target=target;
            deltaX = capSpeed(target.getX() - getX());
            deltaY = capSpeed(target.getY() - getY());
        }
    }
    
    public void setCof(int cof){
        this.cof=cof;
    }
    public void turnTowards(Actor target){
        deltaX = capSpeed(target.getX() - getX());
        deltaY = capSpeed(target.getY() - getY());
        
        setRotation((int) (180 * Math.atan2(deltaY, deltaX) / Math.PI));
    }
    
    private int cof=1;
    public void updateCof(){
        if(Greenfoot.getRandomNumber(2)==0){
            cof=-1;
        }
    }
    
    private int needSpeed(int speed){
        cof=1;
        if(speed<0){
            cof=-1;
        }
        return cof*SPEED;
    }
    
    private int randomSpeed(){
        return cof*SPEED;
    }
    
    /**
     * Walk forward in the current direction with the current speed. 
     * (Does not change direction or speed.)
     */
    private boolean touchObs;
    private int startX;
    private int startY;
    
    private int moverot = 270;
    private boolean leftHand;
    private int summ;
    
    private int rot;
    
    private int runPurposefully = 0;
    private void turnRandom(){
        if(summ==0){
            moverot=((rot+45)/90)*90;
            leftHand=rot<moverot;
            
            setLocation(startX+(int)(Math.cos(Math.toRadians(moverot))*SPEED), startY+(int)(Math.sin(Math.toRadians(moverot))*SPEED));
            
            if(leftHand){
                cof=-1;
            }
            else{
                cof=1;
            }
            if(isTouching(UndergroundObs.class)){
                if(leftHand){
                    moverot-=90;
                    summ--;
                }
                else{
                    moverot+=90;
                    summ++;
                }
            }
            else{
                runPurposefully=5;
            }
        }
        else{
            //setLocation(startX-deltaX, startY-deltaY);
            if(leftHand){
                moverot-=90;
                summ--;
            }
            else{
                moverot+=90;
                summ++;
            }
        }
    }
    
    public void walk(){
        if(summ==0 && runPurposefully==0 || target!=null && intersects(target)){
            if(target!=null && intersects(target)){
                atTarget();
            }
            walk1();
            if(target!=null && target.getWorld()==null){
                target=null;
            }
        }
        else if(summ!=0 || runPurposefully>0){
            if(target!=null && target.getWorld()==null){
                target=null;
            }
            
            if(target!=null && !intersects(target)){
                turnInHome();
            }
        }
    }
    
    public void walk1()
    {
        startX=getX();
        startY=getY();
        rot=getRotation();
        setRotation(0);
        
        setLocation(startX + deltaX, startY);
        if(!isUnderGround()){
            if(getOneIntersectingObject(Obs.class)!=null){
                deltaX=0;
                deltaY=randomSpeed();
                if(touchObs=false){
                    updateCof();
                    touchObs=true;
                }
            }
            else{
                setLocation(startX,startY + deltaY);
                if(getOneIntersectingObject(Obs.class)!=null){
                    deltaY=0;
                    deltaX=randomSpeed();
                    if(touchObs=false){
                        updateCof();
                        touchObs=true;
                    }
                }
                else{
                    touchObs=false;
                }
            }
        }
        else{
            touchObs=false;
            
            if(getOneIntersectingObject(UndergroundObs.class)!=null){
                deltaX=0;
                deltaY=needSpeed(deltaY);
                touchObs=true;
            }
            
            setLocation(startX+deltaX,startY + deltaY);
            if(getOneIntersectingObject(UndergroundObs.class)!=null){
                deltaY=0;
                if(!touchObs){
                    deltaX=needSpeed(deltaX);
                }
                touchObs=true;
            }
            
            if(touchObs){
                turnRandom();
            }
        }
        
        setLocation(startX + deltaX,startY + deltaY);
        
        /*if(isUnderGround() && isTouching(UndergroundObs.class)){
            deltaX=0;
            deltaY=0;
            setLocation(startX,startY);
            //turnRandom();
        }
        else if(!isUnderGround() && isTouching(Obs.class)){
            deltaX=0;
            deltaY=0;
            setLocation(startX,startY);
        }*/
        if(deltaX==0 && deltaY==0){
            setRotation(rot);
        }
        else{
            setRotation((int) (180 * Math.atan2(deltaY, deltaX) / Math.PI));
        }
        
        if(isAtEdge()){
            setCof(cof * -1);
        }
    }
    
    public void atTarget(){
        summ=0;
        runPurposefully=0;
    }
    
    private final int dist=3;
    
    private int startx;
    private int starty;
    private int startRot;
    private void turnInHome(){
        headTowards(getX()+(int)(Math.cos(Math.toRadians(moverot))*SPEED), getY()+(int)(Math.sin(Math.toRadians(moverot))*SPEED));
        walk1();
        if(runPurposefully>0){
            runPurposefully--;
        }
        
        if(summ!=0){
            startx=getX();
            starty=getY();
            
            startRot=getRotation();
            
            moverot%=360;
            if(leftHand){
                turn(90);
                move(dist);
                boolean tObs1=getOneIntersectingObject(Block.class)!=null;
                
                setLocation(startx,starty);
                setRotation(startRot);
                move(-getImage().getWidth());
                boolean tObs2=getOneIntersectingObject(Block.class)!=null;
                turn(90);
                move(dist);
                boolean tObs3=getOneIntersectingObject(Block.class)!=null;
                setLocation(startx,starty);
                setRotation(startRot);
                
                if(tObs1==false && tObs2==false && tObs3==true){
                    moverot+=90;
                    summ++;
                }
            }
            else{
                turn(-90);
                move(dist);
                boolean tObs1=getOneIntersectingObject(Block.class)!=null;
                
                setLocation(startx,starty);
                setRotation(startRot);
                move(-getImage().getWidth());
                boolean tObs2=getOneIntersectingObject(Block.class)!=null;
                turn(-90);
                move(dist);
                //setLocation(getX()+(int)(Math.cos(Math.toRadians(moverot))*getImage().getHeight())
                //, getY()+(int)(Math.sin(Math.toRadians(moverot))*getImage().getHeight()));
                boolean tObs3=getOneIntersectingObject(Block.class)!=null;
                setLocation(startx,starty);
                setRotation(startRot);
                
                if(tObs1==false && tObs2==false && tObs3==true){
                    moverot-=90;
                    summ--;
                }
            }
        }
    }
    /**
     * Adjust the walking direction to head somewhat towards the given co-ordinates. This does not 
     * always head in the same direction. The heading is slightly random (but likely to be somewhat
     * towards the target) to make it look more natural.
     */
    private void headRoughlyTowards(Actor target)
    {
        if(target!=null && target.getWorld()!=null){
            int distanceX = Math.abs(getX() - target.getX());
            int distanceY = Math.abs(getY() - target.getY());
            boolean moveX = (distanceX > 0) && (Greenfoot.getRandomNumber(distanceX + distanceY) < distanceX);
            boolean moveY = (distanceY > 0) && (Greenfoot.getRandomNumber(distanceX + distanceY) < distanceY);
    
            deltaX = computeHomeDelta(moveX, getX(), target.getX());
            deltaY = computeHomeDelta(moveY, getY(), target.getY());
            
            this.target=target;
        }
    }
    
    /**
     * Compute and return the direction (delta) that we should steer in when
     * we're on our way home.
     */
    private int computeHomeDelta(boolean move, int current, int home)
    {
        if (move) {
            if (current > home)
                return -SPEED;
            else
                return SPEED;
        }
        else
            return 0;
    }

    /**
     * Adjust the speed randomly (start moving, continue or slow down). The
     * speed returned is in the range [-SPEED .. SPEED].
     */
    private int adjustSpeed(int speed)
    {
        speed = speed + Greenfoot.getRandomNumber(2 * SPEED - 1) - SPEED + 1;
        return capSpeed(speed);
    }

    /**
     * Make sure the speed returned is in the range [-SPEED .. SPEED].
     */
    private int capSpeed(int speed)
    {
        if (speed < -SPEED)
            return -SPEED;
        else if (speed > SPEED)
            return SPEED;
        else
            return speed;
    }

    /**
     * Return 'true' in exactly 'percent' number of calls. That is: a call
     * randomChance(25) has a 25% chance to return true.
     */
    private boolean randomChance(int percent)
    {
        return Greenfoot.getRandomNumber(100) < percent;
    }

}
