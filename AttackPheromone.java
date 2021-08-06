import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class AttackPheromone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AttackPheromone extends Actor
{
    private final static int MAX_INTENSITY = 320;
    private int intensity;

    /**
     * Create a new drop of pheromone with full intensity.
     */
    public AttackPheromone()
    {
        intensity = MAX_INTENSITY;
        updateImage();
    }
    
    public AttackPheromone(int intensity)
    {
        this.intensity = intensity;
        updateImage();
    }

    /**
     * The pheromone decreases the intensity. When the intensity reaches zero, it disappears.
     */
    public void act()
    {
        intensity -= 1;
        if (intensity <= 0) {
            getWorld().removeObject(this);
        }
        else {
            if ((intensity % 4) == 0) {     // every four steps...
                updateImage();
            }
        }
    }

    /**
     * Make the image. The size and transparency are proportional to the intensity.
     */
    private void updateImage()
    {
        int size = intensity / 3 + 5;
        GreenfootImage image = new GreenfootImage(size + 1, size + 1);
        int alpha = intensity / 3;
        image.setColor(new Color(0, 100, 255, alpha));
        image.fillOval(0, 0, size, size);
        image.setColor(Color.DARK_GRAY);
        image.fillRect(size / 2, size / 2, 2, 2);   // small dot in the middle
        setImage(image);
    }   
}
