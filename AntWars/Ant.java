import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
import java.util.ArrayList;

public class Ant extends Creature
{
    /** Indicate whether we have any food with us. */
    private boolean carryingFood = false;

    /** How much pheromone do we have right now. */
    private int pheromoneLevel = 0;

    /** How well do we remember the last pheromone - larger number: more recent */
    private int foundLastPheromone = 0;
    
    private final int radius = 50;
    
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
        setVariables();
    }
    
    public Ant(AntHill home, int profession)
    {
        updateCof();
        this.profession=profession;
        setHomeHill(home);
        setVariables();
    }
    
    public int getProfession(){
        return profession;
    }
    
    private void setVariables(){
        if(profession==1){
            setHp(3);
        }
        else{
            setHp(6);
        }
        
        setFood(3, 1);
        
        needToCheck=(ArrayList<AntHill>)AntWorld.homeArray.clone();
    }

    /**
     * Do what an ant's gotta do.
     */
    public void act()
    {
        dTimer.calculate();
        if(!isUnderGround()){
            if(profession==1){
                if (carryingFood) {
                    walkTowardsHome();
                    handlePheromoneDrop();
                    carry();
                }
                else {
                    searchForFood();
                }
            }
            else if(profession==2){
                searchEnemies();
            }
        }
        else{
            inHome();
        }
        if(atHome()){
            moveInHome();
        }
        
        eatFood();
        
        updateAnimation();
        
        die();
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
    
    private SimpleTimer dTimer=new SimpleTimer();
    private final int dSteps = 50;
    
    private void searchEnemies(){
        if(seeEnemy()){
            turnTowards(enemy);
            if(!intersects(enemy)){
                headTowards(enemy);
                walk();
            }
            
            attack();
        }
        else if(smellAttackPheromone()){
            walkTowardsAttackPheromone();
        }
        else if(foodNotFully() && canSeeFood()){
            walkTowardsFood();
        }
        else if(seeEnemyHome()){
            headTowards(enemyHome);
            walk();
        }
        else{
            randomWalk();
        }
        
        checkFood();
    }
    
    ArrayList<AntHill> needToCheck=new ArrayList<AntHill>();
    AntHill enemyHome;
    
    boolean checked = false;
    private boolean seeEnemyHome(){
        if(getObjectsInRange(radius, AntHill.class).size()>0){
            enemyHome=getObjectsInRange(radius, AntHill.class).get(0);
            checked=true;
            for(int i=0;i<needToCheck.size();i++){
                if(enemyHome==needToCheck.get(i)){
                    checked=false;
                    break;
                }
            }
            return enemyHome!=getHomeHill() && enemyHome.getAntNumber()>0 && !checked;
        }
        else{
            return false;
        }
    }
    
    private void attack(){
        if(intersects(enemy) && dTimer.getTime()>=dSteps){
            enemy.damage(1+(level/5));
            dropAttackPheromone();
            if(level<MAX_LEVEL)
                level++;
            dTimer.update();
        }
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
    
    private AttackPheromone ap;
    
    private int startAPheromoneValue = 300;
    private void dropAttackPheromone(){
        if(ap!=null && ap.getWorld()==null){
            ap=null;
        }
        if(ap!=null && !intersects(ap) || ap==null){
            ap=new AttackPheromone((startAPheromoneValue + level*24));
            getWorld().addObject(ap, getX(), getY());
        }
    }
    
    private Live crInRadius;
    private Live enemy;
    private boolean seeEnemy(){
        enemy=null;
        for(int i=0;i<getObjectsInRange(radius, Live.class).size();i++){
            crInRadius=getObjectsInRange(radius, Live.class).get(i);
            if(crInRadius.getHomeHill().getTeam()!=getHomeHill().getTeam() && crInRadius.isUnderGround()==isUnderGround()){
                enemy=crInRadius;
            }
        }
        return enemy!=null;
    }
    
    private boolean canSeeFood(){
        return getObjectsInRange(radius, Food.class).size()>0 && !isUnderGround();
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
        Warehouse wr=(Warehouse)getOneIntersectingObject(Warehouse.class);
        if (wr!=null) {
            wr.addFood();
            dropFood();
        }
    }

    /**
     * Are we home?
     */
    private boolean atHome()
    {
        if(profession==2){
            Actor home=getOneIntersectingObject(AntHill.class);
            if (home != null) {
                return (Math.abs(getX() - home.getX()) < 2) && (Math.abs(getY() - home.getY()) < 2);
            }
            else {
                return false;
            }
        }
        else{
            if (getHomeHill() != null) {
                return (Math.abs(getX() - getHomeHill().getX()) < 2) && (Math.abs(getY() - getHomeHill().getY()) < 2);
            }
            else {
                return false;
            }
        }

    }
    
    private Actor target;
    private void inHome(){
        if(target==null){
            target=getHomeHill();
        }
        if(foodNotFully() && getHomeHill().getFood()!=0){
            for(int i=0;i<getObjectsInRange(radius,Warehouse.class).size();i++){
                if(getObjectsInRange(radius,Warehouse.class).get(i).notEmpty()){
                    target=getObjectsInRange(radius,Warehouse.class).get(i);
                    break;
                }
            }
        }
        else{
            target=getHomeHill();
        }
        if(carryingFood){
            for(int i=0;i<getObjectsInRange(radius, Warehouse.class).size();i++){
                if(!getObjectsInRange(radius, Warehouse.class).get(i).isFully()){
                    target=getObjectsInRange(radius, Warehouse.class).get(i);
                    break;
                }
            }
        }
        if(profession==2){
            if(seeEnemy()){
                target=enemy;
                attack();
                turnTowards(enemy);
            }
            else if(isTouching(AntHill.class)){
                for(int i=0;i<needToCheck.size();i++){
                    if((AntHill)getOneIntersectingObject(AntHill.class)==needToCheck.get(i)){
                        needToCheck.remove(i);
                        break;
                    }
                }
            }
        }
        
        if(target!=null && target.getWorld()==null){
            target=null;
        }
        
        if(target!=null && target.getWorld()!=null){
            headTowards(target);
            walk();
        }
        /*if((getX()-getHomeHill().getX())%20>0 && (getX()-getHomeHill().getX())%20<SPEED
        && (getY()-getHomeHill().getY())%20>0 && (getY()-getHomeHill().getY())%20<SPEED){
            setLocation(getX()+(getX()-getHomeHill().getX())%20, getY());
        }*/
        
        if(carryingFood){
            if(tf!=null){
                carry();
                tf.getImage().setTransparency(getImage().getTransparency());
            }
            checkHome();
        }
        
        eat1();
    }
    
    public void eat1(){
        Warehouse wh=(Warehouse)getOneIntersectingObject(Warehouse.class);
        if(wh!=null && wh.notEmpty() && foodNotFully()){
            wh.takeSome();
            eat();
        }
    }

    /**
     * Is there any food here where we are? If so, take some!.
     */
    public void checkFood()
    {
        Food food = (Food) getOneIntersectingObject(Food.class);
        if(food!=null){
            if(foodNotFully()){
                eatFood(food);
            }
            if (profession==1 & food.getWorld()!=null) {
                takeFood(food);
    
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
        
        tf=new TakenFood();
        getWorld().addObject(tf,getX(),getY());
        take(tf);
    }
    
    private void eatFood(Food food){
        food.takeSome();
        eat();
    }

    /**
     * Drop our food in the ant hill.
     */
    private void dropFood()
    {
        carryingFood = false;
        if(level<MAX_LEVEL){
            level++;
        }
        lastPh=null;
        remove();
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
    
    private Pheromone smellPh;
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
                        smellPh=getObjectsInRange(ph.getMaxIntensity()/3,Pheromone.class).get(i);
                        if(!intersects(smellPh) && smellPh.getIntensity()<ph.getIntensity()){
                            nextPh=smellPh;
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
        //for greenfoot web site
        /*if(profession==1){
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
        }
        else{
            setImage("bant"+shot+".png");
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
            getImage().fillRect(2,8,2,4);
        }*/
        if(profession==1){
            setImage("ant"+shot+".png");
            if(getHomeHill().getTeam()==1){
                getImage().setColor(Color.BLUE);
            }
            else if(getHomeHill().getTeam()==2){
                getImage().setColor(Color.CYAN);
            }
            else if(getHomeHill().getTeam()==3){
                getImage().setColor(Color.YELLOW);
            }
            else if(getHomeHill().getTeam()==4){
                getImage().setColor(Color.GREEN);
            }
            getImage().fillRect(1,4,1,2);
        }
        if(profession==2){
            setImage("soldier"+shot+".png");
            if(getHomeHill().getTeam()==1){
                getImage().setColor(Color.BLUE);
            }
            else if(getHomeHill().getTeam()==2){
                getImage().setColor(Color.CYAN);
            }
            else if(getHomeHill().getTeam()==3){
                getImage().setColor(Color.YELLOW);
            }
            else if(getHomeHill().getTeam()==4){
                getImage().setColor(Color.GREEN);
            }
            getImage().fillRect(2,8,2,2);
            //getImage().scale((int)(getImage().getWidth()*2),(int)(getImage().getHeight()*2));
        }
        
        if(isUnderGround()){
            getImage().setTransparency(100);
        }
    }
    
    private void die(){
        if(food<=0 || hp<=0){
            if(tf!=null){
                getWorld().addObject(new Food(1),tf.getX(),tf.getY());
                getWorld().removeObject(tf);
            }
            if(profession==1){
                getHomeHill().antDead();
            }
            else{
                getHomeHill().soldierDead();
            }
            getWorld().removeObject(this);
        }
    }
}