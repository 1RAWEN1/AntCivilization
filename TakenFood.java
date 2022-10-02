import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class takenFood here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TakenFood extends Actor
{
    boolean underGround = false;
    boolean taken = true;
    AntHill ah;
    int typeOfFood;
    int energyCost;

    private int secondTypeOfFood;

    public TakenFood(AntHill ah, int typeOfFood, int secondTypeOfFood, GreenfootImage foodImage){
        this.typeOfFood = typeOfFood;
        this.ah=ah;
        this.secondTypeOfFood = secondTypeOfFood;

        if(typeOfFood == 1){
            if(secondTypeOfFood == 1) {
                energyCost = 1;
            }
            /*GreenfootImage image = new GreenfootImage(2, 2);
            image.setColor(Color.GREEN);
            image.fill();
            setImage(image);*/
        }
        else{
            if(secondTypeOfFood == 1) {
                energyCost = 2;
            }
        }

        int width = Math.min(2, foodImage.getWidth());
        int height = Math.min(2, foodImage.getHeight());
        GreenfootImage myImage = new GreenfootImage(width, height);
        boolean drawImage;
        do{
            myImage.drawImage(foodImage, -Greenfoot.getRandomNumber(foodImage.getWidth())
                    , -Greenfoot.getRandomNumber(foodImage.getHeight()));
            drawImage = true;
            for(int i = 0; i < width * height; i++){
                if(myImage.getColorAt(i % width, i / height).getAlpha() == 0){
                    drawImage = false;
                    break;
                }
            }
        }while(!drawImage);

        setImage(myImage);
    }

    SimpleTimer timer = new SimpleTimer();
    public void act() 
    {
        timer.calculate();
        if(timer.getTime() > 3000) {
            if(underGround)
                ah.eatFood(energyCost);
            getWorld().removeObject(this);
        }
        // Add your action code here.
    }

    public void countFood(){
        ah.countFood(energyCost);
    }
    public void setUnderGround(boolean underGround){
        if(underGround && !this.underGround)
            countFood();
        else if(!underGround && this.underGround)
            removeFromHome();

        this.underGround = underGround;
    }

    public boolean getUnderGround(){
        return underGround;
    }
    
    public void drop(){
        taken=false;
    }

    public void put(){
        taken=true;
    }
    
    public void eat(){
        ah.eatFood(energyCost);
        getWorld().removeObject(this);
    }

    public void removeFromHome(){
        ah.eatFood(energyCost);
    }
}
