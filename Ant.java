import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

public class Ant extends Creature
{
    /** Every how many steps can we place a pheromone drop. */
    private static final int MAX_PH_LEVEL = 18;

    /** How long do we keep direction after finding pheromones. */
    private static final int PH_TIME = 30;

    /** Indicate whether we have any food with us. */
    private boolean carryingFood = false;

    /** How much pheromone do we have right now. */
    private int pheromoneLevel = MAX_PH_LEVEL;

    /** How well do we remember the last pheromone - larger number: more recent */
    private int foundLastPheromone = 0;
    
    private int radius = 50;
    
    private int level = 1;
    
    private final int MAX_LEVEL = 30;
    
    private final int needFood=1;

    /**
     * Create an ant with a given home hill. The initial speed is zero (not moving).
     */
    public Ant(AntHill home)
    {
        updateCof();
        setHomeHill(home);
    }

    /**
     * Do what an ant's gotta do.
     */
    int timer;
    public void act()
    {
        
        if (carryingFood) {
            walkTowardsHome();
            handlePheromoneDrop();
            checkHome();
        }
        else {
            searchForFood();
        }
    }
    
    public void eat(){
        timer++;
        if(timer/250==2){
            timer=0;
        }
    }

    /**
     * Walk around in search of food.
     */
    private void searchForFood()
    {
        if(canSeeFood()){
            walkTowardsFood();
        }
        else if (smellPheromone()) {
            walkTowardsPheromone();
        }
        else {
            randomWalk();
        }
        checkFood();
    }
    
    public boolean canSeeFood(){
        return getObjectsInRange(radius, Food.class).size()>0;
    }
    
    public void walkTowardsFood(){
        Actor food = getObjectsInRange(radius, Food.class).get(0);
        if (food != null) {
            headTowards(food);
            walk();
        }
    }

    /**
     * Are we home? Drop the food if we are, and start heading back out.
     */
    private void checkHome()
    {
        if (atHome()) {
            dropFood();
        }
    }

    /**
     * Are we home?
     */
    private boolean atHome()
    {
        if (getHomeHill() != null) {
            return (Math.abs(getX() - getHomeHill().getX()) < 4) && (Math.abs(getY() - getHomeHill().getY()) < 4);
        }
        else {
            return false;
        }

    }

    /**
     * Is there any food here where we are? If so, take some!.
     */
    public void checkFood()
    {
        Food food = (Food) getOneIntersectingObject(Food.class);
        if (food != null) {
            takeFood(food);

        }
    }

    /**
     * Take some food from a fool pile.
     */
    final int startPheromoneValue=60;
    private void takeFood(Food food)
    {
        carryingFood = true;
        food.takeSome();
        lastPh = new Pheromone(startPheromoneValue + level*4);
        getWorld().addObject(lastPh, getX(), getY());
        setImage("ant-with-food.gif");
    }

    /**
     * Drop our food in the ant hill.
     */
    private void dropFood()
    {
        carryingFood = false;
        getHomeHill().countFood();
        if(level<MAX_LEVEL){
            level++;
        }
        lastPh=null;
        setImage("ant.gif");
    }

    /**
     * Check whether we can drop some pheromone yet. If we can, do it.
     */
    Pheromone lastPh;
    
    int phIndex;
    private void handlePheromoneDrop()
    {
        if (lastPh!=null && !intersects(lastPh)) {
            lastPh = new Pheromone(startPheromoneValue + level*4);
            getWorld().addObject(lastPh, getX(), getY());
        }
    }

    /**
     * Check whether we can smell pheromones. If we can, return true, otherwise return false.
     */
    public boolean smellPheromone()
    {
        Actor ph = getOneIntersectingObject(Pheromone.class);
        return (ph != null);
    }

    /**
     * If we can smell some pheromone, walk towards it. If not, do nothing.
     */
    Pheromone nextPh;
    public void walkTowardsPheromone()
    {
        /*Actor ph = getOneIntersectingObject(Pheromone.class);
        if (ph != null) {
            headTowards(ph);
            walk();
            if (ph.getX() == getX() && ph.getY() == getY()) {
                foundLastPheromone = PH_TIME;
            }
        }*/
        Pheromone ph = (Pheromone)getOneIntersectingObject(Pheromone.class);
        if(nextPh!=null){
            ph=nextPh;
        }
        if (ph != null) {
            try{
                headTowards(ph);
                walk();
                if (ph.getX() == getX() && ph.getY() == getY()) {
                    for(int i=0;i<getObjectsInRange(ph.MAX_INTENSITY/3,Pheromone.class).size();i++){
                        if(getObjectsInRange(ph.MAX_INTENSITY/3,Pheromone.class).get(i).intensity<ph.intensity){
                            nextPh=getObjectsInRange(ph.MAX_INTENSITY/3,Pheromone.class).get(i);
                            break;
                        }
                    }
                }
            }catch(Exception e){
                nextPh=null;
            }
        }
    }
}