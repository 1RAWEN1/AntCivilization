import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//renamed
public class Creature  extends Live
{
    public final int SPEED = 3;

    private int deltaX;
    private int deltaY;

    private Actor takenObject;
    
    private Actor target;

    public Creature()
    {
        getImage().setTransparency(100);
        deltaX = 0;
        deltaY = 0;
    }
    
    public void take(Actor obj){
        takenObject = obj;
    }
    
    public void carry(){
        if(takenObject != null){
            takenObject.setRotation(getRotation());
            takenObject.setLocation(getX() + (int) ((1 + getImage().getWidth() / 2) * Math.cos(Math.toRadians(getRotation())))
            , getY() + (int) ((1 + getImage().getWidth() / 2) * Math.sin(Math.toRadians(getRotation()))));
        }
    }
    
    public void put(){
        takenObject.setLocation(getX(), getY());
        takenObject = null;
    }
    
    public void remove(){
        getWorld().removeObject(takenObject);
        takenObject = null;
    }

    public void randomWalk()
    {
        if (randomChance(50)) {
            deltaX = adjustSpeed(deltaX);
            deltaY = adjustSpeed(deltaY);
        }
        purposefulWalk();
    }

    public void walkTowardsHome()
    {
        if(getHomeHill() == null) {
            return;
        }
        if (randomChance(30) && !intersects(getHomeHill())) {
            randomWalk();
        }
        else {
            headRoughlyTowards(getHomeHill());
            purposefulWalk();
        }
    }
    
    public void walkAwayFromHome()
    {
        if(getHomeHill() == null) {
            return;
        }
        if (randomChance(30)) {
            randomWalk();
        }
        else {
            headRoughlyTowards(getHomeHill());
            deltaX = -deltaX;
            deltaY = -deltaY;
            purposefulWalk();
        }
    }

    public boolean moved(){
        return deltaX != 0 || deltaY != 0;
    }

    public void headTowards(int x, int y){
        deltaX = capSpeed(x - getX());
        deltaY = capSpeed(y - getY());
    }
    public void headTowards(Actor target)
    {
        if(target != null && target.getWorld() != null){
            this.target=target;
            deltaX = capSpeed(target.getX() - getX());
            deltaY = capSpeed(target.getY() - getY());
        }
    }
    
    public void turnTowards(Actor target){
        turnTowards(target.getX(), target.getY());
    }
    
    private int cof = 1;

    public void updateCof(){
        if(Greenfoot.getRandomNumber(2) == 0){
            cof = -1;
        }
    }

    public void setCof(int cof){
        this.cof = cof;
    }

    private int needSpeed(int speed){
        cof = 1;
        if(speed < 0){
            cof = -1;
        }
        return cof*SPEED;
    }
    
    private int randomSpeed(){
        return cof*SPEED;
    }
    
    public void comeHome(){
        moveInHome1();
        atTarget();
    }
    
    
    private int startX;
    private int startY;
    
    private int runningDirection = 270;
    private boolean leftHand;
    private int sum;
    
    private int rot;
    
    private boolean runPurposefully;
    
    private int razn = 0;
    
    private void turnWhenObsIsAhead(Actor obs){
        if(!runPurposefully){
            runPurposefully = true;

            if(target != null){
                turnTowards(target);
                rot = getRotation();
                runningDirection = ((rot + 45) / 90) * 90;
                setRotation(0);
            }
            else if(obs != touchingBlock){
                turnTowards(obs);
                rot = getRotation();
                runningDirection = ((rot + 45) / 90) * 90;
                setRotation(0);
            }
            else {
                runningDirection = Greenfoot.getRandomNumber(4)*90;
            }
            leftHand = rot < runningDirection;
            
            setLocation(startX + (int) (Math.cos(Math.toRadians(runningDirection)) * SPEED), startY + (int) (Math.sin(Math.toRadians(runningDirection)) * SPEED));
            
            if(leftHand){
                cof = -1;
            }
            else{
                cof = 1;
            }
            if(isTouching(UndergroundObs.class) && isUnderGround() || isTouching(Obs.class) && !isUnderGround()){
                if(leftHand){
                    runningDirection -= 90;
                    sum--;
                }
                else{
                    runningDirection += 90;
                    sum++;
                }
            }
            
            if(sum == 0 && !isUnderGround()){
                runPurposefully = false;
            }
        }
        else{
            if(leftHand){
                runningDirection -= 90;
                razn-=90;
                sum--;
            }
            else{
                runningDirection += 90;
                razn+=90;
                sum++;
            }
        }
    }
    
    public void purposefulWalk(){
        
        if(target != null && target.getWorld() == null){
            target = null;
        }
        //try{
            if(target != null && intersects(target) || !isUnderGround() && sum == 0){
                if(target != null && intersects(target)){
                    atTarget();
                }
                walk();
            }
            else{
                turnInMaze();
            }
        //}catch(Exception e){System.out.println(target+" "+target.getWorld());Greenfoot.stop();}
    }
    
    private void isAtEdgeOfTheWorld(){
        runningDirection += 180;
        leftHand = !leftHand;
    }
    
    Block touchingBlock;
    private Obs touchingObs;
    public void walk()
    {
        razn=0;
        startX = getX();
        startY = getY();
        rot = getRotation();
        setRotation(0);
        
        setLocation(startX + deltaX, startY);
        if(!isUnderGround()){
            touchingObs = null;
            if(isTouching(Obs.class)){
                deltaX = 0;
                deltaY = needSpeed(deltaY);
                touchingObs = (Obs) getOneIntersectingObject(Obs.class);
            }
            
            setLocation(startX + deltaX,startY + deltaY);
            if(isTouching(Obs.class)){
                deltaY = 0;
                if(touchingObs == null){
                    deltaX = needSpeed(deltaX);
                    touchingObs = (Obs) getOneIntersectingObject(Obs.class);
                }
            }
            
            if(touchingObs != null){
                turnWhenObsIsAhead(touchingObs);
            }
        }
        else{
            touchingBlock = null;
            if(isTouching(UndergroundObs.class)){
                deltaX = 0;
                deltaY = needSpeed(deltaY);
                touchingBlock = (Block) getOneIntersectingObject(Block.class);
            }
            
            setLocation(startX + deltaX,startY + deltaY);
            if(isTouching(UndergroundObs.class)){
                deltaY = 0;
                if(touchingBlock == null){
                    deltaX = needSpeed(deltaX);
                    touchingBlock = (Block) getOneIntersectingObject(Block.class);
                }
            }
            
            if(touchingBlock != null){
                if(touchingBlock.canDig(this)){
                    touchingBlock.dig(this);
                }
                else{
                    turnWhenObsIsAhead(touchingBlock);
                }
            }
        }
        
        setLocation(startX + deltaX,startY + deltaY);
        
        if(isUnderGround() && isTouching(UndergroundObs.class)){
            deltaX=0;
            deltaY=0;
            setLocation(startX,startY);
        }
        else if(!isUnderGround() && isTouching(Obs.class)){
            deltaX=0;
            deltaY=0;
            setLocation(startX,startY);
        }

        if(deltaX == 0 && deltaY == 0){
            setRotation(rot);
        }
        else{
            setRotation((int) (180 * Math.atan2(deltaY, deltaX) / Math.PI));
        }
        
        if(touchingBlock != null && touchingBlock.getWorld() != null && touchingBlock.canDig(this)){
            turnTowards(touchingBlock);
        }
        
        if(isAtEdge()){
            setCof(cof * -1);
        }
    }

    public boolean haveImpulse() {
        return impulseX + impulseY != 0;
    }

    public void impulseMovement(){
        deltaX = impulseX;
        deltaY = impulseY;

        startX = getX();
        startY = getY();
        setRotation(0);

        setLocation(startX + deltaX, startY);
        if(isTouching(Obstacle.class)){
            deltaX = 0;
        }

        setLocation(startX + deltaX,startY + deltaY);
        if(isTouching(Obstacle.class)){
            deltaY = 0;
        }

        setLocation(startX + deltaX,startY + deltaY);

        setRotation((int) (180 * Math.atan2(deltaY, deltaX) / Math.PI));

        if(impulseX > 0) {
            impulseX--;
        }
        else if(impulseX < 0){
            impulseX++;
        }
        if(impulseY > 0) {
            impulseY--;
        }
        else if(impulseY < 0){
            impulseY++;
        }
    }
    
    public void atTarget(){
        sum = 0;
        runPurposefully = false;
        runningDirection = Greenfoot.getRandomNumber(4)*90;
        target = null;
    }
    
    private final int dist=3;
    
    private int startx;
    private int starty;
    private int startRot;
    private void turnInMaze(){
        headTowards(getX() + (int) (Math.cos(Math.toRadians(runningDirection)) * SPEED), getY() + (int) (Math.sin(Math.toRadians(runningDirection)) * SPEED));
        walk();

        if(sum != 0){
            startx = getX();
            starty = getY();
            
            startRot = getRotation();
            
            runningDirection %=360;
            if(leftHand){
                turn(90);
                move(dist);
                boolean tObs1 = getOneIntersectingObject(UndergroundObs.class) != null;
                boolean tObsLeft = getOneIntersectingObject(Obs.class) != null;
                
                setLocation(startx,starty);
                setRotation(startRot);
                move(-getImage().getWidth());
                boolean tObs2 = getOneIntersectingObject(UndergroundObs.class) != null;
                boolean tObsBack = getOneIntersectingObject(Obs.class) != null;
                turn(90);
                move(dist);
                boolean tObs3 = getOneIntersectingObject(UndergroundObs.class) != null;
                boolean tObsLeftBack = getOneIntersectingObject(Obs.class) != null;
                setLocation(startx, starty);
                setRotation(startRot);
                
                if(!tObs1 && !tObs2 && tObs3 && isUnderGround()
                || !tObsLeft && !tObsBack && tObsLeftBack && !isUnderGround()){
                    runningDirection += 90;
                    sum++;
                }
            }
            else{
                turn(-90);
                move(dist);
                boolean tObs1 = getOneIntersectingObject(UndergroundObs.class) != null;
                boolean tObsLeft = getOneIntersectingObject(Obs.class) != null;
                
                setLocation(startx, starty);
                setRotation(startRot);
                move(-getImage().getWidth());
                boolean tObs2 = getOneIntersectingObject(UndergroundObs.class) != null;
                boolean tObsBack = getOneIntersectingObject(Obs.class) != null;
                turn(-90);
                move(dist);
                boolean tObs3 = getOneIntersectingObject(UndergroundObs.class) != null;
                boolean tObsLeftBack = getOneIntersectingObject(Obs.class) != null;
                setLocation(startx,starty);
                setRotation(startRot);
                
                if(!tObs1 && !tObs2 && tObs3 && isUnderGround()
                || !tObsLeft && !tObsBack && tObsLeftBack && !isUnderGround()){
                    runningDirection -= 90;
                    sum--;
                }
            }
        
            if(sum == 0 && !isUnderGround()){
                atTarget();
            }
        }
        
        if(isAtEdge()){
            atTarget();
        }
    }

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

    private int adjustSpeed(int speed)
    {
        speed = speed + Greenfoot.getRandomNumber(2 * SPEED - 1) - SPEED + 1;
        return capSpeed(speed);
    }

    private int capSpeed(int speed)
    {
        if (speed < -SPEED)
            return -SPEED;
        else if (speed > SPEED)
            return SPEED;
        else
            return speed;
    }

    private boolean randomChance(int percent)
    {
        return Greenfoot.getRandomNumber(100) < percent;
    }

}
