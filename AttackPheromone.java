import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class AttackPheromone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AttackPheromone extends Actor
{

    private int time;
    private final SimpleTimer timer = new SimpleTimer();
    private int maxIntensity = 320;
    private int intensity;

    public AttackPheromone()
    {
        time = 500;
        updateImage();
    }
    
    public AttackPheromone(int intensity, int time)
    {
        this.time = time;
        maxIntensity = intensity;
        updateImage();
    }

    public void act()
    {
        timer.calculate();
        //intensity -= 1;
        intensity = (int)(((double)(time - timer.getTime()) / time) * maxIntensity);
        if (timer.getTime() > time) {
            getWorld().removeObject(this);
        }
        else {
            if ((intensity % 4) == 0) {     // every four steps...
                updateImage();
            }
        }
    }

    private void updateImage()
    {
        int size = intensity / 3 + 5;
        GreenfootImage image = new GreenfootImage(size + 1, size + 1);
        int alpha = intensity / 6;
        image.setColor(new Color(0, 100, 255, alpha));
        image.fillOval(0, 0, size, size);
        image.setColor(Color.DARK_GRAY);
        image.fillRect(size / 2, size / 2, 2, 2);   // small dot in the middle
        setImage(image);
    }   
}
