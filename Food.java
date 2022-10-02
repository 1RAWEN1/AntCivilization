import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

import java.util.Random;

//renamed
public class Food extends Actor
{

    private int secondTypeOfFood;
    private int SIZE = 30;
    private int HALFSIZE = SIZE / 2;
    private static final Color colorLight = new Color(160, 200, 60);
    private static final Color colorMedium = new Color(80, 100, 30);
    private static final Color colorDark = new Color(10, 50, 0);

    private static final Random randomizer = new Random();
    
    private int crumbs = 30;
    static final int MAX_CRUMBS=30;

    private int typeOfFood = 1;

    int energyCost;
    // 1 - plant
    // 2 - meat

    public Food(int typeOfFood, int secondTypeOfFood)
    {
        this.secondTypeOfFood = secondTypeOfFood;
        this.typeOfFood = typeOfFood;
        if(typeOfFood == 1){
            energyCost = 1;
        }
        else{
            energyCost = 2;
        }
        updateImage();
    }
    
    public Food(int crumbs, int typeOfFood, int secondTypeOfFood)
    {
        this.secondTypeOfFood = secondTypeOfFood;
        this.typeOfFood = typeOfFood;
        if(typeOfFood == 1){
            energyCost = 1;
        }
        else{
            energyCost = 2;
        }
        this.crumbs=crumbs;
        updateImage();
    }

    public int getTypeOfFood(){
        return typeOfFood;
    }
    public int getSecondTypeOfFood(){
        return secondTypeOfFood;
    }

    
    public void removeIfTouchingStone(){
        if(getOneIntersectingObject(Stone.class) != null){
            getWorld().removeObject(this);
        }
    }
    
    public void addFood(){
        crumbs++;
        updateImage();
    }
    
    public boolean isCrumbsMax(){
        return crumbs>=MAX_CRUMBS;
    }

    public int getCrumbs(){
        return crumbs;
    }

    public void takeSome()
    {
        crumbs--;
        if (crumbs <= 0) {
            getWorld().removeObject(this);
        }
        else {
            updateImage();
        }
    }

    private void updateImage()
    {
        if(typeOfFood == 1) {
            if(secondTypeOfFood == 1) {
                SIZE = crumbs <= 3 ? Math.max(2, crumbs * 2) : crumbs;
                HALFSIZE = SIZE / 2;
                GreenfootImage image = new GreenfootImage(SIZE, SIZE);

                for (int i = 0; i < crumbs; i++) {
                    int x = randomCoordOfFood();
                    int y = randomCoordOfFood();

                    image.setColorAt(x, y, colorLight);
                    image.setColorAt(x + 1, y, colorMedium);
                    image.setColorAt(x, y + 1, colorMedium);
                    image.setColorAt(x + 1, y + 1, colorDark);
                }
                setImage(image);
            }
        }
        else{
            if(secondTypeOfFood == 1) {
                GreenfootImage image = new GreenfootImage("Meat.png");
                image.scale(Math.max(1, (int) (image.getWidth() * ((double) crumbs / MAX_CRUMBS))), Math.max(1, (int) (image.getHeight() * ((double) crumbs / MAX_CRUMBS))));
                setImage(image);
            }
        }
    }

    private int randomCoordOfFood()
    {
        int val = HALFSIZE + (int) (randomizer.nextGaussian() * (HALFSIZE / 2));
        
        if (val < 0)
            return 0;

        if (val > SIZE - 2)
            return SIZE - 2;
        else
            return val;
    }

    public void act(){
        touchObs();
    }

    int x;
    int y;
    int changeXLoc;
    int changeYLoc;
    int changeXLoc1;
    int changeYLoc1;
    public void touchObs(){
        if(isTouching(Obs.class)) {
            x = getX();
            y = getY();
            changeXLoc = 0;
            changeYLoc = 0;
            for (Obs o : getIntersectingObjects(Obs.class)) {
                changeXLoc1 = (o.getImage().getWidth() / 2) + HALFSIZE - Math.abs(getX() - o.getX());

                changeYLoc1 = (o.getImage().getHeight() / 2) + HALFSIZE - Math.abs(getY() - o.getY());

                if (changeXLoc1 <= changeYLoc1 && changeXLoc1 > 0) {
                    changeYLoc1 = 0;
                } else if (changeXLoc1 > changeYLoc1 && changeYLoc1 > 0) {
                    changeXLoc1 = 0;
                }

                changeXLoc = Math.abs(changeXLoc) > changeXLoc1 ? changeXLoc : changeXLoc1;
                changeYLoc = Math.abs(changeYLoc) > changeYLoc1 ? changeYLoc : changeYLoc1;

                if (o.getX() > getX() && changeXLoc == changeXLoc1) {
                    changeXLoc = -changeXLoc1;
                }
                if (o.getY() > getY() && changeYLoc == changeYLoc1) {
                    changeYLoc = -changeYLoc1;
                }
            }

            setLocation(x + changeXLoc, y + changeYLoc);
        }
    }
}