import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
import org.apache.http.io.SessionOutputBuffer;

import java.util.ArrayList;

//renamed
public class Ant extends Creature
{
    private boolean isCarryingFood = false;

    private final int viewingRadius = 50;
    
    private int level = 1;
    
    private final int MAX_LEVEL = 10;
    
    private int profession;

    public Ant(AntHill home)
    {
        updateCof();
        profession = Greenfoot.getRandomNumber(50) < 10 ? 2 : Greenfoot.getRandomNumber(50) < 10 ? 3 : 1;
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
        if(profession != 2){
            setHp(3);
        }
        else{
            setHp(6);
        }
        
        setFood(3);
    }

    public void act()
    {
        razn=0;
        dTimer.calculate();
        updateVariables();
        searchForEnemies();
        if(!participateInBattle){
            doTrophallaxis();
        }
        if(!isUnderGround()){
            if(!participateInBattle) {
                if (profession == 1) {
                    if (isCarryingFood) {
                        walkTowardsHome();
                        handlePheromoneDrop();
                    }
                    else if(isCarryingBigFood){
                        walkTowardsHome();

                        Food food = (Food)getOneIntersectingObject(Food.class);

                        if(food != null) {
                            handlePheromoneDrop(food.getCrumbs() * 15);
                        }
                    }
                    else {
                        searchForFood();
                    }
                } else if (profession == 2) {
                    randomWalk();
                }
                else if(profession == 3){
                    walkTowardsHome();
                }
            }
        }
        else{
            if(!participateInBattle && profession != 2) {
                if (profession != 3) {
                    inHome();
                } else {
                    nurse();
                }
            }
            else if(profession==2){
                /*if(canSeeEnemy()){
                    myTarget=enemy;
                    attack();
                    if(intersects(enemy)) {
                        turnTowards(enemy);
                    }
                }
                else */
                if(isTouching(AntHill.class)){
                    boolean add = true;
                    for (AntHill checkedHill : checkedHills) {
                        if (getOneIntersectingObject(AntHill.class) == checkedHill) {
                            add = false;
                            break;
                        }
                    }
                    if(add){
                        checkedHills.add((AntHill)getOneIntersectingObject(AntHill.class));
                    }
                }

                if(!participateInBattle){
                    inHome();
                }
            }
        }
        if(atHome()){
            comeHome();
            if(isCarryingGround) {
                updateHome();
            }
        }
        
        eatFood();
        
        updateAnimation();

        if(!participateInBattle)
            checkFood();

        if(haveTakenObject() && getTakenObject().getWorld() != null && !intersects(getTakenObject())
        || haveTakenObject() && getTakenObject().getWorld() == null){
            putObject();
        }

        regenerateHp();

        die();
    }

    public void updateVariables(){
        if(haveTakenObject()){
            isCarryingFood = getTakenObject().getClass().equals(TakenFood.class);

            isCarryingLarva = getTakenObject().getClass().equals(Egg.class);

            isCarryingGround = getTakenObject().getClass().equals(TakenGround.class);

            isCarryingBigFood = getTakenObject().getClass().equals(Food.class);
        }
        else{
            isCarryingBigFood = false;
            isCarryingLarva = false;
            isCarryingGround = false;
            isCarryingFood = false;
        }
    }

    private void updateHome(){
        if(!isUnderGround()){
            if(haveTakenObject()) {
                getHomeHill().countEndurance();
                put();
                getWorld().removeObject(takenGround);
            }
            isCarryingGround = false;
        }
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
            randomWalk();
        }
    }
    
    private final SimpleTimer dTimer=new SimpleTimer();
    private final int dSteps = 50;

    boolean participateInBattle;
    private void searchForEnemies(){
        participateInBattle = false;
        if(canSeeEnemy()){
            participateInBattle = true;
            if(!intersects(enemy)){
                headTowards(enemy);
                purposefulWalk();
            }
            else{
                turnTowards(enemy);
            }
            
            attack();
        }
        else if(smellAttackPheromone()){
            walkTowardsAttackPheromone();
        }
        else if(foodNotFully() && canSeeFood() && profession==2){
            walkTowardsFood();
        }
        else if(canSeeEnemyHome() && profession==2){
            headTowards(enemyHome);
            purposefulWalk();
        }

        if(participateInBattle) {
            putObject();
        }
    }

    public void putObject(){
        if (haveTakenObject()) {
            if (isCarryingFood && isUnderGround()) {
                dropFood();
            } else if (isCarryingFood) {
                dropFood1();
            } else if (isCarryingLarva){
                isCarryingLarva = false;
                put();
            } else if (isCarryingBigFood) {
                isCarryingBigFood = false;
                put();
            } else if(isCarryingGround){
                isCarryingGround = false;
                put();
            }
        }
    }
    
    ArrayList<AntHill> checkedHills = new ArrayList<>();
    AntHill enemyHome;
    
    boolean checked = false;

    private boolean canSeeEnemyHome(){
        if(getObjectsInRange(viewingRadius, AntHill.class).size() > 0){
            enemyHome=getObjectsInRange(viewingRadius, AntHill.class).get(0);
            checked = false;
            for(AntHill ah : checkedHills){
                if(enemyHome == ah){
                    checked = true;
                    break;
                }
            }
            return enemyHome.getTeam() != getHomeHill().getTeam() && enemyHome.getAntNumber() > 0 && !checked;
        }
        else{
            return false;
        }
    }
    
    private void attack(){
        if(intersects(enemy) && enemy.isUnderGround() == isUnderGround() && dTimer.getTime()>=dSteps){
            if(profession==2){
                if(level<MAX_LEVEL) {
                    level++;
                    setHp1(6 + (level / 5));
                }

                enemy.damage(1 + (level / 5));
                //enemy.setImpulse(1+(level/5), getRotation());
            }
            else{
                enemy.damage(1);
                //enemy.setImpulse(1, getRotation());
            }
            dropAttackPheromone();

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
            ap=new AttackPheromone((startAPheromoneValue + profession == 2 ? level * 20 : 0), 250);
            getWorld().addObject(ap, getX(), getY());
        }
    }
    
    private Live crInRadius;
    private Live enemy;
    private boolean canSeeEnemy(){
        enemy=null;
        for(int i = 0; i<getObjectsInRange(viewingRadius, Live.class).size(); i++){
            crInRadius=getObjectsInRange(viewingRadius, Live.class).get(i);
            if(crInRadius.getHomeHill().getTeam()!=getHomeHill().getTeam() && canInteract(crInRadius)){
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
            if (getHomeHill() != null && myTarget == getHomeHill()) {
                return (Math.abs(getX() - getHomeHill().getX()) < 2) && (Math.abs(getY() - getHomeHill().getY()) < 2);
            }
            else {
                return false;
            }
        }

    }

    public void nurse(){
        myTarget = null;
        for (Egg egg : getObjectsInRange(viewingRadius, Egg.class)) {
            if (egg.foodNotFully() && getFood() > 1 + egg.needFood()) {
                myTarget = egg;
                break;
            }
        }
        if(foodNotFully() && getObjectsInRange(viewingRadius,TakenFood.class).size() > 0){
            myTarget = getObjectsInRange(viewingRadius,TakenFood.class).get(0);
        }

        if(isCarryingGround){
            myTarget = getHomeHill();
        }

        if(touchingBlock!=null && touchingBlock.canDig(this) && canDig && !isCarryingGround){
            myTarget = touchingBlock;
            if(haveTakenObject()) {
                putObject();
            }
            else{
                targetBlock = touchingBlock;
                takenGround = new TakenGround();
                getWorld().addObject(takenGround, getX(), getY());
                take(takenGround);
                isCarryingGround = true;
            }
        }
        else if(!haveTakenObject() && isTouching(TakenGround.class)){
            TakenGround ground = (TakenGround) getOneIntersectingObject(TakenGround.class);
            take(ground);
            isCarryingGround = true;
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

        feedLarva();

        transportLarva();

        transportFood();

        eat1();

        if(getHomeHill().getFood() == 0 && !isCarryingLarva){
            profession = 1;
            getHomeHill().reduceNurse();
        }
    }

    public void doTrophallaxis(){
        for(Creature ant : getObjectsInRange(viewingRadius, Creature.class)){
            if(ant.getHomeHill().getTeam() == getHomeHill().getTeam() && ant.getFood() - 1 > getFood() && ant.getFoodPersent() > getFoodPersent() && canInteract(ant)){
                if(!intersects(ant)) {
                    headTowards(ant);
                    purposefulWalk();
                }
                break;
            }
        }

        Creature ant = (Creature) getOneIntersectingObject(Creature.class);
        if(ant != null && ant.getHomeHill().getTeam() == getHomeHill().getTeam() && ant.getFood() - 1 > getFood() && ant.getFoodPersent() > getFoodPersent() && canInteract(ant)){
            ant.eatFood(1);
            eat();

            turnTowards(ant.getX() ,ant.getY());
            ant.turnTowards(getX() ,getY());
            ant.stop();
            stop();
        }
    }

    private void feedLarva(){
        Egg egg = (Egg)getOneIntersectingObject(Egg.class);
        if(egg != null && intersects(egg) && egg.foodNotFully() && food > 1){
            egg.countFood();
            food--;
        }
    }

    boolean isCarryingLarva;
    private void transportLarva(){
        if(calculateDistToHome() < viewingRadius && myTarget != touchingBlock && !isCarryingGround) {
            Egg egg = (Egg) getOneIntersectingObject(Egg.class);
            if (egg != null) {
                take(egg);
                isCarryingLarva = true;
            }
        }
        else if(haveTakenObject() && calculateDistToHome() > viewingRadius + 10 && isCarryingLarva){
            put();
            isCarryingLarva = false;
        }
    }

    public void transportFood(){
        if(calculateDistToHome() < viewingRadius && profession == 3 && myTarget != touchingBlock && !isCarryingGround) {
            TakenFood takenfood = (TakenFood) getOneIntersectingObject(TakenFood.class);
            if (takenfood != null) {
                take(takenfood);
                tf = takenfood;
                tf.put();
                isCarryingFood = true;
            }
        }
        if(isCarryingFood){
            if(tf!=null){
                tf.getImage().setTransparency(getImage().getTransparency());
                tf.setUnderGround(isUnderGround());
                checkHome();
            }
        }
    }
    
    private Actor myTarget;

    private boolean isCarryingGround = false;
    private TakenGround takenGround;

    Block targetBlock;
    private void inHome(){
        if(myTarget==null){
            myTarget=getHomeHill();
        }
        if(foodNotFully() && getObjectsInRange(viewingRadius,TakenFood.class).size()>0){
            myTarget=getObjectsInRange(viewingRadius,TakenFood.class).get(0);
        }
        else if(foodNotFully() && getHomeHill().getFood() > 0){
            myTarget = null;
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
            //}
        }
        if(isCarryingGround){
            myTarget = getHomeHill();
        }

        if(touchingBlock!=null && touchingBlock.canDig(this) && canDig && !isCarryingGround){
            myTarget = touchingBlock;
            if(haveTakenObject()) {
                putObject();
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

        if(myTarget!=null){
            headTowards(myTarget);
            purposefulWalk();
        }
        else{
            randomWalk();
        }
        
        transportFood();

        if(profession == 1 && getHomeHill().getFood() / 3 > getObjectsInRange(viewingRadius, Egg.class).size() && !isCarryingFood && getHomeHill().getNurseNumber() < 5 && getHomeHill().haveQueen()){
            profession = 3;
            getHomeHill().newNurse();
        }
        
        eat1();
    }
    
    public void eat1(){
        TakenFood tf=(TakenFood)getOneIntersectingObject(TakenFood.class);
        if(tf!=null && foodNotFully() && tf.getUnderGround() == isUnderGround()){
            eat(tf.energyCost);
            tf.eat();
        }
    }

    boolean isCarryingBigFood;
    public void checkFood()
    {
        Food food = (Food) getOneIntersectingObject(Food.class);
        if(food!=null && !isUnderGround()){
            if(foodNotFully()){
                eatFood(food);
            }
            else if (profession==1 && food.energyCost == 1 && !haveTakenObject()
            || profession==1 && food.energyCost == 2 && !haveTakenObject() && calculateDistToHome() < viewingRadius) {
                takeFood(food);
            }
            else if(profession==1 && !haveTakenObject() && calculateDistToHome() >= viewingRadius){
                isCarryingBigFood = true;
                take(food);
            }
        }

        if(isCarryingBigFood && calculateDistToHome() < viewingRadius){
            isCarryingBigFood = false;
            put();
        }
        else if(isCarryingBigFood && !intersects(getTakenObject())){
            isCarryingBigFood = false;
            put();
        }
    }

    private TakenFood tf;
    private int pheromoneValue;
    private void takeFood(Food food)
    {
        isCarryingFood = true;
        food.takeSome();

        pheromoneValue = 30 + food.getCrumbs() * 5;
        tf=new TakenFood(getHomeHill(), food.getTypeOfFood(), food.getSecondTypeOfFood(), food.getImage());
        getWorld().addObject(tf,getX(),getY());
        take(tf);
    }

    private void eatFood(Food food){
        food.takeSome();
        eat(food.energyCost);
    }

    public void dropFood1(){
        getWorld().addObject(new Food(1, tf.typeOfFood, 1),getX(),getY());
        put();
        getWorld().removeObject(tf);
        tf = null;
        isCarryingFood = false;
        lastPh=null;
    }

    private void dropFood()
    {
        isCarryingFood = false;
        if(level<MAX_LEVEL){
            level++;
            setSpeed(3 + (level / 5));
        }
        lastPh=null;
        tf.drop();
        put();
        updateCof();
        tf = null;
    }

    private Pheromone lastPh;
    private int time;
    private void handlePheromoneDrop()
    {
        if (lastPh == null || lastPh.getWorld() == null || (int)Math.sqrt(Math.pow(lastPh.getX() - getX(), 2) + Math.pow(lastPh.getY() - getY(), 2)) > pheromoneValue / 10) {
            if(lastPh == null){
                time = (int)(calculateDistToHome() * 1.5);
            }
            lastPh = new Pheromone(pheromoneValue, time);
            getWorld().addObject(lastPh, getX(), getY());
        }
    }

    private void handlePheromoneDrop(int value)
    {
        if(!isTouching(Pheromone.class)) {
            if (lastPh == null || !intersects(lastPh)) {
                lastPh = new Pheromone(value, value);
                getWorld().addObject(lastPh, getX(), getY());
            }
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
        Pheromone ph = (Pheromone)getOneIntersectingObject(Pheromone.class);
        if (ph != null) {
            if(n != 0) {
                headTowards(phX / n, phY / n);
                purposefulWalk();
            }

            if (n != 0 || ph.getX() == getX() && ph.getY() == getY()) {
                phX = 0;
                phY = 0;
                n = 0;

                for(Pheromone pheromone : getIntersectingObjects(Pheromone.class)){
                    //if(!intersects(pheromone)) {
                        int rot = getRotation();
                        turnTowards(pheromone.getX(), pheromone.getY());
                        int rotToPh = getRotation();
                        setRotation(rot);
                        if (Math.abs(rot - rotToPh) <= 90 || Math.abs(rot - rotToPh) > 270) {
                            phX += pheromone.getX();
                            phY += pheromone.getY();
                            n++;
                        }
                    //}
                }

                if(n == 0){
                    setRotation(Greenfoot.getRandomNumber(360));
                }
            }
            else{
                headTowards(ph);
                purposefulWalk();
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

        if(profession!=2){
            setImage("ant"+shot+".png");
            getImage().setColor(AntWorld.teamColors.get(getHomeHill().getTeam() - 1));
            /*if(getHomeHill().getTeam()==1){
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
            }*/
            getImage().fillRect(1,4,1,2);
        }
        else{
            setImage("soldier"+shot+".png");
            getImage().setColor(AntWorld.teamColors.get(getHomeHill().getTeam() - 1));
            getImage().fillRect(2,8,2,2);
        }
        
        if(isUnderGround()){
            getImage().setTransparency(100);
        }
    }
    
    private void die(){
        if(food<=0 || hp<=0){
            if(tf!=null && tf.getWorld()!=null){
                putObject();
            }

            if(profession==1){
                getHomeHill().antDead();
            }
            else if(profession == 2){
                getHomeHill().soldierDead();
            }
            else{
                getHomeHill().nurseDead();
            }

            if(food > 0){
                getWorld().addObject(new Food(food, 2, 1), getX(), getY());
            }

            getWorld().removeObject(this);
        }
    }
}