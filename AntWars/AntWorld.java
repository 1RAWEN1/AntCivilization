import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

public class AntWorld extends World
{
    public static final int SIZE = 620;

    /**
     * Create a new world. It will be initialised with a few ant hills
     * and food sources
     */
    public AntWorld()
    {
        super(SIZE, SIZE, 1);
        AntHill.teams=0;
        setPaintOrder(Ant.class, Pheromone.class, AttackPheromone.class, AntHill.class, Food.class);
        int rand=Greenfoot.getRandomNumber(6)+1;
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
        createStones();
    }

    /**
     * Create world contents: one ant hill and food.
     */
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

    /**
     * Create world contents: two ant hills and food.
     */
    public void setup2()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), 506, 356);
        addObject(new AntHill(40), 95, 267);

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

    /**
     * Create world contents: two ant hills and food.
     */
    public void setup3()
    {
        removeObjects(getObjects(null));  // remove all existing objects
        addObject(new AntHill(40), 576, 134);
        addObject(new AntHill(40), 59, 512);

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
        addObject(new AntHill(40), 29, SIZE/2);
        addObject(new AntHill(40), SIZE-29, 29);
        addObject(new AntHill(40), SIZE-29, SIZE-29);
        
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
        
        addObject(new AntHill(10,2), 29, 29);
        addObject(new AntHill(10,2), 29, SIZE-29);
        addObject(new AntHill(10,2), SIZE-29, 29);
        addObject(new AntHill(10,2), SIZE-29, SIZE-29);
        
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
        addObject(new AntHill(40), 29, 29);
        addObject(new AntHill(40), 29, SIZE-29);
        addObject(new AntHill(40), SIZE-29, 29);
        addObject(new AntHill(40), SIZE-29, SIZE-29);
        
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
            addObject(st,Greenfoot.getRandomNumber(getWidth()),Greenfoot.getRandomNumber(getHeight()));
        }
    }

    private int randomCoord(){
        return Greenfoot.getRandomNumber(SIZE);
    }

    Food newFood;
    public void act(){
        if(Greenfoot.getRandomNumber(200)==1){
            newFood=new Food((Greenfoot.getRandomNumber(Food.MAX_CRUMBS/3)+1)*3);
            addObject(newFood,randomCoord(),randomCoord());
            newFood.addedToWorld();
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
    }
}
