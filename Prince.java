import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class QueenAnt here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Prince extends Creature
{
    private final int viewingRadius = 60;
    public Prince(AntHill home){
        setMAX_AGE(1000);
        setRotation(180);
        setHomeHill(home);

        setHp(3);

        setFood(10);

        setCanDig(false);
    }

    public void act()
    {
        if(isUnderGround()){
            inHome();
        }
        else{
            searchForFood();
        }
        if(atHome() && !isFly()){
            comeHome();
        }

        eatFood();

        updateAnimation();

        checkFood();

        regenerateHp();

        die();
    }

    private Live enemy;
    private boolean canSeeEnemy(){
        enemy=null;
        for(Live l : getObjectsInRange(viewingRadius, Live.class)){
            if(l.getHomeHill().getTeam()!=getHomeHill().getTeam() && canInteract(l)){
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
        if (getHomeHill() != null && target == getHomeHill()) {
            return (Math.abs(getX() - getHomeHill().getX()) < 2) && (Math.abs(getY() - getHomeHill().getY()) < 2);
        } else {
            return false;
        }
    }

    boolean findQueen;
    public void searchForQueen(){
        findQueen = false;
        for(QueenAnt queen : getObjectsInRange(viewingRadius, QueenAnt.class)){
            if(queen.getHomeHill().getTeam()!=getHomeHill().getTeam() && canInteract(queen) && !queen.createAntHill){
                headTowards(queen);
                purposefulWalk();
                findQueen = true;
                break;
            }
        }
    }

    private void searchForFood()
    {
        if(!foodNotFully() || isFly()){
            fly();
            searchForQueen();
            if(!findQueen)
                randomWalk();
        }
        else if(canSeeEnemy() || getHomeHill().getFood() > 0){
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

    private Actor target;
    private void inHome(){
        target = null;
        if (foodNotFully() && getObjectsInRange(viewingRadius, TakenFood.class).size() > 0) {
            target = getObjectsInRange(viewingRadius, TakenFood.class).get(0);
        }
        else if(foodNotFully() && getHomeHill().getFood() > 0){
            target = null;
        }
        else if (getHomeHill().getFood() == 0 || !foodNotFully()) {
            target = getHomeHill();
        }

        if(target != null && target.getWorld() != null){
            headTowards(target);
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

    GreenfootImage prince = new GreenfootImage("prince1.png");
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
            setImage("prince" + shot + ".png");
        }
        else{
            setImage("wings" + shot + ".png");
        }

        if(isFly()){
            getImage().drawImage(prince, getImage().getWidth() - prince.getWidth(), (getImage().getHeight() / 2) - (prince.getHeight() / 2));
            getImage().drawImage(new GreenfootImage("wings" + shot + ".png"), 0 ,0);
        }
        else {
            getImage().drawImage(new GreenfootImage("princeWings.png"), 0, 0);
        }

        getImage().setColor(AntWorld.teamColors.get(getHomeHill().getTeam() - 1));
        getImage().fillRect(getImage().getWidth() - 11,(getImage().getHeight() / 2) - 1,1,2);

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
            getHomeHill().princeDead();
            getWorld().removeObject(this);
        }
    }
}
