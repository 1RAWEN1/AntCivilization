import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class QueenAnt here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class QueenAnt extends Creature
{
    boolean isQueen = true;
    private final int viewingRadius = 60;
    public QueenAnt(AntHill home){
        setMAX_AGE(100000);
        setRotation(180);
        setHomeHill(home);
        
        setHp(8);
        
        setFood(10);
    }

    public void notQueen(){
        isQueen = false;
    }

    public void act() 
    {
        if(isUnderGround()){
            if(isQueen) {
                spawnEgg();
            }
            inHome();
        }
        else{
            searchForFood();
        }
        if(atHome() && !isFly()){
            comeHome();
            if(isCarryingGround){
                updateHome();
            }
        }

        setCanDig(isQueen);
        
        eatFood();
        
        updateAnimation();

        checkFood();

        defend();

        regenerateHp();
        
        die();
    }

    private void updateHome(){
        if(!isUnderGround()){
            getHomeHill().countEndurance();
            put();
            getWorld().removeObject(takenGround);
            isCarryingGround = false;
        }
    }

    private Live enemy;
    private boolean canSeeEnemy(){
        enemy=null;
        for(Live l : getObjectsInRange(viewingRadius, Live.class)){
            if(l.getHomeHill().getTeam()!=getHomeHill().getTeam() && canInteract(l) && !l.isFly()){
                enemy=l;
            }
        }
        return enemy!=null;
    }

    public void defend(){
        dTimer.calculate();

        if(canSeeEnemy()){
            if(intersects(enemy)){
                turnTowards(enemy);
            }

            attack();
        }
    }

    private final SimpleTimer dTimer=new SimpleTimer();
    private final int dSteps = 50;
    private void attack(){
        if(intersects(enemy) && enemy.isUnderGround() == isUnderGround() && dTimer.getTime()>=dSteps){
            enemy.damage(1);
            dropAttackPheromone();

            dTimer.update();
        }
    }

    private AttackPheromone ap;

    private final int startAPheromoneValue = 300;
    private void dropAttackPheromone(){
        if(ap!=null && ap.getWorld()==null){
            ap=null;
        }
        if(ap==null || !intersects(ap)){
            ap=new AttackPheromone(startAPheromoneValue, 250);
            getWorld().addObject(ap, getX(), getY());
        }
    }

    private boolean canSeeEnemy1(){
        enemy=null;
        for(Live l : getObjectsInRange(viewingRadius, Live.class)){
            if(l.getHomeHill().getTeam()!=getHomeHill().getTeam() && !l.isUnderGround()){
                enemy=l;
            }
        }
        return enemy!=null;
    }

    private void eatFood(Food food){
        food.takeSome();
        eat();
    }

    public void checkFood()
    {
        Food food = (Food) getOneIntersectingObject(Food.class);
        if(food!=null && !isUnderGround() && foodNotFully() && !isFly()){
            eatFood(food);
        }
    }

    public boolean atHome(){
        if (getHomeHill() != null && myTarget == getHomeHill()) {
            return (Math.abs(getX() - getHomeHill().getX()) < 2) && (Math.abs(getY() - getHomeHill().getY()) < 2);
        } else {
            return false;
        }
    }

    boolean findMale;
    public void searchForMale(){
        findMale = false;
        for(Prince prince : getObjectsInRange(viewingRadius, Prince.class)){
            if(prince.getHomeHill().getTeam()!=getHomeHill().getTeam() && canInteract(prince)){
                headTowards(prince);
                purposefulWalk();
                findMale = true;
                break;
            }
        }
    }

    public void intersectMale(){
        for(Prince prince : getIntersectingObjects(Prince.class)){
            if(prince.getHomeHill().getTeam()!=getHomeHill().getTeam() && canInteract(prince)){
                createAntHill = true;
                break;
            }
        }
    }

    public void createAntHill(){
        createTimer.calculate();
        if(getObjectsInRange(120, Block.class).size() == 0 && getX() > 40 && getY() > 40 && getX() < getWorld().getWidth() - 40 && getY() < getWorld().getHeight() - 40){
            AntHill ah = new AntHill(40, true, getHomeHill().getTeam());
            ah.setAntNumber(0);
            getHomeHill().princessDead();

            getWorld().addObject(ah, getX(), getY());

            setHomeHill(ah);
            isQueen = true;
            ah.newQueen();

            createAntHill = false;

            myTarget = getHomeHill();
        }
        else if(getObjectsInRange(viewingRadius, AntHill.class).size() > 0 && getObjectsInRange(viewingRadius, AntHill.class).get(0) != getHomeHill() && !getObjectsInRange(viewingRadius, AntHill.class).get(0).haveQueen()){
            getObjectsInRange(viewingRadius, AntHill.class).get(0).setTeam(getHomeHill().getTeam());

            setHomeHill(getObjectsInRange(viewingRadius, AntHill.class).get(0));
            getObjectsInRange(viewingRadius, AntHill.class).get(0).newQueen();

            isQueen = true;
            createAntHill = false;

            myTarget = getObjectsInRange(viewingRadius, AntHill.class).get(0);
        }
        else if(createTimer.getTime() > 1500 || !getHomeHill().haveQueen()){
            isQueen = true;
            getHomeHill().newQueen1();

            createAntHill = false;
        }
    }

    boolean createAntHill = false;
    private SimpleTimer createTimer = new SimpleTimer();
    private void searchForFood()
    {
        if(!foodNotFully() && !isQueen || isFly()){
            if(!createAntHill) {
                fly();
                searchForMale();
                if(!findMale)
                    randomWalk();

                intersectMale();
            }
            else{
                if(isFly() && !canSeeEnemy1()){
                    stopFly();
                }
                else if(!isFly()){
                    if(canSeeEnemy()){
                        walkTowardsHome();
                    }
                    else if(canSeeFood() && foodNotFully()){
                        walkTowardsFood();
                    }
                    else if (smellPheromone() && foodNotFully()) {
                        walkTowardsPheromone();
                    }
                    else {
                        randomWalk();
                    }
                    createAntHill();
                }
                else{
                    randomWalk();
                }
            }
        }
        else if(canSeeEnemy() || !foodNotFully() || calculateDistToHome() < viewingRadius && getHomeHill().getFood() > 0){
            walkTowardsHome();
        }
        else if(canSeeFood()){
            walkTowardsFood();
        }
        else if (smellPheromone()) {
            walkTowardsPheromone();
        }
        else{
            randomWalk();
        }
    }

    public boolean smellPheromone()
    {
        Actor ph = getOneIntersectingObject(Pheromone.class);
        return (ph != null);
    }

    private int phX;
    private int phY;
    private int n;

    public void walkTowardsPheromone()
    {
        phX = 0;
        phY = 0;
        n = 0;

        int sumOfIntensity = 0;
        for(Pheromone pheromone : getIntersectingObjects(Pheromone.class)){
            int rot = getRotation();
            turnTowards(pheromone.getX(), pheromone.getY());
            int rotToPh = getRotation();
            setRotation(rot);
            if (Math.abs(rot - rotToPh) <= 90 || Math.abs(rot - rotToPh) > 270) {
                sumOfIntensity += pheromone.getIntensity();
            }
        }

        for(Pheromone pheromone : getIntersectingObjects(Pheromone.class)){
            //if(!intersects(pheromone)) {
            int rot = getRotation();
            turnTowards(pheromone.getX(), pheromone.getY());
            int rotToPh = getRotation();
            setRotation(rot);
            if (Math.abs(rot - rotToPh) <= 90 || Math.abs(rot - rotToPh) > 270) {
                n++;
                phX += (int)(pheromone.getX() * ((double)pheromone.getIntensity() / sumOfIntensity));
                phY += (int)(pheromone.getY() * ((double)pheromone.getIntensity() / sumOfIntensity));
            }
            //}
        }

        if(n != 0) {
            headTowards(phX, phY);
            purposefulWalk();
        }
        else{
            randomWalk();
        }
    }

    private boolean canSeeFood(){
        return getObjectsInRange(viewingRadius, Food.class).size()>0 && !isUnderGround();
    }

    private void walkTowardsFood(){
        Actor food = getObjectsInRange(viewingRadius, Food.class).get(0);
        if (food != null) {
            headTowards(food);
            purposefulWalk();
        }
    }
    
    private Egg newEgg;
    private void spawnEgg(){
        if(Greenfoot.getRandomNumber(100) < 10 && getObjectsInRange(viewingRadius,Egg.class).size() < Math.min((getHomeHill().getFood() / 3) + (food / 3), getHomeHill().getNurseNumber() + getHomeHill().queens) && !getHomeHill().fully() && !foodNotFully()){
            food--;
            newEgg = new Egg(Greenfoot.getRandomNumber(2) == 1 ? 2 : Greenfoot.getRandomNumber(50) < 20 ? 3 : 1, getHomeHill(), getHomeHill().getFood() >= 10);
            getWorld().addObject(newEgg,getX() + (int) (Math.cos(Math.toRadians(getRotation() + 180)) * getImage().getWidth() / 2)
            , getY() + (int) (Math.sin(Math.toRadians(getRotation() + 180)) * getImage().getWidth() / 2));

            getHomeHill().newEgg(newEgg.getMAX_FOOD());
        }
        if(newEgg != null){
            if(intersects(newEgg) && newEgg.foodNotFully() && food > 1 + needFood()){
                newEgg.countFood();
                food--;
            }
            if(newEgg.getWorld() == null){
                newEgg = null;
            }
        }
    }
    
    private Actor myTarget;

    private boolean isCarryingGround;
    TakenGround takenGround;

    Block targetBlock;
    private void inHome(){
        myTarget = null;

        if(isCarryingGround){
            myTarget = getHomeHill();
        }
        else if(newEgg != null && !intersects(newEgg) && getFood() > 1 && isQueen) {
            myTarget = newEgg;
        }
        else {
            if (foodNotFully() && getObjectsInRange(viewingRadius, TakenFood.class).size() > 0) {
                myTarget = getObjectsInRange(viewingRadius, TakenFood.class).get(0);
            }
            else if(foodNotFully() && getHomeHill().getFood() > 0){
                myTarget = null;
            }
            else if (getHomeHill().getFood() == 0 && foodNotFully() || !isQueen) {
                if (myTarget == null || newEgg != null && newEgg.foodNotFully() && food < 1 + newEgg.needFood()) {
                    myTarget = getHomeHill();
                }
            }
        }

        if(touchingBlock!=null && touchingBlock.canDig(this) && canDig && !isCarryingGround){
            myTarget = touchingBlock;
            if(haveTakenObject()) {
                put();
            }
            else{
                targetBlock = touchingBlock;
                takenGround = new TakenGround();
                getWorld().addObject(takenGround, getX(), getY());
                take(takenGround);
                isCarryingGround = true;
            }
        }
        else if(myTarget == null && targetBlock != null){
            myTarget = targetBlock;
        }

        if(myTarget!=null && myTarget.getWorld()==null){
            if(myTarget == targetBlock){
                targetBlock = null;
            }
            myTarget=null;
        }
            
        if(myTarget != null){
            headTowards(myTarget);
            purposefulWalk();
        }
        else{
            randomWalk();
        }
        
        eat1();
    }
    
    public void eat1(){
        TakenFood tf = (TakenFood)getOneIntersectingObject(TakenFood.class);
        if(tf != null && foodNotFully() && tf.getUnderGround() == isUnderGround() && !isFly()){
            tf.eat();
            eat();
        }
    }
    
    private int animation = 0;
    
    private final int step = 2;
    
    private int shot = 1;
    
    private final int MAX_SHOT = 3;

    GreenfootImage queen = new GreenfootImage("queen1.png");
    private void updateAnimation(){
        if(moved()){
            animation++;
        }
        if(animation >= step){
            animation = 0;
            shot++;
        }
        if(shot > MAX_SHOT){
            shot = 1;
        }
        if(!isFly()) {
            setImage("queen" + shot + ".png");
        }
        else{
            setImage("wings" + shot + ".png");
        }

        if(isFly()){
            getImage().drawImage(queen, getImage().getWidth() - queen.getWidth(), (getImage().getHeight() / 2) - (queen.getHeight() / 2));
            getImage().drawImage(new GreenfootImage("wings" + shot + ".png"), 0 ,0);
        }
        else if(!isQueen){
            getImage().drawImage(new GreenfootImage("wings.png"), 0, 0);
        }

        getImage().setColor(AntWorld.teamColors.get(getHomeHill().getTeam() - 1));
        getImage().fillRect(getImage().getWidth() - 15,(getImage().getHeight() / 2) - 1,1,2);
        
        if(isUnderGround()){
            getImage().setTransparency(100);
        }



        if(isFly()){
            flyAnimation();
        }
    }
    
    private void die(){
        if(hp <= 0 || food <= 0 || grow()){
            if(food > 0){
                getWorld().addObject(new Food(food, 2, 1), getX(), getY());
            }
            if(isQueen) {
                getHomeHill().queenDead();
            }
            else{
                getHomeHill().princessDead();
            }
            getWorld().removeObject(this);
        }
    }
}
