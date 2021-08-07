import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
 
public class AntHill extends Actor
{
    static int teams;
    
    private int teamNum;
    
    private int fullNumberOfAnts = 5;
    /** Number of ants that have come out so far. */
    private int ants = 0;
    
    /** Total number of ants in this hill. */
    private int maxAnts = 40;

    /** Counter to show how much food have been collected so far. */
    private Counter foodCounter;
    
    private Counter antCounter;
    
    private int food;
    
    /**
     * Constructor for ant hill with default number of ants (40).
     */
    public AntHill()
    {
    }

    /**
     * Construct an ant hill with a given number of ants.
     */
    public AntHill(int numberOfAnts)
    {
        maxAnts = numberOfAnts;
        teams++;
        teamNum=teams;
    }
    
    public AntHill(int numberOfAnts, int team)
    {
        maxAnts = numberOfAnts;
        teamNum=team;
    }
    
    public void setAntNumber(int ants){
        fullNumberOfAnts=ants;
    }
    
    public int getTeam(){
        return teamNum;
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
    public void act()
    {
        if(ants<fullNumberOfAnts) 
        {
            if(Greenfoot.getRandomNumber(100) < 10) 
            {
                getWorld().addObject(new Ant(this), getX(), getY());
                ants++;
                countAnts();
            }
        }
        
        if(food>=3 && fullNumberOfAnts<maxAnts){
            food-=3;
            countFood();
            fullNumberOfAnts++;
            countAnts();
        }
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
    
    public void countAnts(){
        if(antCounter == null) 
        {
            antCounter = new Counter("Ants: ");
            int x = getX();
            int y = getY() + getImage().getWidth()/2 + 8;

            getWorld().addObject(antCounter, x, y);
        }     
        antCounter.setValue(fullNumberOfAnts);
        antCounter.countAnts(fullNumberOfAnts-ants);
    }
    
    private SimpleTimer timer = new SimpleTimer();
    private int dieAnts;
    private final int step = 1500;
    
    private void eat(){
        timer.calculate();
        if(timer.getTime()/step>=1){
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
    
    public void antDead(){
        fullNumberOfAnts--;
        ants--;
        countAnts();
    }
}