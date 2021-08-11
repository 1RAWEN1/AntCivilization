import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

public class Pheromone extends Actor
{
    private int maxIntensity = 180;
    private int intensity;
    
    /**
     * Create a new drop of pheromone with full intensity.
     */
    public Pheromone()
    {
        intensity = maxIntensity;
        updateImage();
    }
    
    public Pheromone(int intensity)
    {
        this.intensity = intensity;
        maxIntensity = this.intensity;
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
