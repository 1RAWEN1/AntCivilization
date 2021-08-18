import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

import java.util.Random;

//renamed
public class Food extends Actor
{
    private static final int SIZE = 30;
    private static final int HALFSIZE = SIZE / 2;
    private static final Color colorLight = new Color(160, 200, 60);
    private static final Color colorMedium = new Color(80, 100, 30);
    private static final Color colorDark = new Color(10, 50, 0);

    private static final Random randomizer = new Random();
    
    private int crumbs = 30;
    static final int MAX_CRUMBS=30;

    public Food()
    {
        updateImage();
    }
    
    public Food(int crumbs)
    {
        this.crumbs=crumbs;
        updateImage();
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
}