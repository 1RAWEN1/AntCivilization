import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
import java.util.ArrayList;

//renamed
public class Ant extends Creature
{
    private boolean isCarryingFood = false;

    private final int viewingRadius = 50;
    
    private int level = 1;
    
    private final int MAX_LEVEL = 10;
    
    private int profession = 0;

    public Ant(AntHill home)
    {
        updateCof();
        profession = Greenfoot.getRandomNumber(2) + 1;
        setHomeHill(home);
        setVariables();
    }
    
    public Ant(AntHill home, int profession)
    {
        updateCof();
        this.profession = profession;
        setHomeHill(home);
        setVariables();
    }
    
    public int getProfession(){
        return profession;
    }
    
    private void setVariables(){
        if(profession == 1){
            setHp(3);
        }
        else{
            setHp(6);
        }
        
        setFood(3, 1);
        
        needToCheck = new ArrayList<>(AntWorld.arrayOfHouses);
    }

    public void act()
    {
        razn=0;
        dTimer.calculate();
        if(!isUnderGround()){
            if(profession == 1){
                if (isCarryingFood) {
                    walkTowardsHome();
                    handlePheromoneDrop();
                    carry();
                }
                else {
                    searchForFood();
                }
            }
            else if(profession == 2){
                searchForEnemies();
            }
        }
        else{
            inHome();
        }
        if(atHome()){
            comeHome();
        }
        
        eatFood();
        
        updateAnimation();
        
        checkFood();
        
        die();
    }
    
    private void searchForFood()
    {
        if(canSeeFood()){
            walkTowardsFood();
        }
        else if (smellPheromone()) {
            walkTowardsPheromone();
        }
        else{
            searchForEnemies();
        }
    }
    
    private final SimpleTimer dTimer=new SimpleTimer();
    private final int dSteps = 50;
    
    private void searchForEnemies(){
        if(canSeeEnemy()){
            turnTowards(enemy);
            if(!intersects(enemy)){
                headTowards(enemy);
                purposefulWalk();
            }
            
            attack();
        }
        else if(smellAttackPheromone() && profession==2){
            walkTowardsAttackPheromone();
        }
        else if(foodNotFully() && canSeeFood() && profession==2){
            walkTowardsFood();
        }
        else if(canSeeEnemyHome() && profession==2){
            headTowards(enemyHome);
            purposefulWalk();
        }
        else{
            randomWalk();
        }
    }
    
    ArrayList<AntHill> needToCheck = new ArrayList<>();
    AntHill enemyHome;
    
    boolean checked = false;

    private boolean canSeeEnemyHome(){
        if(getObjectsInRange(viewingRadius, AntHill.class).size() > 0){
            enemyHome=getObjectsInRange(viewingRadius, AntHill.class).get(0);
            checked = true;
            for(AntHill ah: needToCheck){
                if(enemyHome == ah){
                    checked = false;
                    break;
                }
            }
            return enemyHome != getHomeHill() && enemyHome.getAntNumber() > 0 && !checked;
        }
        else{
            return false;
        }
    }
    
    private void attack(){
        if(intersects(enemy) && dTimer.getTime()>=dSteps){
            if(profession==2){
                enemy.damage(1+(level/5));
                //enemy.setImpulse(1+(level/5), getRotation());
            }
            else{
                enemy.damage(1);
                //enemy.setImpulse(1, getRotation());
            }
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
        purposefulWalk();
    }
    
    private AttackPheromone ap;
    
    private final int startAPheromoneValue = 300;
    private void dropAttackPheromone(){
        if(ap!=null && ap.getWorld()==null){
            ap=null;
        }
        if(ap==null || !intersects(ap)){
            ap=new AttackPheromone((startAPheromoneValue + level*24));
            getWorld().addObject(ap, getX(), getY());
        }
    }
    
    private Live crInRadius;
    private Live enemy;
    private boolean canSeeEnemy(){
        enemy=null;
        for(int i = 0; i<getObjectsInRange(viewingRadius, Live.class).size(); i++){
            crInRadius=getObjectsInRange(viewingRadius, Live.class).get(i);
            if(crInRadius.getHomeHill().getTeam()!=getHomeHill().getTeam() && crInRadius.isUnderGround()==isUnderGround()){
                enemy=crInRadius;
            }
        }
        return enemy!=null;
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

    private void checkHome()
    {
        if(touchingBlock!=null && touchingBlock.getWorld()!=null){
            turnTowards(touchingBlock);
        }
        TakenFood touchingFood = (TakenFood)getOneIntersectingObject(TakenFood.class);
        if (touchingBlock!=null && !touchingBlock.canDig(this)
        && Math.sqrt(Math.pow(getX()-getHomeHill().getX(),2)+Math.pow(getY()-getHomeHill().getY(),2)) > viewingRadius) {
            dropFood();
        }
        else if(touchingFood != null && !touchingFood.taken
        && Math.sqrt(Math.pow(getX() - touchingFood.getX(), 2) + Math.pow(getY() - touchingFood.getY(), 2)) < 4){
            dropFood();
        }
        else if(tf.getWorld()==null){
            dropFood();
        }
    }

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
    
    private Actor myTarget;

    private void inHome(){
        if(myTarget==null){
            myTarget=getHomeHill();
        }
        if(foodNotFully() && getHomeHill().getFood()!=0 && getObjectsInRange(viewingRadius,TakenFood.class).size()>0){
            myTarget=getObjectsInRange(viewingRadius,TakenFood.class).get(0);
        }
        else{
            myTarget=getHomeHill();
        }
        if(isCarryingFood){
            /*if(getObjectsInRange(radius,TakenFood.class).size()>0){
                myTarget=getObjectsInRange(radius,TakenFood.class).get(0);
            }
            else{*/
                myTarget=null;
                randomWalk();
            //}
        }
        if(profession==2){
            if(canSeeEnemy()){
                myTarget=enemy;
                attack();
                turnTowards(enemy);
            }
            else if(isTouching(AntHill.class)){
                for(int i=0;i<needToCheck.size();i++){
                    if(getOneIntersectingObject(AntHill.class)==needToCheck.get(i)){
                        needToCheck.remove(i);
                        break;
                    }
                }
            }
        }
        
        if(myTarget!=null && myTarget.getWorld()==null){
            myTarget=null;
        }
        
        if(touchingBlock!=null && touchingBlock.getWorld()!=null && touchingBlock.canDig(this)){
            myTarget=touchingBlock;
        }
        
        if(myTarget!=null){
            headTowards(myTarget);
            purposefulWalk();
        }
        
        if(isCarryingFood){
            if(tf!=null){
                carry();
                tf.getImage().setTransparency(getImage().getTransparency());
            }
            checkHome();
        }
        
        eat1();
    }
    
    public void eat1(){
        TakenFood tf=(TakenFood)getOneIntersectingObject(TakenFood.class);
        if(tf!=null && foodNotFully()){
            tf.eat();
            eat();
        }
    }

    public void checkFood()
    {
        Food food = (Food) getOneIntersectingObject(Food.class);
        if(food!=null){
            if(foodNotFully()){
                eatFood(food);
            }
            if (profession==1 && food.getWorld()!=null && !isCarryingFood) {
                takeFood(food);
    
            }
        }
    }

    private TakenFood tf;
    private final int startPheromoneValue=60;
    private void takeFood(Food food)
    {
        isCarryingFood = true;
        food.takeSome();
        
        tf=new TakenFood(getHomeHill());
        getWorld().addObject(tf,getX(),getY());
        take(tf);
    }
    
    private void eatFood(Food food){
        food.takeSome();
        eat();
    }

    private void dropFood()
    {
        isCarryingFood = false;
        if(level<MAX_LEVEL){
            level++;
        }
        lastPh=null;
        tf.drop();
        put();
        updateCof();
    }

    private Pheromone lastPh;
    private void handlePheromoneDrop()
    {
        if (lastPh==null || !intersects(lastPh)) {
            lastPh = new Pheromone(startPheromoneValue + level*12);
            getWorld().addObject(lastPh, getX(), getY());
        }
    }

    public boolean smellPheromone()
    {
        Actor ph = getOneIntersectingObject(Pheromone.class);
        return (ph != null);
    }

    private Pheromone nextPh;
    
    private Pheromone smellPh;

    public void walkTowardsPheromone()
    {
        Pheromone ph = (Pheromone)getOneIntersectingObject(Pheromone.class);
        if(nextPh!=null){
            ph=nextPh;
        }
        if (ph != null) {
            try{
                headTowards(ph);
                purposefulWalk();
                if (ph.getX() == getX() && ph.getY() == getY()) {
                    for(int i=0;i<getObjectsInRange(ph.getMaxIntensity()/3,Pheromone.class).size();i++){
                        smellPh=getObjectsInRange(ph.getMaxIntensity()/3,Pheromone.class).get(i);
                        if(!intersects(smellPh) && smellPh.getIntensity() < ph.getIntensity()){
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
        if(moved()){
            animation++;
        }

        if(animation>=step){
            animation=0;
            shot++;
        }
        if(shot>MAX_SHOT){
            shot=1;
        }

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
        else{
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
        }
        
        if(isUnderGround()){
            getImage().setTransparency(100);
        }
    }
    
    private void die(){
        if(food<=0 || hp<=0){
            if(tf!=null && tf.getWorld()!=null){
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