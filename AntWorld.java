import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

import java.util.ArrayList;
public class AntWorld extends World
{
    public static final int SIZE = 620;
    
    public static ArrayList<AntHill> arrayOfHouses;

    public AntWorld()
    {
        super(SIZE, SIZE, 1);
        arrayOfHouses =new ArrayList<AntHill>();
        AntHill.teams=0;
        setPaintOrder(Label.class, Scroller.class, Counter.class, QueenAnt.class, Egg.class, Ant.class, TakenFood.class, Pheromone.class, AttackPheromone.class, AntHill.class, Food.class, Stone.class, Block.class);
        newWorld();
    }
    
    private void newWorld(){
        arrayOfHouses.clear();
        AntHill.teams=0;
        
        int rand=Greenfoot.getRandomNumber(5)+2;
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
        }
        //randomGenerationOfWorld();
        createStones();
        createWinLabel();
    }

    public void randomGenerationOfWorld() {
        removeObjects(getObjects(null));

        for(int i=0;i<Greenfoot.getRandomNumber(3)+2;i++){
            addObject(new AntHill(40), randomAntHillCoord(), randomAntHillCoord());
        }

        for(int i=0;i<Greenfoot.getRandomNumber(10)+5;i++){
            addObject(new Food(), randomCoord(), randomCoord());
        }
    }

    public void setup1()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(70,0), SIZE / 2, SIZE / 2);
        addObject(new Food(), SIZE / 2, SIZE / 2 - 260);
        addObject(new Food(), SIZE / 2 + 215, SIZE / 2 - 100);
        addObject(new Food(), SIZE / 2 + 215, SIZE / 2 + 100);
        addObject(new Food(), SIZE / 2, SIZE / 2 + 260);
        addObject(new Food(), SIZE / 2 - 215, SIZE / 2 + 100);
        addObject(new Food(), SIZE / 2 - 215, SIZE / 2 - 100);
    }

    public void setup2()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), 520, 356);
        addObject(new AntHill(40), 100, 267);

        addObject(new Food(), 80, 71);
        addObject(new Food(), 291, 56);
        addObject(new Food(), 516, 212);
        addObject(new Food(), 311, 269);
        addObject(new Food(), 318, 299);
        addObject(new Food(), 315, 331);
        addObject(new Food(), 141, 425);
        addObject(new Food(), 378, 547);
        addObject(new Food(), 566, 529);
    }

    public void setup3()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), 520, 134);
        addObject(new AntHill(40), 100, 512);

        addObject(new Food(), 182, 84);
        addObject(new Food(), 39, 308);
        addObject(new Food(), 249, 251);
        addObject(new Food(), 270, 272);
        addObject(new Food(), 291, 253);
        addObject(new Food(), 339, 342);
        addObject(new Food(), 593, 340);
        addObject(new Food(), 487, 565);
    }

    public void setup4(){
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), 100, SIZE/2);
        addObject(new AntHill(40), SIZE-100, 100);
        addObject(new AntHill(40), SIZE-100, SIZE-100);
        
        Food food = new Food();
        addObject(food,255,280);
        Food food2 = new Food();
        addObject(food2,255,320);
        Food food3 = new Food();
        addObject(food3,315,225);
        Food food4 = new Food();
        addObject(food4,345,260);
        Food food5 = new Food();
        addObject(food5,345,340);
        Food food6 = new Food();
        addObject(food6,315,375);
    }
    
    public void setup5()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        AntHill hill1=new AntHill(40);
        hill1.setAntNumber(20);
        addObject(hill1, SIZE / 2, SIZE / 2);
        
        addObject(new AntHill(10,2), 100, 100);
        addObject(new AntHill(10,2), 100, SIZE-100);
        addObject(new AntHill(10,2), SIZE-100, 100);
        addObject(new AntHill(10,2), SIZE-100, SIZE-100);
        
        addObject(new Food(), SIZE / 2, SIZE / 2 - 260);
        addObject(new Food(), SIZE / 2 + 215, SIZE / 2 - 100);
        addObject(new Food(), SIZE / 2 + 215, SIZE / 2 + 100);
        addObject(new Food(), SIZE / 2, SIZE / 2 + 260);
        addObject(new Food(), SIZE / 2 - 215, SIZE / 2 + 100);
        addObject(new Food(), SIZE / 2 - 215, SIZE / 2 - 100);
    }
    
    public void setup6()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), 100, 100);
        addObject(new AntHill(40), 100, SIZE-100);
        addObject(new AntHill(40), SIZE-100, 100);
        addObject(new AntHill(40), SIZE-100, SIZE-100);
        
        addObject(new Food(), SIZE / 2, SIZE / 2 - 260);
        addObject(new Food(), SIZE / 2 + 215, SIZE / 2 - 100);
        addObject(new Food(), SIZE / 2 + 215, SIZE / 2 + 100);
        addObject(new Food(), SIZE / 2, SIZE / 2 + 260);
        addObject(new Food(), SIZE / 2 - 215, SIZE / 2 + 100);
        addObject(new Food(), SIZE / 2 - 215, SIZE / 2 - 100);
    }
    
    public void createStones(){
        for(int i=0;i<2+Greenfoot.getRandomNumber(4);i++){
            Stone st=new Stone();
            addObject(st,randomCoord(),randomCoord());
        }
    }
    
    Scroller sc;
    Label winLabel;
    public void createWinLabel(){
        winLabel=new Label("",30);
        addObject(winLabel, SIZE/2, SIZE/2);
        
        sc=new Scroller(speed);
        addObject(sc,110,20);
    }

    int coord;

    private int randomCoord(){
        return Greenfoot.getRandomNumber(SIZE);
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

    private int antsInWorld = 0;
    
    private static int speed = 50;

    public void act(){
        if(Greenfoot.getRandomNumber(200)==1){
            newFood=new Food((Greenfoot.getRandomNumber(Food.MAX_CRUMBS/3)+1)*3);
            addObject(newFood,randomCoord(),randomCoord());
            newFood.removeIfTouchingStone();
        }
        
        antsInWorld = 0;
        winTeam=-1;
        //analysis();
        for(int i = 0; i< arrayOfHouses.size(); i++){
            antsInWorld += arrayOfHouses.get(i).getAntNumber();
            if(winTeam==-1 && arrayOfHouses.get(i).getAntNumber()>0 || arrayOfHouses.get(i).fully()){
                winTeam= arrayOfHouses.get(i).getTeam();
            }
            else if(arrayOfHouses.get(i).getTeam()!=winTeam && arrayOfHouses.get(i).getAntNumber()>0){
                winTeam=-1;
                break;
            }
        }
        
        if(antsInWorld==0){
            winTeam=0;
        }
        
        if(winTeam!=-1){
            if(winTeam==1){
                winLabel.setFillColor(Color.BLUE);
            }
            else if(winTeam==2){
                winLabel.setFillColor(Color.CYAN);
            }
            else if(winTeam==3){
                winLabel.setFillColor(Color.YELLOW);
            }
            else if(winTeam==4){
                winLabel.setFillColor(Color.GREEN);
            }
            
            if(winTeam!=0){
                winLabel.setValue("WINNER!");
            }
            else{
                winLabel.setValue("DRAWN!");
            }
            winTimer.calculate();
            if(winTimer.getTime()>250){
                newWorld();
            }
        }
        else{
            winLabel.setValue("");
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
        if(speed>30){
            Greenfoot.setSpeed(speed);
        }
    }
}
