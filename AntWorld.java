import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

import java.util.ArrayList;
public class AntWorld extends World
{
    public static final int SIZE = 620;
    
    public static ArrayList<AntHill> arrayOfHouses;

    public static ArrayList<Color> teamColors = new ArrayList<>();

    public AntWorld()
    {
        super(1400, 750, 1);
        arrayOfHouses = new ArrayList<>();
        if(teamColors.size() == 0){
            generateNewColors();
            /*teamColors.add(Color.BLUE);
            teamColors.add(Color.CYAN);
            teamColors.add(Color.YELLOW);
            teamColors.add(Color.GREEN);
            teamColors.add(new Color(200, 191, 231));
            teamColors.add(Color.LIGHT_GRAY);
            teamColors.add(Color.WHITE);
            teamColors.add(Color.MAGENTA);
            teamColors.add(new Color(185, 122, 87));
            teamColors.add(new Color(163, 73, 164));
            teamColors.add(new Color(153, 217, 234));*/
        }
        AntHill.teams=0;
        setPaintOrder(Label.class, Scroller.class, Counter.class, Prince.class, QueenAnt.class, Egg.class, Ant.class, TakenFood.class, Pheromone.class, AttackPheromone.class, Food.class, AntHill.class, Stone.class, Block.class);
        newWorld();
    }

    int diff;
    boolean currentColor;
    public void generateNewColors(){
        teamColors.clear();
        for(int i = 0; i < 6; i++){
            do {
                teamColors.add(new Color(Greenfoot.getRandomNumber(255), Greenfoot.getRandomNumber(255), Greenfoot.getRandomNumber(255)));

                currentColor = true;
                for(int i1 = 0; i1 < i; i1++){
                    diff = 0;
                    diff += Math.abs(teamColors.get(i1).getRed() - teamColors.get(i).getRed());
                    diff += Math.abs(teamColors.get(i1).getGreen() - teamColors.get(i).getGreen());
                    diff += Math.abs(teamColors.get(i1).getBlue() - teamColors.get(i).getBlue());

                    if(diff <= 100){
                        currentColor = false;
                        break;
                    }
                }
                if(!currentColor){
                    teamColors.remove(i);
                }
            }while(!currentColor);
        }
    }

    public void randomMeatGenerate(){
        for(int i = 0; i < getObjects(AntHill.class).size(); i++){
            addObject(new Food(Greenfoot.getRandomNumber(Food.MAX_CRUMBS)+5, 2, 1), randomCoordX(), randomCoordY());
        }
    }
    
    private void newWorld(){
        hasWinner = false;
        AntHill.teams=0;
        arrayOfHouses.clear();
        generateNewColors();
        
        /*int rand=Greenfoot.getRandomNumber(5)+2;
        if(rand==1){
            setup1();
        }
        else if(rand==2){
            setup2();
        }
        else if(rand==3){
            setup3();
        }
        else if(rand==4){
            setup4();
        }
        else if(rand==5){
            setup5();
        }
        else if(rand==6){
            setup6();
        }*/
        randomGenerationOfWorld();
        randomMeatGenerate();
        createStones();
        createWinLabel();
    }

    private int distanceX;
    private int distanceY;

    int foodNum;
    public void randomGenerationOfWorld() {
        removeObjects(getObjects(null));

        int antHills = Greenfoot.getRandomNumber(5) + 2;
        int angle = Greenfoot.getRandomNumber(90);
        //double cos = Math.cos(Math.toRadians(angle <= 45 ? angle : 90 - angle));
        distanceX = Greenfoot.getRandomNumber((getWidth() / 2) - 300 - 120) + 300;
        distanceY = (int)(((double)distanceX / (getWidth() / 2)) * (getHeight() / 2));
        for(int i=0;i<antHills;i++){
            double a = Math.toRadians(angle + ((360.0 / antHills) * i));
            addObject(new AntHill(40), (int)(distanceX * Math.cos(a)) + (getWidth() / 2), (int)(distanceY * Math.sin(a)) + (getHeight() / 2));
        }

        foodNum = (getWidth() / 200) + (getHeight() / 200) + Greenfoot.getRandomNumber(10);
        for(int i=0;i<foodNum;i++){
            addObject(new Food(1, 1), randomCoordX(), randomCoordY());
        }
    }

    public void setup1()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(70,0), SIZE / 2, SIZE / 2);
        addObject(new Food(1, 1), SIZE / 2, SIZE / 2 - 260);
        addObject(new Food(1, 1), SIZE / 2 + 215, SIZE / 2 - 100);
        addObject(new Food(1, 1), SIZE / 2 + 215, SIZE / 2 + 100);
        addObject(new Food(1, 1), SIZE / 2, SIZE / 2 + 260);
        addObject(new Food(1, 1), SIZE / 2 - 215, SIZE / 2 + 100);
        addObject(new Food(1, 1), SIZE / 2 - 215, SIZE / 2 - 100);
    }

    public void setup2()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), SIZE - 100, SIZE - 264);
        addObject(new AntHill(40), 100, 267);

        addObject(new Food(1, 1), 80, 71);
        addObject(new Food(1, 1), 291, 56);
        addObject(new Food(1, 1), 516, 212);
        addObject(new Food(1, 1), 311, 269);
        addObject(new Food(1, 1), 318, 299);
        addObject(new Food(1, 1), 315, 331);
        addObject(new Food(1, 1), 141, 425);
        addObject(new Food(1, 1), 378, 547);
        addObject(new Food(1, 1), 566, 529);
    }

    public void setup3()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), SIZE - 100, 134);
        addObject(new AntHill(40), 100, SIZE - 100);

        addObject(new Food(1, 1), 182, 84);
        addObject(new Food(1, 1), 39, 308);
        addObject(new Food(1, 1), 249, 251);
        addObject(new Food(1, 1), 270, 272);
        addObject(new Food(1, 1), 291, 253);
        addObject(new Food(1, 1), 339, 342);
        addObject(new Food(1, 1), 593, 340);
        addObject(new Food(1, 1), 487, 565);
    }

    public void setup4(){
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), 100, SIZE/2);
        addObject(new AntHill(40), SIZE-100, 100);
        addObject(new AntHill(40), SIZE-100, SIZE-100);
        
        Food food = new Food(1, 1);
        addObject(food,255,280);
        Food food2 = new Food(1, 1);
        addObject(food2,255,320);
        Food food3 = new Food(1, 1);
        addObject(food3,315,225);
        Food food4 = new Food(1, 1);
        addObject(food4,345,260);
        Food food5 = new Food(1, 1);
        addObject(food5,345,340);
        Food food6 = new Food(1, 1);
        addObject(food6,315,375);
    }
    
    public void setup5()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        AntHill hill1=new AntHill(40, 1, 4);
        hill1.setAntNumber(20);
        addObject(hill1, SIZE / 2, SIZE / 2);
        
        addObject(new AntHill(10,2), 100, 100);
        addObject(new AntHill(10,2), 100, SIZE-100);
        addObject(new AntHill(10,2), SIZE-100, 100);
        addObject(new AntHill(10,2), SIZE-100, SIZE-100);
        
        addObject(new Food(1, 1), SIZE / 2, SIZE / 2 - 260);
        addObject(new Food(1, 1), SIZE / 2 + 215, SIZE / 2 - 100);
        addObject(new Food(1, 1), SIZE / 2 + 215, SIZE / 2 + 100);
        addObject(new Food(1, 1), SIZE / 2, SIZE / 2 + 260);
        addObject(new Food(1, 1), SIZE / 2 - 215, SIZE / 2 + 100);
        addObject(new Food(1, 1), SIZE / 2 - 215, SIZE / 2 - 100);
    }
    
    public void setup6()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), 100, 100);
        addObject(new AntHill(40), 100, SIZE-100);
        addObject(new AntHill(40), SIZE-100, 100);
        addObject(new AntHill(40), SIZE-100, SIZE-100);
        
        addObject(new Food(1, 1), SIZE / 2, SIZE / 2 - 260);
        addObject(new Food(1, 1), SIZE / 2 + 215, SIZE / 2 - 100);
        addObject(new Food(1, 1), SIZE / 2 + 215, SIZE / 2 + 100);
        addObject(new Food(1, 1), SIZE / 2, SIZE / 2 + 260);
        addObject(new Food(1, 1), SIZE / 2 - 215, SIZE / 2 + 100);
        addObject(new Food(1, 1), SIZE / 2 - 215, SIZE / 2 - 100);
    }
    
    public void createStones(){
        for(int i=0;i<10+Greenfoot.getRandomNumber(5);i++){
            Stone st=new Stone();
            addObject(st,randomCoordX(),randomCoordY());
        }
    }
    
    Scroller sc;
    Label winLabel;
    public void createWinLabel(){
        winLabel=new Label("",30);
        addObject(winLabel, getWidth() / 2, getHeight() / 2);
        
        sc=new Scroller(speed);
        addObject(sc,110,20);
    }

    int coord;

    private int randomCoordX(){
        return Greenfoot.getRandomNumber(getWidth());
    }
    private int randomCoordY(){
        return Greenfoot.getRandomNumber(getHeight());
    }

    private int randomAntHillCoord(){
        coord=Greenfoot.getRandomNumber(SIZE);
        return Math.abs(coord-(((coord+SIZE/2)/SIZE)*SIZE))<100 ? (100+(((coord+SIZE/2)/SIZE)*(SIZE-200))):coord;
    }

    private Food newFood;
    
    private int winTeam;
    
    private SimpleTimer winTimer = new SimpleTimer();
    
    private int fullSoldiers;
    
    public void analysis(){
        fullSoldiers=0;
        for(AntHill ah: arrayOfHouses){
            fullSoldiers+=ah.getSolNumber();
        }
        if(fullSoldiers==0){
            fullSoldiers=1;
        }
        for(AntHill ah: arrayOfHouses){
            ah.setChanceToWin((double)ah.getSolNumber()/fullSoldiers);
        }
    }

    private static int speed = 50;

    boolean absWinner;
    boolean hasWinner;
    public void act(){
        MouseInfo mi = Greenfoot.getMouseInfo();
        if(mi != null && mi.getActor() == null) {
            if (Greenfoot.mousePressed(null) && mi.getButton() == 1) {
                Stone s = new Stone();
                addObject(s, mi.getX(), mi.getY());
            } else if (Greenfoot.mousePressed(null) && mi.getButton() == 3) {
                newFood = new Food(Greenfoot.getRandomNumber(Food.MAX_CRUMBS) + 5, Greenfoot.getRandomNumber(3) == 1 ? 2 : 1, 1);
                addObject(newFood, mi.getX(), mi.getY());
            }
        }

        if(Greenfoot.getRandomNumber(300)==1){
            newFood=new Food(Greenfoot.getRandomNumber(Food.MAX_CRUMBS)+5, Greenfoot.getRandomNumber(3) == 1 ? 2 : 1, 1);
            addObject(newFood,randomCoordX(),randomCoordY());
        }

        if(!hasWinner) {
            int antsInWorld = 0;
            winTeam = -1;
            absWinner = false;
            //analysis();
            for (AntHill ah : arrayOfHouses) {
                antsInWorld += ah.getAntNumber();
                if(ah.haveQueen()) {
                    if (winTeam == -1) {
                        winTeam = ah.getTeam();
                        if (ah.fully()) {
                            absWinner = true;
                        }
                    } else if (ah.getTeam() != winTeam && !absWinner || ah.getTeam() != winTeam && ah.fully()) {
                        if (ah.fully() && !absWinner) {
                            winTeam = ah.getTeam();
                            absWinner = true;
                        } else {
                            winTeam = 0;
                        }
                    }
                }
            }

            if (winTeam == 0) {
                winTeam = -1;
            }

            if (antsInWorld == 0) {
                winTeam = 0;
            }
        }
        
        if(winTeam!=-1){
            winLabel.setFillColor(teamColors.get(winTeam - 1));
            
            if(winTeam!=0){
                winLabel.setValue("WINNER!");
            }
            else{
                winLabel.setValue("DRAWN!");
            }
            hasWinner = true;
            winTimer.calculate();
            if(winTimer.getTime() > 150){
                newWorld();
            }
        }
        else{
            winLabel.setValue("");
            winTimer.update();
        }
        /*if(newFood!=null && newFood.getWorld()==null){
        newFood=null;
        }
        if(newFood!=null && Greenfoot.getRandomNumber(100)<10){
        newFood.addFood();
        if(newFood.crumbsIsMax()){
        newFood=null;
        }
        }*/
        speed = sc.getValue();
        if(speed>20){
            Greenfoot.setSpeed(speed);
        }
    }
}
