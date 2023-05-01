import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
import io.github.classgraph.json.JSONUtils;

public class AntHill extends Actor
{
    private Label chanceLabel;
    
    private double chanceToWin;
    
    static int teams;
    private int teamNum;
    
    private int fullNumberOfAnts = 5;
    private int ants = 0;

    private int soldiers = 0;

    private int nurses = 0;
    
    private final int maxAnts = 20;

    private Counter foodCounter;
    
    private Counter antCounter;
    
    private int food;
    
    private final int MAX_FOOD=100;

    private int eggs;

    private int endurance = 0;

    private final int MAX_ENDURANCE = 2500;
    
    public AntHill()
    {
        AntWorld.arrayOfHouses.add(this);
    }

    public AntHill(int numberOfAnts)
    {
        teams++;
        teamNum=teams;
        AntWorld.arrayOfHouses.add(this);
    }

    boolean createWithoutQueen = false;
    public AntHill(int numberOfAnts, boolean createWithoutQueen)
    {
        this.createWithoutQueen = createWithoutQueen;
        if(createWithoutQueen) {
            queens = 0;
            fullNumberOfAnts = 0;
        }

        teams++;
        teamNum=teams;
        AntWorld.arrayOfHouses.add(this);
    }

    public AntHill(int numberOfAnts, boolean createWithoutQueen, int team)
    {
        this.createWithoutQueen = createWithoutQueen;
        if(createWithoutQueen) {
            queens = 0;
            fullNumberOfAnts = 0;
        }

        teamNum=team;
        teams = Math.max(teamNum, teams);
        AntWorld.arrayOfHouses.add(this);
    }
    
    public AntHill(int numberOfAnts, int team)
    {
        teamNum=team;
        teams = Math.max(teamNum, teams);
        AntWorld.arrayOfHouses.add(this);
    }

    public AntHill(int numberOfAnts, int team, int queens)
    {
        teamNum=team;
        teams = Math.max(teamNum, teams);
        this.queens = queens;
        AntWorld.arrayOfHouses.add(this);
    }
    
    public void setAntNumber(int ants){
        fullNumberOfAnts=ants;
    }
    
    private final int BLOCK_SIZE=20;

    int queens = 1;
    public void createUnderground(){
        // || Math.abs(4-x)+Math.abs(4-y)==1
        //        || 4>Math.abs(4-y) && Math.abs(4-y)>1 && Math.abs(4-x)<=1 || 4>Math.abs(4-x) && Math.abs(4-x)>1 && Math.abs(4-y)<=1
        
        //Math.abs(4-x)+Math.abs(4-y)>6 || 
        for(int x=0;x<9;x++){
            for(int y=0;y<9;y++){
                if(Math.abs(4-x)+Math.abs(4-y)>6 || 4-x==0 && 4-y==0){
                    
                }
                else{
                    getWorld().addObject(new Block(x, y),getX()-BLOCK_SIZE+(5-x)*BLOCK_SIZE,getY()-BLOCK_SIZE+(5-y)*BLOCK_SIZE);
                }
            }
        }
        
        //getWorld().addObject(new Warehouse(this), getX(), getY()+50);
        //getWorld().addObject(new Warehouse(this), getX()+50, getY());
        //getWorld().addObject(new Warehouse(this), getX()-50, getY());

        if(!createWithoutQueen) {
            for (int i = 0; i < fullNumberOfAnts; i++) {
                //getWorld().addObject(new Ant(this),getX()-20,getY()-55);
                createNewAnt(new Ant(this));
            }

            for (int i = 0; i < queens; i++) {
                getWorld().addObject(new QueenAnt(this), getX(), getY());
                fullNumberOfAnts++;
            }
        }
        
        countAnts();
    }
    
    public void createNewAnt(Ant ant){
        if(ant.getProfession()==2){
            newSoldier();
        } else if(ant.getProfession()==3){
            newNurse();
        }
        getWorld().addObject(ant ,getX(),getY());
    }
    
    public void newAnt(Ant ant){
        fullNumberOfAnts++;
        countAnts();
        if(ant.getProfession()==2){
            newSoldier();
        } else if(ant.getProfession()==3){
            newNurse();
        }
    }

    public void newAnt(){
        fullNumberOfAnts++;
        countAnts();
    }

    private int princesses;
    public void newPrincess(){
        fullNumberOfAnts++;
        princesses++;
        countAnts();
    }

    public void newQueen1(){
        princesses--;
        queens++;
    }

    public void newQueen(){
        fullNumberOfAnts++;
        queens++;
        countAnts();
    }

    private int princes;
    public void newPrince(){
        fullNumberOfAnts++;
        princes++;
        countAnts();
    }

    public void setTeam(int team){
        teamNum = team;
        teams = Math.max(teamNum, teams);
    }

    public int getTeam(){
        return teamNum;
    }
    
    public int getAntNumber(){
        return fullNumberOfAnts;
    }
    
    public int getSolNumber(){
        return soldiers;
    }
    
    public int getWorkerNumber(){
        return fullNumberOfAnts - soldiers - nurses - queens - princes - princesses;
    }
    
    public void setChanceToWin(double chance){
        chanceToWin=chance;
    }
    
    public boolean fully(){
        return fullNumberOfAnts>=maxAnts;
    }
    
    public void newSoldier(){
        soldiers++;
    }

    public void newNurse(){
        nurses++;
    }

    public int getNurseNumber(){
        return nurses;
    }
    
    public int getFood(){
        return food;
    }
    
    /*private void updateImage(){
        GreenfootImage image=new GreenfootImage(getImage().getWidth()+3,getImage().getHeight()+3);
        image.setColor(Color.BLUE);
        image.fillOval(0,0,image.getWidth()-1,image.getHeight()-1);
        image.drawImage(getImage(),1,1);
        setImage(image);
    }*/

    boolean start=true;
    public void act()
    {
        if(start){
            createUnderground();
            updateImage();
            
            start=false;
        }
        
        drawChance();

        countFood();
    }

    public void drawChance(){
        if(chanceLabel == null) 
        {
            chanceLabel=new Label("",25);
            int x = getX();
            int y = getY() - getImage().getWidth()/2 - 8;

            getWorld().addObject(chanceLabel, x, y);
            chanceLabel.setFillColor(AntWorld.teamColors.get(getTeam() - 1));
        }
        chanceLabel.setFillColor(AntWorld.teamColors.get(getTeam() - 1));
        
        chanceLabel.setValue("AntHill "+getTeam());
    }
    
    public void countFood(int energy)
    {
        if(foodCounter == null) 
        {
            foodCounter = new Counter("Food: ");
            int x = getX();
            int y = getY() + 26 + 20;

            getWorld().addObject(foodCounter, x, y);
        }     
        food+=energy;
        foodCounter.setValue(food);
        foodCounter.draw();
    }
    
    public void eatFood(int energy)
    {
        if(foodCounter == null) 
        {
            foodCounter = new Counter("Food: ");
            int x = getX();
            int y = getY() + 26 + 20;

            getWorld().addObject(foodCounter, x, y);
        }     
        food-=energy;
        foodCounter.setValue(food);
        foodCounter.draw();
    }

    public void countFood(){
        if(foodCounter == null)
        {
            foodCounter = new Counter("Food: ");
            int x = getX();
            int y = getY() + 26 + 20;

            getWorld().addObject(foodCounter, x, y);
        }
        food = 0;
        getObjectsInRange(100, TakenFood.class).forEach((tf) -> {
            if(!tf.taken && tf.underGround){
                food += tf.energyCost;
            }
        });
        foodCounter.setValue(food);
        foodCounter.draw();
    }
    
    public void countAnts(){
        if(antCounter == null) 
        {
            antCounter = new Counter("Ants: ");
            int x = getX();
            int y = getY() + 26 + 8;

            getWorld().addObject(antCounter, x, y);
        }     
        antCounter.setValue(fullNumberOfAnts);
        antCounter.draw();
    }
    
    private final SimpleTimer timer = new SimpleTimer();
    private int dieAnts;
    private final int step = 1500;
    
    private void eat(){
        timer.calculate();
        if(timer.getTime()>=step){
            if(food>fullNumberOfAnts-ants){
                food-=(fullNumberOfAnts-ants);
            }
            else{
                dieAnts=((fullNumberOfAnts-ants)-food);
                ants-=dieAnts;
                fullNumberOfAnts-=dieAnts;
                food=0;
                countAnts();
            }
            timer.update();
        }
    }
    
    public boolean haveQueen(){
        return queens > 0;
    }
    
    public void queenDead(){
        fullNumberOfAnts--;
        queens--;
        countAnts();
    }
    
    public void soldierDead(){
        fullNumberOfAnts--;
        soldiers--;
        countAnts();
    }
    
    public void antDead(){
        fullNumberOfAnts--;
        countAnts();
    }

    public void nurseDead(){
        fullNumberOfAnts--;
        nurses--;
        countAnts();
    }

    public void princeDead(){
        fullNumberOfAnts--;
        princes--;
        countAnts();
    }

    public void princessDead(){
        fullNumberOfAnts--;
        princesses--;
        countAnts();
    }

    public void reduceNurse(){
        nurses--;
    }

    private int eggsFood;
    public void newEgg(int eggFood){
        eggs ++;
        eggsFood += eggFood;
    }

    public void reduseEgg(int eggFood){
        eggs --;
        eggsFood -= eggFood;
    }

    public void countEndurance(){
        if(endurance < MAX_ENDURANCE) {
            endurance++;
            updateImage();
        }
    }

    GreenfootImage antHill = new GreenfootImage("anthill.png");
    GreenfootImage antHill1 = new GreenfootImage("anthill1.png");
    GreenfootImage image;
    final int IMAGE_SIZE = 6;
    private void generateMainImage(){
        image = new GreenfootImage(IMAGE_SIZE, IMAGE_SIZE);
        image.drawImage(antHill, (IMAGE_SIZE / 2) - antHill.getWidth() / 2, (IMAGE_SIZE / 2) - antHill.getHeight() / 2);
    }

    private void updateImage(){
        /*if(image == null){
            generateMainImage();
        }*/

        int s = (int)(Math.pow(IMAGE_SIZE / 2, 2) * Math.PI);
        s += endurance;
        int r = (int)Math.sqrt(s / Math.PI);
        antHill = new GreenfootImage("anthill.png");
        GreenfootImage myImage = new GreenfootImage(Math.max(IMAGE_SIZE, r * 2), Math.max(IMAGE_SIZE, (int)(((double)(r * 2) / antHill.getWidth()) * antHill.getHeight())));
        antHill.scale(myImage.getWidth(), myImage.getHeight());
        myImage.drawImage(antHill, 0, 0);
        myImage.drawImage(antHill1, (myImage.getWidth() / 2) - (IMAGE_SIZE / 2), (myImage.getHeight() / 2) - (IMAGE_SIZE / 2));
        setImage(myImage);
    }
}