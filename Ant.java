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
    
    private final int MAX_LEVEL = 10;
    
    private int profession = 0;

    /**
     * Create an ant with a given home hill. The initial speed is zero (not moving).
     */
    public Ant(AntHill home)
    {
        updateCof();
        profession=Greenfoot.getRandomNumber(2)+1;
        setHomeHill(home);
        setHP();
    }
    
    public Ant(AntHill home, int profession)
    {
        updateCof();
        this.profession=profession;
        setHomeHill(home);
        setHP();
    }
    
    private void setHP(){
        if(profession==1){
            MAX_HP=3;
            hp=MAX_HP;
        }
        else{
            MAX_HP=6;
            hp=MAX_HP;
        }
    }

    /**
     * Do what an ant's gotta do.
     */
    private int food = 1;
    private final int MAX_FOOD=3;
    private SimpleTimer timer = new SimpleTimer();
    public void act()
    {
        timer.calculate();
        dTimer.calculate();
        if(profession==1){
            if (carryingFood) {
                walkTowardsHome();
                handlePheromoneDrop();
                checkHome();
                if(tf!=null){
                    tf.setRotation(getRotation());
                    tf.setLocation(getX()+(int)((getImage().getWidth()/2)*Math.cos(Math.toRadians(getRotation()))), getY()+(int)((getImage().getWidth()/2)*Math.sin(Math.toRadians(getRotation()))));
                }
            }
            else {
                searchForFood();
            }
        }
        else if(profession==2){
            searchEnemies();
        }
        updateAnimation();
        
        eat();
        
        die();
    }
    
    private final int timerStep = 1500;
    private void eat(){
        if(timer.getTime()/timerStep>=1){
            timer.update();
            food--;
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
    
    private int MAX_HP=0;
    private int hp=0;
    
    private SimpleTimer dTimer=new SimpleTimer();
    private final int dSteps = 50;
    
    private void searchEnemies(){
        if(seeEnemy()){
            if(!intersects(enemy)){
                headTowards(enemy);
                walk();
            }
            else if(intersects(enemy) && dTimer.getTime()/dSteps>=1){
                enemy.hp-=1+(level/5);
                dropAttackPheromone();
                if(level<MAX_LEVEL)
                    level++;
                dTimer.update();
            }
        }
        else if(smellAttackPheromone()){
            walkTowardsAttackPheromone();
        }
        else if(food<MAX_FOOD && canSeeFood()){
            walkTowardsFood();
        }
        else{
            randomWalk();
        }
        
        checkFood();
    }
    
    public boolean smellAttackPheromone()
    {
        Actor ph = getOneIntersectingObject(AttackPheromone.class);
        return (ph != null);
    }
    
    private void walkTowardsAttackPheromone(){
        Actor ph = getOneIntersectingObject(AttackPheromone.class);
        headTowards(ph);
        walk();
    }
    
    AttackPheromone ap;
    private void dropAttackPheromone(){
        if(ap!=null && ap.getWorld()==null){
            ap=null;
        }
        if(ap!=null && !intersects(ap) || ap==null){
            ap=new AttackPheromone((startPheromoneValue + level*12)*2);
            getWorld().addObject(ap, getX(), getY());
        }
    }
    
    private Ant enemy;
    private boolean seeEnemy(){
        enemy=null;
        for(int i=0;i<getObjectsInRange(radius, Ant.class).size();i++){
            if(getObjectsInRange(radius, Ant.class).get(i).getHomeHill().getTeam()!=getHomeHill().getTeam()){
                enemy=getObjectsInRange(radius, Ant.class).get(i);
            }
        }
        return enemy!=null;
    }
    
    private boolean canSeeFood(){
        return getObjectsInRange(radius, Food.class).size()>0;
    }
    
    private void walkTowardsFood(){
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
        if(food!=null){
            if (profession==1) {
                takeFood(food);
    
            }
            else if(this.food<MAX_FOOD){
                eatFood(food);
            }
        }
    }

    /**
     * Take some food from a fool pile.
     */
    private TakenFood tf;
    private final int startPheromoneValue=60;
    private void takeFood(Food food)
    {
        carryingFood = true;
        food.takeSome();
        
        if(this.food<MAX_FOOD)
            this.food++;
        
        tf=new TakenFood();
        getWorld().addObject(tf,getX(),getY());
    }
    
    private void eatFood(Food food){
        food.takeSome();
        if(this.food<MAX_FOOD)
            this.food++;
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
        getWorld().removeObject(tf);
        tf=null;
        /*GreenfootImage im=new GreenfootImage(getImage().getWidth()+2,getImage().getHeight());
        im.drawImage(new GreenfootImage("takenFood.png"),0,(im.getHeight()/2)-1);
        im.drawImage(getImage(),2,0);
        setImage(im);*/
        updateCof();
    }

    /**
     * Check whether we can drop some pheromone yet. If we can, do it.
     */
    private Pheromone lastPh;
    
    private int phIndex;
    private void handlePheromoneDrop()
    {
        if (lastPh!=null && !intersects(lastPh) || lastPh==null) {
            lastPh = new Pheromone(startPheromoneValue + level*12);
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
    private Pheromone nextPh;
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
                    for(int i=0;i<getObjectsInRange(ph.getMaxIntensity()/3,Pheromone.class).size();i++){
                        if(getObjectsInRange(ph.getMaxIntensity()/3,Pheromone.class).get(i).getIntensity()<ph.getIntensity()){
                            nextPh=getObjectsInRange(ph.getMaxIntensity()/3,Pheromone.class).get(i);
                            break;
                        }
                    }
                }
            }catch(Exception e){
                nextPh=null;
            }
        }
    }
    
    private int animation;
    
    private final int step=2;
    
    private int shot=1;
    
    private final int MAX_SHOT=3;
    private void updateAnimation(){
        animation++;
        if(animation>=step){
            animation=0;
            shot++;
        }
        if(shot>MAX_SHOT){
            shot=1;
        }
        setImage("ant"+shot+".png");
        if(getHomeHill().getTeam()==1){
            getImage().setColor(Color.BLUE);
        }
        else if(getHomeHill().getTeam()==2){
            getImage().setColor(Color.RED);
        }
        else if(getHomeHill().getTeam()==3){
            getImage().setColor(Color.YELLOW);
        }
        else if(getHomeHill().getTeam()==4){
            getImage().setColor(Color.GREEN);
        }
        getImage().fillRect(1,4,1,2);
        if(profession==2){
            getImage().scale((int)(getImage().getWidth()*2),(int)(getImage().getHeight()*2));
        }
    }
    
    private void die(){
        if(food<=0 || hp<=0){
            if(tf!=null){
                getWorld().removeObject(tf);
            }
            getHomeHill().antDead();
            getWorld().removeObject(this);
        }
    }
}