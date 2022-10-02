import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

public class Pheromone extends Actor
{
    private int maxIntensity = 180;
    private int intensity;

    private int time;
    private final SimpleTimer timer = new SimpleTimer();
    
    /**
     * Create a new drop of pheromone with full intensity.
     */
    public Pheromone()
    {
        time = 500;
        updateImage();
    }
    
    public Pheromone(int intensity, int time)
    {
        this.time = time;
        maxIntensity = intensity;
        updateImage();
    }

    /**
     * The pheromone decreases the intensity. When the intensity reaches zero, it disappears.
     */
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
    
    public int getIntensity(){
        return intensity;
    }
    
    public int getMaxIntensity(){
        return maxIntensity;
    }

    /**
     * Make the image. The size and transparency are proportional to the intensity.
     */
    private void updateImage()
    {
        int size = intensity / 3 + 5;
        GreenfootImage image = new GreenfootImage(size + 1, size + 1);
        int alpha = intensity / 3;
        image.setColor(new Color(255, 255, 255, alpha));
        image.fillOval(0, 0, size, size);
        image.setColor(Color.DARK_GRAY);
        image.fillRect(size / 2, size / 2, 2, 2);   // small dot in the middle
        setImage(image);
    }
}
