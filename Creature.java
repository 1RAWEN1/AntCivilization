import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//renamed
public class Creature  extends Live
{
    public int SPEED = 3;

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

    public Actor getTakenObject(){
        return takenObject;
    }

    public void take(Actor obj){
        if(takenObject == null)
            takenObject = obj;
    }
    
    public void carry(){
        if(takenObject != null && takenObject.getWorld() != null) {
            if (cof1 == 1) {
                takenObject.setRotation(getRotation());
                takenObject.setLocation(getX() + (int) ((getImage().getWidth() / 2) * Math.cos(Math.toRadians(getRotation())))
                        , getY() + (int) ((getImage().getWidth() / 2) * Math.sin(Math.toRadians(getRotation()))));
            } else {
                turnTowards(takenObject);

                takenObject.setLocation(takenObject.getX() + deltaX, takenObject.getY() + deltaY);
            }
        }
    }

    public void setSpeed(int speed){
        SPEED = speed;
    }
    
    public void put(){
        /*if(takenObject != null && getClass().equals(Ant.class) && takenObject.getClass().equals(TakenGround.class)){
            System.out.println(takenObject.getClass() + " " + this);
            Greenfoot.stop();
        }*/
        if(takenObject != null && cof1 == 1) {
            takenObject.setLocation(getX(), getY());
            takenObject = null;
        }
        else if(takenObject != null){
            takenObject = null;
        }
    }


    public boolean haveTakenObject(){
        return takenObject != null;
    }
    
    public void remove(){
        getWorld().removeObject(takenObject);
        takenObject = null;
    }

    public void randomWalk()
    {
        // && getRotation() == secondRot || randomChance(50) && Math.abs(secondRot) + getRotation() == 360
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
        if (randomChance(50)) {
            randomWalk();
        }
        else {
            headRoughlyTowards(getHomeHill());
            deltaX = -deltaX;
            deltaY = -deltaY;
            purposefulWalk();
        }
    }

    int lastX;
    int lastY;
    boolean moved;
    public boolean moved(){
        moved = lastX != getX() || lastY != getY();
        lastX = getX();
        lastY = getY();
        return moved;
    }

    public boolean moved1(){
        moved = lastX != getX() || lastY != getY();
        return moved;
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

    public Actor getTarget(){
        return target;
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
    
    public int razn = 0;

    private int myRot;
    private void turnWhenObsIsAhead(Actor obs){
        if(!runPurposefully){
            myRot = getRotation();
            runPurposefully = true;

            startx = getX();
            starty = getY();
            /*if(startDeltaX != 0 && startDeltaY != 0){
                turnTowards(startX + startDeltaX, startY + startDeltaY);
                rot = getRotation();
                runningDirection = ((rot + 45) / 90) * 90;
                setRotation(myRot);
            }
            else*/// if(obs != touchingBlock){
                turnTowards(obs);
                rot = getRotation();
                runningDirection = ((rot + 45) / 90) * 90;
                setRotation(myRot);

                runningDirection %= 360;

                if(startDeltaX != 0 || startDeltaY != 0){
                    rot = (int) (180 * Math.atan2(startDeltaY, startDeltaX) / Math.PI);
                    rot += 360;
                    rot %= 360;
                }
            /*}
            else {
                runningDirection = Greenfoot.getRandomNumber(4)*90;
            }*/
            if(isUnderGround() || !haveTakenObject())
                leftHand = rot < runningDirection && Math.abs(rot - runningDirection) < 180
                        || rot > runningDirection && Math.abs(rot - runningDirection) > 180
                        || Math.abs(rot - runningDirection) == 180 && Greenfoot.getRandomNumber(2) == 1
                        || rot == runningDirection && Greenfoot.getRandomNumber(2) == 1;
            else
                leftHand = Greenfoot.getRandomNumber(2) == 1;

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

            setLocation(startx, starty);
            
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
        if(isFly()){
            SPEED = 6;
        }
        else{
            SPEED = 3;
        }
        if(target != null && target.getWorld() == null){
            target = null;
        }
        //try{
        if(!stop && !moved1()) {
            if (target != null && intersects(target) || !isUnderGround() && sum == 0) {
                if (target != null && intersects(target)) {
                    atTarget();
                }
                walk();
            } else {
                turnInMaze();
            }

            if(haveTakenObject()){
                carry();
            }
        }
        else{
            stop = false;
        }
        //}catch(Exception e){System.out.println(target+" "+target.getWorld());Greenfoot.stop();}
    }

    private void isAtEdgeOfTheWorld(){
        runningDirection += 180;
        leftHand = !leftHand;
    }

    boolean stop;
    public void stop(){
        stop = true;
    }
    
    Block touchingBlock;
    private Obs touchingObs;

    //int secondRot;
    boolean canDig = true;

    public void setCanDig(boolean canDig){
        this.canDig = canDig;
    }

    double cof1 = 1;
    private int startDeltaX;
    private int startDeltaY;
    public void walk()
    {
        startX = getX();
        startY = getY();
        rot = getRotation();
        startDeltaX = deltaX;
        startDeltaY = deltaY;

        touchingBlock = null;
        touchingObs = null;

        if(deltaX != 0 || deltaY != 0) {
            setRotation((int) (180 * Math.atan2(deltaY, deltaX) / Math.PI));
        }
        setRotation(((getRotation() + 45) / 90) * 90);
        updateSize();

        cof1 = 1;

        if(takenObject != null){
            cof1 = Math.min(1, (double)getImage().getWidth() / takenObject.getImage().getWidth());
            deltaX *= cof1;
            deltaY *= cof1;
        }

        setLocation(startX + deltaX, startY);
        if(!isUnderGround()){
            if(isTouching(Obs.class) && !isFly()){
                touchingObs = (Obs) getOneIntersectingObject(Obs.class);
                touchObs();
                deltaX = getX() - startX;
                deltaY = needSpeed(deltaY);

                if(deltaX != 0 || deltaY != 0) {
                    setRotation((int) (180 * Math.atan2(deltaY, deltaX) / Math.PI));
                }
                setRotation(((getRotation() + 45) / 90) * 90);
                updateSize();
            }
            
            setLocation(getX(),startY + deltaY);
            if(isTouching(Obs.class) && !isFly()){
                if(touchingObs == null){
                    touchingObs = (Obs) getOneIntersectingObject(Obs.class);
                    deltaX = needSpeed(deltaX);
                }
                touchObs();
                deltaY = getY() - startY;

                if(deltaX != 0 || deltaY != 0) {
                    setRotation((int) (180 * Math.atan2(deltaY, deltaX) / Math.PI));
                }
                setRotation(((getRotation() + 45) / 90) * 90);
                updateSize();
            }
        }
        else{
            if(isTouching(UndergroundObs.class) && !isFly()){
                touchingBlock = (Block) getOneIntersectingObject(Block.class);
                touchObs();
                deltaX = getX() - startX;
                deltaY = needSpeed(deltaY);

                if(deltaX != 0 || deltaY != 0) {
                    setRotation((int) (180 * Math.atan2(deltaY, deltaX) / Math.PI));
                }
                setRotation(((getRotation() + 45) / 90) * 90);
                updateSize();
            }

            setLocation(getX(),startY + deltaY);
            if(isTouching(UndergroundObs.class) && !isFly()){
                if(touchingBlock == null){
                    touchingBlock = (Block) getOneIntersectingObject(Block.class);
                    deltaX = needSpeed(deltaX);
                }
                touchObs();
                deltaY = getY() - startY;

                if(deltaX != 0 || deltaY != 0) {
                    setRotation((int) (180 * Math.atan2(deltaY, deltaX) / Math.PI));
                }
                setRotation(((getRotation() + 45) / 90) * 90);
                updateSize();
            }
        }

        setLocation(startX + deltaX, getY());

        if(!isFly()) {
            /*if (isUnderGround() && isTouching(UndergroundObs.class)) {
                touchingBlock = (Block) getOneIntersectingObject(Block.class);
                if(touchingBlock.canDig(this) && canDig && !haveTakenObject()){
                    touchingBlock.dig(this);
                    deltaX = 0;
                    deltaY = 0;
                }
                else{
                    turnWhenObsIsAhead(touchingBlock);
                }
                touchObs();
            }
            else if(!isUnderGround() && isTouching(Obs.class)){
                touchingObs = (Obs) getOneIntersectingObject(Obs.class);
                turnWhenObsIsAhead(touchingObs);
                touchObs();
            }*/
            if (isUnderGround() && isTouching(UndergroundObs.class) || !isUnderGround() && isTouching(Obs.class)) {
                touchObs();
            }
        }

        if(touchingBlock != null){
            if(touchingBlock.canDig(this) && canDig && !haveTakenObject()){
                touchingBlock.dig(this);
                deltaX = 0;
                deltaY = 0;
            }
            else{
                turnWhenObsIsAhead(touchingBlock);
            }
        }
        else if(touchingObs != null){
            turnWhenObsIsAhead(touchingObs);
        }
        /*if(isUnderGround() && isTouching(UndergroundObs.class)){
            getImage().setColor(Color.RED);
            getImage().fill();
            Greenfoot.stop();
        }*/

        if (deltaX != 0 || deltaY != 0) {
            /*secondRot = (int) (180 * Math.atan2(deltaY, deltaX) / Math.PI);
            if (rot != secondRot) {
                rot = spin(rot, secondRot);

                if (rot != secondRot) {
                    setLocation(startX, startY);
                }
            }*/
            rot = (int) (180 * Math.atan2(deltaY, deltaX) / Math.PI);
        }

        setRotation(rot);

        if(touchingBlock != null && touchingBlock.getWorld() != null && touchingBlock.canDig(this)){
            turnTowards(touchingBlock);
        }
        
        if(isAtEdge()){
            setCof(cof * -1);
        }
    }

    public void updateSize(){
        if(getRotation() == 0 || getRotation() == 180){
            imageXSize = (getImage().getWidth() % 2) + (getImage().getWidth() / 2);
            imageYSize = (getImage().getHeight() % 2) + (getImage().getHeight() / 2);
        }
        else{
            imageYSize = (getImage().getWidth() % 2) + (getImage().getWidth() / 2);
            imageXSize = (getImage().getHeight() % 2) + (getImage().getHeight() / 2);
        }
    }

    int x;
    int y;
    int changeXLoc;
    int changeYLoc;

    int changeXLoc1;
    int changeYLoc1;

    int imageXSize;
    int imageYSize;

    public void touchObs(){
        if(isUnderGround() && isTouching(UndergroundObs.class)) {
            x = getX();
            y = getY();
            changeXLoc = 0;
            changeYLoc = 0;
            for (UndergroundObs uo : getIntersectingObjects(UndergroundObs.class)) {
                changeXLoc1 = (uo.getImage().getWidth() / 2) + imageXSize - Math.abs(getX() - uo.getX());

                changeYLoc1 = (uo.getImage().getHeight() / 2) + imageYSize - Math.abs(getY() - uo.getY());

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
        }
        else if(!isUnderGround() && isTouching(Obs.class)){
            x=getX();
            y=getY();
            changeXLoc = 0;
            changeYLoc = 0;
            for (Obs o : getIntersectingObjects(Obs.class)) {
                changeXLoc1 = (o.getImage().getWidth() / 2) + imageXSize - Math.abs(getX() - o.getX());

                changeYLoc1 = (o.getImage().getHeight() / 2) + imageYSize - Math.abs(getY() - o.getY());

                if (changeXLoc1 <= changeYLoc1 && changeXLoc1 > 0) {
                    changeYLoc1 = 0;
                } else if (changeXLoc1 > changeYLoc1 && changeYLoc1 > 0) {
                    changeXLoc1 = 0;
                }

                changeXLoc = Math.abs(changeXLoc) > changeXLoc1 ? changeXLoc : changeXLoc1;
                changeYLoc = Math.abs(changeYLoc) > changeYLoc1 ? changeYLoc : changeYLoc1;

                if (o.getX() > getX() && changeXLoc == changeXLoc1) {
                    changeXLoc = -changeXLoc1;
                }
                if (o.getY() > getY() && changeYLoc == changeYLoc1) {
                    changeYLoc = -changeYLoc1;
                }
            }

            setLocation(x + changeXLoc, y + changeYLoc);

            //deltaX = getX() - startX;
            //deltaY = getY() - startY;
        }
    }
    public void touchObsX(){
        x=getX();
        changeXLoc = 0;
        if(isUnderGround()) {
            for (UndergroundObs uo : getIntersectingObjects(UndergroundObs.class)) {
                changeXLoc1 = (uo.getImage().getWidth() / 2) + imageXSize - Math.abs(getX() - uo.getX()) + 1;

                changeXLoc = Math.abs(changeXLoc) > changeXLoc1 ? changeXLoc : changeXLoc1;

                if (uo.getX() > getX() && changeXLoc == changeXLoc1) {
                    changeXLoc = -changeXLoc1;
                }
            }
        }
        else{
            for (Obs o : getIntersectingObjects(Obs.class)) {
                changeXLoc1 = (o.getImage().getWidth() / 2) + imageXSize - Math.abs(getX() - o.getX()) + 1;

                changeXLoc = Math.abs(changeXLoc) > changeXLoc1 ? changeXLoc : changeXLoc1;

                if (o.getX() > getX() && changeXLoc == changeXLoc1) {
                    changeXLoc = -changeXLoc1;
                }
            }
        }

        setLocation(x + changeXLoc, getY());
    }

    public void touchObsY(){
        y=getY();
        changeYLoc = 0;
        if(isUnderGround()) {
            for (UndergroundObs uo : getIntersectingObjects(UndergroundObs.class)) {
                changeYLoc1 = (uo.getImage().getHeight() / 2) + imageYSize - Math.abs(getY() - uo.getY());

                changeYLoc = Math.abs(changeYLoc) > changeYLoc1 ? changeYLoc : changeYLoc1;

                if (uo.getY() > getY() && changeYLoc == changeYLoc1) {
                    changeYLoc = -changeYLoc1;
                }
            }
        }
        else{
            for (Obs o : getIntersectingObjects(Obs.class)) {
                changeYLoc1 = (o.getImage().getHeight() / 2) + imageYSize - Math.abs(getY() - o.getY());

                changeYLoc = Math.abs(changeYLoc) > changeYLoc1 ? changeYLoc : changeYLoc1;

                if (o.getY() > getY() && changeYLoc == changeYLoc1) {
                    changeYLoc = -changeYLoc1;
                }
            }
        }

        setLocation(getX(), y + changeYLoc);
    }

    public int spin(int myRot1, int needRot){
        int myRot = myRot1;
        if(Math.abs(myRot - needRot) < SPEED * 2 || Math.abs(myRot - needRot) > 360 - (SPEED * 2)){
            myRot = needRot;
        }
        else if(myRot > needRot && myRot - needRot <= 180|| needRot - myRot > 180){
            myRot -= SPEED * 2;
        }
        else{
            myRot += SPEED * 2;
        }
        return myRot;
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
    
    private final int dist = SPEED;
    
    private int startx;
    private int starty;
    private int startRot;
    private void turnInMaze(){
        headTowards(getX() + (int) (Math.cos(Math.toRadians(runningDirection)) * SPEED), getY() + (int) (Math.sin(Math.toRadians(runningDirection)) * SPEED));
        walk();

        if(sum != 0){
            startx = getX();
            starty = getY();
            
            runningDirection %= 360;
            
            startRot = runningDirection;
            setRotation(runningDirection);
            if(leftHand){
                turn(90);
                move(dist);
                boolean tObs1 = getOneIntersectingObject(UndergroundObs.class) != null;
                boolean tObsLeft = getOneIntersectingObject(Obs.class) != null;
                
                setLocation(startx,starty);
                setRotation(startRot);
                move(-SPEED);
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

                if(!tObs1 && !tObs2 && !tObs3 && isUnderGround()
                        || !tObsLeft && !tObsBack && !tObsLeftBack && !isUnderGround()){
                    atTarget();
                }
            }
            else{
                turn(-90);
                move(dist);
                boolean tObs1 = getOneIntersectingObject(UndergroundObs.class) != null;
                boolean tObsLeft = getOneIntersectingObject(Obs.class) != null;
                
                setLocation(startx, starty);
                setRotation(startRot);
                move(-SPEED);
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

                if(!tObs1 && !tObs2 && !tObs3 && isUnderGround()
                        || !tObsLeft && !tObsBack && !tObsLeftBack && !isUnderGround()){
                    atTarget();
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
