import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
 
public class AntHill extends Actor
{
    private Label chanceLabel;
    
    private double chanceToWin;
    
    static int teams;
    
    private int teamNum;
    
    private int fullNumberOfAnts = 5;
    /** Number of ants that have come out so far. */
    private int ants = 0;
    
    private int soldiers = 0;
    
    /** Total number of ants in this hill. */
    private int maxAnts = 40;

    /** Counter to show how much food have been collected so far. */
    private Counter foodCounter;
    
    private Counter antCounter;
    
    private int food;
    
    private final int MAX_FOOD=100;
    
    private boolean haveQueen=true;
    
    /**
     * Constructor for ant hill with default number of ants (40).
     */
    public AntHill()
    {
        AntWorld.homeArray.add(this);
    }

    /**
     * Construct an ant hill with a given number of ants.
     */
    public AntHill(int numberOfAnts)
    {
        maxAnts = numberOfAnts;
        teams++;
        teamNum=teams;
        AntWorld.homeArray.add(this);
    }
    
    public AntHill(int numberOfAnts, int team)
    {
        maxAnts = numberOfAnts;
        teamNum=team;
        AntWorld.homeArray.add(this);
    }
    
    public void setAntNumber(int ants){
        fullNumberOfAnts=ants;
    }
    
    private final int BLOCK_SIZE=20;
    public void createUnderground(){
        for(int x=0;x<9;x++){
            for(int y=0;y<9;y++){
                if(Math.abs(4-x)+Math.abs(4-y)>6 || 4-x==0 && 4-y==0 || Math.abs(4-x)+Math.abs(4-y)==1
                || 4>Math.abs(4-y) && Math.abs(4-y)>1 && Math.abs(4-x)<=1 || 4>Math.abs(4-x) && Math.abs(4-x)>1 && Math.abs(4-y)<=1){
                    
                }
                else{
                    getWorld().addObject(new Block(),getX()-BLOCK_SIZE+(5-x)*BLOCK_SIZE,getY()-BLOCK_SIZE+(5-y)*BLOCK_SIZE);
                }
            }
        }
        
        getWorld().addObject(new Warehouse(this), getX(), getY()+50);
        getWorld().addObject(new Warehouse(this), getX()+50, getY());
        getWorld().addObject(new Warehouse(this), getX()-50, getY());
        
        getWorld().addObject(new QueenAnt(this),getX()-20,getY()-55);
        
        for(int i=0;i<fullNumberOfAnts;i++){
            //getWorld().addObject(new Ant(this),getX()-20,getY()-55);
            createNewAnt(new Ant(this));
        }
        
        countAnts();
    }
    
    public void createNewAnt(Ant ant){
        if(ant.getProfession()==2){
            newSoldier();
        } 
        getWorld().addObject(ant ,getX()-20,getY()-55);
    }
    
    public void newAnt(Ant ant){
        fullNumberOfAnts++;
        countAnts();
        if(ant.getProfession()==2){
            newSoldier();
        } 
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
    
    public int getNeutralAnts(){
        return fullNumberOfAnts-soldiers;
    }
    
    public void setChanceToWin(double chance){
        chanceToWin=chance;
    }
    
    public boolean fully(){
        return fullNumberOfAnts==maxAnts;
    }
    
    public void newSoldier(){
        soldiers++;
    }
    
    public int getFood(){
        return food;
    }
    
    private void updateImage(){
        GreenfootImage image=new GreenfootImage(getImage().getWidth()+3,getImage().getHeight()+3);
        image.setColor(Color.BLUE);
        image.fillOval(0,0,image.getWidth()-1,image.getHeight()-1);
        image.drawImage(getImage(),1,1);
        setImage(image);
    }

    /**
     * Act: If there are still ants left inside, see whether one should come out.
     */
    boolean start=true;
    public void act()
    {
        if(start){
            createUnderground();
            
            start=false;
        }
        
        drawChance();
    }

    public void drawChance(){
        if(chanceLabel == null) 
        {
            chanceLabel=new Label("",25);
            int x = getX();
            int y = getY() - getImage().getWidth()/2 - 8;

            getWorld().addObject(chanceLabel, x, y);
            if(getTeam()==1){
                chanceLabel.setFillColor(Color.BLUE);
            }
            else if(getTeam()==2){
                chanceLabel.setFillColor(Color.CYAN);
            }
            else if(getTeam()==3){
                chanceLabel.setFillColor(Color.YELLOW);
            }
            else if(getTeam()==4){
                chanceLabel.setFillColor(Color.GREEN);
            }
        }
        
        chanceLabel.setValue(""+(double)((int)(chanceToWin*1000))/10+" %");
    }
    
    /**
     * Record that we have collected another bit of food.
     */
    public void countFood()
    {
        if(foodCounter == null) 
        {
            foodCounter = new Counter("Food: ");
            int x = getX();
            int y = getY() + getImage().getWidth()/2 + 20;

            getWorld().addObject(foodCounter, x, y);
        }     
        food++;
        foodCounter.setValue(food);
        foodCounter.draw();
    }
    
    public void eatFood()
    {
        if(foodCounter == null) 
        {
            foodCounter = new Counter("Food: ");
            int x = getX();
            int y = getY() + getImage().getWidth()/2 + 20;

            getWorld().addObject(foodCounter, x, y);
        }     
        food--;
        foodCounter.setValue(food);
        foodCounter.draw();
    }
    
    public void countAnts(){
        if(antCounter == null) 
        {
            antCounter = new Counter("Ants: ");
            int x = getX();
            int y = getY() + getImage().getWidth()/2 + 8;

            getWorld().addObject(antCounter, x, y);
        }     
        antCounter.setValue(fullNumberOfAnts);
        antCounter.draw();
    }
    
    private SimpleTimer timer = new SimpleTimer();
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
        return haveQueen;
    }
    
    public void queenDead(){
        haveQueen=false;
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
}