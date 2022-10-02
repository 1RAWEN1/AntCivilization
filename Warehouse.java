import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.Random;

/**
 * Write a description of class Warehouse here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Warehouse extends Actor
{
    /**
     * Act - do whatever the Warehouse wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private AntHill home;
    private static final int SIZE = 30;
    private static final int HALFSIZE = SIZE / 2;
    private static final Color color1 = new Color(160, 200, 60);
    private static final Color color2 = new Color(80, 100, 30);
    private static final Color color3 = new Color(10, 50, 0);

    private static final Random randomizer = new Random();
    
    private int crumbs = 0;  // number of bits of food in this pile
    static final int MAX_CRUMBS=100;
    public Warehouse(AntHill home){
        this.home=home;
        updateImage();
    }
        
    public void act() 
    {
        // Add your action code here.
    }   
    
    public boolean notEmpty(){
        return crumbs>0;
    }
    
    public void addFood(){
        crumbs++;
        home.countFood(1);
        updateImage();
    }
    
    public void takeSome()
    {
        crumbs--;
        home.eatFood(1);
        updateImage();
    }
    
    public boolean isFully(){
        return crumbs>=MAX_CRUMBS;
    }
    
    private void updateImage()
    {
        GreenfootImage image = new GreenfootImage(SIZE, SIZE);

        for (int i = 0; i < crumbs; i++) {
            int x = randomCoord();
            int y = randomCoord();

            image.setColorAt(x, y, color1);
            image.setColorAt(x + 1, y, color2);
            image.setColorAt(x, y + 1, color2);
            image.setColorAt(x + 1, y + 1, color3);
        }
        
        image.setTransparency(100);
        
        setImage(image);
    }

    /**
     * Returns a random number relative to the size of the food pile.
     */
    private int randomCoord()
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
